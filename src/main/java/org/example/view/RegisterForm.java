package org.example.view;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.example.util.Theme;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterForm extends JFrame {

    private static final Color CARD_BORDER = new Color(244, 225, 228);
    private static final Color FIELD_BORDER = new Color(242, 216, 219);
    private static final Color FIELD_TEXT = new Color(91, 77, 88);
    private static final Color MUTED_TEXT = new Color(142, 113, 116);
    private static final Color SIDE_TOP = new Color(79, 94, 146);
    private static final Color SIDE_BOTTOM = new Color(95, 77, 124);

    private static final int CARD_WIDTH = 520;
    private static final int CARD_HEIGHT = 560;
    private static final int SIDE_WIDTH = 165;
    private static final int FIELD_HEIGHT = 34;
    private static final int GAP = 18;

    private JToggleButton participantButton;
    private JToggleButton organizerButton;

    public RegisterForm() {
        setTitle("Eventix - Register");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel background = new RegisterBackgroundPanel();
        background.setLayout(new GridBagLayout());

        JPanel card = new RoundedPanel(10, Color.WHITE, CARD_BORDER);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        card.add(createSidePanel(), BorderLayout.WEST);
        card.add(createFormPanel(), BorderLayout.CENTER);

        background.add(card);
        add(background);
    }

    private JPanel createSidePanel() {
        JPanel side = new GradientSidePanel();
        side.setPreferredSize(new Dimension(SIDE_WIDTH, CARD_HEIGHT));
        side.setBorder(new EmptyBorder(24, 20, 24, 20));
        side.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        logo.setOpaque(false);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.add(new JLabel(createSvgIcon("Eventix_Icon_White.svg", 18, 24)));

        JLabel logoText = new JLabel("Eventix");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 23));
        logoText.setForeground(Color.WHITE);
        logo.add(logoText);

        JLabel description = new JLabel("<html>Join the premier<br>academic seminar<br>management<br>ecosystem. Professional<br>precision for scholars<br>and organizers.</html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        description.setForeground(new Color(226, 231, 246));
        description.setBorder(new EmptyBorder(26, 2, 0, 0));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(logo);
        top.add(description);

        JPanel certified = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        certified.setOpaque(false);
        certified.add(new JLabel(createSvgIcon("Certivied_Icon.svg", 16, 16)));

        JLabel certifiedText = new JLabel("<html>CERTIFIED<br>PLATFORM</html>");
        certifiedText.setFont(new Font("Segoe UI", Font.BOLD, 10));
        certifiedText.setForeground(new Color(222, 224, 239));
        certified.add(certifiedText);

        side.add(top, BorderLayout.NORTH);
        side.add(certified, BorderLayout.SOUTH);
        return side;
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(26, 24, 20, 24));
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Fill in the details to join Eventix Seminar Management.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(MUTED_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(3));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(24));
        form.add(createLabel("USER ROLE", 0));
        form.add(Box.createVerticalStrut(7));
        form.add(createRoleRow());
        form.add(Box.createVerticalStrut(16));
        form.add(createTwoColumnRow(
                createLabeledField("FULL NAME", "Dr. Jane Doe", false),
                createLabeledField("INSTITUTION", "University", false)
        ));
        form.add(Box.createVerticalStrut(14));
        form.add(createTwoColumnRow(
                createLabeledField("PHONE NUMBER", "+1 (555) 000-0000", false),
                createLabeledField("EMAIL ADDRESS", "jane@university.edu", false)
        ));
        form.add(Box.createVerticalStrut(14));
        form.add(createLabeledField("USERNAME", "janedoe_academic", false));
        form.add(Box.createVerticalStrut(14));
        form.add(createTwoColumnRow(
                createLabeledField("PASSWORD", "\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022", true),
                createLabeledField("CONFIRM PASSWORD", "\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022", true)
        ));
        form.add(Box.createVerticalStrut(13));
        form.add(createPolicyRow());
        form.add(Box.createVerticalStrut(16));
        form.add(createRegisterButton());
        form.add(Box.createVerticalGlue());
        form.add(createSignInRow());

        return form;
    }

    private JPanel createRoleRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMinimumSize(new Dimension(0, 32));
        row.setPreferredSize(new Dimension(320, 32));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        participantButton = createRoleButton("Participant", "User_Icon.svg", "User_Icon_Red.svg");
        organizerButton = createRoleButton("Organizer", "Organizer_Icon.svg", "Organizer_Icon_Red.svg");

        ButtonGroup group = new ButtonGroup();
        group.add(participantButton);
        group.add(organizerButton);

        participantButton.setSelected(true);
        updateRoleButton(participantButton, "User_Icon.svg", "User_Icon_Red.svg");
        updateRoleButton(organizerButton, "Organizer_Icon.svg", "Organizer_Icon_Red.svg");

        row.add(participantButton);
        row.add(organizerButton);
        return row;
    }

    private JToggleButton createRoleButton(String text, String defaultIcon, String selectedIcon) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(6);
        button.setMargin(new Insets(6, 12, 6, 12));
        button.setPreferredSize(new Dimension(0, 32));
        button.setBorder(new RoundedBorder(8, FIELD_BORDER, 1));
        button.addActionListener(e -> {
            updateRoleButton(participantButton, "User_Icon.svg", "User_Icon_Red.svg");
            updateRoleButton(organizerButton, "Organizer_Icon.svg", "Organizer_Icon_Red.svg");
        });
        button.putClientProperty("defaultIcon", defaultIcon);
        button.putClientProperty("selectedIcon", selectedIcon);
        return button;
    }

    private void updateRoleButton(JToggleButton button, String defaultIcon, String selectedIcon) {
        boolean selected = button.isSelected();
        button.setIcon(createSvgIcon(selected ? selectedIcon : defaultIcon, 11, 11));
        button.setForeground(selected ? Theme.PRIMARY : FIELD_TEXT);
        button.setBackground(Color.WHITE);
        button.setBorder(new RoundedBorder(8, selected ? Theme.PRIMARY : FIELD_BORDER, selected ? 2 : 1));
    }

    private JPanel createTwoColumnRow(JPanel left, JPanel right) {
        JPanel row = new JPanel(new GridLayout(1, 2, GAP, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        row.add(left);
        row.add(right);
        return row;
    }

    private JPanel createLabeledField(String labelText, String placeholder, boolean password) {
        JPanel panel = new JPanel(new BorderLayout(0, 7));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JLabel label = createLabel(labelText, 0);
        label.setPreferredSize(new Dimension(1, 13));

        panel.add(label, BorderLayout.NORTH);
        panel.add(createInputField(placeholder, password), BorderLayout.CENTER);
        return panel;
    }

    private JLabel createLabel(String text, int leftPadding) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 9));
        label.setForeground(MUTED_TEXT);
        label.setBorder(new EmptyBorder(0, leftPadding, 0, 0));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setHorizontalTextPosition(SwingConstants.LEFT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        return label;
    }

    private JTextField createInputField(String placeholder, boolean password) {
        JTextField field = password ? new JPasswordField() : new JTextField();
        field.putClientProperty("JTextField.placeholderText", placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        field.setForeground(FIELD_TEXT);
        field.setCaretColor(Theme.PRIMARY);
        field.setBackground(Color.WHITE);
        field.setBorder(new CompoundRoundedBorder(7, FIELD_BORDER, new Insets(0, 12, 0, 12)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
        field.setPreferredSize(new Dimension(150, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(120, FIELD_HEIGHT));
        return field;
    }

    private JPanel createPolicyRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setBorder(new EmptyBorder(0, 0, 0, 0));
        checkBox.setIconTextGap(0);
        checkBox.setIcon(new CheckBoxIcon(false));
        checkBox.setSelectedIcon(new CheckBoxIcon(true));
        checkBox.setPreferredSize(new Dimension(12, 12));
        checkBox.setMaximumSize(new Dimension(12, 12));

        JLabel text = new JLabel("<html>I agree to the <span style='color:#E11D48'>Academic Policy</span> and <span style='color:#E11D48'>Privacy Registry.</span></html>");
        text.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        text.setForeground(FIELD_TEXT);

        row.add(checkBox);
        row.add(Box.createHorizontalStrut(5));
        row.add(text);
        return row;
    }

    private JButton createRegisterButton() {
        JButton button = new JButton("Register", createSvgIcon("Arrow_Icon.svg", 9, 9));
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(196, 0, 64));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setPreferredSize(new Dimension(320, 38));
        button.setIconTextGap(7);
        return button;
    }

    private JPanel createSignInRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));

        JLabel question = new JLabel("Already have an account?");
        question.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        question.setForeground(FIELD_TEXT);

        JLabel signIn = new JLabel(" Sign In");
        signIn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        signIn.setForeground(Theme.PRIMARY);
        signIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginForm().setVisible(true);
                dispose();
            }
        });

        row.add(question);
        row.add(signIn);
        return row;
    }

    private Icon createSvgIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/RegistForm/" + fileName, width, height);
    }

    private static class RegisterBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint base = new GradientPaint(
                    0, 0, new Color(255, 253, 254),
                    getWidth(), getHeight(), new Color(247, 249, 253)
            );
            g2.setPaint(base);
            g2.fillRect(0, 0, getWidth(), getHeight());

            RadialGradientPaint leftGlow = new RadialGradientPaint(
                    new Point(80, getHeight() - 55),
                    360,
                    new float[]{0f, 1f},
                    new Color[]{new Color(225, 29, 72, 95), new Color(225, 29, 72, 0)}
            );
            g2.setPaint(leftGlow);
            g2.fillRect(0, 0, getWidth(), getHeight());

            RadialGradientPaint rightGlow = new RadialGradientPaint(
                    new Point(getWidth() - 90, getHeight() / 2),
                    340,
                    new float[]{0f, 1f},
                    new Color[]{new Color(225, 29, 72, 80), new Color(225, 29, 72, 0)}
            );
            g2.setPaint(rightGlow);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
        }
    }

    private static class GradientSidePanel extends JPanel {
        private GradientSidePanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint paint = new GradientPaint(0, 0, SIDE_TOP, 0, getHeight(), SIDE_BOTTOM);
            g2.setPaint(paint);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() + 12, getHeight(), 10, 10));

            g2.dispose();
            super.paintComponent(g);
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

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final int thickness;

        private RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            int offset = Math.max(1, thickness / 2);
            g2.drawRoundRect(x + offset, y + offset, width - thickness - 1, height - thickness - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    private static class CheckBoxIcon implements Icon {
        private static final int SIZE = 12;
        private final boolean selected;

        private CheckBoxIcon(boolean selected) {
            this.selected = selected;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y, SIZE - 1, SIZE - 1, 3, 3);
            g2.setColor(selected ? Theme.PRIMARY : FIELD_BORDER);
            g2.drawRoundRect(x, y, SIZE - 1, SIZE - 1, 3, 3);

            if (selected) {
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 3, y + 6, x + 5, y + 8);
                g2.drawLine(x + 5, y + 8, x + 9, y + 3);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
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
