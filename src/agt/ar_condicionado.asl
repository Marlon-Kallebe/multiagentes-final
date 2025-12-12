
// Agent ar_condicionado - versao limpa e compativel com Jason
// Controla temperatura da casa com preferencias por usuario

/* PREFERENCIAS DE USUARIOS - Definidas aqui */
temperatura_de_preferencia("Jonas",25).
temperatura_de_preferencia(jonas,25).
temperatura_de_preferencia("Roberto",23).
temperatura_de_preferencia("Maria",24).
temperatura_de_preferencia("Ana",26).

/* Preferencias de iluminacao complementar */
preferencia_cortina("Jonas",100).
preferencia_cortina("Roberto",70).
preferencia_cortina("Maria",50).

preferencia_lampada("Jonas",100).
preferencia_lampada("Roberto",80).

/* Initial goal */
!inicializar_AC.

+!inicializar_AC
  <- makeArtifact("ac_quarto","artifacts.ArCondicionado",[],D);
     focus(D);
     !definir_temperatura.

// Ajusta temperatura baseada na preferencia ou estado atual
 +!definir_temperatura: temperatura_de_preferencia(User,TP) & ligado(false)
 <- ajustar_temperatura(TP);
    .print("Definindo temperatura baseado na preferencia do usuario ", User, ": ", TP, " C").

 +!definir_temperatura: ligado(false)
 <- .print("Usando ultima temperatura do AC").

// Climatizacao (liga/desliga conforme diferenca)
 +!climatizar: temperatura_ambiente(TA) & temperatura_ac(TAC) & TA \== TAC & ligado(false)
 <- ligar;
    .print("Ligando ar condicionado... ambiente:", TA, " alvo:", TAC);
    .wait(1000);
    !!climatizar.

 +!climatizar: temperatura_ambiente(TA) & temperatura_ac(TAC) & TA \== TAC & ligado(true)
 <- .print("Aguardando regulacao de ", TA, " para ", TAC, "...");
    .wait(4000);
    !!climatizar.

 +!climatizar: temperatura_ambiente(TA) & temperatura_ac(TAC) & TA == TAC & ligado(true)
 <- desligar;
    .print("Desligando ar condicionado. Atual:", TA, " Desejado:", TAC).

// Ajustar para preferencias do usuario (cenario chegada)
 +!ajustar_para_preferencia(J): temperatura_de_preferencia(J,TP) & preferencia_cortina(J,PC) & preferencia_lampada(J,PL)
 <- .print("[AR-CONDICIONADO] Ajustando para preferencias de ", J);
    ajustar_temperatura(TP);
    ligar;
    .print("[AR-CONDICIONADO] Temperatura:", TP, " Cortina:", PC, " Lampada:", PL).

 +!ajustar_para_preferencia(J): not temperatura_de_preferencia(J,_)
 <- .print("[AR-CONDICIONADO] Preferencia nao encontrada para ", J, ", usando 22C");
    ajustar_temperatura(22);
    ligar.

// Desligar sistema (cenario saida)
 +!desligar_sistema: ligado(true)
 <- desligar;
    .print("[AR-CONDICIONADO] Sistema desligado (proprietario saiu)").

 +!desligar_sistema: not ligado(true)
 <- .print("[AR-CONDICIONADO] Sistema ja estava desligado").

// Modo intruso: alterna temperaturas para desconforto (TEMPOS REDUZIDOS)
 +!modo_intruso_frio
 <- .print("[AR-CONDICIONADO] MODO DEFESA ATIVADO!");
    ligar;
    ajustar_temperatura(16);
    .wait(4000);  // era 8000
    !alternar_temperatura_intruso.

 +!alternar_temperatura_intruso: intruso_na_casa(X)
 <- ajustar_temperatura(35);
    .print("[AR-CONDICIONADO] Alternando para 35C");
    .wait(4000);  // era 8000
    ajustar_temperatura(16);
    .print("[AR-CONDICIONADO] Alternando para 16C");
    .wait(4000);  // era 8000
    !alternar_temperatura_intruso.

 +!alternar_temperatura_intruso
 <- desligar;
    .print("[AR-CONDICIONADO] Parando alternancia, sistema desligado").

// Interrompe modo intruso quando requisitado
+parar_intruso
 <- .abolish(intruso_na_casa(_));
    .print("[AR-CONDICIONADO] Parando modo intruso via parar_intruso event.").

// Awareness
 +proprietario_em_casa <- .print("[AC] Proprietario esta em casa.").
 +casa_vazia <- .print("[AC] Casa esta vazia.").
 +intruso_na_casa(X) <- .print("[AC] ALERTA! Intruso detectado: ", X).
