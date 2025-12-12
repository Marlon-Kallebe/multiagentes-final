!start.

 +!start <-
  .print("[ALARME] Agente carregado e pronto para alerta de intrusao").

////////////////////////////////////////////////////
// INTRUSO DETECTADO - ALERTA E NOTIFICACAO
////////////////////////////////////////////////////
+intruso_detectado(Pessoa) <-
    .print("========================================");
    .print("[ALARME] *** ALERTA CRITICO DE INTRUSAO! ***");
    .print("[ALARME] Pessoa desconhecida detectada: ", Pessoa);
    .print("========================================");
    !ativar_alarme(Pessoa);
    !enviar_notificacao_intruso(Pessoa).

// Ativa alarme sonoro (simula som)
+!ativar_alarme(Pessoa)
  <- .print("[ALARME] ATIVANDO ALARME SONORO!");
    .print("[ALARME] ALARME SONORO ATIVADO!");
     .wait(500);
     .print("[ALARME] ...silencio...");
     .wait(300);
     .print("[ALARME] ALARME SONORO ATIVADO!");
     .wait(500);
     .print("[ALARME] ...silencio...");
     .wait(300);
    .print("[ALARME] Padrao de alarme completado").

// Envia notificacoes aos moradores
+!enviar_notificacao_intruso(Pessoa)
  <- .print("[ALARME] ENVIANDO NOTIFICACAO AOS MORADORES:");
    .print("[ALARME] [SMS] ALERTA DE SEGURANCA: Pessoa desconhecida ", Pessoa, " foi detectada na residencia!");
    .print("[ALARME] [EMAIL] ALERTA DE SEGURANCA: Intruso detectado - ", Pessoa);
    .print("[ALARME] [PUSH] Notificacao enviada para todos os dispositivos registrados");
    .print("[ALARME] [LOG] Incidente registrado para analise posterior").

////////////////////////////////////////////////////
// PROPRIETARIO CHEGOU - DESATIVAR ALARME
////////////////////////////////////////////////////
+dono_chegou(Pessoa) <-
    .print("[ALARME] Dono chegou - ", Pessoa, " reconhecido");
    !desativar_alarme.

+!desativar_alarme
  <- .print("[ALARME] Sistema de alarme desativado");
     .print("[ALARME] Casa segura").

////////////////////////////////////////////////////
// MORADOR CHEGOU - DESATIVAR ALARME
////////////////////////////////////////////////////
+morador_chegou(Pessoa) <-
    .print("[ALARME] Morador conhecido chegou - ", Pessoa);
    !desativar_alarme.

////////////////////////////////////////////////////
// ALERTA DE TEMPERATURA EXTREMA
////////////////////////////////////////////////////
+alerta_temperatura_alta(T)
  <- .print("[ALARME] ALERTA: Temperatura critica alta: ", T, " C!");
    .print("[ALARME] Verificando se e modo defesa contra intruso...").

 +alerta_temperatura_baixa(T)
  <- .print("[ALARME] ALERTA: Temperatura critica baixa: ", T, " C!");
    .print("[ALARME] Verificando se e modo defesa contra intruso...").

// Plano para quando algum agente requisitar notificar moradores sobre intruso
 +!notificar_intruso(Pessoa)
  <- .print("[ALARME] Notificando moradores sobre intruso: ", Pessoa);
    !enviar_notificacao_intruso(Pessoa).
