package battleship;

import java.util.List;

public class Battleship extends Ship {
    public Battleship(Coordinate[] alivePoints) {
        super(alivePoints);
    }

    @Override
    public void sunkMessage() {
        System.out.println("You just have sunk a Battleship");
    }
}
