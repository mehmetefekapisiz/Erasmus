import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;

public class PPMViewer extends JFrame {

    private BufferedImage image;
    private JLabel imageLabel;

    public PPMViewer() {
        super("PPM Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        add(scrollPane, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadPPM = new JMenuItem("Load PPM");
        JMenuItem saveJPG = new JMenuItem("Save as JPEG");


        loadPPM.addActionListener(e -> loadPPMFile());
        saveJPG.addActionListener(e -> saveAsJPG());

        fileMenu.add(loadPPM);
        fileMenu.add(saveJPG);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void loadPPMFile() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                image = readPPM(file);
                imageLabel.setIcon(new ImageIcon(image));
                setTitle("PPM Viewer - " + file.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void saveAsJPG() {
        if (image == null) {
            JOptionPane.showMessageDialog(this, "No image loaded", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                ImageIO.write(image, "jpg", file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving JPEG: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private BufferedImage readPPM(File file) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            String magic = readNextToken(bis);
            boolean isAscii = magic.equals("P3");
            if (!magic.equals("P3") && !magic.equals("P6")) {
                throw new IOException("Unsupported PPM format: " + magic);
            }

            String token;
            do {token = readNextToken(bis);} while (token.startsWith("#"));

            int width = Integer.parseInt(token);
            int height = Integer.parseInt(readNextToken(bis));
            int maxColor = Integer.parseInt(readNextToken(bis));

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            if (isAscii) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int r = Integer.parseInt(readNextToken(reader));
                        int g = Integer.parseInt(readNextToken(reader));
                        int b = Integer.parseInt(readNextToken(reader));
                        int rgb = new Color(scale(r, maxColor), scale(g, maxColor), scale(b, maxColor)).getRGB();
                        img.setRGB(x, y, rgb);
                    }
                }
            } else {
                int numBytesPerComponent = maxColor < 256 ? 1 : 2;
                int totalBytes = width * height * 3 * numBytesPerComponent;
                byte[] data = bis.readNBytes(totalBytes);
                int index = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int r = readComponent(data, index, numBytesPerComponent);
                        index += numBytesPerComponent;
                        int g = readComponent(data, index, numBytesPerComponent);
                        index += numBytesPerComponent;
                        int b = readComponent(data, index, numBytesPerComponent);
                        index += numBytesPerComponent;
                        int rgb = new Color(scale(r, maxColor), scale(g, maxColor), scale(b, maxColor)).getRGB();
                        img.setRGB(x, y, rgb);
                    }
                }
            }

            return img;
        }
    }

    private int scale(int value, int max) {return (value * 255) / max;}

    private int readComponent(byte[] data, int index, int bytes) {
        if (bytes == 1) {
            return data[index] & 0xFF;
        } else {
            return ((data[index] & 0xFF) << 8) | (data[index + 1] & 0xFF);
        }
    }

    private String readNextToken(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int b;
        while ((b = in.read()) != -1) {
            if (!Character.isWhitespace(b)) {
                sb.append((char) b);
                break;
            }
        }
        while ((b = in.read()) != -1 && !Character.isWhitespace(b)) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    private String readNextToken(BufferedReader reader) throws IOException {
        String token = "";
        while (token.isEmpty()) {
            String line = reader.readLine();
            if (line == null) throw new EOFException();
            line = line.trim();
            if (!line.startsWith("#") && !line.isEmpty()) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreTokens()) token = st.nextToken();
            }
        }
        return token;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PPMViewer viewer = new PPMViewer();
            viewer.setVisible(true);
        });
    }
}