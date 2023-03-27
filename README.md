# Jogo da Velha - Cliente-Servidor em Java usando Sockets

Este é um projeto simples de Jogo da Velha desenvolvido em Java, que utiliza Sockets para permitir a comunicação entre um cliente e um servidor. O objetivo deste projeto é exemplificar como é possível utilizar a tecnologia de Sockets para desenvolver um jogo em rede.

O projeto foi desenvolvido como parte da disciplina de Redes de Computadores lecionada na Universidade Federal de Ouro Preto - UFOP.

## Funcionamento

O programa é composto por dois aplicativos: um servidor e um cliente. O servidor é responsável por gerenciar a comunicação entre os clientes, recebendo e enviando mensagens para os mesmos. O cliente, por sua vez, se conecta ao servidor para jogar o jogo e interage com ele através de mensagens.

Ao iniciar o servidor, ele começa a aguardar conexões de clientes. Quando um cliente se conecta, o servidor inicia um novo jogo e envia as informações para o cliente, que também fica aguardando a conexão de outro jogador para dar continuidade ao processo. O cliente recebe as informações e exibe as diferentes telas para o usuário. Posteriormente, o cliente pode fazer sua jogada e enviar a posição escolhida para o servidor. O servidor valida a jogada e envia o novo estado do jogo de volta para o cliente. O processo se repete até que um jogador vença ou ocorra um empate.

## Utilização

### Executando o servidor

Para executar o servidor, siga os seguintes passos:

1. Abra o terminal ou prompt de comando e navegue até o diretório onde os arquivos do projeto estão armazenados.
2. Compile os arquivos presentes e inicie o servidor.
3. Inicie o servidor usando o seguinte comando: `java network.Server`.

O servidor agora está em execução e aguardando conexões de clientes. A porta que escolhi para o servidor foi a `5000`, para altera-la basta encontrar o lugar onde a mesma é criada e muda-la para uma porta em seu computador que não esteja em uso no momento.

### Executando o cliente

Para executar o cliente, siga os seguintes passos:

1. Abra o terminal ou prompt de comando e navegue até o diretório onde os arquivos do projeto estão armazenados.
2. Inicie o cliente usando o seguinte comando: `java network.Client`.
3. Digite o IP e a porta em que o servidor esta rodando. A porta que defini foi a "5000".

O cliente agora está em execução e pronto para se conectar ao servidor. Após um jogador se conectar, ele ficará travado na tela de espera de jogadores até que outro jogador se conecte ao servidor e clique em "Jogar". Após isso, basta que os dois jogadores cliquem em "Começar" e façam suas jogadas. O jogador que começa jogando é o cliente 0, ou Jogador X se preferir.  

Comandos para compilar todos os arquivos .java:

```
javac controllers/GameController.java
javac controllers/BoardController.java

javac network/Server.java network/ClientHandler.java
javac network/Client.java

```

**Recomenda-se a compilação de todos os arquivos .java antes da execução do servidor e cliente para evitar problemas de compatibilidade de versões do Java.**

## Avisos

Para garantir a conexão correta entre o cliente e o servidor, é importante digitar corretamente o endereço IP do servidor. Para conexões no mesmo computador, pode-se usar o endereço "localhost" como endereço IP. Para conexões na mesma LAN (Local Area Network), é necessário usar o endereço IPV4 do computador onde o servidor está rodando. Esse endereço pode ser obtido através de comandos no terminal, como por exemplo, o comando "ipconfig" no Windows ou "ifconfig" no Linux.

Já para conexões via internet, é necessário digitar o endereço IP público do computador onde o servidor está rodando. O endereço IP público pode ser obtido através de serviços de consulta de IP público na internet ou verificando as configurações do roteador de rede.

Além disso, é importante destacar que o programa foi implementado usando JDK 19 e a interface gráfica foi feita usando a biblioteca Swing no Java. Por esse motivo, é fundamental prestar atenção no diretório onde o programa é descompactado, pois pode ser necessário mudar o caminho das imagens usadas na interface caso o diretório seja alterado.

Esses detalhes técnicos são importantes para garantir o correto funcionamento do programa e para evitar possíveis problemas de conexão entre o cliente e o servidor.

## Liçensa

Este projeto está sob a licença MIT. Consulte o arquivo LICENSE para obter mais informações.
[MIT License](./LICENSE)

