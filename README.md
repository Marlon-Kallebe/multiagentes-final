# Guia de Testes - Smart Home
 
## Iniciar Sistema
 
```bash
./gradlew run
```
 
**Aguarde:** 10 segundos para todas as janelas abrirem espalhadas na tela.
 
---
 
## Testes Principais
 
### Teste 1: Proprietário Chegando
 
**Objetivo:** Verificar automação de boas-vindas
 
**Passos:**
1. Localize janela **Camera**
2. Digite:
   - Nome: `Jonas`
   - Local: `frente`
3. Clique: **ok**
 
**Resultado Esperado:**
- Porta abre automaticamente
- Luzes acendem
- Cortinas abrem (100%)
- Ar-condicionado liga (25°C)
- Janelas abrem
- Log: "CENARIO 1: PROPRIETARIO CHEGOU!"
 
---
 
### Teste 2: Pessoa Saindo
 
**Objetivo:** Verificar modo segurança ao sair
 
**Passos:**
1. Na janela **Camera**, digite:
   - Nome: `Jonas saiu`
2. Clique: **ok**
 
**Resultado Esperado:**
- Todas as luzes apagam
- Cortinas fecham (0%)
- Ar-condicionado desliga
- Janelas fecham e trancam
- Porta fecha e tranca
- Log: "CENARIO 2: Jonas SAIU!"
 
---
 
### Teste 3: Intruso - Cadastrar
 
**Objetivo:** Verificar cadastro de pessoa desconhecida
 
**Passos:**
1. Na janela **Camera**, digite:
   - Nome: `Leo`
2. Clique: **ok**
3. Popup aparece automaticamente
4. Clique: **Cadastrar**
 
**Resultado Esperado:**
- Popup fecha
- SEM modo defesa
- SEM alarme
- Leo aparece na janela **Cadastro**
- Log: "PESSOA CADASTRADA COM SUCESSO!"
 
**Validar Cadastro:**
5. Digite: `sair`
6. Digite: `Leo` novamente
7. Reconhecido como conhecido
8. Porta abre, luzes acendem
9. SEM popup
 
---
 
### Teste 4: Intruso - Rejeitar
 
**Objetivo:** Verificar modo defesa contra intruso
 
**Passos:**
1. Na janela **Camera**, digite:
   - Nome: `carlos`
2. Clique: **ok**
3. Popup aparece
4. Clique: **Rejeitar**
 
**Resultado Esperado:**
- Popup fecha
- Modo defesa ATIVA
- Luzes piscam irregularmente
- Cortinas movem aleatoriamente
- Ar-condicionado alterna (16°C ↔ 35°C)
- Janelas trancam
- Alarme dispara
- Log: "CENARIO 3: INTRUSO DETECTADO!"
 
**Parar Modo Defesa:**
5. Digite: `sair`
6. Loops param em 2-4 segundos
 
---
 
### Teste 5: Morador Conhecido
 
**Objetivo:** Verificar acesso de morador cadastrado
 
**Passos:**
1. Na janela **Camera**, digite:
   - Nome: `Roberto`
2. Clique: **ok**
 
**Resultado Esperado:**
- Porta abre
- Luzes da sala acendem
- SEM popup
- SEM modo defesa
- Log: "MORADOR CONHECIDO CHEGOU!"
 
---
 
## Testes Rápidos (2 minutos)
 
### Sequência Completa:
 
```
1. Jonas chega
   → Tudo liga
 
2. Jonas saiu
   → Tudo desliga
 
3. Leo detectado
   → Cadastrar
   → SEM alarme
 
4. Leo saiu
   → Tudo desliga
 
5. Leo volta
   → Reconhecido
   → Porta abre
 
6. carlos detectado
   → Rejeitar
   → Alarme dispara
 
7. sair
   → Alarme para
```
 
---
 
## Checklist de Validação
 
### Interface:
- [ ] 8 janelas espalhadas (não sobrepostas)
- [ ] Tamanhos legíveis
- [ ] Camera no canto superior esquerdo
- [ ] Cadastro na parte inferior
 
### Cenário 1 - Proprietário:
- [ ] Porta abre
- [ ] Luzes acendem
- [ ] AC liga (25°C)
- [ ] Cortinas abrem
- [ ] Janelas abrem
 
### Cenário 2 - Saída:
- [ ] Tudo desliga
- [ ] Tudo tranca
- [ ] Log mostra nome correto
 
### Cenário 3 - Intruso:
- [ ] Popup aparece
- [ ] Cadastrar → SEM alarme
- [ ] Rejeitar → COM alarme
- [ ] Pessoa cadastrada reconhecida depois
 
### Sensores:
- [ ] Temperatura atualiza a cada 15s
- [ ] Luminosidade atualiza a cada 15s
- [ ] Logs não poluem terminal
 
---
 
## Problemas Comuns
 
### Janelas sobrepostas:
- Feche e execute novamente
- Verifique se está usando versão 2.2
 
### Alarme ao cadastrar:
- Versão correta: SEM alarme ao cadastrar
- Se alarmar: Versão antiga, atualize
 
### Popup não aparece:
- Digite nome desconhecido (não Jonas, Roberto, Maria, Ana)
- Popup é automático
 
### Modo defesa não para:
- Digite: `sair` ou `Nome saiu`
- Para em 2-4 segundos
 
---
 
## Dicas
 
**Testar Cadastro:**
- Use nomes diferentes: Leo, Milena, carlos, Ana Paula
- Cadastre alguns, rejeite outros
- Valide que cadastrados são reconhecidos
 
**Testar Saída:**
- Use formato: `Nome saiu` (ex: `Leo saiu`, `Jonas saiu`)
- Formato antigo `sair` ainda funciona (assume Jonas)
 
**Observar Logs:**
- Logs importantes começam com `[CAMERA]`, `[ALARME]`, etc.
- Sensores atualizam a cada 9 segundos
- Menos poluição visual
 
**Janelas:**
- Camera: Superior esquerdo
- ArCondicionado: Superior centro
- Fechadura: Superior direito
- Lampada: Meio esquerdo
- Cortina: Meio centro
- Janela: Meio direito
- Alarme: Inferior esquerdo
- Cadastro: Inferior centro
 
---
 
## Critérios de Sucesso
 
O sistema está funcionando se:
- Todas as janelas aparecem espalhadas
- Jonas chega → Tudo liga
- Pessoa sai → Tudo desliga
- Cadastrar pessoa → SEM alarme
- Rejeitar pessoa → COM alarme
- Pessoa cadastrada → Reconhecida depois
- Sensores atualizam a cada 9s
 
---
