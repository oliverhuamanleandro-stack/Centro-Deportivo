package com.centrodeportivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class PagosForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbSocio;
    private JTextField txtMonto, txtFecha, txtMetodo;

    public PagosForm() {
        setTitle("Gestión de Pagos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel l1 = new JLabel("Socio:");
        l1.setBounds(20, 20, 100, 25);
        add(l1);
        cbSocio = new JComboBox<>();
        cbSocio.setBounds(120, 20, 150, 25);
        add(cbSocio);

        JLabel l2 = new JLabel("Monto:");
        l2.setBounds(20, 60, 100, 25);
        add(l2);
        txtMonto = new JTextField();
        txtMonto.setBounds(120, 60, 150, 25);
        add(txtMonto);

        JLabel l3 = new JLabel("Fecha:");
        l3.setBounds(20, 100, 100, 25);
        add(l3);
        txtFecha = new JTextField("2025-10-01");
        txtFecha.setBounds(120, 100, 150, 25);
        add(txtFecha);

        JLabel l4 = new JLabel("Método:");
        l4.setBounds(20, 140, 100, 25);
        add(l4);
        txtMetodo = new JTextField("Efectivo");
        txtMetodo.setBounds(120, 140, 150, 25);
        add(txtMetodo);

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

        model = new DefaultTableModel(new String[]{"ID","Socio","Monto","Fecha","Método"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 200, 640, 120);
        add(scroll);

        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> cargar());

        cargarSocios();
        cargar();
    }

    private void cargarSocios() {
        cbSocio.removeAllItems();
        try (Connection conn = ConnectionDB.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT id, nombre FROM socios");
            while (rs.next()) {
                cbSocio.addItem(rs.getInt("id") + "-" + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cargar() {
        model.setRowCount(0);
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "SELECT p.id, s.nombre as socio, p.monto, p.fecha_pago, p.metodo " +
                         "FROM pagos p JOIN socios s ON p.id_socio=s.id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("socio"),
                        rs.getDouble("monto"),
                        rs.getString("fecha_pago"),
                        rs.getString("metodo")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void agregar() {
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO pagos(id_socio,monto,fecha_pago,metodo) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(cbSocio.getSelectedItem().toString().split("-")[0]));
            ps.setDouble(2, Double.parseDouble(txtMonto.getText()));
            ps.setString(3, txtFecha.getText());
            ps.setString(4, txtMetodo.getText());
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
            String sql = "UPDATE pagos SET id_socio=?,monto=?,fecha_pago=?,metodo=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(cbSocio.getSelectedItem().toString().split("-")[0]));
            ps.setDouble(2, Double.parseDouble(txtMonto.getText()));
            ps.setString(3, txtFecha.getText());
            ps.setString(4, txtMetodo.getText());
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
            String sql = "DELETE FROM pagos WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
