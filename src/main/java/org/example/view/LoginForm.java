package org.example.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import dao.AuditLogDAO;
import dao.UserDAO;
import enums.Role;
import model.User;
import org.example.view.Peserta.DashboardPeserta;
import org.example.view.panitia.DashboardPanitia;
import service.AuthService;
import org.example.util.Theme;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginForm extends JFrame {

    private static final Color CARD_BORDER = new Color(244, 210, 218);
    private static final Color FIELD_BORDER = new Color(238, 181, 191);
    private static final Color FIELD_TEXT = new Color(95, 75, 88);
    private static final Color DIVIDER_COLOR = new Color(246, 224, 229);

    private static final int CONTROL_WIDTH = 300;
    private static final int CONTROL_HEIGHT = 42;

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JCheckBox chkRemember;
    private JButton btnLogin;
    private JButton btnGoogle;
    private JButton btnInstitution;

    public LoginForm() {
        setTitle("Eventix - Login");
        setSize(1100, 630);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel background = new SoftBackgroundPanel();
        background.setLayout(new GridBagLayout());
        background.setBorder(new EmptyBorder(18, 0, 18, 0));

        JPanel card = new RoundedPanel(14, Color.WHITE, CARD_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 28, 24, 28));
        card.setPreferredSize(new Dimension(370, 480));
        card.setMaximumSize(new Dimension(370, 480));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 0));
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JLabel logoImage = new JLabel(createSvgIcon("Eventix_Icon_Red.svg", 22, 28));

        JLabel lblLogo = new JLabel("Eventix");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblLogo.setForeground(Theme.SECONDARY);

        logoPanel.add(logoImage);
        logoPanel.add(lblLogo);

        JLabel lblSubtitle = new JLabel("SEMINAR MANAGEMENT SYSTEM");
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblSubtitle.setForeground(new Color(85, 75, 88));

        JLabel lblEmail = createLabel("EMAIL ADDRESS");
        txtEmail = createInputField(
                "e.g. admin@university.edu",
                createSvgIcon("Mail_Icon.svg", 11, 9),
                null,
                false
        );

        JPanel passwordHeader = createControlRow();
        passwordHeader.setLayout(new BorderLayout());

        JLabel lblPassword = createLabel("PASSWORD");
        JLabel lblForgot = new JLabel("Forgot Password?");
        lblForgot.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblForgot.setForeground(Theme.PRIMARY);

        passwordHeader.add(lblPassword, BorderLayout.WEST);
        passwordHeader.add(lblForgot, BorderLayout.EAST);

        txtPassword = (JPasswordField) createInputField(
                "\u2022 \u2022 \u2022 \u2022 \u2022 \u2022 \u2022 \u2022",
                createSvgIcon("Lock_Icon.svg", 9, 12),
                createSvgIcon("Eye_Icon.svg", 12, 8),
                true
        );

        chkRemember = new JCheckBox("Keep me signed in");
        chkRemember.setOpaque(false);
        chkRemember.setFocusPainted(false);
        chkRemember.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        chkRemember.setForeground(new Color(88, 76, 88));
        chkRemember.setIconTextGap(7);
        chkRemember.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkRemember.setMaximumSize(new Dimension(CONTROL_WIDTH, 22));
        chkRemember.setPreferredSize(new Dimension(CONTROL_WIDTH, 22));

        btnLogin = new JButton("LOGIN  \u2192");
        btnLogin.setBackground(Theme.PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(CONTROL_WIDTH, 46));
        btnLogin.setPreferredSize(new Dimension(CONTROL_WIDTH, 46));
        btnLogin.addActionListener(e -> handleLogin());
        getRootPane().setDefaultButton(btnLogin);

        JSeparator separator = new JSeparator();
        separator.setForeground(DIVIDER_COLOR);
        separator.setMaximumSize(new Dimension(CONTROL_WIDTH, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        registerPanel.setOpaque(false);
        registerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerPanel.setMaximumSize(new Dimension(CONTROL_WIDTH, 18));

        JLabel lblQuestion = new JLabel("Don't have an account?");
        lblQuestion.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblQuestion.setForeground(new Color(95, 85, 96));

        JLabel lblRegister = new JLabel(" Register");
        lblRegister.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblRegister.setForeground(Theme.PRIMARY);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterForm().setVisible(true);
                dispose();
            }
        });

        registerPanel.add(lblQuestion);
        registerPanel.add(lblRegister);

        JPanel dividerPanel = createDividerWithText("OR CONNECT WITH");

        btnGoogle = createSocialButton("Google", "LogIn_Icon.svg");
        btnInstitution = createSocialButton("Institusi", "Institute_Icon.svg");

        JPanel socialPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        socialPanel.setOpaque(false);
        socialPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        socialPanel.setMaximumSize(new Dimension(CONTROL_WIDTH, 36));
        socialPanel.setPreferredSize(new Dimension(CONTROL_WIDTH, 36));
        socialPanel.add(btnGoogle);
        socialPanel.add(btnInstitution);

        card.add(logoPanel);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSubtitle);
        card.add(Box.createVerticalStrut(29));
        card.add(lblEmail);
        card.add(Box.createVerticalStrut(7));
        card.add(txtEmail);
        card.add(Box.createVerticalStrut(15));
        card.add(passwordHeader);
        card.add(Box.createVerticalStrut(7));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(12));
        card.add(chkRemember);
        card.add(Box.createVerticalStrut(19));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(22));
        card.add(separator);
        card.add(Box.createVerticalStrut(18));
        card.add(registerPanel);
        card.add(Box.createVerticalStrut(13));
        card.add(dividerPanel);
        card.add(Box.createVerticalStrut(12));
        card.add(socialPanel);

        background.add(card);
        add(background);
    }

    private JPanel createControlRow() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(CONTROL_WIDTH, 18));
        panel.setPreferredSize(new Dimension(CONTROL_WIDTH, 18));
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 9));
        label.setForeground(new Color(76, 62, 76));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(CONTROL_WIDTH, 16));
        label.setPreferredSize(new Dimension(CONTROL_WIDTH, 16));
        return label;
    }

    private JTextField createInputField(String placeholder, Icon leadingIcon, Icon trailingIcon, boolean password) {
        JTextField field = password ? new JPasswordField() : new JTextField();

        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.putClientProperty("JTextField.leadingIcon", leadingIcon);

        if (trailingIcon != null) {
            field.putClientProperty("JTextField.trailingIcon", trailingIcon);
        }

        field.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        field.setForeground(FIELD_TEXT);
        field.setCaretColor(Theme.PRIMARY);
        field.setBackground(Color.WHITE);
        field.setBorder(new CompoundRoundedBorder(8, FIELD_BORDER, new Insets(0, 28, 0, 27)));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setMaximumSize(new Dimension(CONTROL_WIDTH, CONTROL_HEIGHT));
        field.setPreferredSize(new Dimension(CONTROL_WIDTH, CONTROL_HEIGHT));
        field.setMinimumSize(new Dimension(CONTROL_WIDTH, CONTROL_HEIGHT));

        return field;
    }

    private JPanel createDividerWithText(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(CONTROL_WIDTH, 18));
        panel.setPreferredSize(new Dimension(CONTROL_WIDTH, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JSeparator leftLine = new JSeparator();
        leftLine.setForeground(DIVIDER_COLOR);
        panel.add(leftLine, gbc);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 9));
        label.setForeground(new Color(205, 166, 176));
        label.setBorder(new EmptyBorder(0, 10, 0, 10));

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);

        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JSeparator rightLine = new JSeparator();
        rightLine.setForeground(DIVIDER_COLOR);
        panel.add(rightLine, gbc);

        return panel;
    }

    private JButton createSocialButton(String text, String iconPath) {
        JButton button = new JButton(text, createSvgIcon(iconPath, 16, 16));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setForeground(new Color(39, 51, 89));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new CompoundRoundedBorder(8, FIELD_BORDER, new Insets(0, 10, 0, 10)));
        button.setIconTextGap(7);
        return button;
    }

    private Icon createSvgIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/LoginForm/" + fileName, width, height);
    }

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        AuthController authController = new AuthController(new AuthService(new UserDAO(), new AuditLogDAO()));
        String hasil = authController.login(email, password);
        if (!hasil.startsWith("SUKSES|")) {
            JOptionPane.showMessageDialog(this, hasil.replace("ERROR|", ""), "Login gagal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = AuthController.getUserAktif();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Sesi login tidak ditemukan.", "Login gagal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user.getRole() == Role.PANITIA) {
            new DashboardPanitia().setVisible(true);
            dispose();
        } else if (user.getRole() == Role.PESERTA) {
            new DashboardPeserta().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Dashboard untuk role " + user.getRole() + " belum tersedia.", "Login berhasil", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static class SoftBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint base = new GradientPaint(
                    0, 0, new Color(252, 250, 255),
                    getWidth(), getHeight(), new Color(246, 248, 255)
            );
            g2.setPaint(base);
            g2.fillRect(0, 0, getWidth(), getHeight());

            RadialGradientPaint glow = new RadialGradientPaint(
                    new Point(getWidth() / 3, getHeight() / 2),
                    Math.max(getWidth(), getHeight()) / 2f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(255, 231, 238, 175), new Color(255, 255, 255, 0)}
            );
            g2.setPaint(glow);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
        }
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color background;
        private final Color border;

        private RoundedPanel(int radius, Color background, Color border) {
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

    private static class CompoundRoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final Insets padding;

        private CompoundRoundedBorder(int radius, Color color, Insets padding) {
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
}
