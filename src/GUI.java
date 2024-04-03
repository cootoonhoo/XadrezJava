import entidades.*;
import entidades.Pecas.*;
import exception.TabuleiroException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {
    private JPanel tabuleiroPanel;
    private Posicao[][] casas;
    private boolean turnoBranco = true; // Branco começa
    private boolean[][] posicoesPossiveis;
    private static  PartidaXadrez partida;

    public GUI() {
        super("Xadrez");
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("./imagens/black-king.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(logo.getImage());
        setSize(600, 600);

        tabuleiroPanel = new JPanel(new GridLayout(8, 8));
        try {
            PartidaXadrez partida = new PartidaXadrez();
            casas = new Posicao[8][8];
            montarTabuleiro(partida);
            inicializarPeças();
            add(tabuleiroPanel);
            setVisible(true);
        } catch (Exception ignored) {}
    }

    private void montarTabuleiro(PartidaXadrez partidaXadrez) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                casas[i][j] = new Posicao(i,j);
                casas[i][j].setOpaque(true);
                casas[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casas[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                casas[i][j].setFont(new Font("Arial Unicode MS", Font.PLAIN, 40));
                tabuleiroPanel.add(casas[i][j]);
                casas[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Posicao source = (Posicao) e.getSource();
                        if (!source.getText().equals("") && eTurnoCorreto(source.getText())) {
                            Posicao pecaSelecionada = source;
                            try {
                                partida.validarPosicaoDeOrigem(pecaSelecionada);
                                posicoesPossiveis = partida.getTab().peca(pecaSelecionada).movimentosPossiveis();
                                mostrarPossiveisCasas(posicoesPossiveis);

                            } catch (TabuleiroException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Posicao source = (Posicao) e.getSource();
                        Posicao pecaSelecionada = null;
                        if (!source.getText().equals("") && eTurnoCorreto(source.getText())) {
                            pecaSelecionada = source;
                            Posicao destino = (Posicao) tabuleiroPanel.getComponentAt(SwingUtilities.convertPoint(pecaSelecionada, e.getPoint(), tabuleiroPanel));

                            if (destino != pecaSelecionada && destino != null) {

                                try {
                                    partida.validarPosicaoDeDestino(pecaSelecionada, destino);
                                    partida.realizaJogada(pecaSelecionada, destino);
                                    //Vendo se o rei inimigo está em xeque


                                } catch (TabuleiroException ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                                    setCoresClassicasTabuleiro();
                                    return;
                                }

                                destino.setText(pecaSelecionada.getText());
                                pecaSelecionada.setText("");
                                turnoBranco = !turnoBranco; // Alternar o turno
                            }
                            pecaSelecionada = null;
                            setCoresClassicasTabuleiro();
                            if(partida.getXeque())
                            {
                                Peca rei = null;
                                if(turnoBranco)
                                    rei = partida.GetRei(Cor.Branca);
                                else
                                    rei = partida.GetRei(Cor.Preta);

                                MostrarCheque(rei.getPosicao().getLinha(), rei.getPosicao().getColuna());
                            }
                        }
                    }
                });
            }
        }
    }

    private void setCoresClassicasTabuleiro()
    {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                casas[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
    }

    private void mostrarPossiveisCasas(boolean[][] posicoesPossiveis)
    {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(posicoesPossiveis[i][j] == true)
                    casas[i][j].setBackground(new Color(51,153,51));
            }
        }
    }

    private void MostrarCheque(int x, int y)
    {
        casas[x][y].setBackground(new Color(150,0,0));
    }

    private boolean eTurnoCorreto(String peça) {
        // Verifica se a peça é branca e é o turno das brancas ou se a peça é preta e é o turno das pretas
        boolean peçaBranca = peça.matches("[\u2654-\u2659]");
        return peçaBranca == turnoBranco;
    }

    private void inicializarPeças() {
        // Posicionando as peças pretas
        casas[0][0].setText("\u265C"); // Torre
        casas[0][7].setText("\u265C"); // Torre
        casas[0][1].setText("\u265E"); // Cavalo
        casas[0][6].setText("\u265E"); // Cavalo
        casas[0][2].setText("\u265D"); // Bispo
        casas[0][5].setText("\u265D"); // Bispo
        casas[0][3].setText("\u265B"); // Rainha
        casas[0][4].setText("\u265A"); // Rei
        for (int j = 0; j < 8; j++) {
            casas[1][j].setText("\u265F"); // Peões
        }

        // Posicionando as peças brancas
        casas[7][0].setText("\u2656"); // Torre
        casas[7][7].setText("\u2656"); // Torre
        casas[7][1].setText("\u2658"); // Cavalo
        casas[7][6].setText("\u2658"); // Cavalo
        casas[7][2].setText("\u2657"); // Bispo
        casas[7][5].setText("\u2657"); // Bispo
        casas[7][3].setText("\u2655"); // Rainha
        casas[7][4].setText("\u2654"); // Rei
        for (int j = 0; j < 8; j++) {
            casas[6][j].setText("\u2659"); // Peões
        }
    }

    public Peca getPecaType(String uniCode, Tabuleiro tabuleiro, PartidaXadrez partidaXadrez) {
        switch (uniCode) {
            case "\u2654": return new Rei(tabuleiro, Cor.Branca, partidaXadrez);
            case "\u2655": return new Dama(tabuleiro, Cor.Branca);
            case "\u2656": return new Torre(tabuleiro, Cor.Branca);
            case "\u2657": return new Bispo(tabuleiro, Cor.Branca);
            case "\u2658": return new Cavalo(tabuleiro, Cor.Branca);
            case "\u2659": return new Peao(tabuleiro, Cor.Branca, partidaXadrez);
            case "\u265A": return new Rei(tabuleiro, Cor.Preta, partidaXadrez);
            case "\u265B": return new Dama(tabuleiro, Cor.Preta);
            case "\u265C": return new Torre(tabuleiro, Cor.Preta);
            case "\u265D": return new Bispo(tabuleiro, Cor.Preta);
            case "\u265E": return new Cavalo(tabuleiro, Cor.Preta);
            case "\u265F": return new Peao(tabuleiro, Cor.Preta, partidaXadrez);
            default: return null; // Ou lançar uma exceção se for apropriado
        }
    }
    public static void main(String[] args)
    {
        try {
            partida = new PartidaXadrez();
            SwingUtilities.invokeLater(GUI::new);
        } catch (TabuleiroException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
