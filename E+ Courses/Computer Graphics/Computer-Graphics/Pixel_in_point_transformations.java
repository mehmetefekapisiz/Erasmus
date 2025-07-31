import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Pixel_in_point_transformations extends JFrame {
    private BufferedImage originalImage, transformedImage;
    private JLabel imageLabel;

    public Pixel_in_point_transformations() {
        setTitle("Image Converter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4));
        JButton loadButton = new JButton("Upload Image");
        JButton negativeButton = new JButton("Negative");
        JButton addButton = new JButton("Add (+50)");
        JButton subButton = new JButton("Subtract (-50)");
        JButton mulButton = new JButton("Multiply (x1.2)");
        JButton divButton = new JButton("Divide (/1.2)");
        JButton grayscale1 = new JButton("Grey (Y Eq.)");
        JButton grayscale2 = new JButton("Grey (Luma)");

        buttonPanel.add(loadButton);
        buttonPanel.add(negativeButton);
        buttonPanel.add(addButton);
        buttonPanel.add(subButton);
        buttonPanel.add(mulButton);
        buttonPanel.add(divButton);
        buttonPanel.add(grayscale1);
        buttonPanel.add(grayscale2);

        add(buttonPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadImage());
        negativeButton.addActionListener(e -> applyTransformation(this::negative));
        addButton.addActionListener(e -> applyTransformation((rgb) -> add(rgb, 50)));
        subButton.addActionListener(e -> applyTransformation((rgb) -> add(rgb, -50)));
        mulButton.addActionListener(e -> applyTransformation((rgb) -> multiply(rgb, 1.2)));
        divButton.addActionListener(e -> applyTransformation((rgb) -> multiply(rgb, 1.0 / 1.2)));
        grayscale1.addActionListener(e -> applyTransformation(this::grayscaleY));
        grayscale2.addActionListener(e -> applyTransformation(this::grayscaleLuma));
    }

    private void loadImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                originalImage = ImageIO.read(chooser.getSelectedFile());
                transformedImage = deepCopy(originalImage);
                imageLabel.setIcon(new ImageIcon(transformedImage));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Image could not be loaded.");
            }
        }
    }

    private void applyTransformation(ColorTransformer transformer) {
        if (originalImage == null) return;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        transformedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                int newRgb = transformer.transform(rgb);
                transformedImage.setRGB(x, y, newRgb);
            }
        }
        imageLabel.setIcon(new ImageIcon(transformedImage));
    }

    private int negative(int rgb) {
        Color c = new Color(rgb);
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()).getRGB();
    }

    private int add(int rgb, int value) {
        Color c = new Color(rgb);
        int r = clamp(c.getRed() + value);
        int g = clamp(c.getGreen() + value);
        int b = clamp(c.getBlue() + value);
        return new Color(r, g, b).getRGB();
    }

    private int multiply(int rgb, double factor) {
        Color c = new Color(rgb);
        int r = clamp((int) (c.getRed() * factor));
        int g = clamp((int) (c.getGreen() * factor));
        int b = clamp((int) (c.getBlue() * factor));
        return new Color(r, g, b).getRGB();
    }

    private int grayscaleY(int rgb) {
        Color c = new Color(rgb);
        int y = (int) (0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue());
        return new Color(y, y, y).getRGB();
    }

    private int grayscaleLuma(int rgb) {
        Color c = new Color(rgb);
        int y = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
        return new Color(y, y, y).getRGB();
    }

    private int clamp(int value) {
        return Math.min(255, Math.max(0, value));
    }

    private BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    interface ColorTransformer {
        int transform(int rgb);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Pixel_in_point_transformations().setVisible(true));
    }
}