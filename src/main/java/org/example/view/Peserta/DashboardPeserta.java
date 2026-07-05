package org.example.view.Peserta;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import dao.AuditLogDAO;
import dao.PendaftaranDAO;
import dao.SertifikatDAO;
import dao.SeminarDAO;
import dao.PembayaranDAO;
import dao.PresensiDAO;
import model.User;
import org.example.component.ParticipantSidebar;
import org.example.view.LoginForm;

import service.PendaftaranService;
import service.SertifikatService;
import service.DummyPaymentService;
import service.PaymentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DashboardPeserta extends JFrame {

    private static final Color PAGE_BG = new Color(249, 249, 255);
    private static final Color TEXT_DARK = new Color(17, 28, 45);
    private static final Color TEXT_MUTED = new Color(92, 63, 64);
    private static final Color RED_MAIN = new Color(184, 0, 53);
    private static final Color BORDER_COLOR = new Color(229, 189, 190);

    private String userName = "Peserta";
    private int totalSeminarDiikuti = 0;
    private int totalSeminarAkanDatang = 0;
    private int totalSertifikat = 0;
    private List<SeminarInfo> listSeminarKu = new ArrayList<>();
    private List<ActivityInfo> listAktivitasKu = new ArrayList<>();

    public DashboardPeserta() {
        setTitle("Eventix - Dashboard Peserta");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loadBackendData();
        initComponents();
    }

    private void loadBackendData() {
        User userAktif = AuthController.getUserAktif();
        if (userAktif == null) {
            loadDummyData();
            return;
        }
        try {
            userName = userAktif.getNama();
            PembayaranDAO pembayaranDAO = new PembayaranDAO();
            PendaftaranDAO pendaftaranDAO = new PendaftaranDAO();
            AuditLogDAO auditLogDAO = new AuditLogDAO();
            SeminarDAO seminarDAO = new SeminarDAO();
            PresensiDAO presensiDAO = new PresensiDAO();
            SertifikatDAO sertifikatDAO = new SertifikatDAO();

            PaymentService paymentService = new DummyPaymentService(pembayaranDAO);
            PendaftaranService pendaftaranService = new PendaftaranService(
                    pendaftaranDAO, seminarDAO, pembayaranDAO, auditLogDAO, paymentService
            );
            SertifikatService sertifikatService = new SertifikatService(
                    sertifikatDAO, pendaftaranDAO, presensiDAO, auditLogDAO
            );

            List<Object[]> riwayatDaftar = pendaftaranService.getRiwayat(userAktif.getIdUser());
            if (riwayatDaftar != null) {
                totalSeminarDiikuti = riwayatDaftar.size();
                for (Object[] row : riwayatDaftar) {
                    if (row != null && row.length >= 3) {
                        String judul = String.valueOf(row[2]);
                        String kategoriTeks = "GENERAL";

                        if (row.length >= 7 && row[6] != null) {
                            try {
                                int idKategori = Integer.parseInt(String.valueOf(row[6]));
                                if (idKategori == 1) kategoriTeks = "TEKNOLOGI DAN PEMROGRAMAN";
                                else if (idKategori == 2) kategoriTeks = "PENGEMBANGAN DIRI & SOFT SKILL";
                                else if (idKategori == 3) kategoriTeks = "BISNIS DAN KEWIRAUSAHAAN";
                            } catch (NumberFormatException ignored) {}
                        }
                        String randomImg = "https://images.unsplash.com/photo-1544531586-fde5298cdd40?auto=format&fit=crop&w=280&q=80";
                        listSeminarKu.add(new SeminarInfo(judul, kategoriTeks, randomImg));
                    }
                }
            }
            List<Object[]> riwayatSertifikat = sertifikatService.getSertifikatPemesan(userAktif.getIdUser());
            if (riwayatSertifikat != null) totalSertifikat = riwayatSertifikat.size();

            listAktivitasKu.add(new ActivityInfo("Berhasil login ke sistem", "Baru saja", "images/Icon/Dashboard/Panitia/Seminar/AddEdit/Document_Icon.svg"));
        } catch (Exception e) {
            loadDummyData();
        }
    }

    private void loadDummyData() {
        userName = "Miko";
        totalSeminarDiikuti = 8;
        totalSertifikat = 3;
        listSeminarKu.add(new SeminarInfo("AI Ethics in Modern Research", "TEKNOLOGI", "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?auto=format&fit=crop&w=280&q=80"));
        listSeminarKu.add(new SeminarInfo("Advanced Thesis Methodologies", "SOFT SKILL", "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?auto=format&fit=crop&w=280&q=80"));
        listAktivitasKu.add(new ActivityInfo("Teregistrasi untuk AI Ethics Seminar", "2 hours ago", "images/Icon/Dashboard/Panitia/Seminar/AddEdit/Document_Icon.svg"));
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new ParticipantSidebar("Dashboard"), BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(PAGE_BG);
        mainArea.add(createTopNav(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = 920; // 👈 KUNCI: Lebar diperkecil ke 920
                return d;
            }
            @Override
            public Dimension getMaximumSize() { return getPreferredSize(); }
            @Override
            public Dimension getMinimumSize() { return getPreferredSize(); }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PAGE_BG);

        contentPanel.add(createWelcomeHeader());
        contentPanel.add(Box.createVerticalStrut(32));
        contentPanel.add(createStatsGrid());
        contentPanel.add(Box.createVerticalStrut(36));
        contentPanel.add(createSeminarSection());
        contentPanel.add(Box.createVerticalStrut(36));
        contentPanel.add(createActivitySection());

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

    private JPanel createWelcomeHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Selamat Datang, " + userName + "!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        JLabel subtitle = new JLabel("Kamu memiliki " + totalSeminarAkanDatang + " seminar mendatang. Tetap fokus!");
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
        grid.setPreferredSize(new Dimension(920, 190)); // 👈 Lebar menyesuaikan
        grid.setMaximumSize(new Dimension(920, 190));
        grid.add(createStatCard("SEMINAR DIIKUTI", String.valueOf(totalSeminarDiikuti), new Color(183, 196, 253), "images/Icon/Dashboard/Attendance_Icon.svg"));
        grid.add(createStatCard("AKAN DATANG", String.valueOf(totalSeminarAkanDatang), new Color(255, 218, 218), "images/Icon/Dashboard/Panitia/Seminar/Date_Icon.svg"));
        grid.add(createStatCard("SERTIFIKAT", String.valueOf(totalSertifikat), new Color(255, 214, 251), "images/Icon/Dashboard/Certificate_Icon.svg"));
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
        iconBox.add(createIcon(iconPath, 28, 28));
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

    private JPanel createSeminarSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Seminar Yang Diikuti");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);

        JPanel seminarList = new JPanel();
        seminarList.setLayout(new BoxLayout(seminarList, BoxLayout.X_AXIS));
        seminarList.setOpaque(false);

        if (listSeminarKu.isEmpty()) {
            JLabel emptyLbl = new JLabel("Belum ada seminar yang diikuti.");
            emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLbl.setForeground(Color.GRAY);
            seminarList.add(emptyLbl);
        } else {
            for (SeminarInfo sem : listSeminarKu) {
                seminarList.add(createSeminarCard(sem.title, sem.category, sem.imageUrl));
                seminarList.add(Box.createHorizontalStrut(16));
            }
        }

        JScrollPane seminarScroll = new JScrollPane(seminarList);
        seminarScroll.setBorder(null);
        seminarScroll.setOpaque(false);
        seminarScroll.getViewport().setOpaque(false);
        seminarScroll.getHorizontalScrollBar().setUnitIncrement(20);
        // 👈 KUNCI: Memastikan slider horizontal aktif jika card seminar berlebih
        seminarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        seminarScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        seminarScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        seminarScroll.setPreferredSize(new Dimension(920, 310));
        seminarScroll.setMaximumSize(new Dimension(920, 310));

        section.add(title);
        section.add(Box.createVerticalStrut(20));
        section.add(seminarScroll);
        return section;
    }

    private JPanel createSeminarCard(String title, String category, String imageUrl) {
        JPanel card = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 290));
        card.setMinimumSize(new Dimension(280, 290));
        card.setMaximumSize(new Dimension(280, 290));

        JPanel imageContainer = new RoundedPanel(12, new Color(231, 238, 255), BORDER_COLOR);
        imageContainer.setPreferredSize(new Dimension(280, 130));
        imageContainer.setLayout(new BorderLayout());
        JLabel imageLabel = new JLabel("Memuat...", SwingConstants.CENTER);
        imageContainer.add(imageLabel, BorderLayout.CENTER);

        new Thread(() -> {
            try {
                ImageIcon icon = new ImageIcon(new URL(imageUrl));
                Image img = icon.getImage().getScaledInstance(280, 130, Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> {
                    imageLabel.setText("");
                    imageLabel.setIcon(new ImageIcon(img));
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    imageLabel.setText("");
                    imageLabel.setIcon(new FlatSVGIcon("images/Icon/Dashboard/Panitia/Seminar/AddEdit/ImageIcon.svg", 48, 48));
                });
            }
        }).start();

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        badgePanel.setOpaque(false);
        JLabel badge = new JLabel(" " + category + " ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badge.setForeground(RED_MAIN);
        badge.setOpaque(true);
        badge.setBackground(new Color(255, 218, 218));
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));
        badgePanel.add(badge);

        JLabel titleArea = new JLabel("<html><div style='width: 220px; font-family: Segoe UI; font-weight: bold; font-size: 14px; color: #111c2d;'>" + title + "</div></html>");
        textPanel.add(badgePanel);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(titleArea);

        card.add(imageContainer, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createActivitySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Aktivitas Terkini");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);

        JPanel activityBox = new RoundedPanel(12, Color.WHITE, BORDER_COLOR);
        activityBox.setLayout(new BoxLayout(activityBox, BoxLayout.Y_AXIS));

        JPanel actHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 16));
        actHeader.setBackground(new Color(240, 243, 255));
        actHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        actHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel actHeaderLbl = new JLabel("RIWAYAT AKTIVITAS");
        actHeaderLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actHeaderLbl.setForeground(TEXT_MUTED);
        actHeader.add(actHeaderLbl);
        activityBox.add(actHeader);

        if (listAktivitasKu.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setOpaque(false);
            emptyPanel.setBorder(new EmptyBorder(16,16,16,16));
            JLabel emptyLbl = new JLabel("Belum ada riwayat aktivitas.");
            emptyPanel.add(emptyLbl, BorderLayout.CENTER);
            activityBox.add(emptyPanel);
        } else {
            for (ActivityInfo act : listAktivitasKu) {
                activityBox.add(createActivityItem(act.title, act.time, act.iconPath));
            }
        }

        section.add(title);
        section.add(Box.createVerticalStrut(20));
        section.add(activityBox);
        return section;
    }

    private JPanel createActivityItem(String title, String time, String iconPath) {
        JPanel item = new JPanel(new BorderLayout(16, 0));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(16, 16, 16, 16)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        JPanel iconPanel = new RoundedPanel(20, new Color(240, 243, 255), BORDER_COLOR);
        iconPanel.setPreferredSize(new Dimension(42, 42));
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.add(createIcon(iconPath, 24, 24));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel titleLbl = new JLabel("<html><span style='font-family: Segoe UI; font-weight: bold; font-size: 14px; color: #111c2d;'>" + title + "</span></html>");
        JLabel timeLbl = new JLabel(time);
        timeLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLbl.setForeground(TEXT_MUTED);
        textPanel.add(titleLbl);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(timeLbl);

        item.add(iconPanel, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        return item;
    }

    private JLabel createIcon(String path, int width, int height) {
        JLabel lbl = new JLabel();
        try { lbl.setIcon(new FlatSVGIcon(path, width, height)); } catch (Exception ignored) {}
        return lbl;
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg, border;
        public RoundedPanel(int radius, Color bg, Color border) {
            this.radius = radius; this.bg = bg; this.border = border; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.setColor(border);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }

    private static class SeminarInfo {
        String title, category, imageUrl;
        public SeminarInfo(String t, String c, String i) { title = t; category = c; imageUrl = i; }
    }

    private static class ActivityInfo {
        String title, time, iconPath;
        public ActivityInfo(String t, String tm, String i) { title = t; time = tm; iconPath = i; }
    }
}