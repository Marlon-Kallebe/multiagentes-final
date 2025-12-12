// CArtAgO artifact code for project aula10

package artifacts;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cartago.*;
import cartago.tools.GUIArtifact;

public class Camera extends GUIArtifact {
	
	private InterfaceAC frame;
	private CameraLocal camera_model = new CameraLocal(true,"frente", "ninguem");
    
    public void setup(String local, String pessoa) {
    	camera_model.setLocal(local);
    	camera_model.setPessoa(pessoa);
		defineObsProperty("ligada", camera_model.isOn());
        defineObsProperty("local", camera_model.getLocal());	
        defineObsProperty("pessoa_presente", camera_model.getPessoa());	
        create_frame();
	}
    
    public void setup() {
		defineObsProperty("ligada", camera_model.isOn());
        defineObsProperty("local", camera_model.getLocal());	
        defineObsProperty("pessoa_presente", camera_model.getPessoa());	
        create_frame();
	}
    
    void create_frame() {
    	frame = new InterfaceAC();
		linkActionEventToOp(frame.okButton,"ok");
		linkWindowClosingEventToOp(frame, "closed");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		// garante visibilidade e foco da janela
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		// remove alwaysOnTop apos breve intervalo
		new javax.swing.Timer(400, ev -> frame.setAlwaysOnTop(false)).start();
    }
    

	@OPERATION
	void ligar() {
		camera_model.setOn(true);
		getObsProperty("ligada").updateValue(camera_model.isOn());
	}

	@OPERATION
	void desligar() {
		camera_model.setOn(false);
		getObsProperty("ligada").updateValue(camera_model.isOn());
	}
	
	@INTERNAL_OPERATION 
	void ok(ActionEvent ev){
		camera_model.setLocal(frame.getLocal());
		camera_model.setPessoa(frame.getPessoa());
		getObsProperty("local").updateValue(camera_model.getLocal());
		getObsProperty("pessoa_presente").updateValue(camera_model.getPessoa());
		String pessoa = camera_model.getPessoa();
		String local  = camera_model.getLocal();

		
		

		// Lista de pessoas conhecidas (nao sao intrusos)
		String dono = "Jonas";
		String[] conhecidos = { "Roberto", "Maria", "Ana" };

		// Verifica se é comando de saída (formato: "Nome saiu" ou "saiu")
		String[] palavras = pessoa.split(" ");
		boolean isSaida = false;
		String nomeSaida = "";
		
		if (palavras.length == 2 && palavras[1].equalsIgnoreCase("saiu")) {
			// Formato: "Jonas saiu", "Milena saiu", etc
			isSaida = true;
			nomeSaida = palavras[0];
		} else if (pessoa.equalsIgnoreCase("sair") || pessoa.equalsIgnoreCase("saiu")) {
			// Formato antigo: "sair" (assume que é o dono)
			isSaida = true;
			nomeSaida = dono;
		}
		
		// PESSOA SAIU
		if (isSaida) {
			
			signal("pessoa_saiu", nomeSaida);
		}
		// DONO CHEGOU
		else if (pessoa.equalsIgnoreCase(dono) && local.equalsIgnoreCase("frente")) {
			
			signal("dono_chegou", pessoa);
		}
		// OUTRO MORADOR CONHECIDO
		else if (isConhecido(pessoa, conhecidos)) {
			
			signal("morador_chegou", pessoa);
		}
		// INTRUSO
		else if (!pessoa.equalsIgnoreCase("ninguem")) {
			
			signal("intruso_detectado", pessoa);
		}

		

		signal("movimento");
	}
	
	@INTERNAL_OPERATION void closed(WindowEvent ev){
		signal("closed");
	}

	private boolean isConhecido(String pessoa, String[] lista) {
		for (String nome : lista) {
			if (pessoa.equalsIgnoreCase(nome)) {
				return true;
			}
		}
		return false;
	}

	class CameraLocal {
		
		private boolean isOn = false;
		private String local = "unknown";
		private String pessoa = "noone";
		
		public CameraLocal(boolean isOn, String local, String p) {
			super();
			this.isOn = isOn;
			this.local = local;
			this.pessoa = p;
		}

		public boolean isOn() {
			return isOn;
		}

		public void setOn(boolean isOn) {
			this.isOn = isOn;
		}

		public String getLocal() {
			return local;
		}

		public void setLocal(String local) {
			this.local = local;
		}

		public String getPessoa() {
			return pessoa;
		}

		public void setPessoa(String pessoa) {
			this.pessoa = pessoa;
		}

	}
	
class InterfaceAC extends JFrame {	
		
		private JButton okButton;
		private JTextField pessoa;
		private JTextField local;
		
		public InterfaceAC(){
			setTitle(" Camera ");
		setLocation(1200, 400);
			setSize(300,400);
					
			JPanel panel = new JPanel();
			JLabel pessoaL = new JLabel();
			pessoaL.setText("Nome da pessoa:    ");
			JLabel localL = new JLabel();
			localL.setText("Local atual: ");
			setContentPane(panel);
			
			okButton = new JButton("ok");
			okButton.setSize(80,50);
			
			pessoa = new JTextField(10);
			pessoa.setText("Jonas");
			pessoa.setEditable(true);
			
			local = new JTextField(10);
			local.setText("frente");
			local.setEditable(true);
			
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(pessoaL);
			panel.add(pessoa);
			panel.add(localL);
			panel.add(local);
			panel.add(okButton);
			
		}
		
		public String getPessoa(){
			return pessoa.getText();
		}
		
		public String getLocal(){
			return local.getText();
		}
	}
	
}

	

    

