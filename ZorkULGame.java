
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
import java.io.Serializable;
import java.security.Key;
import java.util.Scanner;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ZorkULGame implements Serializable {
    private static final long serialVersionUID = 1L;
    private Parser parser;
    private Character player;
    private int moveCount;
    private int energyLevel;
    private int hungerLevel;
    private double balance;
    
    // clue progression tracking
    private int cluesFound = 0;
    private boolean puzzleAvailable = false;
    private boolean secretRoomUnlocked = false;
    private boolean couponActive = false;
    // npcs only talk if you've spoken to the previous one in the chain
    private boolean clue1Talked = false;
    private boolean clue2Talked = false;
    private boolean clue3Talked = false;
    private boolean clue4Talked = false;
    private boolean clue5Talked = false;
    private int puzzleQuestion = 0; // tracks which question we're on (0=not started, 1-3)
    private int correctAnswers = 0;

    public ZorkULGame() {
        // setup game world and starting stats
        createRooms();
        parser = new Parser();
        moveCount = 0;
        energyLevel = 50;
        hungerLevel = 50;
        balance = 100.0;

    }

    private void createRooms() {
        // declare all the rooms
        Room outside, Coqbul, SuperMacs, lockeBurger, burgerMac, SomeDodgeyChipper, brownThomas, studentUnion,
                ChickenHut, secretStorage;

        outside = new Room("Your outide, the restaurants are waiting to serve you.");
        Coqbul = new Room("Welcome to Coqbul some succulent food in this place.");
        SuperMacs = new Room("Inside SuperMacs Diner. The fries here are almost as salty as the staff's jokes.");
        lockeBurger = new Room("At LockeBurger Bistro, where the burgers are deep and the thoughts are deeper.");
        burgerMac = new Room("You've entered Burger Mac Shack. The cook hasn't left the grill since 1997.");
        SomeDodgeyChipper = new Room("Welcome to Some Dodgey Chipper. The mystery meat is half the fun.");
        brownThomas = new Room("Alone by the legendary Brown Thomas Statue. Even the pigeons eye your snacks.");
        studentUnion = new Room(
                "The bustling Student Union. Students huddle in groups, complaining about prices and deadlines.");
        ChickenHut = new Room(
                "Welcome to chicken hut with sticky tables and flickering lights. The food is cheap for a reason.");
        secretStorage = new Room(
                "A dusty storage room that smells of old cardboard and forgotten dreams. In the corner, a GOLDEN COUPON glows softly!");

        // setup food menus for each restaurant
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

        SomeDodgeyChipper.addMenuOption(1, "Mystery Burger", 5.00);
        SomeDodgeyChipper.addMenuOption(2, "Dodgey Special", 6.50);

        ChickenHut.addMenuOption(1, "Fried Chicken Burger", 5.99);
        ChickenHut.addMenuOption(2, "Spicy Wings Combo", 6.49);

        // winning item hidden in secret room
        Item coupon = new Item("Coupon", "The key to winning make sure to use it wisely!");

        secretStorage.addItem(coupon);

        // each npc gives a clue to find the next one
        NPC student = new NPC("Student",
                "Well lad You look broke like me. Want the CHEAPEST burger on campus? I don't know where it is, but the COOK at SuperMacs might. He's always complaining about competition. I'm to scared to talk to him, can you?");
        studentUnion.addNPC(student);

        NPC cook = new NPC("Cook",
                "Cheapest burger? Bah! Bad for business. But fine... I heard rich students talking about it near the BROWN THOMAS STATUE. That old statue hears everything. Go there!");
        SuperMacs.addNPC(cook);

        NPC statue = new NPC("Statue",
                "Even statues hear secrets... The answer is guarded by a puzzle. Seek the Manager at LockeBurger. They protect the knowledge you seek...");
        brownThomas.addNPC(statue);

        NPC Manager = new NPC("Manager",
                "Ah, a seeker of savings! I guard the secret passage. Solve my puzzle and the path below will open. Type 'solve' when ready!");
        lockeBurger.addNPC(Manager);

        NPC mysteriousFigure = new NPC("MyteriousFigure",
                "Well done, budget master! The secret: SOME DODGEY CHIPPER sells the MYSTERY BURGER for £5.50 - cheapest on campus! I've left you a golden COUPON here. Use it at the Dodgey Chipper and win!");
        secretStorage.addNPC(mysteriousFigure);
        NPC brownThomasFan = new NPC("BrownThomasFan",
                "Did you hear about the curse of Brown Thomas? They say if you touch it, you fail your exams?");
        brownThomas.addNPC(brownThomasFan);

        // connect all the rooms
        outside.setExit(Direction.EAST, Coqbul);
        outside.setExit(Direction.SOUTH, lockeBurger);
        outside.setExit(Direction.WEST, SuperMacs);
        outside.setExit(Direction.NORTH, brownThomas);

        Coqbul.setExit(Direction.WEST, outside);
        Coqbul.setExit(Direction.EAST, SomeDodgeyChipper);

        brownThomas.setExit(Direction.SOUTH, outside);
        brownThomas.setExit(Direction.NORTH, studentUnion);

        studentUnion.setExit(Direction.SOUTH, brownThomas);

        SuperMacs.setExit(Direction.EAST, outside);
        SuperMacs.setExit(Direction.SOUTH, ChickenHut);

        ChickenHut.setExit(Direction.NORTH, SuperMacs);
        ChickenHut.setExit(Direction.EAST, lockeBurger);

        lockeBurger.setExit(Direction.WEST, ChickenHut);

        lockeBurger.setExit(Direction.NORTH, outside);
        lockeBurger.setExit(Direction.EAST, burgerMac);
        lockeBurger.setExit(Direction.SOUTH, secretStorage);

        secretStorage.setExit(Direction.NORTH, lockeBurger);

        burgerMac.setExit(Direction.WEST, lockeBurger);
        burgerMac.setExit(Direction.NORTH, SomeDodgeyChipper);

        SomeDodgeyChipper.setExit(Direction.WEST, Coqbul);
        SomeDodgeyChipper.setExit(Direction.SOUTH, burgerMac);

        // player starts outside
        player = new Character("player", outside);
    }

    // use reflection to build help text from annotations
    private String getAnnotatedCommands() {
        StringBuilder help = new StringBuilder("Available commands:\n");
        Method[] methods = this.getClass().getDeclaredMethods();
        
        for (Method method : methods) {
            if (method.isAnnotationPresent(GameCommand.class)) {
                GameCommand annotation = method.getAnnotation(GameCommand.class);
                help.append("  ").append(annotation.word())
                    .append(" - ").append(annotation.description()).append("\n");
            }
        }
        return help.toString();
    }

    @GameCommand(word = "take", description = "Pick up an item")
    private void cmdTake() {}
    
    @GameCommand(word = "inventory", description = "Show your items")
    private void cmdInventory() {}
    
    @GameCommand(word = "menu", description = "Buy food (menu 1, menu 2, etc)")
    private void cmdMenu() {}
    
    @GameCommand(word = "talk", description = "Talk to an NPC")
    private void cmdTalk() {}
    
    @GameCommand(word = "solve", description = "Answer puzzle questions")
    private void cmdSolve() {}
    
    @GameCommand(word = "use", description = "Use an item")
    private void cmdUse() {}
    
    @GameCommand(word = "look", description = "Look around the room")
    private void cmdLook() {}
    
    @GameCommand(word = "save", description = "Save your game")
    private void cmdSave() {}
    
    @GameCommand(word = "quit", description = "Exit the game")
    private void cmdQuit() {}

    public void play() {
        printWelcome();

        // main game loop
        boolean finished = false;
        while (!finished) {

            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Goodbye.");
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the Restaurant Run!");
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
        // game over if you run out of energy or hunger
        if (energyLevel <= 0 || hungerLevel <= 0) {
            System.out.println("You have run out of juice and DIED. better luck next time!");
            return true;
        }
        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                // moving drains energy and hunger
                moveCount++;
                energyLevel = energyLevel - 1;
                hungerLevel = hungerLevel - 5;

                player.displayEnergyBar(energyLevel);
                hungerLevel = Math.max(0, hungerLevel);
                player.displayHungerBar(hungerLevel);

                break;
            case "take":
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

            case "inventory":
                System.out.println(player.getInventoryString());
                break;

            case "menu":
                player.getCurrentRoom().DisplayMenu();
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter the number of the item you wish to buy:");
                System.out.println("Your balance is: £" + balance);
                int input = sc.nextInt();
                if (input == 3) {
                    System.out.println("Exiting menu.");
                    break;
                }
                // try to buy food, might not have enough money
                try {
                    PurchaseResult result = player.getCurrentRoom().orderItem(balance, input, hungerLevel);
                    hungerLevel = result.hungerLevel;
                    // player.displayHungerBar(hungerLevel);
                    balance = result.balance;
                } catch (InsufficientBalanceException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }

                break;
            case "talk":
                if (command.hasSecondWord()) {
                    String npcName = command.getSecondWord();
                    NPC npc = player.getCurrentRoom().getNPC(npcName);
                    if (npc != null) {
                        if (!npc.hasSpoken()) {
                            // npcs have to be talked to in order
                            if (npc.getName().equalsIgnoreCase("Student")) {
                                System.out.println(npc.speak());
                                clue1Talked = true;
                                System.out.println("\n[Clue 1/5: Learn about the Cook]");

                            } else if (npc.getName().equalsIgnoreCase("Cook")) {
                                if (clue1Talked) {
                                    System.out.println(npc.speak());
                                    clue2Talked = true;
                                    System.out.println("\n[Clue 2/5: Learn about the Statue]");
                                } else {
                                    System.out.println(
                                            "What do you want? I'm busy. Come back after you've done your homework.");
                                    System.out.println("(You need to talk to the Student first)");
                                }

                            } else if (npc.getName().equalsIgnoreCase("Statue")) {
                                if (clue2Talked) {
                                    System.out.println(npc.speak());
                                    clue3Talked = true;
                                    System.out.println("\n[Clue 3/5: Learn about the Manager]");
                                } else {
                                    System.out.println(
                                            "*The statue stands silent, offering no wisdom to those unprepared.*");
                                    System.out.println("(You need to talk to the Cook first)");
                                }

                            } else if (npc.getName().equalsIgnoreCase("Manager")) {
                                if (clue3Talked) {
                                    System.out.println(npc.speak());
                                    clue4Talked = true;
                                    puzzleAvailable = true;
                                    System.out.println("\n[Clue 4/5: Unlock the puzzle!]");
                                    System.out.println("*** The Manager is ready to test you! ***");
                                    System.out.println("*** Type 'solve' to attempt the puzzle! ***\n");
                                } else {
                                    System.out.println(
                                            "I don't have time for random visitors. Come back when you've earned it.");
                                    System.out.println("(You need to talk to the Statue first)");
                                }

                            } else if (npc.getName().equalsIgnoreCase("MysteriousFigure")
                                    || npc.getName().equalsIgnoreCase("Mysterious Figure")) {
                                if (secretRoomUnlocked) {
                                    System.out.println(npc.speak());
                                    clue5Talked = true;
                                    System.out.println(
                                            "\n[Clue 5/5: FINAL CLUE - Some Dodgey Chipper has the Mystery Burger!]");
                                } else {
                                    // Player found secret room but didn't solve puzzle - confused NPC
                                    System.out.println("Who are you? How did you get down here?");
                                    System.out.println("(You need to solve the Manager's puzzle first)");
                                }

                            } else {
                                // Any other NPC
                                System.out.println(npc.speak());
                            }

                        } else {
                            // Already talked to this NPC
                            System.out.println(npc.speak());
                            System.out.println("(You've already talked to them.)");
                        }
                    } else {
                        System.out.println("There is no one here by that name.");
                    }
                } else {
                    System.out.println("Talk to who?");
                }
                break;
            case "solve":
                // Check if Manager is in this room
                NPC manager = player.getCurrentRoom().getNPC("Manager");
                if (manager == null) {
                    System.out.println("There's no one here to give you a puzzle!");
                    break;
                }

                // Check if player talked to Manager first
                if (!puzzleAvailable) {
                    System.out.println("You need to talk to the Manager first!");
                    break;
                }

                // Check if puzzle already solved
                if (secretRoomUnlocked) {
                    System.out.println("You've already solved this puzzle! The path down is open.");
                    break;
                }

                // Track attempts (max 3)
                int attempts = 0;
                boolean puzzleSolved = false;

                while (attempts < 3 && !puzzleSolved) {
                    attempts++;
                    System.out.println("\n=== THE MANAGER'S CHALLENGE ===");
                    System.out.println("Attempt " + attempts + "/3 - Answer ALL 3 questions correctly!\n");

                    // Track correct answers
                    int correctAnswers = 0;
                    Scanner scanner = new Scanner(System.in);

                    // Question 1
                    System.out.println("QUESTION 1: What is the curse of Brown Thomas?");
                    System.out.println("A) If you touch it, you FAIL your exans!");
                    System.out.println("B) It brings bad luck to your ability to pull");
                    System.out.println("C) If you look at it for too long you get lost in its eyes");
                    System.out.print("Your answer (A/B/C): ");
                    String q1 = scanner.nextLine().trim().toUpperCase();
                    if (q1.equals("A")) {
                        System.out.println("✓ Correct! A legendary curse indeed!\n");
                        correctAnswers++;
                    } else {
                        System.out.println("✗ Wrong!\n");
                    }

                    // Question 2
                    System.out.println("QUESTION 2: Which restaurant is the MOST EXPENSIVE?");
                    System.out.println("A) Coqbul");
                    System.out.println("B) LockeBurger");
                    System.out.println("C) SuperMacs");
                    System.out.print("Your answer (A/B/C): ");
                    String q2 = scanner.nextLine().trim().toUpperCase();
                    if (q2.equals("B")) {
                        System.out.println("✓ Correct! LockeBurger is ridiculously expensive!\n");
                        correctAnswers++;
                    } else {
                        System.out.println("✗ Wrong! \n");
                    }

                    // Question 3
                    System.out.println("QUESTION 3: Who has the CHEAPEST burger?");
                    System.out.println("(Psst... this might be useful later!)");
                    System.out.println("A) Some Dodgey Chipper");
                    System.out.println("B) SuperMacs");
                    System.out.println("C) Chicken Hut");
                    System.out.print("Your answer (A/B/C): ");
                    String q3 = scanner.nextLine().trim().toUpperCase();
                    if (q3.equals("A")) {
                        System.out.println("✓ Correct! Remember that hint...\n");
                        correctAnswers++;
                    } else {
                        System.out.println("✗ Wrong! Some Dodgey Chipper has the best deal.\n");
                    }

                    // Check if passed (need all 3 correct)
                    if (correctAnswers == 3) {
                        // Unlock secret room
                        secretRoomUnlocked = true;
                        puzzleSolved = true;
                        System.out.println("=== YOU PASSED! ===");
                        System.out.println("The Manager smiles and snaps their fingers...");
                        System.out.println("*** The floor rumbles... ***");
                        System.out.println("*** A secret passage opens below! ***");
                        System.out.println("*** Type 'go south' to enter the secret storage! ***\n");
                    } else {
                        // Failed this attempt
                        System.out.println("=== ATTEMPT FAILED ===");
                        System.out.println("You got " + correctAnswers + " out of 3 correct.");
                        System.out.println("You lose 5 energy from stress!");
                        energyLevel = energyLevel - 5;
                        energyLevel = Math.max(0, energyLevel);
                        player.displayEnergyBar(energyLevel);

                        // Tell player how many attempts left
                        if (attempts < 3) {
                            System.out.println("You have " + (3 - attempts) + " attempt(s) left!");
                            System.out.println("(Type 'solve' again to retry)\n");
                        } else {
                            System.out.println("Game Over! You've used all 3 attempts!");
                            System.out.println("You can never access the secret room now.\n");
                        }
                    }
                }
                break;

            case "use":
                if (player.hasItem("Coupon")) {
                    if (player.getCurrentRoom().getDescription().contains("Some Dodgey Chipper")) {
                        System.out.println("You used the GOLDEN COUPON!");
                        System.out.println("Congratulations! You've won the game with the cheapest burger on campus!");
                        System.out.println("Thank you for playing. Goodbye.");
                        System.exit(0);
                    } else {
                        System.out.println("You can only use the COUPON at Some Dodgey Chipper!");
                    }
                } else {
                    System.out.println("You don't have a coupon!");
                }
                break;
           
            case "save":
                System.out.println("Enter save file name (e.g., 'mysave'):");
                Scanner saveScanner = new Scanner(System.in);
                String saveName = saveScanner.nextLine().trim();
                SaveGame.saveGame(saveName + ".sav", this);
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

    @GameCommand(word = "help", description = "Display available commands")
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander around the university.");
        System.out.println(getAnnotatedCommands());
        System.out.println(moveCount);
    }

    @GameCommand(word = "go", description = "Move in a direction (north/south/east/west)")
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String directionText = command.getSecondWord();
        Direction direction = Direction.fromString(directionText);
        
        if (direction == null) {
            System.out.println("Invalid direction!");
            return;
        }

        // secret room is locked until player solves the puzzle
        if (directionText.equalsIgnoreCase("south") &&
                player.getCurrentRoom().getDescription().contains("LockeBurger") &&
                !secretRoomUnlocked) {
            System.out.println("A mysterious lock blocks the passage south.");
            System.out.println("Perhaps you need to solve a puzzle first?");
            return;
        }

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

    // GUI METHODS
    public String getWelcomeMessage() {
        return "You are lost. You are alone. You wander around the university.";
    }

    public String getCurrentRoomDescription() {
        return player.getCurrentRoom().getLongDescription();
    }

    // handle commands from the GUI (similar to processCommand but returns strings)
    public String executeCommand(Command command) {
        StringBuilder response = new StringBuilder();
        String commandWord = command.getCommandWord();

        if (commandWord == null) {
            response.append("I don't understand your command...\n");
            return response.toString();
        }

        if (energyLevel <= 0 || hungerLevel <= 0) {
            response.append("You have run out of juice and DIED. Better luck next time!\n");
            return response.toString();
        }

        switch (commandWord) {
            case "help":
                response.append(getAnnotatedCommands());
                break;

            case "go":
                if (!command.hasSecondWord()) {
                    response.append("Go where?\n");
                } else {
                    String directionText = command.getSecondWord();
                    Direction direction = Direction.fromString(directionText);
                    
                    if (direction == null) {
                        response.append("Invalid direction!\n");
                    } else if (directionText.equalsIgnoreCase("south") && player.getCurrentRoom().getDescription().contains("LockeBurger") && !secretRoomUnlocked) {
                        response.append("A mysterious lock blocks the passage south.\n");
                    } else {
                        Room nextRoom = player.getCurrentRoom().getExit(direction);
                        if (nextRoom == null) {
                            response.append("There is no door!\n");
                        } else {
                            player.setCurrentRoom(nextRoom);
                            moveCount++;
                            energyLevel--;
                            hungerLevel -= 5;
                            response.append(player.getCurrentRoom().getLongDescription()).append("\n");
                            response.append("[Energy: ").append(energyLevel).append("/50 | Hunger: ").append(hungerLevel).append("/50]\n");
                        }
                    }
                }
                break;

            case "take":
                if (command.hasSecondWord()) {
                    if (player.takeItem(player.getCurrentRoom(), command.getSecondWord())) {
                        response.append("You picked up: ").append(command.getSecondWord()).append("\n");
                    } else {
                        response.append("That item is not here!\n");
                    }
                } else {
                    response.append("Take what?\n");
                }
                break;

            case "inventory":
                response.append(player.getInventoryString()).append("\n");
                break;

            case "menu":
                if (command.hasSecondWord()) {
                    int input = Integer.parseInt(command.getSecondWord());
                    if (input == 3) {
                        response.append("Exiting menu.\n");
                    } else {
                        try {
                            PurchaseResult result = player.getCurrentRoom().orderItem(balance, input, hungerLevel);
                            hungerLevel = result.hungerLevel;
                            balance = result.balance;
                            response.append(result.toString()).append("\n");
                        } catch (InsufficientBalanceException e) {
                            response.append("ERROR: ").append(e.getMessage()).append("\n");
                        }
                    }
                } else {
                    response.append(player.getCurrentRoom().DisplayMenu()).append("\nBalance: £").append(balance).append("\nType 'menu 1' or 'menu 2'\n");
                }
                break;

            case "talk":
                if (command.hasSecondWord()) {
                    NPC npc = player.getCurrentRoom().getNPC(command.getSecondWord());
                    if (npc != null) {
                        if (!npc.hasSpoken()) {
                            if (npc.getName().equalsIgnoreCase("Student")) {
                                clue1Talked = true;
                                response.append(npc.speak()).append("\n[Clue 1/5: Learn about the Cook]\n");
                            } else if (npc.getName().equalsIgnoreCase("Cook")) {
                                if (clue1Talked) {
                                    clue2Talked = true;
                                    response.append(npc.speak()).append("\n[Clue 2/5: Learn about the Statue]\n");
                                } else {
                                    response.append("What do you want? I'm busy. (Talk to the Student first)\n");
                                }
                            } else if (npc.getName().equalsIgnoreCase("Statue")) {
                                if (clue2Talked) {
                                    clue3Talked = true;
                                    response.append(npc.speak()).append("\n[Clue 3/5: Learn about the Manager]\n");
                                } else {
                                    response.append("*The statue stands silent* (Talk to the Cook first)\n");
                                }
                            } else if (npc.getName().equalsIgnoreCase("Manager")) {
                                if (clue3Talked) {
                                    clue4Talked = true;
                                    puzzleAvailable = true;
                                    response.append(npc.speak()).append("\n[Clue 4/5: Unlock the puzzle!]\n");
                                    response.append("*** The Manager is ready to test you! ***\n");
                                    response.append("*** Type 'solve' to attempt the puzzle! ***\n");
                                } else {
                                    response.append("I don't have time for random visitors. (Talk to the Statue first)\n");
                                }
                            } else if (npc.getName().equalsIgnoreCase("MysteriousFigure")) {
                                if (secretRoomUnlocked) {
                                    clue5Talked = true;
                                    response.append(npc.speak()).append("\n[Clue 5/5: FINAL CLUE]\n");
                                } else {
                                    response.append("Who are you? How did you get down here?\n");
                                }
                            } else {
                                response.append(npc.speak()).append("\n");
                            }
                        } else {
                            response.append(npc.speak()).append("\n(You've already talked to them.)\n");
                        }
                    } else {
                        response.append("No one here by that name.\n");
                    }
                } else {
                    response.append("Talk to who?\n");
                }
                break;

            case "solve":
                if (secretRoomUnlocked) {
                    response.append("Puzzle already solved!\n");
                } else if (!puzzleAvailable) {
                    response.append("Talk to Manager first!\n");
                } else {
                    // Start puzzle or continue answering questions
                    if (puzzleQuestion == 0) {
                        // Start the puzzle
                        puzzleQuestion = 1;
                        correctAnswers = 0;
                        response.append("=== THE MANAGER'S CHALLENGE ===\n\n");
                        response.append("QUESTION 1: What is the curse of Brown Thomas?\n");
                        response.append("A) If you touch it, you FAIL your exams!\n");
                        response.append("B) It brings bad luck to your ability to pull\n");
                        response.append("C) If you look at it for too long you get lost in its eyes\n\n");
                        response.append("Type 'solve A', 'solve B', or 'solve C'\n");
                    } else if (command.hasSecondWord()) {
                        String answer = command.getSecondWord().toUpperCase();
                        
                        if (puzzleQuestion == 1) {
                            // Question 1 answer (correct: A)
                            if (answer.equals("A")) {
                                correctAnswers++;
                                response.append("✓ Correct! A legendary curse indeed!\n\n");
                            } else {
                                response.append("✗ Wrong!\n\n");
                            }
                            puzzleQuestion = 2;
                            response.append("QUESTION 2: Which restaurant is the MOST EXPENSIVE?\n");
                            response.append("A) Coqbul\n");
                            response.append("B) LockeBurger\n");
                            response.append("C) SuperMacs\n\n");
                            response.append("Type 'solve A', 'solve B', or 'solve C'\n");
                            
                        } else if (puzzleQuestion == 2) {
                            // Question 2 answer (correct: B)
                            if (answer.equals("B")) {
                                correctAnswers++;
                                response.append("✓ Correct! LockeBurger is ridiculously expensive!\n\n");
                            } else {
                                response.append("✗ Wrong!\n\n");
                            }
                            puzzleQuestion = 3;
                            response.append("QUESTION 3: Who has the CHEAPEST burger?\n");
                            response.append("A) Some Dodgey Chipper\n");
                            response.append("B) SuperMacs\n");
                            response.append("C) Chicken Hut\n\n");
                            response.append("Type 'solve A', 'solve B', or 'solve C'\n");
                            
                        } else if (puzzleQuestion == 3) {
                            // Question 3 answer (correct: A)
                            if (answer.equals("A")) {
                                correctAnswers++;
                                response.append("✓ Correct! Remember that hint...\n\n");
                            } else {
                                response.append("✗ Wrong!\n\n");
                            }
                            
                            // Check if passed
                            if (correctAnswers == 3) {
                                secretRoomUnlocked = true;
                                response.append("=== YOU PASSED! ===\n");
                                response.append("The Manager smiles and snaps their fingers...\n");
                                response.append("*** The floor rumbles... ***\n");
                                response.append("*** A secret passage opens below! ***\n");
                                response.append("*** Type 'go south' to enter the secret storage! ***\n");
                            } else {
                                energyLevel -= 5;
                                response.append("=== ATTEMPT FAILED ===\n");
                                response.append("You got ").append(correctAnswers).append(" out of 3 correct.\n");
                                response.append("You lost 5 energy from stress!\n");
                                response.append("[Energy: ").append(energyLevel).append("/50]\n");
                            }
                            puzzleQuestion = 0; // reset for next attempt
                        }
                    } else {
                        response.append("Please provide an answer: solve A, solve B, or solve C\n");
                    }
                }
                break;

            case "use":
                if (player.hasItem("Coupon")) {
                    if (player.getCurrentRoom().getDescription().contains("Some Dodgey Chipper")) {
                        response.append("YOU WON! Used the GOLDEN COUPON!\n");
                        System.exit(0);
                    } else {
                        response.append("Use COUPON at Some Dodgey Chipper!\n");
                    }
                } else {
                    response.append("You don't have a coupon!\n");
                }
                break;

            case "look":
                response.append(player.getCurrentRoom().getLongDescription()).append("\n");
                break;

            case "save":
                if (command.hasSecondWord()) {
                    SaveGame.saveGame(command.getSecondWord() + ".sav", this);
                    response.append("Saved to ").append(command.getSecondWord()).append(".sav\n");
                } else {
                    response.append("Usage: save <filename>\n");
                }
                break;

            case "quit":
                response.append("Thanks for playing!\n");
                System.exit(0);
                break;

            default:
                response.append("I don't know what you mean...\n");
                break;
        }
        return response.toString();
    }
}
