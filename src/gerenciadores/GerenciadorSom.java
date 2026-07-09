package gerenciadores;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GerenciadorSom {

    private static Clip musicaAtual;

    public static final String ROUND_1 =
        "/assets/sons/primeiroRound.wav";

    public static final String ROUND_2 =
            "/assets/sons/segundoRound.wav";

    public static final String ROUND_FINAL =
            "/assets/sons/roundFinal.wav";

    public static final String LUTEM =
            "/assets/sons/lutem.wav";

    public static final String PERFEITO =
            "/assets/sons/perfeito.wav";

    public static final String JOGADOR1_VENCEU =
            "/assets/sons/jogadorUmVenceu.wav";

    public static final String JOGADOR2_VENCEU =
            "/assets/sons/jogadorDoisVenceu.wav";

    public static final String MUSICA_LUTA =
            "/assets/sons/UESBFIGHTER_Campus Kombat.wav";

    public static final String MUSICA_MENU =
            "/assets/sons/menu.wav";

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

    public static void tocarLutem() {
        tocarEfeito(LUTEM);
    }

    public static void tocarRound1() {
        tocarEfeito(ROUND_1);
    }

    public static void tocarRound2() {
        tocarEfeito(ROUND_2);
    }

    public static void tocarRoundFinal() {
        tocarEfeito(ROUND_FINAL);
    }

    public static void tocarPerfeito() {
        tocarEfeito(PERFEITO);
    }

    public static void tocarJogador1Venceu() {
        tocarEfeito(JOGADOR1_VENCEU);
    }

    public static void tocarJogador2Venceu() {
        tocarEfeito(JOGADOR2_VENCEU);
    }

    public static void iniciarMusicaLuta() {
        tocarMusica(MUSICA_LUTA);
    }

    public static void iniciarMusicaMenu() {
        tocarMusica(MUSICA_MENU);
    }
}