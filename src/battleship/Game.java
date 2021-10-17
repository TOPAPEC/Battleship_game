package battleship;


import java.util.Scanner;

public class Game {
    Board board;
    boolean isTorpedoModeEnabled;
    boolean isRecoveryModeEnabled;
    int remainingTorpedoes;
    Scanner scanner;

    public Game() {
        scanner = new Scanner(System.in);
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
        // Incorrect!
        boolean failedToPlaceShips = true;
        for (int i = 0; i < attemptsToPlaceShips; ++i) {
            if (tryToFillBoard(shipNums, true)) {
                failedToPlaceShips = false;
                break;
            }
        }
        if (failedToPlaceShips) {
            printFillingError();
        }
        return failedToPlaceShips;
    }

    public void run() {
        torpedoEnablingDialog();
        recoveryModeEnablingDialog();
        boolean exitFlag = false;
        System.out.println("The game begins!");
        while(!checkIfAllShipsAreDestroyed()) {
            printBoard(false);
            printGameCommandList();
            if (!parseAndExecuteCommand()) {
                System.out.println("Goodbye!");
                return;
            }
        }
        System.out.println("You destroyed all ships! This game is over...");
    }

    private boolean parseAndExecuteCommand() {
        String[] commands = scanner.nextLine().split(" ");
        while(!parseCommand(commands)) {
            System.out.println("Incorrect command, please try again.");
            commands = scanner.nextLine().split(" ");
        }
        if (commands[0].equals("T")) {
            int x = Integer.parseInt(commands[1]);
            int y = Integer.parseInt(commands[2]);
            tryToTorpedoCoordinates(new Coordinate(x,y));
        } else if (commands.length == 2) {
            int x = Integer.parseInt(commands[0]);
            int y = Integer.parseInt(commands[1]);
            tryToDamageShipAt(new Coordinate(x,y));
        } else if (commands[0].equals("exit")) {
            return false;
        } else if (commands[0].equals("help")) {
            printGameCommandList();
        } else if (commands[0].equals("printDebug")) {
            printBoard(true);
        }
        return true;
    }

    private void tryToDamageShipAt(Coordinate coordinate) {
        if (board.tryToDamageShip(coordinate, isRecoveryModeEnabled)) {
            System.out.println("Hit!");
        } else {
            System.out.println("Miss!");
        }
    }

    private void notEnoughTorpedoError() {
        System.out.println("Error! You do not have enough torpedoes.");
    }

    private void tryToTorpedoCoordinates(Coordinate coordinate) {
        if (remainingTorpedoes < 1) {
            notEnoughTorpedoError();
            return;
        }
        --remainingTorpedoes;
        board.tryToTorpedoCoordinates(coordinate);
    }

    private boolean parseCommand(String[] commands) {
        try {
            if (commands.length == 3 && commands[0].equals("T")) {
                int x = Integer.parseInt(commands[1]);
                int y = Integer.parseInt(commands[2]);
                if (!checkIfXWithinTheBoard(x) || !checkIfYWithinTheBoard(y)) {
                    return false;
                }
            } else if (commands.length == 2) {
                int x = Integer.parseInt(commands[0]);
                int y = Integer.parseInt(commands[1]);
                if (!checkIfXWithinTheBoard(x) || !checkIfYWithinTheBoard(y)) {
                    return false;
                }
            } else if (commands.length != 1) {
                return false;
            } else if (!commands[0].equals("printDebug") &&
                    !commands[0].equals("help") &&
                    !commands[0].equals("exit")) {
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    private boolean checkIfXWithinTheBoard(int x) {
        return x >= 0 && x < board.getHeight();
    }

    private boolean checkIfYWithinTheBoard(int y) {
        return y >= 0 && y < board.getWidth();
    }

    private void printGameCommandList() {
        System.out.println("" +
                "If you enabled torpedo mode, you can enter:\n" +
                "T x y\n" +
                "to launch torpedo at x y cell\n" +
                "Rest of the commands:\n" +
                "printDebug - print board with enemy ship - for " +
                "debug and evaluation purposes only.\n" +
                "x y - fire the x y cell\n" +
                "help - to view this list again.\n" +
                "exit - to exit game.\n" +
                "Note that all x and y should be within board indexes.");
    }

    private void printStartTips() {
        System.out.println("Welcome to my Battleship game!");
    }

    private void torpedoEnablingDialog() {
        System.out.println("" +
                "Would you like to enable torpedo mode? Print:\n" +
                "yes numberOfTorpedoes\n" +
                "numberOfTorpedoes is in range " +
                "[0, numberOfShips]\n" +
                "or\n" +
                "no\n" +
                "otherwise.");
        String[] commands = scanner.nextLine().split(" ");
        while (!checkIfTorpedoCommandCorrect(commands)) {
            printTorpedoInputError();
            commands = scanner.nextLine().split(" ");
        }
        if (commands[0].equals("yes")) {
            isTorpedoModeEnabled = true;
            remainingTorpedoes = Integer.parseInt(commands[1]);
        } else {
            isTorpedoModeEnabled = false;
            remainingTorpedoes = 0;
        }
    }

    private void recoveryModeEnablingDialog() {
        System.out.println("" +
                "Would you like to enable recovery mode? Print:\n" +
                "yes\n" +
                "or\n" +
                "no\n");
        String command = scanner.nextLine();
        while (!checkIfRecoveryCommandCorrect(command)) {
            printRecoveryInputError();
            command = scanner.nextLine();
        }
        if (command.equals("yes")) {
            isRecoveryModeEnabled = true;
        } else {
            isRecoveryModeEnabled = false;
        }
    }

    private void printTorpedoInputError() {
        System.out.println("Incorrect torpedo mode input!\n" +
                "You should enter:\n" +
                "yes numberOfTorpedoes\n" +
                "numberOfTorpedoes is in range " +
                "[0, numberOfShips]\n" +
                "or\n" +
                "no\n" +
                "otherwise.");
    }

    private void printRecoveryInputError() {
        System.out.println("Incorrect recovery mode input!\n" +
                "You should enter:" +
                "yes\n" +
                "or\n" +
                "no\n");
    }

    private boolean checkIfTorpedoCommandCorrect(String[] commands) {
        boolean isCorrect = true;
        try {
            isCorrect = (commands[0].equals("yes") &&
                    commands.length == 2) ||
                    (commands[0].equals("no") &&
                            commands.length == 1);
            if (isCorrect && commands[0].equals("yes")) {
                int parsedNum = Integer.parseInt(commands[1]);
                isCorrect = parsedNum >= 0 && parsedNum <
                        board.getCurrentShipNumber();
            }
        } catch (Exception exception) {
            return false;
        }
        return isCorrect;
    }

    private boolean checkIfRecoveryCommandCorrect(String command) {
        return command.equals("yes") || command.equals("no");
    }

    public void printFillingError() {
        System.out.println("I failed to fill the field " +
                "with multiple attempts. Please try decreasing" +
                " ship count or increasing field size. " +
                "You will be redirected to input stage again.");
    }


    public void printBoard(boolean printDebugBoard) {
        board.printBoard(printDebugBoard);
    }

    private boolean checkIfAllShipsAreDestroyed() {
        return board.checkIfAllShipsAreDestroyed();
    }



}
