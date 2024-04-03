import entidades.PartidaXadrez;
import entidades.Posicao;
import entidades.Tela;
import exception.TabuleiroException;

import java.util.Scanner;

public class Main {
    public static void main (String[] a)
    {
        try {
            PartidaXadrez partida = new PartidaXadrez();

            while (!partida.isTerminada()) {

                try {
                    Tela.imprimirPartida(partida);

                    System.out.println();
                    System.out.print("Origem: ");
                    Posicao origem = Tela.lerPosicaoXadrez();
                    partida.validarPosicaoDeOrigem(origem);

                    boolean[][] posicoesPossiveis = partida.getTab().peca(origem).movimentosPossiveis();

                    Tela.imprimirTabuleiro(partida.getTab());

                    System.out.println();
                    System.out.print("Destino: ");
                    Posicao destino = Tela.lerPosicaoXadrez();
                    partida.validarPosicaoDeDestino(origem, destino);

                    partida.realizaJogada(origem, destino);
                } catch (TabuleiroException e) {
                    System.out.println(e.getMessage());
                    Scanner scanner = new Scanner(System.in);
                    scanner.nextLine(); // Aguarda o pressionamento de Enter
                }
            }
            Tela.imprimirPartida(partida);
        } catch (TabuleiroException e) {
            System.out.println(e.getMessage());
        }
    }
}
