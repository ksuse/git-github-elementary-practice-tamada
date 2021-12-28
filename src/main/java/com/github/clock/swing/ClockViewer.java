package com.github.clock.swing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.github.clock.Args;
import com.github.clock.Clock;
import com.github.clock.DebugPrinter;
import com.github.clock.Observers;

public class ClockViewer extends JComponent{
    private static final long serialVersionUID = 851223340594030326L;

    private Clock clock;
    private Args args;
    private Optional<Image> background;
    private DebugPrinter printer;
    private Hand[] hands;

    public ClockViewer(Clock clock, Args args, Observers observers, DebugPrinter printer){
        this.args = args;
        observers.add(c -> repaint());
        this.clock = clock;
        clock.start();
        this.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                background = Optional.empty();
            }
        });
        this.printer = printer;
        initializeHands();
    }

    private void initializeHands() {
        hands = new Hand[] {
            new Hand(1, Color.BLACK, 0.8), // 秒針
            new Hand(2, Color.decode(args.getLongHandColor()), 0.7), // 長針
            new Hand(4, Color.BLUE, 0.6)   // 短針
        };
    }

    public void showClock(){
        JFrame frame = new JFrame();
        frame.add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                clock.stop();
            }
        });
        frame.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(300, 300);
    }

    @Override
    protected void paintComponent(Graphics g){
        g.drawImage(background.orElseGet(() -> createBackgroundImage()), 0, 0, null);
        drawHands((Graphics2D)g);
    }

    private double getLength(){
        Dimension size = getSize();
        return Math.min(size.getHeight(), size.getWidth()) / 2;
    }

    private int[] calculateLocations(Clock clock) {
        int hour = clock.getHour();
        int minute = clock.getMinute();
        int second = clock.getSecond();
        // 長針の位置は，分の位置で表そうとすると，時間×5．
        // 24時間制のため，12で割った余りを時間とする．
        int hourPosition = (hour % 12) * 5 + minute / 12;

        printer.println(() -> 
                String.format("%02d:%02d:%02d -> (%02d, %02d, %02d)",
                    hour, minute, second, hourPosition, minute, second));
        return new int[] { clock.getSecond(), clock.getMinute(), hourPosition };
    }

    private void drawHands(Graphics2D g2){
        Dimension size = getSize();
        Point2D center = new Point2D.Double(size.getWidth() / 2.0, size.getHeight() / 2);
        g2.translate(center.getX(), center.getY());
        double length = getLength();
        int[] locations = calculateLocations(clock);
        IntStream.range(0, 3)
            .forEach(index -> hands[index].draw(g2, locations[index], length));
    }

    private class Hand {
        private int stroke;
        private Color color;
        private double magnification;

        public Hand(int stroke, Color color, double lengthMagnification) {
            this.stroke = stroke;
            this.color = color;
            this.magnification = lengthMagnification;
        }

        public void draw(Graphics2D g, int position, double length) {
            g.setStroke(new BasicStroke(stroke));
            g.setColor(color);
            // 1分あたり，60度/360度 = PI/30.
            // 12の位置が - PI / 2.
            double angle = -Math.PI / 2 + (position * (Math.PI / 30));
            double x = length * magnification * Math.cos(angle);
            double y = length * magnification * Math.sin(angle);
            g.draw(new Line2D.Double(0, 0, x, y));
        }
    }

    private Image createBackgroundImage(){
        Dimension size = getSize();
        Image image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D)image.getGraphics();
        final double length = getLength() * 0.9;
        Point2D center = new Point2D.Double(size.getWidth() / 2, size.getHeight() / 2);

        final double angle = - Math.PI / 3;
        g.translate(center.getX(), center.getY());
        g.setColor(Color.BLACK);
        IntStream.range(0, 12)
            .forEach(index -> drawNumber(g, String.valueOf(index + 1), length, angle + (index * Math.PI / 6)));
        printer.println(() -> String.format("create new background image: %s%n", size));
        this.background = Optional.of(image);

        return image;
    }

    private void drawNumber(Graphics2D g, String label, double length, double angle) {
        double x = length * Math.cos(angle);
        double y = length * Math.sin(angle);
        g.drawString(label, (int)x, (int)y);
    }
}
