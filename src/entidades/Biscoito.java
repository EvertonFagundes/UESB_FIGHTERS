package entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Biscoito {

    private int x;
    private int y;

    private int velocidade;

    private int dano;

    private boolean ativo;

    private boolean direita;

    public Biscoito(int x, int y, boolean direita){

        this.x = x;
        this.y = y;
        this.direita = direita;

        velocidade = 12;
        dano = 5;
        ativo = true;
    }


    public void atualizar(){

        if(direita){
            x += velocidade;
        }else{
            x -= velocidade;
        }

        if(x < -50 || x > 1400){
            ativo = false;
        }
    }


    public void desenhar(Graphics g){

        g.setColor(new Color(180,120,50));

        g.fillOval(
            x,
            y,
            30,
            20
        );

    }


    public boolean isAtivo(){
        return ativo;
    }


    public int getDano(){
        return dano;
    }


    public int getX(){
        return x;
    }


    public int getY(){
        return y;
    }

    public Rectangle getHitbox(){

        return new Rectangle(
            x,
            y,
            30,
            20
        );
    }

    public void desativar(){
        ativo = false;
    }

}