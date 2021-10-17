package battleship;

import java.util.List;

public class Submarine extends Ship {
    public Submarine(Coordinate[] alivePoints) {
        super(alivePoints);
    }

    @Override
    public void sunkMessage() {
        System.out.println("You just have sunk a Submarine");
    }
}
