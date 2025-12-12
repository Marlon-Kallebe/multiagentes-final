
!inicializar_cortina.

+!inicializar_cortina
  <- 	makeArtifact("cortina_quarto","artifacts.Cortina",[],D);
  	   	focus(D);
  	   	!abrir_cortina.
  	   	
+ajuste_cortina 
  <-  !!verificar_ajuste.
      
+closed  <-  .print("Close event from GUIInterface").
   
 +!verificar_ajuste: nivel_abertura(N) 
 	<-  .print("Nivel de abertura da cortina: ", N).
 	
 +!abrir_cortina
 	<-  abrir;
 		.print("Cortina aberta!").

////////////////////////////////////////////////////
// CENARIOS
////////////////////////////////////////////////////

// CENARIO PROPRIETARIO CHEGANDO
+!abrir_cortina_total
<- .print("[CORTINA] Recebido comando: abrir cortina");
   abrir;
   .print("[CORTINA] >>> Cortina ABERTA (100%) para o proprietario").

// CENARIO PROPRIETARIO SAINDO
+!fechar_cortina_total
<- .print("[CORTINA] Recebido comando: fechar cortina");
   fechar;
   .print("[CORTINA] >>> Cortina FECHADA (0%) - proprietario saiu").

// CENARIO INTRUSO - Movimenta cortina aleatoriamente (TEMPOS REDUZIDOS)
+!modo_intruso_cortina
<- .print("[CORTINA] *** MODO DEFESA ATIVADO!");
   .print("[CORTINA] Iniciando movimento aleatorio...");
   !movimentar_cortina_intruso.

// Movimenta a cortina em padrao irregular (TEMPOS REDUZIDOS PELA METADE)
+!movimentar_cortina_intruso: intruso_na_casa(X)
<- .print("[CORTINA] FECHANDO");
   fechar;
   .wait(1000);  // era 2000
   .print("[CORTINA] ABRINDO");
   abrir;
   .wait(750);  // era 1500
   .print("[CORTINA] FECHANDO");
   fechar;
   .wait(1500);  // era 3000
   .print("[CORTINA] ABRINDO");
   abrir;
   .wait(500);  // era 1000
   .print("[CORTINA] FECHANDO");
   fechar;
   .wait(1250);  // era 2500
   !movimentar_cortina_intruso.

+!movimentar_cortina_intruso
<- .print("[CORTINA] Intruso saiu, parando movimento de cortina.").

// Handler para parar rotina de intruso
+parar_intruso
 <- .abolish(intruso_na_casa(_));
   .print("[CORTINA] Parando modo intruso via parar_intruso event.").

// Awareness de estado do sistema
+proprietario_em_casa
	<- .print("Cortina: Proprietario esta em casa.").

+casa_vazia
	<- .print("Cortina: Casa esta vazia.").

+intruso_na_casa(X)
	<- .print("Cortina: ALERTA! Intruso detectado: ", X).
