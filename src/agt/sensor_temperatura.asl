// Agent sensor_temperatura
// Monitora a temperatura da casa continuamente

!inicializar_sensor.

+!inicializar_sensor
  <- makeArtifact("sensor_temp","artifacts.SensorTemperatura",[],D);
     focus(D);
     .print("[SENSOR TEMPERATURA] Inicializado com sucesso");
     !!monitorar_temperatura.

// Monitora temperatura continuamente
+!monitorar_temperatura
    <- medir_temperatura;
      .wait(9000);  // Aguarda a proxima medicao do sensor
     !!monitorar_temperatura.

// Evento disparado pelo sensor a cada medicao
 +medicao_temperatura(T)
  <- .print("[SENSOR TEMPERATURA] Temperatura atual da casa: ", T, " C");
     .abolish(temperatura_ambiente(_));
     +temperatura_ambiente(T);
     !!verificar_temperatura(T).


// Verifica se ha anomalias na temperatura
 +!verificar_temperatura(T): T > 30
  <- .print("[SENSOR TEMPERATURA] *** ALERTA: Temperatura muito alta! ", T, " C");
    .broadcast(tell, alerta_temperatura_alta(T)).

 +!verificar_temperatura(T): T < 15
  <- .print("[SENSOR TEMPERATURA] *** ALERTA: Temperatura muito baixa! ", T, " C");
    .broadcast(tell, alerta_temperatura_baixa(T)).

+!verificar_temperatura(T)
  <- true.  // Temperatura normal, nada a fazer

+closed
  <- .print("[SENSOR TEMPERATURA] Sensor finalizado").
