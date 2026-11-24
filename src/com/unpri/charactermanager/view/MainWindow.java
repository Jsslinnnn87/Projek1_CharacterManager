package com.unpri.charactermanager.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;

public class MainWindow extends JFrame {

    private ArrayList<Character> characterList;     // data dari Main.java (Model)

    public MainWindow(ArrayList<Character> characterList) {
        super("Character Manager " + AppTheme.VERSION);
        this.characterList = characterList;

        initComponents();                           // 1. susun semua area

        pack();                                     // 2. ukuran awal = preferred size isi
        setMinimumSize(new Dimension(560, 380));    // 3. batas terkecil yang masih rapi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);                // 4. pusatkan SETELAH pack()
    }

    private void initComponents() {
        add(createNorthPanel(), BorderLayout.NORTH);       // header + panel statistik
        setLayout(new BorderLayout());                     // kerangka 4 wilayah (JFrame meneruskan ke content pane)

        add(createHeaderPanel(), BorderLayout.NORTH);      // judul + jumlah
        add(createListArea(),    BorderLayout.CENTER);     // daftar berscroll: satu-satunya yang tumbuh
        add(createLegendPanel(), BorderLayout.EAST);       // legenda role
        add(createStatusPanel(), BorderLayout.SOUTH);      // bar status
    }

    /** NORTH: judul di kiri, jumlah karakter di kanan. */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());     // panel bersarang dengan layout sendiri
        header.setBackground(AppTheme.BG_MAIN);
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16)); // atas, kiri, bawah, kanan

        JLabel title = new JLabel("Character Manager");
        title.setFont(AppTheme.FONT_TITLE);
        title.setForeground(AppTheme.TEXT_MAIN);

        JLabel count = new JLabel(characterList.size() + " karakter");
        count.setFont(AppTheme.FONT_BODY);
        count.setForeground(AppTheme.GOLD);

        header.add(title, BorderLayout.WEST);               // rata kiri
        header.add(count, BorderLayout.EAST);               // rata kanan
        return header;
    }

    /** NORTH: header di atas, panel statistik 2x2 di bawahnya. */
private JPanel createNorthPanel() {
    JPanel north = new JPanel(new BorderLayout());
    north.setBackground(AppTheme.BG_MAIN);
    north.add(createHeaderPanel(), BorderLayout.NORTH);
    north.add(createStatsPanel(),  BorderLayout.CENTER);
    return north;
}

/** Panel statistik: GridLayout 2x2, sel berukuran sama. */
private JPanel createStatsPanel() {
    int total = characterList.size();
    int maxLevel = 0;
    int sum = 0;
    Set<CharacterType> roles = new HashSet<>();
    for (Character c : characterList) {
        sum += c.getLevel();
        if (c.getLevel() > maxLevel) {
            maxLevel = c.getLevel();
        }
        roles.add(c.getType());
    }
    double avg = (total == 0) ? 0 : (double) sum / total;

    JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8));   // 2 baris, 2 kolom, jarak 8 px
    grid.setBackground(AppTheme.BG_MAIN);
    grid.setBorder(BorderFactory.createEmptyBorder(0, 16, 8, 16));

    grid.add(createStatCell("Total karakter",  String.valueOf(total)));
    grid.add(createStatCell("Level tertinggi", String.valueOf(maxLevel)));
    grid.add(createStatCell("Rata-rata level", String.format("%.1f", avg)));
    grid.add(createStatCell("Jumlah role",     String.valueOf(roles.size())));
    return grid;
}

/** Satu sel statistik: judul di kiri, nilai di kanan. */
private JPanel createStatCell(String title, String value) {
    JPanel cell = new JPanel(new BorderLayout());
    cell.setBackground(AppTheme.BG_CARD);
    cell.setBorder(AppTheme.CARD_BORDER);

    JLabel t = new JLabel(title);
    t.setFont(AppTheme.FONT_MONO);
    t.setForeground(AppTheme.TEXT_MUTED);

    JLabel v = new JLabel(value);
    v.setFont(AppTheme.FONT_BODY.deriveFont(java.awt.Font.BOLD));
    v.setForeground(AppTheme.GOLD);

    cell.add(t, BorderLayout.WEST);
    cell.add(v, BorderLayout.EAST);
    return cell;
}

    /** CENTER: kartu karakter di dalam JScrollPane. */
    private JScrollPane createListArea() {
        JPanel cards = new JPanel(new GridLayout(0, 1, 0, 8)); // satu kolom, jarak 8 px
        cards.setBackground(AppTheme.BG_MAIN);
        for (Character c : characterList) {
            cards.add(createCharacterLabel(c));              // kartu ditambah ke PANEL, bukan ke scroll pane
        }

        // holder: menaruh cards di NORTH agar kartu mempertahankan tinggi alaminya
        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(AppTheme.BG_MAIN);
        holder.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 8));
        holder.add(cards, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(holder);        // bungkus HOLDER
        scroll.setBorder(null);                              // buang border bawaan
        scroll.getViewport().setBackground(AppTheme.BG_MAIN);
        scroll.setPreferredSize(new Dimension(460, 280));    // tanpa ini, 50 karakter = jendela raksasa
        return scroll;
    }

    /** Satu kartu karakter (sama seperti v0.4). */
    private JLabel createCharacterLabel(Character c) {
        String info = c.getName() + " | " + c.getType()
                    + " | Lv." + c.getLevel()
                    + " | HP:" + c.getHitPoints()
                    + " | ATK:" + c.getAttackPower();

        JLabel lbl = new JLabel(info);
        lbl.setFont(AppTheme.FONT_BODY);
        lbl.setForeground(AppTheme.TEXT_LIGHT);
        lbl.setBackground(AppTheme.colorFor(c.getType()));
        lbl.setForeground(AppTheme.TEXT_LIGHT);
        lbl.setBackground(AppTheme.colorFor(c.getType()));
        lbl.setOpaque(true);
        lbl.setOpaque(true);                                 // wajib agar background tampak
        lbl.setBorder(AppTheme.CARD_BORDER);
        lbl.setToolTipText(AppTheme.describe(c.getType()));
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return lbl;
    }

    /** EAST: legenda warna per role, ditumpuk vertikal dengan BoxLayout. */
    private JPanel createLegendPanel() {
        JPanel legend = new JPanel();
        legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS)); // target = panel ini sendiri!
        legend.setBackground(AppTheme.BG_MAIN);
        legend.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 16));

        JLabel head = new JLabel("Legenda Role");
        head.setFont(AppTheme.FONT_BODY.deriveFont(java.awt.Font.BOLD)); // tebalkan tanpa tahu nama font
        head.setForeground(AppTheme.TEXT_MAIN);
        legend.add(head);
        legend.add(Box.createVerticalStrut(8));             // spasi tetap 8 px

        for (CharacterType type : CharacterType.values()) {  // satu chip per role
            JLabel chip = new JLabel(type.name());
            chip.setOpaque(true);
            chip.setBackground(AppTheme.colorFor(type));
            chip.setForeground(AppTheme.TEXT_LIGHT);
            chip.setFont(AppTheme.FONT_MONO);
            chip.setBorder(AppTheme.CARD_BORDER);
            chip.setToolTipText(AppTheme.describe(type));
            legend.add(chip);
            legend.add(Box.createVerticalStrut(6));
        }
        legend.add(Box.createVerticalGlue());               // dorong semua ke atas
        return legend;
    }

    private JPanel createStatusPanel() {
    JPanel status = new JPanel(new BorderLayout());
    status.setBackground(AppTheme.BG_STATUS);
    status.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

    JLabel lbl = new JLabel("Total: " + characterList.size() + " karakter  |  Siap");
    lbl.setFont(AppTheme.FONT_MONO);
    lbl.setForeground(AppTheme.TEXT_MUTED);

    JLabel version = new JLabel(AppTheme.VERSION);
    version.setFont(AppTheme.FONT_MONO);
    version.setForeground(AppTheme.TEXT_MUTED);

    status.add(lbl, BorderLayout.WEST);     // status tetap di kiri
    status.add(version, BorderLayout.EAST); // versi menempel di kanan
    return status;
}
}