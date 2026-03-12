import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class CompilerUI extends JFrame {

    JTextField nameField;
    JTextArea codeArea, outputArea;
    JButton saveBtn, compileBtn, runBtn;

    public CompilerUI() {

        setTitle("Offline Java Compiler");
        setSize(700,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Program name
        JPanel top = new JPanel(new BorderLayout());
        nameField = new JTextField();
        top.add(new JLabel("Program Name: "), BorderLayout.WEST);
        top.add(nameField, BorderLayout.CENTER);
        add(top,BorderLayout.NORTH);

        // Code editor
        codeArea = new JTextArea();
        JScrollPane codeScroll = new JScrollPane(codeArea);
        codeScroll.setBorder(BorderFactory.createTitledBorder("Code Editor"));
        add(codeScroll,BorderLayout.CENTER);

        // Output console
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Output"));
        outputScroll.setPreferredSize(new Dimension(700,150));

        add(outputScroll,BorderLayout.SOUTH);

        // Buttons
        JPanel bottom = new JPanel();

        saveBtn = new JButton("Save");
        compileBtn = new JButton("Compile");
        runBtn = new JButton("Run");

        bottom.add(saveBtn);
        bottom.add(compileBtn);
        bottom.add(runBtn);

        add(bottom,BorderLayout.EAST);

        // Button actions
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveCode();
            }
        });

        compileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compileCode();
            }
        });

        runBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runCode();
            }
        });
    }

    // Save code to PostgreSQL
    void saveCode() {

        String name = nameField.getText();
        String code = codeArea.getText();

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement("INSERT INTO programs(name,code) VALUES (?,?)");

            ps.setString(1, name);
            ps.setString(2, code);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Code Saved!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Compile Java code
    void compileCode() {

        try {

            String programName = nameField.getText();

            FileWriter fw = new FileWriter(programName + ".java");
            fw.write(codeArea.getText());
            fw.close();

            Process p = Runtime.getRuntime().exec("javac " + programName + ".java");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));

            String line;
            outputArea.setText("");

            while((line = br.readLine()) != null) {
                outputArea.append(line + "\n");
            }

            if(outputArea.getText().isEmpty())
                outputArea.setText("Compilation Successful!");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Run compiled program
    void runCode() {

        try {

            String programName = nameField.getText();

            Process p = Runtime.getRuntime().exec("java " + programName);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line;
            outputArea.setText("");

            while((line = br.readLine()) != null) {
                outputArea.append(line + "\n");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        CompilerUI ui = new CompilerUI();
        ui.setVisible(true);

    }
}