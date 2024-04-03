import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {
    private JPanel tabuleiroPanel;
    private JLabel[][] casas;
    private boolean turnoBranco = true; // Branco começa

    public GUI() {
        super("Xadrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        tabuleiroPanel = new JPanel(new GridLayout(8, 8));
        casas = new JLabel[8][8];

        montarTabuleiro();
        inicializarPeças();
        add(tabuleiroPanel);
        setVisible(true);
    }

    private void montarTabuleiro() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                casas[i][j] = new JLabel();
                casas[i][j].setOpaque(true);
                casas[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                casas[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                casas[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                tabuleiroPanel.add(casas[i][j]);

                casas[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        JLabel source = (JLabel) e.getSource();
                        if (!source.getText().equals("") && éTurnoCorreto(source.getText())) {
                            JLabel peçaSelecionada = source;
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        JLabel source = (JLabel) e.getSource();
                        JLabel peçaSelecionada = null;
                        if (!source.getText().equals("") && éTurnoCorreto(source.getText())) {
                            peçaSelecionada = source;
                            JLabel destino = (JLabel) tabuleiroPanel.getComponentAt(SwingUtilities.convertPoint(peçaSelecionada, e.getPoint(), tabuleiroPanel));
                            if (destino != peçaSelecionada && destino != null && destino.getText().equals("")) {
                                destino.setText(peçaSelecionada.getText());
                                peçaSelecionada.setText("");
                                turnoBranco = !turnoBranco; // Alternar o turno
                            }
                            peçaSelecionada = null;
                        }
                    }
                });
            }
        }
    }

    private boolean éTurnoCorreto(String peça) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}