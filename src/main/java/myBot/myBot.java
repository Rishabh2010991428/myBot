package myBot;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class myBot extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    public JLabel label;
    private Connection connection;

    public myBot() {
        initializeUI();
        setupDatabase();
    }

    private void initializeUI() {
        setTitle("Chatbot Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400); 
        setLayout(new BorderLayout());

        try {
            File imageFile = new File("src/main/java/myBot/logo.png");
            Image originalImage = ImageIO.read(imageFile);

            int newWidth = 50; 
            int newHeight = 50; 

            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            ImageIcon imageIcon = new ImageIcon(scaledImage);
            label = new JLabel(imageIcon);

            add(label, BorderLayout.NORTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        

        // Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Poppins", Font.PLAIN, 14)); 
        chatArea.setLineWrap(true);
        chatArea.setBackground(new Color(63, 62, 64));
        chatArea.setForeground(Color.WHITE); 
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        messageField = new JTextField();
        messageField.setFont(new Font("Poppins", Font.PLAIN, 14));  
        messageField.setBackground(Color.WHITE); 
        messageField.setForeground(Color.BLACK);  
        inputPanel.add(messageField, BorderLayout.CENTER);
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Poppins", Font.PLAIN, 14));  
        sendButton.setBackground(new Color(122, 50, 195));  
        sendButton.setForeground(Color.WHITE);  
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void setupDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myBotQueries", "root", "Root1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            saveMessage("User", message);
            chatArea.append("You: " + message + "\n");
            messageField.setText("");
            String botResponse = getChatbotResponse(message);
            saveMessage("MyBot", botResponse);
            chatArea.append("MyBot: " + botResponse + "\n");  
        }
    }

    private void saveMessage(String userName, String message) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat_messages (user_name, message) VALUES (?, ?)");
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, message);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getChatbotResponse(String userMessage) {
        // check for a response in the database
        String responseFromDatabase = queryDatabase(userMessage);

        // response is found in the database
        if (responseFromDatabase != null && !responseFromDatabase.isEmpty()) {
            return responseFromDatabase;
        } else {
            // no response in the database, query from open API
            String apiResponse = queryExternalAPI(userMessage);

            // Return the api response
            return apiResponse;
        }
    }

    // finding query from db
    private String queryDatabase(String userMessage) {
        // return the response from the database if a match is found
        // Otherwise, return null or an empty string
        return null;
    }

    // Implementing a method to query the external API
    private String queryExternalAPI(String userMessage) {
    	String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            String body="{\"model\": \"" + model + "\", \"messages\":[{\"role\": \"user\", \"content\": \"" + userMessage + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            int responseCode = con.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return (response.toString().split("\"content\":\"")[1].split("\"")[0]).substring(4);
            } else {
                System.err.println("Error: HTTP request failed with response code " + responseCode);
                return null;  
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new myBot().setVisible(true);
            }
        });
    }
}
