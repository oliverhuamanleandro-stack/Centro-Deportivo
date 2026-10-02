package com.centrodeportivo;

import javax.swing.*;
import java.awt.*;

public class ConsultaActividadesForm extends JFrame {

    public ConsultaActividadesForm() {
        setTitle("Consulta - Actividades por Entrenador");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());

        JLabel lblTitulo = new JLabel("Consulta de actividades por entrenador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Tabla de ejemplo
        String[] columnas = {"Entrenador", "Actividad", "Horario"};
        Object[][] datos = {
                {"Juan Pérez", "Fútbol", "Lunes 18:00"},
                {"María López", "Natación", "Martes 10:00"},
                {"Carlos Ruiz", "Gimnasio", "Miércoles 20:00"}
        };

        JTable tabla = new JTable(datos, columnas);
        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);

        add(panel);
    }
}
