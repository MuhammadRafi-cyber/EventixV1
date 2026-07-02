package org.example.component;

import org.example.util.Theme;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton {

    public PrimaryButton(String text){

        super(text);

        setBackground(Theme.PRIMARY);

        setForeground(Color.WHITE);

        setFont(new Font("Segoe UI",Font.BOLD,14));

        setFocusPainted(false);

        setBorderPainted(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

}