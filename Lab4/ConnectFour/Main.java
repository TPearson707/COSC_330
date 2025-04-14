class Main {
    public static void main(String[] args) {
        ConnectFour game = ConnectFour.getInstance();
        game.start();
    }
}

/*
 * Notes:
 * Put the game logic into a separate class away from main
 * used the facade pattern to hide the underlying logic for ease of use
 */
