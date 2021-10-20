package battleship;

import java.util.Arrays;

/**
 * Base class for all the ships in the game.
 */
public class Ship {
    public Coordinate[] shipPoints;
    private boolean[] isPointSunk;
    private int remainingPoints;

    private static final int[] SHIP_ID_TO_LENGTH = {5, 4, 3, 2, 1};

    /**
     * Initialises ship's points and hit status.
     *
     * @param alivePoints points, where ship is situated.
     */
    public Ship(Coordinate[] alivePoints) {
        this.shipPoints = alivePoints;
        this.isPointSunk = new boolean[shipPoints.length];
        this.remainingPoints = alivePoints.length;
    }

    /**
     * Fabric for shiptypes.
     *
     * @param id              if of ship in descending by length order.
     * @param shipCoordinates array of ship coordinates.
     * @return constructed ship with specific type determined by id.
     */
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

    /**
     * Finds out if passed coordinates belongs to ship's ones.
     * If so ship takes damage.
     *
     * @param coordinate coordinates of shot.
     * @return true if ship is hit, false otherwise.
     */
    public boolean tryToDamage(Coordinate coordinate) {
        for (int i = 0; i < shipPoints.length; ++i) {
            if (shipPoints[i].equals(coordinate)) {
                if (!isPointSunk[i]) {
                    --remainingPoints;
                }
                isPointSunk[i] = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if ship is sunk.
     *
     * @return true if ship is sunk, false otherwise.
     */
    public boolean isShipSunk() {
        return remainingPoints == 0;
    }

    /**
     * "Regenerates" all the damaged points of the ship.
     */
    public void regenerate() {
        Arrays.fill(isPointSunk, false);
        remainingPoints = isPointSunk.length;
    }

    /**
     * Prints specific sunk message for every type of ship.
     */
    public void sunkMessage() {
        System.out.println("You just have sunk a Ship");
    }

    /**
     * If ship object is equal to another object.
     *
     * @param obj object ot compare to.
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Ship ship2 = (Ship) obj;

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
