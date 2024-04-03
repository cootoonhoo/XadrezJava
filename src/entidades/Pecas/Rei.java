package entidades.Pecas;

import entidades.*;

public class Rei extends Peca {

    private GameMannager partida;

    public Rei(Tabuleiro tab, Cor cor, GameMannager partida) {
        super(tab, cor);
        this.partida = partida;
    }

    private boolean podeMover(Posicao pos) {
        Peca p = tab.peca(pos);
        return p == null || p.getCor() != cor;
    }

    private boolean testeTorreParaRoque(Posicao pos) {
        Peca p = tab.peca(pos);
        return p != null && p instanceof Torre && p.getCor() == cor && qteMovimentos == 0;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[tab.getLinhas()][tab.getColunas()];

        Posicao pos = new Posicao(0, 0);

        // acima
        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna());
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        // ne
        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        // direita
        pos.setarPosicao(posicao.getLinha(), posicao.getColuna() + 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        // sd
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        // abaixo
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna());
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        // so
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        // esquerda
        pos.setarPosicao(posicao.getLinha(), posicao.getColuna() - 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        // no
        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        // Roque
        if (qteMovimentos == 0 && !partida.getXeque()) {
            // #jogadaespecial roque pequeno
            Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 3);
            if (testeTorreParaRoque(posT1)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
                if (tab.peca(p1) == null && tab.peca(p2) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
                }
            }
            // Roque grande
            Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);
            if (testeTorreParaRoque(posT1)) {
                Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
                Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
                if (tab.peca(p1) == null && tab.peca(p2) == null && tab.peca(p3) == null) {
                    mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
                }
            }
        }

        return mat;
    }
}