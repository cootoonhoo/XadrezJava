package entidades;

import entidades.Pecas.*;
import exception.TabuleiroException;

import java.util.HashSet;
import java.util.Set;

public class PartidaXadrez {
    private Tabuleiro tab;
    private int turno;
    private Cor jogadorAtual;
    private boolean terminada;
    private boolean xeque;
    private Peca vulneravelEnPassant;
    private Set<Peca> pecas;
    private Set<Peca> capturadas;

    public Peca getVulneravelEnPassant() {
        return vulneravelEnPassant;
    }

    public boolean getXeque() {
        return xeque;
    }

    public PartidaXadrez() throws TabuleiroException {
        tab = new Tabuleiro(8, 8);
        turno = 1;
        jogadorAtual = Cor.Branca;
        terminada = false;
        xeque = false;
        vulneravelEnPassant = null;
        pecas = new HashSet<>();
        capturadas = new HashSet<>();
        colocarPecas();
    }

    public Peca executaMovimento(Posicao origem, Posicao destino) throws TabuleiroException {
        Peca p = tab.retirarPeca(origem);
        p.incrementarQteMovimentos();
        Peca pecaCapturada = tab.retirarPeca(destino);
        tab.colocarPeca(p, destino);
        if (pecaCapturada != null) {
            capturadas.add(pecaCapturada);
        }

        // #jogadaespecial roque pequeno
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            Peca T = tab.retirarPeca(origemT);
            T.incrementarQteMovimentos();
            tab.colocarPeca(T, destinoT);
        }

        // #jogadaespecial roque grande
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            Peca T = tab.retirarPeca(origemT);
            T.incrementarQteMovimentos();
            tab.colocarPeca(T, destinoT);
        }

        // #jogadaespecial En passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
                Posicao posP;
                if (p.getCor() == Cor.Branca) {
                    posP = new Posicao(destino.getLinha() + 1, destino.getColuna());
                } else {
                    posP = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tab.retirarPeca(posP);
                capturadas.add(pecaCapturada);
            }
        }

        return pecaCapturada;
    }

    public void desfazMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) throws TabuleiroException {
        Peca p = tab.retirarPeca(destino);
        p.decrementarQteMovimentos();
        if (pecaCapturada != null) {
            tab.colocarPeca(pecaCapturada, destino);
            capturadas.remove(pecaCapturada);
        }
        tab.colocarPeca(p, origem);

        // roque pequeno
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            Peca T = tab.retirarPeca(origemT);
            T.decrementarQteMovimentos();
            tab.colocarPeca(T, destinoT);
        }

        // roque grande
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            Peca T = tab.retirarPeca(origemT);
            T.decrementarQteMovimentos();
            tab.colocarPeca(T, destinoT);
        }

        // En passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == vulneravelEnPassant) {
                Peca peao = tab.retirarPeca(destino);
                Posicao posP;
                if (p.getCor() == Cor.Branca) {
                    posP = new Posicao(3, destino.getColuna());
                } else {
                    posP = new Posicao(4, destino.getColuna());
                }
                tab.colocarPeca(peao, posP);
            }
        }
    }

    public void realizaJogada(Posicao origem, Posicao destino) throws TabuleiroException {
        Peca pecaCapturada = executaMovimento(origem, destino);
        if (estaEmXeque(jogadorAtual)) {
            desfazMovimento(origem, destino, pecaCapturada);
            throw new TabuleiroException("Você não pode se colocar em xeque!");
        }

        Peca p = tab.peca(destino);

        // #jogadaespecial promocao
        if (p instanceof Peao) {
            if ((p.getCor() == Cor.Branca && destino.getLinha() == 0) || (p.getCor() == Cor.Preta && destino.getLinha() == 7)) {
                p = tab.retirarPeca(destino);
                pecas.remove(p);
                Peca dama = new Dama(tab, p.getCor());
                tab.colocarPeca(dama, destino);
                pecas.add(dama);
            }
        }

        if (estaEmXeque(adversaria(jogadorAtual))) {
            xeque = true;
        } else {
            xeque = false;
        }
        if (testeXequemate(adversaria(jogadorAtual))) {
            terminada = true;
        } else {
            turno++;
            mudaJogador();
        }

        // #jogadaespecial En Passant
        if (p instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
            vulneravelEnPassant = p;
        } else {
            vulneravelEnPassant = null;
        }
    }

    public boolean testeXequemate(Cor cor) throws TabuleiroException {
        if (!estaEmXeque(cor)) {
            return false;
        }
        for (Peca x : pecasEmJogo(cor)) {
            boolean[][] mat = x.movimentosPossiveis();
            for (int i = 0; i < tab.getLinhas(); i++) {
                for (int j = 0; j < tab.getColunas(); j++) {
                    if (mat[i][j]) {
                        Posicao origem = x.getPosicao();
                        Posicao destino = new Posicao(i, j);
                        Peca pecaCapturada = executaMovimento(origem, destino);
                        boolean testeXeque = estaEmXeque(cor);
                        desfazMovimento(origem, destino, pecaCapturada);
                        if (!testeXeque) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public HashSet<Peca> pecasEmJogo(Cor cor) {
        HashSet<Peca> aux = new HashSet<>();
        for (Peca x : pecas) {
            if (x.getCor() == cor) {
                aux.add(x);
            }
        }
        aux.removeAll(pecasCapturadas(cor));
        return aux;
    }

    public HashSet<Peca> pecasCapturadas(Cor cor) {
        HashSet<Peca> aux = new HashSet<>();
        for (Peca x : capturadas) {
            if (x.getCor() == cor) {
                aux.add(x);
            }
        }
        return aux;
    }
    public Peca GetRei(Cor cor) {
        for (Peca x : pecasEmJogo(cor)) {
            if (x instanceof Rei) {
                return x;
            }
        }
        return null;
    }

    public boolean estaEmXeque(Cor cor) throws TabuleiroException {
        Peca R = GetRei(cor);
        if (R == null) {
            throw new TabuleiroException("Não tem rei da cor " + cor + " no tabuleiro!");
        }
        for (Peca x : pecasEmJogo(adversaria(cor))) {
            boolean[][] mat = x.movimentosPossiveis();
            if (mat[R.getPosicao().getLinha()][R.getPosicao().getColuna()]) {
                return true;
            }
        }
        return false;
    }

    private Cor adversaria(Cor cor) {
        if (cor == Cor.Branca) {
            return Cor.Preta;
        } else {
            return Cor.Branca;
        }
    }

    private void mudaJogador() {
        if (jogadorAtual == Cor.Branca) {
            jogadorAtual = Cor.Preta;
        } else {
            jogadorAtual = Cor.Branca;
        }
    }

    public void validarPosicaoDeOrigem(Posicao pos) throws TabuleiroException {
        if (tab.peca(pos) == null) {
            throw new TabuleiroException("Não existe peça na posição de origem escolhida!");
        }
        if (jogadorAtual != tab.peca(pos).getCor()) {
            throw new TabuleiroException("A peça de origem escolhida não é sua!");
        }
        if (!tab.peca(pos).existeMovimentosPossiveis()) {
            throw new TabuleiroException("Não há movimentos possíveis para a peça de origem escolhida!");
        }
    }


    public void validarPosicaoDeDestino(Posicao origem, Posicao destino) throws TabuleiroException {
        if (!tab.peca(origem).movimentoPossivel(destino)) {
            throw new TabuleiroException("Posição de destino inválida!");
        }
    }

    public void colocarNovaPeca(char coluna, int linha, Peca peca) throws TabuleiroException {
        tab.colocarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
        pecas.add(peca);
    }

    private void colocarPecas() throws TabuleiroException {
        colocarNovaPeca('a', 1, new Torre(tab, Cor.Branca));
        colocarNovaPeca('b', 1, new Cavalo(tab, Cor.Branca));
        colocarNovaPeca('c', 1, new Bispo(tab, Cor.Branca));
        colocarNovaPeca('d', 1, new Dama(tab, Cor.Branca));
        colocarNovaPeca('e', 1, new Rei(tab, Cor.Branca, this));
        colocarNovaPeca('f', 1, new Bispo(tab, Cor.Branca));
        colocarNovaPeca('g', 1, new Cavalo(tab, Cor.Branca));
        colocarNovaPeca('h', 1, new Torre(tab, Cor.Branca));
        colocarNovaPeca('a', 2, new Peao(tab, Cor.Branca, this));
        colocarNovaPeca('b', 2, new Peao(tab, Cor.Branca, this));
        colocarNovaPeca('c', 2, new Peao(tab, Cor.Branca, this));
        colocarNovaPeca('d', 2, new Peao(tab, Cor.Branca, this));
        colocarNovaPeca('e', 2, new Peao(tab, Cor.Branca, this));
        colocarNovaPeca('f', 2, new Peao(tab, Cor.Branca, this));
        colocarNovaPeca('g', 2, new Peao(tab, Cor.Branca, this));
        colocarNovaPeca('h', 2, new Peao(tab, Cor.Branca, this));

        colocarNovaPeca('a', 8, new Torre(tab, Cor.Preta));
        colocarNovaPeca('b', 8, new Cavalo(tab, Cor.Preta));
        colocarNovaPeca('c', 8, new Bispo(tab, Cor.Preta));
        colocarNovaPeca('d', 8, new Dama(tab, Cor.Preta));
        colocarNovaPeca('e', 8, new Rei(tab, Cor.Preta, this));
        colocarNovaPeca('f', 8, new Bispo(tab, Cor.Preta));
        colocarNovaPeca('g', 8, new Cavalo(tab, Cor.Preta));
        colocarNovaPeca('h', 8, new Torre(tab, Cor.Preta));
        colocarNovaPeca('a', 7, new Peao(tab, Cor.Preta, this));
        colocarNovaPeca('b', 7, new Peao(tab, Cor.Preta, this));
        colocarNovaPeca('c', 7, new Peao(tab, Cor.Preta, this));
        colocarNovaPeca('d', 7, new Peao(tab, Cor.Preta, this));
        colocarNovaPeca('e', 7, new Peao(tab, Cor.Preta, this));
        colocarNovaPeca('f', 7, new Peao(tab, Cor.Preta, this));
        colocarNovaPeca('g', 7, new Peao(tab, Cor.Preta, this));
        colocarNovaPeca('h', 7, new Peao(tab, Cor.Preta, this));
    }

    public Tabuleiro getTab() {
        return tab;
    }

    public boolean isTerminada() {
        return terminada;
    }

    public int getTurno() {
        return  turno;
    }

    public String getJogadorAtual() {
        return this.jogadorAtual.toString();
    }
}
