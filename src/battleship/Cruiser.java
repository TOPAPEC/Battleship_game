package battleship;

import java.util.List;

public class Cruiser extends Ship {
    public Cruiser(Coordinate[] alivePoints) {
        super(alivePoints);
    }

    @Override
    public void sunkMessage() {
        System.out.println("You just have sunk a Cruiser");
    }
}
