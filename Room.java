import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private Map<String, Room> exits; // Map direction to neighboring Room
    private ArrayList<Item> items; // items
    private Map<Integer, String> foodMenuNames; // map of names
    private Map<Integer, Double> foodMenuPrices;// map of prices
    private ArrayList<NPC> npcs; //NPCs in the room

    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
        items = new ArrayList<>();
        foodMenuNames = new HashMap<>();
        foodMenuPrices = new HashMap<>();
        npcs = new ArrayList<>();
    }

    public void addMenuOption(int number, String itemName, double price) {
        foodMenuNames.put(number, itemName);
        foodMenuPrices.put(number, price);
    }
    //learn
    public void DisplayMenu() {
        System.out.println("Available options:");
        for (Integer num : foodMenuNames.keySet()) {
            System.out.println(num + ". " + foodMenuNames.get(num) + " - £" + foodMenuPrices.get(num));
        }
        System.out.println("3. Back (Exit Menu)"); 
    }

    public PurchaseResult orderItem(double balance, int choiceNumber, int hungerLevel) {
        if (foodMenuNames.containsKey(choiceNumber)) {
            String itemName = foodMenuNames.get(choiceNumber);
            Double price = foodMenuPrices.get(choiceNumber);
            System.out.println("You ordered: " + itemName + " for £" + price);
            if (balance < price) {
                System.out.println("Insufficient balance to complete the purchase.");
                return new PurchaseResult(hungerLevel, balance); // No change
            }
            balance = balance - price;

            int refill = (int) (price * 3);
            hungerLevel = hungerLevel + refill;
            hungerLevel = Math.min(hungerLevel, 50);
            System.out.println("Your new balance is: £" + balance);
            System.out.println("Your hunger is now: " + hungerLevel);

            return new PurchaseResult(hungerLevel, balance);
        } else {
            System.out.println("Sorry, that item is not on the menu.");
            return new PurchaseResult(hungerLevel, balance);
        }
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
        return null; // Item not found
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
    
     public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public NPC getNPC(String npcName) {
        for (NPC npc : npcs) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                return npc;
            }
        }
        return null; // NPC not found
    }

    public String getNPCsString() {
        if (npcs.isEmpty()) {
            return "nobody";
        }
        String result = "";
        for (NPC npc : npcs) {
            result = result + npc.getName() + ", ";
        }
        return result;
    }
    
    
    
    
    
    
    
    public String getLongDescription() {
        return "You are " + description + ".\nExits: " + getExitString()
                + "\nItems: " + getItemsString() // Add items to description
                + "\nNPCs: " + getNPCsString();
    }
}
