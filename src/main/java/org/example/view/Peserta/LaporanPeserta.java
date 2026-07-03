package org.example.view.Peserta;

import org.example.component.Header;
import org.example.component.ParticipantSidebar;
import util.CsvExporter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LaporanPeserta extends JFrame {

    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color CARD_BORDER = new Color(239, 188, 195);
    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color RED = new Color(198, 0, 64);
    private static final Color BLUE = new Color(80, 95, 148);
    private static final Color SOFT_BLUE = new Color(239, 243, 255);

    public LaporanPeserta() {
        setTitle("Eventix - Laporan Analitik");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(new ParticipantSidebar("Laporan"), BorderLayout.WEST);
        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new Header("Laporan Akademik"), BorderLayout.NORTH);

        // Membungkus panel laporan dengan fitur scroll vertikal
        main.add(scroll(createReportPage(), 16), BorderLayout.CENTER);
        return main;
    }

    // Mengunci lebar agar tidak scroll ke kanan, membiarkan panjang ke bawah
    private JScrollPane scroll(JPanel content, int increment) {
        JScrollPane scrollPane = new JScrollPane(new ScrollableHost(content));
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(increment);
        return scrollPane;
    }

    private JPanel createReportPage() {
        JPanel page = new JPanel();
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(30, 30, 30, 30));
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));

        // Penambahan Komponen dengan jarak vertikal yang lebih lega
        page.add(createHeaderSection());
        page.add(Box.createVerticalStrut(32));

        page.add(createMetricsSection());
        page.add(Box.createVerticalStrut(32));

        page.add(createAnalyticsSection());
        page.add(Box.createVerticalStrut(32));

        page.add(createTimelineSection());
        page.add(Box.createVerticalStrut(20)); // Margin bawah extra

        return page;
    }

    private JPanel createHeaderSection() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(label("Analisis Kemajuan", Font.BOLD, 31, TEXT));
        left.add(Box.createVerticalStrut(5));
        left.add(label("Pantau capaian belajar, pencapaian kompetensi, dan statistik kehadiran Anda.", Font.PLAIN, 14, MUTED));
        row.add(left, BorderLayout.WEST);

        JButton btnExport = filledButton("Unduh Laporan", 160, 48);
        btnExport.addActionListener(e -> downloadCSV());
        row.add(btnExport, BorderLayout.EAST);

        return row;
    }

    private JPanel createMetricsSection() {
        JPanel grid = new JPanel(new GridLayout(1, 4, 24, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Memastikan tinggi card 150px agar isinya lega
        grid.setPreferredSize(new Dimension(956, 150));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        grid.add(metricCard("JAM BELAJAR", "48", "Jam", new Color(79, 92, 142), new Color(183, 196, 253)));
        grid.add(metricCard("SEMINAR SELESAI", "10", "", new Color(184, 0, 53), new Color(184, 0, 53)));
        grid.add(metricCard("SKOR RATA-RATA", "92", "%", new Color(158, 0, 177), new Color(192, 38, 211)));
        grid.add(metricCard("SERTIFIKAT", "10", "", new Color(55, 68, 117), new Color(133, 166, 249)));

        return grid;
    }

    private JPanel metricCard(String title, String value, String unit, Color iconBg, Color iconFg) {
        JPanel card = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(iconBg.getRed(), iconBg.getGreen(), iconBg.getBlue(), 25));
                g2.fillRoundRect(0, 0, 40, 40, 8, 8);
                g2.setColor(iconFg);
                g2.fillRoundRect(10, 10, 20, 20, 4, 4);
                g2.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(40, 40));
        iconPanel.setOpaque(false);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        top.setOpaque(false);
        top.add(iconPanel);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(label(title, Font.BOLD, 11, MUTED));
        bottom.add(Box.createVerticalStrut(6));

        JPanel valRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        valRow.setOpaque(false);
        valRow.add(label(value, Font.BOLD, 32, TEXT));
        if (!unit.isEmpty()) {
            JLabel uLabel = label(unit, Font.PLAIN, 18, MUTED);
            uLabel.setBorder(new EmptyBorder(8,0,0,0));
            valRow.add(uLabel);
        }
        bottom.add(valRow);

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createAnalyticsSection() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 24, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Membebaskan tinggi grafik hingga 400px (Kebutuhan Scroll)
        grid.setPreferredSize(new Dimension(956, 400));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JPanel radarCard = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        radarCard.setLayout(new BorderLayout());
        radarCard.setBorder(new EmptyBorder(24, 24, 24, 24));
        radarCard.add(label("Competency Radar", Font.BOLD, 18, TEXT), BorderLayout.NORTH);
        radarCard.add(new RadarChart(), BorderLayout.CENTER);

        JPanel catCard = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        catCard.setLayout(new BorderLayout());
        catCard.setBorder(new EmptyBorder(24, 24, 24, 24));
        catCard.add(label("Kategori Pembelajaran", Font.BOLD, 18, TEXT), BorderLayout.NORTH);
        catCard.add(new CategoryBars(), BorderLayout.CENTER);

        grid.add(radarCard);
        grid.add(catCard);
        return grid;
    }

    private JPanel createTimelineSection() {
        JPanel barCard = new BorderedPanel(10, Color.WHITE, CARD_BORDER);
        barCard.setLayout(new BorderLayout());
        barCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        barCard.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Membebaskan tinggi Bar Chart hingga 300px (Kebutuhan Scroll)
        barCard.setPreferredSize(new Dimension(956, 300));
        barCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(label("Frekuensi Kehadiran (6 Bulan Terakhir)", Font.BOLD, 18, TEXT), BorderLayout.WEST);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        legend.setOpaque(false);
        legend.add(legendItem("Dihadiri", RED));
        legend.add(legendItem("Tersedia", new Color(249, 210, 218)));
        titleRow.add(legend, BorderLayout.EAST);

        barCard.add(titleRow, BorderLayout.NORTH);
        barCard.add(new BarChart(), BorderLayout.CENTER);

        return barCard;
    }

    private JPanel legendItem(String name, Color color) {
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        wrap.setOpaque(false);
        JPanel dot = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 4, 10, 10);
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(10, 18));
        dot.setOpaque(false);
        wrap.add(dot);
        wrap.add(label(name, Font.PLAIN, 12, MUTED));
        return wrap;
    }

    private void downloadCSV() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(CsvExporter.generateNamaFile("Analitik_Peserta")));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".csv")) filePath += ".csv";

                String[] header = {"Kategori", "Nilai", "Keterangan"};
                List<Object[]> rows = new ArrayList<>();
                rows.add(new Object[]{"Jam Belajar", "48", "Jam"});
                rows.add(new Object[]{"Seminar Diselesaikan", "10", "Kegiatan"});
                rows.add(new Object[]{"Skor Rata-Rata", "92", "%"});
                rows.add(new Object[]{"Sertifikat Didapat", "10", "Dokumen"});

                CsvExporter.ekspor(filePath, header, rows);
                JOptionPane.showMessageDialog(this, "Laporan Analitik berhasil diunduh.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengunduh: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // KOMPONEN GAMBAR GRAFIK
    // =========================================================================

    private class RadarChart extends JPanel {
        public RadarChart() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int cx = getWidth() / 2;
            int cy = getHeight() / 2 + 10;
            // Radius jauh lebih besar sekarang karena tingginya cukup
            int radius = Math.min(getWidth(), getHeight()) / 2 - 40;

            // Menggambar Spider Web
            g2.setColor(new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(1.2f));
            for (int i = 1; i <= 4; i++) {
                int r = radius * i / 4;
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            }
            for (int i = 0; i < 5; i++) {
                double angle = Math.PI * 2 * i / 5 - Math.PI / 2;
                g2.drawLine(cx, cy, cx + (int)(radius * Math.cos(angle)), cy + (int)(radius * Math.sin(angle)));
            }

            // Menggambar Poligon Area
            Polygon p = new Polygon();
            double[] scores = {0.8, 0.9, 0.6, 0.7, 0.85};
            for (int i = 0; i < 5; i++) {
                double angle = Math.PI * 2 * i / 5 - Math.PI / 2;
                int r = (int)(radius * scores[i]);
                p.addPoint(cx + (int)(r * Math.cos(angle)), cy + (int)(r * Math.sin(angle)));
            }
            g2.setColor(new Color(225, 29, 72, 40));
            g2.fillPolygon(p);
            g2.setColor(RED);
            g2.setStroke(new BasicStroke(2f));
            g2.drawPolygon(p);

            // Teks Indikator Sudut
            g2.setColor(MUTED);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString("Teknis", cx - 20, cy - radius - 15);
            g2.drawString("Manajemen", cx + radius + 15, cy - 20);
            g2.drawString("Komunikasi", cx + radius - 30, cy + radius + 20);
            g2.drawString("Etika", cx - radius - 10, cy + radius + 20);
            g2.drawString("Desain", cx - radius - 55, cy - 20);

            g2.dispose();
        }
    }

    private class CategoryBars extends JPanel {
        public CategoryBars() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            String[] labels = {"Teknologi Informasi", "Bisnis & Manajemen", "Seni & Desain", "Kategori Lainnya"};
            Color[] colors = {RED, BLUE, new Color(192, 38, 211), CARD_BORDER};
            int[] percentages = {65, 45, 25, 10};

            int y = 40;
            int barHeight = 14; // Bar dibuat sedikit lebih tebal

            for (int i = 0; i < labels.length; i++) {
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                g2.setColor(TEXT);
                g2.drawString(labels[i], 0, y);

                g2.setColor(MUTED);
                g2.drawString(percentages[i] + "%", getWidth() - 35, y);

                g2.setColor(SOFT_BLUE);
                g2.fillRoundRect(0, y + 14, getWidth() - 50, barHeight, barHeight, barHeight);

                g2.setColor(colors[i]);
                int barWidth = (getWidth() - 50) * percentages[i] / 100;
                g2.fillRoundRect(0, y + 14, barWidth, barHeight, barHeight, barHeight);

                y += 65; // Jarak antar bar direnggangkan
            }
            g2.dispose();
        }
    }

    private class BarChart extends JPanel {
        public BarChart() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int[] maxData = {4, 6, 5, 8, 7, 9};
            int[] attendedData = {2, 5, 2, 7, 4, 6};
            String[] months = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun"};

            int barWidth = 40;
            int gap = (getWidth() - (6 * barWidth)) / 5;
            int x = 0;
            int baseBottom = getHeight() - 30; // Ruang ekstra untuk teks bawah

            for (int i = 0; i < 6; i++) {
                // Skala pengali tinggi disesuaikan agar grafiknya menjulang
                int maxH = maxData[i] * 18;
                int attH = attendedData[i] * 18;

                g2.setColor(new Color(225, 29, 72, 40));
                g2.fillRoundRect(x, baseBottom - maxH, barWidth, maxH, 8, 8);
                g2.fillRect(x, baseBottom - 8, barWidth, 8);

                g2.setColor(RED);
                g2.fillRoundRect(x, baseBottom - attH, barWidth, attH, 8, 8);
                g2.fillRect(x, baseBottom - 8, barWidth, 8);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.setColor(MUTED);
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (barWidth - fm.stringWidth(months[i])) / 2;
                g2.drawString(months[i], textX, baseBottom + 22);

                x += barWidth + gap;
            }
            g2.dispose();
        }
    }

    // =========================================================================
    // UTILITY COMPONENTS (Bawaan dari SeminarPeserta)
    // =========================================================================

    private JLabel label(String text, int style, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(color);
        return label;
    }

    private JButton filledButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(RED);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        return button;
    }

    // Fitur Utama: Kunci lebar ke viewport, tapi biarkan tinggi tidak terbatas
    private static class ScrollableHost extends JPanel implements Scrollable {
        private ScrollableHost(JComponent content) {
            setOpaque(false);
            setLayout(new BorderLayout());
            add(content, BorderLayout.CENTER);
        }
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL ? visibleRect.height : visibleRect.width;
        }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; } // FALSE = BISA SCROLL BAWAH
    }

    private static class BorderedPanel extends JPanel {
        private final int radius;
        private final Color background, border;
        private BorderedPanel(int radius, Color background, Color border) {
            this.radius = radius; this.background = background; this.border = border;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.setColor(border);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }
}