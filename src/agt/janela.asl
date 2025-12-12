// Agent janela
// Controla janelas automaticamente nos 3 cenarios principais

!inicializar_janela.

+!inicializar_janela
  <- makeArtifact("janela_quarto","artifacts.Janela",[],D);
     focus(D);
     .print("[JANELA] Agente iniciado com sucesso").

// Detecta movimento na janela
+movimento_janela <-
    .print("[JANELA] Movimento detectado na janela").

// Detecta movimento na tranca
+movimento_tranca <-
    .print("[JANELA] Movimento detectado na tranca").

////////////////////////////////////////////////////
// CENARIO 1: PROPRIETARIO CHEGANDO
////////////////////////////////////////////////////
+!abrir_janelas_bem_vindo
  <- .print("[JANELA] Recebido comando: abrir janelas para bem-vindo");
     abrir;
     .wait(1000);
    .print("[JANELA] >>> Janelas ABERTAS para o proprietario").

////////////////////////////////////////////////////
// CENARIO 2: PROPRIETARIO SAINDO
////////////////////////////////////////////////////
+!fechar_trancar_janelas_saida
  <- .print("[JANELA] Recebido comando: fechar e trancar janelas");
     fechar;
     .wait(300);
     trancar;
    .print("[JANELA] >>> Janelas FECHADAS e TRANCADAS - proprietario saiu").

////////////////////////////////////////////////////
// CENARIO 3: INTRUSO DETECTADO
////////////////////////////////////////////////////
+!modo_defesa_janelas
  <- .print("[JANELA] *** MODO DEFESA ATIVADO!");
     .print("[JANELA] Fechando e trancando todas as janelas...");
     fechar;
     .wait(300);
     trancar;
    .print("[JANELA] >>> Janelas TRANCADAS IMEDIATAMENTE - Bloqueio de saida do intruso!").

////////////////////////////////////////////////////
// AWARENESS DE ESTADO DO SISTEMA
////////////////////////////////////////////////////

 +proprietario_em_casa
  <- .print("[JANELA] Proprietario esta em casa").

 +casa_vazia
  <- .print("[JANELA] Casa vazia - modo de seguranca ativo").

+intruso_na_casa(X)
  <- .print("[JANELA] *** ALERTA! Intruso detectado: ", X);
     !modo_defesa_janelas.

// Handler para parar rotinas de intruso
+parar_intruso
 <- .drop_intention(modo_defesa_janelas);
   .print("[JANELA] Parando modo intruso via parar_intruso event.").
