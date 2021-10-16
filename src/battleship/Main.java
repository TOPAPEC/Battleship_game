package battleship;

import java.util.Arrays;


// Предоставить примеры готовых входных данных, которые точно должны отработать.
// Написать в доках, почему я выбрал те или иные вероятности.
public class Main {
    public static void main(String[] args) {
        int attemptsToPlaceShips = 10;
        InputParser inputParser = new InputParser();
        if (args.length == 3) {
            inputParser.tryParseCommandLineInput(args);
        }
        inputParser.sequentialConsoleParse();

        Game game = new Game();
        boolean failedToPlaceShips = false;
        do {
            if (failedToPlaceShips) {
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

    }

    private static void printIncorrectInputError() {
        System.out.println("Incorrect input. Please input data" +
                " through console again.");
    }

    public static ParsedInput parseInputString(String inputString) {
        String[] splitInput = inputString.split(" ");
        int height = Integer.parseInt(splitInput[0]);
        int width = Integer.parseInt(splitInput[1]);
        int[] shipNums = parseShipNumsString(splitInput[2]);
        return new ParsedInput(height, width, shipNums);
    }

    private static int[] parseShipNumsString(String s) {
        return Arrays.stream(s.split(","))
                .mapToInt(Integer::parseInt).toArray();
    }

    public static void printConsoleInputTip() {
        System.out.println("Your input should look like:\n" +
                "height width carrierNum,battleshipNum,cruiserNum," +
                "destroyerNum,submarineNum");
    }
}
