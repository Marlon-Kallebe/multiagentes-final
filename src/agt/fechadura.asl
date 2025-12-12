
!inicializar_fechadura.

+!inicializar_fechadura
  <- 	makeArtifact("fechadura_quarto","artifacts.Fechadura",[],D);
  	   	focus(D);
  	   	!fechar_porta.
  	   	
+movimento_macaneta <- !verificar_fechada.

+!verificar_fechada: trancada(true) & intruso_na_casa(X)
  <-  .print("ALERTA! Intruso ", X, " tentou abrir a porta, mas esta trancada!").
+!verificar_fechada: trancada(true) 
  <-  .print("Alguem mexeu na MACANETA, porem a porta esta trancada!").
+!verificar_fechada: fechada(true)
  <-  .print("Alguem mexeu na MACANETA e FECHOU a porta!").
+!verificar_fechada: fechada(false)
  <-  .print("Alguem mexeu na MACANETA e ABRIU a porta!").
  
+movimento_fechadura <- !verificar_trancada.

+!verificar_trancada: trancada(true)
  <-  .print("Alguem mexeu na FECHADURA e TRANCOU a porta!").
+!verificar_trancada: trancada(false)
  <-  .print("Alguem mexeu na FECHADURA e DESTRANCOU a porta!").
      
+closed  <-  .print("Close event from GUIInterface").
   
+!fechar_porta: fechada(true)
 	<-  .print("Porta Fechada!");
 		!trancar_porta.
 	
+!fechar_porta: fechada(false)
 	<-  fechar;
 		.print("FECHEI a porta");
 		!fechar_porta.
 		
+!trancar_porta: trancada(true)
 	<- .print("Porta Trancada!").
 	
+!trancar_porta: trancada(false)
 	<- 	trancar;
 		.print("TRANQUEI a porta!");
 		!trancar_porta.

// Cenario: proprietario chegando
+!abrir_porta
	<- .print("[FECHADURA] Recebido comando: abrir porta");
	   destrancar;
	   .print("[FECHADURA] >>> Porta DESTRANCADA");
   	   abrir;
   	   .print("[FECHADURA] >>> Porta ABERTA para o proprietario");
   	   .print("[FECHADURA] Aguardando 5 segundos para fechar automaticamente...");
   	   .wait(5000);
   	   !fechar_porta_automaticamente.

+!fechar_porta_automaticamente: proprietario_em_casa
	<- fechar;
	   trancar;
	   .print("[FECHADURA] >>> Porta FECHADA e TRANCADA automaticamente").

// Plano fallback sem contexto para evitar "no applicable plan" quando meta for posta
 +!fechar_porta_automaticamente
 	<- fechar;
 	   trancar;
 	   .print("[FECHADURA] >>> Porta FECHADA e TRANCADA automaticamente (fallback)").

// Cenario: proprietario saindo
+!fechar_e_trancar_porta
	<- .print("[FECHADURA] Recebido comando: fechar e trancar");
	   fechar;
	   trancar;
   	   .print("[FECHADURA] >>> Porta FECHADA e TRANCADA - proprietario saiu").

// Cenario: intruso detectado
+!trancar_porta_intruso
	<- .print("[FECHADURA] *** MODO SEGURANCA ATIVADO!");
	   trancar;
   	   .print("[FECHADURA] >>> Porta TRANCADA devido a intruso!").

// Awareness de estado do sistema
+proprietario_em_casa
	<- .print("Fechadura: Proprietario esta em casa.").

+casa_vazia
	<- .print("Fechadura: Casa esta vazia - modo seguranca ativo.").

+intruso_na_casa(X)
	<- .print("Fechadura: ALERTA! Intruso detectado: ", X).
