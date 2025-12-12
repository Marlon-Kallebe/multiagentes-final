package artifacts;
 
import cartago.*;
import cartago.tools.GUIArtifact;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class Janela extends GUIArtifact {
 
	private InterfaceJanela frame;
	private JanelaModel janela_model = new JanelaModel(false);
 
    void setup(boolean fechada, boolean trancada) {
    	janela_model.setFechada(fechada);
    	janela_model.setTrancada(trancada);
		defineObsProperty("fechada", janela_model.isFechada());
		defineObsProperty("trancada", janela_model.isTrancada());
		create_frame();
	}
 
    public void setup() {
    	janela_model.setFechada(true);
    	janela_model.setTrancada(false);
		defineObsProperty("fechada", janela_model.isFechada());
		defineObsProperty("trancada", janela_model.isTrancada());
		create_frame();
	}
 
    void create_frame() {
    	frame = new InterfaceJanela();
		linkActionEventToOp(frame.abrirFecharButton,"abrir_fechar");
		linkActionEventToOp(frame.trancarButton,"trancar_destrancar");
		linkWindowClosingEventToOp(frame, "closed");
		frame.setVisible(true);
    }
 
	@OPERATION
	void destrancar() {
		janela_model.setTrancada(false);
		getObsProperty("trancada").updateValue(janela_model.isTrancada());
	}
 
	@OPERATION
	void trancar() {
		if(janela_model.isFechada()) {
			janela_model.setTrancada(true);
			getObsProperty("trancada").updateValue(janela_model.isTrancada());
		}
	}
 
	@OPERATION
    void abrir() {
		if(janela_model.isTrancada() == false) {
			janela_model.setFechada(false);
			getObsProperty("fechada").updateValue(janela_model.isFechada());
		}
	}
 
	@OPERATION
	void fechar() {
		janela_model.setFechada(true);
		getObsProperty("fechada").updateValue(janela_model.isFechada());
    }
 
	@INTERNAL_OPERATION 
	void abrir_fechar(ActionEvent ev){
		if(janela_model.isTrancada() == false) {
			if(janela_model.isFechada()) {
				janela_model.setFechada(false);
			}else {
				janela_model.setFechada(true);
			}
			getObsProperty("fechada").updateValue(janela_model.isFechada());
		}
		signal("movimento_janela");
	}
 
	@INTERNAL_OPERATION 
	void trancar_destrancar(ActionEvent ev){
		if(janela_model.isFechada()) {
			if(janela_model.isTrancada()) {
				janela_model.setTrancada(false);
			}else {
				janela_model.setTrancada(true);
			}
			getObsProperty("trancada").updateValue(janela_model.isTrancada());
			signal("movimento_tranca");
		}
	}
 
	class JanelaModel{
		boolean fechada = true;
		boolean trancada = true;
 
		public boolean isTrancada() {
			return trancada;
		}
 
		public void setTrancada(boolean trancada) {
			this.trancada = trancada;
		}
 
		public JanelaModel(boolean fechada, boolean trancada) {
			super();
			this.fechada = fechada;
			this.trancada = trancada;
		}
 
		public JanelaModel(boolean fechada) {
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
 
	class InterfaceJanela extends JFrame {	
 
		private JButton abrirFecharButton;
		private JButton trancarButton;
 
		public InterfaceJanela(){
			setTitle(" Janela ");
		setLocation(750, 300);
			setSize(300,150);
 
			JPanel panel = new JPanel();
			setContentPane(panel);
 
			abrirFecharButton = new JButton(" Abrir | Fechar ");
			abrirFecharButton.setSize(80,50);
 
			trancarButton = new JButton("(Des)Trancar");
			trancarButton.setSize(80,50);
 
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(abrirFecharButton);
			panel.add(trancarButton);
 
		}
	}
}