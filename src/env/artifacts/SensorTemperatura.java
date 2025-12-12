package artifacts;

import cartago.*;
import java.util.Random;

public class SensorTemperatura extends Artifact {
	
	private Random random = new Random();
	private int temperaturaAtual = 22; // Temperatura inicial em Celsius
	private boolean ativo = true;
	
	public void setup() {
		defineObsProperty("temperatura_ambiente", temperaturaAtual);
		defineObsProperty("ativo", ativo);
		
		
		// Inicia monitoramento periodico
		new Thread(new TemperatureMonitor()).start();
	}
	
	@OPERATION
	void medir_temperatura() {
		// Simula variacao natural de temperatura
		if (random.nextDouble() < 0.6) {
			if (temperaturaAtual > 15) {
				temperaturaAtual -= random.nextInt(2) + 1;
			}
		} else {
			if (temperaturaAtual < 35) {
				temperaturaAtual += random.nextInt(2) + 1;
			}
		}
		
		// Garantir que a propriedade observavel exista antes de atualizar (evita NPE)
		if (getObsProperty("temperatura_ambiente") == null) {
			defineObsProperty("temperatura_ambiente", temperaturaAtual);
		} else {
			getObsProperty("temperatura_ambiente").updateValue(temperaturaAtual);
		}
		signal("medicao_temperatura", temperaturaAtual);
	}
	
	@OPERATION
	void ajustar_temperatura_simulada(int novaTemp) {
		if (novaTemp >= 15 && novaTemp <= 35) {
			temperaturaAtual = novaTemp;
			getObsProperty("temperatura_ambiente").updateValue(temperaturaAtual);
			
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
	private class TemperatureMonitor implements Runnable {
		@Override
		public void run() {
			while (ativo) {
				try {
					Thread.sleep(5000); // Mede a cada 5 segundos
					execInternalOp("medir_temperatura");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
