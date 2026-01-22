package online_exam;
import javax.swing.*;
import java.sql.*;

public class Login {
    public Login() {
        JFrame f = new JFrame("Login");

        JTextField email = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton login = new JButton("Login");

        f.setLayout(null);

        f.add(new JLabel("Email")).setBounds(40,30,80,30);
        f.add(email).setBounds(130,30,150,30);

        f.add(new JLabel("Password")).setBounds(40,80,80,30);
        f.add(pass).setBounds(130,80,150,30);

        f.add(login).setBounds(130,130,100,30);

        login.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT id FROM users WHERE email=? AND password=?"
                );
                ps.setString(1, email.getText());
                ps.setString(2, String.valueOf(pass.getPassword()));

                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    f.dispose();
                    new Dashboard(rs.getInt(1));
                } else {
                    JOptionPane.showMessageDialog(f,"Invalid Login");
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        f.setSize(330,230);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
