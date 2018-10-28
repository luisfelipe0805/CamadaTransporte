# CamadaTranporte
 Implementando um protocolo de transporte confiavel


Visão Geral
Neste trabalho, você escreverá o código de nível de transporte de envio e recepção, implementando
um simples protocolo de transferência de dados confiável, utilizando o protocolo bit alternante. O trabalho
será divertido, visto que sua implementação irá diferir um pouco do que seria exigido numa situação do
mundo real.
Como você provavelmente não possui máquinas standalone (com um OS que pode ser modificado),
seu código precisará ser executado num ambiente de hardware/software simulado. No entanto, a interface
de programação fornecida para suas rotinas, por exemplo, o código que poderia chamar suas entidades de
cima e de baixo é bem próximo do que é feito num ambiente UNIX atual. (Na verdade, as interfaces de
software descritas neste exercício são muito mais realistas do que remetentes e destinatários de loop infinito
que muitos textos descrevem.) Interrupção/acionamento de temporizadores serão também simulados, e interrupções
feitas pelo temporizador farão com que seu temporizador manipule rotinas a serem ativadas.
