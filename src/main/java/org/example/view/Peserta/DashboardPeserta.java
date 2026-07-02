package org.example.view.Peserta;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import model.User;
import org.example.util.Theme;
import org.example.view.LoginForm;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class DashboardPeserta extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color NAV_BG = new Color(80, 95, 148);
    private static final Color NAV_ACTIVE = new Color(104, 118, 169);
    private static final Color NAV_TEXT = new Color(218, 225, 246);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color SOFT_BLUE = new Color(243, 246, 255);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);
    private static final Color MAGENTA = new Color(192, 38, 211);

    private final List<StatItem> stats = List.of(
            new StatItem("SEMINAR YANG DIIKUTI", "12", "+12%", "Certificate_Icon.svg", new Color(182, 190, 255), TEXT),
            new StatItem("AKAN DATANG", "2", "Bulan Ini", "Seminar_Icon.svg", new Color(255, 214, 220), MUTED),
            new StatItem("SERTIFIKAT", "10", "Lihat Semua", "Certificate_Icon.svg", new Color(255, 202, 250), MUTED)
    );

    public DashboardPeserta() {
        setTitle("Eventix - Dashboard Peserta");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new org.example.component.ParticipantSidebar("Dashboard"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new org.example.component.Header("Dashboard"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(24, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(32, 28, 34, 28));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(createWelcome());
        left.add(Box.createVerticalStrut(28));
        left.add(createStatsRow());
        left.add(Box.createVerticalStrut(34));
        left.add(createRecommendationsHeader());
        left.add(Box.createVerticalStrut(20));
        left.add(createRecommendationCards());
        left.add(Box.createVerticalStrut(32));
        left.add(createActivitySection());

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(300, 0));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(Box.createVerticalStrut(210));
        right.add(createNotifications());
        right.add(Box.createVerticalStrut(32));
        right.add(createUpcomingCard());

        content.add(left, BorderLayout.CENTER);
        content.add(right, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scrollPane, BorderLayout.CENTER);
        return main;
    }

    private JPanel createWelcome() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        User user = AuthController.getUserAktif();
        String namaUser = user != null ? user.getNama() : "Peserta";

        JLabel title = label("Selamat Datang, " + namaUser + "!", Font.BOLD, 31, TEXT);
        JLabel subtitle = label("You have 2 upcoming seminars this week. Stay focused!", Font.PLAIN, 14, MUTED);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);
        return panel;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 178));
        row.setPreferredSize(new Dimension(0, 178));

        for (StatItem item : stats) {
            row.add(createStatCard(item));
        }
        return row;
    }

    private JPanel createStatCard(StatItem item) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(new IconBadge(item.icon(), item.badgeColor()), BorderLayout.WEST);
        JLabel note = label(item.note(), Font.BOLD, 12, item.noteColor());
        note.setHorizontalAlignment(SwingConstants.RIGHT);
        top.add(note, BorderLayout.EAST);

        JLabel title = label(item.title(), Font.BOLD, 12, MUTED);
        JLabel value = label(item.value(), Font.BOLD, 34, TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(top);
        body.add(Box.createVerticalStrut(18));
        body.add(title);
        body.add(Box.createVerticalStrut(4));
        body.add(value);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel createRecommendationsHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label("Direkomendasikan Untuk Anda", Font.BOLD, 25, TEXT));
        text.add(Box.createVerticalStrut(3));
        text.add(label("Berdasarkan minat akademis Anda", Font.PLAIN, 14, MUTED));

        JButton trends = ghostButton("View Trends");
        panel.add(text, BorderLayout.WEST);
        panel.add(trends, BorderLayout.EAST);
        return panel;
    }

    private JPanel createRecommendationCards() {
        JPanel row = new JPanel(new GridLayout(1, 2, 24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 284));
        row.setPreferredSize(new Dimension(0, 284));
        row.add(createSeminarCard("AI Ethics in Modern\nResearch", "Oct 24, 2024", "TECH", true, 42));
        row.add(createSeminarCard("Advanced Thesis\nMethodologies", "Nov 02, 2024", "ACADEMIC", false, 12));
        return row;
    }

    private JPanel createSeminarCard(String title, String date, String tag, boolean wave, int registered) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());

        JLabel image = new JLabel();
        image.setPreferredSize(new Dimension(0,118));
        image.setOpaque(true);
        image.setBackground(new Color(248,250,252));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setVerticalAlignment(SwingConstants.CENTER);
        image.setText("Preview Seminar");
        image.setForeground(new Color(170,170,170));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(image, BorderLayout.CENTER);

        JPanel tagWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,12));
        tagWrap.setOpaque(false);

        JLabel tagLabel = label(tag, Font.PLAIN, 10, MAGENTA);
        tagLabel.setOpaque(true);
        tagLabel.setBackground(Color.WHITE);
        tagLabel.setBorder(new EmptyBorder(5,9,5,9));

        tagWrap.add(tagLabel);
        imagePanel.add(tagWrap, BorderLayout.NORTH);

        card.add(imagePanel, BorderLayout.NORTH);

        JPanel detail = new JPanel();
        detail.setOpaque(false);
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));
        detail.setBorder(new EmptyBorder(20, 18, 16, 18));

        detail.add(multiline(title, Font.BOLD, 19, TEXT));
        detail.add(Box.createVerticalStrut(8));
        detail.add(label(dateLabel(date), Font.PLAIN, 13, MUTED));
        detail.add(Box.createVerticalStrut(16));
        detail.add(createSeminarFooter(registered));
        card.add(detail, BorderLayout.CENTER);
        return card;
    }

    private JPanel createSeminarFooter(int registered) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JPanel avatars = new JPanel(new FlowLayout(FlowLayout.LEFT, -8, 0));
        avatars.setOpaque(false);
        avatars.add(new MiniAvatar(new Color(224, 232, 244)));
        avatars.add(new MiniAvatar(new Color(204, 214, 226)));
        JLabel count = label("+" + registered + " terdaftar", Font.PLAIN, 10, MUTED);
        count.setBorder(new EmptyBorder(5, 12, 0, 0));
        avatars.add(count);

        JButton add = circleButton("+", 32, TEXT, Color.WHITE);
        footer.add(avatars, BorderLayout.WEST);
        footer.add(add, BorderLayout.EAST);
        return footer;
    }

    private JPanel createActivitySection() {
        JPanel section = new JPanel();
        section.setOpaque(false);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = label("Aktivitas terkini", Font.BOLD, 25, TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(title);
        section.add(Box.createVerticalStrut(24));
        section.add(createActivityCard());
        return section;
    }

    private JPanel createActivityCard() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 324));
        card.setPreferredSize(new Dimension(0, 324));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        header.setOpaque(true);
        header.setBackground(SOFT_BLUE);
        header.setBorder(new EmptyBorder(16, 18, 14, 18));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        header.add(new JLabel(dashboardIcon("Help_Icon.svg", 16, 16)));
        header.add(label("RIWAYAT AKTIVITAS", Font.BOLD, 12, MUTED));
        card.add(header);

        card.add(activityRow("Teregistrasi untuk AI Ethics Seminar", "2 hours ago", "Dashboard_Icon.svg", "..."));
        card.add(activityRow("Mendapat Sertifikat: Digital Marketing Essentials", "Yesterday at 4:30 PM", "Certificate_Icon.svg", "download"));
        card.add(activityRow("Feedback Submitted for Python Workshop", "Oct 12, 2024", "Reports_Icon.svg", "view"));

        JButton all = ghostButton("View All History");
        all.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(14, 0, 0, 0));
        footer.add(all);
        card.add(footer);
        return card;
    }

    private JPanel activityRow(String title, String time, String icon, String action) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(17, 18, 17, 18)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 73));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        left.setOpaque(false);
        left.add(new ActivityIcon(icon));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label(title, Font.BOLD, 13, TEXT));
        text.add(label(time, Font.PLAIN, 12, MUTED));
        left.add(text);

        JLabel actionLabel = label(action, Font.BOLD, 18, MUTED);
        row.add(left, BorderLayout.WEST);
        row.add(actionLabel, BorderLayout.EAST);
        return row;
    }

    private JPanel createNotifications() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 352));
        card.setPreferredSize(new Dimension(300, 352));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(true);
        header.setBackground(NAV_BG);
        header.setBorder(new EmptyBorder(18, 18, 18, 18));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        header.add(label("Notifikasi", Font.BOLD, 18, Color.WHITE), BorderLayout.WEST);
        JLabel badge = label("3 BARU", Font.BOLD, 10, Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(RED);
        badge.setBorder(new EmptyBorder(4, 9, 4, 9));
        header.add(badge, BorderLayout.EAST);
        card.add(header);

        JPanel urgent = new JPanel();
        urgent.setOpaque(true);
        urgent.setBackground(new Color(255, 244, 246));
        urgent.setBorder(new EmptyBorder(15, 18, 15, 16));
        urgent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 126));
        urgent.setLayout(new BoxLayout(urgent, BoxLayout.Y_AXIS));
        urgent.add(label("SEMINAR AKAN SEGERA DIMULAI", Font.BOLD, 12, RED));
        urgent.add(Box.createVerticalStrut(8));
        urgent.add(multiline("Data Science Fundamentals akan\ndimulai 15 menit lagi.", Font.PLAIN, 13, TEXT));
        urgent.add(Box.createVerticalStrut(9));
        JButton join = filledButton("JOIN NOW");
        join.setAlignmentX(Component.LEFT_ALIGNMENT);
        urgent.add(join);
        card.add(urgent);

        JPanel info = new BorderedPanel(8, Color.WHITE, CARD_BORDER);
        info.setLayout(new BorderLayout(12, 0));
        info.setBorder(new EmptyBorder(16, 16, 16, 16));
        info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));
        info.add(new JLabel(dashboardIcon("Help_Icon.svg", 16, 16)), BorderLayout.WEST);
        info.add(multiline("Tersedia Materi Baru\nProf. Smith added slides for 'Ethics AI'.", Font.BOLD, 12, TEXT), BorderLayout.CENTER);
        card.add(Box.createVerticalStrut(10));
        card.add(info);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 0, 0, 0));
        footer.add(label("Mark all as read", Font.BOLD, 12, MUTED));
        card.add(footer);
        return card;
    }

    private JPanel createUpcomingCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RED);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(getWidth() - 82, 0, 120, 50, 12, 12);
                g2.setColor(new Color(255, 255, 255, 38));
                g2.fillOval(getWidth() - 54, 12, 13, 13);
                g2.fillOval(getWidth() - 28, 12, 13, 13);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(25, 24, 24, 24));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 248));
        card.setPreferredSize(new Dimension(300, 248));

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(label("AKAN DATANG", Font.BOLD, 12, new Color(255, 174, 196)));
        body.add(Box.createVerticalStrut(12));
        body.add(multiline("Blockchain\nArchitecture", Font.BOLD, 24, Color.WHITE));
        body.add(Box.createVerticalStrut(10));
        body.add(label("Besok, 10:00 AM", Font.PLAIN, 14, Color.WHITE));
        body.add(Box.createVerticalStrut(26));
        JSeparator line = new JSeparator();
        line.setForeground(new Color(255, 255, 255, 70));
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        body.add(line);
        body.add(Box.createVerticalStrut(24));
        body.add(label("Pengajar", Font.PLAIN, 11, new Color(255, 202, 214)));
        body.add(label("Dr. Elena Vance", Font.BOLD, 12, Color.WHITE));

        JButton next = circleButton(">", 42, RED, Color.WHITE);
        card.add(body, BorderLayout.CENTER);
        card.add(next, BorderLayout.EAST);
        return card;
    }

    private String dateLabel(String date) {
        return "<html><span style='font-size:12px'>&#9633;</span> " + date + "</html>";
    }

    private JButton ghostButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(RED);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton filledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(RED);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(246, 32));
        button.setMaximumSize(new Dimension(246, 32));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton circleButton(String text, int size, Color foreground, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(size, CARD_BORDER, new Insets(0, 0, 0, 0)));
        button.setPreferredSize(new Dimension(size, size));
        button.setMaximumSize(new Dimension(size, size));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
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

    private class IconBadge extends JPanel {
        private final String icon;
        private final Color color;

        private IconBadge(String icon, Color color) {
            this.icon = icon;
            this.color = color;
            setOpaque(false);
            setPreferredSize(new Dimension(48, 48));
            setLayout(new GridBagLayout());
            add(new JLabel(dashboardIcon(icon, 20, 20)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, 46, 46, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class SeminarImagePanel extends JPanel {
        private final boolean wave;

        private SeminarImagePanel(boolean wave) {
            this.wave = wave;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape oldClip = g2.getClip();
            g2.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() + 10, 10, 10));
            if (wave) {
                GradientPaint paint = new GradientPaint(0, 0, new Color(11, 23, 41), getWidth(), getHeight(), new Color(34, 78, 108));
                g2.setPaint(paint);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int line = 0; line < 5; line++) {
                    g2.setColor(new Color(255, 80 + line * 18, 128 + line * 10, 110));
                    int base = 28 + line * 13;
                    for (int x = -20; x < getWidth(); x += 14) {
                        int y1 = base + (int) (Math.sin((x + line * 18) * 0.05) * 18);
                        int y2 = base + (int) (Math.sin((x + 14 + line * 18) * 0.05) * 18);
                        g2.drawLine(x, y1, x + 14, y2);
                    }
                }
            } else {
                g2.setColor(new Color(237, 240, 239));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(58, 18, 150, 78, 4, 4);
                g2.setColor(new Color(215, 220, 228));
                g2.drawRoundRect(58, 18, 150, 78, 4, 4);
                g2.setColor(new Color(117, 141, 160));
                g2.drawLine(78, 78, 188, 78);
                g2.setColor(new Color(208, 225, 238));
                g2.fillRoundRect(83, 38, 82, 5, 5, 5);
                g2.fillRoundRect(83, 51, 108, 5, 5, 5);
                g2.setColor(new Color(111, 165, 184));
                g2.drawPolyline(new int[]{83, 112, 139, 166, 191}, new int[]{70, 62, 66, 55, 59}, 5);
                g2.setColor(new Color(82, 126, 78));
                g2.fillOval(getWidth() - 72, 58, 44, 56);
            }
            g2.setClip(oldClip);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class MiniAvatar extends JPanel {
        private final Color color;

        private MiniAvatar(Color color) {
            this.color = color;
            setOpaque(false);
            setPreferredSize(new Dimension(20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillOval(0, 0, 20, 20);
            g2.dispose();
        }
    }

    private class ActivityIcon extends JPanel {
        private final String icon;

        private ActivityIcon(String icon) {
            this.icon = icon;
            setOpaque(false);
            setPreferredSize(new Dimension(38, 38));
            setLayout(new GridBagLayout());
            add(new JLabel(dashboardIcon(icon, 16, 16)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(243, 246, 255));
            g2.fillOval(0, 0, 38, 38);
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

    private record StatItem(String title, String value, String note, String icon, Color badgeColor, Color noteColor) {
    }
}
