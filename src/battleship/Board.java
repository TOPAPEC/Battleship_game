package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private final int width, height;
    private char[][] board;
    private char[][] board_revealed_ships;
    private List<Ship> shipList;
    private Ship lastHitShip;
    private int shotsDone;
    private int cellsOccupied;
    private static final int[] SHIP_ID_TO_LENGTH = {5, 4, 3, 2, 1};
    private static final int[][] DELTAS_FOR_BRUSHING =
            {{0, -1}, {0, 1}, {-1, 0}, {-1, -1},
                    {-1, 1}, {1, 0}, {1, -1}, {1, 1}, {0, 0}};
    private static final int[][] DELTAS_FOR_DIRECTIONS = {{-1, 0}, {0, 1},
            {1, 0}, {0, -1}};
    private static final double INITIAL_PROBABILITY_FOR_10_BY_10_TABLE = 0.04;

    /**
     * @return width of the board.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return height of the board.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Initialises all essential parameters and
     * places ships on the battlefield.
     * Marks meanings:
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

    /**
     * Prints game board with special marks:
     * * X - hit cell with a ship.
     * * - hit cell without a ship.
     * . - not fired cell.
     * S - cell with sunk ship.
     * E is reserved for debug board, E - enemy's ship.
     *
     * @param printDebugBoard if should print the board with revealed
     *                        ships (for evaluation and debug purposes).
     */
    public void printBoard(boolean printDebugBoard) {
        char[][] boardToPrint = board;
        if (printDebugBoard) {
            boardToPrint = board_revealed_ships;
        }
        for (int i = 0; i < boardToPrint.length; ++i) {
            for (int j = 0; j < boardToPrint[0].length; ++j) {
                if (j == 0) {
                    System.out.print(i + "\t");
                }
                System.out.print(boardToPrint[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
        System.out.print("\t");
        for (int i = 0; i < boardToPrint[0].length; ++i) {
            System.out.print(i + "\t");
        }
        System.out.println();
    }

    /**
     * Makes one attempt to fill the field with stated
     * numbers of each shiptype.
     *
     * @param shipNums numbers of each ship.
     * @return true if the field was successfully filled, false otherwise.
     */
    public boolean fillField(int[] shipNums) {
        reinitializeBoard(height, width);
        Random rand = new Random();
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
        refillBoard();
        return true;
    }

    /**
     * @return current number of ships on board.
     */
    public int getCurrentShipNumber() {
        return shipList.size();
    }

    /**
     * @return true if all ships are destroyed, false otherwise.
     */
    public boolean checkIfAllShipsAreDestroyed() {
        return shipList.size() == 0;
    }

    /**
     * Imitates launch of torpedo to stated coordinates.
     *
     * @param coordinate coordinates to launch torpedo to.
     * @return true if torpedo hit the ship.
     */
    public boolean tryToTorpedoCoordinates(Coordinate coordinate) {
        for (Ship ship : shipList) {
            if (ship.tryToDamage(coordinate)) {
                ship.sunkMessage();
                markShipAsSunk(ship);
                shipList.remove(ship);
                return true;
            }
        }
        return false;
    }

    /**
     * Imitates shot in stated coordinates.
     * Supports recovery logic.
     *
     * @param coordinate            coordinate to shoot.
     * @param isRecoveryModeEnabled if the recovery mode is enabled.
     * @return true if the ship is hit, false otherwise.
     */
    public boolean tryToDamageShip(Coordinate coordinate,
                                   boolean isRecoveryModeEnabled) {
        for (Ship ship : shipList) {
            if (ship.tryToDamage(coordinate)) {
                if (isRecoveryModeEnabled && lastHitShip != null) {
                    tryToRegenerate(ship);
                }
                board[coordinate.x][coordinate.y] = 'X';
                if (ship.isShipSunk()) {
                    ship.sunkMessage();
                    markShipAsSunk(ship);
                    shipList.remove(ship);
                    lastHitShip = null;
                } else {
                    lastHitShip = ship;
                }
                return true;
            }
        }
        if (isRecoveryModeEnabled && lastHitShip != null) {
            lastHitShip.regenerate();
            markShipAsCovered(lastHitShip);
            lastHitShip = null;
        }
        return false;
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

    private void fillDebugBoard() {
        for (Ship ship : shipList) {
            for (Coordinate coordinate : ship.shipPoints) {
                board_revealed_ships[coordinate.x][coordinate.y] = 'E';
            }
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
        int shipLength = SHIP_ID_TO_LENGTH[id];
        Coordinate[] shipCoordinates = new Coordinate[shipLength];
        shipCoordinates[0] = new Coordinate(x, y);
        brushAllNearbyCells(x, y);
        for (int shift = 0; shift < shipLength - 1; ++shift) {
            x += DELTAS_FOR_DIRECTIONS[direction][0];
            y += DELTAS_FOR_DIRECTIONS[direction][1];
            shipCoordinates[shift + 1] = new Coordinate(x, y);
            brushAllNearbyCells(x, y);
        }
        shipList.add(Ship.createShip(id, shipCoordinates));
    }

    private void refillBoard() {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                board[i][j] = '.';
            }
        }
    }

    private boolean randomDesicionIfShouldPlace(Random rand) {
        double probability = INITIAL_PROBABILITY_FOR_10_BY_10_TABLE /
                (width * height / 100.0);
        return rand.nextDouble() < probability;
    }

    private boolean checkIfWithinTheBoard(int nx, int ny) {
        return nx >= 0 && nx < width && ny >= 0 && ny < height;
    }


    private void tryToRegenerate(Ship presentlyHitShip) {
        if (!presentlyHitShip.equals(lastHitShip)) {
            lastHitShip.regenerate();
            markShipAsCovered(lastHitShip);
            System.out.println("Previously hit ship regenerated!");
        }
    }

    private void markShipAsSunk(Ship ship) {
        for (Coordinate cell : ship.shipPoints) {
            board[cell.x][cell.y] = 'S';
            board_revealed_ships[cell.x][cell.y] = 'S';
        }
    }

    private void markShipAsCovered(Ship ship) {
        for (Coordinate cell : ship.shipPoints) {
            board[cell.x][cell.y] = '.';
            board_revealed_ships[cell.x][cell.y] = 'E';
        }
    }

}
