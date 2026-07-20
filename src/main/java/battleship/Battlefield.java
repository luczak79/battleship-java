package battleship;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import static java.util.Map.entry;

public class Battlefield {

    private final char[][] battlefield = new char[10][10];
    private final char fog = '~';
    private final char ship = 'o';
    private final char hit = 'X';
    private final char miss = 'M';

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String WHITE = "\u001B[37m";

    private static final String TOO_CLOSE_ERROR_MSG = "Error! You placed it too close to another one. Try again:";
    private static final String WRONG_LENGTH_ERROR_MSG = "Error! Wrong length of the Submarine! Try again:";
    private static final String WRONG_COORDINATES_ERROR_MSG = "Error! You entered the wrong coordinates! Try again:";
    private static final String HIT_MSG = "\nYou hit a ship! Try again:";
    private static final String MISS_MSG = "\nYou missed! Try again:";
    private static final String SUNK_MSG = "\nYou sank a ship! Specify a new target:";
    private static final String SUNK_LAST_MSG = "\nYou sank the last ship. You won. Congratulations!";

    public void askForPlacingShip() {

        Scanner scanner = new Scanner(System.in);

        String[] shipNames = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};

        for (String shipName : shipNames) {
            System.out.print(battleShipsAskMessages.get(shipName));

            boolean shipPlaced = false;
            while (!shipPlaced) {
                String shipCoordinates = scanner.nextLine();

                if (placeShip(shipCoordinates, shipName)) {
                    String[] shipParts = parseShipParts(shipCoordinates);

                    for (String part : shipParts) {
                        char row = part.charAt(0);
                        int col = Integer.parseInt(part.substring(1));
                        battlefield[row - 'A'][col - 1] = ship;
                    }

                    printBattlefield();
                    System.out.println();
                    shipPlaced = true;
                } else {
                    System.out.print("> ");
                }
            }
        }
    }

    Map<String, String> battleShipsAskMessages = Map.ofEntries(
            entry("Aircraft Carrier", "Enter the coordinates of the Aircraft Carrier (5 cells):\n> "),
            entry("Battleship", "Enter the coordinates of the Battleship (4 cells):\n> "),
            entry("Submarine", "Enter the coordinates of the Submarine (3 cells):\n> "),
            entry("Cruiser", "Enter the coordinates of the Cruiser (3 cells):\n> "),
            entry("Destroyer", "Enter the coordinates of the Destroyer (2 cells):\n> ")
    );

    Map<String, Integer> battleShipsCells = Map.ofEntries(
            entry("Aircraft Carrier", 5),
            entry("Battleship", 4),
            entry("Submarine", 3),
            entry("Cruiser", 3),
            entry("Destroyer", 2)
    );

    public Battlefield() {
        for (char[] row : battlefield) {
            Arrays.fill(row, fog);
        }
    }

    public char[][] getBattlefield() {
        return battlefield;
    }

    public void printBattlefield() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(toColoredDot(battlefield[i][j]) + " ");
            }
            System.out.println();
        }
    }

    private String toColoredDot(char field) {
        if (field == fog) {
            return BLUE + "?" + RESET;
        } else if (field == ship) {
            return GREEN + "S" + RESET;
        } else if (field == hit) {
            return RED + "H" + RESET;
        } else if (field == miss) {
            return WHITE + "M" + RESET;
        }

        return String.valueOf(field);
    }

    public void printFogBattlefield() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < 10; j++) {
                if (battlefield[i][j] == ship) {
                    System.out.print(toColoredDot(fog) + " ");
                } else {
                    System.out.print(toColoredDot(battlefield[i][j]) + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean placeShip(String shipCoordinates, String shipName) {

        if (shipCoordinates == null) {
            System.out.println("Error");
            return false;
        }

        shipCoordinates = shipCoordinates.trim();

        if (!isValidShipCoordinates(shipCoordinates)) {
            System.out.println("Error");
            return false;
        }

        String[] shipParts = parseShipParts(shipCoordinates);

        int expectedLength = battleShipsCells.get(shipName);
        if (shipParts.length != expectedLength) {
            System.out.println(WRONG_LENGTH_ERROR_MSG);
            return false;
        }

        if (isTooCloseToAnotherShip(shipParts)) {
            System.out.println(TOO_CLOSE_ERROR_MSG);
            return false;
        }

        return true;
    }

    private boolean isTooCloseToAnotherShip(String[] shipParts) {
        for (String part : shipParts) {
            char row = part.charAt(0);
            int col = Integer.parseInt(part.substring(1));

            int rowIndex = row - 'A';
            int colIndex = col - 1;

            for (int i = rowIndex - 1; i <= rowIndex + 1; i++) {
                for (int j = colIndex - 1; j <= colIndex + 1; j++) {
                    if (i >= 0 && i < battlefield.length && j >= 0 && j < battlefield[i].length) {
                        if (battlefield[i][j] == ship) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private String[] parseShipParts(String shipCoordinates) {
        String[] coordinates = shipCoordinates.split(" ");

        char firstRow = Character.toUpperCase(coordinates[0].charAt(0));
        int firstCol = Integer.parseInt(coordinates[0].substring(1));
        char secondRow = Character.toUpperCase(coordinates[1].charAt(0));
        int secondCol = Integer.parseInt(coordinates[1].substring(1));

        if (firstRow == secondRow) {
            // Horizontal ship
            int minCol = Math.min(firstCol, secondCol);
            int maxCol = Math.max(firstCol, secondCol);
            int length = maxCol - minCol + 1;
            String[] parts = new String[length];

            for (int i = 0; i < length; i++) {
                parts[i] = "" + firstRow + (minCol + i);
            }
            return parts;
        } else {
            // Vertical ship
            char minRow = (char) Math.min(firstRow, secondRow);
            char maxRow = (char) Math.max(firstRow, secondRow);
            int length = maxRow - minRow + 1;
            String[] parts = new String[length];

            for (int i = 0; i < length; i++) {
                parts[i] = "" + (char) (minRow + i) + firstCol;
            }
            return parts;
        }
    }

    private boolean isValidShipCoordinates(String shipCoordinates) {
        String[] coordinates = shipCoordinates.split(" ");

        if (coordinates.length != 2) {
            return false;
        }

        for (String coord : coordinates) {
            if (coord.length() < 2) {
                return false;
            }

            char letter = Character.toUpperCase(coord.charAt(0));
            if (letter < 'A' || letter > 'J') {
                return false;
            }

            String numberPart = coord.substring(1);
            try {
                int number = Integer.parseInt(numberPart);
                if (number < 1 || number > 10) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        char firstLetter = Character.toUpperCase(coordinates[0].charAt(0));
        char secondLetter = Character.toUpperCase(coordinates[1].charAt(0));

        int firstNumber = Integer.parseInt(coordinates[0].substring(1));
        int secondNumber = Integer.parseInt(coordinates[1].substring(1));

        return firstLetter == secondLetter || firstNumber == secondNumber;

    }

    public boolean takeShot() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\nTake a shot!\n> ");

            String shotCoordinates = scanner.nextLine();

            if (!isValidShotCoordinates(shotCoordinates)) {
                System.out.println(WRONG_COORDINATES_ERROR_MSG);
                continue;
            }

            shotCoordinates = shotCoordinates.trim();

            int rowIndex = Character.toUpperCase(shotCoordinates.charAt(0)) - 'A';
            int colIndex = Integer.parseInt(shotCoordinates.substring(1)) - 1;

            if (battlefield[rowIndex][colIndex] == ship) {
                battlefield[rowIndex][colIndex] = hit;
                printFogBattlefield();

                boolean anyShipLeft = Arrays.stream(battlefield)
                        .anyMatch(row -> new String(row).contains(String.valueOf(ship)));

                if (!anyShipLeft) {
                    System.out.println(SUNK_LAST_MSG);
                    return true;
                } else if (isShipSunk(rowIndex, colIndex)) {
                    System.out.println(SUNK_MSG);
                } else {
                    System.out.println(HIT_MSG);
                }

                return false;
            } else if (battlefield[rowIndex][colIndex] == fog) {
                battlefield[rowIndex][colIndex] = miss;
                printFogBattlefield();
                System.out.println(MISS_MSG);
                return false;
            } else {
                printFogBattlefield();
                System.out.println(MISS_MSG);
                return false;
            }
        }
    }

    private boolean isShipSunk(int rowIndex, int colIndex) {
        boolean[][] visited = new boolean[10][10];
        return isShipSunk(rowIndex, colIndex, visited);
    }

    private boolean isShipSunk(int rowIndex, int colIndex, boolean[][] visited) {
        if (rowIndex < 0 || rowIndex >= 10 || colIndex < 0 || colIndex >= 10) {
            return true;
        }

        if (visited[rowIndex][colIndex]) {
            return true;
        }

        visited[rowIndex][colIndex] = true;

        if (battlefield[rowIndex][colIndex] == ship) {
            return false;
        }

        if (battlefield[rowIndex][colIndex] != hit) {
            return true;
        }

        return isShipSunk(rowIndex - 1, colIndex, visited)
                && isShipSunk(rowIndex + 1, colIndex, visited)
                && isShipSunk(rowIndex, colIndex - 1, visited)
                && isShipSunk(rowIndex, colIndex + 1, visited);
    }

    private boolean isValidShotCoordinates(String shotCoordinates) {
        if (shotCoordinates == null) {
            return false;
        }

        shotCoordinates = shotCoordinates.trim();

        if (shotCoordinates.length() < 2) {
            return false;
        }

        char row = Character.toUpperCase(shotCoordinates.charAt(0));

        if (row < 'A' || row > 'J') {
            return false;
        }

        String columnPart = shotCoordinates.substring(1);

        try {
            int column = Integer.parseInt(columnPart);
            return column >= 1 && column <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
