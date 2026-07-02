package org.example.component;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private final int radius;
    private final Color background;

    public RoundedPanel(Color background,int radius){

        this.background = background;
        this.radius = radius;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){

        Graphics2D g2=(Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(background);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                radius,
                radius
        );

        g2.dispose();

        super.paintComponent(g);

    }

}