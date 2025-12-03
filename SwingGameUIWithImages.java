import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class SwingGameUIWithImages extends JFrame {
    private JTextArea outputArea;
    private JTextField inputField;
    private JLabel imageLabel;
    private ZorkULGame game;
    private Parser parser;

    public SwingGameUIWithImages(ZorkULGame existingGame) {
        setTitle("Restaurant Run - Text Adventure");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(45, 45, 48));

        // LEFT: Image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(45, 45, 48));
        imagePanel.setPreferredSize(new Dimension(500, 400));
        
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(500, 400));
        imageLabel.setBackground(new Color(45, 45, 48));
        imageLabel.setForeground(Color.WHITE);
        imageLabel.setOpaque(true);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(imagePanel, BorderLayout.WEST);

        // RIGHT: Text output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(new Color(45, 45, 48));
        outputArea.setForeground(Color.WHITE);
        outputArea.setCaretColor(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 75)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // BOTTOM: Input and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(45, 45, 48));
        
        // Input field and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(45, 45, 48));
        inputField = new JTextField();
        inputField.setBackground(new Color(30, 30, 30));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton sendButton = new JButton("SEND");
        sendButton.setBackground(new Color(60, 60, 65));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        JLabel promptLabel = new JLabel("> ");
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        // Direction buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonPanel.setBackground(new Color(45, 45, 48));
        JButton northBtn = new JButton("NORTH");
        JButton southBtn = new JButton("SOUTH");
        JButton eastBtn = new JButton("EAST");
        JButton westBtn = new JButton("WEST");
        
        for (JButton btn : new JButton[]{northBtn, southBtn, eastBtn, westBtn}) {
            btn.setBackground(new Color(60, 60, 65));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Monospaced", Font.BOLD, 12));
        }
        
        buttonPanel.add(northBtn);
        buttonPanel.add(southBtn);
        buttonPanel.add(eastBtn);
        buttonPanel.add(westBtn);
        
        bottomPanel.add(inputPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event listeners
        sendButton.addActionListener(e -> processCommand());
        inputField.addActionListener(e -> processCommand());
        northBtn.addActionListener(e -> executeDirection("north"));
        southBtn.addActionListener(e -> executeDirection("south"));
        eastBtn.addActionListener(e -> executeDirection("east"));
        westBtn.addActionListener(e -> executeDirection("west"));

        // Initialize game
        game = existingGame;
        parser = new Parser();
        outputArea.append("Welcome to Restaurant Run!\n");
        outputArea.append(game.getWelcomeMessage() + "\n\n");
        outputArea.append(game.getCurrentRoomDescription() + "\n");
        updateRoomImage();

        setVisible(true);
    }

    private void executeDirection(String direction) {
        inputField.setText("go " + direction);
        processCommand();
    }

    private void processCommand() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) return;

        outputArea.append("> " + input + "\n");
        Command command = parser.parseCommand(input);
        String response = game.executeCommand(command);
        outputArea.append(response + "\n");
        updateRoomImage();
        inputField.setText("");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void updateRoomImage() {
        String desc = game.getCurrentRoomDescription().toLowerCase();
        String imageName = "";

        if (desc.contains("outide") || desc.contains("outside")) imageName = "images/Outside.png";
        else if (desc.contains("coqbul")) imageName = "images/coqbul.png";
        else if (desc.contains("supermacs")) imageName = "images/Supermacs.png";
        else if (desc.contains("lockeburger")) imageName = "images/locke-burger.png";
        else if (desc.contains("burger mac")) imageName = "images/burger-mac.png";
        else if (desc.contains("dodgey chipper")) imageName = "images/some-dodgey-chipper.png";
        else if (desc.contains("student union")) imageName = "images/student-union.png";
        else if (desc.contains("chicken hut")) imageName = "images/chicken-hut.png";
        else if (desc.contains("storage") || desc.contains("secret")) imageName = "images/Secretroom.png";
        else if (desc.contains("brown thomas")) imageName = "images/brownthomas.png";

        if (!imageName.isEmpty()) {
            try {
                File imgFile = new File(imageName);
                if (imgFile.exists()) {
                    Image img = ImageIO.read(imgFile);
                    Image scaled = img.getScaledInstance(500, 400, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaled));
                    imageLabel.setText("");
                } else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("No image");
                }
            } catch (Exception e) {
                imageLabel.setIcon(null);
                imageLabel.setText("Error loading image");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingGameUIWithImages(new ZorkULGame()));
    }
}
