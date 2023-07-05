package org.colin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

class Main extends JFrame {
    private List<Gate> gates;
    private List<Wire> wires;

    private Gate selectedGate;
    private Wire currentWire;

    public Main() {
        gates = new ArrayList<>();
        wires = new ArrayList<>();
        selectedGate = null;
        currentWire = null;

        setTitle("Digital Logic Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Gate andGate = new Gate(GateType.AND, 50, 50);
        Gate orGate = new Gate(GateType.OR, 200, 50);

        gates.add(andGate);
        gates.add(orGate);

        for (Gate gate : gates) {
            add(gate);
        }

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                boolean clickedOnNode = false;
                for (Gate gate : gates) {
                    // Check if the click is on any input point
                    for (Point inputPoint : gate.getInputPoints()) {
                        if (inputPoint.equals(e.getPoint())) {
                            clickedOnNode = true;
                            break;
                        }
                    }
                    // Check if the click is on the output point
                    if (gate.getOutputPoint().equals(e.getPoint())) {
                        clickedOnNode = true;
                    }
                    if (clickedOnNode) {
                        break;
                    }
                }

                if (!clickedOnNode) {
                    for (Gate gate : gates) {
                        if (gate.getBounds().contains(e.getPoint())) {
                            selectedGate = gate;
                            break;
                        }
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                selectedGate = null;
                currentWire = null;
            }
        });





        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selectedGate != null) {
                    selectedGate.setLocation(e.getX() - selectedGate.getWidth() / 2, e.getY() - selectedGate.getHeight() / 2);
                    repaint();
                }

                if (currentWire != null) {
                    currentWire.setEndPoint(e.getPoint());
                    repaint();
                }
            }
        });

        setSize(400, 200);
        setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        for (Wire wire : wires) {
            g2d.draw(wire.getLine());
        }

        for (Gate gate : gates) {
            for (Wire wire : gate.getInputWires()) {
                g2d.draw(wire.getLine());
            }
        }
    }


    enum GateType {
        AND, OR
    }

    class Gate extends JComponent {
        private static final int WIDTH = 50;
        private static final int HEIGHT = 50;
        private static final int OUTPUT_X = WIDTH;
        private static final int OUTPUT_Y = HEIGHT / 2;
        private List<Wire> inputWires;

        private GateType type;

        public Gate(GateType type, int x, int y) {
            this.type = type;
            setLocation(x, y);
            setSize(WIDTH, HEIGHT);
            inputWires = new ArrayList<>();

        }
        public List<Wire> getInputWires() {
            return inputWires;
        }
        public List<Point> getInputPoints() {
            List<Point> inputPoints = new ArrayList<>();
            // Calculate and add the input points based on the gate type and dimensions
            if (type == GateType.AND) {
                inputPoints.add(new Point(0, HEIGHT / 4));
                inputPoints.add(new Point(0, HEIGHT * 3 / 4));
            } else if (type == GateType.OR) {
                inputPoints.add(new Point(0, HEIGHT / 4));
                inputPoints.add(new Point(0, HEIGHT * 3 / 4));
                inputPoints.add(new Point(WIDTH / 2, HEIGHT / 2));
            }
            return inputPoints;
        }

        public Point getOutputPoint() {
            Point location = getLocation();
            return new Point(location.x + OUTPUT_X, location.y + OUTPUT_Y);
        }

        public void connectInputWire(Wire wire) {
            inputWires.add(wire);
        }

        public boolean hasValidInput() {
            if (type == GateType.AND) {
                for (Wire wire : inputWires) {
                    if (!wire.hasValidSignal()) {
                        return false;
                    }
                }
                return true;
            } else if (type == GateType.OR) {
                for (Wire wire : inputWires) {
                    if (wire.hasValidSignal()) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);

            // Draw input dots
            for (Point inputPoint : getInputPoints()) {
                int dotSize = 6;
                int dotX = inputPoint.x - dotSize / 2;
                int dotY = inputPoint.y - dotSize / 2;
                g2d.fillOval(dotX, dotY, dotSize, dotSize);
            }

            for (Wire wire : inputWires) {
                g2d.draw(wire.getLine());
            }

            // Draw output dot
            Point outputPoint = getOutputPoint();
            int dotSize = 6;
            int dotX = outputPoint.x - dotSize / 2;
            int dotY = outputPoint.y - dotSize / 2;
            g2d.fillOval(dotX, dotY, dotSize, dotSize);

            if (type == GateType.AND) {
                g2d.drawLine(0, HEIGHT / 4, 0, HEIGHT * 3 / 4);
                g2d.drawLine(0, HEIGHT / 4, WIDTH / 2, HEIGHT / 4);
                g2d.drawLine(0, HEIGHT * 3 / 4, WIDTH / 2, HEIGHT * 3 / 4);
                g2d.drawLine(WIDTH / 2, HEIGHT / 4, WIDTH, HEIGHT / 2);
                g2d.drawLine(WIDTH / 2, HEIGHT * 3 / 4, WIDTH, HEIGHT / 2);
                g2d.drawArc(WIDTH / 2 - HEIGHT / 4, HEIGHT / 4, HEIGHT / 2, HEIGHT / 2, -90, 180);
            } else if (type == GateType.OR) {
                g2d.drawLine(0, HEIGHT / 4, 0, HEIGHT * 3 / 4);
                g2d.drawLine(0, HEIGHT / 4, WIDTH / 2, HEIGHT / 4);
                g2d.drawLine(0, HEIGHT * 3 / 4, WIDTH / 2, HEIGHT * 3 / 4);
                g2d.drawArc(WIDTH / 2 - HEIGHT / 4, HEIGHT / 4, HEIGHT / 2, HEIGHT / 2, -90, 180);
                g2d.drawLine(WIDTH / 2, HEIGHT / 4, WIDTH, HEIGHT / 2);
                g2d.drawLine(WIDTH / 2, HEIGHT * 3 / 4, WIDTH, HEIGHT / 2);
            }
        }
    }

    class Wire {
        private Point startPoint;
        private Point endPoint;

        public Wire(Point startPoint, Point endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
        }

        public Line2D getLine() {
            return new Line2D.Double(startPoint, endPoint);
        }

        public boolean hasValidSignal() {
            // Simulate wire connection and check if the start point gate has a valid input
            if (startPoint != null) {
                Gate startGate = getConnectedGate(startPoint);
                if (startGate != null) {
                    return startGate.hasValidInput();
                }
            }
            return false;
        }

        private Gate getConnectedGate(Point point) {
            for (Gate gate : gates) {
                if (gate.getBounds().contains(point)) {
                    return gate;
                }
            }
            return null;
        }

        public void setEndPoint(Point endPoint) {
            this.endPoint = endPoint;
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
