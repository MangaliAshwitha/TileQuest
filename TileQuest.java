import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class TileQuest extends JFrame {

    private static final String GAME_TITLE = "TileQuest";
    private static final String TAGLINE = "Slide the tiles, conquer the quest!";
    private static final Color BUTTON_COLOR = new Color(255, 215, 0); // Gold
    private static final Color BUTTON_HOVER_COLOR = new Color(255, 225, 50);
    private static final Color BACKGROUND_COLOR = new Color(44, 62, 80); // Dark Blue-Gray

    private int SIZE = 4;
    private JButton[][] buttons;
    private int emptyRow, emptyCol;
    private JLabel moveLabel;
    private int moveCount;
    private JPanel gridPanel;

    public TileQuest() {
        showStartScreen();
    }

    private void showStartScreen() {
        JFrame startFrame = new JFrame(GAME_TITLE);
        startFrame.setSize(800, 600);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLocationRelativeTo(null);
        startFrame.getContentPane().setBackground(BACKGROUND_COLOR);
        startFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel titleLabel = new JLabel(GAME_TITLE, JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 80));
        titleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(20, 10, 0, 10);
        startFrame.add(titleLabel, gbc);

        JLabel taglineLabel = new JLabel(TAGLINE, JLabel.CENTER);
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        taglineLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 10, 100, 10);
        startFrame.add(taglineLabel, gbc);

        JButton startButton = createStyledButton("Start Quest");
        startButton.addActionListener(e -> {
            startFrame.dispose();
            showSizeSelectionScreen();
        });
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        startFrame.add(startButton, gbc);

        startFrame.setVisible(true);
    }

    private void showSizeSelectionScreen() {
        JFrame sizeFrame = new JFrame("Select Puzzle Size");
        sizeFrame.setSize(800, 600);
        sizeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sizeFrame.setLocationRelativeTo(null);
        sizeFrame.getContentPane().setBackground(BACKGROUND_COLOR);
        sizeFrame.setLayout(new GridBagLayout());

        JLabel selectLabel = new JLabel("Choose a puzzle size:");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 24));
        selectLabel.setForeground(Color.WHITE);

        JButton btn3x3 = createStyledButton("3x3");
        JButton btn4x4 = createStyledButton("4x4");
        JButton btn5x5 = createStyledButton("5x5");

        btn3x3.addActionListener(e -> {
            SIZE = 3;
            sizeFrame.dispose();
            setupGameUI();
        });
        btn4x4.addActionListener(e -> {
            SIZE = 4;
            sizeFrame.dispose();
            setupGameUI();
        });
        btn5x5.addActionListener(e -> {
            SIZE = 5;
            sizeFrame.dispose();
            setupGameUI();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        sizeFrame.add(selectLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        sizeFrame.add(btn3x3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        sizeFrame.add(btn4x4, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        sizeFrame.add(btn5x5, gbc);

        sizeFrame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private void setupGameUI() {
        getContentPane().removeAll(); // REMOVE old components
        setTitle(GAME_TITLE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(BACKGROUND_COLOR);

        moveLabel = new JLabel("Moves: 0");
        moveLabel.setFont(new Font("Arial", Font.BOLD, 20));
        moveLabel.setForeground(Color.WHITE);

        JButton restartButton = createStyledButton("Restart");
        restartButton.addActionListener(e -> initPuzzle());

        JButton menuButton = createStyledButton("Menu");
        menuButton.addActionListener(e -> {
            this.dispose();
            showSizeSelectionScreen();
        });

        controlPanel.add(moveLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        controlPanel.add(restartButton);
        controlPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        controlPanel.add(menuButton);

        add(controlPanel, BorderLayout.SOUTH);

        gridPanel = new JPanel(); // NEW panel for the grid
        gridPanel.setBackground(BACKGROUND_COLOR);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(gridPanel, BorderLayout.CENTER);

        initPuzzle(); // Initialize puzzle

        revalidate();
        repaint();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initPuzzle() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < SIZE * SIZE; i++) numbers.add(i);
        numbers.add(0);
        Collections.shuffle(numbers);

        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE, 5, 5));
        buttons = new JButton[SIZE][SIZE];
        moveCount = 0;
        moveLabel.setText("Moves: 0");

        int index = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int num = numbers.get(index++);
                JButton btn = new JButton(num == 0 ? "" : String.valueOf(num));
                btn.setFont(new Font("Arial", Font.BOLD, 36));
                btn.setBackground(num == 0 ? new Color(60, 60, 60) : Color.LIGHT_GRAY);
                btn.setForeground(num == 0 ? Color.WHITE : Color.BLACK);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createRaisedBevelBorder());
                btn.addActionListener(e -> handleMove((JButton) e.getSource()));
                buttons[row][col] = btn;
                gridPanel.add(btn);

                if (num == 0) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void handleMove(JButton clicked) {
        Point clickedPos = getButtonPosition(clicked);

        if (clickedPos != null && isAdjacent(clickedPos.x, clickedPos.y, emptyRow, emptyCol)) {
            JButton emptyBtn = buttons[emptyRow][emptyCol];

            buttons[emptyRow][emptyCol] = clicked;
            buttons[clickedPos.x][clickedPos.y] = emptyBtn;

            gridPanel.removeAll();
            for (int row = 0; row < SIZE; row++)
                for (int col = 0; col < SIZE; col++)
                    gridPanel.add(buttons[row][col]);

            gridPanel.revalidate();
            gridPanel.repaint();

            emptyRow = clickedPos.x;
            emptyCol = clickedPos.y;

            moveCount++;
            moveLabel.setText("Moves: " + moveCount);

            if (checkWin()) {
                this.dispose();
                showWinScreen();
            }
        }
    }

    private Point getButtonPosition(JButton btn) {
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                if (buttons[row][col] == btn) return new Point(row, col);
        return null;
    }

    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        return (Math.abs(row1 - row2) == 1 && col1 == col2) ||
               (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    private boolean checkWin() {
        int count = 1;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String text = buttons[row][col].getText();
                if (row == SIZE - 1 && col == SIZE - 1) {
                    if (!text.equals("")) return false;
                } else {
                    if (!text.equals(String.valueOf(count++))) return false;
                }
            }
        }
        return true;
    }

    private void showWinScreen() {
        JFrame winFrame = new JFrame("Puzzle Solved!");
        winFrame.setSize(800, 600);
        winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        winFrame.setLocationRelativeTo(null);
        winFrame.getContentPane().setBackground(BACKGROUND_COLOR);
        winFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel congratsLabel = new JLabel("Congratulations!", JLabel.CENTER);
        congratsLabel.setFont(new Font("Serif", Font.BOLD, 36));
        congratsLabel.setForeground(Color.WHITE);
        winFrame.add(congratsLabel, gbc);

        JLabel scoreLabel = new JLabel("You solved the puzzle in " + moveCount + " moves!", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setForeground(Color.WHITE);
        winFrame.add(scoreLabel, gbc);

        JButton restartButton = createStyledButton("Restart");
        restartButton.addActionListener(e -> {
            winFrame.dispose();
            setupGameUI();
        });

        JButton mainMenuButton = createStyledButton("Main Menu");
        mainMenuButton.addActionListener(e -> {
            winFrame.dispose();
            showStartScreen();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(restartButton);
        buttonPanel.add(mainMenuButton);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        winFrame.add(buttonPanel, gbc);

        winFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TileQuest::new);
    }
}
