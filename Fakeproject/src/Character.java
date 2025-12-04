
import java.util.ArrayList;
import java.io.Serializable;

public class Character implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Room currentRoom;
    private ArrayList<Item> inventory;

    public Character(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();

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
        Direction dir = Direction.fromString(direction);
        if (dir == null) {
            System.out.println("Invalid direction!");
            return;
        }
        Room nextRoom = currentRoom.getExit(dir);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            System.out.println("You moved to: " + currentRoom.getDescription());
        } else {
            System.out.println("You can't go that way!");
        }
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public Item removeItem(String itemName) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getName().equalsIgnoreCase(itemName)) {
                return inventory.remove(i);
            }
        }
        return null;
    }

    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

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

    public boolean takeItem(Room room, String itemName) {
        try {
            Item item = room.removeItem(itemName);
            this.addItem(item);
            return true;
        } catch (ItemNotFoundException e) {
            return false;
        }
    }

    public void displayEnergyBar(int energyLevel) {
        System.out.println("Energy level");
        String bar = "[" + "*".repeat(energyLevel) + "]";
        if (energyLevel != 50) {
            int goneEnergy = (50 - energyLevel);
            bar = "=".repeat(energyLevel) + " ".repeat(goneEnergy);
        }
        System.out.println("[" + bar + "]");
    }

    public void displayHungerBar(int hungerLevel) {
        System.out.println("Hunger level");
        hungerLevel = Math.max(0, hungerLevel);

        String bar = "[" + "*".repeat(hungerLevel) + "]";
        if (hungerLevel != 50) {
            int goneHunger = (50 - hungerLevel);
            bar = "=".repeat(hungerLevel) + " ".repeat(goneHunger);
        }
        System.out.println("[" + bar + "]");
    }
}
