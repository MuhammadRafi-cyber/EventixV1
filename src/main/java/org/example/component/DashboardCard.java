package org.example.component;

import org.example.util.Theme;

import javax.swing.*;
import java.awt.*;

public class DashboardCard extends RoundedPanel {

    public DashboardCard(String title,String value){

        super(Color.WHITE,20);

        setLayout(new BorderLayout());

        setPreferredSize(new Dimension(230,120));

        JLabel lblTitle=new JLabel(title);

        lblTitle.setFont(new Font("Segoe UI",Font.PLAIN,15));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue=new JLabel(value);

        lblValue.setFont(new Font("Segoe UI",Font.BOLD,34));
        lblValue.setForeground(Theme.SECONDARY);

        JPanel p=new JPanel();

        p.setOpaque(false);

        p.setBorder(BorderFactory.createEmptyBorder(
                20,
                20,
                20,
                20
        ));

        p.setLayout(new BorderLayout());

        p.add(lblTitle,BorderLayout.NORTH);

        p.add(lblValue,BorderLayout.CENTER);

        add(p);

    }

}