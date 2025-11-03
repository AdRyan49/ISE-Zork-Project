import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SimpleJavaFXWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create a button
        Button button = new Button("Click Me");
        
        // Set up the layout
        StackPane root = new StackPane();
        root.getChildren().add(button);
        
        // Create a scene with the layout
        Scene scene = new Scene(root, 300, 200);
        
        // Set the stage (window) title and scene
        primaryStage.setTitle("JavaFX Simple Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
