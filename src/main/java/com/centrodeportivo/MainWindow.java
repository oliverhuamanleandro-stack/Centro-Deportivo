package com.centrodeportivo;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private String usuarioNombre;
    private String rol;

    public MainWindow(String usuarioNombre, String rol) {
        this.usuarioNombre = usuarioNombre;
        this.rol = rol;
        setTitle("Centro Deportivo - " + usuarioNombre + " [" + rol + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu mArchivo = new JMenu("Archivo");
        JMenuItem miSalir = new JMenuItem("Salir");
        miSalir.addActionListener(e -> System.exit(0));
        mArchivo.add(miSalir);

        // Menú Formularios
        JMenu mFormularios = new JMenu("Formularios");
        JMenuItem miSocios = new JMenuItem("Socios");
        miSocios.addActionListener(e -> new SociosForm().setVisible(true));
        JMenuItem miEntrenadores = new JMenuItem("Entrenadores");
        miEntrenadores.addActionListener(e -> new EntrenadoresForm().setVisible(true));
        JMenuItem miActividades = new JMenuItem("Actividades");
        miActividades.addActionListener(e -> new ActividadesForm().setVisible(true));
        JMenuItem miPagos = new JMenuItem("Pagos");
        miPagos.addActionListener(e -> new PagosForm().setVisible(true));
        JMenuItem miReservas = new JMenuItem("Reservas");
        miReservas.addActionListener(e -> new ReservasForm().setVisible(true));

        mFormularios.add(miSocios);
        mFormularios.add(miEntrenadores);
        mFormularios.add(miActividades);
        mFormularios.add(miPagos);
        mFormularios.add(miReservas);

        // Menú Reportes
        JMenu mReportes = new JMenu("Reportes");
        JMenuItem repAsistencia = new JMenuItem("Asistencia socios");
        repAsistencia.addActionListener(e -> Reportes.mostrarReporteAsistencia(this));
        JMenuItem repPagos = new JMenuItem("Pagos por periodo");
        repPagos.addActionListener(e -> Reportes.mostrarReportePagos(this));
        JMenuItem repCanchas = new JMenuItem("Uso de canchas");
        repCanchas.addActionListener(e -> Reportes.mostrarReporteReservas(this));
        mReportes.add(repAsistencia);
        mReportes.add(repPagos);
        mReportes.add(repCanchas);

        // Menú Consultas
        JMenu mConsultas = new JMenu("Consultas");
        JMenuItem cActPorEntr = new JMenuItem("Actividades por entrenador");
        cActPorEntr.addActionListener(e -> new ConsultaActividadesForm().setVisible(true));
        JMenuItem cSociosVenc = new JMenuItem("Socios con membresía vencida");
        cSociosVenc.addActionListener(e -> Consultas.mostrarSociosVencidos(this));
        mConsultas.add(cActPorEntr);
        mConsultas.add(cSociosVenc);

        // Menú Herramientas
        JMenu mHerramientas = new JMenu("Herramientas");
        JMenuItem miCalc = new JMenuItem("Calculadora");
        miCalc.addActionListener(e -> Tools.openCalculator());
        JMenuItem miPaint = new JMenuItem("Paint");
        miPaint.addActionListener(e -> Tools.openPaint());
        JMenuItem miExcel = new JMenuItem("Excel");
        miExcel.addActionListener(e -> Tools.openExcel());
        JMenuItem miChrome = new JMenuItem("Chrome");
        miChrome.addActionListener(e -> Tools.openChrome());
        JMenuItem miCMD = new JMenuItem("CMD");
        miCMD.addActionListener(e -> Tools.openCMD());
        mHerramientas.add(miCalc);
        mHerramientas.add(miPaint);
        mHerramientas.add(miExcel);
        mHerramientas.add(miChrome);
        mHerramientas.add(miCMD);

        // Menú Ayuda
        JMenu mAyuda = new JMenu("Ayuda");
        JMenuItem miManual = new JMenuItem("Manual del sistema");
        miManual.addActionListener(e -> new ManualForm().setVisible(true));
        mAyuda.add(miManual);

        // Agregar menús a la barra
        menuBar.add(mArchivo);
        menuBar.add(mFormularios);
        menuBar.add(mReportes);
        menuBar.add(mConsultas);
        menuBar.add(mHerramientas);
        menuBar.add(mAyuda);

        setJMenuBar(menuBar);

        // Rol-based: ejemplo básico
        if (!rol.equalsIgnoreCase("administrador")) {
            miEntrenadores.setEnabled(false);
        }

        add(new JLabel("Bienvenido al sistema. Use el menú para navegar.", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
