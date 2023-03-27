package controllers;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import network.Client;

public class BoardController extends JFrame implements ActionListener {
    private boolean vezDoJogadorX = false;
    private JButton[][] botoes;
    private Client client;

    public BoardController(Client client) {
        this.client = client;
        this.vezDoJogadorX = client.getId() == 0;
        initPage(); 
    }
    public JButton[][] getBotoes() {
        return this.botoes;
    }

    public void setBotoes(JButton[][] botoes) {
        this.botoes = botoes;
    }

    public void toogleVezDoJogadorX() {
        this.vezDoJogadorX = !this.vezDoJogadorX;
    }

    public void initPage(){

        ImageIcon background = new ImageIcon("img/Board/tictactoepage.png");

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background.getImage(), 0, 0, null);
            }
        };
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(background.getIconWidth(), background.getIconHeight()));
        
        //Configuração da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setTitle("Jogo da Velha");
        setLayout(null); // Definindo um layout nulo

        botoes = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoes[i][j] = new JButton();
                botoes[i][j].setFont(new Font("Arial", Font.BOLD, 105));
                botoes[i][j].addActionListener(this);
                botoes[i][j].setOpaque(false);
                botoes[i][j].setBorderPainted(false);
                botoes[i][j].setContentAreaFilled(false);
                panel.add(botoes[i][j]); // Adiciona o botão ao painel
            }
        }

        botoes[0][0].setBounds(120, 147, 139, 134);
        botoes[0][1].setBounds(281, 147, 139, 134);
        botoes[0][2].setBounds(442, 147, 139, 134);
        botoes[1][0].setBounds(116, 308, 139, 134);
        botoes[1][1].setBounds(281, 308, 139, 134);
        botoes[1][2].setBounds(442, 308, 139, 134);
        botoes[2][0].setBounds(122, 469, 139, 134);
        botoes[2][1].setBounds(281, 469, 139, 134);
        botoes[2][2].setBounds(442, 469, 139, 134);

        setTitle("Velha Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        JButton botaoClicado = (JButton) e.getSource();

        if (botaoClicado.getText().equals("")) {
            if (vezDoJogadorX) {
                
                System.out.println("ActionPerformed" + client.getId());

                String symbol = client.getId() == 0 ? "X" : "O";
                botaoClicado.setText(symbol);
                String collor = client.getId() == 0 ? "#E60067" : "#02A3D9";
                botaoClicado.setForeground(Color.decode(collor)) ;

                int linha = getLinha(botaoClicado);
                int coluna = getColuna(botaoClicado);

                System.out.print(client.socket.isConnected());
                System.out.println(coluna + ", " + linha );
                client.sendMessage(coluna + ", " + linha + ", " + client.getId());

                verificarVencedor(symbol);
                verificarEmpate();
                toogleVezDoJogadorX();

            }
        }
    }
    
    public void updateBoard(int col, int row) {
        System.out.println("ActionPerformed" + client.getId());

        String symbol = client.getId() == 1 ? "X" : "O";
        botoes[col][row].setText(symbol);
        String collor = client.getId() == 1 ? "#E60067" : "#02A3D9";
        botoes[col][row].setForeground(Color.decode(collor)) ;

        verificarVencedor(symbol);
        verificarEmpate();
    }
    
    private int getLinha(JButton botao) {
        int linha = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (botoes[i][j].equals(botao)) {
                    linha = i;
                    break;
                }
            }
        }
        return linha;
    }
    
    private int getColuna(JButton botao) {
        int coluna = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (botoes[i][j].equals(botao)) {
                    coluna = j;
                    break;
                }
            }
        }
        return coluna;
    }

    // Verifica se há um vencedor
    public boolean verificarVencedor(String jogador) {
        // Verifica linhas
        for (int i = 0; i < 3; i++) {
            if (botoes[i][0].getText().equals(jogador) &&
                botoes[i][1].getText().equals(jogador) &&
                botoes[i][2].getText().equals(jogador)) {
                    System.out.println("Vencedor");
                JOptionPane.showMessageDialog(null, "Vencedor: " + jogador);
                reiniciarJogo();
                return true;
            }
        }
        // Verifica colunas
        for (int i = 0; i < 3; i++) {
            if (botoes[0][i].getText().equals(jogador) &&
                    botoes[1][i].getText().equals(jogador) &&
                    botoes[2][i].getText().equals(jogador)) {
                        System.out.println("Vencedor");
                JOptionPane.showMessageDialog(null, "Vencedor: " + jogador);
                reiniciarJogo();
                return true;
            }
        }
        // Verifica diagonais
        if (botoes[0][0].getText().equals(jogador) &&
                botoes[1][1].getText().equals(jogador) &&
                botoes[2][2].getText().equals(jogador)) {
                    System.out.println("Vencedor");
            JOptionPane.showMessageDialog(null, "Vencedor: " + jogador);
            reiniciarJogo();
            return true;
        }
        if (botoes[2][0].getText().equals(jogador) &&
            botoes[1][1].getText().equals(jogador) &&
            botoes[0][2].getText().equals(jogador)) {
            System.out.println("Vencedor");
            JOptionPane.showMessageDialog(null, "Vencedor: " + jogador);
            reiniciarJogo();
            return true;
            
        }
        
        return false;
    }

    // Verifica se há empate
    public boolean verificarEmpate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (botoes[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Empate");
        System.out.println("Empate");
        reiniciarJogo();
        return true;
    }

    
    // Reinicia o jogo
    public void reiniciarJogo() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoes[i][j].setText("");
            }
        }
    }
}