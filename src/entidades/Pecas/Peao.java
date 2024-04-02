package entidades.Pecas;

import entidades.*;

public class Peao  extends Peca {
    private final PartidaXadrez partida;

    public Peao(Tabuleiro tab, Cor cor, PartidaXadrez partida) {
        super(tab, cor);
        this.partida = partida;
    }

    @Override
    public String toString() {
        return "P";
    }

    private boolean existeInimigo(Posicao pos) {
        Peca p = tab.peca(pos);
        return p != null && p.getCor() != cor;
    }

    private boolean livre(Posicao pos) {
        return tab.peca(pos) == null;
    }

    @Override
    public boolean[][] movimentosPossiveis() {
        boolean[][] mat = new boolean[tab.getLinhas()][tab.getColunas()];

        Posicao pos = new Posicao(0, 0);

        if (cor == Cor.Branca) {
            pos.definirValores(posicao.getLinha() - 1, posicao.getColuna());
            if (tab.posicaoValida(pos) && livre(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            pos.definirValores(posicao.getLinha() - 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
            if (tab.posicaoValida(p2) && livre(p2) && tab.posicaoValida(pos) && livre(pos) && qteMovimentos == 0) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            pos.definirValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
            if (tab.posicaoValida(pos) && existeInimigo(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            pos.definirValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
            if (tab.posicaoValida(pos) && existeInimigo(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }

            // #jogadaespecial En Passant

            if (posicao.getLinha() == 3) {
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (tab.posicaoValida(esquerda) && existeInimigo(esquerda) && tab.peca(esquerda) == partida.getVulneravelEnPassant()) {
                    mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
                }
            }
            if (posicao.getLinha() == 3) {
                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (tab.posicaoValida(direita) && existeInimigo(direita) && tab.peca(direita) == partida.getVulneravelEnPassant()) {
                    mat[direita.getLinha() - 1][direita.getColuna()] = true;
                }
            }

        } else {
            pos.definirValores(posicao.getLinha() + 1, posicao.getColuna());
            if (tab.posicaoValida(pos) && livre(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            pos.definirValores(posicao.getLinha() + 2, posicao.getColuna());
            Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
            if (tab.posicaoValida(p2) && livre(p2) && tab.posicaoValida(pos) && livre(pos) && qteMovimentos == 0) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            pos.definirValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
            if (tab.posicaoValida(pos) && existeInimigo(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            pos.definirValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
            if (tab.posicaoValida(pos) && existeInimigo(pos)) {
                mat[pos.getLinha()][pos.getColuna()] = true;
            }
            // #jogadaespecial En Passant

            if (posicao.getLinha() == 4) {
                Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
                if (tab.posicaoValida(esquerda) && existeInimigo(esquerda) && tab.peca(esquerda) == partida.getVulneravelEnPassant()) {
                    mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
                }
            }
            if (posicao.getLinha() == 4) {
                Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
                if (tab.posicaoValida(direita) && existeInimigo(direita) && tab.peca(direita) == partida.getVulneravelEnPassant()) {
                    mat[direita.getLinha() + 1][direita.getColuna()] = true;
                }
            }
        }

        return mat;
    }
}
