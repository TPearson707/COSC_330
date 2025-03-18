public class Ship { 

    // Data Members
    private int length;
    private int hitCount;
    private String name;

    // Constructor
    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.hitCount = 0; // initialize hit count to 0
    }

    // Getter Methods
    public int getLength() {
        return length;
    }

    public int getHitCount() {
        return hitCount;
    }

    public String getName() {
        return name;
    }

    // Method to mark a hit on the ship
    public void markHit() {
        if (hitCount < length) {
            hitCount++;
        }
    }

    // Method to check if the ship is sunk
    public boolean isSunk() {
        return hitCount >= length;
    }
}