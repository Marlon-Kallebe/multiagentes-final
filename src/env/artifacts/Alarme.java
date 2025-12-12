package artifacts;
 
import cartago.*;
import cartago.tools.GUIArtifact;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.util.ArrayList;
import java.util.List;
 
public class Alarme extends GUIArtifact {
 
	private InterfaceAlarme frame;
	private AlarmeModel alarme_model = new AlarmeModel(false);
	private List<String> historico = new ArrayList<>();
 
    public void setup() {
		defineObsProperty("ativado", alarme_model.isAtivado());
		defineObsProperty("ultimo_alerta", "Nenhum");
		create_frame();
	}
 
    void create_frame() {
    	frame = new InterfaceAlarme();
		linkActionEventToOp(frame.limparButton,"limpar");
		linkWindowClosingEventToOp(frame, "closed");
		frame.setVisible(true);
    }
 
	@OPERATION
	void ativar_alarme(String motivo) {
		alarme_model.setAtivado(true);
		getObsProperty("ativado").updateValue(true);
		getObsProperty("ultimo_alerta").updateValue(motivo);
 
		// Tocar som de alarme (beep multiplo)
		new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				Toolkit.getDefaultToolkit().beep();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
 
		String mensagem = "*** ALARME ATIVADO! *** Motivo: " + motivo;
		historico.add(mensagem);
		frame.adicionarAlerta(mensagem);
		
	}
 
	@OPERATION
	void enviar_notificacao(String intruso) {
		String notificacao = "NOTIFICACAO ENVIADA: Intruso detectado - " + intruso;
		historico.add(notificacao);
		frame.adicionarAlerta(notificacao);
		
		
		
	}
 
	@OPERATION
	void desativar_alarme() {
		alarme_model.setAtivado(false);
		getObsProperty("ativado").updateValue(false);
		String mensagem = "Alarme desativado";
		historico.add(mensagem);
		frame.adicionarAlerta(mensagem);
		
	}
 
	@INTERNAL_OPERATION 
	void limpar(ActionEvent ev){
		historico.clear();
		frame.limparHistorico();
		
	}
 
	@INTERNAL_OPERATION 
	void closed(ActionEvent ev){
		signal("closed");
	}
 
	class AlarmeModel {
		private boolean ativado = false;
 
		public AlarmeModel(boolean ativado) {
			this.ativado = ativado;
		}
 
		public boolean isAtivado() {
			return ativado;
		}
 
		public void setAtivado(boolean ativado) {
			this.ativado = ativado;
		}
	}
 
	class InterfaceAlarme extends JFrame {
 
		private JButton limparButton;
		private JTextArea historicoArea;
		private JLabel statusLabel;
 
		public InterfaceAlarme(){
			setTitle(" Alarme ");
		setLocation(50, 550);
			setSize(500,400);
 
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			setContentPane(panel);
 
			statusLabel = new JLabel("Status: Desativado");
			panel.add(statusLabel);
 
			JLabel tituloLabel = new JLabel("Historico de Alertas:");
			panel.add(tituloLabel);
 
			historicoArea = new JTextArea(10, 30);
			historicoArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(historicoArea);
			panel.add(scrollPane);
 
			limparButton = new JButton("Limpar Historico");
			limparButton.setSize(80,50);
			panel.add(limparButton);
		}
 
		public void adicionarAlerta(String alerta) {
			historicoArea.append(alerta + "\n");
			if (alerta.contains("ATIVADO")) {
				statusLabel.setText("Status: ATIVADO - ALERTA!");
			}
		}
 
		public void limparHistorico() {
			historicoArea.setText("");
			statusLabel.setText("Status: Desativado");
		}
	}
}