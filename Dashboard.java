package online_exam;
import javax.swing.*;

public class Dashboard {
    public Dashboard(int userId) {
        JFrame f = new JFrame("Dashboard");

        JButton start = new JButton("Start Exam");
        JButton logout = new JButton("Logout");

        start.setBounds(80,40,140,30);
        logout.setBounds(80,90,140,30);

        start.addActionListener(e -> {
            f.dispose();
            new ExamWindow(userId);
        });

        logout.addActionListener(e -> {
            f.dispose();
            new Login();
        });

        f.setLayout(null);
        f.add(start);
        f.add(logout);

        f.setSize(300,200);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
