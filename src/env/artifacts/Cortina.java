// CArtAgO artifact code for project aula10

package artifacts;

import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cartago.*;
import cartago.tools.GUIArtifact;

public class Cortina extends GUIArtifact {
	
	private InterfaceCortina frame;
	private int avanco = 10; // define o avanco para fechamento e abertura
	private CortinaModel cortina_model = new CortinaModel(0);
	
    void setup(int nivel) {
    	cortina_model.setNivel_abertura(nivel);
		defineObsProperty("nivel_abertura", cortina_model.getNivel_abertura());
		create_frame();
	}
    
    public void setup() {
		defineObsProperty("nivel_abertura", cortina_model.getNivel_abertura());
		create_frame();
	}
	
    void create_frame() {
    	frame = new InterfaceCortina();
		linkActionEventToOp(frame.okButton,"ok");
		frame.setVisible(true);
    }
    

	@OPERATION
	void aumentar_nivel() {
		cortina_model.setNivel_abertura(cortina_model.getNivel_abertura()+avanco); // abre ou fecha 10% por vez
		getObsProperty("nivel_abertura").updateValue(cortina_model.getNivel_abertura());
	}
	
	@OPERATION
	void diminuir_nivel() {
		cortina_model.setNivel_abertura(cortina_model.getNivel_abertura()-avanco); // abre ou fecha 10% por vez
		getObsProperty("nivel_abertura").updateValue(cortina_model.getNivel_abertura());
	}

	@OPERATION
	void fechar() {
		cortina_model.setNivel_abertura(0); 
		getObsProperty("nivel_abertura").updateValue(cortina_model.getNivel_abertura());
	}
	
	@OPERATION
	void abrir()  {
		cortina_model.setNivel_abertura(100); 
		getObsProperty("nivel_abertura").updateValue(cortina_model.getNivel_abertura());
	}
	
	@INTERNAL_OPERATION 
	void ok(ActionEvent ev){
		cortina_model.setNivel_abertura(frame.getnivel());
		getObsProperty("nivel_abertura").updateValue(cortina_model.getNivel_abertura());
		signal("ajuste_cortina");
	}

	
class CortinaModel{
	
	// 0 = fechada, 50 = meio aberta, 100 = totalmente aberta (possivel usar mais valores)
	int nivel_abertura = 0;

	public CortinaModel(int nivel_abertura) {
		this.nivel_abertura = nivel_abertura;
	}

	public int getNivel_abertura() {
		return nivel_abertura;
	}

	public void setNivel_abertura(int nivel_abertura) {
		this.nivel_abertura = nivel_abertura;
	}
	
}

class InterfaceCortina extends JFrame {	
	
	private JButton okButton;
	private JTextField nivel;
	
	public InterfaceCortina(){
		setTitle(" Cortina ");
		setLocation(400, 300);
		setSize(300,400);
					
		JPanel panel = new JPanel();
		JLabel nivelL = new JLabel();
		nivelL.setText("Nivel de abertura:    ");
		setContentPane(panel);
		
		okButton = new JButton("ok");
		okButton.setSize(80,50);
		
		nivel = new JTextField(10);
		nivel.setText("0");
		nivel.setEditable(true);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(nivelL);
		panel.add(nivel);
		panel.add(okButton);
		
	}
	
	public int getnivel(){
		return Integer.parseInt(nivel.getText());
	}
}
	
}


