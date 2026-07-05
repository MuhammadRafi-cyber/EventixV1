package org.example.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import dao.AuditLogDAO;
import dao.UserDAO;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterForm extends JFrame {

    // --- Palet Warna (Sama persis dengan LoginForm) ---
    private static final Color BG_COLOR = new Color(249, 249, 255);
    private static final Color CARD_BG = new Color(249, 249, 255);
    private static final Color BORDER_COLOR = new Color(229, 189, 190);
    private static final Color TEXT_DARK = new Color(17, 28, 45);
    private static final Color TEXT_MUTED = new Color(92, 63, 64);
    private static final Color RED_MAIN = new Color(225, 29, 72); // #E11D48

    // --- VARIABEL INPUT (Dibutuhkan untuk Backend) ---
    private JTextField nameField;
    private JTextField institutionField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox policyCheck;

    // --- CONTROLLER BACKEND ---
    private AuthController authController;

    public RegisterForm() {
        // Inisialisasi Backend
        AuthService authService = new AuthService(new UserDAO(), new AuditLogDAO());
        this.authController = new AuthController(authService);

        setTitle("Eventix - Register");
        setSize(1280, 780); // Ukuran window disamakan dengan LoginForm
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        // 1. Panel Background dengan efek Blur/Ambient
        JPanel backgroundPanel = new AmbientBackground();
        backgroundPanel.setLayout(new GridBagLayout()); // Untuk menengahkan Card

        // 2. Card Container (Lebih lebar dari Login agar muat 2 kolom)
        JPanel registerCard = new RoundedPanel(12, CARD_BG, new Color(229, 189, 190, 76));
        registerCard.setPreferredSize(new Dimension(580, 680));
        registerCard.setLayout(new BoxLayout(registerCard, BoxLayout.Y_AXIS));
        registerCard.setBorder(new EmptyBorder(32, 40, 32, 40));

        // --- SECTION A: HEADING ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleText = new JLabel("Create Account");
        titleText.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleText.setForeground(TEXT_DARK);
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleText = new JLabel("Join Eventix Seminar Management Ecosystem");
        subtitleText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtitleText.setForeground(TEXT_MUTED);
        subtitleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleText);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subtitleText);
        headerPanel.add(Box.createVerticalStrut(28));

        // --- SECTION B: FORM INPUT ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Inisialisasi Field
        nameField = new JTextField();
        institutionField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        // Atur Placeholder (Watermark abu-abu di dalam textfield)
        setPlaceholder(nameField, "Dr. Jane Doe");
        setPlaceholder(institutionField, "ID Institusi (Opsional)");
        setPlaceholder(phoneField, "+62 812 3456");
        setPlaceholder(emailField, "jane@university.edu");
        setPlaceholder(usernameField, "janedoe_academic");
        setPlaceholder(passwordField, "••••••••");
        setPlaceholder(confirmPasswordField, "••••••••");

        // Baris 1: Role Selection
        JPanel roleRow = new JPanel(new GridLayout(1, 2, 16, 0));
        roleRow.setOpaque(false);
        JToggleButton participantBtn = createRoleButton("Participant");
        JToggleButton organizerBtn = createRoleButton("Organizer");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(participantBtn);
        roleGroup.add(organizerBtn);
        participantBtn.setSelected(true); // Default
        participantBtn.setForeground(RED_MAIN);
        roleRow.add(participantBtn);
        roleRow.add(organizerBtn);

        // Baris 2: Name & Institution
        JPanel row1 = new JPanel(new GridLayout(1, 2, 16, 0));
        row1.setOpaque(false);
        row1.add(createLabeledField("FULL NAME", createInputPanel(nameField, "images/Icon/RegistForm/User_Icon.svg")));
        row1.add(createLabeledField("INSTITUTION ID", createInputPanel(institutionField, "images/Icon/LoginForm/Institute_Icon.svg")));

        // Baris 3: Phone & Email
        JPanel row2 = new JPanel(new GridLayout(1, 2, 16, 0));
        row2.setOpaque(false);
        row2.add(createLabeledField("PHONE NUMBER", createInputPanel(phoneField, null))); // Tanpa icon
        row2.add(createLabeledField("EMAIL ADDRESS", createInputPanel(emailField, "images/Icon/LoginForm/Mail_Icon.svg")));

        // Baris 4: Password & Confirm Password
        JPanel row4 = new JPanel(new GridLayout(1, 2, 16, 0));
        row4.setOpaque(false);
        row4.add(createLabeledField("PASSWORD", createPasswordPanel(passwordField)));
        row4.add(createLabeledField("CONFIRM PASSWORD", createPasswordPanel(confirmPasswordField)));

        // Menyusun Form ke dalam Panel
        formPanel.add(createLabeledField("USER ROLE", roleRow));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(row1);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(row2);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(createLabeledField("USERNAME", createInputPanel(usernameField, "images/Icon/RegistForm/User_Icon.svg")));
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(row4);
        formPanel.add(Box.createVerticalStrut(12));

        // Policy Checkbox
        policyCheck = new JCheckBox("I agree to the Academic Policy and Privacy Registry.");
        policyCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        policyCheck.setForeground(TEXT_MUTED);
        policyCheck.setOpaque(false);
        policyCheck.setFocusPainted(false);
        formPanel.add(policyCheck);
        formPanel.add(Box.createVerticalStrut(24));

        // --- SECTION C: TOMBOL & LINK LOGIN ---
        JButton regBtn = new JButton("Register");
        regBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        regBtn.setForeground(Color.WHITE);
        regBtn.setBackground(RED_MAIN);
        regBtn.setFocusPainted(false);
        regBtn.setBorderPainted(false);
        regBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.setMaximumSize(new Dimension(500, 50));
        regBtn.setPreferredSize(new Dimension(500, 50));

        regBtn.addActionListener(e -> prosesRegistrasi());

        JPanel signInRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        signInRow.setOpaque(false);
        JLabel haveAccLabel = new JLabel("Already have an account?");
        haveAccLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        haveAccLabel.setForeground(TEXT_MUTED);

        JLabel signInLink = new JLabel("Sign In");
        signInLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signInLink.setForeground(RED_MAIN);
        signInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginForm().setVisible(true);
            }
        });

        signInRow.add(haveAccLabel);
        signInRow.add(signInLink);

        formPanel.add(regBtn);
        formPanel.add(Box.createVerticalStrut(16));
        formPanel.add(signInRow);

        // Satukan ke Card
        registerCard.add(headerPanel);
        registerCard.add(formPanel);

        backgroundPanel.add(registerCard);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    // --- INTEGRASI BACKEND KE TOMBOL ---
    private void prosesRegistrasi() {
        if (!policyCheck.isSelected()) {
            JOptionPane.showMessageDialog(this, "Anda harus menyetujui Policy & Privacy terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nama = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String noTelp = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        String instString = institutionField.getText().trim();
        Integer idInstitusi = null;
        if (!instString.isEmpty() && !instString.equals("ID Institusi (Opsional)")) {
            try {
                idInstitusi = Integer.parseInt(instString);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID Institusi harus berupa angka!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (!password.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Password dan Confirm Password tidak cocok!", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hasil = authController.registrasi(nama, username, email, password, noTelp, idInstitusi);

        if (hasil.startsWith("SUKSES|")) {
            String pesanSukses = hasil.split("\\|")[1];
            JOptionPane.showMessageDialog(this, pesanSukses + "\nSilakan Login untuk melanjutkan.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginForm().setVisible(true);
        } else {
            String pesanError = hasil.split("\\|")[1];
            JOptionPane.showMessageDialog(this, pesanError, "Gagal Daftar", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- UI UTILITY METHODS (Mirip dengan LoginForm) ---

    private void setPlaceholder(JTextField field, String text) {
        field.putClientProperty("JTextField.placeholderText", text);
    }

    private JPanel createLabeledField(String labelText, JComponent inputComp) {
        JPanel wrap = new JPanel(new BorderLayout(0, 4));
        wrap.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        wrap.add(lbl, BorderLayout.NORTH);
        wrap.add(inputComp, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel createInputPanel(JComponent field, String iconPath) {
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
        panel.setPreferredSize(new Dimension(0, 45));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setBorder(new EmptyBorder(0, 16, 0, 16));

        if (iconPath != null) {
            try {
                JLabel icon = new JLabel(new FlatSVGIcon(iconPath, 16, 16));
                panel.add(icon, BorderLayout.WEST);
            } catch (Exception ignored) {}
        }

        field.setBorder(null);
        field.setOpaque(false);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPasswordPanel(JPasswordField field) {
        JPanel panel = createInputPanel(field, "images/Icon/LoginForm/Lock_Icon.svg");
        try {
            JLabel eyeIcon = new JLabel(new FlatSVGIcon("images/Icon/LoginForm/Eye_Icon.svg", 18, 12));
            eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            eyeIcon.addMouseListener(new MouseAdapter() {
                boolean isVisible = false;
                @Override public void mouseClicked(MouseEvent e) {
                    isVisible = !isVisible;
                    field.setEchoChar(isVisible ? (char) 0 : '•');
                }
            });
            panel.add(eyeIcon, BorderLayout.EAST);
        } catch (Exception ignored) {}
        return panel;
    }

    private JToggleButton createRoleButton(String text) {
        JToggleButton btn = new JToggleButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(new Color(225, 29, 72, 20)); // Soft red fill
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2.setColor(RED_MAIN);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    g2.setColor(BORDER_COLOR);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(TEXT_MUTED);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 40));

        btn.addItemListener(e -> {
            if (btn.isSelected()) btn.setForeground(RED_MAIN);
            else btn.setForeground(TEXT_MUTED);
        });
        return btn;
    }

    // Card Panel Bulat
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
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Background Kustom dengan Efek Ambient Blur Simulation (Sama dengan Login)
    private static class AmbientBackground extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(0, 0, new Color(249, 249, 255), 0, getHeight(), Color.WHITE);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            RadialGradientPaint rpRed = new RadialGradientPaint(
                    getWidth() - 200, 100, 500,
                    new float[]{0f, 1f},
                    new Color[]{new Color(184, 0, 53, 20), new Color(184, 0, 53, 0)}
            );
            g2.setPaint(rpRed);
            g2.fillOval(getWidth() - 700, -200, 1000, 1000);

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
            new RegisterForm().setVisible(true);
        });
    }
}