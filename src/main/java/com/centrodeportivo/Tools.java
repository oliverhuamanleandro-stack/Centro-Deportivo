package com.centrodeportivo;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Tools {

    public static void openCalculator() {
        launch("calc");
    }

    public static void openPaint() {
        launch("mspaint");
    }

    public static void openExcel() {
        try {
            // ✅ OPCIÓN 1: intenta abrir Excel directamente
            Runtime.getRuntime().exec("cmd /c start excel");
        } catch (IOException e1) {
            try {
                // ✅ OPCIÓN 2: abre un archivo Excel con la app predeterminada (Excel o LibreOffice)
                File file = new File("C:\\Users\\Public\\Documents\\ejemplo.xlsx"); 
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.err.println("Excel no está en el PATH y no se encontró el archivo para abrir.");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void openChrome() {
        launch("chrome");
    }

    public static void openCMD() {
        launch("cmd /c start cmd");
    }

    private static void launch(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}