package org.example.view.panitia;

import org.example.util.AppTheme;

import javax.swing.*;

public class BuatSeminar extends SeminarFormBase {

    public BuatSeminar() {
        super("Buat Seminar", "Batalkan", "Simpan", SeminarFormData.empty());
    }

    public static void main(String[] args) {
        AppTheme.setup();
        SwingUtilities.invokeLater(() -> new BuatSeminar().setVisible(true));
    }
}
