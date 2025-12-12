package artifacts;

import cartago.*;
import cartago.tools.GUIArtifact;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ArCondicionado extends GUIArtifact {

	private InterfaceArCondicionado frame;
	private ArCondicionadoModel ar_model = new ArCondicionadoModel(false, 24);
	
	
    void setup(boolean ligado, int temperatura) {
    	ar_model.setLigado(ligado);
    	ar_model.setTemperatura(temperatura);
		defineObsProperty("ligado", ar_model.isLigado());
		defineObsProperty("temperatura", ar_model.getTemperatura());
		create_frame();
	}
    
    public void setup() {
    	ar_model.setLigado(false);
    	ar_model.setTemperatura(24);
		defineObsProperty("ligado", ar_model.isLigado());
		defineObsProperty("temperatura", ar_model.getTemperatura());
		create_frame();
	}
    
    void create_frame() {
    	frame = new InterfaceArCondicionado();
		linkActionEventToOp(frame.ligarButton,"ligar_desligar");
		linkActionEventToOp(frame.ajustarButton,"ajustar_temp");
		linkWindowClosingEventToOp(frame, "closed");
		frame.setVisible(true);
    }

	@OPERATION
	void ligar() {
		ar_model.setLigado(true);
		getObsProperty("ligado").updateValue(ar_model.isLigado());
	}

	@OPERATION
	void desligar() {
		ar_model.setLigado(false);
		getObsProperty("ligado").updateValue(ar_model.isLigado());
	}

	@OPERATION
    void ajustar_temperatura(int temp) {
		ar_model.setTemperatura(temp);
		getObsProperty("temperatura").updateValue(ar_model.getTemperatura());
	}
	
	@INTERNAL_OPERATION 
	void ligar_desligar(ActionEvent ev){
		if(ar_model.isLigado()) {
			ar_model.setLigado(false);
		}else {
			ar_model.setLigado(true);
		}
		getObsProperty("ligado").updateValue(ar_model.isLigado());
		signal("mudanca_estado");
	}
	
	@INTERNAL_OPERATION 
	void ajustar_temp(ActionEvent ev){
		int temp = frame.getTemperatura();
		ar_model.setTemperatura(temp);
		getObsProperty("temperatura").updateValue(ar_model.getTemperatura());
		signal("ajuste_temperatura");
	}

	
	
	class ArCondicionadoModel{
		boolean ligado = false;
		int temperatura = 24;

		public boolean isLigado() {
			return ligado;
		}

		public void setLigado(boolean ligado) {
			this.ligado = ligado;
		}

		public int getTemperatura() {
			return temperatura;
		}

		public void setTemperatura(int temperatura) {
			this.temperatura = temperatura;
		}

		public ArCondicionadoModel(boolean ligado, int temperatura) {
			super();
			this.ligado = ligado;
			this.temperatura = temperatura;
		}
	}
		
	class InterfaceArCondicionado extends JFrame {	
			
		private JButton ligarButton;
		private JButton ajustarButton;
		private JTextField tempField;
			
		public InterfaceArCondicionado(){
			setTitle(" Ar Condicionado ");
		setLocation(400, 50);
			setSize(350,200);
						
			JPanel panel = new JPanel();
			setContentPane(panel);
				
			ligarButton = new JButton(" Ligar/Desligar ");
			ligarButton.setSize(100,50);
			
			JLabel tempLabel = new JLabel("Temperatura:");
			tempField = new JTextField("24", 5);
			
			ajustarButton = new JButton("Ajustar");
			ajustarButton.setSize(80,50);

			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(ligarButton);
			panel.add(tempLabel);
			panel.add(tempField);
			panel.add(ajustarButton);
				
		}
		
		public int getTemperatura(){
			try {
				return Integer.parseInt(tempField.getText());
			} catch (NumberFormatException e) {
				return 24;
			}
		}
	}

		
}


