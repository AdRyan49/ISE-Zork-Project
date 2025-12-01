import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class CommandWords implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, String> validCommands;

    public CommandWords() {
        validCommands = new HashMap<>();
        validCommands.put("go", "Move to another room");
        validCommands.put("quit", "End the game");
        validCommands.put("help", "Show help");
        validCommands.put("look", "Look around");
        validCommands.put("eat", "Eat something");
        validCommands.put("take", "Pick up an item");  
        validCommands.put("drop", "Drop an item");    
        validCommands.put("inventory", "Check your inventory"); 
        validCommands.put("menu", "Display the food menu");
        validCommands.put("buy", "Order food from the menu");
        validCommands.put("talk", "Talk to an NPC");
        validCommands.put("solve", "Solve a puzzle");
        validCommands.put("use", "Use an item ");
        validCommands.put("save", "Save your game progress");
        validCommands.put("load", "Load a previously saved game");

    }

    public boolean isCommand(String commandWord) {
        return validCommands.containsKey(commandWord);
    }

    public void showAll() {
        System.out.print("Valid commands are: ");
        for (String command : validCommands.keySet()) {
            System.out.print(command + " ");
        }
        System.out.println();
    }
}
