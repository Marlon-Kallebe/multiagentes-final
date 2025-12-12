package artifacts;

import cartago.*;
import java.util.Random;

public class SensorIluminacao extends Artifact {
	
	private Random random = new Random();
	private int nivelIluminacao = 50; // Nivel inicial de iluminacao (0-100)
	private boolean ativo = true;
	
	public void setup() {
		defineObsProperty("nivel_iluminacao", nivelIluminacao);
		defineObsProperty("ativo", ativo);
		
		
		// Inicia monitoramento periodico
		new Thread(new LightMonitor()).start();
	}
	
	@OPERATION
	void medir_iluminacao() {
		// Simula variacao de iluminacao ao longo do tempo (ciclo dia/noite)
		if (random.nextDouble() < 0.5) {
			if (nivelIluminacao > 10) {
				nivelIluminacao -= random.nextInt(5) + 1;
			}
		} else {
			if (nivelIluminacao < 100) {
				nivelIluminacao += random.nextInt(5) + 1;
			}
		}
		
		// Garantir que a propriedade observavel exista antes de atualizar (evita NPE)
		if (getObsProperty("nivel_iluminacao") == null) {
			defineObsProperty("nivel_iluminacao", nivelIluminacao);
		} else {
			getObsProperty("nivel_iluminacao").updateValue(nivelIluminacao);
		}
		
		signal("medicao_iluminacao", nivelIluminacao);
	}
    
	
	@OPERATION
	void ajustar_nivel_simulado(int novoNivel) {
		if (novoNivel >= 0 && novoNivel <= 100) {
			nivelIluminacao = novoNivel;
			getObsProperty("nivel_iluminacao").updateValue(nivelIluminacao);
			
		}
	}
	
	@OPERATION
	void ativar_sensor() {
		ativo = true;
		getObsProperty("ativo").updateValue(true);
		
	}
	
	@OPERATION
	void desativar_sensor() {
		ativo = false;
		getObsProperty("ativo").updateValue(false);
		
	}
	
	// Thread para monitoramento continuo
	private class LightMonitor implements Runnable {
		@Override
		public void run() {
			while (ativo) {
				try {
					Thread.sleep(6000); // Mede a cada 6 segundos
					execInternalOp("medir_iluminacao");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}



