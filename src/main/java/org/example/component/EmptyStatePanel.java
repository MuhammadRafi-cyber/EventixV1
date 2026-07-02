package org.example.component;

import javax.swing.*;
import java.awt.*;

public class EmptyStatePanel extends JPanel{

    public EmptyStatePanel(String text){

        setOpaque(false);

        setLayout(new GridBagLayout());

        JLabel label=new JLabel(text);

        label.setFont(new Font("Segoe UI",Font.PLAIN,18));

        label.setForeground(Color.GRAY);

        add(label);

    }

}