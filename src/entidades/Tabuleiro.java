package entidades;

import exception.XadrezException;

public class Tabuleiro {
    private int linhas;
    private int colunas;
    private Peca[][] pecas;

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public Peca[][] getPecas() {
        return pecas;
    }

    public Tabuleiro(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        pecas = new Peca[linhas][colunas];
    }
    public Peca peca(Posicao pos) {
        return pecas[pos.getLinha()][pos.getColuna()];
    }

    public void colocarPeca(Peca p, Posicao pos) {
        pecas[pos.getLinha()][pos.getColuna()] = p;
        p.setPosicao(pos);
    }

    public Peca retirarPeca(Posicao pos) {
        if (!posicaoValida(pos)) {
            return null;
        }
        if (peca(pos) == null) {
            return null;
        }
        Peca aux = peca(pos);
        aux.setPosicao(null);
        pecas[pos.getLinha()][pos.getColuna()] = null;
        return aux;
    }
    public boolean posicaoValida(Posicao pos) {
        return pos.getLinha() >= 0 && pos.getLinha() < linhas && pos.getColuna() >= 0 && pos.getColuna() < colunas;
    }
}