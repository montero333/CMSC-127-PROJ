import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpFrame extends JFrame {
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField ageField;
    private JTextField emailField;
    private JTextField birthdayField;

    private Connection connection;

    public SignUpFrame(Connection connection) {
        this.connection = connection;

        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(699, 522);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null); // Set layout manager to null for manual positioning

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(146, 48, 100, 25);
        panel.add(firstNameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(299, 48, 224, 25);
        panel.add(firstNameField);

        JLabel middleNameLabel = new JLabel("Middle Name:");
        middleNameLabel.setBounds(146, 84, 100, 25);
        panel.add(middleNameLabel);

        middleNameField = new JTextField();
        middleNameField.setBounds(299, 84, 224, 25);
        panel.add(middleNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(146, 120, 100, 25);
        panel.add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(299, 120, 224, 25);
        panel.add(lastNameField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(146, 156, 100, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(299, 156, 224, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(146, 192, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(299, 192, 224, 25);
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(146, 228, 120, 25);
        panel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(299, 228, 224, 25);
        panel.add(confirmPasswordField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(146, 264, 100, 25);
        panel.add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(299, 264, 224, 25);
        panel.add(ageField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(146, 300, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(299, 300, 224, 25);
        panel.add(emailField);

        JLabel birthdayLabel = new JLabel("Birthday (YYYY-MM-DD):");
        birthdayLabel.setBounds(146, 331, 150, 25);
        panel.add(birthdayLabel);

        birthdayField = new JTextField();
        birthdayField.setBounds(299, 336, 224, 25);
        panel.add(birthdayField);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(264, 382, 144, 41);
        signUpButton.addActionListener(e -> signUp());
        panel.add(signUpButton);
        
        JButton backButton = new JButton("Back");
        backButton.setBounds(264, 431, 144, 41);
        backButton.addActionListener(e -> goBack());
        panel.add(backButton);

        getContentPane().add(panel);

        setVisible(true);
    }

    private void goBack() {
    	dispose(); // Close the sign-up frame
        new LoginFrame(connection); // Open the login frame
	}

	private void signUp() {
        // Retrieve values from fields
        String firstName = firstNameField.getText();
        String middleName = middleNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String ageText = ageField.getText();
        String email = emailField.getText();
        String birthday = birthdayField.getText();

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert age to integer
        int age = 0;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the username already exists
        try {
            String checkQuery = "SELECT COUNT(*) FROM Users WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another one.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to check username availability", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Username is available, proceed with sign-up
        try {
            // Prepare the SQL statement
            String sql = "INSERT INTO Users (firstName, middleName, lastName, username, password, age, email, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, middleName);
            statement.setString(3, lastName);
            statement.setString(4, username);
            statement.setString(5, password);
            statement.setInt(6, age);
            statement.setString(7, email);
            statement.setString(8, birthday);

            // Execute the insert query
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Sign up successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the sign-up frame
                new LoginFrame(connection); // Open the login frame
            } else {
                JOptionPane.showMessageDialog(this, "Sign up failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Sign up failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
