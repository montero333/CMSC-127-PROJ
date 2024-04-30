import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            final Connection finalConnection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/CustomerDB", "root", "iamnicoantonio1124");
            SwingUtilities.invokeLater(() -> new LoginFrame(finalConnection));
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
