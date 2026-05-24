package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gerenciadores.GerenciadorTelas;
import gerenciadores.GerenciadorSom;

public class MenuPanel extends JPanel {

    private Image fundo;

    public MenuPanel(GerenciadorTelas janela) {

        fundo = new ImageIcon(
			getClass().getResource(
				"/assets/telas/telaInicial.png"
			)
		).getImage();
        

        setLayout(null);

        JButton btnJogar = new JButton("");

        btnJogar.setBounds(510, 520, 250, 60);

        btnJogar.setFont(new Font("Arial", Font.BOLD, 24));

        btnJogar.setBorderPainted(false);
        btnJogar.setContentAreaFilled(false);
        btnJogar.setFocusPainted(false);
        btnJogar.setOpaque(false);
        btnJogar.setBorder(null);

        btnJogar.addActionListener(e -> {
            // janela.trocarTela(new GamePanel(janela));
            janela.trocarTela(new SelecaoPanel(janela));
        });

        add(btnJogar);
        GerenciadorSom.tocarMusica("/assets/sons/menu.wav");
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(fundo, 0, 0, getWidth(), getHeight(), null);
    }
}