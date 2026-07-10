package telas;

import gerenciadores.GerenciadorSom;
import gerenciadores.GerenciadorTelas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

    private Image fundo;
    private ImageIcon gifIcon;

    public MenuPanel(GerenciadorTelas janela) {

        gifIcon = new ImageIcon(
			getClass().getResource(
				"/assets/telas/telaInicial.gif"
			)
		);
        
        fundo = gifIcon.getImage();

        setLayout(null);

        // repinta enquanto anima
        new javax.swing.Timer(16, e -> repaint()).start();

        JButton btnJogar = new JButton("");

        btnJogar.setBounds(510, 565, 250, 60);

        btnJogar.setFont(new Font("Arial", Font.BOLD, 24));

        btnJogar.setBorderPainted(false);
        btnJogar.setContentAreaFilled(false);
        btnJogar.setFocusPainted(false);
        btnJogar.setOpaque(false);
        btnJogar.setBorder(null);

        btnJogar.addActionListener(e -> {
            janela.trocarTela(new SelecaoPanel(janela));
        });

        add(btnJogar);
        GerenciadorSom.iniciarMusicaMenu();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(fundo, 0, 0, getWidth(), getHeight(), null);
    }
}