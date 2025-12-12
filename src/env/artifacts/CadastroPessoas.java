package artifacts;
 
import cartago.*;
import cartago.tools.GUIArtifact;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.awt.*;
 
public class CadastroPessoas extends GUIArtifact {
 
	private InterfaceCadastro frame;
	private Set<String> pessoasConhecidas = new HashSet<>();
	private String ultimaPessoaDesconhecida = "";
	private CadastroPopup cadastroPopup = null;
 
    public void setup() {
		// Adiciona proprietario padrao
		pessoasConhecidas.add("Jonas");
		pessoasConhecidas.add("jonas");
 
		defineObsProperty("total_cadastrados", pessoasConhecidas.size());
		defineObsProperty("aguardando_cadastro", false);
		defineObsProperty("pessoa_pendente", "");
 
		create_frame();
		frame.atualizarLista(pessoasConhecidas);
	}
 
    void create_frame() {
    	frame = new InterfaceCadastro();
		linkActionEventToOp(frame.cadastrarButton,"cadastrar_pessoa");
		linkActionEventToOp(frame.rejeitarButton,"rejeitar_pessoa");
		linkWindowClosingEventToOp(frame, "closed");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		new javax.swing.Timer(300, ev -> frame.setAlwaysOnTop(false)).start();
    }
 
	@OPERATION
	void verificar_pessoa(String nome) {
		if (pessoasConhecidas.contains(nome)) {
			signal("pessoa_conhecida", nome);
		} else {
			ultimaPessoaDesconhecida = nome;
			getObsProperty("aguardando_cadastro").updateValue(true);
			getObsProperty("pessoa_pendente").updateValue(nome);
			frame.setPessoaPendente(nome);
			// Abre popup de cadastro automatico
			// NAO envia sinal ainda - aguarda decisao do usuario
			mostrar_popup_cadastro(nome);
			signal("aguardando_decisao_cadastro", nome);
		}
	}
 
	@OPERATION
	void mostrar_popup_cadastro(String nome) {
		if (cadastroPopup != null && cadastroPopup.isVisible()) {
			cadastroPopup.dispose();
		}
		cadastroPopup = new CadastroPopup(frame, nome, new CadastroCallback() {
			@Override
			public void onConfirmar(String nomeCadastro) {
				ultimaPessoaDesconhecida = nomeCadastro;
				execInternalOp("confirmarCadastroInternal");
			}
			
			@Override
			public void onRejeitar() {
				ultimaPessoaDesconhecida = nome;
				execInternalOp("rejeitarCadastroInternal");
			}
		});
		cadastroPopup.setVisible(true);
	}
 
	@OPERATION
	void adicionar_pessoa(String nome) {
		pessoasConhecidas.add(nome);
		pessoasConhecidas.add(nome.toLowerCase());
		getObsProperty("total_cadastrados").updateValue(pessoasConhecidas.size());
		getObsProperty("aguardando_cadastro").updateValue(false);
		getObsProperty("pessoa_pendente").updateValue("");
		frame.atualizarLista(pessoasConhecidas);
		frame.limparPendente();
		System.out.println("[CADASTRO] [OK] Pessoa adicionada com sucesso: " + nome);
	}
 
	@OPERATION
	void remover_pessoa(String nome) {
		pessoasConhecidas.remove(nome);
		pessoasConhecidas.remove(nome.toLowerCase());
		getObsProperty("total_cadastrados").updateValue(pessoasConhecidas.size());
		frame.atualizarLista(pessoasConhecidas);
		System.out.println("[CADASTRO] Pessoa removida: " + nome);
	}
 
	@INTERNAL_OPERATION 
	void cadastrar_pessoa(ActionEvent ev){
		if (!ultimaPessoaDesconhecida.isEmpty()) {
			adicionar_pessoa(ultimaPessoaDesconhecida);
			signal("pessoa_cadastrada", ultimaPessoaDesconhecida);
			ultimaPessoaDesconhecida = "";
		}
	}

	@INTERNAL_OPERATION
	void confirmarCadastroInternal() {
		if (!ultimaPessoaDesconhecida.isEmpty()) {
			adicionar_pessoa(ultimaPessoaDesconhecida);
			signal("pessoa_cadastrada", ultimaPessoaDesconhecida);
			ultimaPessoaDesconhecida = "";
		}
	}

	@INTERNAL_OPERATION
	void rejeitarCadastroInternal() {
		if (!ultimaPessoaDesconhecida.isEmpty()) {
			String nomeRejeitado = ultimaPessoaDesconhecida;
			System.out.println("[CADASTRO] Pessoa rejeitada pelo usuario: " + nomeRejeitado);
			signal("pessoa_rejeitada", nomeRejeitado);
			// Agora sim envia sinal de pessoa desconhecida para ativar modo defesa
			signal("pessoa_desconhecida", nomeRejeitado);
			getObsProperty("aguardando_cadastro").updateValue(false);
			getObsProperty("pessoa_pendente").updateValue("");
			frame.limparPendente();
			ultimaPessoaDesconhecida = "";
		}
	}
 
	@INTERNAL_OPERATION 
	void rejeitar_pessoa(ActionEvent ev){
		if (!ultimaPessoaDesconhecida.isEmpty()) {
			System.out.println("[CADASTRO] Pessoa rejeitada: " + ultimaPessoaDesconhecida);
			signal("pessoa_rejeitada", ultimaPessoaDesconhecida);
			getObsProperty("aguardando_cadastro").updateValue(false);
			getObsProperty("pessoa_pendente").updateValue("");
			frame.limparPendente();
			ultimaPessoaDesconhecida = "";
		}
	}
 
	@INTERNAL_OPERATION 
	void closed(ActionEvent ev){
		signal("closed");
	}
 
	// Interface para callback do popup
	interface CadastroCallback {
		void onConfirmar(String nome);
		void onRejeitar();
	}
 
	// Popup para cadastro rapido de pessoa confiavel
	class CadastroPopup extends JDialog {
		private JTextField nomeTextField;
		private JButton confirmarButton;
		private JButton rejeitarButton;
		private CadastroCallback callback;
 
		public CadastroPopup(JFrame parent, String pessoaDesconhecida, CadastroCallback callback) {
			super(parent, "Nova Pessoa Confiavel", true);
			this.callback = callback;
 
			setSize(400, 250);
			setLocationRelativeTo(parent);
			setResizable(false);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
 
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
			// Titulo com alerta
			JLabel tituloLabel = new JLabel("CADASTRAR PESSOA CONFIAVEL");
			tituloLabel.setFont(new Font("Arial", Font.BOLD, 14));
			tituloLabel.setForeground(new Color(0, 100, 0));
			panel.add(tituloLabel);
 
			panel.add(Box.createVerticalStrut(10));
 
			// Mensagem informativa
			    JLabel mensagem = new JLabel("<html>Uma pessoa desconhecida foi detectada.<br>" +
				    "Se e um visitante confiavel, digite o nome e confirme.</html>");
			mensagem.setFont(new Font("Arial", Font.PLAIN, 12));
			panel.add(mensagem);
 
			panel.add(Box.createVerticalStrut(15));
 
			// Label da pessoa desconhecida
			JLabel pessoaLabel = new JLabel("Pessoa detectada: " + pessoaDesconhecida);
			pessoaLabel.setFont(new Font("Arial", Font.ITALIC, 11));
			pessoaLabel.setForeground(new Color(150, 0, 0));
			panel.add(pessoaLabel);
 
			panel.add(Box.createVerticalStrut(10));
 
			// Campo para digitar nome
			JLabel inputLabel = new JLabel("Nome da pessoa confiavel:");
			panel.add(inputLabel);
 
			nomeTextField = new JTextField(pessoaDesconhecida);
			nomeTextField.setFont(new Font("Arial", Font.PLAIN, 14));
			nomeTextField.setMaximumSize(new Dimension(300, 35));
			nomeTextField.selectAll();
			panel.add(nomeTextField);
 
			panel.add(Box.createVerticalStrut(20));
 
			// Botoes
			JPanel botaoPanel = new JPanel();
			botaoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
 
			confirmarButton = new JButton("Cadastrar");
			confirmarButton.setBackground(new Color(0, 150, 0));
			confirmarButton.setForeground(Color.WHITE);
			confirmarButton.setFont(new Font("Arial", Font.BOLD, 12));
			confirmarButton.addActionListener(e -> onConfirmar());
 
			rejeitarButton = new JButton("Rejeitar");
			rejeitarButton.setBackground(new Color(200, 0, 0));
			rejeitarButton.setForeground(Color.WHITE);
			rejeitarButton.setFont(new Font("Arial", Font.BOLD, 12));
			rejeitarButton.addActionListener(e -> onRejeitar());
 
			botaoPanel.add(confirmarButton);
			botaoPanel.add(rejeitarButton);
 
			panel.add(botaoPanel);
 
			setContentPane(panel);
		}
 
		private void onConfirmar() {
			String nome = nomeTextField.getText().trim();
			if (!nome.isEmpty()) {
				callback.onConfirmar(nome);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Por favor, digite um nome!", "Nome vazio", JOptionPane.WARNING_MESSAGE);
			}
		}
 
		private void onRejeitar() {
			callback.onRejeitar();
			dispose();
		}
	}
 
	class InterfaceCadastro extends JFrame {
 
		private JButton cadastrarButton;
		private JButton rejeitarButton;
		private JLabel pendenteLabel;
		private JTextArea listaArea;
 
		public InterfaceCadastro(){
			setTitle(" Cadastro de Pessoas - Smart Home");
			setSize(400, 500);
			setLocationRelativeTo(null);
 
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			setContentPane(panel);
 
			JLabel tituloLabel = new JLabel("Pessoas Cadastradas no Sistema");
			tituloLabel.setFont(new Font("Arial", Font.BOLD, 14));
			panel.add(tituloLabel);
 
			panel.add(Box.createVerticalStrut(10));
 
			listaArea = new JTextArea(12, 25);
			listaArea.setEditable(false);
			listaArea.setFont(new Font("Courier", Font.PLAIN, 11));
			JScrollPane scrollPane = new JScrollPane(listaArea);
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			panel.add(scrollPane);
 
			panel.add(Box.createVerticalStrut(10));
 
			JSeparator sep = new JSeparator();
			panel.add(sep);
 
			panel.add(Box.createVerticalStrut(10));
 
			pendenteLabel = new JLabel("Status: Nenhuma pessoa pendente");
			pendenteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
			panel.add(pendenteLabel);
 
			panel.add(Box.createVerticalStrut(10));
 
			JPanel botaoPanel = new JPanel();
			botaoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
 
			cadastrarButton = new JButton("Cadastrar");
			cadastrarButton.setEnabled(false);
			cadastrarButton.setBackground(new Color(0, 150, 0));
			cadastrarButton.setForeground(Color.WHITE);
 
			rejeitarButton = new JButton("Rejeitar (Intruso)");
			rejeitarButton.setEnabled(false);
			rejeitarButton.setBackground(new Color(200, 0, 0));
			rejeitarButton.setForeground(Color.WHITE);
 
			botaoPanel.add(cadastrarButton);
			botaoPanel.add(rejeitarButton);
			panel.add(botaoPanel);
		}
 
		public void atualizarLista(Set<String> pessoas) {
			listaArea.setText("");
			listaArea.append("================================\n");
			for (String pessoa : pessoas) {
				if (!pessoa.equals(pessoa.toLowerCase())) {
					listaArea.append("[OK] " + pessoa + "\n");
				}
			}
			listaArea.append("================================\n");
			listaArea.append("Total: " + (pessoas.size() / 2) + " pessoas\n");
		}
 
		public void setPessoaPendente(String nome) {
			pendenteLabel.setText("Status: Aguardando acao - " + nome + " (detectado)");
			pendenteLabel.setForeground(new Color(200, 0, 0));
			cadastrarButton.setEnabled(true);
			rejeitarButton.setEnabled(true);
		}
 
		public void limparPendente() {
			pendenteLabel.setText("Status: Nenhuma pessoa pendente");
			pendenteLabel.setForeground(Color.BLACK);
			cadastrarButton.setEnabled(false);
			rejeitarButton.setEnabled(false);
		}
	}
}
