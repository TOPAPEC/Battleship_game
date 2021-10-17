package battleship;

public class Coordinate {
    public int x;
    public int y;


    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Coordinate coordinate2 = (Coordinate)obj;

        return x == coordinate2.x && y == coordinate2.y;
    }

}
