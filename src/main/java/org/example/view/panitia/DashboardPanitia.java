package org.example.view.panitia;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import controller.LaporanController;
import dao.LaporanDAO;
import dao.PendaftaranDAO;
import dao.SeminarDAO;
import enums.StatusSeminar;
import model.Seminar;
import model.User;
import org.example.component.Header;
import org.example.component.Sidebar;
import org.example.util.Theme;
import service.LaporanService;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DashboardPanitia extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color SOFT_BLUE = new Color(238, 242, 255);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);

    private static final DateTimeFormatter FMT_DAFTAR =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FMT_JADWAL =
            DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter FMT_JAM =
            DateTimeFormatter.ofPattern("HH.mm");

    private User panitia;
    private List<Seminar> seminarMilikSaya = new ArrayList<>();
    private List<SummaryItem> summaryItems = new ArrayList<>();
    private List<SeminarItem> seminarItems = new ArrayList<>();
    private final int[] chartValues = new int[7];
    private int totalSeminar = 0;
    private int totalPeserta = 0;
    private int eventAktif = 0;
    private int pendingCount = 0;

    public DashboardPanitia() {
        setTitle("Eventix - Dashboard Panitia");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        loadData();
        initComponents();
    }

    /**
     * Mengambil seluruh data dashboard langsung dari database:
     * - Seminar milik panitia yang sedang login (SeminarDAO.getByPanitia)
     * - Rekap jumlah tiket/peserta per seminar (LaporanDAO.getLaporanPanitia)
     * - Riwayat tanggal pendaftaran untuk grafik tren & jumlah pending (PendaftaranDAO.getPesertaBySeminar)
     */
    private void loadData() {
        panitia = AuthController.getUserAktif();
        int idPanitia = panitia != null ? panitia.getIdUser() : -1;
        if (idPanitia <= 0) {
            summaryItems = buildSummaryItems();
            return;
        }

        SeminarDAO seminarDAO = new SeminarDAO();
        PendaftaranDAO pendaftaranDAO = new PendaftaranDAO();
        LaporanDAO laporanDAO = new LaporanDAO();

        try {
            seminarMilikSaya = seminarDAO.getByPanitia(idPanitia);
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal mengambil seminar panitia: " + e.getMessage());
            seminarMilikSaya = new ArrayList<>();
        }

        totalSeminar = seminarMilikSaya.size();
        for (Seminar s : seminarMilikSaya) {
            if (s.getStatus() == StatusSeminar.DIBUKA) eventAktif++;
        }

        try {
            for (Object[] row : laporanDAO.getLaporanPanitia(idPanitia)) {
                totalPeserta += toInt(row[3]); // jml_tiket
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Gagal mengambil rekap laporan panitia: " + e.getMessage());
        }

        for (Seminar s : seminarMilikSaya) {
            try {
                for (Object[] row : pendaftaranDAO.getPesertaBySeminar(s.getIdSeminar())) {
                    String tanggalDaftar = row[4] != null ? row[4].toString() : null;
                    String status = row[5] != null ? row[5].toString() : "";
                    if ("PENDING".equalsIgnoreCase(status)) pendingCount++;
                    tambahKeChart(tanggalDaftar);
                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Gagal mengambil peserta seminar #" + s.getIdSeminar() + ": " + e.getMessage());
            }
        }

        summaryItems = buildSummaryItems();
        seminarItems = buildSeminarItems();
    }

    private List<SummaryItem> buildSummaryItems() {
        return List.of(
                new SummaryItem("TOTAL SELURUH SEMINAR", formatNumber(totalSeminar), "Seminar yang Anda kelola", "Seminar_Icon_Red.svg"),
                new SummaryItem("TOTAL PESERTA", formatNumber(totalPeserta), "Dari semua seminar Anda", "person_pin.svg"),
                new SummaryItem("EVENT AKTIF", formatNumber(eventAktif), "Status DIBUKA saat ini", "sensors.svg")
        );
    }

    private List<SeminarItem> buildSeminarItems() {
        List<SeminarItem> items = new ArrayList<>();
        for (Seminar s : seminarMilikSaya) {
            items.add(toSeminarItem(s));
        }
        return items;
    }

    private SeminarItem toSeminarItem(Seminar s) {
        String subtitle = (s.getLokasi() != null && !s.getLokasi().isBlank() ? s.getLokasi() : "Lokasi belum diatur")
                + "\n" + (s.getMode() != null ? s.getMode().name() : "-");

        String jadwal = formatJadwal(s.getTanggalMulai(), s.getTanggalSelesai());

        int kuota = s.getKuota();
        int terisi = s.getKuotaTerisi();
        double progress = kuota > 0 ? Math.min(1.0, (double) terisi / kuota) : 0.0;
        int persen = kuota > 0 ? (int) Math.round(progress * 100) : 0;

        String label;
        boolean aksen;
        switch (s.getStatus()) {
            case DIBUKA -> {
                boolean sudahMulai = s.getTanggalMulai() != null && s.getTanggalMulai().isBefore(LocalDateTime.now());
                boolean sudahSelesai = s.getTanggalSelesai() != null && s.getTanggalSelesai().isBefore(LocalDateTime.now());
                if (sudahSelesai) { label = "Menunggu Ditutup"; aksen = false; }
                else if (sudahMulai) { label = "Berlangsung"; aksen = true; }
                else { label = "Akan Datang"; aksen = true; }
            }
            case DITUTUP -> { label = "Ditutup"; aksen = false; }
            case SELESAI -> { label = "Selesai"; aksen = false; }
            case CANCELLED -> { label = "Dibatalkan"; aksen = false; }
            default -> { label = s.getStatus().name(); aksen = false; }
        }

        return new SeminarItem(
                s.getJudul() != null ? s.getJudul() : "-",
                subtitle,
                jadwal,
                terisi + " / " + kuota,
                persen + "% Terisi",
                progress,
                label,
                aksen
        );
    }

    private String formatJadwal(LocalDateTime mulai, LocalDateTime selesai) {
        if (mulai == null) return "-";
        StringBuilder sb = new StringBuilder(mulai.format(FMT_JADWAL));
        sb.append("\n").append(mulai.format(FMT_JAM));
        if (selesai != null) sb.append(" - ").append(selesai.format(FMT_JAM));
        return sb.toString();
    }

    private void tambahKeChart(String tanggalDaftar) {
        if (tanggalDaftar == null || tanggalDaftar.isBlank()) return;
        try {
            LocalDateTime dt;
            try {
                dt = LocalDateTime.parse(tanggalDaftar, FMT_DAFTAR);
            } catch (DateTimeParseException ex) {
                dt = LocalDate.parse(tanggalDaftar.substring(0, 10)).atStartOfDay();
            }
            int index = dt.getDayOfWeek().getValue() - 1; // Senin=0 ... Minggu=6
            if (index >= 0 && index < chartValues.length) chartValues[index]++;
        } catch (Exception ignored) {
            // Format tanggal tidak dikenali — lewati agar dashboard tetap tampil.
        }
    }

    private int toInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number number) return number.intValue();
        try { return Integer.parseInt(value.toString()); } catch (NumberFormatException e) { return 0; }
    }

    private String formatNumber(int value) {
        return String.format("%,d", value).replace(',', '.');
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new Sidebar("Dashboard"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new Header("Dashboard"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(32, 30, 34, 30));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(createStatsRow());
        content.add(Box.createVerticalStrut(28));
        content.add(createSectionHeader());
        content.add(Box.createVerticalStrut(16));
        content.add(createSeminarTable());
        content.add(Box.createVerticalStrut(32));
        content.add(createBottomRow());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scrollPane, BorderLayout.CENTER);
        return main;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        for (SummaryItem item : summaryItems) {
            row.add(createStatCard(item.title(), item.value(), item.note(), item.icon()));
        }
        return row;
    }

    private JPanel createStatCard(String title, String value, String note, String icon) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 26, 22, 26));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel titleLabel = label(title, Font.PLAIN, 10, MUTED);
        JLabel valueLabel = label(value, Font.BOLD, 32, icon.equals("sensors.svg") ? RED : TEXT);
        JLabel noteLabel = label(note, Font.PLAIN, 15, Theme.ACCENT);

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(8));
        text.add(valueLabel);
        text.add(Box.createVerticalStrut(8));
        text.add(noteLabel);
        card.add(text, BorderLayout.WEST);
        card.add(new IconBadge(icon), BorderLayout.EAST);
        return card;
    }

    private JPanel createSectionHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel title = label("Seminar Saya", Font.BOLD, 25, TEXT);
        header.add(title, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(createOutlineButton("Filter", "Filter_Icon.svg"));
        buttons.add(createOutlineButton("Terbaru", "Filer2_Icon.svg"));
        header.add(buttons, BorderLayout.EAST);
        return header;
    }

    private JButton createOutlineButton(String text, String icon) {
        JButton button = new JButton(text);
        button.setIcon(panitiaIcon(icon, 12, 12));
        button.setIconTextGap(8);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setForeground(MUTED);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(8, 16, 8, 16)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createSeminarTable() {
        JPanel table = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
        table.setAlignmentX(Component.LEFT_ALIGNMENT);

        int ditampilkan = Math.min(5, seminarItems.size());
        int tinggiHeader = 56;
        int tinggiFooter = 76;
        int tinggiBaris = seminarItems.isEmpty() ? 90 : ditampilkan * 122;
        int tinggiTotal = tinggiHeader + tinggiBaris + tinggiFooter;
        table.setMaximumSize(new Dimension(Integer.MAX_VALUE, tinggiTotal));
        table.setPreferredSize(new Dimension(0, tinggiTotal));

        table.add(createTableHeader());
        if (seminarItems.isEmpty()) {
            table.add(createEmptySeminarRow());
        } else {
            for (SeminarItem item : seminarItems.subList(0, ditampilkan)) {
                table.add(createSeminarRow(item));
            }
        }
        table.add(createTableFooter());
        return table;
    }

    private JPanel createEmptySeminarRow() {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setPreferredSize(new Dimension(0, 90));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        row.add(label("Anda belum memiliki seminar. Buat seminar baru untuk mulai mengelola peserta.", Font.PLAIN, 14, MUTED));
        return row;
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(true);
        header.setBackground(new Color(243, 246, 255));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        header.setPreferredSize(new Dimension(0, 56));
        header.setBorder(new EmptyBorder(0, 26, 0, 20));

        addHeaderCell(header, "DETAIL SEMINAR", 0, 3);
        addHeaderCell(header, "JADWAL", 1, 2);
        addHeaderCell(header, "REGISTRASI", 2, 2);
        addHeaderCell(header, "STATUS", 3, 2);
        addHeaderCell(header, "AKSI", 4, 1);
        return header;
    }

    private void addHeaderCell(JPanel row, String text, int gridx, int weight) {
        GridBagConstraints gbc = constraints(gridx, weight);
        row.add(label(text, Font.BOLD, 12, MUTED), gbc);
    }

    private JPanel createSeminarRow(SeminarItem item) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 122));
        row.setPreferredSize(new Dimension(0, 122));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(20, 26, 18, 24)
        ));

        addSeminarDetails(row, item.name(), item.institution());
        addCell(row, multiline(item.schedule(), Font.PLAIN, 16, TEXT), 1, 2);
        addRegistration(row, item.count(), item.percent(), item.progress());
        addStatus(row, item.status(), item.upcoming());
        addActions(row);
        return row;
    }

    private void addSeminarDetails(JPanel row, String name, String institution) {
        JPanel details = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        details.setOpaque(false);
        details.add(new ThumbnailPanel());

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(multiline(name, Font.BOLD, 14, TEXT));
        text.add(multiline(institution, Font.PLAIN, 16, MUTED));
        details.add(text);
        addCell(row, details, 0, 3);
    }

    private void addRegistration(JPanel row, String count, String percent, double progress) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel left = label(count, Font.PLAIN, 10, progress > 0 ? RED : MUTED);
        JLabel right = label(percent, Font.PLAIN, 10, MUTED);
        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        panel.add(top);
        panel.add(Box.createVerticalStrut(7));
        panel.add(new ProgressLine(progress));
        addCell(row, panel, 2, 2);
    }

    private void addStatus(JPanel row, String status, boolean upcoming) {
        JLabel pill = label("  " + status + "  ", Font.PLAIN, 11, upcoming ? Theme.ACCENT : MUTED);
        pill.setHorizontalAlignment(SwingConstants.CENTER);
        pill.setOpaque(true);
        pill.setBackground(upcoming ? new Color(250, 229, 255) : new Color(235, 240, 252));
        pill.setBorder(new EmptyBorder(6, 12, 6, 12));

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.add(pill);
        addCell(row, wrap, 3, 2);
    }

    private void addActions(JPanel row) {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(createSmallButton("Edit", false));
        actions.add(createSmallButton("Hapus", true));
        addCell(row, actions, 4, 1);
    }

    private JButton createSmallButton(String text, boolean filled) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setForeground(filled ? Color.WHITE : new Color(80, 95, 148));
        button.setBackground(filled ? RED : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(filled
                ? new EmptyBorder(8, 15, 8, 15)
                : new RoundedBorder(8, CARD_BORDER, new Insets(8, 15, 8, 15)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createTableFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(true);
        footer.setBackground(new Color(243, 246, 255));
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        footer.setPreferredSize(new Dimension(0, 76));
        footer.setBorder(new EmptyBorder(18, 26, 18, 26));

        int ditampilkan = Math.min(5, seminarItems.size());
        footer.add(label("Menampilkan " + ditampilkan + " dari " + seminarItems.size() + " seminar", Font.PLAIN, 11, MUTED), BorderLayout.WEST);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pages.setOpaque(false);
        pages.add(createPageButton("<", false));
        pages.add(createPageButton("1", true));
        pages.add(createPageButton("2", false));
        pages.add(createPageButton("3", false));
        pages.add(createPageButton(">", false));
        footer.add(pages, BorderLayout.EAST);
        return footer;
    }

    private JButton createPageButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(active ? RED : MUTED);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(34, 32));
        button.setBorder(new RoundedBorder(8, CARD_BORDER, new Insets(0, 0, 0, 0)));
        return button;
    }

    private JPanel createBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 24, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 355));
        row.setPreferredSize(new Dimension(0, 355));
        row.add(createChartCard());
        row.add(createQuickActionsCard());
        return row;
    }

    private JPanel createChartCard() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(26, 26, 20, 26));
        card.add(label("Tren Registrasi", Font.PLAIN, 16, TEXT), BorderLayout.NORTH);
        card.add(new ChartPanel(chartValues), BorderLayout.CENTER);
        return card;
    }

    private JPanel createQuickActionsCard() {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(26, 26, 26, 26));
        card.add(label("Aksi Cepat", Font.PLAIN, 16, TEXT), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 24));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(28, 0, 0, 0));
        grid.add(createQuickAction("Kirim Email", "Beri tahu semua peserta", "Mail_Icon_Red.svg", null));
        grid.add(createQuickAction("Ekspor CSV", "Unduh laporan seminar saya", "Download_Icon_Red.svg", this::handleEksporCsv));
        grid.add(createQuickAction("Validasi Dokumen", "Cek " + pendingCount + " tertunda", "Seminar_Icon_Red.svg", null));
        grid.add(createQuickAction("Bantuan", "Dukungan tersedia", "Help_Icon_Red.svg", null));
        card.add(grid, BorderLayout.CENTER);
        return card;
    }

    private JPanel createQuickAction(String title, String subtitle, String icon, Runnable onClick) {
        JPanel action = new BorderedPanel(10, SOFT_BLUE, CARD_BORDER);
        action.setLayout(new BoxLayout(action, BoxLayout.Y_AXIS));
        action.setBorder(new EmptyBorder(14, 12, 12, 12));
        action.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(panitiaIcon(icon, 20, 20));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel titleLabel = label(title, Font.PLAIN, 16, TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitleLabel = label(subtitle, Font.PLAIN, 10, MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        action.add(iconLabel);
        action.add(Box.createVerticalStrut(10));
        action.add(titleLabel);
        action.add(Box.createVerticalStrut(2));
        action.add(subtitleLabel);

        if (onClick != null) {
            action.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    onClick.run();
                }
            });
        }
        return action;
    }

    /**
     * Mengekspor laporan seminar milik panitia yang sedang login ke file CSV,
     * menggunakan LaporanController → LaporanService → LaporanDAO (data dari database).
     */
    private void handleEksporCsv() {
        if (panitia == null) {
            JOptionPane.showMessageDialog(this, "Anda harus login terlebih dahulu.", "Ekspor CSV", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("laporan_panitia_" + panitia.getIdUser() + ".csv"));
        int pilihan = chooser.showSaveDialog(this);
        if (pilihan != JFileChooser.APPROVE_OPTION) return;

        LaporanController laporanController = new LaporanController(new LaporanService(new LaporanDAO()));
        String hasil = laporanController.eksporPanitia(panitia, chooser.getSelectedFile().getAbsolutePath());
        if (hasil.startsWith("SUKSES|")) {
            JOptionPane.showMessageDialog(this, hasil.replace("SUKSES|", ""), "Ekspor CSV", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, hasil.replace("ERROR|", ""), "Ekspor CSV Gagal", JOptionPane.ERROR_MESSAGE);
        }
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
        JLabel label = label("<html>" + text.replace("\n", "<br>") + "</html>", style, size, color);
        return label;
    }

    private Icon panitiaIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/Panitia/" + fileName, width, height);
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

        private IconBadge(String icon) {
            this.icon = icon;
            setOpaque(false);
            setPreferredSize(new Dimension(50, 50));
            setLayout(new GridBagLayout());
            add(new JLabel(panitiaIcon(icon, 19, 19)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int box = 46;
            int x = (getWidth() - box) / 2;
            int y = (getHeight() - box) / 2;
            g2.setColor(new Color(255, 241, 246));
            g2.fillRoundRect(x, y, box, box, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ThumbnailPanel extends JPanel {
        private ThumbnailPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(56, 56));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint paint = new GradientPaint(0, 0, new Color(18, 39, 61), getWidth(), getHeight(), new Color(10, 180, 190));
            g2.setPaint(paint);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.setColor(new Color(255, 255, 255, 45));
            for (int i = 8; i < getWidth(); i += 10) {
                g2.drawLine(i, 8, i + 16, getHeight() - 8);
            }
            g2.dispose();
        }
    }

    private static class ProgressLine extends JPanel {
        private final double progress;

        private ProgressLine(double progress) {
            this.progress = progress;
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
            g2.setColor(RED);
            g2.fillRoundRect(0, 1, (int) (getWidth() * progress), 6, 8, 8);
            g2.dispose();
        }
    }

    private static class ChartPanel extends JPanel {
        private final int[] values;
        private final int hariIni = LocalDate.now().getDayOfWeek().getValue() - 1;
        private static final String[] labels = {"Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"};

        private ChartPanel(int[] values) {
            this.values = values;
            setOpaque(false);
            setPreferredSize(new Dimension(0, 250));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int chartHeight = 180;
            int baseY = getHeight() - 38;
            int startX = 42;
            int gap = 56;
            int barWidth = 56;

            int max = 1;
            for (int v : values) max = Math.max(max, v);

            for (int i = 0; i < values.length; i++) {
                int h = values[i] * chartHeight / max;
                int x = startX + i * gap;
                int y = baseY - h;
                boolean sorot = i == hariIni;
                g2.setColor(sorot ? new Color(204, 48, 99) : new Color(229, 236, 252));
                g2.fillRoundRect(x, y, barWidth, Math.max(h, 3), 9, 9);
                g2.setColor(sorot ? Theme.PRIMARY : MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString(labels[i], x + 18, baseY + 28);
            }

            boolean adaData = max > 1 || java.util.Arrays.stream(values).anyMatch(v -> v > 0);
            if (!adaData) {
                g2.setColor(MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.drawString("Belum ada data pendaftaran.", startX, baseY - chartHeight / 2);
            }
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

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = padding.top;
            insets.left = padding.left;
            insets.bottom = padding.bottom;
            insets.right = padding.right;
            return insets;
        }
    }

    private record SummaryItem(String title, String value, String note, String icon) {
    }

    private record SeminarItem(String name, String institution, String schedule, String count, String percent,
                               double progress, String status, boolean upcoming) {
    }
}
