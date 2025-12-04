package Fakeproject.src;

import java.io.Serializable;

public class NPC implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String dialogue;
    private boolean hasSpoken;

    public NPC(String name, String dialogue) {
        // npcs start untalked-to
        this.name = name;
        this.dialogue = dialogue;
        this.hasSpoken = false;
    }

    public String getName() {
        return name;
    }

    public String speak() {
        hasSpoken = true; // mark as talked to
        return name + " says: \"" + dialogue + "\"";
    }

    public boolean hasSpoken() {
        return hasSpoken;
    }

}
