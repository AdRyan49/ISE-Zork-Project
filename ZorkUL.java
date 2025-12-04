import java.util.Scanner;
import javax.swing.SwingUtilities;

public class ZorkUL {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ZorkULGame game;
  

        // show welcome and let player choose new/load
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   Welcome to Restaurant Run!       ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        System.out.println("1) Start new game");
        System.out.println("2) Load saved game");
        System.out.print("Choose (1 or 2): ");
        
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("2")) {
            // try to load saved game
            System.out.println("\nEnter save file name to load (e.g., 'mysave'):");
            String saveName = scanner.nextLine().trim();
            
            game = SaveGame.loadGame(saveName + ".sav");
            
            if (game == null) {
                System.out.println("✗ Save file not found! Starting new game instead.\n");
                game = new ZorkULGame();
            } else {
                System.out.println("✓ Save loaded! Continuing your adventure...\n");
            }
        } else {
            game = new ZorkULGame();
        }
        
        // let player choose text or GUI mode
        System.out.println("\nChoose game mode:");
        System.out.println("1) Text-based (console)");
        System.out.println("2) Graphical (Swing GUI)");
        System.out.print("Choose (1 or 2): ");
        String uiChoice = scanner.nextLine().trim();

        if (uiChoice.equals("2")) {
            // Launch Swing GUI with loaded or new game
            scanner.close();
            final ZorkULGame finalGame = game;
            SwingUtilities.invokeLater(() -> new SwingGameUIWithImages(finalGame));
        } else {
            // Launch text-based game
            game.play();
        }
    }
}
