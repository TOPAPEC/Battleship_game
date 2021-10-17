package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class InputParser {
    public ParsedInput parsedInput;
    Scanner scanner;

    public InputParser() {
        this.parsedInput = new ParsedInput();
        scanner = new Scanner(System.in);
    }

    public void tryParseCommandLineInput(String[] args) {
        try {
            System.out.println("Initialising with command line input");
            parsedInput.correct = true;
            parseShipNums(args[2]);
            parseHeight(args[0]);
            parseWidth(args[1]);
        } catch (Exception exception) {
            printIncorrectInputError();
            parsedInput.correct = false;
        }
    }

    private void parseHeight(String heightString) {
        int height = Integer.parseInt(heightString);
        if (height < 5 || height > 30) {
            parsedInput.correct = false;
        }
        parsedInput.height = height;
    }

    private void parseWidth(String widthString) {
        int width = Integer.parseInt(widthString);
        if (width < 5 || width > 30) {
            parsedInput.correct = false;
        }
        parsedInput.width = width;
    }

    public void tryParseConsoleInput() {
        try {
            System.out.println("Initialising with console input:");
            printConsoleInputTip();
            parsedInput.correct = true;
            parseInputString(scanner.nextLine());
        } catch (Exception exception) {
            printIncorrectInputError();
            parsedInput.correct = false;
        }
    }

    public void sequentialConsoleParse() {
        while (!parsedInput.correct) {
            tryParseConsoleInput();
            if (!parsedInput.correct) {
                printInputLimitsError();
            }
        }
    }

    public void parseInputString(String inputString) {
        String[] splitInput = inputString.split(" ");
        parseHeight(splitInput[0]);
        parseWidth(splitInput[1]);
        parseShipNums(splitInput[2]);
    }


    private void printIncorrectInputError() {
        System.out.println("Incorrect input. Please " +
                "input data through console again.");
    }

    private void parseShipNums(String s) {
        int[] shipNums = Arrays.stream(s.split(","))
                .mapToInt(Integer::parseInt).toArray();
        for (int shipNum : shipNums) {
            if (shipNum > 5 || shipNum < 0) {
                parsedInput.correct = false;
                break;
            }
        }
        parsedInput.shipNums = shipNums;
    }

    public void printConsoleInputTip() {
        System.out.println("Your input should look like:\n" +
                "height width carrierNum,battleshipNum," +
                "cruiserNum,destroyerNum,submarineNum");
    }

    public void printInputLimitsError() {
        System.out.println("""
                Entered values do not match possible limits:
                height and width should be within [5;30].
                Each shipNumber should be within [0;10].""");
    }


}
