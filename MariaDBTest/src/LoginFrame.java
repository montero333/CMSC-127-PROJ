import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    private Connection connection;


    public LoginFrame(Connection connection) {
        this.connection = connection;

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(491, 351);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null); // Set null layout

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(68, 128, 80, 25); // Manually set bounds
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(155, 128, 256, 25); // Manually set bounds
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(68, 176, 80, 25); // Manually set bounds
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(155, 176, 256, 25); // Manually set bounds
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(180, 229, 109, 38); // Manually set bounds
        loginButton.addActionListener(e -> login());
        panel.add(loginButton);

        JLabel signUpLabel = new JLabel("New user? Add account here.");
        signUpLabel.setForeground(Color.BLUE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new SignUpFrame(connection);
            }
        });
        signUpLabel.setBounds(155, 279, 180, 25); // Manually set bounds
        panel.add(signUpLabel);

        getContentPane().add(panel);

        setVisible(true);
    }
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String loggedInUsername = resultSet.getString("username");
                JOptionPane.showMessageDialog(this, "User " + loggedInUsername + " has logged in successfully.", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while logging in", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
