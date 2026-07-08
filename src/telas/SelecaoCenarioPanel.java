package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gerenciadores.GerenciadorTelas;

public class SelecaoCenarioPanel extends JPanel {

    // nomes exibidos nos cards
    private String[] nomesCenarios = {
        "BIBLIOTECA",
        "FACHADA",
        "PÁTIO",
        "PISCINA",
        "QUADRA"
    };

    // arquivos dentro de assets/cenarios/
    private String[] arquivosCenarios = {
        "uesb_biblioteca.png",
        "uesb_fachada.png",
        "uesb_meio.png",
        "uesb_piscina.png",
        "uesb_quadra.png"
    };

    private Image[] cenarios = new Image[nomesCenarios.length];

    private Image fundo;

    private int selecionado = 0;

    // personagens já escolhidos na tela anterior
    private int player1;
    private int player2;

    GerenciadorTelas janela = null;

    public SelecaoCenarioPanel(
        GerenciadorTelas janela,
        int player1,
        int player2
    ) {
        this.janela = janela;
        this.player1 = player1;
        this.player2 = player2;

        // fundo da tela (opcional, caso o asset ainda não exista)
        URL fundoUrl = getClass().getResource(
            "/assets/telas/selectScenario.png"
        );

        if (fundoUrl != null) {
            fundo = new ImageIcon(fundoUrl).getImage();
        }

        setLayout(null);
        setBackground(Color.DARK_GRAY);

        JLabel titulo =
            new JLabel("SELEÇÃO DE CENÁRIO");

        titulo.setBounds(350, 100, 600, 80);

        titulo.setFont(
            new Font("Arial", Font.BOLD, 40)
        );

        titulo.setForeground(Color.WHITE);

        add(titulo);

        for (int i = 0; i < arquivosCenarios.length; i++) {

            URL url = getClass().getResource(
                "/assets/cenarios/" + arquivosCenarios[i]
            );

            if (url != null) {
                cenarios[i] = new ImageIcon(url).getImage();
            }
        }

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

                if (selecionado > cenarios.length - 1) {
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
                    selecionado = cenarios.length - 1;
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

        if (fundo != null) {

            g.drawImage(
                fundo,
                0,
                0,
                getWidth(),
                getHeight(),
                null
            );
        }

        int cardLargura = 220;
        int cardAltura = 200;
        int espaco = 15;

        int larguraTotal =
            (cardLargura * cenarios.length)
            + (espaco * (cenarios.length - 1));

        int xInicial = (1280 - larguraTotal) / 2;
        int y = 260;

        for (int i = 0; i < cenarios.length; i++) {

            int x = xInicial + (i * (cardLargura + espaco));

            desenharCard(
                g,
                cenarios[i],
                nomesCenarios[i],
                x,
                y,
                cardLargura,
                cardAltura,
                i
            );
        }
    }

    private void desenharCard(
        Graphics g,
        Image imagem,
        String nome,
        int x,
        int y,
        int largura,
        int altura,
        int indice
    ) {

        if (selecionado == indice) {

            g.setColor(Color.YELLOW);

            g.fillRoundRect(
                x - 4,
                y - 4,
                largura + 8,
                altura + 8,
                20,
                20
            );

        } else {

            g.setColor(Color.WHITE);

            g.fillRoundRect(
                x - 2,
                y - 2,
                largura + 4,
                altura + 4,
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
            largura,
            altura,
            20,
            20
        );

        if (imagem != null) {

            g.drawImage(
                imagem,
                x + 10,
                y + 10,
                largura - 20,
                altura - 60,
                this
            );
        }

        g.setColor(Color.WHITE);

        g.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                20
            )
        );

        g.drawString(
            nome,
            x + 20,
            y + altura - 20
        );
    }

    private void confirmarSelecao() {

        String cenarioEscolhido = arquivosCenarios[selecionado];

        System.out.println(
            "Cenário escolhido: " + nomesCenarios[selecionado]
        );

        //troca de tela
        janela.trocarTela(
            new GamePanel(
                janela,
                player1,
                player2,
                cenarioEscolhido
            )
        );
    }

}
