package org.example.view;

import model.Seminar;
import service.SeminarService;
import dao.SeminarDAO;
import dao.AuditLogDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LandingPage extends JFrame {

    // --- Palet Warna & Font ---
    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color FOOTER_BG = new Color(240, 243, 255);
    private static final Color TEXT_DARK = new Color(17, 28, 45);
    private static final Color TEXT_MUTED = new Color(92, 63, 64);
    private static final Color LINK_COLOR = new Color(55, 68, 117);
    private static final Color RED_MAIN = new Color(184, 0, 53);
    private static final Color BORDER_COLOR = new Color(229, 189, 190);
    private static final Color BLUE = new Color(79, 92, 142);
    private static final String PLACEHOLDER_TEXT = "Search seminars, speakers, or topics...";

    // Komponen Dinamis & Layout
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField searchInput;
    private JPopupMenu suggestionMenu;
    private JPanel bentoGrid;
    private JPanel categoryContainer;

    // Navigasi Atas Dinamis
    private JLabel navHome;
    private JLabel navSeminars;

    // Status Filter, Data & Saklar Pencarian
    private String activeCategory = "All Seminars";
    private String currentSearchQuery = "";
    private boolean isProgrammaticUpdate = false;
    private List<DummySeminar> allSeminars;

    public LandingPage() {
        setTitle("Eventix - The Ultimate Seminar Management System");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Memuat data dari Database (atau fallback ke Dummy)
        loadDataFromDatabase();

        initComponents();
    }

    // =========================================================================
    // 👉 INTEGRASI DATABASE BACKEND
    // =========================================================================
    private void loadDataFromDatabase() {
        allSeminars = new ArrayList<>();
        try {
            // Instansiasi Service beserta DAO-nya
            SeminarService seminarService = new SeminarService(new SeminarDAO(), new AuditLogDAO());

            // Mengambil data seminar yang berstatus DIBUKA
            List<Seminar> listDb = seminarService.getDibuka();

            if (listDb != null && !listDb.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

                for (Seminar s : listDb) {
                    String title = s.getJudul();
                    String desc = s.getDeskripsi() != null ? s.getDeskripsi() : "Tidak ada deskripsi tersedia.";

                    // Asumsi Mapping ID Kategori ke String untuk UI
                    // Sesuaikan ID ini dengan isi tabel kategori di database kamu
                    String category = "General";
                    Color badgeColor = TEXT_MUTED;

                    if (s.getIdKategori() != null) {
                        switch (s.getIdKategori()) {
                            case 1: category = "Technology"; badgeColor = RED_MAIN; break;
                            case 2: category = "Business"; badgeColor = new Color(192, 38, 211); break;
                            case 3: category = "Arts & Design"; badgeColor = RED_MAIN; break;
                            case 4: category = "Medical"; badgeColor = BLUE; break;
                            default: category = "General"; badgeColor = TEXT_MUTED; break;
                        }
                    }

                    // Format Tanggal dan Lokasi
                    String dateLoc = "";
                    if (s.getTanggalMulai() != null) {
                        dateLoc += s.getTanggalMulai().format(formatter) + " • ";
                    }
                    dateLoc += (s.getLokasi() != null && !s.getLokasi().isEmpty()) ? s.getLokasi() : s.getMode().toString();

                    allSeminars.add(new DummySeminar(title, desc, category, dateLoc, badgeColor));
                }
            } else {
                initDummyDataFallback(); // Fallback jika tabel kosong
            }
        } catch (Exception e) {
            System.err.println("Database belum terhubung. Menggunakan data dummy sementara.");
            initDummyDataFallback(); // Fallback jika XAMPP/MySQL mati
        }
    }

    // Data Cadangan jika Database bermasalah/kosong
    private void initDummyDataFallback() {
        allSeminars = new ArrayList<>();
        allSeminars.add(new DummySeminar("AI & The Future of Machine Learning", "Membahas dampak AI di berbagai industri dan etika kecerdasan buatan.", "Technology", "24 Oct 2026 • Auditorium Utama", RED_MAIN));
        allSeminars.add(new DummySeminar("The Digital Renaissance", "Exploring how NFT culture impacts traditional museum curation.", "Arts & Design", "05 Nov 2026 • Galeri Seni", RED_MAIN));
        allSeminars.add(new DummySeminar("Micro-Biology Workshop", "Advanced methodologies in CRISPR sequence mapping.", "Medical", "12 Nov 2026 • Lab Biomedis", BLUE));
        allSeminars.add(new DummySeminar("Startup Fundraising 101", "Cara mendapatkan pendanaan dari Venture Capital.", "Business", "20 Nov 2026 • Ruang Seminar B", new Color(192, 38, 211)));
        allSeminars.add(new DummySeminar("Cybersecurity in Banking", "Strategi mengamankan data nasabah dari serangan siber.", "Technology", "01 Des 2026 • Hall C", BLUE));
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);

        root.add(createTopNavBar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(PAGE_BG);

        cardPanel.add(createHomeView(), "HOME");
        cardPanel.add(createSeminarsView(), "SEMINARS");

        root.add(cardPanel, BorderLayout.CENTER);
        add(root);

        switchToHome();
    }

    // =========================================================================
    // 1. TOP NAVIGATION BAR
    // =========================================================================
    private JPanel createTopNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        navBar.setPreferredSize(new Dimension(1280, 64));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 32, 12));
        leftPanel.setOpaque(false);

        JLabel logo = label("Eventix", Font.BOLD, 32, RED_MAIN);
        logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { switchToHome(); }
        });
        leftPanel.add(logo);

        JPanel menuLinks = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        menuLinks.setOpaque(false);
        menuLinks.setBorder(new EmptyBorder(8, 0, 0, 0));

        navHome = hoverLabel("Home", RED_MAIN);
        navSeminars = hoverLabel("Seminars", TEXT_MUTED);

        navHome.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { switchToHome(); }
        });
        navSeminars.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                switchToSeminars("", "All Seminars");
            }
        });

        menuLinks.add(navHome);
        menuLinks.add(navSeminars);
        leftPanel.add(menuLinks);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 16));
        rightPanel.setOpaque(false);

        JLabel btnLogin = hoverLabel("Login", TEXT_DARK);
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginForm().setVisible(true);
            }
        });

        JButton btnRegister = new JButton("Sign Up");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(RED_MAIN);
        btnRegister.setPreferredSize(new Dimension(100, 32));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterForm().setVisible(true);
        });

        rightPanel.add(btnLogin);
        rightPanel.add(btnRegister);

        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);
        return navBar;
    }

    private void updateNavbarState(String activeView) {
        if ("HOME".equals(activeView)) {
            navHome.setForeground(RED_MAIN);
            navSeminars.setForeground(TEXT_MUTED);
        } else {
            navHome.setForeground(TEXT_MUTED);
            navSeminars.setForeground(RED_MAIN);
        }
    }

    // =========================================================================
    // 2. HALAMAN 1 (HOME VIEW) - Pencarian & Rekomendasi
    // =========================================================================
    private JScrollPane createHomeView() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(PAGE_BG);

        JPanel hero = new JPanel();
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setOpaque(false);
        hero.setBorder(new EmptyBorder(120, 0, 100, 0));
        hero.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title1 = label("Event, Information Exchange,", Font.BOLD, 54, RED_MAIN);
        JLabel title2 = label("and Experience", Font.BOLD, 54, RED_MAIN);
        title1.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = label("The ultimate platform for discovering and managing academic seminars.", Font.PLAIN, 18, TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchBar = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        searchBar.setLayout(new BorderLayout());
        searchBar.setPreferredSize(new Dimension(672, 60));
        searchBar.setMaximumSize(new Dimension(672, 60));
        searchBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchBar.setBorder(new EmptyBorder(4, 12, 4, 4));

        searchInput = new JTextField(PLACEHOLDER_TEXT);
        searchInput.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchInput.setForeground(Color.GRAY);
        searchInput.setBorder(null);
        searchInput.setOpaque(false);

        suggestionMenu = new JPopupMenu();

        searchInput.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if(searchInput.getText().equals(PLACEHOLDER_TEXT)) {
                    isProgrammaticUpdate = true;
                    searchInput.setText("");
                    isProgrammaticUpdate = false;
                    searchInput.setForeground(TEXT_DARK);
                }
            }
        });

        searchInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { if(!isProgrammaticUpdate) updateSuggestions(); }
            @Override public void removeUpdate(DocumentEvent e) { if(!isProgrammaticUpdate) updateSuggestions(); }
            @Override public void changedUpdate(DocumentEvent e) { if(!isProgrammaticUpdate) updateSuggestions(); }
        });

        JButton btnSearch = new JButton("Explore");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setBackground(RED_MAIN);
        btnSearch.setPreferredSize(new Dimension(120, 48));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSearch.addActionListener(e -> {
            String q = searchInput.getText();
            if(q.equals(PLACEHOLDER_TEXT)) q = "";
            suggestionMenu.setVisible(false);
            switchToSeminars(q, "All Seminars");
        });

        searchBar.add(searchInput, BorderLayout.CENTER);
        searchBar.add(btnSearch, BorderLayout.EAST);

        hero.add(title1);
        hero.add(title2);
        hero.add(Box.createVerticalStrut(24));
        hero.add(subtitle);
        hero.add(Box.createVerticalStrut(40));
        hero.add(searchBar);

        content.add(hero);
        content.add(Box.createVerticalGlue());
        content.add(createFooter());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private void updateSuggestions() {
        SwingUtilities.invokeLater(() -> {
            String text = searchInput.getText();
            if (text.equals(PLACEHOLDER_TEXT) || text.trim().isEmpty()) {
                suggestionMenu.setVisible(false);
                return;
            }

            suggestionMenu.removeAll();
            boolean hasMatches = false;

            for (DummySeminar s : allSeminars) {
                if (s.title.toLowerCase().contains(text.toLowerCase()) || s.category.toLowerCase().contains(text.toLowerCase())) {

                    JMenuItem item = new JMenuItem(s.title);
                    item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    item.setForeground(TEXT_DARK);
                    item.setBackground(Color.WHITE);
                    item.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    item.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) { item.setBackground(new Color(231, 238, 255)); }
                        @Override
                        public void mouseExited(MouseEvent e) { item.setBackground(Color.WHITE); }
                    });

                    item.addActionListener(e -> {
                        isProgrammaticUpdate = true;
                        searchInput.setText(s.title);
                        isProgrammaticUpdate = false;

                        suggestionMenu.setVisible(false);
                        switchToSeminars(s.title, "All Seminars");
                    });

                    suggestionMenu.add(item);
                    hasMatches = true;
                }
            }

            if (hasMatches) {
                if (!suggestionMenu.isVisible()) {
                    suggestionMenu.show(searchInput, 0, searchInput.getHeight());
                }
                searchInput.requestFocusInWindow();
            } else {
                suggestionMenu.setVisible(false);
            }
        });
    }

    // =========================================================================
    // 3. HALAMAN 2 (SEMINARS VIEW) - Filter & List Seminar
    // =========================================================================
    private JScrollPane createSeminarsView() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(PAGE_BG);

        JPanel filterWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filterWrap.setBackground(FOOTER_BG);
        filterWrap.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        filterWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        categoryContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 24));
        categoryContainer.setOpaque(false);
        refreshCategoryUI();
        filterWrap.add(categoryContainer);

        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setBorder(new EmptyBorder(48, 48, 64, 48));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(1100, 40));
        header.add(label("Discover Seminars", Font.BOLD, 24, TEXT_DARK), BorderLayout.WEST);

        bentoGrid = new JPanel(new BorderLayout(24, 0));
        bentoGrid.setOpaque(false);
        bentoGrid.setMaximumSize(new Dimension(1100, 380));
        bentoGrid.setPreferredSize(new Dimension(1100, 380));

        section.add(header);
        section.add(Box.createVerticalStrut(24));
        section.add(bentoGrid);

        content.add(filterWrap);
        content.add(section);
        content.add(Box.createVerticalGlue());
        content.add(createFooter());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private void refreshCategoryUI() {
        categoryContainer.removeAll();
        String[] categories = {"All Seminars", "Technology", "Business", "Arts & Design", "Medical"};
        for (String cat : categories) {
            boolean isActive = cat.equals(activeCategory);
            JPanel pill = new RoundedPanel(20, isActive ? RED_MAIN : Color.WHITE, isActive ? RED_MAIN : BORDER_COLOR);
            pill.setLayout(new BorderLayout());
            pill.setBorder(new EmptyBorder(8, 24, 8, 24));
            pill.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel lbl = label(cat, Font.BOLD, 13, isActive ? Color.WHITE : TEXT_MUTED);
            pill.add(lbl, BorderLayout.CENTER);

            pill.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    activeCategory = cat;
                    currentSearchQuery = "";

                    isProgrammaticUpdate = true;
                    searchInput.setText(PLACEHOLDER_TEXT);
                    isProgrammaticUpdate = false;

                    searchInput.setForeground(Color.GRAY);

                    refreshCategoryUI();
                    updateBentoGrid(currentSearchQuery, activeCategory);
                }
            });
            categoryContainer.add(pill);
        }
        categoryContainer.revalidate();
        categoryContainer.repaint();
    }

    private void updateBentoGrid(String searchQuery, String category) {
        bentoGrid.removeAll();

        List<DummySeminar> filtered = allSeminars.stream()
                .filter(s -> category.equals("All Seminars") || s.category.equals(category))
                .filter(s -> s.title.toLowerCase().contains(searchQuery.toLowerCase()) || s.desc.toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            JLabel empty = label("Oops, tidak ada seminar yang cocok dengan pencarianmu.", Font.PLAIN, 18, TEXT_MUTED);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            bentoGrid.add(empty, BorderLayout.CENTER);
        } else {
            DummySeminar first = filtered.get(0);
            bentoGrid.add(createBigCard(first.title, first.dateLoc, first.category), BorderLayout.WEST);

            JPanel rightCol = new JPanel(new GridLayout(2, 1, 0, 24));
            rightCol.setOpaque(false);

            if (filtered.size() > 1) {
                rightCol.add(createListCard(filtered.get(1).title, filtered.get(1).desc, filtered.get(1).category, filtered.get(1).color));
            }
            if (filtered.size() > 2) {
                rightCol.add(createListCard(filtered.get(2).title, filtered.get(2).desc, filtered.get(2).category, filtered.get(2).color));
            }

            bentoGrid.add(rightCol, BorderLayout.CENTER);
        }

        bentoGrid.revalidate();
        bentoGrid.repaint();
    }

    private JPanel createBigCard(String title, String subtitle, String badgeText) {
        JPanel bigCard = new RoundedPanel(12, Color.DARK_GRAY, Color.DARK_GRAY);
        bigCard.setPreferredSize(new Dimension(650, 380));
        bigCard.setLayout(new BorderLayout());

        JPanel overlayText = new JPanel();
        overlayText.setLayout(new BoxLayout(overlayText, BoxLayout.Y_AXIS));
        overlayText.setOpaque(false);
        overlayText.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel badge = label(" " + badgeText.toUpperCase() + " ", Font.BOLD, 10, Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(RED_MAIN);
        badge.setBorder(new EmptyBorder(4,8,4,8));

        overlayText.add(Box.createVerticalGlue());
        overlayText.add(badge);
        overlayText.add(Box.createVerticalStrut(12));

        JTextArea titleArea = new JTextArea(title);
        titleArea.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleArea.setForeground(Color.WHITE);
        titleArea.setOpaque(false);
        titleArea.setLineWrap(true);
        titleArea.setWrapStyleWord(true);
        titleArea.setEditable(false);
        overlayText.add(titleArea);

        overlayText.add(Box.createVerticalStrut(8));
        overlayText.add(label(subtitle, Font.PLAIN, 14, Color.LIGHT_GRAY));

        bigCard.add(overlayText, BorderLayout.CENTER);
        return bigCard;
    }

    private JPanel createListCard(String title, String desc, String cat, Color catColor) {
        JPanel card = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BorderLayout(16, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel imgBox = new RoundedPanel(8, new Color(240, 243, 255), new Color(240, 243, 255));
        imgBox.setPreferredSize(new Dimension(120, 120));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(4, 0, 0, 0));

        textPanel.add(label(cat.toUpperCase(), Font.BOLD, 10, catColor));
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(label(title, Font.BOLD, 18, TEXT_DARK));
        textPanel.add(Box.createVerticalStrut(8));

        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDesc.setForeground(TEXT_MUTED);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);

        textPanel.add(txtDesc);

        card.add(imgBox, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    // =========================================================================
    // 4. FOOTER LENGKAP & RAPI
    // =========================================================================
    private JPanel createFooter() {
        JPanel footerWrapper = new JPanel();
        footerWrapper.setLayout(new BoxLayout(footerWrapper, BoxLayout.Y_AXIS));
        footerWrapper.setBackground(FOOTER_BG);
        footerWrapper.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JPanel topFooter = new JPanel();
        topFooter.setLayout(new BoxLayout(topFooter, BoxLayout.X_AXIS));
        topFooter.setOpaque(false);
        topFooter.setBorder(new EmptyBorder(40, 80, 40, 80));

        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
        col1.setOpaque(false);
        col1.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel brand = label("Eventix", Font.BOLD, 28, RED_MAIN);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        col1.add(brand);
        col1.add(Box.createVerticalStrut(12));

        JTextArea brandDesc = new JTextArea("The ultimate seminar management system providing seamless exchange of information and unique academic experiences.");
        brandDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        brandDesc.setForeground(TEXT_MUTED);
        brandDesc.setLineWrap(true);
        brandDesc.setWrapStyleWord(true);
        brandDesc.setEditable(false);
        brandDesc.setOpaque(false);
        brandDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        brandDesc.setMaximumSize(new Dimension(300, 100));
        col1.add(brandDesc);

        JPanel col2 = createFooterColumn("Platform", new String[]{"Find Seminars", "Become a Speaker", "Institutional Access", "Pricing Plans"});
        col2.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel col3 = createFooterColumn("Resources", new String[]{"Academic Policy", "Terms of Service", "Privacy Registry", "Support Center"});
        col3.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel col4 = new JPanel();
        col4.setLayout(new BoxLayout(col4, BoxLayout.Y_AXIS));
        col4.setOpaque(false);
        col4.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel connectLbl = label("Connect", Font.BOLD, 14, TEXT_DARK);
        connectLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        col4.add(connectLbl);
        col4.add(Box.createVerticalStrut(16));

        JPanel iconsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        iconsRow.setOpaque(false);
        iconsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        iconsRow.add(createCircleIcon("W"));
        iconsRow.add(createCircleIcon("C"));
        iconsRow.add(createCircleIcon("@"));
        col4.add(iconsRow);

        topFooter.add(col1);
        topFooter.add(Box.createHorizontalStrut(80));
        topFooter.add(col2);
        topFooter.add(Box.createHorizontalStrut(60));
        topFooter.add(col3);
        topFooter.add(Box.createHorizontalStrut(60));
        topFooter.add(col4);

        JPanel bottomFooterWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 16));
        bottomFooterWrapper.setOpaque(false);

        JPanel bottomFooter = new JPanel(new BorderLayout());
        bottomFooter.setOpaque(false);
        bottomFooter.setPreferredSize(new Dimension(1100, 30));
        bottomFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JLabel copyright = label("© 2026 Eventix Seminar Management System. All Rights Reserved.", Font.PLAIN, 12, TEXT_MUTED);
        copyright.setBorder(new EmptyBorder(16, 0, 0, 0));
        bottomFooter.add(copyright, BorderLayout.WEST);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        badges.setOpaque(false);
        badges.setBorder(new EmptyBorder(16, 0, 0, 0));
        badges.add(label("🌐 Indonesia (ID)", Font.BOLD, 12, TEXT_MUTED));
        badges.add(label("🛡️ Institusi Terverifikasi", Font.BOLD, 12, TEXT_MUTED));
        bottomFooter.add(badges, BorderLayout.EAST);

        bottomFooterWrapper.add(bottomFooter);

        footerWrapper.add(topFooter);
        footerWrapper.add(bottomFooterWrapper);
        return footerWrapper;
    }

    private JPanel createFooterColumn(String title, String[] links) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);

        JLabel titleLbl = label(title, Font.BOLD, 14, TEXT_DARK);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(titleLbl);
        col.add(Box.createVerticalStrut(16));

        for (String linkText : links) {
            JLabel linkLbl = hoverLabel(linkText, LINK_COLOR);
            linkLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            col.add(linkLbl);
            col.add(Box.createVerticalStrut(12));
        }
        return col;
    }

    private JPanel createCircleIcon(String text) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, 36, 36);
                g2.setColor(BORDER_COLOR);
                g2.drawOval(0, 0, 35, 35);
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(36, 36));
        p.setOpaque(false);
        p.setLayout(new GridBagLayout());
        p.add(label(text, Font.BOLD, 14, TEXT_DARK));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginForm().setVisible(true);
            }
        });
        return p;
    }

    // =========================================================================
    // LOGIKA PERPINDAHAN HALAMAN & UTILITY
    // =========================================================================

    private void switchToHome() {
        updateNavbarState("HOME");
        currentSearchQuery = "";
        activeCategory = "All Seminars";

        isProgrammaticUpdate = true;
        searchInput.setText(PLACEHOLDER_TEXT);
        isProgrammaticUpdate = false;

        searchInput.setForeground(Color.GRAY);
        cardLayout.show(cardPanel, "HOME");
    }

    private void switchToSeminars(String searchQuery, String category) {
        updateNavbarState("SEMINARS");
        currentSearchQuery = searchQuery;
        activeCategory = category;
        refreshCategoryUI();
        updateBentoGrid(searchQuery, category);
        cardLayout.show(cardPanel, "SEMINARS");
    }

    private JLabel label(String text, int style, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        return l;
    }

    private JLabel hoverLabel(String text, Color baseColor) {
        JLabel l = label(text, Font.BOLD, 13, baseColor);
        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { l.setForeground(TEXT_DARK); }
            @Override public void mouseExited(MouseEvent e) { l.setForeground(baseColor); }
        });
        return l;
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg, border;
        public RoundedPanel(int radius, Color bg, Color border) {
            this.radius = radius; this.bg = bg; this.border = border; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.setColor(border);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }

    private static class DummySeminar {
        String title, desc, category, dateLoc;
        Color color;
        public DummySeminar(String t, String d, String cat, String dl, Color c) {
            title = t; desc = d; category = cat; dateLoc = dl; color = c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LandingPage().setVisible(true);
        });
    }
}