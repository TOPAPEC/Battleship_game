package battleship;

import java.util.Arrays;

public class Ship {
    public Coordinate[] shipPoints;
    public boolean[] isPointSunk;
    private int remainingPoints;

    private static final int[] SHIP_ID_TO_LENGTH = {5, 4, 3, 2, 1};

    public Ship(Coordinate[] alivePoints) {
        this.shipPoints = alivePoints;
        this.isPointSunk = new boolean[shipPoints.length];
        this.remainingPoints = alivePoints.length;
    }

    public static Ship createShip(int id, Coordinate[] shipCoordinates) {
        return switch (id) {
            case 0 -> new Carrier(shipCoordinates);
            case 1 -> new Battleship(shipCoordinates);
            case 2 -> new Cruiser(shipCoordinates);
            case 3 -> new Destroyer(shipCoordinates);
            case 4 -> new Submarine(shipCoordinates);
            default -> null;
        };
    }

    // Здесь будут методы для нанесения урону кораблю, его восстановления и уничтожения торпедой.

    // Попадание в уже поврежденную часть корабля все равно считается попаданием, потому что технически торпеда
    // может попасть в поврежденную часть корабля и уничтожить его полностью.

    public boolean tryToDamage(Coordinate coordinate) {
        for (int i = 0; i < shipPoints.length; ++i) {
            if (shipPoints[i].equals(coordinate)) {
                isPointSunk[i] = true;
                --remainingPoints;
                return true;
            }
        }
        return false;
    }

    public boolean isShipSunk() {
        return remainingPoints == 0;
    }

    public void regenerate() {
        Arrays.fill(isPointSunk, false);
        remainingPoints = isPointSunk.length;
    }


    public void sunkMessage() {
        System.out.println("You just have sunk a Ship");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Ship ship2 = (Ship)obj;

        if (shipPoints.length != ship2.shipPoints.length) {
            return false;
        }

        for (int i = 0; i < shipPoints.length; ++i) {
            if (!shipPoints[i].equals(ship2.shipPoints[i])) {
                return false;
            }
        }

        return true;

    }
}
