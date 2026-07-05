package org.example.view.Peserta;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import dao.LaporanDAO;
import model.User;
import org.example.component.ParticipantSidebar;
import org.example.component.ScrollablePanel;
import org.example.view.LoginForm;
import service.LaporanService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class SertifikatPeserta extends JFrame {

    // --- Palet Warna Konsisten ---
    private static final Color PAGE_BG = new Color(249, 249, 255);
    private static final Color TEXT_DARK = new Color(17, 28, 45);
    private static final Color TEXT_MUTED = new Color(92, 63, 64);
    private static final Color RED_MAIN = new Color(225, 29, 72);
    private static final Color BORDER_COLOR = new Color(229, 189, 190);

    // --- Data Dinamis Murni Database ---
    private User userAktif;
    private String userName = "Peserta";
    private List<CertificateInfo> listSertifikat = new ArrayList<>();
    private List<CertificateInfo> filteredSertifikat = new ArrayList<>();

    // --- State Pagination ---
    private int currentPage = 1;
    private final int itemsPerPage = 6; // Grid 3x2 (Maksimal 6 per halaman)

    // --- Komponen Dinamis ---
    private JPanel gridPanel;
    private JPanel paginationPanel;
    private JLabel paginationText;
    private JComboBox<String> kategoriFilter;

    public SertifikatPeserta() {
        setTitle("Eventix - Sertifikat Saya");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loadBackendData();
        initComponents();
    }

    // =========================================================================
    // 1. INTEGRASI BACKEND MURNI (TANPA DUMMY)
    // =========================================================================
    private void loadBackendData() {
        userAktif = AuthController.getUserAktif();
        if (userAktif == null) {
            System.err.println("Gagal memuat data: Tidak ada user yang login.");
            return;
        }

        try {
            userName = userAktif.getNama();
            LaporanService laporanService = new LaporanService(new LaporanDAO());

            // Urutan index dari getLaporanPeserta:
            // [0] judul, [1] tanggal, [2] nama, [3] kode, [4] status_hadir, [5] nomor_sertifikat
            List<Object[]> dataLaporan = laporanService.getLaporanPeserta(userAktif.getIdUser());

            if (dataLaporan != null && !dataLaporan.isEmpty()) {
                for (Object[] row : dataLaporan) {
                    String noSertif = row[5] != null ? String.valueOf(row[5]) : "";

                    // Hanya ambil data jika peserta benar-benar memiliki sertifikat
                    if (!noSertif.isEmpty() && !noSertif.equals("null")) {
                        String judul = String.valueOf(row[0]);
                        String tanggal = String.valueOf(row[1]);

                        // Deteksi Kategori Otomatis
                        String judulLower = judul.toLowerCase();
                        String kategoriTeks = "Pengembangan Diri & Soft Skill"; // Default

                        if (judulLower.contains("tech") || judulLower.contains("ai") || judulLower.contains("web") || judulLower.contains("data") || judulLower.contains("code")) {
                            kategoriTeks = "Teknologi & Pemrograman";
                        } else if (judulLower.contains("business") || judulLower.contains("bisnis") || judulLower.contains("marketing") || judulLower.contains("startup")) {
                            kategoriTeks = "Bisnis & Kewirausahaan";
                        }

                        listSertifikat.add(new CertificateInfo(judul, kategoriTeks, noSertif, tanggal));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat Sertifikat dari database: " + e.getMessage());
        }
    }

    // =========================================================================
    // 2. LOGIKA FILTER & PAGINATION
    // =========================================================================
    private void applyFilters() {
        String selectedKategori = (String) kategoriFilter.getSelectedItem();
        filteredSertifikat.clear();

        for (CertificateInfo info : listSertifikat) {
            if (selectedKategori.equals("Semua Kategori") || info.category.equalsIgnoreCase(selectedKategori)) {
                filteredSertifikat.add(info);
            }
        }

        currentPage = 1;
        refreshGrid();
    }

    private void refreshGrid() {
        gridPanel.removeAll();

        int totalItems = filteredSertifikat.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        if (totalPages == 0) totalPages = 1;

        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        if (totalItems == 0) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 80));
            emptyPanel.setOpaque(false);
            emptyPanel.setPreferredSize(new Dimension(880, 200));
            JLabel emptyLbl = new JLabel("Belum ada sertifikat di kategori ini.");
            emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyLbl.setForeground(Color.GRAY);
            emptyPanel.add(emptyLbl);
            gridPanel.add(emptyPanel);
        } else {
            for (int i = startIndex; i < endIndex; i++) {
                gridPanel.add(createCertificateCard(filteredSertifikat.get(i)));
            }
        }

        renderPaginationControls(totalItems, totalPages, startIndex, endIndex);

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void renderPaginationControls(int totalItems, int totalPages, int startIndex, int endIndex) {
        paginationPanel.removeAll();

        // Teks Informasi Menampilkan
        int tampilAkhir = (totalItems == 0) ? 0 : endIndex;
        int tampilAwal = (totalItems == 0) ? 0 : (startIndex + 1);
        paginationText.setText("Menampilkan " + tampilAwal + "-" + tampilAkhir + " dari " + totalItems + " Sertifikat");

        // Tombol Pagination
        JPanel buttonsWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonsWrap.setOpaque(false);

        buttonsWrap.add(createPageButton("<", false, currentPage > 1, () -> {
            currentPage--;
            refreshGrid();
        }));

        for (int i = 1; i <= totalPages; i++) {
            final int page = i;
            buttonsWrap.add(createPageButton(String.valueOf(i), page == currentPage, true, () -> {
                currentPage = page;
                refreshGrid();
            }));
        }

        buttonsWrap.add(createPageButton(">", false, currentPage < totalPages, () -> {
            currentPage++;
            refreshGrid();
        }));

        paginationPanel.add(buttonsWrap, BorderLayout.EAST);
        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    // =========================================================================
    // 3. THE ULTIMATE ANCHOR LAYOUT (Vaksin Anti-Gepeng)
    // =========================================================================
    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new ParticipantSidebar("Sertifikat"), BorderLayout.WEST); // 👉 Fokus Tab Sertifikat

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(PAGE_BG);
        mainArea.add(createTopNav(), BorderLayout.NORTH);

        ScrollablePanel contentPanel = new ScrollablePanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PAGE_BG);

        // Lebar konsisten 920px, Tinggi bebas melar
        contentPanel.setPreferredSize(new Dimension(920, 1000));
        contentPanel.setMaximumSize(new Dimension(920, Integer.MAX_VALUE));
        contentPanel.setMinimumSize(new Dimension(920, 1000));

        contentPanel.add(createHeaderSection());
        contentPanel.add(Box.createVerticalStrut(32));
        contentPanel.add(createFilterAndGridSection());

        // JANGKAR MUTLAK KIRI ATAS
        JPanel anchorWrapper = new JPanel(new GridBagLayout());
        anchorWrapper.setBackground(PAGE_BG);
        anchorWrapper.setBorder(new EmptyBorder(32, 32, 32, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        anchorWrapper.add(contentPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(anchorWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);

        mainArea.add(scrollPane, BorderLayout.CENTER);
        root.add(mainArea, BorderLayout.CENTER);
        add(root);

        applyFilters();
    }

    // =========================================================================
    // 4. KOMPONEN UI
    // =========================================================================
    private JPanel createTopNav() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(PAGE_BG);
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        nav.setPreferredSize(new Dimension(0, 64));

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 16));
        profilePanel.setOpaque(false);
        JLabel profileName = new JLabel(userName + " (Peserta)");
        profileName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        profileName.setForeground(TEXT_DARK);
        profilePanel.add(profileName);
        profilePanel.add(createIcon("images/Icon/Dashboard/user_icon.svg", 28, 28));

        nav.add(profilePanel, BorderLayout.EAST);
        return nav;
    }

    private JPanel createHeaderSection() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(920, 80));

        JLabel title = new JLabel("Kelola Sertifikat");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Lihat dan kelola sertifikat yang diperoleh dari seminar yang telah diselesaikan.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_MUTED);

        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createFilterAndGridSection() {
        JPanel section = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        section.setLayout(new BorderLayout());
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(920, Integer.MAX_VALUE));

        // --- Filter Row ---
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 20));
        filterRow.setOpaque(false);
        filterRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JLabel filterLbl = new JLabel("Kategori: ");
        filterLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterLbl.setForeground(TEXT_MUTED);

        String[] kategories = {"Semua Kategori", "Teknologi & Pemrograman", "Bisnis & Kewirausahaan", "Pengembangan Diri & Soft Skill"};
        kategoriFilter = new JComboBox<>(kategories);
        kategoriFilter.setPreferredSize(new Dimension(250, 40));
        kategoriFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        kategoriFilter.setBackground(Color.WHITE);
        kategoriFilter.setFocusable(false);
        kategoriFilter.addActionListener(e -> applyFilters());

        filterRow.add(filterLbl);
        filterRow.add(kategoriFilter);

        // --- Grid Cards ---
        gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 24));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        // --- Pagination Row ---
        JPanel footerRow = new JPanel(new BorderLayout());
        footerRow.setOpaque(false);
        footerRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                new EmptyBorder(24, 24, 24, 24)
        ));

        paginationText = new JLabel("Menampilkan 0 dari 0 Sertifikat");
        paginationText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        paginationText.setForeground(TEXT_MUTED);
        footerRow.add(paginationText, BorderLayout.WEST);

        paginationPanel = new JPanel(new BorderLayout());
        paginationPanel.setOpaque(false);
        footerRow.add(paginationPanel, BorderLayout.EAST);

        section.add(filterRow, BorderLayout.NORTH);
        section.add(gridPanel, BorderLayout.CENTER);
        section.add(footerRow, BorderLayout.SOUTH);

        return section;
    }

    private JPanel createCertificateCard(CertificateInfo info) {
        JPanel card = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BorderLayout());

        // Lebar 280px agar muat 3 kolom di dalam lebar 920px (280*3 + jarak)
        card.setPreferredSize(new Dimension(276, 320));
        card.setMinimumSize(new Dimension(276, 320));

        // Top Area: Placeholder Gambar Sertifikat
        JPanel imgContainer = new RoundedPanel(12, new Color(240, 243, 255), BORDER_COLOR);
        imgContainer.setPreferredSize(new Dimension(276, 150));
        imgContainer.setLayout(new GridBagLayout());

        JLabel certIcon = createIcon("images/Icon/Dashboard/Certificate_Icon.svg", 48, 48);
        imgContainer.add(certIcon);

        // Bottom Area: Info & Action
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Badge Verifikasi
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgePanel.setOpaque(false);
        JLabel badge = new JLabel(" TERVERIFIKASI ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(new Color(21, 128, 61)); // Hijau Tua
        badge.setOpaque(true);
        badge.setBackground(new Color(220, 252, 231)); // Hijau Muda
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));
        badgePanel.add(badge);

        JLabel titleArea = new JLabel("<html><div style='width: 230px; font-family: Segoe UI; font-weight: bold; font-size: 16px; color: #111c2d;'>" + info.title + "</div></html>");

        // Baris Tombol Aksi
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionRow.setOpaque(false);

        JPanel unduhBtn = new RoundedPanel(8, RED_MAIN, RED_MAIN);
        unduhBtn.setPreferredSize(new Dimension(100, 36));
        unduhBtn.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 6));
        unduhBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel unduhIco = createIcon("images/Icon/Dashboard/Panitia/Download_Icon_Red.svg", 14, 14); // Menggunakan ikon download tapi paksa warna putih via text kalo bisa, atau pakai yang ada
        JLabel unduhTxt = new JLabel("Unduh");
        unduhTxt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        unduhTxt.setForeground(Color.WHITE);
        unduhBtn.add(unduhTxt);

        unduhBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Aksi Unduh Nyata
                JOptionPane.showMessageDialog(null,
                        "Memproses pengunduhan sertifikat:\nNo: " + info.noSertifikat + "\nSeminar: " + info.title,
                        "Unduh Sertifikat", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        actionRow.add(unduhBtn);

        textPanel.add(badgePanel);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(titleArea);
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(actionRow);

        card.add(imgContainer, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createPageButton(String text, boolean isActive, boolean isEnabled, Runnable action) {
        JPanel btn = new RoundedPanel(8, isActive ? RED_MAIN : Color.WHITE, BORDER_COLOR);
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setLayout(new GridBagLayout());

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(isActive ? Color.WHITE : (isEnabled ? TEXT_MUTED : Color.LIGHT_GRAY));

        btn.add(lbl);

        if (isEnabled) {
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { action.run(); }
            });
        }
        return btn;
    }

    private JLabel createIcon(String path, int width, int height) {
        JLabel lbl = new JLabel();
        try { lbl.setIcon(new FlatSVGIcon(path, width, height)); } catch (Exception ignored) {}
        return lbl;
    }

    // =========================================================================
    // UTILITY CLASSES
    // =========================================================================
    private static class RoundedPanel extends JPanel {
        private final int radius; private final Color bg, border;
        public RoundedPanel(int radius, Color bg, Color border) {
            this.radius = radius; this.bg = bg; this.border = border; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg); g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.setColor(border); g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }

    private static class CertificateInfo {
        String title, category, noSertifikat, date;
        public CertificateInfo(String t, String c, String no, String d) {
            title = t; category = c; noSertifikat = no; date = d;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SertifikatPeserta().setVisible(true);
        });
    }
}