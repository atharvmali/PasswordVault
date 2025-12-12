package ui;

import db.VaultDAO;
import model.VaultItem;
import utils.AutoLockThread;
import security.EncryptionUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class VaultScreen extends JFrame {

    VaultDAO dao = new VaultDAO();
    ArrayList<VaultItem> list;
    JTable table;

    AutoLockThread autoLock;

    JTextField searchField;
    JComboBox<String> sortBox;
    JCheckBox showPasswordBox;

    public VaultScreen() {
        setTitle("Password Vault");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        autoLock = new AutoLockThread(this);
        autoLock.start();

        // -------------------
        // TOP PANEL (Search + Sort + Show Password)
        // -------------------
        JPanel topPanel = new JPanel();

        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            autoLock.resetTimer();
            searchItems();
        });

        sortBox = new JComboBox<>(new String[]{"Sort by ID", "Sort by Service", "Sort by Username"});
        sortBox.addActionListener(e -> {
            autoLock.resetTimer();
            sortItems();
        });

        showPasswordBox = new JCheckBox("Show Passwords");
        showPasswordBox.addActionListener(e -> {
            autoLock.resetTimer();
            loadTable(); // Reload table with password visible or masked
        });

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(sortBox);
        topPanel.add(showPasswordBox);

        add(topPanel, BorderLayout.NORTH);

        // -------------------
        // TABLE
        // -------------------
        String[] cols = {"ID", "Service", "Username", "Password"};
        table = new JTable(new DefaultTableModel(cols, 0));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // -------------------
        // BOTTOM BUTTONS
        // -------------------
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");

        addBtn.addActionListener(e -> { autoLock.resetTimer(); addItem(); });
        updateBtn.addActionListener(e -> { autoLock.resetTimer(); updateItem(); });
        deleteBtn.addActionListener(e -> { autoLock.resetTimer(); deleteItem(); });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addBtn);
        bottomPanel.add(updateBtn);
        bottomPanel.add(deleteBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        loadTable();
        setVisible(true);
    }

    // -------------------
    // LOAD TABLE
    // -------------------
    private void loadTable() {
        list = dao.getAllItems();

        // Search filter applied?
        String keyword = searchField.getText().trim().toLowerCase();
        if (!keyword.isEmpty()) {
            list = (ArrayList<VaultItem>) list.stream()
                    .filter(v -> v.getService().toLowerCase().contains(keyword)
                            || v.getUsername().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        // Sorting applied?
        applySorting();

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (VaultItem item : list) {

            String passDisplay;

            if (showPasswordBox.isSelected()) {
                passDisplay = EncryptionUtil.decrypt(item.getPassword());
            } else {
                passDisplay = "***";
            }

            model.addRow(new Object[]{
                    item.getId(),
                    item.getService(),
                    item.getUsername(),
                    passDisplay
            });
        }
    }

    // -------------------
    // ADD ITEM
    // -------------------
    private void addItem() {
        JTextField s = new JTextField();
        JTextField u = new JTextField();
        JTextField p = new JTextField();

        Object[] fields = {
                "Service:", s,
                "Username:", u,
                "Password:", p
        };

        int ok = JOptionPane.showConfirmDialog(this, fields, "Add Password", JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            dao.insertItem(new VaultItem(0, s.getText(), u.getText(), p.getText()));
            loadTable();
        }
    }

    // -------------------
    // UPDATE ITEM
    // -------------------
    private void updateItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) table.getValueAt(row, 0);
        VaultItem item = list.stream().filter(v -> v.getId() == id).findFirst().orElse(null);

        JTextField s = new JTextField(item.getService());
        JTextField u = new JTextField(item.getUsername());
        JTextField p = new JTextField(EncryptionUtil.decrypt(item.getPassword()));

        Object[] fields = {
                "Service:", s,
                "Username:", u,
                "Password:", p
        };

        int ok = JOptionPane.showConfirmDialog(this, fields, "Update Password", JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            item.setService(s.getText());
            item.setUsername(u.getText());
            item.setPassword(p.getText());

            dao.updateItem(item);
            loadTable();
        }
    }

    // -------------------
    // DELETE ITEM
    // -------------------
    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) table.getValueAt(row, 0);
        dao.deleteItem(id);
        loadTable();
    }

    // -------------------
    // SEARCH FEATURE
    // -------------------
    private void searchItems() {
        loadTable();
    }

    // -------------------
    // SORT FEATURE
    // -------------------
    private void sortItems() {
        loadTable();
    }

    private void applySorting() {
        String sort = (String) sortBox.getSelectedItem();

        if (sort.equals("Sort by Service")) {
            list.sort(Comparator.comparing(VaultItem::getService));
        }
        else if (sort.equals("Sort by Username")) {
            list.sort(Comparator.comparing(VaultItem::getUsername));
        }
        else {
            list.sort(Comparator.comparingInt(VaultItem::getId)); // Default
        }
    }
}
