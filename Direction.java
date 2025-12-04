public enum Direction {
    // all possible movement directions in the game
    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west");

    private final String text;

    Direction(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    // convert user input like "north" into Direction.NORTH
    public static Direction fromString(String text) {
        if (text == null) return null;
        for (Direction dir : Direction.values()) {
            if (dir.text.equalsIgnoreCase(text)) {
                return dir;
            }
        }
        return null;
    }
}
