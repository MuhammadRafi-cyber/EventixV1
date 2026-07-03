package org.example.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import org.example.util.Theme;
import org.example.view.LoginForm;
import org.example.view.panitia.DashboardPanitia;
import org.example.view.panitia.PesertaPanitia;
import org.example.view.panitia.SeminarPanitia;
import org.example.view.panitia.LaporanPanitia; // 👉 Import Halaman Laporan Panitia

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sidebar extends JPanel {

    private static final Color NAV_BG = new Color(80, 95, 148);
    private static final Color NAV_ACTIVE = new Color(104, 118, 169);
    private static final Color NAV_TEXT = new Color(218, 225, 246);
    private static final int WIDTH = 262;

    public Sidebar(String active) {
        setPreferredSize(new Dimension(WIDTH, 0));
        setBackground(NAV_BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(34, 10, 24, 8));

        add(createBrand());
        add(Box.createVerticalStrut(28));

        add(menu("Dashboard", "Dashboard", "Dashboard_Icon.svg", "Dashboard_Icon_White.svg", active));
        add(menu("Seminar", "Seminar", "Seminar_Icon.svg", "Seminar_Icon_White.svg", active));
        add(menu("Peserta", "Peserta", "Attendance_Icon.svg", "Attendance_Icon_White.svg", active));
        add(menu("Sertifikat", "Sertifikat", "Certificate_Icon.svg", "Certificate_Icon_White.svg", active));
        add(menu("Laporan", "Laporan", "Reports_Icon.svg", "Reports_Icon_White.svg", active));

        add(Box.createVerticalStrut(32));
        add(createDivider());
        add(Box.createVerticalStrut(26));
        add(menu("Pengaturan", "Pengaturan", "Settings_Icon.svg", "Settings_Icon.svg", active));

        JPanel logout = menu("Keluar", "Keluar", "LogOut_Icon.svg", "LogOut_Icon.svg", active);
        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Window window = SwingUtilities.getWindowAncestor(Sidebar.this);
                AuthController.setUserAktif(null);
                new LoginForm().setVisible(true);
                if (window != null) {
                    window.dispose();
                }
            }
        });
        add(logout);
        add(Box.createVerticalGlue());
    }

    private JPanel createBrand() {
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        brand.setOpaque(false);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        brand.add(new JLabel(icon("Certificate_Icon_White.svg", 19, 24)));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Eventix");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Manajemen Seminar");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(NAV_TEXT);

        text.add(title);
        text.add(subtitle);
        brand.add(text);
        return brand;
    }

    private JPanel menu(String label, String key, String normalIcon, String activeIcon, String active) {
        boolean selected = active.equalsIgnoreCase(key) || active.equalsIgnoreCase(label);
        JPanel item = selected ? new ActiveItemPanel() : new JPanel(new BorderLayout());
        item.setLayout(new BorderLayout());
        item.setOpaque(false);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        item.setPreferredSize(new Dimension(230, 50));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(15, 18, 0, 0));
        content.add(new JLabel(icon(selected ? activeIcon : normalIcon, 15, 15)));

        JLabel text = new JLabel(label);
        text.setFont(new Font("Segoe UI", Font.BOLD, 12));
        text.setForeground(selected ? Color.WHITE : NAV_TEXT);
        content.add(text);

        item.add(content, BorderLayout.CENTER);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!selected) {
                    navigate(key);
                }
            }
        });
        return item;
    }

    // 👉 INI ADALAH METHOD NAVIGATE YANG SUDAH DITAMBAHKAN LAPORAN
    private void navigate(String key) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if ("Dashboard".equalsIgnoreCase(key)) {
            new DashboardPanitia().setVisible(true);
            if (window != null) {
                window.dispose();
            }
        } else if ("Seminar".equalsIgnoreCase(key)) {
            new SeminarPanitia().setVisible(true);
            if (window != null) {
                window.dispose();
            }
        } else if ("Peserta".equalsIgnoreCase(key)) {
            new PesertaPanitia().setVisible(true);
            if (window != null) {
                window.dispose();
            }
        } else if ("Laporan".equalsIgnoreCase(key)) { // 👉 Logika tab laporan ditambahkan di sini
            new LaporanPanitia().setVisible(true);
            if (window != null) {
                window.dispose();
            }
        }
    }

    private JComponent createDivider() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(113, 127, 176));
        separator.setMaximumSize(new Dimension(244, 1));
        return separator;
    }

    private Icon icon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/" + fileName, width, height);
    }

    private static class ActiveItemPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(NAV_ACTIVE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Theme.PRIMARY);
            g2.fillRect(0, 0, 4, getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }
}