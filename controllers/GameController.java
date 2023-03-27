package controllers;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Desktop;
import java.awt.event.*;
import java.awt.Font;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import network.ClientHandler;
import network.Client;
import network.Server;

public class GameController extends JFrame {
    
    private static final String BACKGROUND_PATH = "img/MainPage/main_page.png";
    private static final String WINDOW_TITLE = "Velha Online";
    private ClientHandler clientHandler;
    private Client client;
    private Server server;
    private JFrame allPlayersConnected;

    public GameController(Client client) {
        this.client = client;
    }

    private JButton createButton(ImageIcon buttonIcon, ImageIcon buttonHoverIcon, int x, int y, ActionListener actionListener) {
        JButton button = new JButton(buttonIcon);
        button.setSize(buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        button.setLocation(x, y);
        button.setBorderPainted(false);
        
        button.addActionListener(actionListener);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(buttonHoverIcon);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(buttonIcon);
            }
        });
        
        return button;
    }

    public void waitingForPlayersPage() {
    
        ImageIcon background = new ImageIcon("img/PlayButtonPage/waitingForPlayers.png");
    
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, null);
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(background.getIconWidth(), background.getIconHeight()));
    
        setTitle("Velha Online - Conectando Jogadores!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    
    }  
    
    public void allPlayersConnected(){
        ImageIcon background = new ImageIcon("img/PlayButtonPage/readyToPlay.png");
    
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, null);
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(background.getIconWidth(),background.getIconHeight()));
        
        ImageIcon startButtonIcon = new ImageIcon("img/PlayButtonPage/startButtonOff.png");
        ImageIcon startButtonHoverIcon = new ImageIcon("img/PlayButtonPage/startButtonOn.png");
        JButton jogarButton = createButton(startButtonIcon, startButtonHoverIcon, 217, 536, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("ACTION:START");
                //waitingForPlayersPage();
            }
        });
        panel.add(jogarButton);

        setTitle("Velha Online - Todos os Jogadores Conectados!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        allPlayersConnected = this;
    }

    public void closeAllPlayersConnected() {
        allPlayersConnected.dispose();
    }

    public void helpButtonPage(){
        // Configurações do JFrame
        setTitle("Exemplo de JPanel Simples");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 700));
        setLocationRelativeTo(null);
        
        // Criação do JPanel
        JPanel panel = new JPanel();
        
        // Criação do JLabel com o texto
        JLabel label = new JLabel("Pagina About Option");
        label.setFont(new Font("Arial", Font.PLAIN, 24));
        
        // Adição do JLabel ao JPanel
        panel.add(label);
        
        // Configuração do JPanel como conteúdo do JFrame
        setContentPane(panel);
        
        pack();
        setVisible(true);
    }

    public void mainPage() throws IOException {

        ImageIcon background = new ImageIcon(BACKGROUND_PATH);
        JPanel panel = new JPanel() {
            // Sobrescrita do método paintComponent para desenhar a imagem de fundo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, null);
            }
        };

        // Configuração do tamanho e posicionamento do painel
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(background.getIconWidth(), background.getIconHeight()));
    
        // Criação dos botões
        ImageIcon jogarButtonIcon = new ImageIcon("img/MainPage/ButtonsActionMouse/buttonOff_03.png");
        ImageIcon jogarButtonHoverIcon = new ImageIcon("img/MainPage/ButtonsActionMouse/buttonOn_03.png");
        JButton jogarButton = createButton(jogarButtonIcon, jogarButtonHoverIcon, 244, 327, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("ACTION:PLAY");
                waitingForPlayersPage();

            }
        });
        panel.add(jogarButton);
        
        ImageIcon sairButtonIcon = new ImageIcon("img/MainPage/ButtonsActionMouse/buttonOff_06.png");
        ImageIcon sairButtonHoverIcon = new ImageIcon("img/MainPage/ButtonsActionMouse/buttonOn_06.png");
        JButton sairButton = createButton(sairButtonIcon, sairButtonHoverIcon, 244, 402, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage("QUIT");
                System.exit(0);
            }
        });
        panel.add(sairButton);

        ImageIcon githubButtonIcon = new ImageIcon("img/MainPage/ButtonsActionMouse/githubOff.png");
        ImageIcon githubButtonHoverIcon = new ImageIcon("img/MainPage/ButtonsActionMouse/githubOn.png");
        JButton githubButton = createButton(githubButtonIcon, githubButtonHoverIcon, 356, 671, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/JoJowam"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(githubButton);
    
        // Configuração do JFrame
        setTitle(WINDOW_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    
    public static void main(String[] args) {

    }
}
