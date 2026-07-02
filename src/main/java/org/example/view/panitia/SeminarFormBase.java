package org.example.view.panitia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.component.Header;
import org.example.component.Sidebar;
import org.example.util.Theme;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

abstract class SeminarFormBase extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color FIELD_BORDER = new Color(238, 181, 191);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);
    private static final Color SOFT_BLUE = new Color(238, 242, 255);

    protected SeminarFormBase(String title, String cancelText, String saveText, SeminarFormData data) {
        setTitle("Eventix - " + title);
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents(title, cancelText, saveText, data);
    }

    private void initComponents(String title, String cancelText, String saveText, SeminarFormData data) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new Sidebar("Seminar"), BorderLayout.WEST);
        root.add(createMainArea(title, cancelText, saveText, data), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea(String title, String cancelText, String saveText, SeminarFormData data) {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new Header("Seminar"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(32, 30, 34, 30));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(createTitleRow(title, cancelText, saveText));
        content.add(Box.createVerticalStrut(30));
        content.add(createFormGrid(data));

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scrollPane, BorderLayout.CENTER);
        return main;
    }

    private JPanel createTitleRow(String title, String cancelText, String saveText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(label(title, Font.BOLD, 25, TEXT));
        text.add(Box.createVerticalStrut(8));
        text.add(breadcrumb());
        row.add(text, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        actions.setOpaque(false);
        JButton cancel = outlineButton(cancelText);
        cancel.addActionListener(e -> openSeminarList());
        JButton save = primaryButton(saveText);
        actions.add(cancel);
        actions.add(save);
        row.add(actions, BorderLayout.EAST);
        return row;
    }

    private JPanel breadcrumb() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label("Seminar", Font.PLAIN, 12, MUTED));
        panel.add(label(">", Font.PLAIN, 12, MUTED));
        panel.add(label("Edit Seminar", Font.BOLD, 12, RED));
        return panel;
    }

    private JPanel createFormGrid(SeminarFormData data) {
        JPanel grid = new JPanel(new BorderLayout(24, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        left.add(infoCard(data));
        left.add(Box.createVerticalStrut(24));
        left.add(scheduleCard(data));
        left.add(Box.createVerticalStrut(24));
        left.add(descriptionCard(data));

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(305, 0));
        right.setMaximumSize(new Dimension(305, Integer.MAX_VALUE));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(posterCard());
        right.add(Box.createVerticalStrut(24));
        right.add(priceCard(data));
        right.add(Box.createVerticalGlue());

        grid.add(left, BorderLayout.CENTER);
        grid.add(right, BorderLayout.EAST);
        return grid;
    }

    private JPanel infoCard(SeminarFormData data) {
        JPanel card = card("Informasi Umum", addEditIcon("Info_Icon.svg", 15, 15));
        card.add(field("Judul Seminar", data.title(), "contoh: Advanced Quantum Computing in Modern Architecture"));
        card.add(Box.createVerticalStrut(18));
        JPanel row = new JPanel(new GridLayout(1, 2, 18, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        row.add(field("Nama Pembicara", data.speaker(), "Dr. Julian Vane"));
        row.add(selectField("Kategori", data.category()));
        card.add(row);
        return card;
    }

    private JPanel scheduleCard(SeminarFormData data) {
        JPanel card = card("Jadwal & Lokasi", addEditIcon("Location_Icon_Red.svg", 15, 15));
        JPanel row = new JPanel(new GridLayout(1, 3, 18, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        row.add(field("Tanggal", data.date(), "yyyy/mm/dd"));
        row.add(field("Waktu", data.time(), "--:-- --"));
        row.add(field("Kuota", data.quota(), "0"));
        card.add(row);
        card.add(Box.createVerticalStrut(18));
        card.add(field("Lokasi atau Link", data.location(), "Auditorium A, Building 4 atau Zoom Link"));
        return card;
    }

    private JPanel descriptionCard(SeminarFormData data) {
        JPanel card = card("Deskripsi Seminar", addEditIcon("Document_Icon.svg", 15, 15));
        JPanel editor = new JPanel(new BorderLayout());
        editor.setOpaque(false);
        editor.setAlignmentX(Component.LEFT_ALIGNMENT);
        editor.setBorder(new RoundedBorder(8, FIELD_BORDER, new Insets(1, 1, 1, 1)));

        JTextArea area = new JTextArea(data.description());
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setForeground(new Color(104, 113, 130));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(18, 18, 18, 18));
        area.setBackground(Color.WHITE);

        editor.add(area, BorderLayout.CENTER);
        editor.setPreferredSize(new Dimension(0, 210));
        editor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));
        card.add(editor);
        return card;
    }

    private JPanel posterCard() {
        JPanel card = card("Poster Seminar", addEditIcon("ImageIcon.svg", 15, 15));
        JPanel upload = new DashedPanel();
        upload.setPreferredSize(new Dimension(0, 216));
        upload.setMaximumSize(new Dimension(Integer.MAX_VALUE, 216));
        upload.setLayout(new BoxLayout(upload, BoxLayout.Y_AXIS));
        upload.setBorder(new EmptyBorder(32, 18, 24, 18));

        UploadIcon uploadIcon = new UploadIcon();
        uploadIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = label("Klik untuk unggah poster", Font.PLAIN, 16, TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel hint = label("<html><div style='text-align:center'>Ukuran disarankan:<br>1200x1600px. Maksimal 5MB.</div></html>",
                Font.PLAIN, 12, MUTED);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        upload.add(uploadIcon);
        upload.add(Box.createVerticalStrut(18));
        upload.add(title);
        upload.add(Box.createVerticalStrut(8));
        upload.add(hint);
        card.add(upload);
        return card;
    }

    private JPanel priceCard(SeminarFormData data) {
        JPanel card = card("Harga & Akses", addEditIcon("Money_Icon.svg", 19, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 290));
        card.add(field("Biaya Registrasi (Rp)", data.price(), "Rp0.00"));
        card.add(Box.createVerticalStrut(8));
        card.add(label("Isi 0 untuk seminar gratis.", Font.PLAIN, 11, MUTED));
        card.add(Box.createVerticalStrut(18));

        JPanel member = new JPanel(new BorderLayout());
        member.setBackground(SOFT_BLUE);
        member.setBorder(new EmptyBorder(12, 14, 12, 14));
        member.setPreferredSize(new Dimension(0, 66));
        member.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        member.add(iconLabel(addEditIcon("Shield_Icon.svg", 15, 15)), BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label("Khusus Member", Font.PLAIN, 16, TEXT));
        text.add(label("Batasi untuk staf universitas", Font.PLAIN, 10, MUTED));
        member.add(text, BorderLayout.CENTER);
        member.add(new ToggleSwitch(data.memberOnly()), BorderLayout.EAST);
        card.add(member);
        return card;
    }

    private JPanel card(String title, Icon icon) {
        JPanel card = new BorderedPanel(10, Color.WHITE, new Color(218, 226, 238));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(26, 26, 26, 26));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        header.add(iconLabel(icon));
        header.add(label(title, Font.PLAIN, 17, TEXT));
        card.add(header);
        card.add(Box.createVerticalStrut(24));
        return card;
    }

    private JPanel field(String label, String value, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        panel.add(label(label, Font.PLAIN, 16, MUTED), BorderLayout.NORTH);

        JTextField input = new JTextField();
        input.putClientProperty("JTextField.placeholderText", placeholder);
        input.setText(value);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        input.setForeground(TEXT);
        input.setBackground(Color.WHITE);
        input.setBorder(new RoundedBorder(8, FIELD_BORDER, new Insets(0, 16, 0, 16)));
        input.setPreferredSize(new Dimension(0, 52));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        panel.add(input, BorderLayout.CENTER);
        return panel;
    }

    private JPanel selectField(String label, String value) {
        JPanel panel = field(label, value, "Pilih kategori");
        JTextField field = (JTextField) panel.getComponent(1);
        field.putClientProperty("JTextField.trailingIcon", addEditIcon("Arrow_Down_Icon.svg", 11, 11));
        return panel;
    }

    private JButton outlineButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setForeground(new Color(80, 95, 148));
        button.setBackground(Color.WHITE);
        button.putClientProperty("JButton.arc", 10);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(12, 26, 12, 26)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(RED);
        button.putClientProperty("JButton.arc", 10);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(8, RED, new Insets(12, 32, 12, 32)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void openSeminarList() {
        new SeminarPanitia().setVisible(true);
        dispose();
    }

    private JLabel label(String text, int style, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(color);
        return label;
    }

    private Icon addEditIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/Panitia/Seminar/AddEdit/" + fileName, width, height);
    }

    private JLabel iconLabel(Icon icon) {
        JLabel label = new JLabel(icon);
        label.setPreferredSize(new Dimension(24, 22));
        label.setMinimumSize(new Dimension(24, 22));
        return label;
    }

    protected record SeminarFormData(String title, String speaker, String category, String date, String time,
                                     String quota, String location, String description, String price,
                                     boolean memberOnly) {
        static SeminarFormData empty() {
            return new SeminarFormData("", "", "Riset Akademik", "", "", "", "",
                    "Berikan gambaran detail tentang topik seminar, hasil pembelajaran, dan prasyarat...", "Rp0.00", false);
        }

        static SeminarFormData sample() {
            return new SeminarFormData("International Tech Expo 2024", "Dr. Julian Vane", "Riset Akademik",
                    "2024/10/24", "09:00 AM", "200", "Grand Ballroom, Jakarta",
                    "Tren global dalam software engineering, praktik industri, dan sesi diskusi bersama pembicara.",
                    "Rp0.00", true);
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

    private static class DashedPanel extends JPanel {
        private DashedPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            g2.setColor(CARD_BORDER);
            g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6, 5}, 0));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class UploadIcon extends JPanel {
        private UploadIcon() {
            setOpaque(false);
            setPreferredSize(new Dimension(64, 64));
            setMaximumSize(new Dimension(64, 64));
            setLayout(new GridBagLayout());
            add(new JLabel(addEditIcon("Upload_Icon.svg", 22, 22)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SOFT_BLUE);
            g2.fill(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ToggleSwitch extends JPanel {
        private final boolean active;

        private ToggleSwitch(boolean active) {
            this.active = active;
            setOpaque(false);
            setPreferredSize(new Dimension(44, 26));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(active ? Theme.PRIMARY : CARD_BORDER);
            g2.fillRoundRect(0, 1, 42, 24, 24, 24);
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Double(active ? 20 : 3, 4, 18, 18));
            g2.dispose();
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
}
