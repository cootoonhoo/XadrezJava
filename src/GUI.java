import entidades.*;
import entidades.Pecas.*;
import exception.XadrezException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {
    private JPanel tabuleiroPanel;
    private Posicao[][] casas;
    private boolean turnoBranco = true; // Branco começa
    private boolean[][] posicoesPossiveis;
    private static GameMannager partida;

    public GUI() {
        super("Xadrez");
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("./imagens/black-king.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(logo.getImage());
        setSize(600, 600);

        tabuleiroPanel = new JPanel(new GridLayout(8, 8));
        try {
            GameMannager partida = new GameMannager();
            casas = new Posicao[8][8];
            montarTabuleiro(partida);
            inicializarPecas();
            add(tabuleiroPanel);
            setVisible(true);
        } catch (Exception ex)
        {
            // Casos em que o erro não foi previsto
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Aviso", JOptionPane.ERROR);
        }
    }

    private void montarTabuleiro(GameMannager gameMannager) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                casas[i][j] = new Posicao(i,j);
                casas[i][j].setOpaque(true);
                casas[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casas[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                casas[i][j].setFont(new Font("Arial Unicode MS", Font.PLAIN, 55));
                tabuleiroPanel.add(casas[i][j]);
                casas[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        Posicao source = (Posicao) e.getSource();
                        if (!source.getText().equals("") && TurnoCorreto(source.getText())) {
                            Posicao pecaSelecionada = source;
                            try {
                                partida.validarPosicaoDeOrigem(pecaSelecionada);
                                posicoesPossiveis = partida.getTabuleiro().peca(pecaSelecionada).movimentosPossiveis();
                                mostrarPossiveisCasas(posicoesPossiveis);

                            } catch (XadrezException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Posicao source = (Posicao) e.getSource();
                        Posicao pecaSelecionada = null;
                        if (!source.getText().equals("") && TurnoCorreto(source.getText())) {
                            pecaSelecionada = source;
                            Posicao destino = (Posicao) tabuleiroPanel.getComponentAt(SwingUtilities.convertPoint(pecaSelecionada, e.getPoint(), tabuleiroPanel));

                            if (destino != pecaSelecionada && destino != null) {

                                try {
                                    // Pegando peça para validação da promoção
                                    Peca getPecaSelecionada = partida.getTabuleiro().getPecas()[pecaSelecionada.getLinha()][ pecaSelecionada.getColuna()];
                                    partida.validarPosicaoDeDestino(pecaSelecionada, destino);
                                    partida.fazerMovimento(pecaSelecionada, destino);

                                    // Validar as jogadas especiais!
                                    // Roque
                                    Posicao torre = partida.getPosAnteriorTorreRoque();
                                    if(torre != null)
                                    {
                                        Cor corTurno = turnoBranco? Cor.Branca: Cor.Preta;
                                        String uniPeca = turnoBranco? "\u2656" : "\u265C";
                                        Peca rei = null;
                                            rei = partida.getReiEmJogo(corTurno);
                                            if(rei.getPosicao().getColuna() > torre.getColuna())
                                            {
                                                //Roque pequeno
                                                casas[rei.getPosicao().getLinha()][rei.getPosicao().getColuna() + 1].setText("");
                                                casas[rei.getPosicao().getLinha()][rei.getPosicao().getColuna() - 1].setText(uniPeca);
                                            }
                                            else
                                            {
                                                //Roque Grande
                                                casas[rei.getPosicao().getLinha()][rei.getPosicao().getColuna() - 2].setText("");
                                                casas[rei.getPosicao().getLinha()][rei.getPosicao().getColuna() + 1].setText(uniPeca);
                                            }
                                    }


                                    Peca getPecaDestino = partida.getTabuleiro().getPecas()[destino.getLinha()][ destino.getColuna()];
                                    if(getPecaSelecionada instanceof Peao)
                                    {
                                        // Promoção
                                        if((getPecaSelecionada.getCor() == Cor.Branca && destino.getLinha() == 0) || (getPecaSelecionada.getCor() == Cor.Preta && destino.getLinha() == 7))
                                        {
                                            if(getPecaDestino.getCor() ==  Cor.Branca)
                                                pecaSelecionada.setText("\u2655");
                                            else
                                                pecaSelecionada.setText("\u265B");
                                        }
                                        // En Passant
                                        if(destino.getColuna() != pecaSelecionada.getColuna() && destino.getText() == "")
                                        {
                                            // Nesse caso o movimento de mudar de coluna do pião é possivel e a casa pra que ele vai é vazia. Logo isso é um en passant;
                                            if(getPecaSelecionada.getCor() == Cor.Branca)
                                                casas[destino.getLinha()+1][destino.getColuna()].setText("");
                                            else
                                                casas[destino.getLinha()-1][destino.getColuna()].setText("");
                                        }
                                    }
                                } catch (XadrezException ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                                    setCoresClassicasTabuleiro();
                                    return;
                                } catch (Exception ex)
                                {
                                    // Casos em que o erro não foi previsto
                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro não esperado", JOptionPane.ERROR);
                                    dispose();
                                }

                                destino.setText(pecaSelecionada.getText());
                                pecaSelecionada.setText("");
                                turnoBranco = !turnoBranco; // Alternar o turno
                            }
                            pecaSelecionada = null;
                            setCoresClassicasTabuleiro();

                            //Vendo se o rei inimigo está em xeque
                            // OBS: O turno é alterado caso o xeque ocorra. Por isso quando turnoBranco for true, mudaremos a cor da casa do rei das brancas
                            if(partida.getXeque())
                            {
                                Peca rei = null;
                                if(turnoBranco)
                                    rei = partida.getReiEmJogo(Cor.Branca);
                                else
                                    rei = partida.getReiEmJogo(Cor.Preta);
                                MostrarCheque(rei.getPosicao().getLinha(), rei.getPosicao().getColuna());

                                if(partida.isTerminada())
                                {
                                    // Mesma lógica do xeque aqui, aplicada para o mate.
                                    if(turnoBranco)
                                        JOptionPane.showMessageDialog(null, "As Pretas venceram o jogo!", "Xeque-Mate!", JOptionPane.INFORMATION_MESSAGE);
                                    else
                                        JOptionPane.showMessageDialog(null, "As Brancas venceram o jogo!", "Xeque-Mate!", JOptionPane.INFORMATION_MESSAGE);
                                    dispose();
                                }
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

    private boolean TurnoCorreto(String peca) {
        // Verifica se a peça é branca e é o turno das brancas ou se a peça é preta e é o turno das pretas
        boolean pecaBranca = peca.matches("[\u2654-\u2659]");
        return pecaBranca == turnoBranco;
    }

    private void inicializarPecas() {
        // Posicionando as peças pretas
        casas[0][0].setText("\u265C"); // Torre ♜
        casas[0][7].setText("\u265C"); // Torre ♜
        casas[0][1].setText("\u265E"); // Cavalo ♞
        casas[0][6].setText("\u265E"); // Cavalo ♞
        casas[0][2].setText("\u265D"); // Bispo ♝
        casas[0][5].setText("\u265D"); // Bispo ♝
        casas[0][3].setText("\u265B"); // Rainha ♛
        casas[0][4].setText("\u265A"); // Rei ♚
        for (int j = 0; j < 8; j++) {
            casas[1][j].setText("\u265F"); // Peões ♟
        }

        // Posicionando as peças brancas
        casas[7][0].setText("\u2656"); // Torre ♖
        casas[7][7].setText("\u2656"); // Torre ♖
        casas[7][1].setText("\u2658"); // Cavalo ♘
        casas[7][6].setText("\u2658"); // Cavalo ♘
        casas[7][2].setText("\u2657"); // Bispo ♗
        casas[7][5].setText("\u2657"); // Bispo ♗
        casas[7][3].setText("\u2655"); // Rainha ♕
        casas[7][4].setText("\u2654"); // Rei ♔
        for (int j = 0; j < 8; j++) {
            casas[6][j].setText("\u2659"); // Peões ♙
        }
    }
    public static void main(String[] args)
    {
        try {
            partida = new GameMannager();
            SwingUtilities.invokeLater(GUI::new);
        } catch (XadrezException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex)
        {
            // Casos em que o erro não foi previsto
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro não esperado", JOptionPane.ERROR);
            return;
        }
    }
}
