package gerenciadores;

public class GerenciadorRounds {

    private int roundAtual;

    private int vitoriasJogador1;

    private int vitoriasJogador2;

    private static final int MAX_VITORIAS = 2;


    public GerenciadorRounds() {

        roundAtual = 1;

        vitoriasJogador1 = 0;

        vitoriasJogador2 = 0;
    }


    // registra vitória do jogador 1
    public void jogador1Venceu() {

        vitoriasJogador1++;

    }


    // registra vitória do jogador 2
    public void jogador2Venceu() {

        vitoriasJogador2++;

    }


    // verifica se a luta acabou
    public boolean lutaTerminou() {

        return vitoriasJogador1 >= MAX_VITORIAS ||
               vitoriasJogador2 >= MAX_VITORIAS;
    }


    // retorna o vencedor final
    public int vencedorFinal() {

        if (vitoriasJogador1 >= MAX_VITORIAS) {
            return 1;
        }

        if (vitoriasJogador2 >= MAX_VITORIAS) {
            return 2;
        }

        return 0;
    }


    // avança para o próximo round
    public void proximoRound() {

        roundAtual++;

    }

    public void resetar(){

        roundAtual = 1;

        vitoriasJogador1 = 0;

        vitoriasJogador2 = 0;

    }


    public int getRoundAtual() {

        return roundAtual;

    }


    public int getVitoriasJogador1() {

        return vitoriasJogador1;

    }


    public int getVitoriasJogador2() {

        return vitoriasJogador2;

    }


    public String placar() {

        return vitoriasJogador1 + " x " + vitoriasJogador2;

    }


    public String getTextoRound() {

        return "ROUND " + roundAtual;

    }
}