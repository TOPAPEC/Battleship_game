package battleship;

public class ParsedInput {
    public int height;
    public int width;
    public int[] shipNums;
    public boolean correct;

    public ParsedInput(int height, int width, int[] shipNums) {
        this.height = height;
        this.width = width;
        this.shipNums = shipNums;
        this.correct = true;
    }

    public ParsedInput() {
        this.correct = false;
    }
}
