package org.example.view.Peserta;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import dao.LaporanDAO;
import model.User;
import org.example.component.ParticipantSidebar;
import org.example.component.ScrollablePanel;
import service.LaporanService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.List;

public class LaporanPeserta extends JFrame {

    private static final Color PAGE_BG = new Color(249, 249, 255);
    private static final Color TEXT_DARK = new Color(17, 28, 45);
    private static final Color TEXT_MUTED = new Color(144, 111, 112);
    private static final Color BORDER_COLOR = new Color(229, 189, 190);
    private static final Color RED_MAIN = new Color(225, 29, 72);

    private User userAktif;
    private LaporanService laporanService;
    private String userName = "Peserta";

    private int jamBelajar = 0;
    private int seminarSelesai = 0;
    private int sertifikatDidapat = 0;

    // 👈 KUNCI: Kategori Umum Dihapus, sisa 3 Matriks Asli
    private int techCount = 0;
    private int softCount = 0;
    private int businessCount = 0;

    public LaporanPeserta() {
        setTitle("Eventix - Laporan Peserta");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loadBackendData();
        initComponents();
    }

    private void loadBackendData() {
        userAktif = AuthController.getUserAktif();
        LaporanDAO laporanDAO = new LaporanDAO();
        laporanService = new LaporanService(laporanDAO);

        if (userAktif == null) return;

        try {
            userName = userAktif.getNama();
            List<Object[]> dataLaporan = laporanService.getLaporanPeserta(userAktif.getIdUser());

            if (dataLaporan != null && !dataLaporan.isEmpty()) {
                for (Object[] row : dataLaporan) {
                    String status = row[4] != null ? String.valueOf(row[4]) : "";
                    if ("HADIR".equalsIgnoreCase(status)) {
                        seminarSelesai++;
                        jamBelajar += 3;
                    }

                    String noSertif = row[5] != null ? String.valueOf(row[5]) : "";
                    if (!noSertif.isEmpty() && !noSertif.equals("null")) {
                        sertifikatDidapat++;
                    }

                    // 👈 KUNCI: Klasifikasi dipaksa ke 3 matriks ini saja
                    String judul = row[0] != null ? String.valueOf(row[0]).toLowerCase() : "";
                    if (judul.contains("tech") || judul.contains("ai") || judul.contains("web") || judul.contains("data") || judul.contains("code") || judul.contains("cyber")) {
                        techCount++;
                    } else if (judul.contains("business") || judul.contains("bisnis") || judul.contains("marketing") || judul.contains("startup") || judul.contains("finance")) {
                        businessCount++;
                    } else {
                        // Kategori lainnya akan dipukul rata masuk ke Pengembangan Diri / Soft Skill
                        softCount++;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat Laporan dari database: " + e.getMessage());
        }
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new ParticipantSidebar("Laporan"), BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(PAGE_BG);
        mainArea.add(createTopNav(), BorderLayout.NORTH);

        ScrollablePanel contentPanel = new ScrollablePanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PAGE_BG);

        // 👈 KUNCI: Lebar dikecilkan ke 920px agar tidak kepotong
        contentPanel.setPreferredSize(new Dimension(920, 1000));
        contentPanel.setMaximumSize(new Dimension(920, Integer.MAX_VALUE));

        contentPanel.add(createHeaderSection());
        contentPanel.add(Box.createVerticalStrut(32));
        contentPanel.add(createMetricsRow());
        contentPanel.add(Box.createVerticalStrut(32));
        contentPanel.add(createAnalyticsSection());
        contentPanel.add(Box.createVerticalGlue());

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

    private JPanel createHeaderSection() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setMaximumSize(new Dimension(920, 80));

        JPanel textWrap = new JPanel();
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));
        textWrap.setOpaque(false);

        JLabel title = new JLabel("Laporan Akademik");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Analisis kemajuan belajar, pencapaian kompetensi, dan riwayat partisipasi.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_MUTED);

        textWrap.add(title);
        textWrap.add(Box.createVerticalStrut(4));
        textWrap.add(subtitle);

        JPanel downloadBtn = new RoundedPanel(8, RED_MAIN, RED_MAIN);
        downloadBtn.setPreferredSize(new Dimension(200, 48));
        downloadBtn.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 12));
        downloadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel btnIcon = createIcon("images/Icon/Dashboard/Panitia/Download_Icon_Red.svg", 16, 16);
        JLabel btnText = new JLabel("Unduh Laporan");
        btnText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnText.setForeground(Color.WHITE);

        downloadBtn.add(btnIcon);
        downloadBtn.add(btnText);

        downloadBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (userAktif == null) return;
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Simpan Laporan CSV");
                    fileChooser.setSelectedFile(new File("Laporan_Seminar_Peserta.csv"));

                    int userSelection = fileChooser.showSaveDialog(null);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        if (!filePath.endsWith(".csv")) filePath += ".csv";
                        laporanService.eksporLaporanPesertaCsv(userAktif.getIdUser(), filePath);
                        JOptionPane.showMessageDialog(null, "Laporan berhasil diunduh ke:\n" + filePath, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal mengunduh laporan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 16));
        rightWrap.setOpaque(false);
        rightWrap.add(downloadBtn);

        header.add(textWrap, BorderLayout.WEST);
        header.add(rightWrap, BorderLayout.EAST);

        return header;
    }

    private JPanel createMetricsRow() {
        JPanel grid = new JPanel(new GridLayout(1, 3, 24, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setPreferredSize(new Dimension(920, 160));
        grid.setMaximumSize(new Dimension(920, 160));

        grid.add(createMetricCard("JAM BELAJAR", String.valueOf(jamBelajar), " Jam", new Color(183, 196, 253), "images/Icon/Dashboard/Panitia/Seminar/Date_Icon.svg"));
        grid.add(createMetricCard("SEMINAR DISELESAIKAN", String.valueOf(seminarSelesai), "", new Color(255, 218, 218), "images/Icon/Dashboard/Attendance_Icon.svg"));
        grid.add(createMetricCard("SERTIFIKAT DIDAPAT", String.valueOf(sertifikatDidapat), "", new Color(224, 231, 255), "images/Icon/Dashboard/Certificate_Icon.svg"));

        return grid;
    }

    private JPanel createMetricCard(String title, String value, String suffix, Color iconBgColor, String iconPath) {
        JPanel card = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topRow.setOpaque(false);

        JPanel iconBox = new RoundedPanel(8, iconBgColor, iconBgColor);
        iconBox.setPreferredSize(new Dimension(40, 40));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(createIcon(iconPath, 20, 20));
        topRow.add(iconBox);

        JPanel textWrap = new JPanel();
        textWrap.setLayout(new BoxLayout(textWrap, BoxLayout.Y_AXIS));
        textWrap.setOpaque(false);
        textWrap.setBorder(new EmptyBorder(16, 0, 0, 0));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLbl.setForeground(TEXT_MUTED);

        JPanel valWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        valWrap.setOpaque(false);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valLbl.setForeground(TEXT_DARK);
        valWrap.add(valLbl);

        if (!suffix.isEmpty()) {
            JLabel sufLbl = new JLabel(suffix);
            sufLbl.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            sufLbl.setForeground(TEXT_MUTED);
            sufLbl.setBorder(new EmptyBorder(6, 0, 0, 0));
            valWrap.add(sufLbl);
        }

        textWrap.add(titleLbl);
        textWrap.add(Box.createVerticalStrut(4));
        textWrap.add(valWrap);

        card.add(topRow, BorderLayout.NORTH);
        card.add(textWrap, BorderLayout.CENTER);
        return card;
    }

    private JPanel createAnalyticsSection() {
        JPanel section = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        section.setLayout(new BorderLayout());
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setPreferredSize(new Dimension(920, 360)); // Tinggi dikurangi karena sisa 3 bar
        section.setMaximumSize(new Dimension(920, 360));
        section.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Kategori Pembelajaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_DARK);
        section.add(title, BorderLayout.NORTH);

        JPanel barsPanel = new JPanel();
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setOpaque(false);
        barsPanel.setBorder(new EmptyBorder(24, 0, 0, 0));

        int totalCat = techCount + softCount + businessCount;
        if (totalCat == 0) totalCat = 1; // Cegah divide by zero

        double pTech = (double) techCount / totalCat;
        double pSoft = (double) softCount / totalCat;
        double pBus = (double) businessCount / totalCat;

        // 👈 KUNCI: Cuma Nampilin 3 Baris Matriks Saja
        barsPanel.add(createProgressBarItem("Teknologi & Pemrograman", pTech, RED_MAIN));
        barsPanel.add(Box.createVerticalStrut(28));
        barsPanel.add(createProgressBarItem("Pengembangan Diri & Soft Skill", pSoft, new Color(79, 92, 142)));
        barsPanel.add(Box.createVerticalStrut(28));
        barsPanel.add(createProgressBarItem("Bisnis & Kewirausahaan", pBus, new Color(192, 38, 211)));

        section.add(barsPanel, BorderLayout.CENTER);
        return section;
    }

    private JPanel createProgressBarItem(String categoryName, double percentage, Color fillCol) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel textRow = new JPanel(new BorderLayout());
        textRow.setOpaque(false);
        textRow.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel nameLbl = new JLabel(categoryName);
        nameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLbl.setForeground(TEXT_DARK);

        int pctInt = (int) Math.round(percentage * 100);
        JLabel pctLbl = new JLabel(pctInt + "%");
        pctLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pctLbl.setForeground(TEXT_MUTED);

        textRow.add(nameLbl, BorderLayout.WEST);
        textRow.add(pctLbl, BorderLayout.EAST);

        JPanel bar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(231, 238, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(fillCol);
                int fillWidth = (int) (getWidth() * percentage);
                if (fillWidth > 0) g2.fillRoundRect(0, 0, fillWidth, getHeight(), 12, 12);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(920, 12));

        wrap.add(textRow, BorderLayout.NORTH);
        wrap.add(bar, BorderLayout.CENTER);

        return wrap;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LaporanPeserta().setVisible(true);
        });
    }
}