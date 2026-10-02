package com.centrodeportivo;

import javax.swing.*;

public class ManualForm extends JFrame {

    public ManualForm() {
        setTitle("Manual del Sistema");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(
            "Manual del Sistema Centro Deportivo\n\n" +
            "1. Inicie sesión con usuario y contraseña.\n" +
            "2. En el menú 'Formularios' puede gestionar Socios, Entrenadores, Actividades, Pagos y Reservas.\n" +
            "3. En 'Reportes' puede consultar asistencia, pagos y uso de canchas.\n" +
            "4. En 'Consultas' puede ver actividades por entrenador y socios con membresía vencida.\n" +
            "5. En 'Herramientas' puede abrir Calculadora, Paint, Excel, Internet (Chrome) y CMD.\n" +
            "6. Para salir del sistema use el menú 'Salir'."
        );

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);
    }
}
