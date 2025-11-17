/* This game is a classic text-based adventure set in a university environment.
   The player starts outside the main entrance and can navigate through different rooms like a 
   lecture Coqbul, campus SuperMacs, computing lockeBurger, and admin burgerMac using simple text commands (e.g., "go east", "go west").
    The game provides descriptions of each location and lists possible exits.

Key features include:
Room navigation: Moving among interconnected rooms with named exits.
Simple command parser: Recognizes a limited set of commands like "go", "help", and "quit".
Player character: Tracks current location and handles moving between rooms.
Text descriptions: Provides immersive text output describing the player's surroundings and available options.
Help system: Lists valid commands to guide the player.
Overall, it recreates the classic Zork interactive fiction experience with a university-themed setting, 
emphasizing exploration and simple command-driven gameplay
*/

import java.security.Key;
import java.util.Scanner;

public class ZorkULGame {
    private Parser parser;
    private Character player;
    private int moveCount;
    private int energyLevel;
    private int hungerLevel;
    private double balance;

    public ZorkULGame() {
        createRooms();
        parser = new Parser();
        moveCount = 0;
        energyLevel = 50;
        hungerLevel = 50; // initial hunger level
        balance = 20.0;

    }

    private void createRooms() {
        Room outside, Coqbul, SuperMacs, lockeBurger, burgerMac, SomeDodgeyChipper, brownThomas;

        // create rooms
        outside = new Room("Your outide, the restaurants are waiting to serve you.");
        Coqbul = new Room("Welcome to Coqbul some succulent food in this place.");
        SuperMacs = new Room("Inside SuperMacs Diner. The fries here are almost as salty as the staff's jokes.");
        lockeBurger = new Room("At LockeBurger Bistro, where the burgers are deep and the thoughts are deeper.");
        burgerMac = new Room("You've entered Burger Mac Shack. The cook hasn't left the grill since 1997.");
        SomeDodgeyChipper = new Room("Welcome to Some Dodgey Chipper. The mystery meat is half the fun.");
        brownThomas = new Room("Alone by the legendary Brown Thomas Statue. Even the pigeons eye your snacks.");

        // Set up food menus for each room
        // NEW (correct format with numbers)
        outside.addMenuOption(1, "Cheeseburger", 5.99);
        outside.addMenuOption(2, "Veggie Burger", 4.49);

        Coqbul.addMenuOption(1, "Double Burger", 7.99);
        Coqbul.addMenuOption(2, "Chicken Burger", 6.49);

        SuperMacs.addMenuOption(1, "Fish Burger", 5.49);
        SuperMacs.addMenuOption(2, "Spicy Burger", 6.99);

        lockeBurger.addMenuOption(1, "Classic Burger", 8.50);
        lockeBurger.addMenuOption(2, "Premium Burger", 9.99);

        burgerMac.addMenuOption(1, "Mac Burger", 7.50);
        burgerMac.addMenuOption(2, "Deluxe Mac", 8.99);

        SomeDodgeyChipper.addMenuOption(1, "Mystery Burger", 5.50);
        SomeDodgeyChipper.addMenuOption(2, "Dodgey Special", 6.50);

        // Creat items
        Item key = new Item("Coupon", "a cheeky discount coupon for your next meal");

        // Place items
        outside.addItem(key);

        // initialise room exits
        outside.setExit("east", Coqbul);
        outside.setExit("south", lockeBurger);
        outside.setExit("west", SuperMacs);
        outside.setExit("north", brownThomas);

        Coqbul.setExit("west", outside);
        Coqbul.setExit("east", SomeDodgeyChipper);

        brownThomas.setExit("south", outside);

        SuperMacs.setExit("east", outside);

        lockeBurger.setExit("north", outside);
        lockeBurger.setExit("east", burgerMac);

        burgerMac.setExit("west", lockeBurger);
        burgerMac.setExit("north", SomeDodgeyChipper);

        SomeDodgeyChipper.setExit("west", Coqbul);
        SomeDodgeyChipper.setExit("south", burgerMac);
        Item item = new Item("Generic Box", "This is a generic box holding a item.");

        // create the player character and start outside
        player = new Character("player", outside);
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        while (!finished) {

            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Goodbye.");
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the University adventure!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());

    }

    private boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();

        if (commandWord == null) {
            System.out.println("I don't understand your command...");
            return false;
        }
        // check for game over conditions
                if (energyLevel <= 0 || hungerLevel <= 0) {
                    System.out.println("You have run out of juice and DIED. better luck next time!");
                    return true; // End the game
                }
        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                moveCount++;
                energyLevel = energyLevel - 1;
                hungerLevel = hungerLevel - 5;
                

                // use the Character's energy-bar method
                player.displayEnergyBar(energyLevel);
                hungerLevel = Math.max(0, hungerLevel);
                player.displayHungerBar(hungerLevel);

                break;
            case "take": // Handle take command
                if (command.hasSecondWord()) {
                    String itemName = command.getSecondWord();
                    if (player.takeItem(player.getCurrentRoom(), itemName)) {
                        System.out.println("You picked up: " + itemName);
                    } else {
                        System.out.println("That item is not here!");
                    }
                } else {
                    System.out.println("Take what?");
                }
                break;

            case "drop": // Handle drop command
                if (command.hasSecondWord()) {
                    String itemName = command.getSecondWord();
                    if (player.dropItem(player.getCurrentRoom(), itemName)) {
                        System.out.println("You dropped: " + itemName);
                    } else {
                        System.out.println("You don't have that item!");
                    }
                } else {
                    System.out.println("Drop what?");
                }
                break;

            case "inventory": // Handle inventory command
                System.out.println(player.getInventoryString());
                break;

            case "Menu":
                player.getCurrentRoom().DisplayMenu();
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter the number of the item you wish to buy:");
                System.out.println("Your balance is: £" + balance);
                int input = sc.nextInt();
                PurchaseResult result = player.getCurrentRoom().orderItem(balance, input, hungerLevel);
                hungerLevel = result.hungerLevel;
                player.displayHungerBar(hungerLevel);                
                balance = result.balance;

                break;

            case "quit":
                if (command.hasSecondWord()) {
                    System.out.println("Quit what?");
                    return false;
                } else {
                    return true; // signal to quit
                }

            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return false;
    }
    // enerrgy bar method
    // private void displatEnergyBar(){
    // System.out.println("Energy level");
    // String bar = "[" +"*".repeat(energyLevel)+ "]";
    // if(energyLevel != 50){
    // int goneEnergy = (50 - energyLevel);
    // bar = "=".repeat(energyLevel)+ " ".repeat(goneEnergy);
    // }
    // System.out.println("["+bar+"]");
    // }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander around the university.");
        System.out.print("Your command words are: ");
        parser.showCommands();
        System.out.println(moveCount);
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            player.setCurrentRoom(nextRoom);
            System.out.println(player.getCurrentRoom().getLongDescription());
        }
    }

    public static void main(String[] args) {
        ZorkULGame game = new ZorkULGame();
        game.play();
    }
}
