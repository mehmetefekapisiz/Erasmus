import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Bezier_Curve extends JFrame {
    public Bezier_Curve() {
        setTitle("Bezier Curve Drawer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        BezierPanel panel = new BezierPanel();
        add(panel, BorderLayout.CENTER);

        JButton clearBtn = new JButton("Clean");
        clearBtn.addActionListener(e -> panel.clearPoints());

        JButton increaseDegreeBtn = new JButton("Increase Degree");
        increaseDegreeBtn.addActionListener(e -> panel.increaseDegree());

        JButton decreaseDegreeBtn = new JButton("Decrease Degree");
        decreaseDegreeBtn.addActionListener(e -> panel.decreaseDegree());

        JButton deletePointBtn = new JButton("Delete Selected Point");
        deletePointBtn.addActionListener(e -> panel.deleteSelectedPoint());

        JCheckBox showPolygonBox = new JCheckBox("Show Control Polygon", true);
        showPolygonBox.addActionListener(e -> {
            panel.setShowControlPolygon(showPolygonBox.isSelected());
            panel.repaint();
        });

        JPanel bottom = new JPanel();
        bottom.add(clearBtn);
        bottom.add(increaseDegreeBtn);
        bottom.add(decreaseDegreeBtn);
        bottom.add(deletePointBtn);
        bottom.add(showPolygonBox);
        add(bottom, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bezier_Curve().setVisible(true));
    }
}

class BezierPanel extends JPanel {
    private ArrayList<Point> controlPoints = new ArrayList<>();
    private Point draggingPoint = null;
    private Point selectedPoint = null;
    private boolean showControlPolygon = true;

    public BezierPanel() {
        setBackground(Color.white);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                selectedPoint = null;
                for (Point p : controlPoints) {
                    if (p.distance(e.getPoint()) < 10) {
                        draggingPoint = p;
                        selectedPoint = p;
                        return;
                    }
                }
                controlPoints.add(e.getPoint());
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                draggingPoint = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (draggingPoint != null) {
                    draggingPoint.setLocation(e.getPoint());
                    repaint();
                }
            }
        });
    }

    public void clearPoints() {
        controlPoints.clear();
        selectedPoint = null;
        repaint();
    }

    public void deleteSelectedPoint() {
        if (selectedPoint != null) {
            controlPoints.remove(selectedPoint);
            selectedPoint = null;
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a point to delete first.",
                    "No Point Selected", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setShowControlPolygon(boolean show) {
        showControlPolygon = show;
    }

    public void increaseDegree() {
        if (controlPoints.size() < 2) {
            JOptionPane.showMessageDialog(this, "At least two control points are needed to increase degree.",
                    "Insufficient Control Points", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Point> newControlPoints = new ArrayList<>();
        int n = controlPoints.size() - 1;

        newControlPoints.add(new Point(controlPoints.get(0)));

        for (int i = 0; i < n; i++) {
            int x = (int) ((n - i) * controlPoints.get(i).x + (i + 1) * controlPoints.get(i + 1).x) / (n + 1);
            int y = (int) ((n - i) * controlPoints.get(i).y + (i + 1) * controlPoints.get(i + 1).y) / (n + 1);
            newControlPoints.add(new Point(x, y));
        }

        newControlPoints.add(new Point(controlPoints.get(n)));

        controlPoints = newControlPoints;
        repaint();
    }

    public void decreaseDegree() {
        if (controlPoints.size() <= 2) {
            JOptionPane.showMessageDialog(this, "Cannot decrease degree below linear curve.",
                    "Minimum Degree Reached", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Point> newControlPoints = new ArrayList<>();
        int n = controlPoints.size() - 1;

        newControlPoints.add(new Point(controlPoints.get(0)));

        for (int i = 1; i < n; i++) {
            double alpha = (double) i / n;

            int x = (int) ((controlPoints.get(i).x - (1 - alpha) * controlPoints.get(0).x - alpha * controlPoints.get(n).x)
                    / (alpha * (1 - alpha) * n * (n - 1)) * (n - 1) * (n - 1)
                    + (1 - alpha) * newControlPoints.get(0).x + alpha * controlPoints.get(n).x);

            int y = (int) ((controlPoints.get(i).y - (1 - alpha) * controlPoints.get(0).y - alpha * controlPoints.get(n).y)
                    / (alpha * (1 - alpha) * n * (n - 1)) * (n - 1) * (n - 1)
                    + (1 - alpha) * newControlPoints.get(0).y + alpha * controlPoints.get(n).y);

            newControlPoints.add(new Point(x, y));
        }

        newControlPoints.add(new Point(controlPoints.get(n)));

        controlPoints = newControlPoints;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (controlPoints.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Point p : controlPoints) {
            if (p == selectedPoint) {
                g2.setColor(Color.green);
                g2.fillOval(p.x - 6, p.y - 6, 12, 12);
            } else {
                g2.setColor(Color.red);
                g2.fillOval(p.x - 5, p.y - 5, 10, 10);
            }
        }

        if (showControlPolygon && controlPoints.size() > 1) {
            g2.setColor(Color.lightGray);
            for (int i = 0; i < controlPoints.size() - 1; i++) {
                g2.drawLine(controlPoints.get(i).x, controlPoints.get(i).y,
                        controlPoints.get(i + 1).x, controlPoints.get(i + 1).y);
            }
        }

        if (controlPoints.size() >= 2) {
            g2.setColor(Color.blue);
            Point2D.Double prev = calculateBezierPoint(0);
            for (double t = 0.01; t <= 1.0; t += 0.01) {
                Point2D.Double curr = calculateBezierPoint(t);
                g2.draw(new Line2D.Double(prev, curr));
                prev = curr;
            }
        }

        g2.setColor(Color.black);
        g2.drawString("Curve Degree: " + (controlPoints.size() - 1), 10, 20);
    }

    private Point2D.Double calculateBezierPoint(double t) {
        int n = controlPoints.size() - 1;
        double x = 0, y = 0;

        for (int i = 0; i <= n; i++) {
            double binCoeff = binomialCoefficient(n, i);
            double term = binCoeff * Math.pow(1 - t, n - i) * Math.pow(t, i);
            x += term * controlPoints.get(i).x;
            y += term * controlPoints.get(i).y;
        }

        return new Point2D.Double(x, y);
    }

    private long binomialCoefficient(int n, int k) {
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result *= n--;
            result /= i;
        }
        return result;
    }
}