package org.example.view.Peserta;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import dao.AuditLogDAO;
import dao.PembayaranDAO;
import dao.PendaftaranDAO;
import dao.SeminarDAO;
import model.User;
import org.example.component.ParticipantSidebar;
import service.DummyPaymentService;
import service.PaymentService;
import service.PendaftaranService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class KehadiranPeserta extends JFrame {

    private static final Color PAGE_BG = new Color(249, 249, 255);
    private static final Color TEXT_DARK = new Color(17, 28, 45);
    private static final Color TEXT_MUTED = new Color(92, 63, 64);
    private static final Color RED_MAIN = new Color(184, 0, 53);
    private static final Color BORDER_COLOR = new Color(229, 189, 190);

    private String userName = "Peserta";
    private int totalHadir = 0, totalIzin = 0, totalAbsen = 0;

    private List<AttendanceInfo> listKehadiran = new ArrayList<>();
    private List<AttendanceInfo> filteredKehadiran = new ArrayList<>();

    private int currentPage = 1;
    private final int itemsPerPage = 5;
    private JPanel tableBody;
    private JPanel paginationPanel;
    private JComboBox<String> statusFilter;
    private JComboBox<String> timeFilter;

    public KehadiranPeserta() {
        setTitle("Eventix - Kehadiran Saya");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadBackendData();
        initComponents();
    }

    private void loadBackendData() {
        User userAktif = AuthController.getUserAktif();
        if (userAktif == null) return;

        try {
            userName = userAktif.getNama();
            PendaftaranDAO pendaftaranDAO = new PendaftaranDAO();
            PaymentService paymentService = new DummyPaymentService(new PembayaranDAO());
            PendaftaranService pendaftaranService = new PendaftaranService(
                    pendaftaranDAO, new SeminarDAO(), new PembayaranDAO(), new AuditLogDAO(), paymentService
            );

            List<Object[]> riwayatDaftar = pendaftaranService.getRiwayat(userAktif.getIdUser());
            if (riwayatDaftar != null) {
                for (Object[] row : riwayatDaftar) {
                    if (row != null && row.length >= 4) {
                        String judul = String.valueOf(row[2]);
                        String fullTanggal = String.valueOf(row[3]);
                        String tanggal = fullTanggal;
                        String waktuSeminar = "Sesuai Jadwal";
                        if (fullTanggal.length() >= 16) {
                            tanggal = fullTanggal.substring(0, 10);
                            waktuSeminar = fullTanggal.substring(11, 16) + " WIB";
                        }
                        String speaker = "Penyelenggara Eventix";
                        String waktuPresensi = "-";
                        String statusKehadiran = "ABSEN";

                        if (row.length >= 9) {
                            if (row[7] != null) {
                                String presensiStr = String.valueOf(row[7]);
                                waktuPresensi = presensiStr.length() >= 16 ? presensiStr.substring(11, 16) + " WIB" : presensiStr;
                            }
                            if (row[8] != null) statusKehadiran = String.valueOf(row[8]);
                        }
                        listKehadiran.add(new AttendanceInfo(judul, speaker, tanggal, waktuSeminar, waktuPresensi, statusKehadiran));
                    }
                }
            }
            kalkulasiStatistik();
        } catch (Exception e) {
            kalkulasiStatistik();
        }
    }

    private void kalkulasiStatistik() {
        totalHadir = 0; totalIzin = 0; totalAbsen = 0;
        for (AttendanceInfo info : listKehadiran) {
            if (info.status.equalsIgnoreCase("HADIR")) totalHadir++;
            else if (info.status.equalsIgnoreCase("IZIN") || info.status.equalsIgnoreCase("TERLAMBAT")) totalIzin++;
            else totalAbsen++;
        }
    }

    private void applyFilters() {
        String selectedStatus = (String) statusFilter.getSelectedItem();
        String selectedTime = (String) timeFilter.getSelectedItem();
        filteredKehadiran.clear();
        LocalDate today = LocalDate.now();

        for (AttendanceInfo info : listKehadiran) {
            boolean matchStatus = selectedStatus.equals("Semua Status") || info.status.equalsIgnoreCase(selectedStatus);
            boolean matchTime = true;
            if (!selectedTime.equals("Semua Waktu")) {
                try {
                    LocalDate eventDate = LocalDate.parse(info.date);
                    long daysBetween = ChronoUnit.DAYS.between(eventDate, today);
                    if (selectedTime.equals("7 Hari Terakhir")) matchTime = daysBetween >= 0 && daysBetween <= 7;
                    else if (selectedTime.equals("30 Hari Terakhir")) matchTime = daysBetween >= 0 && daysBetween <= 30;
                } catch (Exception ignored) {}
            }
            if (matchStatus && matchTime) filteredKehadiran.add(info);
        }
        currentPage = 1;
        refreshTable();
    }

    private void refreshTable() {
        tableBody.removeAll();
        int totalItems = filteredKehadiran.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        if (totalPages == 0) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;
        if (currentPage < 1) currentPage = 1;

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        if (totalItems == 0) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
            emptyPanel.setOpaque(false);
            JLabel emptyLbl = new JLabel("Tidak ada riwayat kehadiran yang sesuai.");
            emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLbl.setForeground(Color.GRAY);
            emptyPanel.add(emptyLbl);
            tableBody.add(emptyPanel);
        } else {
            for (int i = startIndex; i < endIndex; i++) {
                tableBody.add(createTableRow(filteredKehadiran.get(i)));
            }
        }
        renderPaginationControls(totalPages);
        tableBody.revalidate();
        tableBody.repaint();
    }

    private void renderPaginationControls(int totalPages) {
        paginationPanel.removeAll();
        paginationPanel.add(createPageButton("<", false, currentPage > 1, () -> { currentPage--; refreshTable(); }));
        for (int i = 1; i <= totalPages; i++) {
            final int page = i;
            paginationPanel.add(createPageButton(String.valueOf(i), page == currentPage, true, () -> { currentPage = page; refreshTable(); }));
        }
        paginationPanel.add(createPageButton(">", false, currentPage < totalPages, () -> { currentPage++; refreshTable(); }));
        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new ParticipantSidebar("Kehadiran"), BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(PAGE_BG);
        mainArea.add(createTopNav(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize(); d.width = 920; return d; // 👈 KUNCI: 920px
            }
            @Override
            public Dimension getMaximumSize() { return new Dimension(920, Integer.MAX_VALUE); }
            @Override
            public Dimension getMinimumSize() { Dimension d = super.getMinimumSize(); d.width = 920; return d; }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PAGE_BG);

        contentPanel.add(createWelcomeHeader());
        contentPanel.add(Box.createVerticalStrut(32));
        contentPanel.add(createStatsGrid());
        contentPanel.add(Box.createVerticalStrut(32));
        contentPanel.add(createTableSection());

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

    private JPanel createWelcomeHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Riwayat Kehadiran Saya");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        JLabel subtitle = new JLabel("Pantau partisipasi dan status kehadiran Anda di seluruh sesi seminar.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_MUTED);
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JPanel createStatsGrid() {
        JPanel grid = new JPanel(new GridLayout(1, 3, 24, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setPreferredSize(new Dimension(920, 190));
        grid.setMaximumSize(new Dimension(920, 190));
        grid.add(createStatCard("TOTAL HADIR", String.valueOf(totalHadir), new Color(222, 232, 255), "images/Icon/Dashboard/Panitia/Seminar/AddEdit/Document_Icon.svg"));
        grid.add(createStatCard("IZIN / TERLAMBAT", String.valueOf(totalIzin), new Color(255, 237, 213), "images/Icon/Dashboard/Panitia/Seminar/AddEdit/Info_Icon.svg"));
        grid.add(createStatCard("TOTAL ABSEN", String.valueOf(totalAbsen), new Color(255, 218, 214), "images/Icon/Dashboard/Panitia/Mail_Icon_Red.svg"));
        return grid;
    }

    private JPanel createStatCard(String title, String value, Color iconBgColor, String iconPath) {
        JPanel card = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topRow.setOpaque(false);
        JPanel iconBox = new RoundedPanel(8, iconBgColor, iconBgColor);
        iconBox.setPreferredSize(new Dimension(50, 50));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(createIcon(iconPath, 24, 24));
        topRow.add(iconBox);
        JPanel textWrap = new JPanel();
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));
        textWrap.setOpaque(false);
        textWrap.setBorder(new EmptyBorder(16, 0, 0, 0));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLbl.setForeground(TEXT_MUTED);
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valLbl.setForeground(TEXT_DARK);
        textWrap.add(titleLbl);
        textWrap.add(Box.createVerticalStrut(4));
        textWrap.add(valLbl);
        card.add(topRow, BorderLayout.NORTH);
        card.add(textWrap, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTableSection() {
        JPanel section = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        section.setLayout(new BorderLayout());
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(920, Integer.MAX_VALUE));

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 20));
        filterRow.setOpaque(false);
        filterRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        statusFilter = new JComboBox<>(new String[]{"Semua Status", "Hadir", "Izin", "Terlambat", "Absen"});
        statusFilter.setPreferredSize(new Dimension(160, 42));
        statusFilter.addActionListener(e -> applyFilters());

        timeFilter = new JComboBox<>(new String[]{"Semua Waktu", "7 Hari Terakhir", "30 Hari Terakhir"});
        timeFilter.setPreferredSize(new Dimension(180, 42));
        timeFilter.addActionListener(e -> applyFilters());

        filterRow.add(statusFilter);
        filterRow.add(timeFilter);

        JPanel tableHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tableHeader.setBackground(new Color(249, 249, 255));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tableHeader.setPreferredSize(new Dimension(920, 50));

        // 👈 KUNCI: Lebar kolom dikurangi agar totalnya pas 920px (300+220+150+150+100)
        tableHeader.add(createHeaderCell("JUDUL SEMINAR", 300));
        tableHeader.add(createHeaderCell("TANGGAL & WAKTU", 220));
        tableHeader.add(createHeaderCell("PRESENSI", 150));
        tableHeader.add(createHeaderCell("STATUS", 150));
        tableHeader.add(createHeaderCell("AKSI", 100));

        tableBody = new JPanel();
        tableBody.setLayout(new BoxLayout(tableBody, BoxLayout.Y_AXIS));
        tableBody.setOpaque(false);

        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 24));
        paginationPanel.setBackground(new Color(249, 249, 255));
        paginationPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(filterRow, BorderLayout.NORTH);
        topContainer.add(tableHeader, BorderLayout.SOUTH);

        section.add(topContainer, BorderLayout.NORTH);
        section.add(tableBody, BorderLayout.CENTER);
        section.add(paginationPanel, BorderLayout.SOUTH);

        return section;
    }

    private JPanel createHeaderCell(String title, int width) {
        JPanel cell = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        cell.setOpaque(false);
        cell.setPreferredSize(new Dimension(width, 50));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        cell.add(lbl);
        return cell;
    }

    private JPanel createTableRow(AttendanceInfo info) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        row.setPreferredSize(new Dimension(920, 85));
        row.setMaximumSize(new Dimension(920, 85));

        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
        col1.setOpaque(false);
        col1.setPreferredSize(new Dimension(300, 85));
        col1.setBorder(new EmptyBorder(20, 16, 20, 16));
        JLabel titleLbl = new JLabel("<html><div style='width:240px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>" + info.title + "</div></html>");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(TEXT_DARK);
        JLabel catLbl = new JLabel(info.speaker);
        catLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        catLbl.setForeground(TEXT_MUTED);
        col1.add(titleLbl);
        col1.add(Box.createVerticalStrut(4));
        col1.add(catLbl);

        JPanel col2 = new JPanel();
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));
        col2.setOpaque(false);
        col2.setPreferredSize(new Dimension(220, 85));
        col2.setBorder(new EmptyBorder(20, 16, 20, 16));
        JLabel dateLbl = new JLabel(info.date);
        dateLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLbl.setForeground(TEXT_DARK);
        JLabel timeLbl = new JLabel(info.time);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLbl.setForeground(TEXT_MUTED);
        col2.add(dateLbl);
        col2.add(Box.createVerticalStrut(4));
        col2.add(timeLbl);

        JPanel col3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 32));
        col3.setOpaque(false);
        col3.setPreferredSize(new Dimension(150, 85));
        JLabel presenceLbl = new JLabel(info.presenceTime);
        presenceLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        presenceLbl.setForeground(TEXT_DARK);
        col3.add(presenceLbl);

        JPanel col4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 28));
        col4.setOpaque(false);
        col4.setPreferredSize(new Dimension(150, 85));
        col4.add(createStatusPill(info.status));

        JPanel col5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 26));
        col5.setOpaque(false);
        col5.setPreferredSize(new Dimension(100, 85));
        JLabel actionBtn = new JLabel("•••");
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        actionBtn.setForeground(TEXT_MUTED);
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        col5.add(actionBtn);

        row.add(col1);
        row.add(col2);
        row.add(col3);
        row.add(col4);
        row.add(col5);
        return row;
    }

    private JPanel createStatusPill(String status) {
        Color bgColor, fgColor;
        if (status.equalsIgnoreCase("HADIR")) {
            bgColor = new Color(236, 253, 245); fgColor = new Color(5, 150, 105);
        } else if (status.equalsIgnoreCase("TERLAMBAT") || status.equalsIgnoreCase("IZIN")) {
            bgColor = new Color(255, 237, 213); fgColor = new Color(217, 119, 6);
        } else {
            bgColor = new Color(255, 218, 214); fgColor = new Color(186, 26, 26);
        }
        JPanel pill = new RoundedPanel(20, bgColor, bgColor);
        pill.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 6));
        JLabel lbl = new JLabel(status.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(fgColor);
        pill.add(lbl);
        return pill;
    }

    private JPanel createPageButton(String text, boolean isActive, boolean isEnabled, Runnable action) {
        JPanel btn = new RoundedPanel(8, isActive ? RED_MAIN : Color.WHITE, BORDER_COLOR);
        btn.setPreferredSize(new Dimension(40, 40));
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

    private static class AttendanceInfo {
        String title, speaker, date, time, presenceTime, status;
        public AttendanceInfo(String t, String c, String d, String tm, String pt, String s) {
            title = t; speaker = c; date = d; time = tm; presenceTime = pt; status = s;
        }
    }
}