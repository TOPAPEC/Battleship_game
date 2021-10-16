package battleship;

import javax.lang.model.type.NullType;
import java.util.List;

public class Ship {
    public List<Coordinate> alivePoints;
    private final int[] SHIP_ID_TO_LENGTH = {5, 4, 3, 2, 1};

    public Ship(List<Coordinate> alivePoints) {
        this.alivePoints = alivePoints;
    }

    public static Ship createShip(int id, List<Coordinate> shipCoordinates) {
        switch (id) {
            case 0:
                return new Carrier(shipCoordinates);
            case 1:
                return new Battleship(shipCoordinates);
            case 2:
                return new Cruiser(shipCoordinates);
            case 3:
                return new Destroyer(shipCoordinates);
            case 4:
                return new Submarine(shipCoordinates);
            default:
                return null;
        }
    }

    // Здесь будут методы для нанесения урону кораблю, его восстановления и уничтожения торпедой.

    public void sunkMessage() {
        System.out.println("You just have sunk a Ship");
    }
}
