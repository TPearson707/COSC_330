public class Model {
    private static Model instance = null;
    public Player[] players;
    private int currentPlayerIndex;
    private boolean gameOver;
    private boolean randomPlacement;
    private boolean isMyTurn;
    // Constructor: Initialize players and the game state
    private Model() {
        players = new Player[2];
        players[0] = new Player("Player 1");
        players[1] = new Player("Player 2");
        currentPlayerIndex = 0;
        gameOver = false;
        randomPlacement = false;
        isMyTurn = false;
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    // Start the game and set up ships
    public void startGame() {

        if (randomPlacement) {
            System.out.println("Starting the game...");

            System.out.println("Placing ships...");
            placeShipsRandomly(players[currentPlayerIndex].getBoard());
            System.out.println("Finished place player " + (currentPlayerIndex + 1) + "'s ships...");

        } else {
            System.out.println("Starting the game...");
        }   
    }

    public void placeShipsRandomly(Board board) {
        String[] shipTypes = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        char[] directions = {'N', 'E', 'S', 'W'};
        
        for (String shipType : shipTypes) {
            boolean placed = false;
            while (!placed) {
                int row = (int)(Math.random() * Board.GRID_ROWS);
                int col = (int)(Math.random() * Board.GRID_COLUMNS);
                char direction = directions[(int)(Math.random() * 4)];
                
                board.addShipToBoard(shipType, row, col, direction);
                if (shipIsOnBoard(board.getGrid(), shipType)) {
                    placed = true;
                }
            }
        }
    }

    private boolean shipIsOnBoard(String[][] grid, String shipType) {
        for (int i = 0; i < Board.GRID_ROWS; i++) {
            for (int j = 0; j < Board.GRID_COLUMNS; j++) {
                if (grid[i][j].equals(shipType)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public Player getOpponent() {
        if (currentPlayerIndex == 0) {
            return players[1];
        } else {
            return players[0];
        }
    }

    public boolean getRandomPlacement() {
        return randomPlacement;
    }

    public void setCurrentPlayerIndex(int player) {
        currentPlayerIndex = player;
    }


    public void setTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public void setRandomPlacement(boolean randomPlacement) {
        this.randomPlacement = randomPlacement;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    // Handle an attack
    public AttackResult performAttack(int playerIndex, int row, int col) {
        if (gameOver) {
            System.err.println("Game is already over. Cannot perform attack.");
            return null;
        }
    
        Player attacker = players[playerIndex];
        Player opponent = players[(playerIndex + 1) % 2];
    
        if (attacker == null || opponent == null) {
            System.err.println("Invalid player index. Attacker or opponent is null.");
            return null;
        }
    
        AttackResult result = attacker.shootAt(opponent, row, col);
        if (result == null) {
            System.err.println("shootAt returned null. Check implementation.");
            return null;
        }
    
        System.out.println(attacker.getName() + " attacks (" + row + ", " + col + "): " + result);
    
        if (checkGameOver()) {
            gameOver = true;
        }
    
        return result;
    }

    // Check if all ships are sunk
    private boolean checkGameOver() {

        System.out.println("Checking if game is over...");
    
        boolean player1ShipsSunk = players[0].getBoard().allShipsSunk();
        boolean player2ShipsSunk = players[1].getBoard().allShipsSunk();
        
        System.out.println("Player 1 ships all sunk: " + player1ShipsSunk);
        System.out.println("Player 2 ships all sunk: " + player2ShipsSunk);
        
        if (player1ShipsSunk) {
            System.out.println("All of player 1 ships were sunk, player 2 wins!");
            return true;
        } else if (player2ShipsSunk) {
            System.out.println("All of player 2 ships were sunk, player 1 wins!");
            return true;
        }
        
        return false;
    }

    // Add ships to a player's board
    public void addShipToBoard(int playerIndex, String shipType, int row, int col, char direction) {
        System.out.println("Adding ship: " + shipType + " to player " + (playerIndex + 1) + " at (" + row + ", " + col + ") with direction " + direction);
        players[playerIndex].getBoard().addShipToBoard(shipType, row - 1, col - 1, direction);
    }

    public void addShipToBoard(Player player, String shipType, int row, int col, char direction) {
        System.out.println("Adding ship: " + shipType + " to player " + (getCurrentPlayerIndex() + 1) + " at (" + row + ", " + col + ") with direction " + direction);
        player.getBoard().addShipToBoard(shipType, row - 1, col - 1, direction);
    }


    public void printBoard(int playerIndex) {
        Board board = players[playerIndex].getBoard();

        board.printGrid();
    }

    public void printAllBoards() {
        for (Player player : players) {
            System.out.println(player.getName() + "'s Board:");
            player.getBoard().printGrid();
        }
    }

    public void printShips(int playerIndex) {
        Board board = players[playerIndex].getBoard();

        board.printAllShips();
    }

    public void printAllShips() {
        for (Player player : players) {
            System.out.println(player.getName() + "'s Ships:");
            player.getBoard().printAllShips();
        }
    }
}
