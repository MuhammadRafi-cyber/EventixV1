package org.example.util;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class AppTheme {

    public static void setup() {

        FlatLightLaf.setup();

        UIManager.put("Component.arc", 18);
        UIManager.put("Button.arc", 18);
        UIManager.put("TextComponent.arc", 15);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("defaultFont",
                new Font("Segoe UI", Font.PLAIN, 14));
    }
}