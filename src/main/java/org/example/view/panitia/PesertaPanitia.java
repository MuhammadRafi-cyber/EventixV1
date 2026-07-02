package org.example.view.panitia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import dao.PendaftaranDAO;
import org.example.component.Header;
import org.example.component.Sidebar;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PesertaPanitia extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color TABLE_HEAD = new Color(243, 246, 255);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);
    private static final Color GREEN = new Color(19, 140, 73);
    private static final Color YELLOW = new Color(178, 111, 0);

    private final List<ParticipantItem> participants;

    public PesertaPanitia() {
        participants = loadParticipants();
        setTitle("Eventix - Kelola Peserta");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new Sidebar("Peserta"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new Header("Peserta"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(32, 30, 34, 30));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createHeroRow());
        content.add(Box.createVerticalStrut(24));
        content.add(createStatsRow());
        content.add(Box.createVerticalStrut(28));
        content.add(createTable());

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
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label("Kelola Peserta", Font.BOLD, 31, TEXT));
        text.add(Box.createVerticalStrut(7));
        text.add(label("Pantau dan kelola seluruh pendaftar seminar.", Font.PLAIN, 15, MUTED));
        row.add(text, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actions.setOpaque(false);
        actions.add(createFilter());
        actions.add(createExportButton());
        row.add(actions, BorderLayout.EAST);
        return row;
    }

    private JComboBox<String> createFilter() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Semua Seminar"});
        combo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        combo.setForeground(TEXT);
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(254, 38));
        combo.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(0, 12, 0, 12)));
        return combo;
    }

    private JButton createExportButton() {
        JButton button = new JButton("Export CSV", panitiaIcon("Download_Icon_Red.svg", 16, 16));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(TEXT);
        button.setBackground(Color.WHITE);
        button.setIconTextGap(10);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(8, 16, 8, 16)));
        return button;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 152));
        row.setPreferredSize(new Dimension(0, 152));

        int total = participants.size();
        int confirmed = countStatus("CONFIRMED");
        int pending = countStatus("PENDING");
        int cancelled = countStatus("CANCELLED");

        row.add(statCard("Total Peserta", formatNumber(total), "+12%", "person_pin.svg", new Color(255, 235, 242), RED));
        row.add(statCard("Terkonfirmasi", formatNumber(confirmed), "", "Seminar_Icon_Red.svg", new Color(245, 226, 255), new Color(180, 38, 211)));
        row.add(statCard("Pending Requests", formatNumber(pending), "", "Filer2_Icon.svg", new Color(237, 241, 255), new Color(80, 95, 148)));
        row.add(statCard("Dibatalkan", formatNumber(cancelled), "", "Mail_Icon_Red.svg", new Color(255, 238, 240), RED));
        return row;
    }

    private JPanel statCard(String title, String value, String note, String icon, Color iconBg, Color iconFg) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 22, 24));

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(new IconBadge(icon, iconBg), BorderLayout.WEST);
        if (!note.isEmpty()) {
            JLabel noteLabel = label(note + "  ↗", Font.BOLD, 12, iconFg);
            noteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            top.add(noteLabel, BorderLayout.EAST);
        }

        body.add(top);
        body.add(Box.createVerticalStrut(14));
        body.add(label(title, Font.PLAIN, 13, MUTED));
        body.add(Box.createVerticalStrut(4));
        body.add(label(value, Font.BOLD, 35, TEXT));
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTable() {
        JPanel table = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
        table.setAlignmentX(Component.LEFT_ALIGNMENT);
        table.setMaximumSize(new Dimension(Integer.MAX_VALUE, 700));
        table.setPreferredSize(new Dimension(0, 700));
        table.add(createTableHeader());

        if (participants.isEmpty()) {
            table.add(createEmptyRow());
        } else {
            for (ParticipantItem item : participants.subList(0, Math.min(5, participants.size()))) {
                table.add(createParticipantRow(item));
            }
        }
        table.add(createTableFooter());
        return table;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(true);
        header.setBackground(TABLE_HEAD);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        header.setPreferredSize(new Dimension(0, 66));
        header.setBorder(new EmptyBorder(0, 30, 0, 16));
        addHeaderCell(header, "NAMA\nPESERTA", 0, 2);
        addHeaderCell(header, "ALAMAT EMAIL", 1, 2);
        addHeaderCell(header, "JUDUL\nSEMINAR", 2, 2);
        addHeaderCell(header, "TANGGAL\nREGISTRASI", 3, 2);
        addHeaderCell(header, "STATUS", 4, 1);
        addHeaderCell(header, "AKSI", 5, 1);
        return header;
    }

    private JPanel createParticipantRow(ParticipantItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 114));
        row.setPreferredSize(new Dimension(0, 114));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(246, 224, 229)),
                new EmptyBorder(18, 30, 18, 16)
        ));
        addCell(row, createNameCell(item), 0, 2);
        addCell(row, label(item.email(), Font.PLAIN, 14, MUTED), 1, 2);
        addCell(row, createSeminarCell(item.seminar()), 2, 2);
        addCell(row, label(formatDate(item.registeredAt()), Font.PLAIN, 14, MUTED), 3, 2);
        addCell(row, statusBadge(item.status()), 4, 1);
        addCell(row, actionsLabel(), 5, 1);
        return row;
    }

    private JPanel createNameCell(ParticipantItem item) {
        JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        cell.setOpaque(false);
        cell.add(new JLabel(dashboardIcon("user_icon.svg", 38, 38)));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label(item.name(), Font.BOLD, 13, TEXT));
        text.add(Box.createVerticalStrut(3));
        text.add(label("ID: " + item.id(), Font.PLAIN, 12, MUTED));
        cell.add(text);
        return cell;
    }

    private JPanel createSeminarCell(String title) {
        JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        cell.setOpaque(false);
        JPanel marker = new JPanel();
        marker.setOpaque(true);
        marker.setBackground(RED);
        marker.setPreferredSize(new Dimension(3, 18));
        cell.add(marker);
        cell.add(multiline(title, Font.BOLD, 14, TEXT));
        return cell;
    }

    private JLabel actionsLabel() {
        JLabel label = label("⋮", Font.BOLD, 24, MUTED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createEmptyRow() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 456));
        row.setPreferredSize(new Dimension(0, 456));
        row.add(label("Belum ada peserta terdaftar.", Font.PLAIN, 15, MUTED));
        return row;
    }

    private JPanel createTableFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(true);
        footer.setBackground(TABLE_HEAD);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        footer.setPreferredSize(new Dimension(0, 64));
        footer.setBorder(new EmptyBorder(0, 32, 0, 32));
        footer.add(label("Menampilkan " + Math.min(5, participants.size()) + " dari " + formatNumber(participants.size()) + " peserta", Font.PLAIN, 13, MUTED), BorderLayout.WEST);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pages.setOpaque(false);
        pages.add(pageButton("‹", false));
        pages.add(pageButton("1", true));
        pages.add(pageButton("2", false));
        pages.add(pageButton("3", false));
        pages.add(label("...", Font.BOLD, 13, MUTED));
        pages.add(pageButton("›", false));
        footer.add(pages, BorderLayout.EAST);
        return footer;
    }

    private JButton pageButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(active ? Color.WHITE : MUTED);
        button.setBackground(active ? RED : TABLE_HEAD);
        button.setPreferredSize(new Dimension(32, 32));
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(8, active ? RED : CARD_BORDER, new Insets(0, 0, 0, 0)));
        return button;
    }

    private JLabel statusBadge(String status) {
        boolean confirmed = "CONFIRMED".equalsIgnoreCase(status);
        Color bg = confirmed ? new Color(216, 248, 229) : new Color(255, 240, 194);
        Color fg = confirmed ? GREEN : YELLOW;
        JLabel label = label(confirmed ? "TERKONFIRMASI" : "PENDING", Font.BOLD, 11, fg);
        label.setOpaque(true);
        label.setBackground(bg);
        label.setBorder(new EmptyBorder(5, 12, 5, 12));
        return label;
    }

    private void addHeaderCell(JPanel row, String text, int gridx, int weight) {
        addCell(row, multiline(text, Font.BOLD, 12, MUTED), gridx, weight);
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
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private int countStatus(String status) {
        int count = 0;
        for (ParticipantItem item : participants) {
            if (status.equalsIgnoreCase(item.status())) count++;
        }
        return count;
    }

    private List<ParticipantItem> loadParticipants() {
        List<ParticipantItem> items = new ArrayList<>();
        try {
            for (Object[] row : new PendaftaranDAO().getSemuaPesertaPanitia()) {
                items.add(new ParticipantItem(
                        String.valueOf(row[0]),
                        value(row[1]),
                        value(row[2]),
                        value(row[3]),
                        value(row[4]),
                        value(row[5])
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal mengambil data peserta: " + e.getMessage());
        }
        return items;
    }

    private String value(Object value) {
        return value != null ? value.toString() : "-";
    }

    private String formatDate(String value) {
        if (value == null || value.length() < 10) return "-";
        return value.substring(0, 10);
    }

    private String formatNumber(int value) {
        return String.format("%,d", value).replace(',', '.');
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

    private Icon dashboardIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/" + fileName, width, height);
    }

    private Icon panitiaIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/Panitia/" + fileName, width, height);
    }

    private class IconBadge extends JPanel {
        private final String icon;
        private final Color background;

        private IconBadge(String icon, Color background) {
            this.icon = icon;
            this.background = background;
            setOpaque(false);
            setPreferredSize(new Dimension(40, 40));
            setLayout(new GridBagLayout());
            add(new JLabel(panitiaIcon(icon, 18, 18)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillRoundRect(0, 0, 40, 40, 8, 8);
            g2.dispose();
            super.paintComponent(g);
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

    private record ParticipantItem(String id, String name, String email, String seminar, String registeredAt, String status) {
    }
}
