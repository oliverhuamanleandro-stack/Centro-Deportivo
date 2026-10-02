package com.centrodeportivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Reportes {

    // Reporte: asistencia de socios
    public static void mostrarReporteAsistencia(JFrame parent) {
        String sql = "SELECT s.nombre AS socio, a.fecha " +
                     "FROM asistencia a " +
                     "JOIN socios s ON a.id_socio = s.id";

        mostrarTabla(parent, "Reporte de Asistencia", sql, "Socio", "Fecha");
    }

    // Reporte: pagos por período (ejemplo: último mes)
    public static void mostrarReportePagos(JFrame parent) {
        String sql = "SELECT s.nombre AS socio, p.monto, p.fecha_pago " +
                     "FROM pagos p " +
                     "JOIN socios s ON p.id_socio = s.id " +
                     "WHERE p.fecha_pago >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";

        mostrarTabla(parent, "Pagos del Último Mes", sql, "Socio", "Monto", "Fecha");
    }

    // Reporte: uso de canchas
    public static void mostrarReporteReservas(JFrame parent) {
        String sql = "SELECT c.nombre AS cancha, r.fecha, s.nombre AS socio " +
                     "FROM reservas r " +
                     "JOIN canchas c ON r.id_cancha = c.id " +
                     "JOIN socios s ON r.id_socio = s.id";

        mostrarTabla(parent, "Uso de Canchas", sql, "Cancha", "Fecha", "Socio");
    }

    // Método genérico para mostrar resultados en JTable
    private static void mostrarTabla(JFrame parent, String titulo, String sql, String... columnas) {
        try (Connection con = ConnectionDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            DefaultTableModel model = new DefaultTableModel(columnas, 0);

            while (rs.next()) {
                Object[] fila = new Object[columnas.length];
                for (int i = 0; i < columnas.length; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                model.addRow(fila);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);

            JDialog dialog = new JDialog(parent, titulo, true);
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(parent);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
