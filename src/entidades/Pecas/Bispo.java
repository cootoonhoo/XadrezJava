package entidades.Pecas;

import entidades.Cor;
import entidades.Peca;
import entidades.Posicao;
import entidades.Tabuleiro;

public class Bispo extends Peca {

        public Bispo(Tabuleiro tab, Cor cor) {
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
            pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() - 1);
            while (tab.posicaoValida(pos) && podeMover(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
                if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                    break;
                }
                pos.setarPosicao(pos.getLinha() - 1, pos.getColuna() - 1);
            }
            pos.setarPosicao(posicao.getLinha() - 1, posicao.getColuna() + 1);
            while (tab.posicaoValida(pos) && podeMover(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
                if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                    break;
                }
                pos.setarPosicao(pos.getLinha() - 1, pos.getColuna() + 1);
            }
            pos.setarPosicao(posicao.getLinha() + 1, posicao.getColuna() + 1);
            while (tab.posicaoValida(pos) && podeMover(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
                if (tab.peca(pos) != null && tab.peca(pos).getCor() != cor) {
                    break;
                }
                pos.setarPosicao(pos.getLinha() + 1, pos.getColuna() + 1);
            }
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
