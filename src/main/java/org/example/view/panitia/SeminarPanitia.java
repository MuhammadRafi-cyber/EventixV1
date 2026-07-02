package org.example.view.panitia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.component.Header;
import org.example.component.Sidebar;
import org.example.util.AppTheme;
import org.example.util.Theme;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class SeminarPanitia extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);

    private final List<SeminarItem> seminarItems = List.of(
            new SeminarItem("International\nTech Expo 2024", "Tren Global dalam\nSoftware Engineering",
                    "24 Okt 2024,\n09.00", "Grand Ballroom,\nJakarta", "172 / 200", "86%", 0.86, "Aktif", Status.ACTIVE),
            new SeminarItem("Advanced Quantum\nComputing", "Workshop Kriptografi\nPost-Quantum",
                    "12 Nov 2024,\n14.00", "Virtual Event\n(Zoom)", "45 / 150", "30%", 0.30, "Mendatang", Status.UPCOMING),
            new SeminarItem("AI Ethics\nSymposium", "Mendefinisikan Kemanusiaan\ndi Era Silicon",
                    "05 Sep 2024,\n10.00", "Auditorium\nUtama, UI", "500 / 500", "100%", 1.0, "Selesai", Status.COMPLETED)
    );

    public SeminarPanitia() {
        setTitle("Eventix - Kelola Seminar");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    public static void main(String[] args) {
        AppTheme.setup();
        SwingUtilities.invokeLater(() -> new SeminarPanitia().setVisible(true));
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new Sidebar("Seminar"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new Header("Seminar"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(32, 30, 34, 30));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createHeroRow());
        content.add(Box.createVerticalStrut(30));
        content.add(createTabs());
        content.add(Box.createVerticalStrut(24));
        content.add(createSeminarTable());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scrollPane, BorderLayout.CENTER);
        return main;
    }

    private JPanel createHeroRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label("Kelola Seminar", Font.BOLD, 25, TEXT));
        text.add(Box.createVerticalStrut(8));
        text.add(label("Kelola dan pantau semua acara seminar Anda di satu tempat.", Font.PLAIN, 15, MUTED));
        row.add(text, BorderLayout.WEST);

        JButton create = new JButton("Buat Seminar Baru");
        create.setIcon(seminarIcon("Add_Icon.svg", 14, 14));
        create.setIconTextGap(10);
        create.setFont(new Font("Segoe UI", Font.BOLD, 15));
        create.setForeground(Color.WHITE);
        create.setBackground(RED);
        create.putClientProperty("JButton.arc", 12);
        create.setBorder(new RoundedBorder(10, RED, new Insets(11, 24, 11, 24)));
        create.setFocusPainted(false);
        create.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        create.addActionListener(e -> openFrame(new BuatSeminar()));
        row.add(create, BorderLayout.EAST);
        return row;
    }

    private JPanel createTabs() {
        JPanel tabs = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        tabs.setLayout(new GridLayout(1, 4, 0, 0));
        tabs.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabs.setMaximumSize(new Dimension(430, 58));
        tabs.setPreferredSize(new Dimension(430, 58));
        tabs.setBorder(new EmptyBorder(4, 4, 4, 4));
        tabs.add(tabButton("Semua", true));
        tabs.add(tabButton("Mendatang", false));
        tabs.add(tabButton("Aktif", false));
        tabs.add(tabButton("Selesai", false));
        return tabs;
    }

    private JButton tabButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(active ? Color.WHITE : MUTED);
        button.setBackground(active ? RED : Color.WHITE);
        button.putClientProperty("JButton.arc", 10);
        button.setBorder(active ? new RoundedBorder(9, RED, new Insets(0, 0, 0, 0)) : new EmptyBorder(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createSeminarTable() {
        JPanel table = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
        table.setAlignmentX(Component.LEFT_ALIGNMENT);
        table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 646));
        table.setPreferredSize(new Dimension(0, 646));
        table.add(createTableHeader());
        for (SeminarItem item : seminarItems) {
            table.add(createSeminarRow(item));
        }
        table.add(createTableFooter());
        return table;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(true);
        header.setBackground(new Color(243, 246, 255));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        header.setPreferredSize(new Dimension(0, 66));
        header.setBorder(new EmptyBorder(0, 26, 0, 20));
        addHeaderCell(header, "DETAIL SEMINAR", 0, 3);
        addHeaderCell(header, "JADWAL", 1, 2);
        addHeaderCell(header, "REGISTRASI", 2, 2);
        addHeaderCell(header, "STATUS", 3, 2);
        addHeaderCell(header, "AKSI", 4, 1);
        return header;
    }

    private void addHeaderCell(JPanel row, String text, int gridx, int weight) {
        row.add(label(text, Font.BOLD, 12, MUTED), constraints(gridx, weight));
    }

    private JPanel createSeminarRow(SeminarItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        row.setPreferredSize(new Dimension(0, 160));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(20, 26, 20, 24)
        ));
        addDetails(row, item);
        addSchedule(row, item);
        addRegistration(row, item);
        addStatus(row, item);
        addActions(row);
        return row;
    }

    private void addDetails(JPanel row, SeminarItem item) {
        JPanel details = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        details.setOpaque(false);
        details.add(new ThumbnailPanel());

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(multiline(item.title(), Font.PLAIN, 17, TEXT));
        text.add(Box.createVerticalStrut(6));
        text.add(multiline(item.subtitle(), Font.PLAIN, 13, MUTED));
        details.add(text);
        addCell(row, details, 0, 3);
    }

    private void addSchedule(JPanel row, SeminarItem item) {
        JPanel schedule = new JPanel();
        schedule.setOpaque(false);
        schedule.setLayout(new BoxLayout(schedule, BoxLayout.Y_AXIS));
        schedule.add(iconText(item.date(), "Date_Icon.svg"));
        schedule.add(Box.createVerticalStrut(12));
        schedule.add(iconText(item.location(), "Location_Icon.svg"));
        addCell(row, schedule, 1, 2);
    }

    private JPanel iconText(String text, String icon) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panel.setOpaque(false);
        JLabel iconLabel = new JLabel(seminarIcon(icon, 11, 11));
        iconLabel.setPreferredSize(new Dimension(16, 16));
        panel.add(iconLabel);
        panel.add(multiline(text, Font.PLAIN, 14, TEXT));
        return panel;
    }

    private void addRegistration(JPanel row, SeminarItem item) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(label(item.count(), Font.BOLD, 12, TEXT), BorderLayout.WEST);
        top.add(label(item.percent(), Font.BOLD, 12, RED), BorderLayout.EAST);
        panel.add(top);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new ProgressLine(item.progress(), RED));
        addCell(row, panel, 2, 2);
    }

    private void addStatus(JPanel row, SeminarItem item) {
        Color bg = switch (item.statusType()) {
            case ACTIVE -> new Color(176, 191, 255);
            case UPCOMING -> new Color(225, 234, 255);
            case COMPLETED -> new Color(246, 232, 232);
        };
        JLabel pill = label(item.status(), Font.BOLD, 11, MUTED);
        pill.setHorizontalAlignment(SwingConstants.CENTER);
        pill.setOpaque(true);
        pill.setBackground(bg);
        pill.setBorder(new EmptyBorder(6, 16, 6, 16));

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.add(pill);
        addCell(row, wrap, 3, 2);
    }

    private void addActions(JPanel row) {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton edit = smallButton("Edit", false);
        edit.addActionListener(e -> openFrame(new EditSeminar()));
        actions.add(edit);
        actions.add(smallButton("Hapus", true));
        addCell(row, actions, 4, 1);
    }

    private JButton smallButton(String text, boolean filled) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setForeground(filled ? Color.WHITE : new Color(80, 95, 148));
        button.setBackground(filled ? RED : Color.WHITE);
        button.putClientProperty("JButton.arc", 10);
        button.setFocusPainted(false);
        button.setBorder(filled
                ? new RoundedBorder(8, RED, new Insets(9, 16, 9, 16))
                : new RoundedBorder(8, CARD_BORDER, new Insets(9, 16, 9, 16)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createTableFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(true);
        footer.setBackground(new Color(243, 246, 255));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 98));
        footer.setPreferredSize(new Dimension(0, 98));
        footer.setBorder(new EmptyBorder(28, 26, 28, 26));
        footer.add(label("Menampilkan 3 dari 12 seminar", Font.PLAIN, 12, MUTED), BorderLayout.WEST);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pages.setOpaque(false);
        pages.add(pageButton("<", false));
        pages.add(pageButton("1", true));
        pages.add(pageButton("2", false));
        pages.add(pageButton("3", false));
        pages.add(pageButton(">", false));
        footer.add(pages, BorderLayout.EAST);
        return footer;
    }

    private JButton pageButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(34, 32));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(active ? RED : MUTED);
        button.setBackground(Color.WHITE);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(0, 0, 0, 0)));
        button.setFocusPainted(false);
        return button;
    }

    private void openFrame(JFrame frame) {
        frame.setVisible(true);
        dispose();
    }

    private void addCell(JPanel row, Component component, int gridx, int weight) {
        row.add(component, constraints(gridx, weight));
    }

    private GridBagConstraints constraints(int gridx, int weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
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

    private Icon seminarIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/Panitia/Seminar/" + fileName, width, height);
    }

    private static class ThumbnailPanel extends JPanel {
        private ThumbnailPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(80, 56));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint paint = new GradientPaint(0, 0, new Color(12, 36, 57), getWidth(), getHeight(), new Color(18, 150, 160));
            g2.setPaint(paint);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.setColor(new Color(255, 255, 255, 42));
            for (int i = 12; i < getWidth(); i += 12) {
                g2.drawLine(i, 8, i + 18, getHeight() - 8);
            }
            g2.dispose();
        }
    }

    private static class ProgressLine extends JPanel {
        private final double progress;
        private final Color color;

        private ProgressLine(double progress, Color color) {
            this.progress = progress;
            this.color = color;
            setOpaque(false);
            setPreferredSize(new Dimension(128, 8));
            setMaximumSize(new Dimension(128, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(229, 236, 252));
            g2.fillRoundRect(0, 1, getWidth(), 6, 8, 8);
            g2.setColor(color);
            g2.fillRoundRect(0, 1, (int) (getWidth() * progress), 6, 8, 8);
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
    }

    private enum Status {
        ACTIVE, UPCOMING, COMPLETED
    }

    private record SeminarItem(String title, String subtitle, String date, String location, String count,
                               String percent, double progress, String status, Status statusType) {
    }
}
