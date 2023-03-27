package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import controllers.GameController;
import controllers.BoardController;

/*Classe responsavel pela conexão dos Clientes ao Servidor e pela
* cooerdenação de mensagens entre os mesmos.*/

public class Client implements Runnable {

    public Socket socket;
    private String serverAddress;
    private int serverPort;
    private BufferedReader in; //Leitura de mensagens do servidor.
    private PrintWriter out; //Envio de mensagens para o servidor.
    private int clientNumber;

    private GameController gameController = new GameController(this);
    private BoardController boardController;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void disconnect(){
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Erro ao desconectar do servidor: " + e.getMessage());
        }
        
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public int getId(){
        return clientNumber; 
    }

    private void receiveMessageThread() throws IOException {
        Client originalThis = this;
        new Thread(new Runnable(){
            @Override
            public void run() {
                String response;
                try {
                    while (socket.isConnected()) {
                        response = receiveMessage();

                        //Recebe o id dos jogadores.
                        if (response.matches("id:\\d")) {
                            System.out.println("Entrou no if");
                            String[] tokens = response.split(":");
                            int id = Integer.parseInt(tokens[1]);
                            originalThis.clientNumber = id;
                        }

                        //Os dois clicaram em Play.
                        if (response.equals("SHOW:DONE")) {
                            //Mostrando a tela de começar o jogo.
                            gameController.allPlayersConnected();
                        }

                        //os dois clientes clicaram em começar o jogo.
                        if (response.equals("SHOW:PLAY")) {
                            gameController.closeAllPlayersConnected();
                            boardController = new BoardController(originalThis);
                        }

                        //Coletando as coordenadas do tabuleiro e atualizando o mesmo.
                        if (response.matches("\\d, \\d")) {
                            String[] tokens = response.split(", ");
                            int row = Integer.parseInt(tokens[0]);
                            int column = Integer.parseInt(tokens[1]);

                            boardController.updateBoard(column, row);
                            boardController.toogleVezDoJogadorX();
                        }
                        System.out.println("Mensagem do servidor para o cliente " + clientNumber + ": " + response);
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }                     
            }
        }).start();
    }

    private String receiveMessage() throws IOException {
        return in.readLine();
    }

    @Override
    public void run() {
        try {
            connect();
            gameController.mainPage();
            //Chamando a Thread responsável por receber as mensagens do servidor.
            receiveMessageThread();
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
            disconnect();
        }
    }

    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog("Informe o endereço IP do servidor:");
        int serverPort = Integer.parseInt(JOptionPane.showInputDialog("Informe a porta do servidor:"));
        try {
            Client client = new Client(serverAddress, serverPort);
            Thread thread = new Thread(client);
            thread.start();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

