package online_exam;
import javax.swing.*;
import java.sql.*;

public class Result {
    public Result(int userId) {
        JFrame f = new JFrame("Result");
        JTextArea ta = new JTextArea();
        ta.setEditable(false);

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM results WHERE userId=? ORDER BY id DESC LIMIT 1"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                ta.setText(
                        "Total: "+rs.getInt("total")+
                                "\nAttempted: "+rs.getInt("attempted")+
                                "\nCorrect: "+rs.getInt("correct")+
                                "\nWrong: "+rs.getInt("wrong")+
                                "\nScore: "+rs.getInt("score")
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        f.add(ta);
        f.setSize(300,250);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
