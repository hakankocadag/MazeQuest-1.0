//Hakan ve ALi
package mazegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MazeGame extends JFrame {
    private static final int SMALL_CARROT_COUNT = 18;
    
    private int currentLevel = 1;
    private int mazeRows;
    private int mazeCols;
    private String playerName;
    
    private Cell[][] currentMazeData;
    private boolean[][] carrotGrid;
    private boolean[][] initialCarrotGrid;
    private MazePanel mazePanel;
    private ControlPanel controlPanel;
    private CommandProcessor processor;
    private SoundManager soundManager;
    
    private Timer executionTimer;
    private int currentStep = 0;
    private java.util.ArrayList<Character> currentSequence;
    private java.util.ArrayList<Integer> currentLineMapping;
    private boolean isPaused = false;

    public MazeGame() {
        this.setUndecorated(false); 
        setTitle("MazeQuest");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        playerName = JOptionPane.showInputDialog(null, 
            "Enter your name to begin the game:", 
            "Welcome to MazeQuest!", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }

        processor = new CommandProcessor();
        soundManager = new SoundManager();
        
        setMazeSizeForLevel(currentLevel);
        generateNewGameData();

        JLabel scoreLabel = new JLabel("Score: 0 points", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scoreLabel.setForeground(new Color(0, 150, 136));
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        mazePanel = new MazePanel(currentMazeData, carrotGrid, mazeRows, mazeCols, scoreLabel, this);
        controlPanel = new ControlPanel(scoreLabel, playerName);

        add(mazePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        setupEvents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                soundManager.stopBackground();
                soundManager.closeClip();
            }
        });
        setupWindow();
        
        soundManager.loadBackground("src/mazegame/background.wav");
        soundManager.playBackground();
    }
    
    private void setMazeSizeForLevel(int level) {
        if (level <= 10) {
            mazeRows = level + 3;
            mazeCols = 3 + (2 * level);
        } else {
            mazeRows = 13;
            mazeCols = 23;
        }
    }

    private void generateNewGameData() {
        MazeGenerator gen = new MazeGenerator(mazeRows, mazeCols);
        currentMazeData = gen.generateMaze();
        carrotGrid = new boolean[mazeRows][mazeCols];
        Random rand = new Random();
        int carrotCount = Math.min(SMALL_CARROT_COUNT, (mazeRows * mazeCols) / 3);
        int count = 0;
        while (count < carrotCount) {
            int r = rand.nextInt(mazeRows);
            int c = rand.nextInt(mazeCols);
            if ((r != 0 || c != 0) && (r != mazeRows - 1 || c != mazeCols - 1) && !carrotGrid[r][c]) {
                carrotGrid[r][c] = true;
                count++;
            }
        }
        initialCarrotGrid = new boolean[mazeRows][mazeCols];
        for (int r = 0; r < mazeRows; r++) {
            for (int c = 0; c < mazeCols; c++) {
                initialCarrotGrid[r][c] = carrotGrid[r][c];
            }
        }
    }

    private void setupEvents() {
        controlPanel.btnRun.addActionListener(this::executeInstructions);
        controlPanel.btnPause.addActionListener(e -> pauseExecution());
        controlPanel.btnStep.addActionListener(e -> stepExecution());
        controlPanel.btnNew.addActionListener(e -> startNewGame());
        controlPanel.btnSkipLevel.addActionListener(e -> skipLevel());
        controlPanel.themeSelector.addActionListener(e -> changeTheme());
        controlPanel.btnExit.addActionListener(e -> {
            soundManager.stopBackground();
            soundManager.closeClip();
            System.exit(0);
        });
    }

    private void startNewGame() {
        currentLevel = 1;
        setMazeSizeForLevel(currentLevel);
        generateNewGameData();
        mazePanel.updateMazeData(currentMazeData, carrotGrid, mazeRows, mazeCols);
        controlPanel.clearCode();
        controlPanel.scoreLabel.setText("Score: 0 points");
        controlPanel.levelLabel.setText("Level: " + currentLevel);
        controlPanel.efficiencyLabel.setText("Moves: 0");
        controlPanel.clearHighlights();
        controlPanel.codeEditor.requestFocusInWindow();
    }

    private void skipLevel() {
        if (executionTimer != null && executionTimer.isRunning()) {
            executionTimer.stop();
        }
        
        if (currentLevel >= 10) {
            JOptionPane.showMessageDialog(this, 
                "YOU WON! \n\n" +
                "You completed all 10 levels!\n\n" +
                "If you want to replay with different mazes,\n" +
                "click NEW GAME.\n\n" +
                "Or begin to code with Python!",
                "Game Complete!", JOptionPane.INFORMATION_MESSAGE);
            
            controlPanel.btnRun.setEnabled(false);
            controlPanel.btnPause.setEnabled(false);
            controlPanel.btnStep.setEnabled(false);
            controlPanel.btnSkipLevel.setEnabled(false);
            return;
        }
        
        currentLevel++;
        setMazeSizeForLevel(currentLevel);
        generateNewGameData();
        mazePanel.updateMazeData(currentMazeData, carrotGrid, mazeRows, mazeCols);
        controlPanel.clearCode();
        controlPanel.levelLabel.setText("Level: " + currentLevel);
        controlPanel.efficiencyLabel.setText("Moves: 0");
        controlPanel.clearHighlights();
        controlPanel.btnRun.setEnabled(true);
        controlPanel.btnPause.setEnabled(false);
        controlPanel.btnStep.setEnabled(false);
        controlPanel.codeEditor.requestFocusInWindow();
    }
    
    private void changeTheme() {
        String selectedTheme = (String) controlPanel.themeSelector.getSelectedItem();
        mazePanel.applyTheme(selectedTheme);
        controlPanel.applyTheme(selectedTheme);
        
        soundManager.stopBackground();
        soundManager.closeClip();
        
        if (selectedTheme.equals("Stranger Things")) {
            soundManager.loadBackground("src/mazegame/videoplayback.wav");
        } else {
            soundManager.loadBackground("src/mazegame/background.wav");
        }
        soundManager.playBackground();
    }

    private void executeInstructions(ActionEvent e) {
        String[] codeLines = controlPanel.getCodeLines();
        if (codeLines.length == 0) return;
        
        controlPanel.clearHighlights();
        controlPanel.errorDisplay.setText("");

        java.util.ArrayList<String> validCommands = new java.util.ArrayList<>();
        for (int i = 0; i < codeLines.length; i++) {
            String line = codeLines[i].trim();
            if (line.isEmpty()) continue;
            
            String result = processor.parseInput(line);
            if (result == null) {
                controlPanel.setErrorLine(i);
                controlPanel.errorDisplay.setText("Syntax error on line " + (i + 1));
                return;
            }
            validCommands.add(result);
        }
        
        for (int r = 0; r < mazeRows; r++) {
            for (int c = 0; c < mazeCols; c++) {
                carrotGrid[r][c] = initialCarrotGrid[r][c];
            }
        }
        mazePanel.repaint();
        
        currentSequence = processor.getExecutionSequenceFromCommands(validCommands);
        currentLineMapping = processor.getLineMappingFromCommands(validCommands);
        mazePanel.resetPlayer(0);
        currentStep = 0;
        isPaused = false;
        
        controlPanel.efficiencyLabel.setText("Moves: 0 / " + currentSequence.size());
        
        controlPanel.btnRun.setEnabled(false);
        controlPanel.btnPause.setEnabled(true);
        controlPanel.btnStep.setEnabled(false);

        int speed = getSpeedDelay();
        executionTimer = new Timer(speed, null);
        executionTimer.addActionListener(ae -> executeNextStep());
        executionTimer.start();
    }
    
    private int getSpeedDelay() {
        try {
            int speed = Integer.parseInt(controlPanel.speedField.getText().trim());
            speed = Math.max(0, Math.min(100, speed));
            return 1000 - (speed * 9);
        } catch (NumberFormatException e) {
            return 550;
        }
    }
    
    private void executeNextStep() {
        if (currentStep < currentSequence.size()) {
            int currentLineIndex = currentLineMapping.get(currentStep);
            controlPanel.setCurrentExecutingLine(currentLineIndex);
            
            int movesUsed = currentStep + 1;
            int totalMoves = currentSequence.size();
            controlPanel.efficiencyLabel.setText("Moves: " + movesUsed + " / " + totalMoves);
            
            char action = currentSequence.get(currentStep);
            boolean success = true;
            switch (action) {
                case 'G': success = mazePanel.moveForward(); break;
                case 'L': mazePanel.turnLeft(); break;
                case 'R': mazePanel.turnRight(); break;
                case 'C': 
                    if (mazePanel.isAtExit()) {
                        executionTimer.stop();
                        controlPanel.clearHighlights();
                        int finalScore = mazePanel.getTotalScore() + 100;
                        
                        if (currentLevel == 10) {
                            JOptionPane.showMessageDialog(this, 
                                "🎉 YOU WON! 🎉\n\n" +
                                "Final Score: " + finalScore + "\n" +
                                "Moves used: " + currentSequence.size() + "\n\n" +
                                "Now learn how to code!",
                                "Game Complete!", JOptionPane.INFORMATION_MESSAGE);
                            
                            controlPanel.btnRun.setEnabled(false);
                            controlPanel.btnPause.setEnabled(false);
                            controlPanel.btnStep.setEnabled(false);
                            controlPanel.btnSkipLevel.setEnabled(false);
                            return;
                        }
                        
                        JOptionPane.showMessageDialog(this, 
                            "Level Complete!\n" +
                            "Score: " + finalScore + "\n" +
                            "Moves used: " + currentSequence.size(),
                            "Level " + currentLevel + " Complete!", JOptionPane.INFORMATION_MESSAGE);
                        
                        currentLevel++;
                        setMazeSizeForLevel(currentLevel);
                        generateNewGameData();
                        mazePanel.updateMazeData(currentMazeData, carrotGrid, mazeRows, mazeCols);
                        controlPanel.clearCode();
                        controlPanel.levelLabel.setText("Level: " + currentLevel);
                        controlPanel.efficiencyLabel.setText("Moves: 0");
                        controlPanel.btnRun.setEnabled(true);
                        controlPanel.btnPause.setEnabled(false);
                        controlPanel.btnStep.setEnabled(false);
                        return;
                    }
                    success = mazePanel.capture(); 
                    break;
            }
            if (!success) {
                executionTimer.stop();
                controlPanel.setErrorLine(currentLineIndex);
                JOptionPane.showMessageDialog(this, "CRASH!");
                controlPanel.clearHighlights();
                controlPanel.btnRun.setEnabled(true);
                controlPanel.btnPause.setEnabled(false);
                controlPanel.btnStep.setEnabled(false);
                return;
            }
            currentStep++;
        } else { 
            executionTimer.stop();
            controlPanel.clearHighlights();
            controlPanel.btnRun.setEnabled(true);
            controlPanel.btnPause.setEnabled(false);
            controlPanel.btnStep.setEnabled(false);
        }
    }
    
    private void pauseExecution() {
        if (executionTimer != null && executionTimer.isRunning()) {
            executionTimer.stop();
            isPaused = true;
            controlPanel.btnPause.setText("RESUME");
            controlPanel.btnStep.setEnabled(true);
        } else if (isPaused) {
            executionTimer.start();
            isPaused = false;
            controlPanel.btnPause.setText("PAUSE");
            controlPanel.btnStep.setEnabled(false);
        }
    }
    
    private void stepExecution() {
        if (isPaused) {
            executeNextStep();
        }
    }

    private void setupWindow() {
        this.setSize(1400, 870);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);
        controlPanel.codeEditor.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeGame::new);
    }
}