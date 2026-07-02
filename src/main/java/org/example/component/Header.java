package org.example.component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import controller.AuthController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Header extends JPanel {

    private static final Color TEXT = new Color(18, 28, 45);
    private static final Color MUTED = new Color(88, 68, 72);
    private static final Color BORDER = new Color(239, 188, 195);

    public Header(String halaman) {
        setPreferredSize(new Dimension(0, 64));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 30, 12, 34));

        add(createSearch(), BorderLayout.WEST);
        add(createActions(), BorderLayout.EAST);
    }

    private JPanel createSearch() {
        JTextField search = new JTextField();
        search.putClientProperty("JTextField.placeholderText", "Cari seminar, sertifikat...");
        search.putClientProperty("JTextField.leadingIcon", dashboardIcon("Search_Icon.svg", 12, 12));
        search.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        search.setBorder(new EmptyBorder(0, 12, 0, 12));
        search.setBackground(new Color(243, 246, 255));
        search.setPreferredSize(new Dimension(384, 36));

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(search);
        return wrapper;
    }

    private JPanel createActions() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        actions.setOpaque(false);
        actions.add(new JLabel(dashboardIcon("Bell_Icon.svg", 15, 15)));
        actions.add(new JLabel(dashboardIcon("Help_Icon.svg", 15, 15)));

        JSeparator vertical = new JSeparator(SwingConstants.VERTICAL);
        vertical.setPreferredSize(new Dimension(1, 32));
        vertical.setForeground(BORDER);
        actions.add(vertical);
        actions.add(createProfile());
        return actions;
    }

    private JPanel createProfile() {
        JPanel profile = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        profile.setOpaque(false);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        User user = AuthController.getUserAktif();
        String namaUser = user != null ? user.getNama() : "Panitia";
        String roleUser = user != null ? user.getRole().name() : "PANITIA";

        JLabel name = new JLabel(namaUser);
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));
        name.setForeground(TEXT);
        name.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel role = new JLabel(roleUser);
        role.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        role.setForeground(MUTED);
        role.setAlignmentX(Component.RIGHT_ALIGNMENT);

        text.add(name);
        text.add(role);
        profile.add(text);
        profile.add(new UserAvatar());
        return profile;
    }

    private Icon dashboardIcon(String fileName, int width, int height) {
        return new FlatSVGIcon("images/Icon/Dashboard/" + fileName, width, height);
    }

    private static class UserAvatar extends JPanel {
        private UserAvatar() {
            setOpaque(false);
            setPreferredSize(new Dimension(34, 34));
            setLayout(new GridBagLayout());
            add(new JLabel(new FlatSVGIcon("images/Icon/Dashboard/user_icon.svg", 24, 24)));
        }
    }
}
