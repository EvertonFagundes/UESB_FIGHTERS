package gerenciadores;

import javax.swing.JFrame;
import javax.swing.JPanel;

import telas.*;

public class GerenciadorTelas extends JFrame {

    public GerenciadorTelas() {

        setTitle("UESB Fighters");

        setSize(1280, 720);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        add(new MenuPanel(this));

        setVisible(true);
    }

    public void trocarTela(JPanel painel) {

		getContentPane().removeAll();

		add(painel);

		revalidate();

		repaint();
	}
}