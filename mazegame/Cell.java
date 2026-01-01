//Hakan
package mazegame;

public class Cell {
    public int row;
    public int col;
    public boolean visited = false;
    
    public boolean wallN = true;
    public boolean wallE = true;
    public boolean wallS = true;
    public boolean wallW = true;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
    