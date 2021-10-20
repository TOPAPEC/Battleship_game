package battleship;

import java.util.List;

public class Submarine extends Ship {
    public Submarine(Coordinate[] alivePoints) {
        super(alivePoints);
    }

    /**
     * Prints specific sunk message for every type of ship.
     */
    @Override
    public void sunkMessage() {
        System.out.println("You just have sunk a Submarine");
    }
}
