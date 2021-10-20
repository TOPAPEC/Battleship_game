package battleship;

import java.util.List;

public class Battleship extends Ship {
    public Battleship(Coordinate[] alivePoints) {
        super(alivePoints);
    }

    /**
     * Prints specific sunk message for every type of ship.
     */
    @Override
    public void sunkMessage() {
        System.out.println("You just have sunk a Battleship");
    }
}
