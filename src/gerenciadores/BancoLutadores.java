package gerenciadores;

import entidades.LutadorUESB;

public class BancoLutadores {

    private static final LutadorUESB[] lutadores = {

        new LutadorUESB(
            0,
            "EVERTON",
            "everton",
            100,    // vida
            6,      // velocidade
            10,     // força
            18,     // altura do pulo
            8,      // dano soco
            12,     // dano chute
            3,      // defesa
            60,     // alcance soco
            90,     // alcance chute
            8,      // velocidade animação
            180,    // largura hitbox
            260,     // altura hitbox
            300,    //largura sprite
            400,     //altura sprite
            0
        ),

        new LutadorUESB(
            1,
            "ERICK",
            "erick",
            100,
            5,
            12,
            16,
            10,
            15,
            4,
            70,
            100,
            7,
            170,
            270,
            100,
            450,
            -15
        ),

        new LutadorUESB(
            2,
            "GIULIA",
            "giulia",
            100,
            8,
            8,
            20,
            7,
            11,
            2,
            55,
            80,
            10,
            160,
            250,
            300,
            400,
            30
        )
    };

    public static LutadorUESB get(int indice) {
        return lutadores[indice];
    }

    public static int quantidade() {
        return lutadores.length;
    }
}