import java.util.HashMap;
import java.util.Map;

public class Board {

    // Constants for grid size
    public static final int GRID_ROWS = 10;
    public static final int GRID_COLUMNS = 10;

    // Grid and ships storage
    private String[][] grid;
    private HashMap<String, Ship> ships;

    // Constructor initializes grid and ships
    public Board() {
        grid = new String[GRID_ROWS][GRID_COLUMNS];
        ships = new HashMap<>();
        initializeGrid();  // Initialize the grid on creation
        initializeShips(); // Initialize the ships
    }

    // Method to initialize the grid with water
    private void initializeGrid() {
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLUMNS; j++) {
                grid[i][j] = "~"; // Represents water
            }
        }
    }

    public int getGridRows() {
        return GRID_ROWS;
    }

    public int getGridColumns() {
        return GRID_COLUMNS;
    }

    // Initialize ships with name and length
    private void initializeShips() {
        ships.put("Carrier", new Ship("Carrier", 5));
        ships.put("Battleship", new Ship("Battleship", 4));
        ships.put("Cruiser", new Ship("Cruiser", 3));
        ships.put("Submarine", new Ship("Submarine", 3));
        ships.put("Destroyer", new Ship("Destroyer", 2));
    }

    public void addCarrier() {
        ships.put("Carrier", new Ship("Carrier", 5));
    }

    public void addBattleship() {
        ships.put("Battleship", new Ship("Battleship", 4));
    }

    // Method to set a value in the grid
    public void setGrid(int x, int y, String value) {
        grid[x][y] = value;
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }

    // Accessor method for the grid
    public String[][] getGrid() {
        return grid;
    }

    // Method to get a ship from the HashMap
    public Ship getShip(String shipType) {
        return ships.get(shipType);
    }

    // Method to return the ships hashmap
    public HashMap<String, Ship> getShips() {
        return ships;
    }

    // Method to clear all ships from the board
    public void clearShips() {
        ships.clear();
    }

    // Method to check if placing a ship at the given position will cause a collision
    private boolean checkCollision(int row, int col, int length, char direction) {
        // Check bounds
        if (direction == 'N') {
            if (row - (length - 1) < 0) {
                return true; // Ship will not fit vertically north
            }
            for (int i = 0; i < length; i++) {
                if (!grid[row - i][col].equals("~")) {
                    return true; // Collision with another ship
                }
            }
        } else if (direction == 'E') {  // East placement
            if (col + length > GRID_COLUMNS) {
                return true;  // Ship will not fit horizontally east
            }
            for (int i = 0; i < length; i++) {
                if (!grid[row][col + i].equals("~")) {
                    return true;  // Collision with another ship
                }
            }
        } else if (direction == 'S') {  // South placement
            if (row + length > GRID_ROWS) {
                return true;  // Ship will not fit vertically south
            }
            for (int i = 0; i < length; i++) {
                if (!grid[row + i][col].equals("~")) {
                    return true;  // Collision with another ship
                }
            }
        } else if (direction == 'W') {
            if (col - (length - 1) < 0) {
                return true; // Ship will not fit horizontally west
            }
            for (int i = 0; i < length; i++) {
                if (!grid[row][col - i].equals("~")) {
                    return true; // Collision with another ship
                }
            }
        }
        return false;  // No collision
    }

    // Method to add a ship to the board at the specified location and direction N, E, S, W
    public void addShipToBoard(String shipType, int row, int col, char direction) {
        // Check if the ship exists
        if (ships.containsKey(shipType)) {
            Ship ship = ships.get(shipType);
            int length = ship.getLength();

            // Check if the ship can be placed without collision
            if (checkCollision(row, col, length, direction)) {
                System.out.println("ERROR: Collision detected! Cannot place " + shipType);
                return;  // Exit if collision occurs
            }

            // If no collision, place the ship on the grid
            if (direction == 'N') { // North placement
                for (int i = 0; i < length; i++) {
                    grid[row - i][col] = shipType;
                }
            } else if (direction == 'E') {  // East placement
                for (int i = 0; i < length; i++) {
                    grid[row][col + i] = shipType;
                }
            } else if (direction == 'S') {  // South placement
                for (int i = 0; i < length; i++) {
                    grid[row + i][col] = shipType;
                }
            } else if (direction == 'W') { // West placement
                for (int i = 0; i < length; i++) {
                    grid[row][col - i] = shipType;
                }
            }
            row++;
            col++;
            System.out.println(shipType + " successfully placed at (" + row + ", " + col + ") facing " + direction);
        } else {
            System.out.println("ERROR: Unable to add " + shipType + " this ship does not exist!");
        }
    }

    public AttackResult attackCell(int x, int y) {
        String gridValue = grid[x][y];
        if (!gridValue.equals("~")) {
            // If it's not water, it's a hit
            Ship ship = ships.get(gridValue);
            if (ship != null) {
                ship.markHit();
                grid[x][y] = "~";
                boolean sunk = ship.isSunk();
                return new AttackResult(true, sunk, ship.getName());
            }
        }
        return new AttackResult(false, false, null);
    }

    public boolean allShipsSunk() {
        System.out.println("Checking if all ships are sunk...");
        
        for (Map.Entry<String, Ship> entry : ships.entrySet()) {
            String shipName = entry.getKey();
            Ship ship = entry.getValue();
            boolean isSunk = ship.isSunk();
            
            System.out.println("Ship: " + shipName + ", Length: " + ship.getLength() + 
                              ", Hits: " + ship.getHitCount() + ", Sunk: " + isSunk);
            
            if (!isSunk) {
                System.out.println("Not all ships are sunk.");
                return false;
            }
        }
        
        System.out.println("All ships are sunk!");
        return true;
    }

    // Method to print the grid
    public void printGrid() {
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLUMNS; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to print all ships
    public void printAllShips() {
        if (ships.isEmpty()) {
            System.out.println("No ships available.");
            return;
        }
        System.out.println("Ships that are initialized:");
        for (Ship ship : ships.values()) {
            System.out.println("Ship Name: " + ship.getName() + ", Length: " + ship.getLength() + ", Hits: " + ship.getHitCount());
        }
    }

    public boolean isShipAt(int row, int col) {
        // Validate bounds
        if (row < 0 || row >= GRID_ROWS || col < 0 || col >= GRID_COLUMNS) {
            return false; // Out of bounds
        }

        // Check if the grid cell contains a ship (not water)
        return !grid[row][col].equals("~");
    }

    public String getShipNameAt(int row, int col) {
        // Validate bounds
        if (row < 0 || row >= GRID_ROWS || col < 0 || col >= GRID_COLUMNS) {
            return null; // Out of bounds
        }

        // Return the ship name if the cell contains a ship
        String cellValue = grid[row][col];
        return cellValue.equals("~") ? null : cellValue;
    }

    public boolean isShipSunk(String shipName) {
        // Check if the ship exists in the HashMap
        if (ships.containsKey(shipName)) {
            Ship ship = ships.get(shipName);
            return ship.isSunk(); // Delegate to Ship's isSunk method
        }
        return false; // Ship not found
    }
}