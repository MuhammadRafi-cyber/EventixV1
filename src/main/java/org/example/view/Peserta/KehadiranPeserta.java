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

public class KehadiranPeserta extends JFrame {

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
    private static final Color GREEN = new Color(5, 150, 105);
    private static final Color ORANGE = new Color(217, 119, 6);

    private final List<AttendanceItem> items = List.of(
            new AttendanceItem("Advanced Quantum\nMechanics", "Prof. Dr. Ir. Heryanto", "15 Okt 2023", "09:00 - 12:00 WIB", "08:45 WIB", "HADIR"),
            new AttendanceItem("Micro-Service Architecture", "Engr. Sarah Wijaya", "12 Okt 2023", "14:00 - 16:00 WIB", "14:15 WIB", "TERLAMBAT"),
            new AttendanceItem("Cybersecurity Fundamentals", "Indra Kusuma, CISSP", "08 Okt 2023", "10:00 - 13:00 WIB", "-", "ABSEN"),
            new AttendanceItem("AI in Healthcare", "Dr. Amelia Rossi", "05 Okt 2023", "13:00 - 15:00 WIB", "12:50 WIB", "HADIR")
    );

    public KehadiranPeserta() {
        setTitle("Eventix - Kehadiran Peserta");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new org.example.component.ParticipantSidebar("Kehadiran"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new org.example.component.Header("Kehadiran"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(32, 30, 34, 30));

        content.add(createTitle());
        content.add(Box.createVerticalStrut(32));
        content.add(createStatsRow());
        content.add(Box.createVerticalStrut(32));
        content.add(createAttendanceTable());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scrollPane, BorderLayout.CENTER);
        return main;
    }

    private JPanel createTitle() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label("Riwayat Kehadiran Saya", Font.BOLD, 31, TEXT));
        panel.add(Box.createVerticalStrut(6));
        panel.add(label("Pantau partisipasi dan status kehadiran Anda di seluruh sesi seminar.", Font.PLAIN, 15, MUTED));
        return panel;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        row.setPreferredSize(new Dimension(0, 110));
        row.add(statCard("TOTAL SEMINAR", "12", dashboardIcon("Seminar_Icon.svg", 22, 22), new Color(229, 235, 255)));
        row.add(statCard("HADIR", "10", null, new Color(220, 252, 231)));
        row.add(statCard("TERLAMBAT / ABSEN", "2", null, new Color(254, 226, 226)));
        return row;
    }

    private JPanel statCard(String title, String value, Icon icon, Color badgeBg) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 24));
        card.setBorder(new EmptyBorder(0, 24, 0, 0));
        card.add(new IconSlot(icon, badgeBg));
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label(title, Font.PLAIN, 11, MUTED));
        text.add(Box.createVerticalStrut(4));
        text.add(label(value, Font.BOLD, 30, TEXT));
        card.add(text);
        return card;
    }

    private JPanel createAttendanceTable() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 646));
        card.setPreferredSize(new Dimension(0, 646));
        card.add(createFilterRow());
        card.add(createTableHeader());
        for (AttendanceItem item : items) {
            card.add(createTableRow(item));
        }
        card.add(createPagination());
        return card;
    }

    private JPanel createFilterRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(24, 25, 22, 25));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        filters.setOpaque(false);
        filters.add(outlineControl("Semua Status", null, 164));
        filters.add(outlineControl("30 Hari Terakhir", icon("images/Icon/Dashboard/Panitia/Seminar/Date_Icon.svg", 17, 17), 180));
        row.add(filters, BorderLayout.WEST);

        JLabel count = label("Menampilkan 1-10 dari 12 entri", Font.PLAIN, 16, MUTED);
        row.add(count, BorderLayout.EAST);
        return row;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(true);
        header.setBackground(new Color(250, 249, 255));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(18, 32, 18, 32)
        ));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        addHeader(header, "JUDUL SEMINAR", 0, 3);
        addHeader(header, "TANGGAL &\nWAKTU", 1, 2);
        addHeader(header, "WAKTU\nPRESENSI", 2, 2);
        addHeader(header, "STATUS", 3, 1);
        addHeader(header, "AKSI", 4, 1);
        return header;
    }

    private void addHeader(JPanel row, String text, int x, int weight) {
        row.add(multiline(text, Font.BOLD, 11, MUTED), constraints(x, weight));
    }

    private JPanel createTableRow(AttendanceItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(26, 32, 24, 32)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));

        JPanel seminar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        seminar.setOpaque(false);
        seminar.add(new ImagePlaceholder());
        JPanel title = new JPanel();
        title.setOpaque(false);
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        title.add(multiline(item.title(), Font.PLAIN, 17, TEXT));
        title.add(label(item.speaker(), Font.PLAIN, 12, MUTED));
        seminar.add(title);
        row.add(seminar, constraints(0, 3));

        JPanel date = new JPanel();
        date.setOpaque(false);
        date.setLayout(new BoxLayout(date, BoxLayout.Y_AXIS));
        date.add(label(item.date(), Font.PLAIN, 16, TEXT));
        date.add(label(item.time(), Font.PLAIN, 12, MUTED));
        row.add(date, constraints(1, 2));

        row.add(label(item.presenceTime(), Font.PLAIN, 16, statusColor(item.status())), constraints(2, 2));
        row.add(statusPill(item.status()), constraints(3, 1));
        JLabel action = label("⋮", Font.BOLD, 24, MUTED);
        action.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(action, constraints(4, 1));
        return row;
    }

    private JPanel statusPill(String status) {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        JLabel pill = label(status, Font.BOLD, 12, statusColor(status));
        pill.setOpaque(true);
        pill.setBackground(statusBg(status));
        pill.setBorder(new EmptyBorder(7, 12, 7, 12));
        wrap.add(pill);
        return wrap;
    }

    private JPanel createPagination() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 24));
        footer.setOpaque(false);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        footer.add(pageButton("<", false));
        footer.add(pageButton("1", true));
        footer.add(pageButton("2", false));
        footer.add(pageButton(">", false));
        return footer;
    }

    private JButton pageButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(active ? Color.WHITE : MUTED);
        button.setBackground(active ? RED : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(10, 14, 10, 14)));
        button.setPreferredSize(new Dimension(40, 40));
        return button;
    }

    private JButton outlineControl(String text, Icon icon, int width) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(TEXT);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setIconTextGap(10);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(9, 16, 9, 16)));
        button.setPreferredSize(new Dimension(width, 42));
        return button;
    }

    private Color statusColor(String status) {
        return switch (status) {
            case "HADIR" -> GREEN;
            case "TERLAMBAT" -> ORANGE;
            default -> new Color(220, 38, 38);
        };
    }

    private Color statusBg(String status) {
        return switch (status) {
            case "HADIR" -> new Color(220, 252, 231);
            case "TERLAMBAT" -> new Color(255, 237, 213);
            default -> new Color(254, 226, 226);
        };
    }

    private GridBagConstraints constraints(int x, int weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
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

    private Icon icon(String path, int width, int height) {
        return new FlatSVGIcon(path, width, height);
    }

    private Icon dashboardIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/" + fileName, width, height);
    }
    private static class ImagePlaceholder extends JPanel {
        private ImagePlaceholder() {
            setOpaque(false);
            setPreferredSize(new Dimension(40, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(226, 233, 255));
            g2.fillRoundRect(0, 0, 40, 40, 8, 8);
            g2.dispose();
        }
    }

    private static class IconSlot extends JPanel {
        private final Icon icon;
        private final Color background;

        private IconSlot(Icon icon, Color background) {
            this.icon = icon;
            this.background = background;
            setOpaque(false);
            setPreferredSize(new Dimension(56, 56));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillOval(0, 0, 56, 56);
            if (icon != null) {
                icon.paintIcon(this, g2, 17, 17);
            }
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

    private record AttendanceItem(String title, String speaker, String date, String time, String presenceTime,
                                  String status) {
    }
}
