package com.blackOlives;

import model.CardMap;
import model.Player;
import test.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DoubleSkinGame {

    public static final boolean isSideKill = true;
    public static final int waitTime = 10000;
    public static final Test testHelper = new Test();
    public static final Utility util = new Utility();
    public static final boolean testMode = false;
    public static final int testCases = 10000;
    public static final boolean isSevenPlayers = false;
    public static final String promptForSeventhPlayer = (isSevenPlayers?",7 ":" ");
    public static final List<String> silencerAvailableIndex = isSevenPlayers?Arrays.asList("1", "2", "3", "4", "5", "6", "7", "n")
            :Arrays.asList("1", "2", "3", "4", "5", "6", "n");
    public static final List<String> availableIndex = isSevenPlayers?Arrays.asList("1", "2", "3", "4", "5", "6", "7")
            :Arrays.asList("1", "2", "3", "4", "5", "6");


    public static void main(String[] args) throws InterruptedException {

        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to Double Skin - version 0.1.0. This software is developed by Henry Wang, " +
                    "henrybryantwang@gmail.com, \na property of Black Olive Studio. Copyright 2020. GLHF!");
            playGame(sc);
            System.out.print("Do you want to play another round? Y/N: ");
            String anotherRound = sc.nextLine();
            if (anotherRound.equalsIgnoreCase("N")) break;
        }

        System.out.println("GAME ENDED! GOOD NIGHT!");
    }

    public static void playGame(Scanner sc) throws InterruptedException {
        boolean isGunSmithFired = false;

        if (testMode) {
            System.out.println("Testing Player Assignment...");
            testHelper.testPlayerList(testCases, isSevenPlayers);
            System.out.println("Test Success");
        }

        System.out.println("Assigning players...");

        List<Player> playerList = util.assignPlayers(isSevenPlayers);

        decideOrder(playerList, sc);

        boolean isVillager = isVillagerExists(playerList);

        while (true) {
            System.out.print("Ready to start game? Y/N ");
            String isReady = sc.nextLine();
            if (isReady.equalsIgnoreCase("y")) break;
        }
        System.out.println("GAME STARTING! PLEASE CLOSE YOUR EYES!");
        Thread.sleep(3000);

        while (true) {
            String silencedPlayer="", killedPlayer="", injectedPlayer="", gunnedPlayer="";
            if (isCharacterExist(playerList, "silencer")) {
                silencedPlayer = getChosenPlayer(sc, playerList, "Silencer");
            } else {
                System.out.println("Silencer can click n to not silence any player");
                System.out.println("Silencer please choose a player: 1,2,3,4,5,6" + promptForSeventhPlayer);
                Thread.sleep(waitTime);
                createEmptySpace();
            }

            if (isKillerExist(playerList)) {
                killedPlayer = getChosenPlayer(sc, playerList, "Killer");
            } else {
                System.out.println("Killer please choose a player: 1,2,3,4,5,6" + promptForSeventhPlayer);
                Thread.sleep(waitTime);
                createEmptySpace();
            }

            if (isCharacterExist(playerList, "police")) {
                policeTurn(sc, playerList);
            } else {
                System.out.println("Police please choose a player: 1,2,3,4,5,6" + promptForSeventhPlayer);
                Thread.sleep(waitTime);
                createEmptySpace();
            }

            if (isCharacterExist(playerList, "doctor")) {
                injectedPlayer = getChosenPlayer(sc, playerList, "Doctor");
            } else {
                System.out.println("Doctor please choose a player: 1,2,3,4,5,6" + promptForSeventhPlayer);
                Thread.sleep(waitTime);
                createEmptySpace();
            }

            if (!isGunSmithFired && isCharacterExist(playerList, "gunSmith")) {
                gunnedPlayer = getGunnedPlayer(sc, playerList);
                if (!gunnedPlayer.isEmpty()) isGunSmithFired=true;
            } else {
                System.out.println("Gun Smith do you want to use your ability? Y/N ");
                Thread.sleep(waitTime);
                createEmptySpace();
            }

            List<String> deadPlayers = getDeadPlayers(killedPlayer, injectedPlayer, gunnedPlayer, playerList);

            updatePlayerStatus(playerList, deadPlayers);

            // Printing the results of current night
            if (silencedPlayer.isEmpty()) {
                System.out.println("No one has been silenced");
            } else {
                System.out.println("Player " + silencedPlayer + " has been silenced!");
            }
            if (deadPlayers.isEmpty()) {
                System.out.println("No one has been killed!");
            }
            for (String deadPlayer: deadPlayers) {
                System.out.println("Player " + deadPlayer + " has been killed!");
            }

            // Checks if game is over
            if (badGuysWon(playerList, isVillager) || goodGuysWon(playerList)) break;

            System.out.print("Gun Smith Fires? 1,2,3,4,5,6" + (isSevenPlayers?",7":"") +",N ");
            String gunSmithAction = sc.nextLine();
            if (availableIndex.contains(gunSmithAction)) {
                if (!playerList.get(Integer.parseInt(gunSmithAction)-1).getCard1().isEmpty()) {
                    playerList.get(Integer.parseInt(gunSmithAction)-1).setCard1("");
                } else {
                    playerList.get(Integer.parseInt(gunSmithAction)-1).setCard2("");
                }
                isGunSmithFired = true;
            }

            if (badGuysWon(playerList, isVillager) || goodGuysWon(playerList)) break;

            vote(sc,playerList);

            if (badGuysWon(playerList, isVillager) || goodGuysWon(playerList)) break;
        }

        System.out.println("GAME OVER!");
        if (badGuysWon(playerList, isVillager)) {
            System.out.println("Bad Guys Won!");
        } else {
            System.out.println("Good Guys Won!");
        }
    }

    private static boolean isVillagerExists(List<Player> playerList) {
        return playerList.stream().anyMatch(a -> a.getCategory().equalsIgnoreCase("villager"));
    }

    private static boolean isCharacterExist(List<Player> playerList, String character) {
        for (Player player: playerList) {
            if (player.getCard1().equalsIgnoreCase(character) && !player.isNeutralized()) {
                return true;
            } else if (player.getCard1().isEmpty() && player.getCard2().equalsIgnoreCase(character) && !player.isNeutralized()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isKillerExist(List<Player> playerList) {
        for (Player player: playerList) {
            if (player.getCard1().equalsIgnoreCase("killer") ||
                    player.getCard1().equalsIgnoreCase("killerMaster") ||
                    player.getCard2().equalsIgnoreCase("killerMaster")) return true;
            else if (player.getCard1().isEmpty() && player.getCard2().equalsIgnoreCase("killer")) return true;
        }
        return false;
    }

    private static void vote(Scanner sc, List<Player> playerList) {
        while (true) {
            System.out.print("Please vote out one player: 1,2,3,4,5,6" + (isSevenPlayers?",7 ":" "));
            String voted = sc.nextLine();
            if (availableIndex.contains(voted)) {
                if (!playerList.get(Integer.parseInt(voted)-1).getCard1().isEmpty()) {
                    playerList.get(Integer.parseInt(voted)-1).setCard1("");
                    playerList.get(Integer.parseInt(voted)-1).setPoisonRecord(0);
                } else if (!playerList.get(Integer.parseInt(voted)-1).getCard2().isEmpty()) {
                    playerList.get(Integer.parseInt(voted)-1).setCard2("");
                } else {
                    System.out.println("Voted player is already dead. Please vote again.");
                    continue;
                }
            }
            System.out.print("Keep voting? Y/N (Selecting N will continue to next night) ");
            String keepVoting = sc.nextLine();
            if (keepVoting.equalsIgnoreCase("n")) break;
        }
    }

    private static void updatePlayerStatus(List<Player> playerList, List<String> deadPlayers) {
        for (String deadPlayer: deadPlayers) {
            if (!playerList.get(Integer.parseInt(deadPlayer)-1).getCard1().isEmpty()) {
                playerList.get(Integer.parseInt(deadPlayer)-1).setCard1("");
            } else {
                playerList.get(Integer.parseInt(deadPlayer)-1).setCard2("");
            }
            playerList.get(Integer.parseInt(deadPlayer)-1).setPoisonRecord(0); // reset poison record to 0
        }
    }

    private static List<String> getDeadPlayers(String killedPlayer, String injectedPlayer, String gunnedPlayer,
                                               List<Player> playerList) {
        List<String> deadPlayers = new ArrayList<>();
        if (!gunnedPlayer.isEmpty()) deadPlayers.add(gunnedPlayer);

        if (!killedPlayer.equals(injectedPlayer) && !killedPlayer.isEmpty() && !deadPlayers.contains(killedPlayer))
            deadPlayers.add(killedPlayer);

        if (!injectedPlayer.isEmpty() && !killedPlayer.equals(injectedPlayer)) {
            playerList.get(Integer.parseInt(injectedPlayer)-1).increasePoison();
            if (playerList.get(Integer.parseInt(injectedPlayer)-1).getPoisonRecord() == 2) {
                if (!deadPlayers.contains(injectedPlayer)) {
                    deadPlayers.add(injectedPlayer);
                }
            }
        }

        return deadPlayers;
    }

    private static String getGunnedPlayer(Scanner sc, List<Player> playerList) {
        System.out.println("Gun Smith do you want to use your ability? Y/N ");
        String isGun = sc.nextLine();
        if (isGun.equalsIgnoreCase("N")) {
            createEmptySpace();
            return "";
        } else {
            return getChosenPlayer(sc, playerList, "Gun Smith");
        }
    }

    private static void policeTurn(Scanner sc, List<Player> playerList) throws InterruptedException {
        String checkedPlayer;
        String currentIdentity;
        while (true) {
            System.out.println("Police please choose a player to check: 1,2,3,4,5,6" + promptForSeventhPlayer);
            checkedPlayer = sc.nextLine();
            if (!availableIndex.contains(checkedPlayer)) {
                System.out.println("Please choose a valid player to check!");
            } else if (playerList.get(Integer.parseInt(checkedPlayer)-1).getCard2().isEmpty()) {
                System.out.println("The player you chose is dead. Please choose again!");
            } else {
                if (!playerList.get(Integer.parseInt(checkedPlayer)-1).getCard1().isEmpty()) {
                    currentIdentity = playerList.get(Integer.parseInt(checkedPlayer)-1).getCard1();
                } else {
                    currentIdentity = playerList.get(Integer.parseInt(checkedPlayer)-1).getCard2();
                }
                if (playerList.get(Integer.parseInt(checkedPlayer)-1).isNeutralized()) {
                    currentIdentity = "villager";
                }
                break;
            }
        }
        CardMap cardMap = new CardMap();
        String identity = cardMap.getMap().get(currentIdentity) < 0 ? "Bad": "Good";
        System.out.println("The current skin of the selected player is: " + identity);
        Thread.sleep(5000);
        createEmptySpace();
    }

    private static String getChosenPlayer(Scanner sc, List<Player> playerList, String character) {
        String chosenPlayer;
        while (true) {
            List<String> available = null;
            if (character.equalsIgnoreCase("Silencer")) {
                available = silencerAvailableIndex;
                System.out.println("Silencer can click n to not silence any player");
            } else {
                available = availableIndex;
            }
            System.out.println(character + " please choose a player: 1,2,3,4,5,6" + promptForSeventhPlayer);
            chosenPlayer = sc.nextLine();
            if (!available.contains(chosenPlayer)) {
                System.out.println("Please choose a valid player!");
            } else if (chosenPlayer.equalsIgnoreCase("n") && character.equalsIgnoreCase("Silencer")) {
                chosenPlayer = "";
                break;
            } else if (playerList.get(Integer.parseInt(chosenPlayer)-1).getCard2().isEmpty()) {
                System.out.println("The player you chose is dead. Please choose again!");
            } else break;
        }
        createEmptySpace();
        return chosenPlayer;
    }

    private static boolean goodGuysWon(List<Player> playerList) {
        return playerList.stream().noneMatch(a -> a.getIdentity().equals("B") && !a.getCard2().isEmpty());
    }

    private static boolean badGuysWon(List<Player> playerList, boolean isVillagerExists) {
        if (isSideKill && isVillagerExists) {
            int god = 0;
            int villager = 0;
            for (Player player: playerList) {
                if (!player.getCard2().isEmpty() && player.getCategory().equalsIgnoreCase("god")) {
                    god++;
                } else if (!player.getCard2().isEmpty() && player.getCategory().equalsIgnoreCase("villager")) {
                    villager++;
                }
            }
            return god == 0 || villager == 0;
        } else {
            return isAllGoodPlayersDead(playerList);
        }
    }

    private static Boolean isAllGoodPlayersDead(List<Player> playerList) {
        return playerList.stream().noneMatch(a -> a.getIdentity().equalsIgnoreCase("G")
                && !a.getCard2().isEmpty());
    }

    private static void decideOrder(List<Player> playerList, Scanner sc) {
        int playerIndex = 1;
        for (Player player: playerList) {
            while(true) {
                System.out.print("Ready to see player " + playerIndex + " Cards? Y/N ");
                String readyToSeeCards = sc.nextLine();
                if (readyToSeeCards.equalsIgnoreCase("Y")) {
                    System.out.println("Card 1: " + (player.isNeutralized()?"villager":player.getCard1()));
                    System.out.println("Card 2: " + (player.isNeutralized()?"villager":player.getCard2()));
                    System.out.println("Identity: " + player.getIdentity());
                    System.out.print("Do you want to re-arrange? Y/N ");
                    String rearrange = sc.nextLine();
                    if (rearrange.equalsIgnoreCase("Y")) {
                        String temp = player.getCard1();
                        player.setCard1(player.getCard2());
                        player.setCard2(temp);
                    }
                    break;
                }
            }
            createEmptySpace();
            playerIndex++;
        }
    }

    private static void createEmptySpace() {
        System.out.println("*****************************");
        for (int i=0;i<20;i++) {
            System.out.println();
        }
        System.out.println("=============================");
    }

}
