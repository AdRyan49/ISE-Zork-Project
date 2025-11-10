import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Room {
    private String description;
    private Map<String, Room> exits; // Map direction to neighboring Room
    private ArrayList<Item> items; //items
    private Map<String, Double> foodMenu; //menu

    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
        items = new ArrayList<>();
        foodMenu = new HashMap<>();
    }
    public void addMenuOption(String itemName, double price) {
        foodMenu.put(itemName, price);
    }
    public void DisplayMenu() {
        System.out.println("Available options:");
            for (Map.Entry<String, Double> entry : foodMenu.entrySet()) {
            String name = entry.getKey();    // burger name
            Double price = entry.getValue(); // burger price
            System.out.println(name + ": $" + price);
        }
        System.out.println();
    }
    

    public String getDescription() {
        return description;
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (String direction : exits.keySet()) {
            sb.append(direction).append(" ");
        }
        return sb.toString().trim();
    }
    
    /**
     * Adds an item to this room
     * param item The item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }
    /**
     * Removes an item from this room by name
     * param itemName The name of the item to remove
     * return The removed item, or null if not found
     */
    public Item removeItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(item);
                return item;
            }
        }
        return null;  // Item not found
    }
     /**
     * Returns a formatted string of all items in this room
     * return String listing all items, or "none" if empty
     */
    public String getItemsString() {
        if (items.isEmpty()) {
            return "none";
        }
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append(item.getName()).append(", ");
        }
        // Remove trailing comma and space
        return sb.substring(0, sb.length() - 2);
    }


    public String getLongDescription() {
        return "You are " + description + ".\nExits: " + getExitString() 
               + "\nItems: " + getItemsString();  //Add items to description
    }
}
