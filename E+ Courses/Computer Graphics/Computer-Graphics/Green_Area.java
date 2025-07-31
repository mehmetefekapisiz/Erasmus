import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.text.DecimalFormat;

public class Green_Area extends JFrame {
    private BufferedImage originalImage;
    private BufferedImage processedImage;
    private JLabel imageLabel;
    private JLabel resultLabel;

    public Green_Area() {
        setTitle("Green Area Rate Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton loadButton = new JButton("Select Image");
        loadButton.addActionListener(this::loadImage);
        controlPanel.add(loadButton);

        resultLabel = new JLabel("Green area ratio: -");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        controlPanel.add(resultLabel);

        add(controlPanel, BorderLayout.NORTH);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void loadImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(selectedFile);
                if (originalImage != null) {
                    displayImage(originalImage);
                    processImage();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The selected file is not a valid image.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "An error occurred while loading the image: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        if (width > 700 || height > 500) {
            double scale = Math.min(700.0 / width, 500.0 / height);
            width = (int) (width * scale);
            height = (int) (height * scale);

            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setIcon(new ImageIcon(img));
        }

        pack();
        setSize(Math.max(800, width + 50), Math.max(600, height + 100));
    }

    private void processImage() {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        processedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int totalPixels = width * height;
        int greenPixels = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(originalImage.getRGB(x, y));

                int r = pixelColor.getRed();
                int g = pixelColor.getGreen();
                int b = pixelColor.getBlue();

                boolean isGreen = g > r * 1.1 && g > b * 1.1;

                if (isGreen) {
                    processedImage.setRGB(x, y, Color.GREEN.getRGB());
                    greenPixels++;
                } else {
                    processedImage.setRGB(x, y, originalImage.getRGB(x, y));
                }
            }
        }

        double greenPercentage = (double) greenPixels / totalPixels * 100.0;

        displayProcessedImage();

        DecimalFormat df = new DecimalFormat("#.##");
        resultLabel.setText("Green area ratio: %" + df.format(greenPercentage));
    }

    private void displayProcessedImage() {
        JFrame processedFrame = new JFrame("Rendered Image - Green Areas");
        processedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel processedImageLabel = new JLabel();
        processedImageLabel.setHorizontalAlignment(JLabel.CENTER);

        int width = processedImage.getWidth();
        int height = processedImage.getHeight();

        if (width > 700 || height > 500) {
            double scale = Math.min(700.0 / width, 500.0 / height);
            width = (int) (width * scale);
            height = (int) (height * scale);

            Image scaledImage = processedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            processedImageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            processedImageLabel.setIcon(new ImageIcon(processedImage));
        }

        processedFrame.add(new JScrollPane(processedImageLabel));
        processedFrame.setSize(Math.min(800, width + 50), Math.min(600, height + 100));
        processedFrame.setLocationRelativeTo(null);
        processedFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Green_Area detector = new Green_Area();
            detector.setVisible(true);
        });
    }
}