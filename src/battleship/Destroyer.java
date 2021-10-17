package battleship;

import java.util.List;

public class Destroyer extends Ship {
    public Destroyer(Coordinate[] alivePoints) {
        super(alivePoints);
    }

    @Override
    public void sunkMessage() {
        System.out.println("You just have sunk a Destroyer");
    }
}
