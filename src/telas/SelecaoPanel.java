package telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gerenciadores.GerenciadorTelas;

public class SelecaoPanel extends JPanel {

    private Image fundo;

    public SelecaoPanel(GerenciadorTelas janela) {

		fundo = new ImageIcon(
			getClass().getResource(
				"/assets/telas/selectUsers.png"
			)
		).getImage();

        setLayout(null);
        setBackground(Color.DARK_GRAY);

        JLabel titulo = new JLabel("SELEÇÃO DE JOGADORES");
        titulo.setBounds(350, 100, 600, 80);
        titulo.setFont(new Font("Arial", Font.BOLD, 40));
        titulo.setForeground(Color.WHITE);

    }

	@Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(fundo, 0, 0, getWidth(), getHeight(), null);
    }
}