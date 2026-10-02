package com.centrodeportivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Consultas {

    // Consulta: actividades por entrenador
    public static void mostrarActividadesPorEntrenador(JFrame parent) {
        String sql = "SELECT e.nombre AS entrenador, a.nombre AS actividad " +
                     "FROM actividades a " +
                     "JOIN entrenadores e ON a.id_entrenador = e.id " +
                     "ORDER BY e.nombre";

        mostrarTabla(parent, "Actividades por Entrenador", sql, "Entrenador", "Actividad");
    }

// Consulta: socios con membresías vencidas
public static void mostrarSociosVencidos(JFrame parent) {
String sql = "SELECT s.nombre, m.fecha_vencimiento " +
             "FROM socios s " +
             "JOIN membresias m ON s.id_membresia = m.id " +
             "WHERE m.fecha_vencimiento < CURDATE()";

    mostrarTabla(parent, "Socios con Membresías Vencidas", sql, "Socio", "Fecha Vencimiento");
}

    // Método genérico para mostrar JTable
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
