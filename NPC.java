public class NPC {
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
