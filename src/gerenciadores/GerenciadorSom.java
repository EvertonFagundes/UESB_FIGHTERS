package gerenciadores;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Random;

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
            "/assets/sons/UESBFIGHTER_Campus-Kombat.wav";

    public static final String MUSICA_MENU =
            "/assets/sons/menu.wav";

    public static final String SOCO =
            "/assets/sons/soco.wav";

    public static final String WHOOSH =
            "/assets/sons/whoosh.wav";

    public static final String[] DANO_MASCULINO = {
        "/assets/sons/aah.wav",
        "/assets/sons/gh.wav",
        "/assets/sons/ghh.wav",
        "/assets/sons/hff.wav"
    };

    public static final String DANO_FEMININO =
            "/assets/sons/ungh.wav";

    private static Clip musicaMenu;
    private static Clip musicaLuta;

    public static void pararMusica() {

        if (musicaAtual != null) {

            musicaAtual.stop();
            musicaAtual.setFramePosition(0);

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

    private static Clip carregarClip(String caminho){

        try{

            URL url = GerenciadorSom.class.getResource(caminho);

            AudioInputStream audio =
                AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();

            clip.open(audio);

            return clip;

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void inicializar(){

        musicaMenu = carregarClip(MUSICA_MENU);

        musicaLuta = carregarClip(MUSICA_LUTA);

    }

    public static void iniciarMusicaMenu(){

        if(musicaAtual != null)
            musicaAtual.stop();

        musicaAtual = musicaMenu;

        musicaAtual.setFramePosition(0);
        musicaAtual.loop(Clip.LOOP_CONTINUOUSLY);
        musicaAtual.start();
    }

    public static void iniciarMusicaLuta(){

        if(musicaAtual != null)
            musicaAtual.stop();

        musicaAtual = musicaLuta;

        musicaAtual.setFramePosition(0);
        musicaAtual.loop(Clip.LOOP_CONTINUOUSLY);
        musicaAtual.start();
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

    public static void tocarSoco(){
        tocarEfeito(SOCO);
    }

    public static void tocarWhoosh(){
        tocarEfeito(WHOOSH);
    }

    public static void tocarDano(boolean feminino){

        if(feminino){

            tocarEfeito(DANO_FEMININO);

        }else{
            Random random = new Random();

            int indice = random.nextInt(DANO_MASCULINO.length);

            tocarEfeito(DANO_MASCULINO[indice]);

        }
    }
}