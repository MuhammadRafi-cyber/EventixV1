package org.example.view.panitia;

import org.example.component.Header;
import org.example.component.Sidebar; // Menggunakan Sidebar khusus Panitia
import org.example.component.RoundedTable;
import util.CsvExporter; // Sesuaikan import utilitas

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LaporanPanitia extends JFrame {

    // --- Palet Warna Seragam Eventix ---
    private static final Color PAGE_BG = new Color(248, 249, 255);
    private static final Color CARD_BORDER = new Color(229, 189, 190);
    private static final Color TEXT = new Color(17, 28, 45);
    private static final Color MUTED = new Color(92, 63, 64);
    private static final Color RED = new Color(184, 0, 53);
    private static final Color BLUE = new Color(79, 92, 142);
    private static final Color GREEN = new Color(21, 128, 61);
    private static final Color GREEN_BG = new Color(220, 252, 231);

    public LaporanPanitia() {
        setTitle("Eventix - Laporan Penyelenggara (Panitia)");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);

        // Sidebar Panitia
        root.add(new Sidebar("Laporan"), BorderLayout.WEST);

        root.add(createMainArea(), BorderLayout.CENTER);
        add(root);
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);
        main.add(new Header("Laporan Penyelenggara"), BorderLayout.NORTH);

        main.add(scroll(createReportPage(), 16), BorderLayout.CENTER);
        return main;
    }

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
        page.setBorder(new EmptyBorder(32, 32, 32, 32));
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));

        page.add(createHeaderSection());
        page.add(Box.createVerticalStrut(32));

        page.add(createSummaryCards());
        page.add(Box.createVerticalStrut(32));

        page.add(createChartsSection());
        page.add(Box.createVerticalStrut(32));

        page.add(createTableSection());
        page.add(Box.createVerticalStrut(20));

        return page;
    }

    // =========================================================================
    // 1. HEADER SECTION
    // =========================================================================
    private JPanel createHeaderSection() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(label("Laporan Penyelenggara", Font.BOLD, 31, TEXT));
        left.add(Box.createVerticalStrut(5));
        left.add(label("Ringkasan performa finansial, partisipasi, dan statistik acara.", Font.PLAIN, 14, MUTED));
        row.add(left, BorderLayout.WEST);

        JButton btnExport = filledButton("Download Laporan", 180, 48);
        btnExport.addActionListener(e -> downloadCSV());
        row.add(btnExport, BorderLayout.EAST);

        return row;
    }

    // =========================================================================
    // 2. SUMMARY CARDS (BENTO GRID)
    // =========================================================================
    private JPanel createSummaryCards() {
        JPanel grid = new JPanel(new GridLayout(1, 3, 24, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setPreferredSize(new Dimension(956, 170));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        grid.add(createStatCard("TOTAL PENDAPATAN", "Rp 142.580.000", "+12.5%", true, RED, new Color(216, 227, 251)));
        grid.add(createStatCard("RATA-RATA KEHADIRAN", "94.2%", "+5.2%", true, BLUE, new Color(216, 227, 251)));

        // Kartu Ketiga: Top Performing Seminar
        JPanel topCard = new BorderedPanel(12, Color.WHITE, CARD_BORDER);
        topCard.setLayout(new BoxLayout(topCard, BoxLayout.Y_AXIS));
        topCard.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconWrap.setOpaque(false);
        iconWrap.add(new CircleIcon(RED, Color.WHITE));

        topCard.add(iconWrap);
        topCard.add(Box.createVerticalStrut(12));
        topCard.add(label("SEMINAR KINERJA TERBAIK", Font.BOLD, 10, MUTED));
        topCard.add(Box.createVerticalStrut(4));
        topCard.add(label("<html>Advanced Data Science<br>& AI Ethics</html>", Font.BOLD, 18, TEXT));
        topCard.add(Box.createVerticalStrut(6));
        topCard.add(label("420 Peserta • Rp 24.500.000", Font.PLAIN, 12, MUTED));

        grid.add(topCard);
        return grid;
    }

    private JPanel createStatCard(String title, String value, String tag, boolean isPositive, Color iconColor, Color iconBg) {
        JPanel card = new BorderedPanel(12, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(new SquareIcon(iconColor, iconBg), BorderLayout.WEST);

        JLabel tagLabel = label(tag, Font.BOLD, 11, isPositive ? GREEN : RED);
        tagLabel.setOpaque(true);
        tagLabel.setBackground(isPositive ? GREEN_BG : new Color(254, 226, 226));
        tagLabel.setBorder(new EmptyBorder(4, 8, 4, 8));
        JPanel tagWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        tagWrap.setOpaque(false);
        tagWrap.add(tagLabel);
        top.add(tagWrap, BorderLayout.EAST);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(label(title, Font.BOLD, 11, MUTED));
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(label(value, Font.BOLD, 32, TEXT));

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    // =========================================================================
    // 3. CHARTS SECTION
    // =========================================================================
    private JPanel createChartsSection() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 32, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setPreferredSize(new Dimension(956, 360));
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));

        // Line Chart Area
        JPanel lineCard = new BorderedPanel(12, Color.WHITE, CARD_BORDER);
        lineCard.setLayout(new BorderLayout());
        lineCard.setBorder(new EmptyBorder(24, 24, 24, 24));
        lineCard.add(label("Pertumbuhan Pendaftaran", Font.BOLD, 18, TEXT), BorderLayout.NORTH);
        lineCard.add(new LineChart(), BorderLayout.CENTER);

        // Bar Chart Area
        JPanel barCard = new BorderedPanel(12, Color.WHITE, CARD_BORDER);
        barCard.setLayout(new BorderLayout());
        barCard.setBorder(new EmptyBorder(24, 24, 24, 24));
        barCard.add(label("Pendapatan per Kategori (Juta Rp)", Font.BOLD, 18, TEXT), BorderLayout.NORTH);
        barCard.add(new HorizontalBarChart(), BorderLayout.CENTER);

        grid.add(lineCard);
        grid.add(barCard);
        return grid;
    }

    // =========================================================================
    // 4. DETAILED DATA TABLE SECTION
    // =========================================================================
    private JPanel createTableSection() {
        JPanel tableCard = new BorderedPanel(12, Color.WHITE, CARD_BORDER);
        tableCard.setLayout(new BorderLayout());
        tableCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableCard.setPreferredSize(new Dimension(956, 380));
        tableCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        // Table Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(24, 24, 24, 24));
        header.add(label("Rincian Laporan Seminar", Font.BOLD, 18, TEXT), BorderLayout.WEST);

        // Pencarian (Dekorasi)
        JTextField search = new JTextField("Cari data...");
        search.setPreferredSize(new Dimension(200, 36));
        search.setBackground(new Color(240, 243, 255));
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER), new EmptyBorder(0, 10, 0, 10)));
        JPanel rightHead = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightHead.setOpaque(false);
        rightHead.add(search);
        header.add(rightHead, BorderLayout.EAST);

        tableCard.add(header, BorderLayout.NORTH);

        // Setup Tabel (Menggunakan komponen RoundedTable dari repo)
        String[] columns = {"NAMA SEMINAR", "KATEGORI", "TANGGAL", "KEHADIRAN", "PENDAPATAN"};
        Object[][] data = {
                {"Quantum Physics Foundations", "SAINS", "12 Nov 2026", "150/150", "Rp 22.500.000"},
                {"Digital Marketing Strategy", "BISNIS", "18 Nov 2026", "200/220", "Rp 30.000.000"},
                {"Psychology of Leadership", "SOSIAL", "22 Nov 2026", "95/100", "Rp 14.250.000"},
                {"Advanced AI Ethics", "TEKNOLOGI", "01 Des 2026", "420/450", "Rp 24.500.000"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        // Gunakan JTable standar bawaan Java
        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);

        JTableHeader th = table.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(new Color(249, 249, 255));
        th.setForeground(MUTED);
        th.setPreferredSize(new Dimension(100, 48));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        return tableCard;
    }

    // =========================================================================
    // FITUR DOWNLOAD CSV
    // =========================================================================
    private void downloadCSV() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(CsvExporter.generateNamaFile("Laporan_Penyelenggara")));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".csv")) filePath += ".csv";

                String[] header = {"Nama Seminar", "Kategori", "Tanggal", "Kehadiran", "Pendapatan"};
                List<Object[]> rows = new ArrayList<>();
                rows.add(new Object[]{"Quantum Physics Foundations", "SAINS", "12 Nov 2026", "150", "22500000"});
                rows.add(new Object[]{"Digital Marketing Strategy", "BISNIS", "18 Nov 2026", "200", "30000000"});
                rows.add(new Object[]{"Psychology of Leadership", "SOSIAL", "22 Nov 2026", "95", "14250000"});
                rows.add(new Object[]{"Advanced AI Ethics", "TEKNOLOGI", "01 Des 2026", "420", "24500000"});

                CsvExporter.ekspor(filePath, header, rows);
                JOptionPane.showMessageDialog(this, "Laporan Panitia berhasil diunduh ke:\n" + filePath);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengunduh: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // CUSTOM PAINTING (GRAFIK & ICON)
    // =========================================================================

    private class LineChart extends JPanel {
        public LineChart() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int[] data = {20, 45, 30, 80, 60, 95};
            String[] months = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun"};

            int w = getWidth();
            int h = getHeight() - 30; // Ruang untuk teks bulan
            int padding = 20;

            // Grid horizontal
            g2.setColor(new Color(240, 240, 245));
            for(int i=0; i<4; i++) {
                int y = padding + (h - padding * 2) * i / 3;
                g2.drawLine(padding, y, w - padding, y);
            }

            int stepX = (w - padding * 2) / 5;
            Polygon area = new Polygon();
            area.addPoint(padding, h - padding);

            // Kalkulasi titik garis
            int[] xPoints = new int[6];
            int[] yPoints = new int[6];
            for (int i = 0; i < 6; i++) {
                xPoints[i] = padding + i * stepX;
                yPoints[i] = h - padding - (int) ((data[i] / 100.0) * (h - padding * 2));
                area.addPoint(xPoints[i], yPoints[i]);

                // Label Bulan
                g2.setColor(MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.drawString(months[i], xPoints[i] - 10, h + 15);
            }
            area.addPoint(xPoints[5], h - padding);

            // Fill Area Bawah Garis
            g2.setColor(new Color(184, 0, 53, 30));
            g2.fillPolygon(area);

            // Gambar Garis
            g2.setColor(RED);
            g2.setStroke(new BasicStroke(3f));
            for (int i = 0; i < 5; i++) {
                g2.drawLine(xPoints[i], yPoints[i], xPoints[i+1], yPoints[i+1]);
            }

            // Gambar Titik (Dots)
            g2.setColor(Color.WHITE);
            for (int i = 0; i < 6; i++) {
                g2.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                g2.setColor(RED);
                g2.drawOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                g2.setColor(Color.WHITE);
            }
            g2.dispose();
        }
    }

    private class HorizontalBarChart extends JPanel {
        public HorizontalBarChart() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            String[] categories = {"Teknologi", "Bisnis", "Kesenian", "Medis"};
            Color[] colors = {RED, BLUE, new Color(192, 38, 211), MUTED};
            int[] values = {85, 60, 40, 20}; // Dalam persen visual

            int y = 40;
            int barHeight = 16;
            for (int i = 0; i < 4; i++) {
                g2.setColor(TEXT);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString(categories[i], 10, y);

                g2.setColor(RED);
                g2.drawString("Rp " + (values[i] * 1.5) + " Jt", getWidth() - 80, y);

                // Bar Background
                g2.setColor(new Color(239, 243, 255));
                g2.fillRoundRect(10, y + 10, getWidth() - 100, barHeight, barHeight, barHeight);

                // Bar Foreground
                g2.setColor(colors[i]);
                int barWidth = (getWidth() - 100) * values[i] / 100;
                g2.fillRoundRect(10, y + 10, barWidth, barHeight, barHeight, barHeight);

                y += 55;
            }
            g2.dispose();
        }
    }

    private static class SquareIcon extends JPanel {
        Color fg, bg;
        SquareIcon(Color fg, Color bg) { this.fg = fg; this.bg = bg; setPreferredSize(new Dimension(48, 48)); setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg); g2.fillRoundRect(0,0,48,48,12,12);
            g2.setColor(fg); g2.fillRoundRect(14,14,20,20,6,6);
            g2.dispose();
        }
    }

    private static class CircleIcon extends JPanel {
        Color fg, bg;
        CircleIcon(Color fg, Color bg) { this.fg = fg; this.bg = bg; setPreferredSize(new Dimension(48, 48)); setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fg); g2.fillOval(0,0,48,48);
            g2.setColor(bg); g2.fillOval(16,16,16,16);
            g2.dispose();
        }
    }

    // =========================================================================
    // KUMPULAN UTILITAS BAWAAN REPO
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

    private static class ScrollableHost extends JPanel implements Scrollable {
        private ScrollableHost(JComponent content) {
            setOpaque(false); setLayout(new BorderLayout()); add(content, BorderLayout.CENTER);
        }
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return o == SwingConstants.VERTICAL ? r.height : r.width; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }

    private static class BorderedPanel extends JPanel {
        private final int radius; private final Color background, border;
        private BorderedPanel(int radius, Color background, Color border) {
            this.radius = radius; this.background = background; this.border = border; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g); Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background); g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.setColor(border); g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
        }
    }
}