package com.centrodeportivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ReservasForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbSocio;
    private JTextField txtCancha, txtFecha, txtHoraInicio, txtHoraFin;

    public ReservasForm() {
        setTitle("Gestión de Reservas");
        setSize(750, 420);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel l1 = new JLabel("Socio:");
        l1.setBounds(20, 20, 100, 25);
        add(l1);
        cbSocio = new JComboBox<>();
        cbSocio.setBounds(120, 20, 150, 25);
        add(cbSocio);

        JLabel l2 = new JLabel("Cancha:");
        l2.setBounds(20, 60, 100, 25);
        add(l2);
        txtCancha = new JTextField();
        txtCancha.setBounds(120, 60, 150, 25);
        add(txtCancha);

        JLabel l3 = new JLabel("Fecha:");
        l3.setBounds(20, 100, 100, 25);
        add(l3);
        txtFecha = new JTextField("2025-10-01");
        txtFecha.setBounds(120, 100, 150, 25);
        add(txtFecha);

        JLabel l4 = new JLabel("Hora Inicio:");
        l4.setBounds(20, 140, 100, 25);
        add(l4);
        txtHoraInicio = new JTextField("10:00");
        txtHoraInicio.setBounds(120, 140, 150, 25);
        add(txtHoraInicio);

        JLabel l5 = new JLabel("Hora Fin:");
        l5.setBounds(20, 180, 100, 25);
        add(l5);
        txtHoraFin = new JTextField("11:00");
        txtHoraFin.setBounds(120, 180, 150, 25);
        add(txtHoraFin);

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

        model = new DefaultTableModel(new String[]{"ID","Socio","Cancha","Fecha","Inicio","Fin"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 230, 680, 120);
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
            String sql = "SELECT r.id, s.nombre as socio, r.cancha, r.fecha, r.hora_inicio, r.hora_fin " +
                         "FROM reservas r JOIN socios s ON r.id_socio=s.id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("socio"),
                        rs.getString("cancha"),
                        rs.getString("fecha"),
                        rs.getString("hora_inicio"),
                        rs.getString("hora_fin")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void agregar() {
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO reservas(id_socio,cancha,fecha,hora_inicio,hora_fin) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(cbSocio.getSelectedItem().toString().split("-")[0]));
            ps.setString(2, txtCancha.getText());
            ps.setString(3, txtFecha.getText());
            ps.setString(4, txtHoraInicio.getText());
            ps.setString(5, txtHoraFin.getText());
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
            String sql = "UPDATE reservas SET id_socio=?,cancha=?,fecha=?,hora_inicio=?,hora_fin=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(cbSocio.getSelectedItem().toString().split("-")[0]));
            ps.setString(2, txtCancha.getText());
            ps.setString(3, txtFecha.getText());
            ps.setString(4, txtHoraInicio.getText());
            ps.setString(5, txtHoraFin.getText());
            ps.setInt(6, id);
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
            String sql = "DELETE FROM reservas WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            cargar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}