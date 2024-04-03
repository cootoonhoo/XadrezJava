package entidades;

import entidades.Pecas.*;
import exception.XadrezException;

import java.util.HashSet;
import java.util.Set;

public class GameMannager {
    private Tabuleiro tabuleiro;
    private Cor jogadorAtual;
    private boolean terminada;
    private boolean xeque;
    private Peca piaoVulneravelAoEnPassant;
    private Posicao posAnteriorTorreRoque;
    private Set<Peca> pecas;
    private Set<Peca> capturadas;

    public Peca getPiaoVulneravelAoEnPassant() {
        return piaoVulneravelAoEnPassant;
    }

    public Posicao getPosAnteriorTorreRoque()
    {
        return  posAnteriorTorreRoque;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public boolean isTerminada() {
        return terminada;
    }

    public boolean getXeque() {
        return xeque;
    }

    public GameMannager() throws XadrezException {
        tabuleiro = new Tabuleiro(8, 8);
        jogadorAtual = Cor.Branca;
        terminada = false;
        xeque = false;
        piaoVulneravelAoEnPassant = null;
        pecas = new HashSet<>();
        capturadas = new HashSet<>();
        gerarTabuleiro();
    }

    public Peca executaMovimento(Posicao origem, Posicao destino) throws XadrezException {
        Peca p = tabuleiro.retirarPeca(origem);
        p.incrementarMovimentos();
        Peca pecaCapturada = tabuleiro.retirarPeca(destino);
        tabuleiro.colocarPeca(p, destino);
        if (pecaCapturada != null) {
            capturadas.add(pecaCapturada);
        }
        posAnteriorTorreRoque = null;

        // Roque pequeno
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            Peca T = tabuleiro.retirarPeca(origemT);
            T.incrementarMovimentos();
            tabuleiro.colocarPeca(T, destinoT);
            posAnteriorTorreRoque = T.getPosicao();
        }

        // Roque grande
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            Peca T = tabuleiro.retirarPeca(origemT);
            T.incrementarMovimentos();
            tabuleiro.colocarPeca(T, destinoT);
            posAnteriorTorreRoque = T.getPosicao();
        }

        // En passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
                Posicao posP;
                if (p.getCor() == Cor.Branca) {
                    posP = new Posicao(destino.getLinha() + 1, destino.getColuna());
                } else {
                    posP = new Posicao(destino.getLinha() - 1, destino.getColuna());
                }
                pecaCapturada = tabuleiro.retirarPeca(posP);
                capturadas.add(pecaCapturada);
            }
        }

        return pecaCapturada;
    }

    public void desfazMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) throws XadrezException {
        Peca p = tabuleiro.retirarPeca(destino);
        p.decrementarMovimentos();
        if (pecaCapturada != null) {
            tabuleiro.colocarPeca(pecaCapturada, destino);
            capturadas.remove(pecaCapturada);
        }
        tabuleiro.colocarPeca(p, origem);

        // roque pequeno
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
            Peca T = tabuleiro.retirarPeca(origemT);
            T.decrementarMovimentos();
            tabuleiro.colocarPeca(T, destinoT);
        }

        // roque grande
        if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
            Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
            Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
            Peca T = tabuleiro.retirarPeca(origemT);
            T.decrementarMovimentos();
            tabuleiro.colocarPeca(T, destinoT);
        }

        // En passant
        if (p instanceof Peao) {
            if (origem.getColuna() != destino.getColuna() && pecaCapturada == piaoVulneravelAoEnPassant) {
                Peca peao = tabuleiro.retirarPeca(destino);
                Posicao posP;
                if (p.getCor() == Cor.Branca) {
                    posP = new Posicao(3, destino.getColuna());
                } else {
                    posP = new Posicao(4, destino.getColuna());
                }
                tabuleiro.colocarPeca(peao, posP);
            }
        }
    }

    public void fazerMovimento(Posicao origem, Posicao destino) throws XadrezException {
        Peca pecaCapturada = executaMovimento(origem, destino);
        if (estaEmXeque(jogadorAtual)) {
            desfazMovimento(origem, destino, pecaCapturada);
            throw new XadrezException("Você não pode se colocar em xeque!");
        }

        Peca p = tabuleiro.peca(destino);

        // Promocao do Piao
        if (p instanceof Peao) {
            if ((p.getCor() == Cor.Branca && destino.getLinha() == 0) || (p.getCor() == Cor.Preta && destino.getLinha() == 7)) {
                p = tabuleiro.retirarPeca(destino);
                pecas.remove(p);
                Peca dama = new Dama(tabuleiro, p.getCor());
                tabuleiro.colocarPeca(dama, destino);
                pecas.add(dama);
            }
        }

        xeque = estaEmXeque(JogadorAdversario(jogadorAtual));
        if (validaXequeMate(JogadorAdversario(jogadorAtual))) {
            terminada = true;
        } else {
            mudarDeTurno();
        }

        // En Passant
        if (p instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
            piaoVulneravelAoEnPassant = p;
        } else {
            piaoVulneravelAoEnPassant = null;
        }
    }

    public boolean validaXequeMate(Cor cor) throws XadrezException {
        if (!estaEmXeque(cor)) {
            return false;
        }
        for (Peca x : pecasEmJogo(cor)) {
            boolean[][] mat = x.movimentosPossiveis();
            for (int i = 0; i < tabuleiro.getLinhas(); i++) {
                for (int j = 0; j < tabuleiro.getColunas(); j++) {
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
    public Peca getReiEmJogo(Cor cor) {
        for (Peca x : pecasEmJogo(cor)) {
            if (x instanceof Rei) {
                return x;
            }
        }
        return null;
    }

    public boolean estaEmXeque(Cor cor) {
        Peca Rei = getReiEmJogo(cor);
        for (Peca x : pecasEmJogo(JogadorAdversario(cor))) {
            boolean[][] mat = x.movimentosPossiveis();
            if (mat[Rei.getPosicao().getLinha()][Rei.getPosicao().getColuna()]) {
                return true;
            }
        }
        return false;
    }

    private Cor JogadorAdversario(Cor cor) {
        if (cor == Cor.Branca) {
            return Cor.Preta;
        } else {
            return Cor.Branca;
        }
    }

    private void mudarDeTurno() {
        if (jogadorAtual == Cor.Branca) {
            jogadorAtual = Cor.Preta;
        } else {
            jogadorAtual = Cor.Branca;
        }
    }

    public void validarPosicaoDeOrigem(Posicao pos) throws XadrezException {
        if (!tabuleiro.peca(pos).existeMovimentosPossiveis()) {
            throw new XadrezException("Não há movimentos possíveis para a peça de origem escolhida!");
        }
    }

    public void validarPosicaoDeDestino(Posicao origem, Posicao destino) throws XadrezException {
        if (!tabuleiro.peca(origem).movimentoPossivel(destino)) {
            throw new XadrezException("Posição de destino inválida!");
        }
    }

    public void instanciarPeca(char coluna, int linha, Peca peca) throws XadrezException {
        tabuleiro.colocarPeca(peca, new Posicao(8 - linha,coluna - 'a')); //Convertendo coordenadas em xadrez para entidade Posicao
        pecas.add(peca);
    }

    private void gerarTabuleiro() throws XadrezException {
        instanciarPeca('a', 1, new Torre(tabuleiro, Cor.Branca));
        instanciarPeca('b', 1, new Cavalo(tabuleiro, Cor.Branca));
        instanciarPeca('c', 1, new Bispo(tabuleiro, Cor.Branca));
        instanciarPeca('d', 1, new Dama(tabuleiro, Cor.Branca));
        instanciarPeca('e', 1, new Rei(tabuleiro, Cor.Branca, this));
        instanciarPeca('f', 1, new Bispo(tabuleiro, Cor.Branca));
        instanciarPeca('g', 1, new Cavalo(tabuleiro, Cor.Branca));
        instanciarPeca('h', 1, new Torre(tabuleiro, Cor.Branca));
        instanciarPeca('a', 2, new Peao(tabuleiro, Cor.Branca, this));
        instanciarPeca('b', 2, new Peao(tabuleiro, Cor.Branca, this));
        instanciarPeca('c', 2, new Peao(tabuleiro, Cor.Branca, this));
        instanciarPeca('d', 2, new Peao(tabuleiro, Cor.Branca, this));
        instanciarPeca('e', 2, new Peao(tabuleiro, Cor.Branca, this));
        instanciarPeca('f', 2, new Peao(tabuleiro, Cor.Branca, this));
        instanciarPeca('g', 2, new Peao(tabuleiro, Cor.Branca, this));
        instanciarPeca('h', 2, new Peao(tabuleiro, Cor.Branca, this));

        instanciarPeca('a', 8, new Torre(tabuleiro, Cor.Preta));
        instanciarPeca('b', 8, new Cavalo(tabuleiro, Cor.Preta));
        instanciarPeca('c', 8, new Bispo(tabuleiro, Cor.Preta));
        instanciarPeca('d', 8, new Dama(tabuleiro, Cor.Preta));
        instanciarPeca('e', 8, new Rei(tabuleiro, Cor.Preta, this));
        instanciarPeca('f', 8, new Bispo(tabuleiro, Cor.Preta));
        instanciarPeca('g', 8, new Cavalo(tabuleiro, Cor.Preta));
        instanciarPeca('h', 8, new Torre(tabuleiro, Cor.Preta));
        instanciarPeca('a', 7, new Peao(tabuleiro, Cor.Preta, this));
        instanciarPeca('b', 7, new Peao(tabuleiro, Cor.Preta, this));
        instanciarPeca('c', 7, new Peao(tabuleiro, Cor.Preta, this));
        instanciarPeca('d', 7, new Peao(tabuleiro, Cor.Preta, this));
        instanciarPeca('e', 7, new Peao(tabuleiro, Cor.Preta, this));
        instanciarPeca('f', 7, new Peao(tabuleiro, Cor.Preta, this));
        instanciarPeca('g', 7, new Peao(tabuleiro, Cor.Preta, this));
        instanciarPeca('h', 7, new Peao(tabuleiro, Cor.Preta, this));
    }
}
