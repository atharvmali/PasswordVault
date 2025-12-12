package ui;

import db.VaultDAO;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    JPasswordField passField = new JPasswordField(15);
    VaultDAO dao = new VaultDAO();

    public LoginScreen() {
        setTitle("Password Vault - Login");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Master Password:"));
        panel.add(passField);

        JButton loginBtn = new JButton("Login");
        JButton setBtn = new JButton("Set Password");
        panel.add(loginBtn);
        panel.add(setBtn);

        add(panel);

        loginBtn.addActionListener(e -> login());
        setBtn.addActionListener(e -> setPassword());

        setVisible(true);
    }

    private void login() {
        String pwd = new String(passField.getPassword());

        if (dao.validateMasterPassword(pwd)) {
            dispose();
            new VaultScreen();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect Password!");
        }
    }

    private void setPassword() {
        if (dao.masterPasswordExists()) {
            JOptionPane.showMessageDialog(this, "Master password already exists!");
            return;
        }

        String pwd = new String(passField.getPassword());
        if (pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a password!");
            return;
        }

        dao.setMasterPassword(pwd);
        JOptionPane.showMessageDialog(this, "Master Password Set!");
    }
}
