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

public class SeminarPeserta extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color NAV_BG = new Color(80, 95, 148);
    private static final Color NAV_ACTIVE = new Color(104, 118, 169);
    private static final Color NAV_TEXT = new Color(218, 225, 246);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color SOFT_BLUE = new Color(239, 243, 255);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);
    private static final Color BLUE = new Color(80, 95, 148);

    private static final String CATALOG = "catalog";
    private static final String DETAIL = "detail";
    private static final String STEP_ONE = "step_one";
    private static final String STEP_TWO = "step_two";
    private static final String SUCCESS = "success";

    private final CardLayout pages = new CardLayout();
    private final JPanel pageHost = new JPanel(pages);

    private final List<SeminarItem> seminars = List.of(
            new SeminarItem("Neural Architectures: The Future of Generative...", "Dr. Elena Rodriguez", "MIT Research Labs",
                    "October 24, 2024", "10:00 AM - 12:30 PM (PST)", "Rp150.000", "Tersisa 12 Kursi", "TEKNOLOGI", 0, true),
            new SeminarItem("Global Market Trends & Sustainable Investment", "Mark Stevenson, MBA", "London School of Economics",
                    "October 28, 2024", "02:00 PM - 05:00 PM (GMT)", "Free", "Limited Spots", "BISNIS", 1, true),
            new SeminarItem("Post-Modernism: Deconstructing Spatial...", "Dr. Arthur Penhaligon", "Royal Academy of Arts",
                    "November 02, 2024", "11:00 AM - 1:00 PM (EST)", "Rp75.000", "Tersisa 45 Kursi", "SENI", 2, true),
            new SeminarItem("Gene Editing: Ethical Frontiers in 2025", "Prof. Sarah Chen", "Stanford University",
                    "November 05, 2024", "09:00 AM - 12:00 PM (PST)", "Rp210.000", "Terjual Habis", "BIOSAINS", 3, false),
            new SeminarItem("Digital Marketing Foundations", "Prof. Sarah Chen", "Stanford University",
                    "November 05, 2024", "11:00 AM - 14:00 PM (PST)", "Rp250.000", "Tersisa 9 Kursi", "BISNIS", 4, true),
            new SeminarItem("Gene Editing: Ethical Frontiers in 2025", "Prof. Sarah Chen", "Stanford University",
                    "November 05, 2024", "9:00 AM - 12:00 PM (PST)", "Rp210.000", "Terjual Habis", "TEKNOLOGI", 5, false)
    );

    public SeminarPeserta() {
        setTitle("Eventix - Katalog Seminar");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new org.example.component.ParticipantSidebar("Seminar"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new org.example.component.Header("Seminar"), BorderLayout.NORTH);

        pageHost.setOpaque(false);
        pageHost.add(scroll(createCatalogPage(), 16), CATALOG);
        pageHost.add(scroll(createDetailPage(), 16), DETAIL);
        pageHost.add(scroll(createRegistrationStepOne(), 16), STEP_ONE);
        pageHost.add(scroll(createPaymentStep(), 16), STEP_TWO);
        pageHost.add(createSuccessPage(), SUCCESS);
        main.add(pageHost, BorderLayout.CENTER);
        return main;
    }

    private JScrollPane scroll(JPanel content, int increment) {
        JScrollPane scrollPane = new JScrollPane(new ScrollableHost(content));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(increment);
        return scrollPane;
    }

    /**
     * Membungkus konten halaman agar lebarnya selalu ikut mengikuti lebar
     * viewport JScrollPane (hanya bisa scroll vertikal, tidak horizontal).
     * Tanpa ini, JPanel biasa akan tetap memakai preferred width-nya sendiri,
     * sehingga jika lebih lebar dari area yang tersedia, sisanya akan
     * "terpotong" / tidak terlihat ke arah kanan alih-alih menyesuaikan diri.
     */
    private static class ScrollableHost extends JPanel implements Scrollable {
        private ScrollableHost(JComponent content) {
            setOpaque(false);
            setLayout(new BorderLayout());
            add(content, BorderLayout.CENTER);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL ? visibleRect.height : visibleRect.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private JPanel createCatalogPage() {
        JPanel page = pagePanel(30, 30, 30, 30);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        JPanel titleText = new JPanel();
        titleText.setOpaque(false);
        titleText.setLayout(new BoxLayout(titleText, BoxLayout.Y_AXIS));
        titleText.add(label("Katalog Seminar", Font.BOLD, 31, TEXT));
        titleText.add(Box.createVerticalStrut(5));
        titleText.add(label("Jelajahi dan daftarlah untuk sesi akademik serta workshop profesional mendatang.", Font.PLAIN, 14, MUTED));
        titleRow.add(titleText, BorderLayout.WEST);
        titleRow.add(outlineButton("Filter", icon("images/Icon/Dashboard/Panitia/Filter_Icon.svg", 16, 16)), BorderLayout.EAST);
        page.add(titleRow);
        page.add(Box.createVerticalStrut(18));
        page.add(createFilterBar());
        page.add(Box.createVerticalStrut(32));
        page.add(createSeminarGrid());
        page.add(Box.createVerticalStrut(32));
        page.add(createPagination());
        return page;
    }

    private JPanel createFilterBar() {
        JPanel bar = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        bar.setLayout(new GridBagLayout());
        bar.setBorder(new EmptyBorder(16, 18, 16, 18));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 86));

        addFilter(bar, "Kategori", "All Categories", 0, 1.1);
        addFilter(bar, "Kisaran harga", "Any Price", 1, 1.0);
        addFilter(bar, "Rentang Tanggal", "mm/dd/yyyy", 2, 1.05);
        bar.add(createAttendanceToggle(), gbc(3, 1.45));
        JLabel result = multiline("Menampilkan 6 dari 48\nseminar", Font.PLAIN, 13, MUTED);
        result.setHorizontalAlignment(SwingConstants.RIGHT);
        bar.add(result, gbc(4, 1.0));
        return bar;
    }

    private void addFilter(JPanel parent, String title, String value, int x, double weight) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.add(label(title, Font.BOLD, 12, MUTED));
        JTextField input = new JTextField(value);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        input.setForeground(TEXT);
        input.setBackground(Color.WHITE);
        input.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(7, 14, 7, 14)));
        input.setMaximumSize(new Dimension(158, 32));
        wrap.add(Box.createVerticalStrut(5));
        wrap.add(input);
        parent.add(wrap, gbc(x, weight));
    }

    private JPanel createAttendanceToggle() {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.add(label("Kehadiran", Font.BOLD, 12, MUTED));
        JPanel toggle = new JPanel(new GridLayout(1, 3, 0, 0));
        toggle.setOpaque(true);
        toggle.setBackground(SOFT_BLUE);
        toggle.setBorder(new EmptyBorder(4, 4, 4, 4));
        toggle.setMaximumSize(new Dimension(204, 34));
        toggle.add(toggleLabel("All", true));
        toggle.add(toggleLabel("Online", false));
        toggle.add(toggleLabel("Offline", false));
        wrap.add(Box.createVerticalStrut(5));
        wrap.add(toggle);
        return wrap;
    }

    private JLabel toggleLabel(String text, boolean active) {
        JLabel label = label(text, Font.BOLD, 12, active ? RED : MUTED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(active ? Color.WHITE : SOFT_BLUE);
        return label;
    }

    private JPanel createSeminarGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 32, 32));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1056));
        for (SeminarItem item : seminars) {
            grid.add(createCatalogCard(item));
        }
        return grid;
    }

    private JPanel createCatalogCard(SeminarItem item) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(292, 512));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPage(DETAIL);
            }
        });

        JPanel image = new ImagePlaceholder();
        image.setPreferredSize(new Dimension(0, 174));
        image.setLayout(new BorderLayout());
        JPanel chipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 18));
        chipPanel.setOpaque(false);
        chipPanel.add(chip(item.category()));
        image.add(chipPanel, BorderLayout.NORTH);
        card.add(image, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(24, 25, 24, 25));
        body.add(multiline(item.title(), Font.BOLD, 20, TEXT));
        body.add(Box.createVerticalStrut(12));
        body.add(label(item.speaker(), Font.BOLD, 12, TEXT));
        body.add(label(item.organization(), Font.PLAIN, 11, MUTED));
        body.add(Box.createVerticalStrut(16));
        body.add(meta(item.date(), "images/Icon/Dashboard/Panitia/Seminar/Date_Icon.svg"));
        body.add(Box.createVerticalStrut(8));
        body.add(meta(item.time(), "images/Icon/Dashboard/Help_Icon.svg"));
        body.add(Box.createVerticalStrut(12));
        body.add(separator());
        body.add(Box.createVerticalStrut(13));
        body.add(priceRow(item));
        body.add(Box.createVerticalGlue());
        JButton action = item.open() ? filledWide(item.price().equals("Free") ? "Claim Ticket" : "Daftar Sekarang") : disabledWide("Waitlist Only");
        action.addActionListener(e -> showPage(DETAIL));
        body.add(action);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JPanel priceRow(SeminarItem item) {
        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(label("REGISTRATION", Font.PLAIN, 9, MUTED));
        left.add(label(item.price(), Font.BOLD, 19, RED));
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JLabel title = label("TERSEDIANYA", Font.PLAIN, 9, MUTED);
        JLabel seats = label(item.seats(), Font.BOLD, 12, item.open() ? BLUE : RED);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        seats.setAlignmentX(Component.RIGHT_ALIGNMENT);
        right.add(title);
        right.add(seats);
        row.add(left);
        row.add(right);
        return row;
    }

    private JPanel createPagination() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        row.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER));
        row.add(label("< Previous", Font.PLAIN, 13, MUTED), BorderLayout.WEST);
        JPanel pages = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 22));
        pages.setOpaque(false);
        JLabel one = label("1", Font.BOLD, 14, Color.WHITE);
        one.setHorizontalAlignment(SwingConstants.CENTER);
        one.setOpaque(true);
        one.setBackground(RED);
        one.setPreferredSize(new Dimension(40, 40));
        pages.add(one);
        pages.add(label("2", Font.PLAIN, 14, TEXT));
        pages.add(label("3", Font.PLAIN, 14, TEXT));
        pages.add(label("...", Font.PLAIN, 14, TEXT));
        pages.add(label("8", Font.PLAIN, 14, TEXT));
        row.add(pages, BorderLayout.CENTER);
        row.add(label("Next >", Font.PLAIN, 13, MUTED), BorderLayout.EAST);
        return row;
    }

    private JPanel createDetailPage() {
        JPanel page = pagePanel(24, 30, 86, 30);
        page.setLayout(new BorderLayout(22, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(createHero());
        left.add(Box.createVerticalStrut(22));
        left.add(createInfoTiles());
        left.add(Box.createVerticalStrut(30));
        left.add(createOverview());
        left.add(Box.createVerticalStrut(22));
        left.add(createSpeaker());
        left.add(Box.createVerticalStrut(22));
        left.add(createSchedule());

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(270, 0));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(createRequirementCard());
        right.add(Box.createVerticalStrut(24));
        right.add(label("Seminar Terkait", Font.BOLD, 14, MUTED));
        right.add(Box.createVerticalStrut(14));
        right.add(relatedCard("Quantum Computing Foundations", "COMPUTER SCIENCE", "Nov 12, 2024", 5));
        right.add(Box.createVerticalStrut(14));
        right.add(relatedCard("Bioinformatics for Drug Discovery", "LIFE SCIENCES", "Dec 05, 2024", 1));
        right.add(Box.createVerticalStrut(14));
        right.add(relatedCard("Deep Learning Workshop", "DATA SCIENCE", "Jan 15, 2025", 4));

        page.add(left, BorderLayout.CENTER);
        page.add(right, BorderLayout.EAST);
        page.add(createDetailBottomBar(), BorderLayout.SOUTH);
        return page;
    }

    private JPanel createHero() {
        JPanel hero = new BorderedPanel(10, BLUE, BLUE);
        hero.setPreferredSize(new Dimension(0, 200));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        hero.setLayout(new BorderLayout());
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(26, 30, 26, 30));
        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chips.setOpaque(false);
        chips.add(chip("ADVANCED SERIES"));
        chips.add(softChip("ACADEMIC CREDIT AVAILABLE"));
        content.add(chips);
        content.add(Box.createVerticalStrut(16));
        content.add(wrapped("The Future of Generative AI in Academic Research & Ethics", Font.BOLD, 26, Color.WHITE, 560));
        content.add(Box.createVerticalStrut(8));
        content.add(wrapped("Explore the transformative potential and ethical boundaries of LLMs within higher education frameworks.", Font.PLAIN, 14, new Color(220, 224, 235), 560));
        hero.add(content, BorderLayout.CENTER);
        return hero;
    }

    private JPanel createInfoTiles() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 14));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 214));
        grid.add(infoTile("TANGGAL", "Oct 24, 2024", "images/Icon/Dashboard/Panitia/Seminar/Date_Icon.svg"));
        grid.add(infoTile("WAKTU", "09:00 AM -\n04:00 PM", null));
        grid.add(infoTile("LOKASI", "Grand Hall,\nBlock A", "images/Icon/Dashboard/Panitia/Seminar/Location_Icon.svg"));
        grid.add(infoTile("HARGA", "Rp150.000 / Kursi", "images/Icon/Dashboard/Panitia/Seminar/AddEdit/Money_Icon.svg"));
        return grid;
    }

    private JPanel infoTile(String title, String value, String iconPath) {
        JPanel tile = new BorderedPanel(8, Color.WHITE, CARD_BORDER);
        tile.setLayout(new BorderLayout(12, 0));
        tile.setBorder(new EmptyBorder(22, 20, 22, 20));
        if (iconPath != null) {
            tile.add(new IconBox(iconPath, new Color(255, 216, 224)), BorderLayout.WEST);
        }
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label(title, Font.BOLD, 9, MUTED));
        text.add(multiline(value, Font.BOLD, 14, TEXT));
        tile.add(text, BorderLayout.CENTER);
        return tile;
    }

    private JPanel createOverview() {
        JPanel card = contentCard();
        card.add(sectionTitle("Seminar Overview"));
        card.add(Box.createVerticalStrut(16));
        card.add(wrapped("This intensive one-day seminar addresses the rapid integration of Large Language Models (LLMs) into the academic workflow. From automating literature reviews to the nuances of AI-assisted data analysis, we will delve into how these technologies are reshaping the landscape of research.<br><br>Participants will engage with leading experts to discuss the critical ethical considerations, including academic integrity, bias in generative outputs, and the shifting definition of original contribution in a post-AI era.", Font.PLAIN, 15, MUTED, 560));
        card.add(Box.createVerticalStrut(18));
        card.add(label("Key Takeaways:", Font.BOLD, 14, TEXT));
        card.add(Box.createVerticalStrut(10));
        JPanel points = new JPanel(new GridLayout(2, 2, 18, 10));
        points.setOpaque(false);
        points.add(label("Practical frameworks for ethical AI usage.", Font.PLAIN, 13, MUTED));
        points.add(label("Hands-on evaluation of LLM-driven tools.", Font.PLAIN, 13, MUTED));
        points.add(label("Understanding institutional policy shifts.", Font.PLAIN, 13, MUTED));
        points.add(label("Strategies for transparent AI publication.", Font.PLAIN, 13, MUTED));
        card.add(points);
        return card;
    }

    private JPanel createSpeaker() {
        JPanel card = contentCard();
        card.add(label("Pembaca Terhormat", Font.BOLD, 23, TEXT));
        card.add(Box.createVerticalStrut(26));
        JPanel row = new JPanel(new BorderLayout(24, 0));
        row.setOpaque(false);
        JPanel photo = new ImagePlaceholder();
        photo.setPreferredSize(new Dimension(112, 116));
        photo.setMaximumSize(new Dimension(112, 116));
        row.add(photo, BorderLayout.WEST);
        JPanel bio = new JPanel();
        bio.setOpaque(false);
        bio.setLayout(new BoxLayout(bio, BoxLayout.Y_AXIS));
        bio.add(label("Dr. Julian Vance", Font.BOLD, 24, TEXT));
        bio.add(label("DIRECTOR OF AI ETHICS LAB, STANMORE", Font.BOLD, 13, RED));
        bio.add(Box.createVerticalStrut(18));
        bio.add(wrapped("Dr. Vance is a pioneer in the field of computational ethics. With over 150 peer-reviewed publications, he has served as a key advisor to global tech consortia.", Font.PLAIN, 15, MUTED, 460));
        bio.add(Box.createVerticalStrut(16));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttons.setOpaque(false);
        buttons.add(softButton("Portfolio"));
        buttons.add(softButton("Connect"));
        bio.add(buttons);
        row.add(bio, BorderLayout.CENTER);
        card.add(row);
        return card;
    }

    private JPanel createSchedule() {
        JPanel card = contentCard();
        card.add(label("Jadwal Event", Font.BOLD, 24, TEXT));
        card.add(Box.createVerticalStrut(26));
        card.add(scheduleRow("Registration & Opening Plenary", "Main Lobby & Auditorium. Welcoming remarks by the Dean of Science.", "09:00 AM", true));
        card.add(scheduleRow("Session 1: LLMs in Literature Review", "Deep dive into automated citation tracking and synthesis algorithms.", "10:30 AM", false));
        card.add(scheduleRow("Networking Lunch", "Complimentary buffet and breakout discussion groups.", "12:30 PM", false));
        card.add(scheduleRow("Session 2: Ethical Governance", "Policy design workshop and case study analysis.", "02:00 PM", false));
        return card;
    }

    private JPanel scheduleRow(String title, String desc, String time, boolean active) {
        JPanel row = new JPanel(new BorderLayout(16, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 12, 0));
        row.add(new TimelineDot(active), BorderLayout.WEST);
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(label(title, Font.BOLD, 14, TEXT));
        text.add(label(desc, Font.PLAIN, 12, MUTED));
        row.add(text, BorderLayout.CENTER);
        JLabel timeLabel = label(time, Font.BOLD, 14, active ? RED : MUTED);
        timeLabel.setOpaque(true);
        timeLabel.setBackground(active ? new Color(255, 224, 232) : SOFT_BLUE);
        timeLabel.setBorder(new EmptyBorder(7, 9, 7, 9));
        row.add(timeLabel, BorderLayout.EAST);
        return row;
    }

    private JPanel createDetailBottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(true);
        bar.setBackground(Color.WHITE);
        bar.setBorder(new EmptyBorder(16, 0, 0, 0));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        left.add(multiline("TERSEDIA KURSI TERBATAS\n<b>Rp150.000</b>  |  Tersisa 12 kursi dari 150", Font.PLAIN, 12, TEXT));
        bar.add(left, BorderLayout.WEST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        actions.setOpaque(false);
        actions.add(outlineButton("Simpan untuk Nanti", null));
        JButton register = filledButton("Daftar Sekarang ->", 190, 52);
        register.addActionListener(e -> showPage(STEP_ONE));
        actions.add(register);
        bar.add(actions, BorderLayout.EAST);
        return bar;
    }

    private JPanel createRequirementCard() {
        JPanel card = contentCard();
        card.setPreferredSize(new Dimension(270, 204));
        card.setMaximumSize(new Dimension(270, 204));
        card.add(label("Prasyarat", Font.BOLD, 15, TEXT));
        card.add(Box.createVerticalStrut(16));
        card.add(label("Laptop pribadi diperlukan untuk Workshop.", Font.PLAIN, 12, TEXT));
        card.add(Box.createVerticalStrut(14));
        card.add(label("Pemahaman dasar mengenai konsep AI/ML.", Font.PLAIN, 12, TEXT));
        card.add(Box.createVerticalStrut(14));
        card.add(label("Membaca materi disediakan setelah pendaftaran.", Font.PLAIN, 12, TEXT));
        return card;
    }

    private JPanel relatedCard(String title, String category, String date, int visual) {
        JPanel card = new BorderedPanel(8, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));
        card.setMaximumSize(new Dimension(270, 174));
        JPanel image = new ImagePlaceholder();
        image.setPreferredSize(new Dimension(242, 78));
        image.setMaximumSize(new Dimension(242, 78));
        card.add(image);
        card.add(Box.createVerticalStrut(9));
        card.add(label(category, Font.BOLD, 9, RED));
        card.add(label(title, Font.BOLD, 13, TEXT));
        card.add(label(date, Font.PLAIN, 11, MUTED));
        return card;
    }

    private JPanel createRegistrationStepOne() {
        JPanel page = pagePanel(36, 30, 40, 30);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.add(breadcrumb("Seminars > International Tech Expo 2024 > Registrasi"));
        page.add(Box.createVerticalStrut(24));
        page.add(stepper(1));
        page.add(Box.createVerticalStrut(34));
        JPanel row = new JPanel(new BorderLayout(24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(registrationForm(), BorderLayout.CENTER);
        row.add(orderSideOne(), BorderLayout.EAST);
        page.add(row);
        return page;
    }

    private JPanel registrationForm() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(36, 32, 32, 32));
        card.setPreferredSize(new Dimension(630, 504));
        card.add(label("Informasi Peserta", Font.BOLD, 25, TEXT));
        card.add(Box.createVerticalStrut(26));
        card.add(label("Lengkapi data diri Anda untuk pendaftaran seminar.", Font.PLAIN, 14, MUTED));
        card.add(Box.createVerticalStrut(28));
        card.add(formField("Nama Lengkap", "Alex Rivera"));
        JPanel two = new JPanel(new GridLayout(1, 2, 16, 0));
        two.setOpaque(false);
        two.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        two.add(formField("Email", "john.doe@university.ac.id"));
        two.add(formField("Nomor Telepon", "+62 812-3456-7890"));
        card.add(two);
        card.add(formField("Instansi / Organisasi", "Masukkan nama instansi"));
        card.add(Box.createVerticalStrut(22));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton next = filledButton("Lanjutkan ->", 150, 48);
        next.addActionListener(e -> showPage(STEP_TWO));
        actions.add(next);
        card.add(actions);
        return card;
    }

    private JPanel orderSideOne() {
        JPanel side = new JPanel();
        side.setOpaque(false);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(306, 0));
        side.add(summarySeminarCard());
        side.add(Box.createVerticalStrut(24));
        side.add(orderSummary(false));
        return side;
    }

    private JPanel summarySeminarCard() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(306, 266));
        JPanel img = new ImagePlaceholder();
        img.setPreferredSize(new Dimension(306, 160));
        img.setMaximumSize(new Dimension(306, 160));
        card.add(img);
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 20, 24));
        body.add(label("International Tech Expo", Font.BOLD, 18, TEXT));
        body.add(Box.createVerticalStrut(18));
        body.add(meta("24 Agustus 2024", "images/Icon/Dashboard/Panitia/Seminar/Date_Icon.svg"));
        body.add(Box.createVerticalStrut(16));
        body.add(meta("Auditorium Pusat, Gedung A", "images/Icon/Dashboard/Panitia/Seminar/Location_Icon.svg"));
        card.add(body);
        return card;
    }

    private JPanel orderSummary(boolean payment) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setMaximumSize(new Dimension(306, payment ? 526 : 350));
        card.add(label("RINGKASAN PESANAN", Font.BOLD, 12, MUTED));
        card.add(Box.createVerticalStrut(26));
        card.add(summaryLine("Standard Pass", "Rp 150.000", true));
        card.add(summaryLine("Biaya Layanan", payment ? "Rp 15.000" : "Rp 5.000", false));
        card.add(Box.createVerticalStrut(18));
        card.add(separator());
        card.add(Box.createVerticalStrut(24));
        card.add(summaryLine("Total" + (payment ? " Bayar" : ""), payment ? "Rp 155.428" : "Rp 155.000", true, 25));
        if (payment) {
            card.add(Box.createVerticalStrut(22));
            card.add(formInput("Masukkan kode..."));
            card.add(Box.createVerticalStrut(16));
            JButton pay = filledButton("Bayar Sekarang", 252, 56);
            pay.addActionListener(e -> showPage(SUCCESS));
            card.add(pay);
            card.add(Box.createVerticalStrut(16));
            JLabel safe = multiline("Keamanan transaksi terjamin oleh sistem\nEventix.", Font.PLAIN, 11, MUTED);
            safe.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(safe);
        } else {
            card.add(Box.createVerticalStrut(28));
            JPanel info = new JPanel();
            info.setOpaque(true);
            info.setBackground(SOFT_BLUE);
            info.setBorder(new EmptyBorder(16, 18, 16, 18));
            info.add(multiline("E-Tiket akan dikirimkan ke email\nAnda segera setelah pembayaran\nberhasil diverifikasi oleh sistem.", Font.PLAIN, 11, BLUE));
            card.add(info);
        }
        return card;
    }

    private JPanel createPaymentStep() {
        JPanel page = pagePanel(36, 30, 40, 30);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.add(breadcrumb("Seminars > International Tech Expo 2024 > Registration"));
        page.add(Box.createVerticalStrut(24));
        page.add(stepper(2));
        page.add(Box.createVerticalStrut(28));
        JPanel row = new JPanel(new BorderLayout(24, 0));
        row.setOpaque(false);
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(paymentMethodCard());
        left.add(Box.createVerticalStrut(24));
        left.add(paymentDetailCard());
        row.add(left, BorderLayout.CENTER);
        row.add(paymentSummaryCard(), BorderLayout.EAST);
        page.add(row);
        return page;
    }

    private JPanel paymentMethodCard() {
        JPanel card = contentCard();
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));
        card.add(label("Pilih Metode Pembayaran", Font.BOLD, 20, TEXT));
        card.add(Box.createVerticalStrut(14));
        card.add(paymentOptions("VIRTUAL ACCOUNT", "BCA Virtual Account", "Mandiri Virtual Account", true));
        card.add(Box.createVerticalStrut(16));
        card.add(paymentOptions("E-WALLET", "GoPay", "OVO", false));
        card.add(Box.createVerticalStrut(16));
        card.add(paymentOptions("LAINNYA", "Kartu Kredit", "Transfer Manual", false));
        return card;
    }

    private JPanel paymentOptions(String group, String first, String second, boolean selected) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.add(label(group, Font.BOLD, 12, MUTED));
        wrap.add(Box.createVerticalStrut(8));
        JPanel row = new JPanel(new GridLayout(1, 2, 16, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        row.add(paymentBox(first, selected));
        row.add(paymentBox(second, false));
        wrap.add(row);
        return wrap;
    }

    private JPanel paymentBox(String name, boolean selected) {
        JPanel box = new BorderedPanel(8, Color.WHITE, CARD_BORDER);
        box.setLayout(new BorderLayout());
        box.setBorder(new EmptyBorder(18, 20, 18, 20));
        box.add(label(name, Font.BOLD, 14, TEXT), BorderLayout.WEST);
        if (selected) {
            box.add(label("✓", Font.BOLD, 20, RED), BorderLayout.EAST);
        }
        return box;
    }

    private JPanel paymentDetailCard() {
        JPanel card = contentCard();
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 365));
        JPanel title = new JPanel(new BorderLayout());
        title.setOpaque(false);
        title.add(label("Rincian Pembayaran", Font.BOLD, 20, TEXT), BorderLayout.WEST);
        title.add(softChip("BCA VIRTUAL ACCOUNT"), BorderLayout.EAST);
        card.add(title);
        card.add(Box.createVerticalStrut(18));
        JPanel box = new JPanel();
        box.setOpaque(true);
        box.setBackground(SOFT_BLUE);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(new EmptyBorder(24, 26, 24, 26));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        box.add(label("NOMOR VIRTUAL ACCOUNT", Font.BOLD, 12, MUTED));
        box.add(Box.createVerticalStrut(8));
        box.add(label("8802 0812 3456 7890", Font.BOLD, 26, TEXT));
        box.add(Box.createVerticalStrut(22));
        box.add(separator());
        box.add(Box.createVerticalStrut(16));
        box.add(summaryLine("NAMA MERCHANT\nUNIVERSITAS EVENTIX INDONESIA", "BATAS PEMBAYARAN\n23 Jan 2024, 23:59 WIB", true));
        card.add(box);
        card.add(Box.createVerticalStrut(16));
        JPanel info = new BorderedPanel(8, new Color(255, 246, 248), CARD_BORDER);
        info.setLayout(new BorderLayout());
        info.setBorder(new EmptyBorder(16, 18, 16, 18));
        info.add(multiline("Informasi Penting\nPembayaran melalui BCA Virtual Account tidak memerlukan konfirmasi manual.\nStatus registrasi akan otomatis diperbarui setelah pembayaran berhasil.", Font.BOLD, 12, RED), BorderLayout.CENTER);
        card.add(info);
        return card;
    }

    private JPanel paymentSummaryCard() {
        JPanel card = orderSummary(true);
        card.setPreferredSize(new Dimension(306, 526));
        return card;
    }

    private JPanel createSuccessPage() {
        JPanel page = pagePanel(30, 30, 30, 30);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.add(stepper(3));
        page.add(Box.createVerticalStrut(34));
        JLabel check = label("✓", Font.BOLD, 48, Color.WHITE);
        check.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel checkWrap = new SuccessCircle();
        checkWrap.setLayout(new GridBagLayout());
        checkWrap.add(check);
        checkWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        page.add(checkWrap);
        page.add(Box.createVerticalStrut(24));
        JLabel title = label("Pendaftaran Berhasil!", Font.BOLD, 31, TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        page.add(title);
        page.add(Box.createVerticalStrut(10));
        JLabel subtitle = multiline("E-tiket telah dikirimkan ke email Anda. Silakan simpan detail\npendaftaran di bawah ini untuk referensi.", Font.PLAIN, 14, MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        page.add(subtitle);
        page.add(Box.createVerticalStrut(34));
        page.add(successDetails());
        page.add(Box.createVerticalStrut(30));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        actions.setOpaque(false);
        JButton download = filledButton("Download E-Ticket", 214, 56);
        JButton dashboard = outlineButton("Lihat Dashboard", icon("images/Icon/Dashboard/Dashboard_Icon.svg", 15, 15));
        dashboard.addActionListener(e -> {
            new DashboardPeserta().setVisible(true);
            dispose();
        });
        actions.add(download);
        actions.add(dashboard);
        page.add(actions);
        page.add(Box.createVerticalStrut(28));
        JLabel help = label("Memerlukan bantuan? Hubungi IT Support di extension 104.", Font.PLAIN, 12, MUTED);
        help.setAlignmentX(Component.CENTER_ALIGNMENT);
        page.add(help);
        return page;
    }

    private JPanel successDetails() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 24, 24, 24));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(672, 266));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(label("RINCIAN PENDAFTARAN", Font.BOLD, 12, MUTED), BorderLayout.WEST);
        top.add(chip("RE-2024-X92B"), BorderLayout.EAST);
        card.add(top);
        card.add(Box.createVerticalStrut(26));
        JPanel grid = new JPanel(new GridLayout(2, 2, 46, 26));
        grid.setOpaque(false);
        grid.add(detailField("Nama Peserta", "Dr. Hendra Wijaya, M.Sc."));
        grid.add(detailField("Judul Seminar", "Modern Java Architecture in\nEnterprise Systems"));
        grid.add(detailField("Tanggal Seminar", "15 Oktober 2024, 09:00 WIB"));
        grid.add(detailField("Lokasi", "Auditorium Utama, Gedung\nRektorat"));
        card.add(grid);
        return card;
    }

    private JPanel detailField(String title, String value) {
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.Y_AXIS));
        field.add(label(title, Font.PLAIN, 12, MUTED));
        field.add(Box.createVerticalStrut(8));
        field.add(multiline(value, Font.BOLD, 18, TEXT));
        return field;
    }

    private JPanel stepper(int current) {
        JPanel stepper = new JPanel(new GridBagLayout());
        stepper.setOpaque(false);
        stepper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        stepper.setAlignmentX(Component.LEFT_ALIGNMENT);
        addStep(stepper, 1, current, "Data Peserta", 0);
        addStepLine(stepper, current >= 2, 1);
        addStep(stepper, 2, current, "Pembayaran", 2);
        addStepLine(stepper, current >= 3, 3);
        addStep(stepper, 3, current, "Konfirmasi", 4);
        return stepper;
    }

    private void addStep(JPanel parent, int number, int current, String title, int x) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        CircleStep circle = new CircleStep(number, current);
        circle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label = label(current > number ? "✓" : String.valueOf(number), Font.BOLD, 16, Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        circle.setLayout(new GridBagLayout());
        circle.add(label);
        wrap.add(circle);
        wrap.add(Box.createVerticalStrut(8));
        JLabel text = label(title, Font.BOLD, 12, current >= number ? RED : MUTED);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrap.add(text);
        parent.add(wrap, stepGbc(x, 0.15));
    }

    private void addStepLine(JPanel parent, boolean active, int x) {
        JPanel line = new JPanel();
        line.setOpaque(true);
        line.setBackground(active ? RED : CARD_BORDER);
        line.setPreferredSize(new Dimension(320, 2));
        parent.add(line, stepGbc(x, 0.85));
    }

    private GridBagConstraints stepGbc(int x, double weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
    }

    private JPanel formField(String title, String value) {
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.Y_AXIS));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
        field.add(label(title, Font.BOLD, 12, MUTED));
        field.add(Box.createVerticalStrut(8));
        field.add(formInput(value));
        return field;
    }

    private JTextField formInput(String value) {
        JTextField input = new JTextField(value);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        input.setForeground(TEXT);
        input.setBackground(new Color(246, 248, 255));
        input.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(10, 16, 10, 16)));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        input.setPreferredSize(new Dimension(0, 44));
        return input;
    }

    private JPanel summaryLine(String left, String right, boolean bold) {
        return summaryLine(left, right, bold, bold ? 14 : 13);
    }

    private JPanel summaryLine(String left, String right, boolean bold, int rightSize) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        row.add(multiline(left, bold ? Font.BOLD : Font.PLAIN, bold ? 14 : 13, TEXT), BorderLayout.WEST);
        row.add(multiline(right, bold ? Font.BOLD : Font.PLAIN, rightSize, rightSize > 18 ? RED : TEXT), BorderLayout.EAST);
        return row;
    }

    private JLabel breadcrumb(String text) {
        JLabel label = label(text, Font.BOLD, 12, text.contains("Registr") ? RED : MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel pagePanel(int top, int left, int bottom, int right) {
        JPanel page = new JPanel();
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(top, left, bottom, right));
        return page;
    }

    private JPanel contentCard() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(26, 30, 26, 30));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return card;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = label(text, Font.BOLD, 24, TEXT);
        label.setBorder(new EmptyBorder(0, 14, 0, 0));
        return label;
    }

    private JPanel meta(String text, String iconPath) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);
        if (iconPath != null) {
            row.add(new JLabel(icon(iconPath, 15, 15)));
        }
        row.add(label(text, Font.PLAIN, 13, MUTED));
        return row;
    }

    private JLabel chip(String text) {
        JLabel chip = label(text, Font.BOLD, 10, Color.WHITE);
        chip.setOpaque(true);
        chip.setBackground(RED);
        chip.setBorder(new EmptyBorder(5, 16, 5, 16));
        return chip;
    }

    private JLabel softChip(String text) {
        JLabel chip = label(text, Font.BOLD, 10, BLUE);
        chip.setOpaque(true);
        chip.setBackground(new Color(222, 229, 255));
        chip.setBorder(new EmptyBorder(5, 16, 5, 16));
        return chip;
    }

    private JButton filledWide(String text) {
        return filledButton(text, 248, 48);
    }

    private JButton disabledWide(String text) {
        JButton button = filledButton(text, 248, 48);
        button.setBackground(new Color(232, 187, 192));
        button.setForeground(MUTED);
        button.setEnabled(false);
        return button;
    }

    private JButton filledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(text.contains("Daftar") ? RED : BLUE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        return button;
    }

    private JButton outlineButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(BLUE);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setIconTextGap(8);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(12, 22, 12, 22)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton softButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(MUTED);
        button.setBackground(SOFT_BLUE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private JSeparator separator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(CARD_BORDER);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }

    private GridBagConstraints gbc(int x, double weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 0;
        gbc.weightx = weight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 18);
        return gbc;
    }

    private void showPage(String name) {
        pages.show(pageHost, name);
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

    /**
     * Sama seperti multiline(), tetapi teks akan membungkus (word-wrap)
     * secara otomatis mengikuti lebar maksimum yang diberikan, bukan
     * mengandalkan tebakan patah baris manual ("\n") yang mudah meleset
     * saat lebar kolom berubah.
     */
    private JLabel wrapped(String text, int style, int size, Color color, int maxWidthPx) {
        String html = "<html><div style='width:" + maxWidthPx + "px;'>" + text.replace("\n", "<br>") + "</div></html>";
        return label(html, style, size, color);
    }

    private Icon icon(String path, int width, int height) {
        return new FlatSVGIcon(path, width, height);
    }

    private Icon dashboardIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/" + fileName, width, height);
    }
    /**
     * Placeholder polos untuk slot gambar seminar/foto pembicara.
     * Menggantikan gambar yang sebelumnya dibuat manual (gradient/garis acak)
     * karena akan diganti dengan gambar asli dari backend saat integrasi.
     */
    private static class ImagePlaceholder extends JPanel {
        private ImagePlaceholder() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SOFT_BLUE);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
            g2.setColor(CARD_BORDER);
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, 10, 10));
            int size = Math.max(18, Math.min(28, Math.min(getWidth(), getHeight()) / 3));
            new FlatSVGIcon("images/Icon/Dashboard/Panitia/Seminar/AddEdit/ImageIcon.svg", size, size)
                    .paintIcon(this, g2, (getWidth() - size) / 2, (getHeight() - size) / 2);
            g2.dispose();
        }
    }

    private static class TimelineDot extends JPanel {
        private final boolean active;

        private TimelineDot(boolean active) {
            this.active = active;
            setPreferredSize(new Dimension(20, 42));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(active ? RED : BLUE);
            g2.fillOval(3, 8, 14, 14);
            g2.dispose();
        }
    }

    private static class CircleStep extends JPanel {
        private final int number;
        private final int current;

        private CircleStep(int number, int current) {
            this.number = number;
            this.current = current;
            setOpaque(false);
            setPreferredSize(new Dimension(42, 42));
            setMaximumSize(new Dimension(42, 42));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(current >= number ? RED : new Color(226, 232, 246));
            g2.fillOval(0, 0, 42, 42);
            g2.setColor(CARD_BORDER);
            g2.drawOval(0, 0, 41, 41);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class SuccessCircle extends JPanel {
        private SuccessCircle() {
            setOpaque(false);
            setPreferredSize(new Dimension(96, 96));
            setMaximumSize(new Dimension(96, 96));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(220, 252, 231));
            g2.fillOval(0, 0, 96, 96);
            g2.setColor(new Color(22, 163, 74));
            g2.fillOval(24, 24, 48, 48);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class IconBox extends JPanel {
        private final String iconPath;
        private final Color background;

        private IconBox(String iconPath, Color background) {
            this.iconPath = iconPath;
            this.background = background;
            setPreferredSize(new Dimension(44, 44));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillRoundRect(0, 0, 44, 44, 8, 8);
            new FlatSVGIcon(iconPath, 18, 18).paintIcon(this, g2, 13, 13);
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

    private record SeminarItem(String title, String speaker, String organization, String date, String time,
                               String price, String seats, String category, int visual, boolean open) {
    }
}
