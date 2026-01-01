//Hakan ve Ali
package mazegame;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.Component;

public class ControlPanel extends JPanel {
    public JButton btnRun, btnNew, btnExit, btnPause, btnStep, btnSkipLevel;
    public JTextField speedField;
    public JTextArea codeEditor;
    private JTextArea lineNumbers;
    public JLabel scoreLabel;
    public JLabel errorDisplay;
    public JLabel levelLabel;
    public JLabel efficiencyLabel;
    public JLabel playerLabel;
    public JComboBox<String> themeSelector;
    private int currentExecutingLine = -1;
    private int errorLine = -1;

    public ControlPanel(JLabel scoreLabel, String playerName) {
        this.scoreLabel = scoreLabel;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(380, 0));
        setBackground(new Color(245, 255, 245));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(245, 255, 245));
        
        playerLabel = new JLabel("Player: " + playerName, SwingConstants.CENTER);
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        playerLabel.setForeground(new Color(63, 81, 181));
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(playerLabel);
        topPanel.add(Box.createVerticalStrut(5));
        
        levelLabel = new JLabel("Level: 1", SwingConstants.CENTER);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        levelLabel.setForeground(new Color(156, 39, 176));
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(levelLabel);
        topPanel.add(Box.createVerticalStrut(5));
        
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(scoreLabel);
        topPanel.add(Box.createVerticalStrut(3));
        
        efficiencyLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        efficiencyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        efficiencyLabel.setForeground(new Color(0, 150, 136));
        efficiencyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(efficiencyLabel);
        topPanel.add(Box.createVerticalStrut(8));
        
        JLabel themeLabel = new JLabel("Theme:", SwingConstants.CENTER);
        themeLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        themeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(themeLabel);
        topPanel.add(Box.createVerticalStrut(3));
        
        themeSelector = new JComboBox<>(new String[]{"Default", "Stranger Things"});
        themeSelector.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        themeSelector.setMaximumSize(new Dimension(200, 25));
        themeSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeSelector.setBackground(new Color(180, 180, 180));
        themeSelector.setForeground(Color.BLACK);
        topPanel.add(themeSelector);
        topPanel.add(Box.createVerticalStrut(8));

        errorDisplay = new JLabel("", SwingConstants.CENTER);
        errorDisplay.setForeground(new Color(211, 47, 47));
        errorDisplay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        errorDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(errorDisplay);
        topPanel.add(Box.createVerticalStrut(5));

        JButton syntaxButton = new JButton("Syntax Help");
        syntaxButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        syntaxButton.setBackground(new Color(33, 150, 243));
        syntaxButton.setForeground(Color.WHITE);
        syntaxButton.setFocusPainted(false);
        syntaxButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        syntaxButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        syntaxButton.addActionListener(e -> showSyntaxHelp());
        topPanel.add(syntaxButton);
        topPanel.add(Box.createVerticalStrut(10));
        add(topPanel, BorderLayout.NORTH);

        codeEditor = new JTextArea();
        codeEditor.setFont(new Font("Consolas", Font.PLAIN, 16));
        codeEditor.setBackground(new Color(255, 255, 255));
        codeEditor.setForeground(new Color(33, 33, 33));
        codeEditor.setTabSize(2);
        codeEditor.setLineWrap(false);
        codeEditor.setMargin(new Insets(5, 5, 5, 5));
        
        lineNumbers = new JTextArea("1");
        lineNumbers.setFont(new Font("Consolas", Font.PLAIN, 16));
        lineNumbers.setBackground(new Color(240, 240, 240));
        lineNumbers.setForeground(new Color(100, 100, 100));
        lineNumbers.setEditable(false);
        lineNumbers.setMargin(new Insets(5, 5, 5, 5));
        lineNumbers.setPreferredSize(new Dimension(40, 0));
        
        codeEditor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); updateHighlighting(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); updateHighlighting(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); updateHighlighting(); }
        });
        
        JScrollPane editorScrollPane = new JScrollPane(codeEditor);
        editorScrollPane.setRowHeaderView(lineNumbers);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 189, 189), 1, true), 
            "Algorithm Code:", 0, 0, new Font("Segoe UI", Font.BOLD, 11), new Color(69, 90, 100)));
        
        add(editorScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnRun = new JButton("RUN");
        btnPause = new JButton("PAUSE");
        btnStep = new JButton("STEP");
        
        Font btnFont = new Font("Segoe UI", Font.BOLD, 11);
        btnRun.setFont(btnFont); btnPause.setFont(btnFont); btnStep.setFont(btnFont);
        
        btnRun.setBackground(new Color(76, 175, 80));
        btnRun.setForeground(Color.BLACK);
        btnRun.setFocusPainted(false);
        btnRun.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRun.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btnRun.isEnabled()) btnRun.setBackground(new Color(104, 195, 108));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRun.setBackground(new Color(76, 175, 80));
            }
        });
        
        btnPause.setBackground(new Color(255, 152, 0));
        btnPause.setForeground(Color.BLACK);
        btnPause.setFocusPainted(false);
        btnPause.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnPause.setEnabled(false);
        btnPause.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btnPause.isEnabled()) btnPause.setBackground(new Color(255, 167, 38));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPause.setBackground(new Color(255, 152, 0));
            }
        });
        
        btnStep.setBackground(new Color(3, 169, 244));
        btnStep.setForeground(Color.BLACK);
        btnStep.setFocusPainted(false);
        btnStep.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnStep.setEnabled(false);
        btnStep.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btnStep.isEnabled()) btnStep.setBackground(new Color(41, 182, 246));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnStep.setBackground(new Color(3, 169, 244));
            }
        });
        
        row1.add(btnRun); row1.add(btnPause); row1.add(btnStep);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnNew = new JButton("NEW GAME");
        btnSkipLevel = new JButton("SKIP LEVEL");
        btnExit = new JButton("EXIT");

        Font btnFont2 = new Font("Segoe UI", Font.BOLD, 12);
        btnNew.setFont(btnFont2); btnSkipLevel.setFont(btnFont2); btnExit.setFont(btnFont2);
        
        btnNew.setBackground(new Color(255, 193, 7));
        btnNew.setForeground(new Color(33, 33, 33));
        btnNew.setFocusPainted(false);
        btnNew.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNew.setBackground(new Color(255, 202, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNew.setBackground(new Color(255, 193, 7));
            }
        });
        
        btnSkipLevel.setBackground(new Color(103, 58, 183));
        btnSkipLevel.setForeground(Color.WHITE);
        btnSkipLevel.setFocusPainted(false);
        btnSkipLevel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSkipLevel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSkipLevel.setBackground(new Color(123, 78, 203));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSkipLevel.setBackground(new Color(103, 58, 183));
            }
        });
        
        btnExit.setBackground(new Color(244, 67, 54));
        btnExit.setForeground(Color.BLACK);
        btnExit.setFocusPainted(false);
        btnExit.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExit.setBackground(new Color(229, 57, 53));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExit.setBackground(new Color(244, 67, 54));
            }
        });
        
        row2.add(btnNew); row2.add(btnExit); row2.add(btnSkipLevel);
        
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel speedLabel = new JLabel("Speed (0-100):", SwingConstants.CENTER);
        speedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        speedField = new JTextField("50", 5);
        speedField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        speedField.setHorizontalAlignment(JTextField.CENTER);
        
        speedField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = speedField.getText().trim();
                if (!text.isEmpty()) {
                    try {
                        int value = Integer.parseInt(text);
                        if (value < 0) {
                            speedField.setText("0");
                        } else if (value > 100) {
                            speedField.setText("100");
                        }
                    } catch (NumberFormatException e) {
                        speedField.setText("50");
                    }
                }
            }
        });
        
        row3.add(speedLabel);
        row3.add(speedField);

        btnPanel.add(row1); btnPanel.add(row2); btnPanel.add(row3);

        JLabel algoInfoLabel = new JLabel("Algorithm: A set of precise instructions.", SwingConstants.CENTER);
        algoInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        algoInfoLabel.setForeground(new Color(117, 117, 117));

        bottomPanel.add(btnPanel, BorderLayout.CENTER);
        bottomPanel.add(algoInfoLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void updateLineNumbers() {
        int lineCount = codeEditor.getLineCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            sb.append(i).append("\n");
        }
        lineNumbers.setText(sb.toString());
    }
    
    private void updateHighlighting() {
        SwingUtilities.invokeLater(() -> {
            try {
                Highlighter highlighter = codeEditor.getHighlighter();
                highlighter.removeAllHighlights();
                
                if (currentExecutingLine >= 0) {
                    int startOffset = codeEditor.getLineStartOffset(currentExecutingLine);
                    int endOffset = codeEditor.getLineEndOffset(currentExecutingLine);
                    highlighter.addHighlight(startOffset, endOffset, 
                        new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 249, 196)));
                }
                
                if (errorLine >= 0) {
                    int startOffset = codeEditor.getLineStartOffset(errorLine);
                    int endOffset = codeEditor.getLineEndOffset(errorLine);
                    highlighter.addHighlight(startOffset, endOffset, 
                        new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 205, 210)));
                }
            } catch (BadLocationException ex) {
            }
        });
    }
    
    public void setCurrentExecutingLine(int lineIndex) {
        this.currentExecutingLine = lineIndex;
        this.errorLine = -1;
        updateHighlighting();
        try {
            if (lineIndex >= 0) {
                int startOffset = codeEditor.getLineStartOffset(lineIndex);
                codeEditor.setCaretPosition(startOffset);
                Rectangle viewRect = codeEditor.modelToView(startOffset);
                if (viewRect != null) {
                    codeEditor.scrollRectToVisible(viewRect);
                }
            }
        } catch (BadLocationException ex) {
        }
    }
    
    public void setErrorLine(int lineIndex) {
        this.errorLine = lineIndex;
        this.currentExecutingLine = -1;
        updateHighlighting();
        try {
            if (lineIndex >= 0) {
                int startOffset = codeEditor.getLineStartOffset(lineIndex);
                codeEditor.setCaretPosition(startOffset);
                Rectangle viewRect = codeEditor.modelToView(startOffset);
                if (viewRect != null) {
                    codeEditor.scrollRectToVisible(viewRect);
                }
            }
        } catch (BadLocationException ex) {
        }
    }
    
    public void clearHighlights() {
        this.currentExecutingLine = -1;
        this.errorLine = -1;
        updateHighlighting();
    }
    
    public String[] getCodeLines() {
        String text = codeEditor.getText().trim();
        if (text.isEmpty()) return new String[0];
        return text.split("\n");
    }
    
    public void clearCode() {
        codeEditor.setText("");
    }
    
    private void showSyntaxHelp() {
        String helpText = "<html><body style='width: 400px; padding: 10px;'>" +
            "<h2 style='color: #1976D2;'>Command Syntax Guide</h2>" +
            "<hr>" +
            "<h3>Basic Commands:</h3>" +
            "<ul>" +
            "<li><b>GO()</b> - Move forward one step</li>" +
            "<li><b>LEFT()</b> - Turn left</li>" +
            "<li><b>RIGHT()</b> - Turn right</li>" +
            "<li><b>CATCH()</b> - Catch carrot at current position</li>" +
            "</ul>" +
            "<h3>Repeat Commands:</h3>" +
            "<ul>" +
            "<li><b>GO(5)</b> - Move forward 5 steps</li>" +
            "<li><b>LEFT(3)</b> - Turn left 3 times</li>" +
            "<li><b>RIGHT(2)</b> - Turn right 2 times</li>" +
            "</ul>" +
            "<h3>Combined Commands:</h3>" +
            "<ul>" +
            "<li><b>GO(3,CATCH)</b> - Move 3 steps, then catch</li>" +
            "<li><b>GO(2,LEFT)</b> - Move 2 steps, then turn left</li>" +
            "<li><b>GO(5,RIGHT)</b> - Move 5 steps, then turn right</li>" +
            "<li><b>LEFT(2,GO)</b> - Turn left twice, then move</li>" +
            "</ul>" +
            "<p style='margin-top: 15px; color: #666;'><i>Tip: Commands are case-insensitive!</i></p>" +
            "</body></html>";
        
        JOptionPane.showMessageDialog(this, helpText, "Syntax Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void applyTheme(String theme) {
        if (theme.equals("Stranger Things")) {
            setBackground(new Color(30, 30, 30));
            
            for (Component comp : getComponents()) {
                applyThemeToComponent(comp, new Color(30, 30, 30), new Color(200, 200, 200));
            }
            
            codeEditor.setBackground(new Color(40, 40, 40));
            codeEditor.setForeground(new Color(255, 200, 200));
            codeEditor.setCaretColor(Color.WHITE);
            lineNumbers.setBackground(new Color(25, 25, 25));
            lineNumbers.setForeground(new Color(150, 150, 150));
        } else {
            setBackground(new Color(245, 255, 245));
            
            for (Component comp : getComponents()) {
                applyThemeToComponent(comp, new Color(245, 255, 245), new Color(33, 33, 33));
            }
            
            codeEditor.setBackground(new Color(255, 255, 255));
            codeEditor.setForeground(new Color(33, 33, 33));
            codeEditor.setCaretColor(Color.BLACK);
            lineNumbers.setBackground(new Color(240, 240, 240));
            lineNumbers.setForeground(new Color(100, 100, 100));
        }
        repaint();
    }
    
    private void applyThemeToComponent(Component comp, Color bgColor, Color fgColor) {
        if (comp instanceof JPanel jPanel) {
            comp.setBackground(bgColor);
            JPanel panel = jPanel;
            for (Component child : panel.getComponents()) {
                applyThemeToComponent(child, bgColor, fgColor);
            }
        } else if (comp instanceof JLabel) {
            if (!comp.equals(playerLabel) && !comp.equals(levelLabel) && !comp.equals(scoreLabel) && !comp.equals(errorDisplay) && !comp.equals(efficiencyLabel)) {
                comp.setForeground(fgColor);
            }
        } else if (comp instanceof JTextField) {
            if (comp.equals(speedField)) {
                comp.setBackground(bgColor);
                comp.setForeground(fgColor);
            }
        } else if (comp instanceof JScrollPane) {
            comp.setBackground(bgColor);
            JScrollPane scroll = (JScrollPane) comp;
            if (scroll.getViewport() != null) {
                scroll.getViewport().setBackground(bgColor);
            }
        }
    }
}