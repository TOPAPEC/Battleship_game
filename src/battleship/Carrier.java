package battleship;

import java.util.List;

public class Carrier extends Ship {
    public Carrier(List<Coordinate> alivePoints) {
        super(alivePoints);
    }

    @Override
    public void sunkMessage() {
        System.out.println("You just have sunk a Carrier");
    }
}
