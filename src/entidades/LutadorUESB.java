package entidades;

public class LutadorUESB {

    private int id;

    private String nome;

    private String pastaSprites;

    private int vida;

    private int velocidade;

    private int forca;

    private int alturaPulo;

    private int danoSoco;

    private int danoChute;

    private int defesa;

    private int alcanceSoco;

    private int alcanceChute;

    private int velocidadeAnimacao;

    private int larguraHitbox;

    private int alturaHitbox;

    private int larguraSprite;

    private int alturaSprite;

    private int ajusteY;

    public LutadorUESB(
            int id,
            String nome,
            String pastaSprites,
            int vida,
            int velocidade,
            int forca,
            int alturaPulo,
            int danoSoco,
            int danoChute,
            int defesa,
            int alcanceSoco,
            int alcanceChute,
            int velocidadeAnimacao,
            int larguraHitbox,
            int alturaHitbox,
            int larguraSprite,
            int alturaSprite,
            int ajusteY) {
        
        this.id = id;
        this.nome = nome;
        this.pastaSprites = pastaSprites;
        this.vida = vida;
        this.velocidade = velocidade;
        this.forca = forca;
        this.alturaPulo = alturaPulo;
        this.danoSoco = danoSoco;
        this.danoChute = danoChute;
        this.defesa = defesa;
        this.alcanceSoco = alcanceSoco;
        this.alcanceChute = alcanceChute;
        this.velocidadeAnimacao = velocidadeAnimacao;
        this.larguraHitbox = larguraHitbox;
        this.alturaHitbox = alturaHitbox;
        this.larguraSprite = larguraSprite;
        this.alturaSprite = alturaSprite;
        this.ajusteY = ajusteY;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPastaSprites() {
        return pastaSprites;
    }

    public int getVida() {
        return vida;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getForca() {
        return forca;
    }

    public int getAlturaPulo() {
        return alturaPulo;
    }

    public int getDanoSoco() {
        return danoSoco;
    }

    public int getDanoChute() {
        return danoChute;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getAlcanceSoco() {
        return alcanceSoco;
    }

    public int getAlcanceChute() {
        return alcanceChute;
    }

    public int getVelocidadeAnimacao() {
        return velocidadeAnimacao;
    }

    public int getLarguraHitbox() {
        return larguraHitbox;
    }

    public int getAlturaHitbox() {
        return alturaHitbox;
    }

    public int getLarguraSprite() {
        return larguraSprite;
    }

    public int getAlturaSprite() {
        return alturaSprite;
    }

    public int getAjusteY() {
        return ajusteY;
    }
}