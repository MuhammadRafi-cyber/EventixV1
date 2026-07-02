package org.example.view.Peserta;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.util.Theme;
import org.example.view.LoginForm;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class SertifikatPeserta extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color NAV_BG = new Color(80, 95, 148);
    private static final Color NAV_ACTIVE = new Color(104, 118, 169);
    private static final Color NAV_TEXT = new Color(218, 225, 246);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color SOFT_BLUE = new Color(243, 246, 255);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);
    private static final Color BLUE = new Color(80, 95, 148);
    private static final Color GREEN = new Color(22, 163, 74);
    private static final Color ORANGE = new Color(217, 119, 6);

    private final List<CertificateItem> certificates = List.of(
            new CertificateItem("Advanced Quantum\nMechanics", "Diterbitkan Dec 12, 2023", "TERVERIFIKASI"),
            new CertificateItem("Strategic Leadership\nin Business", "Diterbitkan Nov 28, 2023", "TERVERIFIKASI"),
            new CertificateItem("Data Science &\nEthics Seminar", "Diterbitkan Oct 15, 2023", "DALAM PROSES"),
            new CertificateItem("Digital Marketing\nFoundations", "Diterbitkan Sep 20, 2023", "TERVERIFIKASI"),
            new CertificateItem("Principles of\nBehavioral\nEconomics", "Diterbitkan Aug 05, 2023", "TERVERIFIKASI"),
            new CertificateItem("Principles of\nBehavioral\nEconomics", "Diterbitkan Aug 05, 2023", "TERVERIFIKASI")
    );

    public SertifikatPeserta() {
        setTitle("Eventix - Sertifikat Peserta");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new org.example.component.ParticipantSidebar("Sertifikat"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new org.example.component.Header("Sertifikat"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(32, 30, 30, 30));
        content.add(createTitleRow());
        content.add(Box.createVerticalStrut(28));
        content.add(createFilterPanel());
        content.add(Box.createVerticalStrut(32));
        content.add(createCertificateGrid());
        content.add(Box.createVerticalStrut(32));
        content.add(createFooterPagination());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scrollPane, BorderLayout.CENTER);
        return main;
    }

    private JPanel createTitleRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label("Kelola Sertifikat", Font.BOLD, 25, TEXT));
        text.add(Box.createVerticalStrut(6));
        text.add(label("Lihat dan kelola sertifikat yang diperoleh dari seminar yang telah diselesaikan.", Font.PLAIN, 14, MUTED));
        row.add(text, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actions.setOpaque(false);
        actions.add(outlineButton("Filter", icon("images/Icon/Dashboard/Panitia/Filter_Icon.svg", 16, 16), 108));
        actions.add(filledButton("Bulk Export", icon("images/Icon/Dashboard/Panitia/Download_Icon_Red.svg", 16, 16), 152));
        row.add(actions, BorderLayout.EAST);
        return row;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
        panel.add(filterField("Nama Seminar", "e.g. Advanced Quantum Mechanics", 410), gbc(0, 2.2));
        panel.add(filterField("Rentang Tanggal", "Oct 2023 - Jan 2024", 256), gbc(1, 1.35));
        panel.add(filterField("Status", "Diterbitkan", 192), gbc(2, 1.0));
        return panel;
    }

    private JPanel filterField(String title, String value, int width) {
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.Y_AXIS));
        field.add(label(title, Font.BOLD, 12, MUTED));
        field.add(Box.createVerticalStrut(7));
        JTextField input = new JTextField(value);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        input.setForeground(value.startsWith("e.g.") ? new Color(118, 128, 149) : TEXT);
        input.setBackground(Color.WHITE);
        input.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(9, 15, 9, 15)));
        input.setPreferredSize(new Dimension(width, 38));
        input.setMaximumSize(new Dimension(width, 38));
        field.add(input);
        return field;
    }

    private JPanel createCertificateGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 24, 24));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 790));
        for (CertificateItem item : certificates) {
            grid.add(createCertificateCard(item));
        }
        return grid;
    }

    private JPanel createCertificateCard(CertificateItem item) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(302, 382));
        CertificateImagePlaceholder image = new CertificateImagePlaceholder();
        image.setPreferredSize(new Dimension(0, 190));
        card.add(image, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(24, 25, 24, 25));

        JPanel titleRow = new JPanel(new BorderLayout(10, 0));
        titleRow.setOpaque(false);
        titleRow.add(multiline(item.title(), Font.BOLD, 19, TEXT), BorderLayout.WEST);
        titleRow.add(statusPill(item.status()), BorderLayout.EAST);
        body.add(titleRow);
        body.add(Box.createVerticalStrut(8));
        body.add(metaDate(item.date()));
        body.add(Box.createVerticalStrut(20));

        JPanel actions = new JPanel(new BorderLayout(16, 0));
        actions.setOpaque(false);
        actions.add(downloadButton(), BorderLayout.CENTER);
        actions.add(shareButton(), BorderLayout.EAST);
        body.add(actions);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel metaDate(String date) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        row.setOpaque(false);
        row.add(new JLabel(icon("images/Icon/Dashboard/Panitia/Seminar/Date_Icon.svg", 13, 13)));
        row.add(label(date, Font.PLAIN, 13, MUTED));
        return row;
    }

    private JLabel statusPill(String status) {
        JLabel pill = label(status, Font.BOLD, 9, "DALAM PROSES".equals(status) ? ORANGE : GREEN);
        pill.setOpaque(true);
        pill.setBackground("DALAM PROSES".equals(status) ? new Color(254, 243, 199) : new Color(220, 252, 231));
        pill.setBorder(new EmptyBorder(5, 8, 5, 8));
        return pill;
    }

    private JButton downloadButton() {
        JButton button = new JButton("Download PDF", icon("images/Icon/Dashboard/Panitia/Download_Icon_Red.svg", 15, 15));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(Theme.PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setIconTextGap(8);
        button.setPreferredSize(new Dimension(196, 34));
        return button;
    }

    private JButton shareButton() {
        JButton button = new JButton("Bagikan");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 0));
        button.setForeground(MUTED);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(8, 10, 8, 10)));
        button.setPreferredSize(new Dimension(42, 40));
        return button;
    }

    private JPanel createFooterPagination() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER));
        JLabel count = label("Menampilkan 6 dari 24 Sertifikat", Font.PLAIN, 13, MUTED);
        footer.add(count, BorderLayout.WEST);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 22));
        pages.setOpaque(false);
        pages.add(pageButton("<", false));
        pages.add(pageButton("1", true));
        pages.add(pageButton("2", false));
        pages.add(pageButton("3", false));
        pages.add(label("...", Font.PLAIN, 14, MUTED));
        pages.add(pageButton("5", false));
        pages.add(pageButton(">", false));
        footer.add(pages, BorderLayout.CENTER);
        return footer;
    }

    private JButton pageButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(active ? Color.WHITE : MUTED);
        button.setBackground(active ? Theme.PRIMARY : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(6, CARD_BORDER, new Insets(7, 11, 7, 11)));
        button.setPreferredSize(new Dimension(34, 34));
        return button;
    }

    private JButton outlineButton(String text, Icon icon, int width) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(TEXT);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setIconTextGap(8);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(10, 16, 10, 16)));
        button.setPreferredSize(new Dimension(width, 36));
        return button;
    }

    private JButton filledButton(String text, Icon icon, int width) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(Theme.PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setIconTextGap(8);
        button.setPreferredSize(new Dimension(width, 36));
        return button;
    }

    private GridBagConstraints gbc(int x, double weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, x == 2 ? 0 : 24);
        return gbc;
    }

    private JLabel label(String text, int style, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(color);
        return label;
    }

    private JLabel multiline(String text, int style, int size, Color color) {
        return label("<html>" + text.replace("\n", "<br>") + "</html>", style, size, color);
    }

    private Icon icon(String path, int width, int height) {
        return new FlatSVGIcon(path, width, height);
    }

    private Icon dashboardIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/" + fileName, width, height);
    }
    private static class CertificateImagePlaceholder extends JPanel {
        private CertificateImagePlaceholder() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(246, 247, 252));
            g2.fillRoundRect(0, 0, getWidth(), getHeight() + 8, 10, 10);
            g2.setColor(new Color(232, 236, 246));
            g2.fillRoundRect(24, 24, getWidth() - 48, getHeight() - 48, 8, 8);
            g2.dispose();
        }
    }
    private static class BorderedPanel extends JPanel {
        private final int radius;
        private final Color background;
        private final Color border;

        private BorderedPanel(int radius, Color background, Color border) {
            this.radius = radius;
            this.background = background;
            this.border = border;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.setColor(border);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final Insets padding;

        private RoundedBorder(int radius, Color color, Insets padding) {
            this.radius = radius;
            this.color = color;
            this.padding = padding;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return padding;
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = padding.top;
            insets.left = padding.left;
            insets.bottom = padding.bottom;
            insets.right = padding.right;
            return insets;
        }
    }

    private record CertificateItem(String title, String date, String status) {
    }
}
