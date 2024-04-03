package entidades.Pecas;

import entidades.Cor;
import entidades.Peca;
import entidades.Posicao;
import entidades.Tabuleiro;

public class Cavalo extends Peca {

    public Cavalo(Tabuleiro tab, Cor cor) {
        super(tab, cor);
    }

    private boolean podeMover(Posicao pos) {
        Peca p = tab.peca(pos);
        return p == null || p.getCor() != cor;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[tab.getLinhas()][tab.getColunas()];

        Posicao pos = new Posicao(0, 0);

        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() - 2);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        pos.setarPosicao(posicao.getLinha() - 2, posicao.getColuna() - 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        pos.setarPosicao(posicao.getLinha() - 2, posicao.getColuna() + 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() + 2);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna() + 2);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        pos.setarPosicao(posicao.getLinha() + 2, posicao.getColuna() + 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        pos.setarPosicao(posicao.getLinha() + 2, posicao.getColuna() - 1);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna() - 2);
        if (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
        }

        return mat;
    }
}
