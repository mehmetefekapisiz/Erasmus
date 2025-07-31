import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Color_Models extends JFrame {
    private JTextField rField, gField, bField;
    private JTextField cField, mField, yField, kField;
    private JPanel rgbColorPreview, cmykColorPreview;

    public Color_Models() {
        setTitle("Color Converter");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 1));

        JPanel rgbPanel = new JPanel();
        rgbPanel.setBorder(BorderFactory.createTitledBorder("RGB Input"));
        rField = new JTextField("255", 3);
        gField = new JTextField("255", 3);
        bField = new JTextField("255", 3);
        rgbPanel.add(new JLabel("R:"));
        rgbPanel.add(rField);
        rgbPanel.add(new JLabel("G:"));
        rgbPanel.add(gField);
        rgbPanel.add(new JLabel("B:"));
        rgbPanel.add(bField);
        JButton toCMYK = new JButton("RGB → CMYK");
        rgbPanel.add(toCMYK);

        JPanel cmykPanel = new JPanel();
        cmykPanel.setBorder(BorderFactory.createTitledBorder("CMYK Input"));
        cField = new JTextField("0.0", 4);
        mField = new JTextField("0.0", 4);
        yField = new JTextField("0.0", 4);
        kField = new JTextField("0.0", 4);
        cmykPanel.add(new JLabel("C:"));
        cmykPanel.add(cField);
        cmykPanel.add(new JLabel("M:"));
        cmykPanel.add(mField);
        cmykPanel.add(new JLabel("Y:"));
        cmykPanel.add(yField);
        cmykPanel.add(new JLabel("K:"));
        cmykPanel.add(kField);
        JButton toRGB = new JButton("CMYK → RGB");
        cmykPanel.add(toRGB);

        JPanel previewPanel = new JPanel(new GridLayout(1, 2));
        previewPanel.setBorder(BorderFactory.createTitledBorder("Color View"));

        JPanel rgbPreviewContainer = new JPanel(new BorderLayout());
        rgbPreviewContainer.setBorder(BorderFactory.createTitledBorder("RGB → CMYK Output"));
        rgbColorPreview = new JPanel();
        rgbColorPreview.setPreferredSize(new Dimension(100, 40));
        rgbColorPreview.setBackground(Color.WHITE);
        rgbPreviewContainer.add(rgbColorPreview, BorderLayout.CENTER);

        JPanel cmykPreviewContainer = new JPanel(new BorderLayout());
        cmykPreviewContainer.setBorder(BorderFactory.createTitledBorder("CMYK → RGB Output"));
        cmykColorPreview = new JPanel();
        cmykColorPreview.setPreferredSize(new Dimension(100, 40));
        cmykColorPreview.setBackground(Color.WHITE);
        cmykPreviewContainer.add(cmykColorPreview, BorderLayout.CENTER);

        previewPanel.add(rgbPreviewContainer);
        previewPanel.add(cmykPreviewContainer);

        inputPanel.add(rgbPanel);
        inputPanel.add(cmykPanel);
        inputPanel.add(previewPanel);

        add(inputPanel, BorderLayout.NORTH);

        JPanel drawPanel = new JPanel(new GridLayout(1, 2));

        RGBCube3DPanel rgbCubePanel = new RGBCube3DPanel();
        rgbCubePanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        JPanel rgbContainer = new JPanel(new BorderLayout());
        rgbContainer.setBorder(BorderFactory.createTitledBorder("3D RGB Cube"));
        rgbContainer.add(rgbCubePanel, BorderLayout.CENTER);

        HSVCone3DPanel hsvConePanel = new HSVCone3DPanel();
        hsvConePanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        JPanel hsvContainer = new JPanel(new BorderLayout());
        hsvContainer.setBorder(BorderFactory.createTitledBorder("3D HSV Cone"));
        hsvContainer.add(hsvConePanel, BorderLayout.CENTER);

        drawPanel.add(rgbContainer);
        drawPanel.add(hsvContainer);

        add(drawPanel, BorderLayout.CENTER);

        toCMYK.addActionListener(e -> convertRGBtoCMYK());
        toRGB.addActionListener(e -> convertCMYKtoRGB());
    }

    private void convertRGBtoCMYK() {
        try {
            int r = Math.min(255, Math.max(0, Integer.parseInt(rField.getText())));
            int g = Math.min(255, Math.max(0, Integer.parseInt(gField.getText())));
            int b = Math.min(255, Math.max(0, Integer.parseInt(bField.getText())));

            rField.setText(String.valueOf(r));
            gField.setText(String.valueOf(g));
            bField.setText(String.valueOf(b));

            float rf = r / 255f;
            float gf = g / 255f;
            float bf = b / 255f;

            float black = 1 - Math.max(Math.max(rf, gf), bf);

            float epsilon = 0.00001f;

            float cyan, magenta, yellow;
            if (Math.abs(black - 1.0f) < epsilon) {
                cyan = magenta = yellow = 0;
            } else {
                cyan = (1 - rf - black) / (1 - black);
                magenta = (1 - gf - black) / (1 - black);
                yellow = (1 - bf - black) / (1 - black);
            }

            cyan = Math.min(1.0f, Math.max(0.0f, cyan));
            magenta = Math.min(1.0f, Math.max(0.0f, magenta));
            yellow = Math.min(1.0f, Math.max(0.0f, yellow));
            black = Math.min(1.0f, Math.max(0.0f, black));

            cField.setText(String.format("%.2f", cyan));
            mField.setText(String.format("%.2f", magenta));
            yField.setText(String.format("%.2f", yellow));
            kField.setText(String.format("%.2f", black));

            rgbColorPreview.setBackground(new Color(r, g, b));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter the RGB values correctly (0-255)");
        }
    }

    private void convertCMYKtoRGB() {
        try {
            float c = validateCMYKValue(cField.getText());
            float m = validateCMYKValue(mField.getText());
            float y = validateCMYKValue(yField.getText());
            float k = validateCMYKValue(kField.getText());

            cField.setText(String.format("%.2f", c));
            mField.setText(String.format("%.2f", m));
            yField.setText(String.format("%.2f", y));
            kField.setText(String.format("%.2f", k));

            int r = (int) (255 * (1 - Math.min(1, c * (1 - k) + k)));
            int g = (int) (255 * (1 - Math.min(1, m * (1 - k) + k)));
            int b = (int) (255 * (1 - Math.min(1, y * (1 - k) + k)));

            r = Math.min(255, Math.max(0, r));
            g = Math.min(255, Math.max(0, g));
            b = Math.min(255, Math.max(0, b));

            rField.setText(String.valueOf(r));
            gField.setText(String.valueOf(g));
            bField.setText(String.valueOf(b));

            cmykColorPreview.setBackground(new Color(r, g, b));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter the correct CMYK values (between 0.0 - 1.0)");
        }
    }

    private float validateCMYKValue(String value) {
        try {
            value = value.replace(',', '.');
            float val = Float.parseFloat(value);
            return Math.min(1.0f, Math.max(0.0f, val));
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    class RGBCube3DPanel extends JPanel {
        private double rotationX = 0.3;
        private double rotationY = 0.5;
        private Point lastMousePos;
        private boolean isDragging = false;

        public RGBCube3DPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lastMousePos = e.getPoint();
                    isDragging = true;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isDragging = false;
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (isDragging && lastMousePos != null) {
                        int dx = e.getX() - lastMousePos.x;
                        int dy = e.getY() - lastMousePos.y;

                        rotationY += dx * 0.01;
                        rotationX += dy * 0.01;

                        lastMousePos = e.getPoint();
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            int size = Math.min(width, height) / 4;

            drawRGBCube(g2, centerX, centerY, size);
        }

        private void drawRGBCube(Graphics2D g2, int centerX, int centerY, int size) {
            Point3D[] vertices = {
                    new Point3D(-size, -size, -size),
                    new Point3D(size, -size, -size),
                    new Point3D(size, size, -size),
                    new Point3D(-size, size, -size),
                    new Point3D(-size, -size, size),
                    new Point3D(size, -size, size),
                    new Point3D(size, size, size),
                    new Point3D(-size, size, size)
            };

            Point[] projectedVertices = new Point[8];
            for (int i = 0; i < 8; i++) {
                Point3D rotated = rotatePoint(vertices[i], rotationX, rotationY);
                projectedVertices[i] = project3Dto2D(rotated, centerX, centerY, 500);
            }

            int[][] faces = {
                    {0, 1, 2, 3},
                    {4, 7, 6, 5},
                    {0, 4, 5, 1},
                    {2, 6, 7, 3},
                    {0, 3, 7, 4},
                    {1, 5, 6, 2}
            };

            Color[] vertexColors = {
                    new Color(0, 0, 0),
                    new Color(255, 0, 0),
                    new Color(255, 255, 0),
                    new Color(0, 255, 0),
                    new Color(0, 0, 255),
                    new Color(255, 0, 255),
                    new Color(255, 255, 255),
                    new Color(0, 255, 255)
            };

            for (int[] face : faces) {
                Polygon poly = new Polygon();
                for (int vertex : face) {
                    poly.addPoint(projectedVertices[vertex].x, projectedVertices[vertex].y);
                }

                int avgR = 0, avgG = 0, avgB = 0;
                for (int vertex : face) {
                    Color c = vertexColors[vertex];
                    avgR += c.getRed();
                    avgG += c.getGreen();
                    avgB += c.getBlue();
                }
                avgR /= 4; avgG /= 4; avgB /= 4;

                Color faceColor = new Color(avgR, avgG, avgB, 120);
                g2.setColor(faceColor);
                g2.fill(poly);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
                g2.draw(poly);
            }

            for (int i = 0; i < 8; i++) {
                g2.setColor(vertexColors[i]);
                int x = projectedVertices[i].x - 4;
                int y = projectedVertices[i].y - 4;
                g2.fillOval(x, y, 8, 8);
                g2.setColor(Color.BLACK);
                g2.drawOval(x, y, 8, 8);
            }

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Drag to rotate", 10, 20);
        }

        private Point3D rotatePoint(Point3D point, double angleX, double angleY) {
            double cosX = Math.cos(angleX);
            double sinX = Math.sin(angleX);
            double y1 = point.y * cosX - point.z * sinX;
            double z1 = point.y * sinX + point.z * cosX;

            double cosY = Math.cos(angleY);
            double sinY = Math.sin(angleY);
            double x2 = point.x * cosY + z1 * sinY;
            double z2 = -point.x * sinY + z1 * cosY;

            return new Point3D(x2, y1, z2);
        }

        private Point project3Dto2D(Point3D point, int centerX, int centerY, double distance) {
            double scale = distance / (distance + point.z);
            int x = (int) (centerX + point.x * scale);
            int y = (int) (centerY + point.y * scale);
            return new Point(x, y);
        }
    }

    class HSVCone3DPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            int radius = Math.min(width, height) / 4;
            int coneHeight = radius * 2;

            drawHSVCone(g2, centerX, centerY, radius, coneHeight);
        }

        private void drawHSVCone(Graphics2D g2, int centerX, int centerY, int radius, int coneHeight) {
            double angleX = 0.3;
            double angleY = 0.5;

            int baseY = centerY + coneHeight / 3;

            int segments = 36;
            for (int i = 0; i < segments; i++) {
                double angle1 = (i * 2 * Math.PI) / segments;
                double angle2 = ((i + 1) * 2 * Math.PI) / segments;

                float hue = i / (float) segments;
                Color segmentColor = Color.getHSBColor(hue, 1.0f, 1.0f);

                int x1 = (int) (centerX + radius * Math.cos(angle1));
                int y1 = (int) (baseY + radius * Math.sin(angle1) * 0.3);
                int x2 = (int) (centerX + radius * Math.cos(angle2));
                int y2 = (int) (baseY + radius * Math.sin(angle2) * 0.3);

                Polygon segment = new Polygon();
                segment.addPoint(centerX, baseY);
                segment.addPoint(x1, y1);
                segment.addPoint(x2, y2);

                g2.setColor(segmentColor);
                g2.fill(segment);
                g2.setColor(Color.BLACK);
                g2.draw(segment);
            }

            int valueSteps = 10;
            for (int v = 0; v < valueSteps; v++) {
                float value = 1.0f - (v / (float) valueSteps);
                int currentRadius = (int) (radius * value);
                int currentY = baseY - (int) ((coneHeight * value) * 0.8);

                int satSteps = 5;
                for (int s = 0; s < satSteps; s++) {
                    float saturation = 1.0f - (s / (float) satSteps);
                    int ringRadius = (int) (currentRadius * saturation);

                    for (int i = 0; i < segments / 2; i++) {
                        double angle1 = (i * 2 * Math.PI) / (segments / 2);
                        double angle2 = ((i + 1) * 2 * Math.PI) / (segments / 2);

                        float hue = i / (float) (segments / 2);
                        Color ringColor = Color.getHSBColor(hue, saturation, value);

                        int x1 = (int) (centerX + ringRadius * Math.cos(angle1));
                        int y1 = (int) (currentY + ringRadius * Math.sin(angle1) * 0.3);
                        int x2 = (int) (centerX + ringRadius * Math.cos(angle2));
                        int y2 = (int) (currentY + ringRadius * Math.sin(angle2) * 0.3);

                        g2.setColor(ringColor);
                        g2.setStroke(new BasicStroke(2));
                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("HSV Color Space", 10, 20);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.drawString("H: Hue (around)", 10, 35);
            g2.drawString("S: Saturation (radius)", 10, 48);
            g2.drawString("V: Value (height)", 10, 61);
        }
    }

    class Point3D {
        double x, y, z;

        public Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Color_Models().setVisible(true));
    }
}
