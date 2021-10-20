package battleship;

import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        welcomeMessage();
        int attemptsToPlaceShips = 10;
        InputParser inputParser = new InputParser();
        if (args.length == 3) {
            inputParser.tryParseCommandLineInput(args);
        } else {
            inputParser.tryParseConsoleInput();
        }

        inputParser.sequentialConsoleParse();

        Game game = new Game();
        boolean failedToPlaceShips = false;
        do {
            if (failedToPlaceShips) {
                inputParser.parsedInput.correct = false;
                inputParser.sequentialConsoleParse();
            }
            game.initialiseBoard(inputParser.parsedInput.height,
                    inputParser.parsedInput.width);
            failedToPlaceShips = game
                    .fillTheBoardWithMultipleAttempts(
                            attemptsToPlaceShips,
                            inputParser.parsedInput.shipNums
                    );
        } while (failedToPlaceShips);
        game.run();
        System.out.println("Thanks for playing\n" +
                "by TOPAPEC");
    }

    private static void printIncorrectInputError() {
        System.out.println("Incorrect input. Please input data" +
                " through console again.");
    }

    private static void welcomeMessage() {
        System.out.println("Welcome! Fullscreen is recommended.");
    }

    private static int[] parseShipNumsString(String s) {
        return Arrays.stream(s.split(","))
                .mapToInt(Integer::parseInt).toArray();
    }

}
