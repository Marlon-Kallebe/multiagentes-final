
!inicializar_lampada.

+!inicializar_lampada
  <- 	makeArtifact("lampada_quarto","artifacts.Lampada",[],D);
  	   	focus(D);
  	   	!ligar_lampada.
  	   	
+interuptor 
  <-  !!verificar_lampada.
      
+closed  <-  .print("Close event from GUIInterface").
   
 +!verificar_lampada: ligada(false)  
 	<-  .print("Alguem DESLIGOU a Lampada").
 	
 +!verificar_lampada: ligada(true)  
 	<-  .print("Alguem LIGOU a Lampada").
 	
 +!ligar_lampada
 	<-  ligar;
 		.print("Liguei a Lampada!").

////////////////////////////////////////////////////
// CENARIOS
////////////////////////////////////////////////////

// CENARIO PROPRIETARIO CHEGANDO
+!ligar_lampada_proprietario
	<- .print("[LAMPADA] Recebido comando: ligar luzes");
	   ligar;
   	   .print("[LAMPADA] >>> Luzes ACESAS para receber o proprietario").

// Plano usado por camera/controle para ligar a lampada da sala
 +!ligar_lampada_sala
 	<- .print("[LAMPADA] Plano recebido: ligar_lampada_sala");
 	   ligar;
 	   .print("[LAMPADA] >>> Luzes da sala ligadas (via ligar_lampada_sala)").

// CENARIO PROPRIETARIO SAINDO
+!desligar_tudo
	<- .print("[LAMPADA] Recebido comando: desligar tudo");
	   desligar;
   	   .print("[LAMPADA] >>> Todas as luzes DESLIGADAS - proprietario saiu").
	   
// CONTROLE AUTOMATICO POR SENSOR DE LUZ
+!ligar_automatico
	<- .print("[LAMPADA] Ligando automaticamente (sensor de luz)");
	   ligar.
 
+!desligar_automatico
	<- .print("[LAMPADA] Desligando automaticamente (sensor de luz)");
	   desligar.
 
// CENARIO INTRUSO - Pisca luzes para desorientar (TEMPOS REDUZIDOS)
+!modo_intruso
	<- .print("[LAMPADA] *** MODO DEFESA ATIVADO!");
	   .print("[LAMPADA] Iniciando padrao de luzes irregular...");
   	   !piscar_luzes_intruso.

// Pisca as luzes em padrao irregular (TEMPOS REDUZIDOS PELA METADE)
+!piscar_luzes_intruso: intruso_na_casa(X)
	<- .print("[LAMPADA] OFF");
	   desligar;
	   .wait(500);  // era 1000
	   .print("[LAMPADA] ON");
	   ligar;
	   .wait(250);  // era 500
	   .print("[LAMPADA] OFF");
	   desligar;
	   .wait(750);  // era 1500
	   .print("[LAMPADA] ON");
	   ligar;
	   .wait(400);  // era 800
	   .print("[LAMPADA] OFF");
	   desligar;
	   .wait(1000);  // era 2000
	   .print("[LAMPADA] ON");
	   ligar;
	   .wait(150);  // era 300
	   !piscar_luzes_intruso.

+!piscar_luzes_intruso
	<- .print("[LAMPADA] Intruso saiu, parando padrao de luzes.").

// Interrompe o modo intruso se solicitado
+parar_intruso
 <- .abolish(intruso_na_casa(_));
    .print("[LAMPADA] Parando modo intruso via parar_intruso event.").

// Awareness de estado do sistema
+proprietario_em_casa
	<- .print("Lampada: Proprietario esta em casa.").

+casa_vazia
	<- .print("Lampada: Casa esta vazia.").

+intruso_na_casa(X)
	<- .print("Lampada: ALERTA! Intruso detectado: ", X).
