package online_exam;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import javax.swing.Timer;

public class ExamWindow {

    JFrame f = new JFrame("Online Exam");
    JLabel qLabel = new JLabel();
    JLabel timerLabel = new JLabel("Time: 120");

    JRadioButton a = new JRadioButton();
    JRadioButton b = new JRadioButton();
    JRadioButton c = new JRadioButton();
    JRadioButton d = new JRadioButton();

    ButtonGroup bg = new ButtonGroup();
    JButton next = new JButton("Next");
    JButton submit = new JButton("Submit");

    List<String[]> questions = new ArrayList<>();
    int index = 0, userId;
    int total = 0, attempted = 0, correct = 0;
    int timeLeft = 120;
    Timer timer;

    public ExamWindow(int userId) {
        this.userId = userId;
        loadQuestions();
        setupUI();
        startTimer();
        showQuestion();
    }

    void setupUI() {
        f.setLayout(null);

        qLabel.setBounds(30,20,600,30);
        timerLabel.setBounds(520,20,100,30);

        a.setBounds(50,70,200,30);
        b.setBounds(50,110,200,30);
        c.setBounds(50,150,200,30);
        d.setBounds(50,190,200,30);

        next.setBounds(150,240,100,30);
        submit.setBounds(270,240,100,30);

        bg.add(a); bg.add(b); bg.add(c); bg.add(d);

        f.add(qLabel); f.add(timerLabel);
        f.add(a); f.add(b); f.add(c); f.add(d);
        f.add(next); f.add(submit);

        next.addActionListener(e -> nextQuestion());
        submit.addActionListener(e -> submitExam());

        f.setSize(650,350);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    void loadQuestions() {
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT * FROM questions"
            );
            while(rs.next()) {
                questions.add(new String[]{
                        rs.getString("question"),
                        rs.getString("optA"),
                        rs.getString("optB"),
                        rs.getString("optC"),
                        rs.getString("optD"),
                        rs.getString("correct")
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void showQuestion() {
        bg.clearSelection();
        String[] q = questions.get(index);
        qLabel.setText((index+1)+". "+q[0]);
        a.setText("A. "+q[1]);
        b.setText("B. "+q[2]);
        c.setText("C. "+q[3]);
        d.setText("D. "+q[4]);
    }

    void nextQuestion() {
        checkAnswer();
        index++;
        if(index < questions.size())
            showQuestion();
        else
            submitExam();
    }

    void checkAnswer() {
        total++;
        String selected = null;

        if(a.isSelected()) selected = "A";
        if(b.isSelected()) selected = "B";
        if(c.isSelected()) selected = "C";
        if(d.isSelected()) selected = "D";

        if(selected == null) return;

        attempted++;
        if(selected.equals(questions.get(index)[5]))
            correct++;
    }

    void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: "+timeLeft);
            if(timeLeft == 0) submitExam();
        });
        timer.start();
    }

    void submitExam() {
        timer.stop();
        if(index < questions.size()) checkAnswer();

        int wrong = attempted - correct;

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO results(userId,total,attempted,correct,wrong,score,date) " +
                            "VALUES (?,?,?,?,?,?,NOW())"
            );
            ps.setInt(1, userId);
            ps.setInt(2, total);
            ps.setInt(3, attempted);
            ps.setInt(4, correct);
            ps.setInt(5, wrong);
            ps.setInt(6, correct);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }

        f.dispose();
        new Result(userId);
    }
}
