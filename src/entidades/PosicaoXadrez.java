package entidades;

public class PosicaoXadrez {
    private char coluna;
    private int linha;

    public PosicaoXadrez(char coluna, int linha) {
        this.coluna = coluna;
        this.linha = linha;
    }

    public Posicao toPosicao() {
        return new Posicao(8 - linha, coluna - 'a');
    }

    @Override
    public String toString() {
        return "" + coluna + linha;
    }
}
