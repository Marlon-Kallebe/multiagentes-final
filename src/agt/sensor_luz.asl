// Agent sensor_luz
// Monitora a luminosidade da casa e controla iluminacao automaticamente

!inicializar_sensor_luz.

+!inicializar_sensor_luz
  <- makeArtifact("sensor_iluminacao","artifacts.SensorIluminacao",[],D);
     focus(D);
     .print("[SENSOR LUZ] Inicializado com sucesso");
     !!monitorar_iluminacao.

// Monitora iluminacao continuamente
 +!monitorar_iluminacao
  <- medir_iluminacao;
    .wait(9000);  // Aguarda a proxima medicao do sensor
    !!monitorar_iluminacao.

// Evento disparado pelo sensor a cada medicao
+medicao_iluminacao(Nivel)
  <- .print("[SENSOR LUZ] Nivel de iluminacao atual: ", Nivel, "%");
     .abolish(nivel_luminosidade(_));
     +nivel_luminosidade(Nivel);
     !!verificar_iluminacao(Nivel).

// Verifica se precisa ligar luzes automaticamente
 +!verificar_iluminacao(Nivel): Nivel < 30 & not intruso_na_casa(_) & not casa_vazia
  <- .print("[SENSOR LUZ] Pouca luz detectada - Ativando iluminacao automatica");
    .send(lampada, achieve, ligar_automatico).

// Verifica se precisa desligar luzes
+!verificar_iluminacao(Nivel): Nivel > 70 & not intruso_na_casa(_)
  <- .print("[SENSOR LUZ] Luz ambiente adequada - Desligando iluminacao automatica");
     .send(lampada, achieve, desligar_automatico).

 +!verificar_iluminacao(Nivel)
  <- true.  // Iluminacao em nivel adequado

+closed
  <- .print("[SENSOR LUZ] Sensor finalizado").
