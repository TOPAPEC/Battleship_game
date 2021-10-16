package battleship;

public class Game {
    Board board;

    public Game() {

    }

    public void initialiseBoard(int height, int width) {
        board = new Board(height, width);
    }

    public boolean tryToFillBoard(int[] shipNums, boolean suppressErrors) {
        boolean isFieldFilled = board.fillField(shipNums);
        if (!isFieldFilled && !suppressErrors) {
            printFillingError();
        }
        return isFieldFilled;
    }

    public boolean fillTheBoardWithMultipleAttempts(int attemptsToPlaceShips,
                                                    int[] shipNums) {
        boolean failedToPlaceShips = false;
        for (int i = 0; i < attemptsToPlaceShips; ++i) {
            if (tryToFillBoard(shipNums, true)) {
                failedToPlaceShips = true;
                break;
            }
        }
        if (failedToPlaceShips) {
            printFillingError();
        }
        return failedToPlaceShips;
    }

    public void run() {

    }

    private void printStartTips() {

    }

    public void printFillingError() {
        System.out.println("I failed to fill the field " +
                "with multiple attempts. Please try decreasing" +
                " ship count or increasing field size. " +
                "You will be redirected to input stage again.");
    }

    public void printBoard() {
        board.printBoard();
    }

    public void printBoardDebug() {
        board.printBoardDebug();
    }


}
