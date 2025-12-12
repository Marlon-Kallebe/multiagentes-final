package artifacts;

import cartago.*;
import cartago.tools.GUIArtifact;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Fechadura extends GUIArtifact {

	private InterfaceFechadura frame;
	private FechaduraModel fechadura_model = new FechaduraModel(false);
	
	
    void setup(boolean fechada, boolean trancada) {
    	fechadura_model.setFechada(fechada);
    	fechadura_model.setTrancada(trancada);
		defineObsProperty("fechada", fechadura_model.isFechada());
		defineObsProperty("trancada", fechadura_model.isTrancada());
		create_frame();
	}
    
    public void setup() {
    	fechadura_model.setFechada(true);
    	fechadura_model.setTrancada(false);
		defineObsProperty("fechada", fechadura_model.isFechada());
		defineObsProperty("trancada", fechadura_model.isTrancada());
		create_frame();
	}
    
    void create_frame() {
    	frame = new InterfaceFechadura();
		linkActionEventToOp(frame.macanetaButton,"macaneta");
		linkActionEventToOp(frame.fechaduraButton,"fechadura");
		linkWindowClosingEventToOp(frame, "closed");
		frame.setVisible(true);
    }

	@OPERATION
	void destrancar() {
		fechadura_model.setTrancada(false);
		getObsProperty("trancada").updateValue(fechadura_model.isTrancada());
	}

	@OPERATION
	void trancar() {
		if(fechadura_model.isFechada()) {
			fechadura_model.setTrancada(true);
			getObsProperty("trancada").updateValue(fechadura_model.isTrancada());
		}
	}

	@OPERATION
    void abrir() {
		if(fechadura_model.isTrancada() == false) {
			fechadura_model.setFechada(false);
			getObsProperty("fechada").updateValue(fechadura_model.isFechada());
		}
	}

	@OPERATION
	void fechar() {
		fechadura_model.setFechada(true);
		getObsProperty("fechada").updateValue(fechadura_model.isFechada());
    }
	
	@INTERNAL_OPERATION 
	void macaneta(ActionEvent ev){
		if(fechadura_model.isTrancada() == false) {
			if(fechadura_model.isFechada()) {
				fechadura_model.setFechada(false);;
			}else {
				fechadura_model.setFechada(true);;
			}
			getObsProperty("fechada").updateValue(fechadura_model.isFechada());
		}
		signal("movimento_macaneta");
	}
	
	@INTERNAL_OPERATION 
	void fechadura(ActionEvent ev){
		if(fechadura_model.isFechada()) {
			if(fechadura_model.isTrancada()) {
				fechadura_model.setTrancada(false);;
			}else {
				fechadura_model.setTrancada(true);;
			}
			getObsProperty("trancada").updateValue(fechadura_model.isTrancada());
			signal("movimento_fechadura");
		}
	}

	
	
	class FechaduraModel{
		boolean fechada = true;
		boolean trancada = true;

		public boolean isTrancada() {
			return trancada;
		}

		public void setTrancada(boolean trancada) {
			this.trancada = trancada;
		}

		public FechaduraModel(boolean fechada, boolean trancada) {
			super();
			this.fechada = fechada;
			this.trancada = trancada;
		}

		public FechaduraModel(boolean fechada) {
			super();
			this.fechada = fechada;
		}

		public boolean isFechada() {
			return fechada;
		}

		public void setFechada(boolean fechada) {
			this.fechada = fechada;
		}
	}
		
	class InterfaceFechadura extends JFrame {	
			
		private JButton macanetaButton;
		private JButton fechaduraButton;
			
		public InterfaceFechadura(){
			setTitle(" Fechadura ");
		setLocation(750, 50);
			setSize(300,150);
							
			JPanel panel = new JPanel();
			setContentPane(panel);
				
			macanetaButton = new JButton(" Abrir | Fechar ");
			macanetaButton.setSize(80,50);
			
			fechaduraButton = new JButton("(Des)Trancar");
			fechaduraButton.setSize(80,50);

			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(macanetaButton);
			panel.add(fechaduraButton);
				
		}
	}

		
}


