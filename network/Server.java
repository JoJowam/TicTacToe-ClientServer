/**
 * @file Server.Java
 * @brief Arquivo principal do Jogo da Velha com Sistema Client x Servidor
 * @autor Josué Villa Real (vilareal.on@gmail.com)
 * @date 27/03/2023
 * @using JDK 19; Swing; Socket;
 */

package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*A Classe Server abaixo, fica responsavel por abrir a conexão do servidor em uma porta
* e esperar pela conexão dos clientes, ápos os mesmos se conectarem, é criado uma conexão
* entre o ServerSocket (Servidor) e os Sockets Jogador1 e Jogado2 (Clientes). as demais
* funcionalidades são tratadas na classe "ClientHandler" */

public class Server {

    private int port;
    private ServerSocket serverSocket;

    private static Socket jogador1;
    private static Socket jogador2;

    private volatile boolean running; // torna a variável running thread-safe
    private int connectedClients = 0;

    private List<Socket> socketsList = new ArrayList<>();
    private List<ClientHandler> handlersList = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;

        System.out.println("Servidor iniciado na porta " + port);

        while (running) {
            if (connectedClients < 2) {

                System.out.println("Aguardando conexão de jogadores...");
                System.out.println("Endereço IP do servidor: " + serverSocket.getInetAddress().getHostAddress());

                jogador1 = serverSocket.accept();
                socketsList.add(jogador1);
                connectedClients++;

                System.out.println("Jogador " + connectedClients + " conectado: " + jogador1.getInetAddress().getHostAddress());

                // cria um novo handler para o jogador1 e inicia uma nova thread para ele
                ClientHandler handler1 = new ClientHandler(jogador1, socketsList, handlersList, 0);
                handlersList.add(handler1);
                new Thread(handler1).start();

                jogador2 = serverSocket.accept();
                socketsList.add(jogador2);
                connectedClients++;
                System.out.println("Jogador " + connectedClients + " conectado: " + jogador2.getInetAddress().getHostAddress());
                 
                // cria um novo handler para o jogador1 e inicia uma nova thread para ele
                ClientHandler handler2 = new ClientHandler(jogador2, socketsList, handlersList, 1);
                handlersList.add(handler2);
                new Thread(handler2).start();

            } else {
                System.out.println("Número máximo de jogadores conectados.");
                break;
            }
        }
    }

    public void stop() throws IOException {
        running = false;
        serverSocket.close();
    }

    //Envia uma mensagem para todos os clientes conectados.
    public void broadcast(String message) {
        for (ClientHandler handler : handlersList) {
            handler.sendMessage(message);
        }
    }

    //Método para tratar mensagens recebidas pelos clientes.
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

    public static void main(String[] args) {
        Server server = new Server(5000);
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}