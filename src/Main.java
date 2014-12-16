import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;

import java.util.*;

public class Main extends BasicGame {

    private static int gridX = 60;
    private static int gridY = 60;
    private static int gridElemSize = 10;

    Cell[][] grid = new Cell[gridX][gridY];

    public Stack<Cell> toNeutralize = new Stack();
    public Stack<Cell> toRevitalize = new Stack();

    public Main(String title) {
        super(title);
    }

    private void initGrid() {
        for(int r = 0; r < gridY; r++) {
            for(int c = 0; c < gridX; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
    }

    public void pulseGrid() {
        // Update board by iterating through stacks
        // (neutralize and revitalize stacks)

        while(!toNeutralize.isEmpty()) {
            toNeutralize.pop().neutralize();
        }

        while(!toRevitalize.isEmpty()) {
            toRevitalize.pop().revitalize();
        }
    }

    public void init(GameContainer gc) throws SlickException {
        gc.setMinimumLogicUpdateInterval(500);
        gc.setTargetFrameRate(60);
        gc.setAlwaysRender(true);
        gc.setVSync(true);

        initGrid();

        /*
         * Generate sample initial configuration
         * Should form basic pulsar
        */
        grid[29][30].revitalize();
        grid[30][30].revitalize();
        grid[31][30].revitalize();
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.drawString(Integer.toString(gc.getFPS()), 0, 0);

        // Set background color to white
        g.setBackground(Color.white);

        // Draw cell grid
        for(Cell[] r : grid) {
            for (Cell c : r) {
                if (c.alive) {
                    g.setColor(Color.black);
                    g.fillRect(c.x * gridElemSize, c.y * gridElemSize, gridElemSize, gridElemSize);
                }
                c.pulseCell(grid, toNeutralize, toRevitalize);
            }
        }

        System.out.println(toNeutralize.size());
        pulseGrid();
    }

    public void update(GameContainer gc, int delta) throws SlickException {
        //
    }

    public static void main(String[] args) {
        try {
            AppGameContainer game = new AppGameContainer(new Main("Cellular Automaton - Conway's Game of Life"));
            game.setDisplayMode(gridX * gridElemSize, gridY * gridElemSize, false);
            game.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}
