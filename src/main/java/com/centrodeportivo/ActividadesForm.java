package com.centrodeportivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ActividadesForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNombre, txtHorario;
    private JComboBox<String> cbEntrenador;

    public ActividadesForm() {
        setTitle("Gestión de Actividades");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel l1 = new JLabel("Nombre:");
        l1.setBounds(20, 20, 100, 25);
        add(l1);
        txtNombre = new JTextField();
        txtNombre.setBounds(120, 20, 150, 25);
        add(txtNombre);

        JLabel l2 = new JLabel("Horario:");
        l2.setBounds(20, 60, 100, 25);
        add(l2);
        txtHorario = new JTextField();
        txtHorario.setBounds(120, 60, 150, 25);
        add(txtHorario);

        JLabel l3 = new JLabel("Entrenador:");
        l3.setBounds(20, 100, 100, 25);
        add(l3);
        cbEntrenador = new JComboBox<>();
        cbEntrenador.setBounds(120, 100, 150, 25);
        add(cbEntrenador);

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

        model = new DefaultTableModel(new String[]{"ID","Nombre","Horario","Entrenador"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 200, 640, 120);
        add(scroll);

        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargar());

        cargarEntrenadores();
        cargar();
    }

    private void cargarEntrenadores() {
        cbEntrenador.removeAllItems();
        try (Connection conn = ConnectionDB.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id, nombre FROM entrenadores");
            while (rs.next()) {
                cbEntrenador.addItem(rs.getInt("id") + "-" + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargar() {
        model.setRowCount(0);
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "SELECT a.id, a.nombre, a.horario, e.nombre as entrenador FROM actividades a LEFT JOIN entrenadores e ON a.id_entrenador=e.id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("horario"),
                        rs.getString("entrenador")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void agregar() {
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO actividades(nombre,horario,id_entrenador) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtHorario.getText());
            ps.setInt(3, Integer.parseInt(cbEntrenador.getSelectedItem().toString().split("-")[0]));
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
            String sql = "UPDATE actividades SET nombre=?,horario=?,id_entrenador=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtHorario.getText());
            ps.setInt(3, Integer.parseInt(cbEntrenador.getSelectedItem().toString().split("-")[0]));
            ps.setInt(4, id);
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
            String sql = "DELETE FROM actividades WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
