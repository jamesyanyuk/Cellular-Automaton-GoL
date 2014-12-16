import java.util.*;

public class Cell {
    public int x;
    public int y;
    public boolean alive;

    public Cell() {}

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        alive = false;
    }

    public void revitalize() {
        alive = true;
    }

    public void neutralize() {
        alive = false;
    }

    public void pulseCell(Cell[][] grid, Stack<Cell> toNeutralize, Stack<Cell> toRevitalize) {
        checkNeighbors(grid, toNeutralize, toRevitalize);
    }

    private void checkNeighbors(Cell[][] grid, Stack<Cell> toNeutralize, Stack<Cell> toRevitalize) {
        if(alive == true && neighbors(grid) != 2 && neighbors(grid) != 3) {
            toNeutralize.push(this);
            //System.out.println(x + ", " + y + " not enough neighbors " + neighbors(grid));
        } else if(alive == false && neighbors(grid) == 3) {
            toRevitalize.push(this);
        }
    }

    private int neighbors(Cell[][] grid) {
        // Check 8 surrounding cells for live neighbors
        int neighbors = 0;

        neighbors += (x - 1) >= 0 && grid[x - 1][y].alive ? 1 : 0;
        neighbors += (x + 1) < 60 && grid[x + 1][y].alive ? 1 : 0;

        neighbors += (y - 1) >= 0 && grid[x][y - 1].alive ? 1 : 0;
        neighbors += (y + 1) < 60 && grid[x][y + 1].alive ? 1 : 0;

        neighbors += (x - 1) >= 0 && (y - 1) >= 0 && grid[x - 1][y - 1].alive ? 1 : 0;
        neighbors += (x + 1) < 60 && (y + 1) < 60 && grid[x + 1][y + 1].alive ? 1 : 0;

        neighbors += (x - 1) >= 0 && (y + 1) < 60 && grid[x - 1][y + 1].alive ? 1 : 0;
        neighbors += (x + 1) < 60 && (y - 1) >= 0 && grid[x + 1][y - 1].alive ? 1 : 0;

        return neighbors;
    }
}
