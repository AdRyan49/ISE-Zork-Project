import java.io.Serializable;

public class NPC implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String dialogue;
    private boolean hasSpoken;

    public NPC(String name, String dialogue) {
        this.name = name;
        this.dialogue = dialogue;
        this.hasSpoken = false;
    }

    public String getName() {
        return name;
    }

    public String speak() {
        hasSpoken = true;
        return name + " says: \"" + dialogue + "\"";
    }

    public boolean hasSpoken() {
        return hasSpoken;
    }

}
