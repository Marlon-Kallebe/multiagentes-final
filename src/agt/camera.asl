!inicializar_camera.

+!inicializar_camera
  <- makeArtifact("camera_quarto","artifacts.Camera",[],D);
     makeArtifact("cadastro_pessoas","artifacts.CadastroPessoas",[],D2);
    focus(D);
    focus(D2);
    .print("[CAMERA] Camera e sistema de cadastro inicializados").

+closed
  <- .print("Close event from GUIInterface").

+!verificar_pessoa : pessoa_presente(P) & local(L)
  <- .print("Pessoa: ", P, " reconhecida no local ", L, " da casa.").


////////////////////////////////////////////////////
// EVENTOS REAIS QUE VIRAO DO Camera.java
////////////////////////////////////////////////////

// 1 - DONO CHEGOU
+dono_chegou(J)[artifact_id(ArtId)]
<-  .print("========================================");
    .print("CENARIO 1: PROPRIETARIO CHEGOU!");
    .print("Camera identificou: ", J);
    .print("========================================");
    .broadcast(untell, intruso_na_casa(_));
    .broadcast(untell, casa_vazia);
    .wait(50);
    .broadcast(tell, parar_intruso);
    .wait(50);
    .send(fechadura, achieve, abrir_porta);
    .send(ar_condicionado, achieve, ajustar_para_preferencia(J));
    .send(lampada, achieve, ligar_lampada_proprietario);
    .send(cortina, achieve, abrir_cortina_total);
    .send(janela, achieve, abrir_janelas_bem_vindo);
    .broadcast(tell, proprietario_em_casa);
    .print("Acoes para chegada do dono executadas.").


// 2 - MORADOR (nao dono) CHEGOU
+morador_chegou(Pessoa)[artifact_id(ArtId)]
<-  .print("========================================");
    .print("CENARIO 1B: MORADOR CONHECIDO CHEGOU!");
    .print("Camera identificou morador: ", Pessoa);
    .print("========================================");
    .broadcast(untell, intruso_na_casa(_));
    .broadcast(untell, casa_vazia);
    .wait(50);
    .broadcast(tell, parar_intruso);
    .wait(50);
    .send(fechadura, achieve, abrir_porta);
    .send(lampada, achieve, ligar_lampada_sala);
    .broadcast(tell, morador_em_casa(Pessoa)).
    

// 3 - PESSOA SAIU
+pessoa_saiu(Pessoa)[artifact_id(ArtId)]
<-  .print("========================================");
    .print("CENARIO 2: ", Pessoa, " SAIU!");
    .print("Camera detectou saida de: ", Pessoa);
    .print("========================================");
    .broadcast(untell, intruso_na_casa(_));
    .broadcast(untell, proprietario_em_casa);
    .broadcast(untell, morador_em_casa(_));
    .wait(50);
    .broadcast(tell, parar_intruso);
    .wait(50);
    .send(ar_condicionado, achieve, desligar_sistema);
    .send(lampada, achieve, desligar_tudo);
    .send(cortina, achieve, fechar_cortina_total);
    .send(janela, achieve, fechar_trancar_janelas_saida);
    .send(fechadura, achieve, fechar_e_trancar_porta);
    .broadcast(tell, casa_vazia);
    .print("Sistemas ajustados - ", Pessoa, " saiu da casa").


// 4 - INTRUSO DETECTADO - Verifica se pessoa ja foi cadastrada
+intruso_detectado(X)[artifact_id(ArtId)]
<-  // Primeiro verifica se a pessoa foi cadastrada
    verificar_pessoa(X);
    .wait(100).

// 4X - Aguardando decisao do usuario (popup aberto)
+aguardando_decisao_cadastro(X)[artifact_id(ArtId)]
<-  .print("[CAMERA] Aguardando decisao do usuario sobre: ", X);
    .print("[CAMERA] Popup de cadastro aberto...").

// 4A - Pessoa desconhecida confirmada (nao esta no cadastro)
+pessoa_desconhecida(X)[artifact_id(ArtId)]
<-  .print("========================================");
    .print("*** CENARIO 3: INTRUSO DETECTADO! ***");
    .print("Pessoa desconhecida: ", X);
    .print("ATIVANDO MODO DEFESA!");
    .print("========================================");
    .broadcast(untell, intruso_na_casa(_));
    .broadcast(untell, proprietario_em_casa);
    .broadcast(untell, casa_vazia);
    .wait(50);
    .broadcast(tell, intruso_na_casa(X));
    .send(ar_condicionado, achieve, modo_intruso_frio);
    .send(lampada, achieve, modo_intruso);
    .send(cortina, achieve, modo_intruso_cortina);
    .send(janela, achieve, modo_defesa_janelas);
    .send(fechadura, achieve, trancar_porta_intruso);
    .send(alarme, achieve, notificar_intruso(X));
    .print("Modo defesa ativado para todos os sistemas").

// 4B - Pessoa conhecida (ja cadastrada) - trata como morador
+pessoa_conhecida(X)[artifact_id(ArtId)]
<-  .print("========================================");
    .print("PESSOA CONHECIDA DETECTADA!");
    .print("Camera identificou pessoa cadastrada: ", X);
    .print("========================================");
    .broadcast(untell, intruso_na_casa(_));
    .broadcast(untell, casa_vazia);
    .wait(50);
    .broadcast(tell, parar_intruso);
    .wait(50);
    .send(fechadura, achieve, abrir_porta);
    .send(lampada, achieve, ligar_lampada_sala);
    .broadcast(tell, morador_em_casa(X)).

////////////////////////////////////////////////////
// INTEGRACAO COM CADASTRO - OFERECER CADASTRO
////////////////////////////////////////////////////

 +pessoa_cadastrada(Pessoa)
  <- .print("[CAMERA] ========================================");
     .print("[CAMERA] PESSOA CADASTRADA COM SUCESSO!");
     .print("[CAMERA] ", Pessoa, " agora e reconhecida como confiavel");
     .print("[CAMERA] PROXIMAS visitas: Acesso automatico");
     .print("[CAMERA] ========================================");
     // Para o modo intruso pois pessoa foi cadastrada como confiavel
     .broadcast(untell, intruso_na_casa(_));
     .wait(50);
     .broadcast(tell, parar_intruso);
     .print("[CAMERA] Modo defesa desativado - pessoa cadastrada").

 +pessoa_rejeitada(Pessoa)
  <- .print("[CAMERA] ========================================");
     .print("[CAMERA] Pessoa ", Pessoa, " foi REJEITADA");
     .print("[CAMERA] Sera necessaria nova autorizacao para acesso");
     .print("[CAMERA] ========================================").

