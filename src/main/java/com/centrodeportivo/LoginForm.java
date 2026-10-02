package com.centrodeportivo;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginForm() {
        setTitle("Login - Centro Deportivo");
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        JPanel panel = new JPanel(new GridLayout(3,2,10,10));

        panel.add(new JLabel("Usuario:"));
        txtUser = new JTextField();
        panel.add(txtUser);

        panel.add(new JLabel("Contraseña:"));
        txtPass = new JPasswordField();
        panel.add(txtPass);

        btnLogin = new JButton("Ingresar");
        panel.add(new JLabel()); // espacio
        panel.add(btnLogin);

        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        try (Connection con = ConnectionDB.getConnection()) {
            String sql = "SELECT u.nombre, r.nombre AS rol FROM usuarios u JOIN roles r ON u.id_rol=r.id WHERE u.username=? AND u.password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String rol = rs.getString("rol");
                // Cerrar login y abrir MainWindow
                dispose();
                new MainWindow(nombre, rol).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // 🔹 Punto de entrada principal
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}