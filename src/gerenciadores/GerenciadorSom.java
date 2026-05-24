package gerenciadores;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GerenciadorSom {

    private static Clip musicaAtual;

    public static void tocarMusica(String caminho) {

        try {

            if (musicaAtual != null && musicaAtual.isRunning()) {
                musicaAtual.stop();
                musicaAtual.close();
            }

            URL url = GerenciadorSom.class.getResource(caminho);

            AudioInputStream audio =
                AudioSystem.getAudioInputStream(url);

            musicaAtual = AudioSystem.getClip();

            musicaAtual.open(audio);

            musicaAtual.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pararMusica() {

        if (musicaAtual != null) {
            musicaAtual.stop();
            musicaAtual.close();
        }
    }

    public static void tocarEfeito(String caminho) {

        try {

            URL url = GerenciadorSom.class.getResource(caminho);

            AudioInputStream audio =
                AudioSystem.getAudioInputStream(url);

            Clip efeito = AudioSystem.getClip();

            efeito.open(audio);

            efeito.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}