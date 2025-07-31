import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class Binarization extends JFrame {
    private BufferedImage originalImage, grayImage, binarizedImage;
    private JLabel originalLabel, processedLabel, thresholdValueLabel;
    private JTextField manualThreshold;

    public Binarization() {
        setTitle("Binarization and Thresholding");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        originalLabel = new JLabel("Original / Grayscale", JLabel.CENTER);
        processedLabel = new JLabel("Binarized Image", JLabel.CENTER);
        imagePanel.add(originalLabel);
        imagePanel.add(processedLabel);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(imagePanel, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        String[] methods = {
                "Manual", "Percent Black", "Mean Iterative", "Entropy", "Min Error", "Fuzzy Min Error"
        };
        JComboBox<String> methodBox = new JComboBox<>(methods);
        JButton loadButton = new JButton("Upload Image");
        JButton applyButton = new JButton("Apply");
        manualThreshold = new JTextField("128", 5);
        thresholdValueLabel = new JLabel("Current Threshold: -");

        JPanel percentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel percentLabel = new JLabel("Percent Black Value (%):");
        JTextField percentValue = new JTextField("30", 3);
        percentPanel.add(percentLabel);
        percentPanel.add(percentValue);

        controls.add(loadButton);
        controls.add(new JLabel("Method:"));
        controls.add(methodBox);
        controls.add(new JLabel("Threshold (manual):"));
        controls.add(manualThreshold);
        controls.add(percentPanel);
        controls.add(applyButton);
        controls.add(thresholdValueLabel);

        add(controls, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton showOriginalButton = new JButton("Show Original");
        JButton showGrayscaleButton = new JButton("Show Grayscale");
        topPanel.add(showOriginalButton);
        topPanel.add(showGrayscaleButton);
        add(topPanel, BorderLayout.NORTH);

        loadButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    originalImage = ImageIO.read(fc.getSelectedFile());
                    grayImage = toGrayscale(originalImage);
                    originalLabel.setIcon(new ImageIcon(originalImage));
                    processedLabel.setIcon(null);
                    thresholdValueLabel.setText("Current Threshold: -");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error loading image: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        showOriginalButton.addActionListener(e -> {
            if (originalImage != null) {
                originalLabel.setIcon(new ImageIcon(originalImage));
            }
        });

        showGrayscaleButton.addActionListener(e -> {
            if (grayImage != null) {
                originalLabel.setIcon(new ImageIcon(grayImage));
            }
        });

        applyButton.addActionListener(e -> {
            if (grayImage == null) {
                JOptionPane.showMessageDialog(this,
                        "Please load an image first",
                        "No Image", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String method = (String) methodBox.getSelectedItem();
            int threshold = 128;

            try {
                if (method.equals("Manual")) {
                    try {
                        threshold = Integer.parseInt(manualThreshold.getText());
                        if (threshold < 0 || threshold > 255) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter a valid threshold (0-255)",
                                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if (method.equals("Percent Black")) {
                    try {
                        int percent = Integer.parseInt(percentValue.getText());
                        if (percent < 0 || percent > 100) throw new NumberFormatException();
                        threshold = percentBlack(grayImage, percent);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter a valid percentage (0-100)",
                                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if (method.equals("Mean Iterative")) {
                    threshold = meanIterative(grayImage);
                } else if (method.equals("Entropy")) {
                    threshold = entropyThreshold(grayImage);
                } else if (method.equals("Min Error")) {
                    threshold = minError(grayImage);
                } else if (method.equals("Fuzzy Min Error")) {
                    threshold = fuzzyMinError(grayImage);
                }

                binarizedImage = applyThreshold(grayImage, threshold);
                processedLabel.setIcon(new ImageIcon(binarizedImage));
                thresholdValueLabel.setText("Current Threshold: " + threshold);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error processing image: " + ex.getMessage(),
                        "Processing Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    private BufferedImage toGrayscale(BufferedImage img) {
        BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = gray.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return gray;
    }

    private BufferedImage applyThreshold(BufferedImage img, int threshold) {
        BufferedImage bin = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster src = img.getRaster();
        WritableRaster dst = bin.getRaster();
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++) {
                int val = src.getSample(x, y, 0);
                dst.setSample(x, y, 0, val > threshold ? 1 : 0);
            }
        return bin;
    }

    private int[] histogram(BufferedImage img) {
        int[] hist = new int[256];
        Raster r = img.getRaster();
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++)
                hist[r.getSample(x, y, 0)]++;
        return hist;
    }

    private int percentBlack(BufferedImage img, int percent) {
        int[] hist = histogram(img);
        int total = img.getWidth() * img.getHeight();
        int target = total * percent / 100;
        int sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += hist[t];
            if (sum >= target) return t;
        }
        return 128;
    }

    private int meanIterative(BufferedImage img) {
        int[] hist = histogram(img);
        int T = 128, prev;
        do {
            prev = T;
            long sum1 = 0, count1 = 0, sum2 = 0, count2 = 0;
            for (int i = 0; i <= T; i++) {
                sum1 += i * hist[i];
                count1 += hist[i];
            }
            for (int i = T + 1; i < 256; i++) {
                sum2 += i * hist[i];
                count2 += hist[i];
            }
            int m1 = (count1 == 0) ? 0 : (int)(sum1 / count1);
            int m2 = (count2 == 0) ? 0 : (int)(sum2 / count2);
            T = (m1 + m2) / 2;
        } while (Math.abs(T - prev) > 1);
        return T;
    }

    private int entropyThreshold(BufferedImage img) {
        int[] hist = histogram(img);
        int total = Arrays.stream(hist).sum();
        double maxEntropy = -1;
        int threshold = 128;

        for (int t = 0; t < 256; t++) {
            int wB = 0, wF = 0;
            for (int i = 0; i <= t; i++) wB += hist[i];
            for (int i = t + 1; i < 256; i++) wF += hist[i];
            if (wB == 0 || wF == 0) continue;

            double hB = 0, hF = 0;
            for (int i = 0; i <= t; i++) {
                double p = (double) hist[i] / wB;
                if (p > 0) hB -= p * Math.log(p);
            }
            for (int i = t + 1; i < 256; i++) {
                double p = (double) hist[i] / wF;
                if (p > 0) hF -= p * Math.log(p);
            }

            double entropy = hB + hF;
            if (entropy > maxEntropy) {
                maxEntropy = entropy;
                threshold = t;
            }
        }

        return threshold;
    }

    private int minError(BufferedImage img) {
        int[] hist = histogram(img);
        int total = Arrays.stream(hist).sum();
        double minError = Double.MAX_VALUE;
        int bestT = 128;

        for (int t = 1; t < 255; t++) {
            double w0 = 0, w1 = 0;
            double mu0 = 0, mu1 = 0;
            for (int i = 0; i <= t; i++) {
                w0 += hist[i];
                mu0 += i * hist[i];
            }
            for (int i = t + 1; i < 256; i++) {
                w1 += hist[i];
                mu1 += i * hist[i];
            }
            if (w0 == 0 || w1 == 0) continue;
            mu0 /= w0;
            mu1 /= w1;
            w0 /= total;
            w1 /= total;
            double sigma0 = 0, sigma1 = 0;
            for (int i = 0; i <= t; i++) sigma0 += hist[i] * (i - mu0) * (i - mu0);
            for (int i = t + 1; i < 256; i++) sigma1 += hist[i] * (i - mu1) * (i - mu1);
            sigma0 = sigma0 > 0 ? sigma0 / (w0 * total) : 0.00001;
            sigma1 = sigma1 > 0 ? sigma1 / (w1 * total) : 0.00001;

            double error = w0 * Math.log(sigma0) + w1 * Math.log(sigma1);
            if (error < minError) {
                minError = error;
                bestT = t;
            }
        }

        return bestT;
    }

    private int fuzzyMinError(BufferedImage img) {
        int[] hist = histogram(img);
        double[] fuzzyMembership = new double[256];
        int total = img.getWidth() * img.getHeight();

        double mean = 0;
        for (int i = 0; i < 256; i++) {
            mean += i * hist[i];
        }
        mean /= total;

        double variance = 0;
        for (int i = 0; i < 256; i++) {
            variance += (i - mean) * (i - mean) * hist[i];
        }
        variance /= total;
        double stdDev = Math.sqrt(variance);

        for (int i = 0; i < 256; i++) {
            fuzzyMembership[i] = Math.exp(-0.5 * Math.pow((i - mean) / stdDev, 2));
        }

        double minError = Double.MAX_VALUE;
        int bestT = 128;

        for (int t = 1; t < 255; t++) {
            double w0 = 0, w1 = 0;
            double mu0 = 0, mu1 = 0;

            for (int i = 0; i <= t; i++) {
                double fuzzyCount = hist[i] * fuzzyMembership[i];
                w0 += fuzzyCount;
                mu0 += i * fuzzyCount;
            }

            for (int i = t + 1; i < 256; i++) {
                double fuzzyCount = hist[i] * (1 - fuzzyMembership[i]);
                w1 += fuzzyCount;
                mu1 += i * fuzzyCount;
            }

            if (w0 == 0 || w1 == 0) continue;
            mu0 /= w0;
            mu1 /= w1;

            double sigma0 = 0, sigma1 = 0;
            for (int i = 0; i <= t; i++) {
                sigma0 += fuzzyMembership[i] * hist[i] * (i - mu0) * (i - mu0);
            }
            for (int i = t + 1; i < 256; i++) {
                sigma1 += (1 - fuzzyMembership[i]) * hist[i] * (i - mu1) * (i - mu1);
            }

            sigma0 = sigma0 > 0 ? sigma0 / w0 : 0.00001;
            sigma1 = sigma1 > 0 ? sigma1 / w1 : 0.00001;

            w0 /= total;
            w1 /= total;

            double error = w0 * Math.log(sigma0) + w1 * Math.log(sigma1);
            if (error < minError) {
                minError = error;
                bestT = t;
            }
        }

        return bestT;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Binarization().setVisible(true));
    }
}