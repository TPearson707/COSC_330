public class AttackResult {
    private boolean hit;
    private boolean sunk;
    private String shipName;

    public AttackResult(boolean hit, boolean sunk, String shipName) {
        this.hit = hit;
        this.sunk = sunk;
        this.shipName = shipName;
    }

    public boolean isHit() { 
        return hit;
    }
    
    public boolean isSunk() { 
        return sunk;
    }
    
    public String getShipName() {
        return shipName; 
    }

    public String serialize() {
        return (hit ? "1" : "0") + "," 
         + (sunk ? "1" : "0") + "," 
         + (shipName != null ? shipName : "");
    }
    
    public static AttackResult deserialize(String serialized) {
        String[] parts = serialized.split(",", 3); // Split into 3 parts max
        boolean hit = parts[0].equals("1");
        boolean sunk = parts[1].equals("1");
        String shipName = parts.length > 2 ? parts[2] : "";
        return new AttackResult(hit, sunk, shipName.isEmpty() ? null : shipName);
    }

    @Override
    public String toString() {
        if (hit) {
            return sunk ? "Hit! " + shipName + " has been sunk!" : "Hit! " + shipName;
        } else {
            return "Miss! No ship at this location.";
        }
    }
}
