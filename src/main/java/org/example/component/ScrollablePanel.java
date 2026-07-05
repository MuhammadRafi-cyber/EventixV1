package org.example.component;

import javax.swing.*;
import java.awt.*;

/**
 * Komponen ajaib untuk mengatasi bug "gepeng" pada JScrollPane Java Swing.
 * Memaksa lebar mengikuti layar (100%), tapi membiarkan tinggi bebas (Auto-Scroll).
 */
public class ScrollablePanel extends JPanel implements Scrollable {

    public ScrollablePanel() {
        super();
    }

    public ScrollablePanel(LayoutManager layout) {
        super(layout);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 25; // Kecepatan scroll mouse yang halus
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 50;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true; // KUNCI UTAMA: Mencegah komponen terkompresi secara horizontal
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false; // KUNCI UTAMA: Membiarkan komponen memanjang ke bawah
    }
}