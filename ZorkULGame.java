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

import java.util.Scanner;

public class ZorkULGame {
    private Parser parser;
    private Character player;
    private int moveCount;
    private int energyLevel;
    private int hungerLevel;

    public ZorkULGame() {
        createRooms();
        parser = new Parser();
        moveCount = 0;
        energyLevel = 50;
        hungerLevel = 50; // initial hunger level
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
        outside.addMenuOption("Cheeseburger", 5.99);
        outside.addMenuOption("Veggie Burger", 4.49);

        Coqbul.addMenuOption("Double Burger", 7.99);
        Coqbul.addMenuOption("Chicken Burger", 6.49);

        SuperMacs.addMenuOption("Fish Burger", 5.49);
        SuperMacs.addMenuOption("Spicy Burger", 6.99);
        //Creat items
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
        
        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                moveCount++;
                energyLevel = energyLevel - 5;
                hungerLevel = hungerLevel - 3;
                 // check for game over conditions
                if(energyLevel <= 0 || hungerLevel <= 0) {
                    System.out.println("You have run out of juice and DIED. better luck next time!");
                    return true; // End the game
                }
                // use the Character's energy-bar method
                player.displayEnergyBar(energyLevel);
                player.displayHungerBar(hungerLevel);
                
                 
                break;
            case "take":  // Handle take command
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

            case "drop":  // Handle drop command
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

                case "inventory":  // Handle inventory command
                    System.out.println(player.getInventoryString());
                    break;

                case "Menu":
                    player.getCurrentRoom().DisplayMenu();
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
    //enerrgy bar method
    // private void displatEnergyBar(){
    //     System.out.println("Energy level");
    //             String bar = "[" +"*".repeat(energyLevel)+ "]";
    //             if(energyLevel != 50){
    //                 int goneEnergy = (50 - energyLevel);
    //                 bar = "=".repeat(energyLevel)+ " ".repeat(goneEnergy); 
    //             }
    //             System.out.println("["+bar+"]");
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
