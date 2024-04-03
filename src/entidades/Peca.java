package entidades;

public abstract class Peca {
    protected Posicao posicao;
    protected Cor cor;
    protected int qteMovimentos;
    protected Tabuleiro tab;

    public Peca(Tabuleiro tab, Cor cor) {
        this.posicao = null;
        this.tab = tab;
        this.cor = cor;
        this.qteMovimentos = 0;
    }

    public boolean[][] movimentosPossiveis()
    {
        throw new UnsupportedOperationException("A funcao de posicoes possiveis não foi implementada!");
    };

    public boolean existeMovimentosPossiveis() {
        boolean[][] mat = movimentosPossiveis();
        for (int i = 0; i < tab.getLinhas(); i++) {
            for (int j = 0; j < tab.getColunas(); j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean movimentoPossivel(Posicao pos) {
        return movimentosPossiveis()[pos.getLinha()][pos.getColuna()];
    }

    public void incrementarMovimentos() {
        qteMovimentos++;
    }

    public void decrementarMovimentos() {
        qteMovimentos--;
    }

    public void setPosicao(Posicao pos) {
        this.posicao = pos;
    }

    public Cor getCor() {
        return this.cor;
    }

    public Posicao getPosicao() {
        return  this.posicao;
    }
}
