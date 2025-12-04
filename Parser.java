import java.util.Scanner;
import java.io.Serializable;

public class Parser implements Serializable {
    private static final long serialVersionUID = 1L;
    private CommandWords commands;
    private transient Scanner reader;

    public Parser() {
        // setup command recognition and input reader
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    // scanner can't be serialized, so reinitialize it after loading a save
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        reader = new Scanner(System.in);
    }
    public Command getCommand() {
        System.out.print("> ");
        String inputLine = reader.nextLine();

        // split input into command word and second word
        String word1 = null;
        String word2 = null;

        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next();
            }
        }

        if (commands.isCommand(word1)) {
            return new Command(word1, word2);
        } else {
            return new Command(null, word2);
        }
    }

    // parse commands from GUI text input
    public Command parseCommand(String inputLine) {
        String word1 = null;
        String word2 = null;

        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next();
            }
        }
        tokenizer.close();

        if (commands.isCommand(word1)) {
            return new Command(word1, word2);
        } else {
            return new Command(null, word2);
        }
    }
}
