import java.util.ArrayList;

public class Character {
    private String name;
    private Room currentRoom;
    private ArrayList<Item> inventory;  // NEW: Player's inventory
    

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();  // NEW: Initialize empty inventory
        
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            System.out.println("You moved to: " + currentRoom.getDescription());
        } else {
            System.out.println("You can't go that way!");
        }
    }

    // NEW: Add item to inventory
    /**
     * Adds an item to the character's inventory
     * param item The item to add
     */
    public void addItem(Item item) {
        inventory.add(item);
    }

    // NEW: Remove item from inventory
    /**
     * Removes an item from inventory by name
     * param itemName The name of the item to remove
     * return The removed item, or null if not found
     */
    public Item removeItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                inventory.remove(item);
                return item;
            }
        }
        return null;
    }

    // NEW: Check if inventory has an item
    /**
     * Checks if the character has an item
     * param itemName The name of the item to check for
     * return true if item is in inventory, false otherwise
     */
    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    // NEW: Get inventory as string
    /**
     * Returns a formatted string of all items in inventory
     * return String listing all items with details
     */
    public String getInventoryString() {
        if (inventory.isEmpty()) {
            return "Your inventory is empty.";
        }
        StringBuilder sb = new StringBuilder("You are carrying:\n");
        for (Item item : inventory) {
            sb.append("  - ").append(item.getName()).append(" : ").append(item.getDescription()).append("\n");
        }
        return sb.toString();
    }

    // NEW: Take an item from a room and add to inventory
    /**
     * Takes an item from a specific room and adds it to inventory
     * param room The room to take the item from
     * param itemName The name of the item to take
     * return true if item was taken successfully, false otherwise
     */
    public boolean takeItem(Room room, String itemName) {
        Item item = room.removeItem(itemName);
        if (item != null) {
            this.addItem(item);
            return true;
        }
        return false;
    }

    // NEW: Drop an item from inventory to a room
    /**
     * Drops an item from inventory into a specific room
     *  room The room to drop the item into
     *  itemName The name of the item to drop
     * return true if item was dropped successfully, false otherwise
     */
    public boolean dropItem(Room room, String itemName) {
        Item item = this.removeItem(itemName);
        if (item != null) {
            room.addItem(item);
            return true;
        }
        return false;
    }

                
   
   public void displayEnergyBar(int energyLevel){
             System.out.println("Energy level");
                String bar = "[" +"*".repeat(energyLevel)+ "]";
            if(energyLevel != 50){
                    int goneEnergy = (50 - energyLevel);
                    bar = "=".repeat(energyLevel)+ " ".repeat(goneEnergy); 
                }
                System.out.println("["+bar+"]");
    }
    public void displayHungerBar(int hungerLevel){
            hungerLevel = Math.max(0, hungerLevel);
              
             System.out.println("Hunger level");
                String bar = "[" +"*".repeat(hungerLevel)+ "]";
            if(hungerLevel != 50){
                    int goneHunger = (50 - hungerLevel);
                    bar = "=".repeat(hungerLevel)+ " ".repeat(goneHunger); 
                }
                System.out.println("["+bar+"]");
    }
}
