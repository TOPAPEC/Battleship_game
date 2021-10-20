package battleship;

/**
 * Class for ships' coordinates.
 */
public class Coordinate {
    public int x;
    public int y;


    /**
     * Constructs object from x, y coordinates.
     *
     * @param x x-axis coordinate.
     * @param y y-axis coordinate.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if object is equal to instance of this class.
     *
     * @param obj object to compare to.
     * @return true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Coordinate coordinate2 = (Coordinate) obj;

        return x == coordinate2.x && y == coordinate2.y;
    }

}
