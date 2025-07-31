import javax.swing.*;
import java.awt.*;

public class Transformations2D extends JFrame {
    public Transformations2D() {
        setTitle("2D Transformations");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TransformPanel panel = new TransformPanel();
        add(panel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(7, 1));

        JPanel shapePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel shapeLabel = new JLabel("Shape:");
        JButton triangleBtn = new JButton("Triangle");
        JButton rectangleBtn = new JButton("Rectangle");

        triangleBtn.addActionListener(e -> panel.setShapeType(TransformPanel.TRIANGLE));
        rectangleBtn.addActionListener(e -> panel.setShapeType(TransformPanel.RECTANGLE));

        shapePanel.add(shapeLabel);
        shapePanel.add(triangleBtn);
        shapePanel.add(rectangleBtn);

        JPanel rotatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel rotateLabel = new JLabel("Rotation:");
        JTextField rotateField = new JTextField("45", 5);
        JButton rotateBtn = new JButton("Rotate");

        rotateBtn.addActionListener(e -> {
            try {
                double degrees = Double.parseDouble(rotateField.getText());
                panel.rotate(Math.toRadians(degrees));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        rotatePanel.add(rotateLabel);
        rotatePanel.add(rotateField);
        rotatePanel.add(rotateBtn);

        JPanel scalePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel scaleLabel = new JLabel("Scale:");
        JTextField scaleXField = new JTextField("2", 3);
        JTextField scaleYField = new JTextField("2", 3);
        JButton scaleBtn = new JButton("Scale");

        scaleBtn.addActionListener(e -> {
            try {
                double sx = Double.parseDouble(scaleXField.getText());
                double sy = Double.parseDouble(scaleYField.getText());
                panel.scale(sx, sy);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        scalePanel.add(scaleLabel);
        scalePanel.add(new JLabel("X:"));
        scalePanel.add(scaleXField);
        scalePanel.add(new JLabel("Y:"));
        scalePanel.add(scaleYField);
        scalePanel.add(scaleBtn);

        JPanel shearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel shearLabel = new JLabel("Shear:");
        JTextField shearXField = new JTextField("0.5", 3);
        JTextField shearYField = new JTextField("0", 3);
        JButton shearBtn = new JButton("Apply Shear");

        shearBtn.addActionListener(e -> {
            try {
                double shx = Double.parseDouble(shearXField.getText());
                double shy = Double.parseDouble(shearYField.getText());
                panel.shear(shx, shy);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        shearPanel.add(shearLabel);
        shearPanel.add(new JLabel("X:"));
        shearPanel.add(shearXField);
        shearPanel.add(new JLabel("Y:"));
        shearPanel.add(shearYField);
        shearPanel.add(shearBtn);

        JPanel reflectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton reflectXBtn = new JButton("Reflect X");
        JButton reflectYBtn = new JButton("Reflect Y");

        reflectXBtn.addActionListener(e -> panel.reflect(true));
        reflectYBtn.addActionListener(e -> panel.reflect(false));

        reflectPanel.add(new JLabel("Reflect:"));
        reflectPanel.add(reflectXBtn);
        reflectPanel.add(reflectYBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> panel.resetShape());

        controlPanel.add(shapePanel);
        controlPanel.add(rotatePanel);
        controlPanel.add(scalePanel);
        controlPanel.add(shearPanel);
        controlPanel.add(reflectPanel);
        controlPanel.add(resetBtn);

        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Transformations2D().setVisible(true));
    }
}

class TransformPanel extends JPanel {
    public static final int TRIANGLE = 0;
    public static final int RECTANGLE = 1;

    private double[][] originalPoints;
    private double[][] transformMatrix;
    private int currentShapeType = TRIANGLE;

    public TransformPanel() {
        setBackground(Color.white);
        resetShape();
    }

    public void setShapeType(int shapeType) {
        currentShapeType = shapeType;
        resetShape();
    }

    public void resetShape() {
        if (currentShapeType == TRIANGLE) {
            originalPoints = new double[][] {
                    {0, 50},
                    {50, 50},
                    {25, 0}
            };
        } else {
            originalPoints = new double[][] {
                    {-40, -30},
                    {40, -30},
                    {40, 30},
                    {-40, 30}
            };
        }

        transformMatrix = new double[][] {
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };

        repaint();
    }

    private double[][] multiplyMatrices(double[][] a, double[][] b) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    private double[] transformPoint(double x, double y) {
        double[] result = new double[2];
        result[0] = transformMatrix[0][0] * x + transformMatrix[0][1] * y + transformMatrix[0][2];
        result[1] = transformMatrix[1][0] * x + transformMatrix[1][1] * y + transformMatrix[1][2];
        return result;
    }

    public void rotate(double theta) {
        double[][] rotationMatrix = new double[][] {
                {Math.cos(theta), -Math.sin(theta), 0},
                {Math.sin(theta), Math.cos(theta), 0},
                {0, 0, 1}
        };

        transformMatrix = multiplyMatrices(transformMatrix, rotationMatrix);
        repaint();
    }

    public void scale(double sx, double sy) {
        double[][] scaleMatrix = new double[][] {
                {sx, 0, 0},
                {0, sy, 0},
                {0, 0, 1}
        };

        transformMatrix = multiplyMatrices(transformMatrix, scaleMatrix);
        repaint();
    }

    public void shear(double shx, double shy) {
        double[][] shearMatrix = new double[][] {
                {1, shy, 0},
                {shx, 1, 0},
                {0, 0, 1}
        };

        transformMatrix = multiplyMatrices(transformMatrix, shearMatrix);
        repaint();
    }

    public void reflect(boolean xAxis) {
        double[][] reflectMatrix;
        if (xAxis) {
            reflectMatrix = new double[][] {
                    {1, 0, 0},
                    {0, -1, 0},
                    {0, 0, 1}
            };
        } else {
            reflectMatrix = new double[][] {
                    {-1, 0, 0},
                    {0, 1, 0},
                    {0, 0, 1}
            };
        }

        transformMatrix = multiplyMatrices(transformMatrix, reflectMatrix);
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.lightGray);
        g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

        g2.setColor(Color.black);
        g2.drawString(String.format("Transformation Matrix:"), 10, 20);
        g2.drawString(String.format("[%.2f, %.2f, %.2f]",
                transformMatrix[0][0], transformMatrix[0][1], transformMatrix[0][2]), 10, 35);
        g2.drawString(String.format("[%.2f, %.2f, %.2f]",
                transformMatrix[1][0], transformMatrix[1][1], transformMatrix[1][2]), 10, 50);
        g2.drawString(String.format("[%.2f, %.2f, %.2f]",
                transformMatrix[2][0], transformMatrix[2][1], transformMatrix[2][2]), 10, 65);

        String shapeInfo = currentShapeType == TRIANGLE ? "Shape: Triangle" : "Shape: Rectangle";
        g2.drawString(shapeInfo, 10, 85);

        int[] xPoints = new int[originalPoints.length];
        int[] yPoints = new int[originalPoints.length];

        for (int i = 0; i < originalPoints.length; i++) {
            double[] transformed = transformPoint(originalPoints[i][0], originalPoints[i][1]);
            xPoints[i] = (int) (transformed[0] + getWidth() / 2);
            yPoints[i] = (int) (transformed[1] + getHeight() / 2);
        }

        g2.setColor(Color.blue);
        g2.fillPolygon(xPoints, yPoints, originalPoints.length);

        g2.setColor(Color.DARK_GRAY);
        g2.drawPolygon(xPoints, yPoints, originalPoints.length);
    }
}
