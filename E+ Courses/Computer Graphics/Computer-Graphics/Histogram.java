import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Histogram extends JFrame {
    private BufferedImage originalImage, processedImage;
    private JLabel imageLabel;
    private HistogramPanel histogramPanel;

    public Histogram() {
        setTitle("Histogram Processing");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        imageLabel = new JLabel("", JLabel.CENTER);
        histogramPanel = new HistogramPanel();

        add(imageLabel, BorderLayout.CENTER);
        add(histogramPanel, BorderLayout.EAST);

        JPanel buttons = new JPanel(new FlowLayout());
        JButton load = new JButton("Upload Image");
        JButton stretch = new JButton("Contrast Stretch");
        JButton equalize = new JButton("Histogram Equalize");

        buttons.add(load);
        buttons.add(stretch);
        buttons.add(equalize);
        add(buttons, BorderLayout.SOUTH);

        load.addActionListener(e -> loadImage());
        stretch.addActionListener(e -> {
            if (originalImage != null) {
                processedImage = contrastStretch(originalImage);
                updateImage(processedImage);
            }
        });

        equalize.addActionListener(e -> {
            if (originalImage != null) {
                processedImage = equalizeHistogram(originalImage);
                updateImage(processedImage);
            }
        });
    }

    private void loadImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                originalImage = ImageIO.read(chooser.getSelectedFile());
                processedImage = convertToGrayscale(originalImage);
                updateImage(processedImage);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Image could not be loaded.");
            }
        }
    }

    private void updateImage(BufferedImage img) {
        imageLabel.setIcon(new ImageIcon(img));
        histogramPanel.setImage(img);
        histogramPanel.repaint();
    }

    private BufferedImage convertToGrayscale(BufferedImage img) {
        BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = gray.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return gray;
    }

    private BufferedImage contrastStretch(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        Raster src = img.getRaster();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster dst = result.getRaster();

        int min = 255, max = 0;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int val = src.getSample(x, y, 0);
                if (val < min) min = val;
                if (val > max) max = val;
            }

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int val = src.getSample(x, y, 0);
                int newVal = (val - min) * 255 / (max - min);
                dst.setSample(x, y, 0, newVal);
            }

        return result;
    }

    private BufferedImage equalizeHistogram(BufferedImage img) {
        int w = img.getWidth(), h = img.getHeight();
        Raster src = img.getRaster();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster dst = result.getRaster();

        int[] hist = new int[256];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                hist[src.getSample(x, y, 0)]++;

        float[] cdf = new float[256];
        cdf[0] = hist[0];
        for (int i = 1; i < 256; i++)
            cdf[i] = cdf[i - 1] + hist[i];

        for (int i = 0; i < 256; i++)
            cdf[i] = (cdf[i] - cdf[0]) / (w * h - 1) * 255;

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int val = src.getSample(x, y, 0);
                int newVal = Math.min(255, Math.max(0, Math.round(cdf[val])));
                dst.setSample(x, y, 0, newVal);
            }

        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Histogram().setVisible(true));
    }
}

class HistogramPanel extends JPanel {
    private int[] histogram = new int[256];

    public void setImage(BufferedImage img) {
        histogram = new int[256];
        Raster r = img.getRaster();
        for (int y = 0; y < img.getHeight(); y++)
            for (int x = 0; x < img.getWidth(); x++)
                histogram[r.getSample(x, y, 0)]++;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int max = 0;
        for (int h : histogram) if (h > max) max = h;

        g.setColor(Color.BLACK);
        for (int i = 0; i < 256; i++) {
            int height = (int) ((histogram[i] / (float) max) * getHeight());
            g.drawLine(i, getHeight(), i, getHeight() - height);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(256, 400);
    }
}