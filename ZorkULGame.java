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

    public ZorkULGame() {
        createRooms();
        parser = new Parser();
        moveCount = 0;
        energyLevel = 50;
    }

    private void createRooms() {
        Room outside, Coqbul, SuperMacs, lockeBurger, burgerMac, SomeDodgeyChipper, brownThomas;

        // create rooms
        outside = new Room("outside the main entrance of the university");
        Coqbul = new Room("in a lecture Coqbul");
        SuperMacs = new Room("in the campus SuperMacs");
        lockeBurger = new Room("in a computing lockeBurger");
        burgerMac = new Room("in the computing admin burgerMac");
        SomeDodgeyChipper = new Room("in the SomeDodgeyChipper");
        brownThomas = new Room("You are on a room alone with the icocnic Brown Thomas Statue");

        //Creat items
        Item key = new Item("key", "a rusty old key");
        Item book = new Item("book", "a thick Java programming book");
        Item laptop = new Item("laptop", "a modern laptop computer");
        Item mug = new Item("mug", "a coffee mug with university logo");

        // Place items 
        outside.addItem(key);
        Coqbul.addItem(book);
        lockeBurger.addItem(laptop);
        SuperMacs.addItem(mug);

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
                if(energyLevel <= 0) {
                    System.out.println("You have run out of energy and DIED. better luck next time!");
                    return true; // End the game
                }
                displatEnergyBar();
                
                 
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
    private void displatEnergyBar(){
        System.out.println("Energy level");
                String bar = "[" +"*".repeat(energyLevel)+ "]";
                if(energyLevel != 50){
                    int goneEnergy = (50 - energyLevel);
                    bar = "=".repeat(energyLevel)+ " ".repeat(goneEnergy); 
                }
                System.out.println("["+bar+"]");
    }

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
