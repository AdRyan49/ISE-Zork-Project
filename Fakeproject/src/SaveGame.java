
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveGame implements Serializable {
    private static final long serialVersionUID = 1L;
    public static void saveGame(String filename, ZorkULGame game) {
    try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            
            // Write entire game object to file
            out.writeObject(game);
            
            out.close();
            fileOut.close();
            
            System.out.println("✓ Game saved to " + filename);
            
        } catch (IOException e) {
            System.out.println("✗ Error saving game: " + e.getMessage());
        }
    }
    
    // Load game using serialization
    public static ZorkULGame loadGame(String filename) {
        ZorkULGame game = null;
        
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            
            // Read entire game object from file
            game = (ZorkULGame) in.readObject();
            
            in.close();
            fileIn.close();
            
            System.out.println("✓ Game loaded from " + filename);
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("✗ Error loading game: " + e.getMessage());
        }
        
        return game;
    }
}
    

