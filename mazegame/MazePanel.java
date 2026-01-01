//Ali
package mazegame;

import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private Cell[][] mazeGrid;
    private boolean[][] carrotGrid;
    private final int WALL_THICKNESS = 4;
    
    public int playerRow = 0; 
    public int playerCol = 0;
    private int playerDirection = 1;
    
    private int mazeRows;
    private int mazeCols;
    private int totalScore = 0;
    
    private JLabel scoreLabel;
    private JFrame parentFrame;

    private final int SMALL_CARROT_SCORE = 10;
    private final int EXIT_CARROT_SCORE = 100;
    
    private String currentTheme = "Default";
    private Color bgColor1, bgColor2, wallColor, carrotColor, carrotStroke, doorColor, doorStroke, playerColor;

    public MazePanel(Cell[][] grid, boolean[][] carrots, int rows, int cols, JLabel scoreLabel, JFrame parent) {
        this.mazeGrid = grid;
        this.carrotGrid = carrots;
        this.mazeRows = rows;
        this.mazeCols = cols;
        this.scoreLabel = scoreLabel;
        this.parentFrame = parent;
        
        applyTheme("Default");
        
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                repaint(); 
            }
        });
    }
    
    public void updateMazeData(Cell[][] newGrid, boolean[][] newCarrots, int rows, int cols) {
        this.mazeGrid = newGrid;
        this.carrotGrid = newCarrots;
        this.mazeRows = rows;
        this.mazeCols = cols;
        resetPlayer(0);
        repaint();
    }

    private int getCellSize() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int size = Math.min(panelWidth / mazeCols, panelHeight / mazeRows);
        return Math.max(size, 10); 
    }
    
    private int getOffsetX() {
        int cs = getCellSize();
        int mazeWidth = mazeCols * cs;
        return (getWidth() - mazeWidth) / 2;
    }
    
    private int getOffsetY() {
        int cs = getCellSize();
        int mazeHeight = mazeRows * cs;
        return (getHeight() - mazeHeight) / 2;
    }
    
    public void applyTheme(String theme) {
        this.currentTheme = theme;
        if (theme.equals("Stranger Things")) {
            bgColor1 = new Color(20, 20, 20);
            bgColor2 = new Color(30, 30, 30);
            wallColor = new Color(139, 0, 0);
            carrotColor = new Color(255, 0, 0);
            carrotStroke = new Color(180, 0, 0);
            doorColor = new Color(80, 0, 0);
            doorStroke = new Color(50, 0, 0);
            playerColor = new Color(255, 255, 255);
            setBackground(new Color(20, 20, 20));
        } else {
            bgColor1 = new Color(102, 204, 102);
            bgColor2 = new Color(119, 221, 119);
            wallColor = new Color(46, 125, 50);
            carrotColor = new Color(255, 140, 0);
            carrotStroke = new Color(230, 115, 0);
            doorColor = new Color(139, 69, 19);
            doorStroke = new Color(101, 67, 33);
            playerColor = new Color(255, 255, 255);
            setBackground(new Color(102, 204, 102));
        }
        repaint();
    }

    public void resetPlayer(int initialScore) {
        this.playerRow = 0;
        this.playerCol = 0;
        this.playerDirection = 1; 
        this.totalScore = initialScore;
        updateScoreLabel();
        repaint();
    }

    private void updateScoreLabel() {
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + totalScore + " points");
        }
    }

    public void turnLeft() {
        playerDirection = (playerDirection + 3) % 4;
        repaint();
    }

    public void turnRight() {
        playerDirection = (playerDirection + 1) % 4;
        repaint();
    }
    
    public boolean moveForward() {
        Cell currentCell = mazeGrid[playerRow][playerCol];
        int newR = playerRow;
        int newC = playerCol;

        if (playerDirection == 0 && !currentCell.wallN){
            newR--; 
        }else if (playerDirection == 1 && !currentCell.wallE){
            newC++; 
        }else if (playerDirection == 2 && !currentCell.wallS){
            newR++; 
        }else if (playerDirection == 3 && !currentCell.wallW){ 
            newC--; 
        }else { 
            return false; 
        }
        
        playerRow = newR;
        playerCol = newC;
        repaint();
        return true; 
    }
    
    public boolean capture() {
        if (playerRow == mazeRows - 1 && playerCol == mazeCols - 1) {
            totalScore += EXIT_CARROT_SCORE;
            updateScoreLabel();
            return true;
        }
        
        if (carrotGrid[playerRow][playerCol]) {
            carrotGrid[playerRow][playerCol] = false;
            totalScore += SMALL_CARROT_SCORE;
            updateScoreLabel();
            repaint();
            return true;
        }
        return false;
    }
    
    public boolean isAtExit() {
        return playerRow == mazeRows - 1 && playerCol == mazeCols - 1;
    }
    
    public int getTotalScore() {
        return totalScore;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cs = getCellSize();
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();

        for (int r = 0; r < mazeRows; r++) {
            for (int c = 0; c < mazeCols; c++) {
                if ((r + c) % 2 == 0) {
                    g2d.setColor(bgColor1);
                } else {
                    g2d.setColor(bgColor2);
                }
                g2d.fillRect(offsetX + c * cs, offsetY + r * cs, cs, cs);
            }
        }
        
        for (int r = 0; r < mazeRows; r++) {
            for (int c = 0; c < mazeCols; c++) {
                int x = offsetX + c * cs;
                int y = offsetY + r * cs;

                if (carrotGrid[r][c]) {
                    int carrotWidth = cs / 3;
                    int carrotHeight = (int)(cs * 0.5);
                    int carrotX = x + cs / 2;
                    int carrotY = y + cs / 2;
                    
                    if (currentTheme.equals("Stranger Things")) {
                        int demoSize = (int)(cs * 0.6);
                        int demoX = carrotX;
                        int demoY = carrotY;

                        g2d.setColor(new Color(80, 20, 20));
                        g2d.fillOval(demoX - demoSize/4, demoY - demoSize/4, demoSize/2, demoSize/2);
                        
                        g2d.setColor(new Color(139, 0, 0));
                        int petalCount = 6;
                        int petalLength = demoSize / 3;
                        for (int i = 0; i < petalCount; i++) {
                            double angle = (2 * Math.PI / petalCount) * i;
                            int x1 = demoX;
                            int y1 = demoY;
                            int x2 = (int)(demoX + Math.cos(angle) * petalLength);
                            int y2 = (int)(demoY + Math.sin(angle) * petalLength);
                            g2d.setStroke(new BasicStroke(3));
                            g2d.drawLine(x1, y1, x2, y2);
                        }
                        
                        g2d.setColor(new Color(0, 0, 0));
                        g2d.fillOval(demoX - demoSize/8, demoY - demoSize/8, demoSize/4, demoSize/4);
                    } else {
                        Polygon carrot = new Polygon();
                        carrot.addPoint(carrotX, carrotY + carrotHeight / 2);
                        carrot.addPoint(carrotX - carrotWidth / 2, carrotY - carrotHeight / 4);
                        carrot.addPoint(carrotX + carrotWidth / 2, carrotY - carrotHeight / 4);
                        
                        g2d.setColor(carrotColor);
                        g2d.fillPolygon(carrot);
                        g2d.setColor(carrotStroke);
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawPolygon(carrot);
                        
                        g2d.setColor(new Color(76, 175, 80));
                        g2d.setStroke(new BasicStroke(3));
                        g2d.drawLine(carrotX - 3, carrotY - carrotHeight / 4, carrotX - 5, carrotY - carrotHeight / 2);
                        g2d.drawLine(carrotX, carrotY - carrotHeight / 4, carrotX, carrotY - carrotHeight / 2 - 2);
                        g2d.drawLine(carrotX + 3, carrotY - carrotHeight / 4, carrotX + 5, carrotY - carrotHeight / 2);
                    }
                }
                
                Cell cell = mazeGrid[r][c];
                g2d.setColor(wallColor); 
                g2d.setStroke(new BasicStroke(WALL_THICKNESS, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                if (cell.wallN) g2d.drawLine(x, y, x + cs, y);
                if (cell.wallE) g2d.drawLine(x + cs, y, x + cs, y + cs);
                if (cell.wallS) g2d.drawLine(x, y + cs, x + cs, y + cs);
                if (cell.wallW) g2d.drawLine(x, y, x, y + cs);
            }
        }
        
        int exitX = offsetX + (mazeCols - 1) * cs;
        int exitY = offsetY + (mazeRows - 1) * cs;
        
        int doorWidth = (int)(cs * 0.6);
        int doorHeight = (int)(cs * 0.8);
        int doorX = exitX + (cs - doorWidth) / 2;
        int doorY = exitY + (cs - doorHeight) / 2;
        
        g2d.setColor(doorColor);
        g2d.fillRoundRect(doorX, doorY, doorWidth, doorHeight, 10, 10);
        
        g2d.setColor(doorStroke);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(doorX, doorY, doorWidth, doorHeight, 10, 10);
        
        g2d.setColor(new Color(255, 215, 0));
        int knobSize = cs / 10;
        g2d.fillOval(doorX + doorWidth - knobSize - 5, doorY + doorHeight / 2 - knobSize / 2, knobSize, knobSize);
        
        g2d.setColor(new Color(80, 50, 20));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(doorX + 5, doorY + 5, doorX + 5, doorY + doorHeight - 5);
        g2d.drawLine(doorX + doorWidth / 2, doorY + 5, doorX + doorWidth / 2, doorY + doorHeight - 5);
        g2d.drawLine(doorX + doorWidth - 5, doorY + 5, doorX + doorWidth - 5, doorY + doorHeight - 5); 
        
        int bunnySize = (int)(cs * 0.6);
        int kidX = offsetX + playerCol * cs + (cs / 2) - (bunnySize / 2);
        int kidY = offsetY + playerRow * cs + (cs / 2) - (bunnySize / 2);
        
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(kidX + 2, kidY + 2, bunnySize, bunnySize);
        
        g2d.setColor(playerColor);
        g2d.fillOval(kidX, kidY, bunnySize, bunnySize);
        
        int earWidth = bunnySize / 4;
        int earHeight = bunnySize / 2;
        if (playerDirection == 0) {
            g2d.fillOval(kidX + bunnySize/4 - earWidth/2, kidY - earHeight/2, earWidth, earHeight);
            g2d.fillOval(kidX + 3*bunnySize/4 - earWidth/2, kidY - earHeight/2, earWidth, earHeight);
        } else if (playerDirection == 1) {
            g2d.fillOval(kidX + bunnySize - earHeight/2, kidY + bunnySize/4 - earWidth/2, earHeight, earWidth);
            g2d.fillOval(kidX + bunnySize - earHeight/2, kidY + 3*bunnySize/4 - earWidth/2, earHeight, earWidth);
        } else if (playerDirection == 2) {
            g2d.fillOval(kidX + bunnySize/4 - earWidth/2, kidY + bunnySize - earHeight/2, earWidth, earHeight);
            g2d.fillOval(kidX + 3*bunnySize/4 - earWidth/2, kidY + bunnySize - earHeight/2, earWidth, earHeight);
        } else {
            g2d.fillOval(kidX - earHeight/2, kidY + bunnySize/4 - earWidth/2, earHeight, earWidth);
            g2d.fillOval(kidX - earHeight/2, kidY + 3*bunnySize/4 - earWidth/2, earHeight, earWidth);
        }
        
        g2d.setColor(new Color(255, 192, 203));
        int innerEarSize = earWidth / 2;
        if (playerDirection == 0) {
            g2d.fillOval(kidX + bunnySize/4 - innerEarSize/2, kidY, innerEarSize, earHeight/2);
            g2d.fillOval(kidX + 3*bunnySize/4 - innerEarSize/2, kidY, innerEarSize, earHeight/2);
        } else if (playerDirection == 1) {
            g2d.fillOval(kidX + bunnySize - earHeight/2 + earHeight/4, kidY + bunnySize/4 - innerEarSize/2, earHeight/2, innerEarSize);
            g2d.fillOval(kidX + bunnySize - earHeight/2 + earHeight/4, kidY + 3*bunnySize/4 - innerEarSize/2, earHeight/2, innerEarSize);
        } else if (playerDirection == 2) {
            g2d.fillOval(kidX + bunnySize/4 - innerEarSize/2, kidY + bunnySize - earHeight/2 + earHeight/4, innerEarSize, earHeight/2);
            g2d.fillOval(kidX + 3*bunnySize/4 - innerEarSize/2, kidY + bunnySize - earHeight/2 + earHeight/4, innerEarSize, earHeight/2);
        } else {
            g2d.fillOval(kidX, kidY + bunnySize/4 - innerEarSize/2, earHeight/2, innerEarSize);
            g2d.fillOval(kidX, kidY + 3*bunnySize/4 - innerEarSize/2, earHeight/2, innerEarSize);
        }
        
        g2d.setColor(new Color(50, 50, 50));
        int eyeSize = bunnySize / 8;
        int eyeOffset = bunnySize / 4;
        if (playerDirection == 0) {
            g2d.fillOval(kidX + eyeOffset, kidY + bunnySize/3, eyeSize, eyeSize);
            g2d.fillOval(kidX + bunnySize - eyeOffset - eyeSize, kidY + bunnySize/3, eyeSize, eyeSize);
        } else if (playerDirection == 1) {
            g2d.fillOval(kidX + bunnySize - bunnySize/3 - eyeSize, kidY + eyeOffset, eyeSize, eyeSize);
            g2d.fillOval(kidX + bunnySize - bunnySize/3 - eyeSize, kidY + bunnySize - eyeOffset - eyeSize, eyeSize, eyeSize);
        } else if (playerDirection == 2) {
            g2d.fillOval(kidX + eyeOffset, kidY + bunnySize - bunnySize/3 - eyeSize, eyeSize, eyeSize);
            g2d.fillOval(kidX + bunnySize - eyeOffset - eyeSize, kidY + bunnySize - bunnySize/3 - eyeSize, eyeSize, eyeSize);
        } else {
            g2d.fillOval(kidX + bunnySize/3, kidY + eyeOffset, eyeSize, eyeSize);
            g2d.fillOval(kidX + bunnySize/3, kidY + bunnySize - eyeOffset - eyeSize, eyeSize, eyeSize);
        }
        
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(kidX, kidY, bunnySize, bunnySize);
        
        if (currentTheme.equals("Stranger Things")) {
            g2d.setColor(new Color(139, 0, 0));
            g2d.fillOval(kidX + bunnySize/3, kidY + bunnySize/4, bunnySize/8, bunnySize/10);
            g2d.fillOval(kidX + bunnySize/2, kidY + bunnySize/2, bunnySize/10, bunnySize/8);
            g2d.fillOval(kidX + 2*bunnySize/3, kidY + 2*bunnySize/3, bunnySize/9, bunnySize/9);
            g2d.fillOval(kidX + bunnySize/5, kidY + 3*bunnySize/5, bunnySize/11, bunnySize/10);
        }

    }
}