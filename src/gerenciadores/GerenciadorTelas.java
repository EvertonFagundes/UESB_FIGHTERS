package gerenciadores;

import javax.swing.JFrame;
import javax.swing.JPanel;

import telas.*;

public class GerenciadorTelas extends JFrame {

    private InputManager input = new InputManager();

    public GerenciadorTelas() {

        GerenciadorSom.inicializar();

        setTitle("UESB Fighters");

        setSize(1280, 720);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        addKeyListener(input);

        setFocusable(true);

        requestFocus();

        add(new MenuPanel(this));

        setVisible(true);


    }

    public void trocarTela(JPanel painel) {

        getContentPane().removeAll();

        add(painel);

        revalidate();

        repaint();

        javax.swing.SwingUtilities
            .invokeLater(() -> {

                requestFocus();

                painel.requestFocusInWindow();
            });
    }
}