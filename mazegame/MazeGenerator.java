//Hakan
package mazegame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private final int ROWS;
    private final int COLS;
    private Cell[][] grid;
    private Stack<Cell> stack = new Stack<>();
    private Random rand = new Random();

    public MazeGenerator(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.grid = new Cell[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) grid[r][c] = new Cell(r, c);
        }
    }

    public Cell[][] generateMaze() {
        Cell startCell = grid[0][0];
        startCell.visited = true;
        stack.push(startCell);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            ArrayList<Cell> neighbors = getUnvisitedNeighbors(current);
            if (!neighbors.isEmpty()) {
                Cell next = neighbors.get(rand.nextInt(neighbors.size()));
                carvePassage(current, next);
                next.visited = true;
                stack.push(next);
            } else {
                stack.pop();
            }
        }
        
        
        return grid;
    }

    private ArrayList<Cell> getUnvisitedNeighbors(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        int r = cell.row, c = cell.col;
        if (r > 0 && !grid[r-1][c].visited) neighbors.add(grid[r-1][c]);
        if (c < COLS-1 && !grid[r][c+1].visited) neighbors.add(grid[r][c+1]);
        if (r < ROWS-1 && !grid[r+1][c].visited) neighbors.add(grid[r+1][c]);
        if (c > 0 && !grid[r][c-1].visited) neighbors.add(grid[r][c-1]);
        return neighbors;
    }
    
    private void carvePassage(Cell current, Cell next) {
        int rDiff = current.row - next.row;
        int cDiff = current.col - next.col;
        if (rDiff == 1){
            current.wallN = false; next.wallS = false; 
        } else if (rDiff == -1) { 
            current.wallS = false; next.wallN = false; 
        } else if (cDiff == 1) { 
            current.wallW = false; next.wallE = false; 
        } else if (cDiff == -1) { 
            current.wallE = false; next.wallW = false; 
        }
    }
}