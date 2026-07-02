package org.example.view.panitia;

import org.example.util.AppTheme;

import javax.swing.*;

public class EditSeminar extends SeminarFormBase {

    public EditSeminar() {
        super("Edit Seminar", "Batalkan Edit", "Simpan Perubahan", SeminarFormData.sample());
    }

    public static void main(String[] args) {
        AppTheme.setup();
        SwingUtilities.invokeLater(() -> new EditSeminar().setVisible(true));
    }
}
