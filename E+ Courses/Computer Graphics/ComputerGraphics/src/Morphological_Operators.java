import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Morphological_Operators extends JFrame {
    BufferedImage original, binary, result;
    JLabel imageLabel;

    int[][] structElem = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0}
    };

    int[][] hitMissKernel = {
            {-1, -1, -1},
            {-1,  1, -1},
            {-1, -1, -1}
    };

    public Morphological_Operators() {
        setTitle("Morphological Operators");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        imageLabel = new JLabel("No image", JLabel.CENTER);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(2, 4));

        String[] ops = {"Load", "Binarize", "Dilate", "Erode", "Open", "Close", "HitMiss"};
        for (String op : ops) {
            JButton btn = new JButton(op);
            btn.addActionListener(e -> handleAction(op));
            panel.add(btn);
        }

        add(panel, BorderLayout.SOUTH);
    }

    void handleAction(String action) {
        switch (action) {
            case "Load":
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        original = ImageIO.read(chooser.getSelectedFile());
                        binary = deepCopy(original);
                        imageLabel.setIcon(new ImageIcon(original));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this,
                                "Error loading image: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
                break;
            case "Binarize":
                if (original == null) {
                    JOptionPane.showMessageDialog(this, "Please load an image first");
                    return;
                }
                binary = binarize(original, 128);
                imageLabel.setIcon(new ImageIcon(binary));
                break;
            case "Dilate":
                if (binary == null) {
                    JOptionPane.showMessageDialog(this, "Please binarize the image first");
                    return;
                }
                result = dilate(binary, structElem);
                imageLabel.setIcon(new ImageIcon(result));
                break;
            case "Erode":
                if (binary == null) {
                    JOptionPane.showMessageDialog(this, "Please binarize the image first");
                    return;
                }
                result = erode(binary, structElem);
                imageLabel.setIcon(new ImageIcon(result));
                break;
            case "Open":
                if (binary == null) {
                    JOptionPane.showMessageDialog(this, "Please binarize the image first");
                    return;
                }
                result = open(binary, structElem);
                imageLabel.setIcon(new ImageIcon(result));
                break;
            case "Close":
                if (binary == null) {
                    JOptionPane.showMessageDialog(this, "Please binarize the image first");
                    return;
                }
                result = close(binary, structElem);
                imageLabel.setIcon(new ImageIcon(result));
                break;
            case "HitMiss":
                if (binary == null) {
                    JOptionPane.showMessageDialog(this, "Please binarize the image first");
                    return;
                }
                result = hitOrMiss(binary, hitMissKernel);
                imageLabel.setIcon(new ImageIcon(result));
                break;
        }
    }

    BufferedImage binarize(BufferedImage img, int threshold) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(img.getRGB(x, y));
                int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                int val = (gray < threshold) ? 0 : 255;
                out.setRGB(x, y, new Color(val, val, val).getRGB());
            }
        }
        return out;
    }

    BufferedImage dilate(BufferedImage img, int[][] kernel) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D g = out.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
        g.dispose();

        int kr = kernel.length / 2;

        for (int y = kr; y < h - kr; y++) {
            for (int x = kr; x < w - kr; x++) {
                boolean match = false;
                for (int j = 0; j < kernel.length; j++) {
                    for (int i = 0; i < kernel[0].length; i++) {
                        if (kernel[j][i] == 1) {
                            int px = x + i - kr;
                            int py = y + j - kr;
                            int rgb = new Color(img.getRGB(px, py)).getRed();
                            if (rgb == 255) {
                                match = true;
                                break;
                            }
                        }
                    }
                    if (match) break;
                }
                int val = match ? 255 : 0;
                out.setRGB(x, y, new Color(val, val, val).getRGB());
            }
        }

        return out;
    }

    BufferedImage erode(BufferedImage img, int[][] kernel) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D g = out.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
        g.dispose();

        int kr = kernel.length / 2;

        for (int y = kr; y < h - kr; y++) {
            for (int x = kr; x < w - kr; x++) {
                boolean match = true;
                for (int j = 0; j < kernel.length; j++) {
                    for (int i = 0; i < kernel[0].length; i++) {
                        if (kernel[j][i] == 1) {
                            int px = x + i - kr;
                            int py = y + j - kr;
                            int rgb = new Color(img.getRGB(px, py)).getRed();
                            if (rgb == 0) {
                                match = false;
                                break;
                            }
                        }
                    }
                    if (!match) break;
                }
                int val = match ? 255 : 0;
                out.setRGB(x, y, new Color(val, val, val).getRGB());
            }
        }

        return out;
    }

    BufferedImage open(BufferedImage img, int[][] kernel) {
        return dilate(erode(img, kernel), kernel);
    }

    BufferedImage close(BufferedImage img, int[][] kernel) {
        return erode(dilate(img, kernel), kernel);
    }

    BufferedImage hitOrMiss(BufferedImage img, int[][] kernel) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D g = out.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
        g.dispose();

        int kr = kernel.length / 2;

        for (int y = kr; y < h - kr; y++) {
            for (int x = kr; x < w - kr; x++) {
                boolean match = true;
                for (int j = 0; j < kernel.length; j++) {
                    for (int i = 0; i < kernel[0].length; i++) {
                        if (kernel[j][i] != 0) {
                            int px = x + i - kr;
                            int py = y + j - kr;
                            int pixelVal = new Color(img.getRGB(px, py)).getRed();

                            if ((kernel[j][i] == 1 && pixelVal == 0) ||
                                    (kernel[j][i] == -1 && pixelVal == 255)) {
                                match = false;
                                break;
                            }
                        }
                    }
                    if (!match) break;
                }
                int val = match ? 255 : 0;
                out.setRGB(x, y, new Color(val, val, val).getRGB());
            }
        }
        return out;
    }

    BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Morphological_Operators().setVisible(true));
    }
}
