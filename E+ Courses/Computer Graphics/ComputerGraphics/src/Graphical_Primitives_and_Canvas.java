import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Graphical_Primitives_and_Canvas extends JFrame {
    private JTextField lineX1, lineY1, lineX2, lineY2;
    private JTextField rectX, rectY, rectW, rectH;
    private JTextField circleX, circleY, circleR;
    private DrawPanel drawPanel;

    private String activeShape = "";
    private Point firstClick = null;

    public Graphical_Primitives_and_Canvas() {
        setTitle("Graphics Editor");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawPanel = new DrawPanel();
        JPanel controlPanel = new JPanel(new GridLayout(4, 1));

        JPanel linePanel = new JPanel();
        linePanel.add(new JLabel("Line (x1, y1, x2, y2):"));
        lineX1 = new JTextField("50", 3);
        lineY1 = new JTextField("50", 3);
        lineX2 = new JTextField("150", 3);
        lineY2 = new JTextField("150", 3);
        linePanel.add(lineX1);
        linePanel.add(lineY1);
        linePanel.add(lineX2);
        linePanel.add(lineY2);
        JButton drawLine = new JButton("Draw Line");
        linePanel.add(drawLine);

        drawLine.addActionListener(e -> {
            drawPanel.setLine(
                    Integer.parseInt(lineX1.getText()),
                    Integer.parseInt(lineY1.getText()),
                    Integer.parseInt(lineX2.getText()),
                    Integer.parseInt(lineY2.getText())
            );
            activeShape = "line";
            firstClick = null;
        });

        JPanel rectPanel = new JPanel();
        rectPanel.add(new JLabel("Rectangle (x, y, w, h):"));
        rectX = new JTextField("200", 3);
        rectY = new JTextField("50", 3);
        rectW = new JTextField("100", 3);
        rectH = new JTextField("80", 3);
        rectPanel.add(rectX);
        rectPanel.add(rectY);
        rectPanel.add(rectW);
        rectPanel.add(rectH);
        JButton drawRect = new JButton("Draw Rect");
        rectPanel.add(drawRect);

        drawRect.addActionListener(e -> {
            drawPanel.setRect(
                    Integer.parseInt(rectX.getText()),
                    Integer.parseInt(rectY.getText()),
                    Integer.parseInt(rectW.getText()),
                    Integer.parseInt(rectH.getText())
            );
            activeShape = "rect";
        });

        JPanel circlePanel = new JPanel();
        circlePanel.add(new JLabel("Circle (x, y, r):"));
        circleX = new JTextField("350", 3);
        circleY = new JTextField("200", 3);
        circleR = new JTextField("40", 3);
        circlePanel.add(circleX);
        circlePanel.add(circleY);
        circlePanel.add(circleR);
        JButton drawCircle = new JButton("Draw Circle");
        circlePanel.add(drawCircle);

        drawCircle.addActionListener(e -> {
            drawPanel.setCircle(
                    Integer.parseInt(circleX.getText()),
                    Integer.parseInt(circleY.getText()),
                    Integer.parseInt(circleR.getText())
            );
            activeShape = "circle";
        });

        controlPanel.add(linePanel);
        controlPanel.add(rectPanel);
        controlPanel.add(circlePanel);

        add(controlPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                switch (activeShape) {
                    case "rect":
                        int rw = Integer.parseInt(rectW.getText());
                        int rh = Integer.parseInt(rectH.getText());
                        drawPanel.setRect(x, y, rw, rh);
                        break;
                    case "circle":
                        int r = Integer.parseInt(circleR.getText());
                        drawPanel.setCircle(x, y, r);
                        break;
                    case "line":
                        if (firstClick == null) {
                            firstClick = new Point(x, y);
                        } else {
                            drawPanel.setLine(firstClick.x, firstClick.y, x, y);
                            firstClick = null;
                        }
                        break;
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Graphical_Primitives_and_Canvas().setVisible(true));
    }

    class DrawPanel extends JPanel {
        private Line2D line = null;
        private Rectangle2D rect = null;
        private Ellipse2D circle = null;

        public void setLine(int x1, int y1, int x2, int y2) {
            line = new Line2D.Float(x1, y1, x2, y2);
            repaint();
        }

        public void setRect(int x, int y, int w, int h) {
            rect = new Rectangle2D.Float(x, y, w, h);
            repaint();
        }

        public void setCircle(int x, int y, int r) {
            circle = new Ellipse2D.Float(x - r, y - r, 2 * r, 2 * r);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLUE);
            if (line != null) g2.draw(line);
            if (rect != null) g2.draw(rect);
            if (circle != null) g2.draw(circle);
        }
    }
}