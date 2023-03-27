package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/*A Classe ClientHandler, fica por fim de tratar as conexões dos Clientes
* ela é responsavel por gerenciar e cooerdenar mensagens entre o Cliente e
* o Servidor.*/

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final List<Socket> socketsList;
    private List<ClientHandler> handlersList;
    private final BufferedReader in;
    private final PrintWriter out;
    private final int id;
    private static int actionPlayCounter = 0;
    private static int actionStartCounter = 0;
    private static int turn = 0;

    public ClientHandler(Socket clientSocket, List<Socket> socketsList, List<ClientHandler> handlersList, int id) throws IOException {
        this.clientSocket = clientSocket;
        this.socketsList = socketsList;
        this.handlersList = handlersList;
        this.id = id;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public int getId() {
        return id;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendMessageToClient(int clientId, String message) {
        for (ClientHandler handler : handlersList) {
            if (handler.getId() == clientId) {
                handler.sendMessage(message);
                break;
            }
        }
    }

    @Override
    public void run() {
        
        try {
            String message;
            
            sendMessage("id:" + Integer.toString(getId()));

            while ((message = in.readLine()) != null) {
                //Evento de Quando os dois jogadores Clicam em Play.
                if (message.equals("ACTION:PLAY")) {
                    actionPlayCounter++;
                    // Verifica se o contador chegou a 2.
                    if (actionPlayCounter == 2) {
                        // Quando a mensagem é recebida de ambos os jogadores, envia "SHOW:DONE" para eles.
                        sendMessageToClient(0, "SHOW:DONE");
                        sendMessageToClient(1, "SHOW:DONE");
                        actionPlayCounter = 0; // zera o contador
                    }
                }
                //Evento de quando os dois jogadores clicam em Começar
                if (message.equals("ACTION:START")) {
                    actionStartCounter++;
                    // Verifica se o contador chegou a 2
                    if (actionStartCounter == 2) {
                        // Quando a mensagem é recebida de ambos os jogadores, envia "SHOW:PLAY" para eles.
                        sendMessageToClient(0, "SHOW:PLAY");
                        sendMessageToClient(1, "SHOW:PLAY");
                        actionStartCounter = 0; 
                    }
                }

                if (message.matches("\\d, \\d, \\d")) {
                    //Recebe a mensagem referente a jogada do jogador.
                    //id, row, col.
                    String[] tokens = message.split(", ");
                    int id = Integer.parseInt(tokens[2]);
                    System.out.println(tokens[0] + tokens[1] + tokens[2]);
                    System.out.println(id);
                    if (turn == id) {
                        System.out.println("Alternou");
                        System.out.println(id);

                        turn = (turn == 0) ? 1 : 0;
                        //Manda a mensagem referente a jogada de um jogador para o outro.
                        sendMessageToClient(turn, tokens[0] + ", " + tokens[1]);;  
                    }
                    
                }

                if (message.equals("QUIT")) {
                    System.out.println("Cliente " + id + " desconectando...");
                    // Fecha o socket e remove o handler da lista
                    clientSocket.close();
                    handlersList.remove(this);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error in client handler: " + e.getMessage());
        } finally {
            try {
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                clientSocket.close();
                socketsList.remove(clientSocket);
            } catch (IOException e) {
                System.out.println("Error closing client handler: " + e.getMessage());
            }
        }
    }
}