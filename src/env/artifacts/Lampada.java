package artifacts;

import cartago.*;
import cartago.tools.GUIArtifact;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Lampada extends GUIArtifact{
    
	private InterfaceLampada frame;
	private LampadaModel lampada_model = new LampadaModel(false);
    
    void setup(boolean ligada) {
    	lampada_model.setOn(ligada);
		defineObsProperty("ligada", lampada_model.isOn());
		create_frame();
	}
    
    public void setup() {
    	lampada_model.setOn(false);
		defineObsProperty("ligada", lampada_model.isOn());
		create_frame();
	}
    
    void create_frame() {
    	frame = new InterfaceLampada();
		linkActionEventToOp(frame.okButton,"ok");
		linkWindowClosingEventToOp(frame, "closed");
		frame.setVisible(true);
    }

	@OPERATION
	void ligar() {
		lampada_model.setOn(true);
		getObsProperty("ligada").updateValue(lampada_model.isOn());
	}

	@OPERATION
	void desligar() {
		lampada_model.setOn(false);
		getObsProperty("ligada").updateValue(lampada_model.isOn());
    }
	
	@INTERNAL_OPERATION 
	void ok(ActionEvent ev){
		if (lampada_model.isOn()) {
			lampada_model.setOn(false);
			getObsProperty("ligada").updateValue(lampada_model.isOn());
		}else {
			lampada_model.setOn(true);
			getObsProperty("ligada").updateValue(lampada_model.isOn());
		}
		signal("interuptor");
	}
	
	class LampadaModel {
		
		boolean isOn = false;

		public LampadaModel(boolean isOn) {
			super();
			this.isOn = isOn;
		}

		public boolean isOn() {
			return isOn;
		}

		public void setOn(boolean isOn) {
			this.isOn = isOn;
		}
				
	}
	
	class InterfaceLampada extends JFrame {	
		
		private JButton okButton;
		
		public InterfaceLampada(){
			setTitle(" Lampada ");
		setLocation(50, 300);
			setSize(300,150);
						
			JPanel panel = new JPanel();
			JLabel label = new JLabel();
			label.setText("Ligar/Desligar: ");
			setContentPane(panel);
			
			okButton = new JButton("L/D");
			okButton.setSize(80,50);

			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(label);
			panel.add(okButton);
			
		}
	}


}
