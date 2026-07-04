package org.example;

import org.example.util.AppTheme;
import org.example.view.LandingPage;
import org.example.view.LoginForm;
import org.example.view.Peserta.DashboardPeserta;

import javax.swing.*;

public class TesFrontEnd {

    public static void main(String[] args) {

        AppTheme.setup();

        SwingUtilities.invokeLater(() ->
                new LandingPage().setVisible(true)
        );
    }
}