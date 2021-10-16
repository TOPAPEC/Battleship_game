package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class Board {
    private final int width, height;
    private char[][] board;
    private char[][] board_revealed_ships;
    private List<Ship> shipList;
    private int shotsDone;
    private int cellsOccupied;
    private final int[] SHIP_ID_TO_LENGTH = {5, 4, 3, 2, 1};
    private final int[][] DELTAS_FOR_BRUSHING =
            {{0, -1}, {0, 1}, {-1, 0}, {-1, -1},
                    {-1, 1}, {1, 0}, {1, -1}, {1, 1}, {0, 0}};
    private final int[][] DELTAS_FOR_DIRECTIONS = {{-1, 0}, {0, 1},
            {1, 0}, {0, -1}};
    private final double INITIAL_PROBABILITY_FOR_10_BY_10_TABLE = 0.04;

    /**
     * Initialises all essential parameters and places ships on the battlefield.
     * X - hit cell with a ship.
     * * - hit cell without a ship.
     * . - not fired cell.
     * S - cell with sunk ship.
     * E is reserved for debug board, E - enemy's ship.
     *
     * @param width  Width of field to generate.
     * @param height Height of field to generate.
     */
    public Board(int height, int width) {
        this.width = width;
        this.height = height;
        this.shotsDone = 0;
        this.shipList = new ArrayList<>();
    }

    private void reinitializeBoard(int height, int width) {
        shipList.clear();
        board = new char[height][width];
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                board[i][j] = '.';
            }
        }
        board_revealed_ships = new char[height][width];
        for (int i = 0; i < board_revealed_ships.length; ++i) {
            for (int j = 0; j < board_revealed_ships[0].length; ++j) {
                board_revealed_ships[i][j] = '.';
            }
        }
    }

    public void printBoard() {
        for (char[] cellRow : board) {
            for (char cell : cellRow) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private void fillDebugBoard() {
        for (Ship ship : shipList) {
            for (Coordinate coordinate : ship.alivePoints) {
                board_revealed_ships[coordinate.x][coordinate.y] = 'E';
            }
        }
    }

    public void printBoardDebug() {
        for (char[] cellRow : board_revealed_ships) {
            for (char cell : cellRow) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private boolean checkIfCanPlaceShip(int x, int y, int direction, int id) {
        int shipLength = SHIP_ID_TO_LENGTH[id];
        for (int shift = 0; shift < shipLength - 1; ++shift) {
            x += DELTAS_FOR_DIRECTIONS[direction][0];
            y += DELTAS_FOR_DIRECTIONS[direction][1];
            if (!(checkIfWithinTheBoard(x, y) && board[x][y] == '.')) {
                return false;
            }
        }
        return true;
    }

    private void brushAllNearbyCells(int x, int y) {
        for (int[] delta : DELTAS_FOR_BRUSHING) {
            int nx = delta[0] + x;
            int ny = delta[1] + y;
            if (checkIfWithinTheBoard(nx, ny)) {
                board[nx][ny] = '*';
            }
        }
    }

    private void placeShip(int x, int y, int direction, int id) {
        List<Coordinate> shipCoordinates = new ArrayList<>();
        int shipLength = SHIP_ID_TO_LENGTH[id];
        shipCoordinates.add(new Coordinate(x, y));
        brushAllNearbyCells(x, y);
        for (int shift = 0; shift < shipLength - 1; ++shift) {
            x += DELTAS_FOR_DIRECTIONS[direction][0];
            y += DELTAS_FOR_DIRECTIONS[direction][1];
            shipCoordinates.add(new Coordinate(x, y));
            brushAllNearbyCells(x, y);
        }
        shipList.add(Ship.createShip(id, shipCoordinates));
    }

    private boolean checkIfWithinTheBoard(int nx, int ny) {
        return nx >= 0 && nx < width && ny >= 0 && ny < height;
    }

    public boolean fillField(int[] shipNums) {
        reinitializeBoard(height, width);
        Random rand = new Random();
        int iteration_count = 0;
        for (int shipType = 0; shipType < shipNums.length; ++shipType) {
            for (int shipNumber = 0; shipNumber < shipNums[shipType];
                 ++shipNumber) {
                boolean breakFlag = false;
                for (int attemptToPlace = 0; attemptToPlace < 10;
                     ++attemptToPlace) {
                    for (int i = 0; i < board.length; ++i) {
                        for (int j = 0; j < board[0].length; ++j) {
                            int direction = rand.nextInt(3);
                            if (board[i][j] == '.' &&
                                    randomDesicionIfShouldPlace(rand) &&
                                    checkIfCanPlaceShip(i, j,
                                            direction, shipType)) {
                                placeShip(i, j, direction, shipType);
                                breakFlag = true;
                                break;
                            }
                        }
                        if (breakFlag)
                            break;
                    }
                    if (breakFlag)
                        break;
                }
                if (!breakFlag) {
                    return false;
                }
            }
        }
        fillDebugBoard();
        return true;
    }

    private boolean randomDesicionIfShouldPlace(Random rand) {
        double probability = INITIAL_PROBABILITY_FOR_10_BY_10_TABLE /
                (width * height / 100.0);
        return rand.nextDouble() < probability;
    }

}
