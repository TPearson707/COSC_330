public class Player {
    private String name;
    private Board board;


    public Player(String name) {
        this.name = name;
        this.board = new Board();
    }

    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }


    public AttackResult shootAt(Player opponent, int row, int col) {
        if (opponent == null || opponent.getBoard() == null) {
            System.err.println("Opponent or their board is null.");
            return null;
        }
    
        Board opponentBoard = opponent.getBoard();
        boolean hit = opponentBoard.isShipAt(row, col);
        String shipName = hit ? opponentBoard.getShipNameAt(row, col) : null;
        boolean sunk = hit && opponentBoard.isShipSunk(shipName);
    
        return new AttackResult(hit, sunk, shipName);
    }
}
