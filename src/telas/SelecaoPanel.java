package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gerenciadores.GerenciadorTelas;
import gerenciadores.InputManager;

public class SelecaoPanel extends JPanel {

    private Image fundo;
    private Image everton;
    private Image erick;
    private Image giulia;
    private int selecionado = 0;

    private int jogadorAtual = 1;

    private int player1;
    private int player2;
    GerenciadorTelas janela = null;

    public SelecaoPanel(GerenciadorTelas janela) {
        this.janela = janela;

        fundo = new ImageIcon(
            getClass().getResource(
                "/assets/telas/selectUsers.png"
            )
        ).getImage();

        setLayout(null);
        setBackground(Color.DARK_GRAY);

        JLabel titulo =
            new JLabel("SELEÇÃO DE JOGADORES");

        titulo.setBounds(350, 100, 600, 80);

        titulo.setFont(
            new Font("Arial", Font.BOLD, 40)
        );

        titulo.setForeground(Color.WHITE);

        add(titulo);

        everton = new ImageIcon(
            getClass().getResource(
                "/assets/personagens/everton/everton.png"
            )
        ).getImage();

        erick = new ImageIcon(
            getClass().getResource(
                "/assets/personagens/erick/erick.png"
            )
        ).getImage();

        giulia = new ImageIcon(
            getClass().getResource(
                "/assets/personagens/giulia/giulia.png"
            )
        ).getImage();

    //DIREITA
    getInputMap().put(
        javax.swing.KeyStroke.getKeyStroke("RIGHT"),
        "direita"
    );

    getInputMap().put(
        javax.swing.KeyStroke.getKeyStroke("D"),
        "direita"
    );

    getActionMap().put("direita", new javax.swing.AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {

            selecionado++;

            if (selecionado > 2) {
                selecionado = 0;
            }

            repaint();
        }
    });

    //ESQUERDA
    getInputMap().put(
        javax.swing.KeyStroke.getKeyStroke("LEFT"),
        "esquerda"
    );

    getInputMap().put(
        javax.swing.KeyStroke.getKeyStroke("A"),
        "esquerda"
    );

    getActionMap().put("esquerda", new javax.swing.AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {

            selecionado--;

            if (selecionado < 0) {
                selecionado = 2;
            }

            repaint();
        }
    });

    getInputMap().put(
        javax.swing.KeyStroke.getKeyStroke("ENTER"),
        "confirmar"
    );

    getActionMap().put("confirmar", new javax.swing.AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {

            confirmarSelecao();
        }
    });

        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(
            fundo,
            0,
            0,
            getWidth(),
            getHeight(),
            null
        );

        desenharCard(
            g,
            everton,
            "EVERTON",
            80,
            220,
            0
        );

        desenharCard(
            g,
            erick,
            "ERICK",
            430,
            220,
            1
        );

        desenharCard(
            g,
            giulia,
            "GIULIA",
            780,
            220,
            2
        );
    }

    private void desenharCard(
        Graphics g,
        Image imagem,
        String nome,
        int x,
        int y,
        int indice
    ) {

        if (selecionado == indice) {

        g.setColor(Color.YELLOW);

        g.fillRoundRect(
            x - 4,
            y - 4,
            258,
            308,
            20,
            20
        );

    } else {

        g.setColor(Color.WHITE);

        g.fillRoundRect(
            x - 2,
            y - 2,
            254,
            304,
            20,
            20
        );
    }

        g.setColor(
            new Color(0, 0, 0, 180)
        );

        g.fillRoundRect(
            x,
            y,
            250,
            300,
            20,
            20
        );

        g.drawImage(
            imagem,
            x + 25,
            y + 20,
            200,
            200,
            this
        );

        g.setColor(Color.WHITE);

        g.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                24
            )
        );

        g.drawString(
            nome,
            x + 65,
            y + 260
        );
    }

    private void confirmarSelecao() {
        if (jogadorAtual == 1) {

            player1 = selecionado;

            System.out.println(
                "Player 1 escolheu: " + player1
            );

            jogadorAtual = 2;

            selecionado = 0;

            return;
        }

        if (jogadorAtual == 2) {

            player2 = selecionado;

            System.out.println(
                "Player 2 escolheu: " + player2
            );

            System.out.println("Personagens escolhidos, seguindo para seleção de cenário");

            //troca de tela
            janela.trocarTela(
                new SelecaoCenarioPanel(janela, player1, player2)
            );
        }
    }

}