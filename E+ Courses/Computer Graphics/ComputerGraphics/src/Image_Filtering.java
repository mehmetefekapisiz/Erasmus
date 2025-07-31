import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class Image_Filtering extends JFrame {
    BufferedImage original, processed;
    JLabel label;

    public Image_Filtering() {
        setTitle("Image Filtering");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JButton loadBtn = new JButton("Load Image");
        loadBtn.addActionListener(e -> loadImage());

        JPanel buttons = new JPanel(new GridLayout(2, 4));
        buttons.add(loadBtn);

        String[] filters = {"Mean", "Median", "Gaussian", "Sharpen", "Sobel X", "Sobel Y", "Custom"};
        for (String f : filters) {
            JButton btn = new JButton(f);
            btn.addActionListener(e -> applyFilter(f));
            buttons.add(btn);
        }

        add(buttons, BorderLayout.SOUTH);

        label = new JLabel("No image loaded", JLabel.CENTER);
        add(new JScrollPane(label), BorderLayout.CENTER);
    }

    void loadImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                original = ImageIO.read(chooser.getSelectedFile());
                processed = deepCopy(original);
                label.setIcon(new ImageIcon(processed));
                label.setText(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    void applyFilter(String type) {
        if (original == null) return;

        switch (type) {
            case "Mean":
                processed = convolve(original, meanKernel(3));
                break;
            case "Median":
                processed = medianFilter(original, 3);
                break;
            case "Gaussian":
                processed = convolve(original, gaussianKernel(3, 1));
                break;
            case "Sharpen":
                processed = convolve(original, new float[][]{
                        { 0, -1,  0},
                        {-1,  5, -1},
                        { 0, -1,  0}
                });
                break;
            case "Sobel X":
                processed = convolve(original, new float[][]{
                        {-1, 0, 1},
                        {-2, 0, 2},
                        {-1, 0, 1}
                });
                break;
            case "Sobel Y":
                processed = convolve(original, new float[][]{
                        {-1, -2, -1},
                        { 0,  0,  0},
                        { 1,  2,  1}
                });
                break;
            case "Custom":
                int size = Integer.parseInt(JOptionPane.showInputDialog(this, "Kernel size (odd):", "3"));
                float[][] custom = new float[size][size];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        custom[i][j] = Float.parseFloat(JOptionPane.showInputDialog(this, "Value at [" + i + "][" + j + "]"));
                    }
                }
                processed = convolve(original, custom);
                break;
        }

        label.setIcon(new ImageIcon(processed));
    }

    BufferedImage convolve(BufferedImage img, float[][] kernel) {
        int w = img.getWidth(), h = img.getHeight(), kw = kernel.length, kh = kernel[0].length;
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = kh / 2; y < h - kh / 2; y++) {
            for (int x = kw / 2; x < w - kw / 2; x++) {
                float[] rgb = new float[3];

                for (int ky = 0; ky < kh; ky++) {
                    for (int kx = 0; kx < kw; kx++) {
                        int px = x + kx - kw / 2;
                        int py = y + ky - kh / 2;
                        Color c = new Color(img.getRGB(px, py));
                        float coeff = kernel[ky][kx];
                        rgb[0] += c.getRed() * coeff;
                        rgb[1] += c.getGreen() * coeff;
                        rgb[2] += c.getBlue() * coeff;
                    }
                }

                for (int i = 0; i < 3; i++) {
                    rgb[i] = Math.max(0, Math.min(255, rgb[i]));
                }

                out.setRGB(x, y, new Color((int) rgb[0], (int) rgb[1], (int) rgb[2]).getRGB());
            }
        }

        return out;
    }

    BufferedImage medianFilter(BufferedImage img, int size) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int r = size / 2;

        for (int y = r; y < h - r; y++) {
            for (int x = r; x < w - r; x++) {
                int[] reds = new int[size * size];
                int[] greens = new int[size * size];
                int[] blues = new int[size * size];
                int idx = 0;

                for (int dy = -r; dy <= r; dy++) {
                    for (int dx = -r; dx <= r; dx++) {
                        Color c = new Color(img.getRGB(x + dx, y + dy));
                        reds[idx] = c.getRed();
                        greens[idx] = c.getGreen();
                        blues[idx] = c.getBlue();
                        idx++;
                    }
                }

                Arrays.sort(reds);
                Arrays.sort(greens);
                Arrays.sort(blues);
                out.setRGB(x, y, new Color(reds[idx / 2], greens[idx / 2], blues[idx / 2]).getRGB());
            }
        }

        return out;
    }

    float[][] meanKernel(int size) {
        float[][] kernel = new float[size][size];
        float val = 1.0f / (size * size);
        for (float[] row : kernel) Arrays.fill(row, val);
        return kernel;
    }

    float[][] gaussianKernel(int size, float sigma) {
        float[][] kernel = new float[size][size];
        int r = size / 2;
        float sum = 0;
        for (int y = -r; y <= r; y++) {
            for (int x = -r; x <= r; x++) {
                float val = (float) (Math.exp(-(x * x + y * y) / (2 * sigma * sigma)));
                kernel[y + r][x + r] = val;
                sum += val;
            }
        }
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                kernel[y][x] /= sum;
        return kernel;
    }

    BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Image_Filtering().setVisible(true));
    }
}