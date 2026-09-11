package vn.edu.eaut.warehouse;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import vn.edu.eaut.warehouse.view.MainFrame;

import javax.swing.*;

/**
 * Warehouse Desktop Application entry point.
 *
 * Uses FlatLaf for modern UI with dark mode support.
 * To enable dark mode: run with -Dflatlaf.theme=dark
 */
public class WarehouseApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String theme = System.getProperty("flatlaf.theme");
                if ("dark".equalsIgnoreCase(theme)) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } else {
                    // Apply FlatLaf light theme by default
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                }

                // Customize component defaults for better UX
                UIManager.put("Button.arc", 12);
                UIManager.put("Component.arc", 8);
                UIManager.put("TextComponent.arc", 6);
                UIManager.put("ScrollBar.width", 10);

            } catch (Exception ex) {
                System.err.println("Failed to apply FlatLaf, falling back to system look: " + ex.getMessage());
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            new MainFrame();
        });
    }
}
