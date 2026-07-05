package org.example.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import dao.AuditLogDAO;
import dao.UserDAO;
import enums.Role;
import model.User;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginForm extends JFrame {

    // --- Palet Warna ---
    private static final Color BG_COLOR = new Color(249, 249, 255);
    private static final Color CARD_BG = new Color(249, 249, 255);
    private static final Color BORDER_COLOR = new Color(229, 189, 190);
    private static final Color TEXT_DARK = new Color(17, 28, 45);
    private static final Color TEXT_MUTED = new Color(92, 63, 64);
    private static final Color RED_MAIN = new Color(225, 29, 72); // Tombol Login (#E11D48)
    private static final Color RED_LOGO = new Color(184, 0, 53);  // Warna Logo (#B80035)

    // --- Komponen Input ---
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheck;

    // --- Controller ---
    private AuthController authController;

    public LoginForm() {
        // Inisialisasi Backend
        AuthService authService = new AuthService(new UserDAO(), new AuditLogDAO());
        this.authController = new AuthController(authService);

        setTitle("Eventix - Login");
        setSize(1280, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        // 1. Panel Background dengan efek Blur/Ambient
        JPanel backgroundPanel = new AmbientBackground();
        backgroundPanel.setLayout(new GridBagLayout()); // Untuk menengahkan Card Login

        // 2. Card Container (Boks Login)
        JPanel loginCard = new RoundedPanel(12, CARD_BG, new Color(229, 189, 190, 76));
        loginCard.setPreferredSize(new Dimension(440, 517));
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(new EmptyBorder(32, 32, 32, 32));

        // --- SECTION A: LOGO & HEADING ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Baris Logo + Eventix
        JPanel brandRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        brandRow.setOpaque(false);
        brandRow.add(new JLabel(new FlatSVGIcon("images/Icon/LoginForm/Eventix_Icon_Red.svg", 26, 35)));
        JLabel brandText = new JLabel("Eventix");
        brandText.setFont(new Font("Segoe UI", Font.BOLD, 32));
        brandText.setForeground(TEXT_DARK);
        brandRow.add(brandText);

        JLabel subtitleText = new JLabel("SEMINAR MANAGEMENT SYSTEM");
        subtitleText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtitleText.setForeground(TEXT_MUTED);
        subtitleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(brandRow);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subtitleText);
        headerPanel.add(Box.createVerticalStrut(32)); // Margin bawah 32px

        // --- SECTION B: FORM INPUT ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // 1. Kolom Email
        JPanel emailWrap = new JPanel(new BorderLayout(0, 4));
        emailWrap.setOpaque(false);
        JLabel emailLabel = new JLabel("EMAIL ADDRESS");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setForeground(TEXT_MUTED);
        emailWrap.add(emailLabel, BorderLayout.NORTH);

        JPanel emailInputPanel = createInputPanel("images/Icon/LoginForm/Mail_Icon.svg");
        emailField = new JTextField();
        emailField.setBorder(null);
        emailField.setOpaque(false);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailInputPanel.add(emailField, BorderLayout.CENTER);
        emailWrap.add(emailInputPanel, BorderLayout.CENTER);

        // 2. Kolom Password
        JPanel passWrap = new JPanel(new BorderLayout(0, 4));
        passWrap.setOpaque(false);

        JPanel passLabelRow = new JPanel(new BorderLayout());
        passLabelRow.setOpaque(false);
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(TEXT_MUTED);

        JLabel forgotLabel = new JLabel("Forgot password?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLabel.setForeground(TEXT_MUTED);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        passLabelRow.add(passLabel, BorderLayout.WEST);
        passLabelRow.add(forgotLabel, BorderLayout.EAST);
        passWrap.add(passLabelRow, BorderLayout.NORTH);

        JPanel passInputPanel = createInputPanel("images/Icon/LoginForm/Lock_Icon.svg");
        passwordField = new JPasswordField();
        passwordField.setBorder(null);
        passwordField.setOpaque(false);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel eyeIcon = new JLabel(new FlatSVGIcon("images/Icon/LoginForm/Eye_Icon.svg", 18, 12));
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Fitur sederhana show/hide password
        eyeIcon.addMouseListener(new MouseAdapter() {
            boolean isVisible = false;
            @Override public void mouseClicked(MouseEvent e) {
                isVisible = !isVisible;
                passwordField.setEchoChar(isVisible ? (char) 0 : '•');
            }
        });

        passInputPanel.add(passwordField, BorderLayout.CENTER);
        passInputPanel.add(eyeIcon, BorderLayout.EAST);
        passWrap.add(passInputPanel, BorderLayout.CENTER);

        // 3. Remember Me
        JPanel rememberRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        rememberRow.setOpaque(false);
        rememberMeCheck = new JCheckBox("Remember Me");
        rememberMeCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberMeCheck.setForeground(TEXT_MUTED);
        rememberMeCheck.setOpaque(false);
        rememberMeCheck.setFocusPainted(false);
        rememberRow.add(rememberMeCheck);

        formPanel.add(emailWrap);
        formPanel.add(Box.createVerticalStrut(16));
        formPanel.add(passWrap);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(rememberRow);
        formPanel.add(Box.createVerticalStrut(24));

        // --- SECTION C: TOMBOL & LINK DAFTAR ---
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(RED_MAIN);
        loginBtn.setIcon(new FlatSVGIcon("images/Icon/LoginForm/LogIn_Icon.svg", 16, 16));
        loginBtn.setIconTextGap(8);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(374, 56));
        loginBtn.setPreferredSize(new Dimension(374, 56));

        // Aksi Tombol Login ke Database Backend
        loginBtn.addActionListener(e -> prosesLogin());

        // Divider
        JPanel dividerPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(229, 189, 190, 76)); // Transparan 30%
                g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
            }
        };
        dividerPanel.setOpaque(false);
        dividerPanel.setPreferredSize(new Dimension(374, 20));
        dividerPanel.setMaximumSize(new Dimension(374, 20));

        JPanel signUpRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        signUpRow.setOpaque(false);
        JLabel noAccLabel = new JLabel("Don't have an account?");
        noAccLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccLabel.setForeground(TEXT_MUTED);

        JLabel signUpLink = new JLabel("Sign up");
        signUpLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signUpLink.setForeground(RED_MAIN);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                dispose();
                new RegisterForm().setVisible(true);
            }
        });

        signUpRow.add(noAccLabel);
        signUpRow.add(signUpLink);

        formPanel.add(loginBtn);
        formPanel.add(Box.createVerticalStrut(24));
        formPanel.add(dividerPanel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(signUpRow);

        // Satukan ke Card
        loginCard.add(headerPanel);
        loginCard.add(formPanel);

        backgroundPanel.add(loginCard); // GridBagLayout akan otomatis menengahkan
        add(backgroundPanel, BorderLayout.CENTER);
    }

    // --- FUNGSI LOGIN KE BACKEND ---
    private void prosesLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Panggil fungsi login dari AuthController
        String hasil = authController.login(email, password);

        if (hasil.startsWith("SUKSES|")) {
            // Ambil User yang berhasil login
            User userAktif = AuthController.getUserAktif();

            // Redirect ke halaman yang sesuai Role
            if (userAktif.getRole() == Role.PANITIA || userAktif.getRole() == Role.ADMIN) {
                new org.example.view.panitia.DashboardPanitia().setVisible(true);
            } else {
                new org.example.view.Peserta.DashboardPeserta().setVisible(true);
            }
            dispose(); // Tutup form login
        } else {
            // Tampilkan pesan error dari backend
            String pesanError = hasil.split("\\|")[1];
            JOptionPane.showMessageDialog(this, pesanError, "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- UI UTILITY METHODS ---

    // Membuat boks input dengan background putih, rounded border, dan ikon di kiri
    private JPanel createInputPanel(String iconPath) {
        JPanel panel = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(374, 50));
        panel.setMaximumSize(new Dimension(374, 50));
        panel.setBorder(new EmptyBorder(0, 16, 0, 16));

        JLabel icon = new JLabel(new FlatSVGIcon(iconPath, 16, 16));
        panel.add(icon, BorderLayout.WEST);

        return panel;
    }

    // Card Panel Bulat
    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg, border;
        public RoundedPanel(int radius, Color bg, Color border) {
            this.radius = radius; this.bg = bg; this.border = border;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Background Kustom dengan Efek Ambient Blur Simulation
    private static class AmbientBackground extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. Latar Belakang Linear Gradient (#F9F9FF ke Putih)
            GradientPaint gp = new GradientPaint(0, 0, new Color(249, 249, 255), 0, getHeight(), Color.WHITE);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // 2. Bola Blur Kanan Atas (Merah B80035, 5% Opacity)
            RadialGradientPaint rpRed = new RadialGradientPaint(
                    getWidth() - 200, 100, 500,
                    new float[]{0f, 1f},
                    new Color[]{new Color(184, 0, 53, 20), new Color(184, 0, 53, 0)}
            );
            g2.setPaint(rpRed);
            g2.fillOval(getWidth() - 700, -200, 1000, 1000);

            // 3. Bola Blur Kiri Bawah (Biru 4F5C8E, 5% Opacity)
            RadialGradientPaint rpBlue = new RadialGradientPaint(
                    100, getHeight() - 100, 400,
                    new float[]{0f, 1f},
                    new Color[]{new Color(79, 92, 142, 20), new Color(79, 92, 142, 0)}
            );
            g2.setPaint(rpBlue);
            g2.fillOval(-300, getHeight() - 500, 800, 800);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}