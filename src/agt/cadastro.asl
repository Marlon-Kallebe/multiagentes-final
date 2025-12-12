// Agent cadastro
// Gerencia o cadastro de pessoas conhecidas e desconhecidas na casa

!inicializar_cadastro.

+!inicializar_cadastro <-
    .print("[CADASTRO] Agente iniciado e monitorando sistema").

////////////////////////////////////////////////////
// PESSOA DESCONHECIDA DETECTADA
////////////////////////////////////////////////////
+pessoa_desconhecida(Nome) <-
    .print("========================================");
    .print("[CADASTRO] *** PESSOA DESCONHECIDA DETECTADA! ***");
    .print("[CADASTRO] Nome: ", Nome);
    .print("[CADASTRO] Oferecendo opcao de cadastro...");
    .print("========================================").

////////////////////////////////////////////////////
// PESSOA CONHECIDA DETECTADA
////////////////////////////////////////////////////
 +pessoa_conhecida(Nome) <-
  .print("[CADASTRO] [OK] Pessoa conhecida identificada: ", Nome);
    .print("[CADASTRO] Acesso autorizado").

////////////////////////////////////////////////////
// CADASTRO BEM-SUCEDIDO
////////////////////////////////////////////////////
 +pessoa_cadastrada(Nome)
  <- .print("========================================");
    .print("[CADASTRO] [OK] CADASTRO BEM-SUCEDIDO!");
    .print("[CADASTRO] Pessoa: ", Nome);
    .print("[CADASTRO] Status: Agora reconhecida como conhecida");
    .print("[CADASTRO] Proximas visitas: Acesso automatico");
    .print("========================================");
     !registrar_pessoa_novo_cadastro(Nome).

// Registra informacoes da nova pessoa cadastrada
+!registrar_pessoa_novo_cadastro(Nome)
  <- .abolish(pessoa_desconhecida(Nome));
     +pessoa_conhecida(Nome);
     .print("[CADASTRO] Registro atualizado no banco de dados").

////////////////////////////////////////////////////
// REJEICAO DE CADASTRO
////////////////////////////////////////////////////
 +pessoa_rejeitada(Nome)
  <- .print("[CADASTRO] [X] Cadastro rejeitado para: ", Nome);
    .print("[CADASTRO] Pessoa NAO sera cadastrada no sistema");
    .print("[CADASTRO] Futuros acessos necessitarao nova autorizacao").

////////////////////////////////////////////////////
// REMOVER PESSOA DO CADASTRO
////////////////////////////////////////////////////
+!remover_pessoa(Nome)
  <- .print("[CADASTRO] Removendo pessoa do cadastro: ", Nome);
     .abolish(pessoa_conhecida(Nome)).
