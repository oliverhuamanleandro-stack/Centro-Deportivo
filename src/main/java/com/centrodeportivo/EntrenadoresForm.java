package com.centrodeportivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class EntrenadoresForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNombre, txtEspecialidad, txtTelefono, txtEmail;

    public EntrenadoresForm() {
        setTitle("Gestión de Entrenadores");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel l1 = new JLabel("Nombre:");
        l1.setBounds(20, 20, 100, 25);
        add(l1);
        txtNombre = new JTextField();
        txtNombre.setBounds(120, 20, 150, 25);
        add(txtNombre);

        JLabel l2 = new JLabel("Especialidad:");
        l2.setBounds(20, 60, 100, 25);
        add(l2);
        txtEspecialidad = new JTextField();
        txtEspecialidad.setBounds(120, 60, 150, 25);
        add(txtEspecialidad);

        JLabel l3 = new JLabel("Teléfono:");
        l3.setBounds(20, 100, 100, 25);
        add(l3);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(120, 100, 150, 25);
        add(txtTelefono);

        JLabel l4 = new JLabel("Email:");
        l4.setBounds(20, 140, 100, 25);
        add(l4);
        txtEmail = new JTextField();
        txtEmail.setBounds(120, 140, 150, 25);
        add(txtEmail);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(400, 20, 100, 25);
        add(btnAgregar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(400, 60, 100, 25);
        add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(400, 100, 100, 25);
        add(btnEliminar);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(400, 140, 100, 25);
        add(btnActualizar);

        model = new DefaultTableModel(new String[]{"ID","Nombre","Especialidad","Teléfono","Email"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 200, 640, 120);
        add(scroll);

        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {
        model.setRowCount(0);
        try (Connection conn = ConnectionDB.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM entrenadores");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especialidad"),
                        rs.getString("telefono"),
                        rs.getString("email")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void agregar() {
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO entrenadores(nombre,especialidad,telefono,email) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtEspecialidad.getText());
            ps.setString(3, txtTelefono.getText());
            ps.setString(4, txtEmail.getText());
            ps.executeUpdate();
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void editar() {
        int fila = table.getSelectedRow();
        if (fila == -1) return;
        int id = (int) model.getValueAt(fila, 0);
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "UPDATE entrenadores SET nombre=?,especialidad=?,telefono=?,email=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtEspecialidad.getText());
            ps.setString(3, txtTelefono.getText());
            ps.setString(4, txtEmail.getText());
            ps.setInt(5, id);
            ps.executeUpdate();
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void eliminar() {
        int fila = table.getSelectedRow();
        if (fila == -1) return;
        int id = (int) model.getValueAt(fila, 0);
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "DELETE FROM entrenadores WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
