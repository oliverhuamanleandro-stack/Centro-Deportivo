package com.centrodeportivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class SociosForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNombre, txtTelefono, txtEmail, txtIngreso, txtVencimiento;

    public SociosForm() {
        setTitle("Gestión de Socios");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel l1 = new JLabel("Nombre:");
        l1.setBounds(20, 20, 100, 25);
        add(l1);
        txtNombre = new JTextField();
        txtNombre.setBounds(120, 20, 150, 25);
        add(txtNombre);

        JLabel l2 = new JLabel("Teléfono:");
        l2.setBounds(20, 60, 100, 25);
        add(l2);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(120, 60, 150, 25);
        add(txtTelefono);

        JLabel l3 = new JLabel("Email:");
        l3.setBounds(20, 100, 100, 25);
        add(l3);
        txtEmail = new JTextField();
        txtEmail.setBounds(120, 100, 150, 25);
        add(txtEmail);

        JLabel l4 = new JLabel("Ingreso (YYYY-MM-DD):");
        l4.setBounds(20, 140, 200, 25);
        add(l4);
        txtIngreso = new JTextField();
        txtIngreso.setBounds(220, 140, 150, 25);
        add(txtIngreso);

        JLabel l5 = new JLabel("Vencimiento (YYYY-MM-DD):");
        l5.setBounds(20, 180, 200, 25);
        add(l5);
        txtVencimiento = new JTextField();
        txtVencimiento.setBounds(220, 180, 150, 25);
        add(txtVencimiento);

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

        // Tabla
        model = new DefaultTableModel(new String[]{"ID","Nombre","Teléfono","Email","Ingreso","Vencimiento"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 220, 640, 120);
        add(scroll);

        // Eventos
        btnAgregar.addActionListener(e -> agregarSocio());
        btnEditar.addActionListener(e -> editarSocio());
        btnEliminar.addActionListener(e -> eliminarSocio());
        btnActualizar.addActionListener(e -> cargarSocios());

        cargarSocios();
    }

    private void cargarSocios() {
        model.setRowCount(0); // limpiar tabla
        try (Connection conn = ConnectionDB.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM socios");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getDate("fecha_ingreso"),
                    rs.getDate("fecha_vencimiento")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar socios: " + e.getMessage());
        }
    }

    private void agregarSocio() {
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO socios (nombre, telefono, email, fecha_ingreso, fecha_vencimiento) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtTelefono.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, txtIngreso.getText());
            ps.setString(5, txtVencimiento.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Socio agregado.");
            cargarSocios();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar socio: " + e.getMessage());
        }
    }

    private void editarSocio() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un socio para editar.");
            return;
        }
        int id = (int) model.getValueAt(fila, 0);
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "UPDATE socios SET nombre=?, telefono=?, email=?, fecha_ingreso=?, fecha_vencimiento=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtTelefono.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, txtIngreso.getText());
            ps.setString(5, txtVencimiento.getText());
            ps.setInt(6, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Socio actualizado.");
            cargarSocios();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al editar socio: " + e.getMessage());
        }
    }

    private void eliminarSocio() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un socio para eliminar.");
            return;
        }
        int id = (int) model.getValueAt(fila, 0);
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "DELETE FROM socios WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Socio eliminado.");
            cargarSocios();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar socio: " + e.getMessage());
        }
    }
}
