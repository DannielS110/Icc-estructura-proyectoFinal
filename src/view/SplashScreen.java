package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import controller.MazeController;
import view.MinecraftTheme;
import view.IntermediatePanel;
import utils.ImageLoader;

/**
 * Ventana de inicio con imagen de fondo
 */
public class SplashScreen extends JFrame {
    
    private JProgressBar progressBar;
    private Timer timer;
    private int progress = 0;
    private final int DURATION_MS = 5000; // 5 segundos
    private final int UPDATE_INTERVAL = 50; // Actualizar cada 50ms
    private final int TOTAL_UPDATES = DURATION_MS / UPDATE_INTERVAL;
    
    // Cache de la imagen para evitar cargarla múltiples veces
    private BufferedImage cachedBackgroundImage = null;
    
    // Fuente similar a Minecraft
    private Font minecraftFont = null;
    
    // Tema Minecraft
    private MinecraftTheme theme;
    
    public SplashScreen() {
        // Configurar la ventana
        setTitle("Resolvedor de Laberintos - Cargando...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setUndecorated(true); // Sin bordes de ventana
        
        // Cargar la imagen una sola vez
        loadBackgroundImage();
        
        // Cargar la fuente Minecraft
        loadMinecraftFont();
        
        // Inicializar tema
        theme = new MinecraftTheme();
        
        // Configurar el layout
        setLayout(new BorderLayout());
        
        // Crear un panel que cubra toda la ventana con la imagen de fondo
        JPanel fullScreenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Dibujar la imagen de fondo si está disponible
                if (cachedBackgroundImage != null) {
                    // Obtener dimensiones del panel
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();
                    
                    // Obtener dimensiones originales de la imagen
                    int imageWidth = cachedBackgroundImage.getWidth();
                    int imageHeight = cachedBackgroundImage.getHeight();
                    
                    // Calcular proporciones para mantener aspect ratio
                    double scaleX = (double) panelWidth / imageWidth;
                    double scaleY = (double) panelHeight / imageHeight;
                    double scale = Math.max(scaleX, scaleY); // Usar el mayor para cubrir todo el panel
                    
                    int scaledWidth = (int) (imageWidth * scale);
                    int scaledHeight = (int) (imageHeight * scale);
                    
                    // Calcular posición para centrar la imagen
                    int x = (panelWidth - scaledWidth) / 2;
                    int y = (panelHeight - scaledHeight) / 2;
                    
                    // Escalar la imagen
                    Image scaledImg = cachedBackgroundImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                    
                    // Dibujar la imagen de fondo centrada
                    g.drawImage(scaledImg, x, y, this);
                } else {
                    // Fallback: color de fondo estilo Minecraft
                    g.setColor(MinecraftTheme.BACKGROUND_DARK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Dibujar patrón de tierra
                    g.setColor(MinecraftTheme.DIRT_BROWN);
                    for (int i = 0; i < getWidth(); i += 20) {
                        for (int j = 0; j < getHeight(); j += 20) {
                            if ((i + j) % 40 == 0) {
                                g.fillRect(i, j, 20, 20);
                            }
                        }
                    }
                    
                    // Dibujar texto de fallback
                    g.setColor(MinecraftTheme.TEXT_GOLD);
                    g.setFont(MinecraftTheme.getMinecraftFont(24f));
                    
                    String title = "Resolvedor de Laberintos";
                    FontMetrics fm = g.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(title)) / 2;
                    int y = getHeight() / 2;
                    g.drawString(title, x, y);
                }
            }
        };
        
        fullScreenPanel.setLayout(new BorderLayout());
        
        // Agregar título en la parte superior
        JLabel titleLabel = new JLabel("PROYECTO FINAL - Resolvedor de Laberintos", SwingConstants.CENTER);
        MinecraftTheme.applyMinecraftTitleStyle(titleLabel);
        titleLabel.setFont(MinecraftTheme.getMinecraftFont(28f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        fullScreenPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Crear y configurar la barra de progreso
        createProgressBar(fullScreenPanel);
        
        // Agregar el panel completo a la ventana
        add(fullScreenPanel, BorderLayout.CENTER);
        
        // Forzar el repintado inicial
        revalidate();
        repaint();
        
        // Iniciar el timer para la barra de progreso
        startProgressTimer();
    }
    
    private void loadBackgroundImage() {
        // Intentar cargar desde el JAR primero
        cachedBackgroundImage = ImageLoader.loadImageFromJar("/images/inicio.jpg");
        
        // Si no se pudo cargar desde el JAR, intentar desde el sistema de archivos
        if (cachedBackgroundImage == null) {
            try {
                File imageFile = new File("inicio.jpg");
                String absolutePath = imageFile.getAbsolutePath();
                
                System.out.println("Cargando imagen desde sistema de archivos: " + absolutePath);
                
                ImageIcon imageIcon = new ImageIcon(absolutePath);
                Image originalImage = imageIcon.getImage();
                
                // Convertir a BufferedImage para asegurar que esté completamente cargada
                if (originalImage != null) {
                    cachedBackgroundImage = new BufferedImage(
                        originalImage.getWidth(null), 
                        originalImage.getHeight(null), 
                        BufferedImage.TYPE_INT_RGB
                    );
                    Graphics2D g2d = cachedBackgroundImage.createGraphics();
                    g2d.drawImage(originalImage, 0, 0, null);
                    g2d.dispose();
                }
                
                if (cachedBackgroundImage != null) {
                    System.out.println("¡Imagen cargada exitosamente desde sistema de archivos! Dimensiones: " + 
                                     cachedBackgroundImage.getWidth(null) + "x" + cachedBackgroundImage.getHeight(null));
                } else {
                    System.out.println("ERROR: La imagen es null");
                }
            } catch (Exception e) {
                System.out.println("Error cargando imagen desde sistema de archivos: " + e.getMessage());
                cachedBackgroundImage = null;
            }
        }
    }
    
    private void loadMinecraftFont() {
        minecraftFont = MinecraftTheme.getMinecraftFont(28f);
        System.out.println("Fuente Minecraft cargada: " + minecraftFont.getFamily());
    }
    

    
    private void createProgressBar(JPanel parentPanel) {
        // Panel para la barra de progreso (transparente para que se vea la imagen)
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setOpaque(false);
        
        // Barra de progreso estilo Minecraft
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Cargando...");
        progressBar.setFont(MinecraftTheme.getMinecraftFont(14f));
        progressBar.setForeground(MinecraftTheme.EMERALD_GREEN);
        progressBar.setBackground(MinecraftTheme.STONE_GRAY);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(400, 30));
        
        // Panel centrado para la barra (transparente)
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(progressBar);
        
        progressPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Agregar el panel de progreso en la parte inferior del panel padre
        parentPanel.add(progressPanel, BorderLayout.SOUTH);
    }
    
    private void startProgressTimer() {
        timer = new Timer(UPDATE_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress++;
                int percentage = (progress * 100) / TOTAL_UPDATES;
                progressBar.setValue(percentage);
                
                // Actualizar el texto de la barra
                if (percentage < 30) {
                    progressBar.setString("Inicializando...");
                } else if (percentage < 60) {
                    progressBar.setString("Cargando algoritmos...");
                } else if (percentage < 90) {
                    progressBar.setString("Preparando interfaz...");
                } else {
                    progressBar.setString("¡Listo!");
                }
                
                // Cuando llegue al 100%, cerrar esta ventana y abrir la principal
                if (progress >= TOTAL_UPDATES) {
                    timer.stop();
                    closeSplashAndOpenMain();
                }
            }
        });
        
        timer.start();
    }
    
    private void closeSplashAndOpenMain() {
        // Cerrar esta ventana
        dispose();
        
        // Abrir el panel intermedio
        SwingUtilities.invokeLater(() -> {
            try {
                // Crear y mostrar el panel intermedio
                new IntermediatePanel();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error al abrir el panel intermedio: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    public static void main(String[] args) {
        // Mostrar la ventana de inicio
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
        });
    }
} 