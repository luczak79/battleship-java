package battleship;

import java.util.Scanner;

public class Main {

    private static final String PASS_MSG = "\nPress Enter and pass the move to another player";
    private static final String PLACE_SHIP_PLAYER1_MSG = "\nPlayer 1, place your ships on the game field\n";
    private static final String PLACE_SHIP_PLAYER2_MSG = "\nPlayer 2, place your ships on the game field\n";
    private static final String PLAYER1_TURN_MSG = "\nPlayer 1, it's your turn:";
    private static final String PLAYER2_TURN_MSG = "\nPlayer 2, it's your turn:";
    private static final String DIVIDER_MSG = "---------------------";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Battlefield player1Battlefield = new Battlefield();
        Battlefield player2Battlefield = new Battlefield();

        System.out.println(PLACE_SHIP_PLAYER1_MSG);
        player1Battlefield.printBattlefield();
        player1Battlefield.askForPlacingShip();

        System.out.println(PASS_MSG);
        scanner.nextLine();

        System.out.println(PLACE_SHIP_PLAYER2_MSG);
        player2Battlefield.printBattlefield();
        player2Battlefield.askForPlacingShip();

        System.out.println(PASS_MSG);
        scanner.nextLine();

        System.out.println("The game starts!");

        boolean player1Turn = true;
        boolean gameFinished = false;

        while (!gameFinished) {
            if (player1Turn) {
                player2Battlefield.printFogBattlefield();
                System.out.println(DIVIDER_MSG);
                player1Battlefield.printBattlefield();
                System.out.println(PLAYER1_TURN_MSG);

                gameFinished = player2Battlefield.takeShot();
            } else {
                player1Battlefield.printFogBattlefield();
                System.out.println(DIVIDER_MSG);
                player2Battlefield.printBattlefield();
                System.out.println(PLAYER2_TURN_MSG);

                gameFinished = player1Battlefield.takeShot();
            }

            if (!gameFinished) {
                System.out.println(PASS_MSG);
                scanner.nextLine();
                player1Turn = !player1Turn;
            }
        }

    }

}
