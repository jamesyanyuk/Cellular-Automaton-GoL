import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import java.util.*;

public class Main extends BasicGame {

    private static int gridX = 200;
    private static int gridY = 140;
    private static int gridElemSize = 5;

    private static final int MODE_DRAW = 0;
    private static final int MODE_SIMULATE = 1;
    private static int mode = MODE_DRAW;

    Cell[][] grid = new Cell[gridX][gridY];

    public Stack<Cell> toNeutralize = new Stack();
    public Stack<Cell> toRevitalize = new Stack();

    private Input input;

    private int runningTime = 0;
    private int updateTime = 30;

    public Main(String title) {
        super(title);
    }

    private void initGrid() {
        for(int r = 0; r < gridX; r++) {
            for(int c = 0; c < gridY; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
    }

    private void pulseGrid() {
        // Update board by iterating through stacks
        // (neutralize and revitalize stacks)

        //System.out.println("Pulsing grid...");

        while(!toNeutralize.isEmpty()) {
            toNeutralize.pop().neutralize();
        }

        while(!toRevitalize.isEmpty()) {
            toRevitalize.pop().revitalize();
        }
    }

    private void catchInput(GameContainer gc, int delta) {
        if(input.isKeyPressed(Input.KEY_P)) {
            runningTime = 0;

            if(mode == MODE_DRAW) mode = MODE_SIMULATE;
            else if(mode == MODE_SIMULATE) mode = MODE_DRAW;
        }
    }

    private void placeCell(double x, double y) {
        int xPos = (int) x / gridElemSize;
        int yPos = (int) y / gridElemSize;

        grid[xPos][yPos].revitalize();
    }

    private void removeCell(double x, double y) {
        int xPos = (int) x / gridElemSize;
        int yPos = (int) y / gridElemSize;

        grid[xPos][yPos].neutralize();
    }

    public void init(GameContainer gc) throws SlickException {
        input = gc.getInput();

        //gc.setMinimumLogicUpdateInterval(500);
        gc.setTargetFrameRate(60);
        gc.setAlwaysRender(true);
        gc.setVSync(true);

        initGrid();
    }

    public void render(GameContainer gc, Graphics g) throws SlickException {
        // Set background color to white
        g.setBackground(Color.white);

        g.setColor(Color.black);

        g.drawString("Cellular Automaton - Conway's Game of Life", 5, 0);
        g.drawString("Press 'P' to toggle drawing mode (default)", 5, 20);
        g.drawString("Mode: " + (mode == MODE_DRAW ? "Draw" : "Simulation"), 5, gridY * gridElemSize - 20);

        // Draw cell grid
        for(Cell[] r : grid) {
            for (Cell c : r) {
                if (c.alive)
                    g.fillRect(c.x * gridElemSize, c.y * gridElemSize, gridElemSize, gridElemSize);
            }
        }
    }

    public void update(GameContainer gc, int delta) throws SlickException {
        // Catch any key or mouse input
        catchInput(gc, delta);

        runningTime += delta;

        if (mode == MODE_SIMULATE && runningTime >= updateTime) {
            for (Cell[] r : grid) {
                for (Cell c : r) {
                    c.pulseCell(grid, toNeutralize, toRevitalize, gridX, gridY);
                }
            }

            pulseGrid();
            runningTime = 0;
        } else if (mode == MODE_DRAW) {
            if (input.isMouseButtonDown(0))
                placeCell(input.getMouseX(), input.getMouseY());
            if (input.isMouseButtonDown(1))
                removeCell(input.getMouseX(), input.getMouseY());
        }
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
