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
		System.out.println("[SensorTemperatura] Sensor inicializado com temperatura: " + temperaturaAtual + " C");
		
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
		System.out.println("[SensorTemperatura] Temperatura atual: " + temperaturaAtual + " C");
		signal("medicao_temperatura", temperaturaAtual);
	}
	
	@OPERATION
	void ajustar_temperatura_simulada(int novaTemp) {
		if (novaTemp >= 15 && novaTemp <= 35) {
			temperaturaAtual = novaTemp;
			getObsProperty("temperatura_ambiente").updateValue(temperaturaAtual);
			System.out.println("[SensorTemperatura] Temperatura ajustada para: " + temperaturaAtual + " C");
		}
	}
	
	@OPERATION
	void ativar_sensor() {
		ativo = true;
		getObsProperty("ativo").updateValue(true);
		System.out.println("[SensorTemperatura] Sensor ativado");
	}
	
	@OPERATION
	void desativar_sensor() {
		ativo = false;
		getObsProperty("ativo").updateValue(false);
		System.out.println("[SensorTemperatura] Sensor desativado");
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
