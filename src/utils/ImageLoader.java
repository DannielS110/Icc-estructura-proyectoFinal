package utils;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Clase utilitaria para cargar imágenes desde el JAR
 */
public class ImageLoader {
    
    /**
     * Carga una imagen desde el JAR usando getResource()
     * @param imagePath Ruta de la imagen dentro del JAR (ej: "/images/pasto.jpg")
     * @return BufferedImage cargada o null si no se puede cargar
     */
    public static BufferedImage loadImageFromJar(String imagePath) {
        try {
            // Intentar cargar desde el JAR
            InputStream inputStream = ImageLoader.class.getResourceAsStream(imagePath);
            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);
                inputStream.close();
                System.out.println("Imagen cargada desde JAR: " + imagePath);
                return image;
            } else {
                System.out.println("No se pudo cargar la imagen desde JAR: " + imagePath);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error cargando imagen desde JAR: " + imagePath + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Carga una imagen desde el JAR y la convierte a Image
     * @param imagePath Ruta de la imagen dentro del JAR
     * @return Image cargada o null si no se puede cargar
     */
    public static Image loadImageAsImage(String imagePath) {
        BufferedImage bufferedImage = loadImageFromJar(imagePath);
        return bufferedImage;
    }
    
    /**
     * Carga una imagen desde el JAR y la escala
     * @param imagePath Ruta de la imagen dentro del JAR
     * @param width Ancho deseado
     * @param height Alto deseado
     * @return Image escalada o null si no se puede cargar
     */
    public static Image loadAndScaleImage(String imagePath, int width, int height) {
        BufferedImage originalImage = loadImageFromJar(imagePath);
        if (originalImage != null) {
            return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
        return null;
    }
} 