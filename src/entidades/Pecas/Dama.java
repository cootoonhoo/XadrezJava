package entidades.Pecas;

import entidades.Cor;
import entidades.Peca;
import entidades.Posicao;
import entidades.Tabuleiro;

public class Dama extends Peca {

    public Dama(Tabuleiro tab, Cor cor) {
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

        // esquerda
        pos.setarPosicao(posicao.getLinha(), posicao.getColuna() - 1);
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha(), pos.getColuna() - 1);
        }

        // direita
        pos.setarPosicao(posicao.getLinha(), posicao.getColuna() + 1);
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha(), pos.getColuna() + 1);
        }

        // acima
        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna());
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha() - 1, pos.getColuna());
        }

        // abaixo
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna());
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha() + 1, pos.getColuna());
        }

        // NO
        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha() - 1, pos.getColuna() - 1);
        }

        // NE
        pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha() - 1, pos.getColuna() + 1);
        }

        // SE
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha() + 1, pos.getColuna() + 1);
        }

        // SO
        pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna() - 1);
        while (tab.posicaoValida(pos) && podeMover(pos)) {
            mat[pos.getLinha()][pos.getColuna()] = true;
            if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                break;
            }
            pos.setarPosicao(pos.getLinha() + 1, pos.getColuna() - 1);
        }

        return mat;
    }
}
