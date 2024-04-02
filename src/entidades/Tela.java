package entidades;

import java.awt.*;
import java.util.HashSet;
import java.util.Scanner;

public class Tela {

    public static void imprimirPartida(PartidaXadrez partida) {
        imprimirTabuleiro(partida.getTab());
        System.out.println();
        imprimirPecasCapturadas(partida);
        System.out.println();
        System.out.println("Turno: " + partida.getTurno());
        System.out.println("Aguardando jogada: " + partida.getJogadorAtual());
        if (!partida.isTerminada()) {
            if (partida.getXeque()) {
                System.out.println("XEQUE!");
            }
        } else {
            System.out.println("XEQUEMATE!");
            System.out.println("Vencedor: " + partida.getJogadorAtual());
        }
    }

    public static void imprimirPecasCapturadas(PartidaXadrez partida) {
        System.out.println("Peças capturadas:");
        System.out.print("Brancas: ");
        imprimirConjunto(partida.pecasCapturadas(Cor.Branca));
        System.out.print("Pretas: ");
        imprimirConjunto(partida.pecasCapturadas(Cor.Preta));
        System.out.println();
    }

    public static void imprimirConjunto(HashSet<Peca> conjunto) {
        System.out.print("[");
        for (Peca x : conjunto) {
            System.out.print(x + " ");
        }
        System.out.println("]");
    }

    public static void imprimirTabuleiro(Tabuleiro tab) {
        for (int i = 0; i < tab.getLinhas(); i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < tab.getColunas(); j++) {
                imprimirPeca(tab.peca(i, j));
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }


    public static void imprimirPeca(Peca peca) {
        if (peca == null) {
            System.out.print("- ");
        } else {
            if (peca.getCor() == Cor.Branca) {
                System.out.print(peca);
            } else {
                System.out.print(peca);
            }
            System.out.print(" ");
        }
    }

    public static Posicao lerPosicaoXadrez() {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        char coluna = s.charAt(0);
        int linha = Integer.parseInt(s.charAt(1) + "");
        return new PosicaoXadrez(coluna, linha).toPosicao();
    }
}
