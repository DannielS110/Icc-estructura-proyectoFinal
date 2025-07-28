package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import controller.MazeController;
import view.MinecraftTheme;
import utils.ImageLoader;

/**
 * Panel intermedio entre SplashScreen y MazeView
 */
public class IntermediatePanel extends JFrame {

    // Cache de la imagen de fondo
    private BufferedImage cachedBackgroundImage = null;

    public IntermediatePanel() {
        // Configurar la ventana
        setTitle("Resolvedor de Laberintos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setUndecorated(true); // Sin bordes de ventana

        // Cargar la imagen de fondo
        loadBackgroundImage();

        // Configurar el layout
        setLayout(new BorderLayout());

        // Crear el panel principal con imagen de fondo
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Forzar el repintado inicial
        revalidate();
        repaint();

        // Hacer visible la ventana
        setVisible(true);
    }

    private void loadBackgroundImage() {
        // Intentar cargar desde el JAR primero
        cachedBackgroundImage = ImageLoader.loadImageFromJar("/images/integrantes.jpg");

        // Si no se pudo cargar desde el JAR, intentar desde el sistema de archivos
        if (cachedBackgroundImage == null) {
            try {
                File imageFile = new File("src/images/integrantes.jpg");
                String absolutePath = imageFile.getAbsolutePath();

                System.out.println("Cargando imagen de integrantes desde sistema de archivos: " + absolutePath);

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
                    System.out.println("¡Imagen de fondo cargada exitosamente desde sistema de archivos! Dimensiones: " +
                            cachedBackgroundImage.getWidth(null) + "x" + cachedBackgroundImage.getHeight(null));
                } else {
                    System.out.println("ERROR: La imagen de fondo es null");
                }
            } catch (Exception e) {
                System.out.println("Error cargando imagen de fondo desde sistema de archivos: " + e.getMessage());
                cachedBackgroundImage = null;
            }
        }
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel() {
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

        panel.setLayout(new BorderLayout());

        // Crear y configurar el botón con flecha en la esquina inferior derecha
        createArrowButton(panel);

        return panel;
    }

    private void createArrowButton(JPanel parentPanel) {
        // Panel para el botón (transparente)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 30)); // Margen inferior derecho

        // Cargar icono de flecha desde JAR primero
        ImageIcon arrowIcon = null;
        BufferedImage arrowImage = ImageLoader.loadImageFromJar("/images/flecha.png");

        if (arrowImage != null) {
            // Escalar el icono si es necesario
            Image scaledImage = arrowImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            arrowIcon = new ImageIcon(scaledImage);
        } else {
            // Fallback: intentar desde sistema de archivos
            try {
                File arrowFile = new File("src/images/flecha.png");
                arrowIcon = new ImageIcon(arrowFile.getAbsolutePath());

                // Escalar el icono si es necesario
                if (arrowIcon.getImage() != null) {
                    Image scaledImage = arrowIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    arrowIcon = new ImageIcon(scaledImage);
                }
            } catch (Exception e) {
                System.out.println("Error cargando icono de flecha: " + e.getMessage());
            }
        }

        // Crear botón con flecha
        JButton continueButton = new JButton("Continuar", arrowIcon);
        MinecraftTheme.applyMinecraftButtonStyle(continueButton);
        continueButton.setPreferredSize(new Dimension(150, 40));
        continueButton.setFont(MinecraftTheme.getMinecraftFont(16f));

        // Agregar listener para continuar al MazeView
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAndOpenMain();
            }
        });

        buttonPanel.add(continueButton);
        parentPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void closeAndOpenMain() {
        // Cerrar esta ventana
        dispose();

        // Abrir la ventana principal
        SwingUtilities.invokeLater(() -> {
            try {
                // Crear y mostrar la ventana principal
                MazeController controller = new MazeController();
                // El MazeController ya hace setVisible(true) en su constructor
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error al abrir la aplicación principal: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    public static void main(String[] args) {
        // Mostrar el panel intermedio
        SwingUtilities.invokeLater(() -> {
            IntermediatePanel panel = new IntermediatePanel();
            panel.setVisible(true);
        });
    }
}