package battleship;

/**
 * Class for storing parsed input for game initialisation with
 * the flag that indicates if the contained input is correct
 */
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
