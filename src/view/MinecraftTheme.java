package view;

import java.awt.*;

/**
 * Clase de utilidades para el tema visual de Minecraft
 */
public class MinecraftTheme {
    
    // Colores principales de Minecraft
    public static final Color DIRT_BROWN = new Color(139, 69, 19);        // Marrón tierra
    public static final Color GRASS_GREEN = new Color(34, 139, 34);       // Verde hierba
    public static final Color STONE_GRAY = new Color(105, 105, 105);      // Gris piedra
    public static final Color WOOD_BROWN = new Color(160, 82, 45);        // Marrón madera
    public static final Color WATER_BLUE = new Color(30, 144, 255);       // Azul agua
    public static final Color LAVA_ORANGE = new Color(255, 69, 0);        // Naranja lava
    public static final Color GOLD_YELLOW = new Color(255, 215, 0);       // Amarillo oro
    public static final Color DIAMOND_BLUE = new Color(0, 191, 255);      // Azul diamante
    public static final Color EMERALD_GREEN = new Color(0, 255, 127);     // Verde esmeralda
    public static final Color REDSTONE_RED = new Color(220, 20, 60);      // Rojo redstone
    public static final Color NETHER_BLACK = new Color(25, 25, 25);       // Negro nether
    public static final Color END_PURPLE = new Color(138, 43, 226);       // Púrpura end
    
    // Colores para la interfaz
    public static final Color BACKGROUND_DARK = new Color(40, 40, 40);    // Fondo oscuro
    public static final Color PANEL_BROWN = new Color(101, 67, 33);       // Panel marrón
    public static final Color BUTTON_BROWN = new Color(139, 69, 19);      // Botón marrón
    public static final Color BUTTON_HOVER = new Color(160, 82, 45);      // Botón hover
    public static final Color TEXT_WHITE = new Color(255, 255, 255);      // Texto blanco
    public static final Color TEXT_GOLD = new Color(255, 215, 0);         // Texto dorado
    public static final Color BORDER_BROWN = new Color(101, 67, 33);      // Borde marrón
    
    // Colores para el laberinto
    public static final Color WALL_COLOR = WOOD_BROWN;                    // Paredes (marrón madera)
    public static final Color PATH_COLOR = DIRT_BROWN;                    // Camino
    public static final Color START_COLOR = EMERALD_GREEN;                // Inicio
    public static final Color END_COLOR = DIAMOND_BLUE;                   // Final
    public static final Color VISITED_COLOR = GRASS_GREEN;                // Visitado
    public static final Color CURRENT_COLOR = GOLD_YELLOW;                // Actual
    public static final Color SOLUTION_COLOR = REDSTONE_RED;              // Solución
    
    /**
     * Obtiene la fuente Minecraft
     */
    public static Font getMinecraftFont(float size) {
        String[] minecraftFonts = {
            "Courier New", "Consolas", "Monaco", 
            "DejaVu Sans Mono", "Lucida Console", 
            "Terminal", "Fixedsys"
        };
        
        for (String fontName : minecraftFonts) {
            Font testFont = new Font(fontName, Font.BOLD, (int)size);
            if (!testFont.getFamily().equals("Dialog")) {
                return testFont.deriveFont(size);
            }
        }
        
        return new Font(Font.MONOSPACED, Font.BOLD, (int)size);
    }
    
    /**
     * Aplica el tema Minecraft a un botón
     */
    public static void applyMinecraftButtonStyle(javax.swing.JButton button) {
        button.setFont(getMinecraftFont(14f));
        button.setBackground(BUTTON_BROWN);
        button.setForeground(TEXT_WHITE);
        button.setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        button.setOpaque(true);
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BROWN);
            }
        });
    }
    
    /**
     * Aplica el tema Minecraft a un panel
     */
    public static void applyMinecraftPanelStyle(javax.swing.JPanel panel) {
        panel.setBackground(PANEL_BROWN);
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(BORDER_BROWN, 2));
    }
    
    /**
     * Aplica el tema Minecraft a un label
     */
    public static void applyMinecraftLabelStyle(javax.swing.JLabel label) {
        label.setFont(getMinecraftFont(14f));
        label.setForeground(TEXT_WHITE);
        label.setOpaque(false);
    }
    
    /**
     * Aplica el tema Minecraft a un label con fondo claro
     */
    public static void applyMinecraftLabelStyleLight(javax.swing.JLabel label) {
        label.setFont(getMinecraftFont(14f));
        label.setForeground(Color.BLACK);
        label.setOpaque(false);
    }
    
    /**
     * Aplica el tema Minecraft a un título
     */
    public static void applyMinecraftTitleStyle(javax.swing.JLabel label) {
        label.setFont(getMinecraftFont(18f));
        label.setForeground(TEXT_GOLD);
        label.setOpaque(false);
    }
} 