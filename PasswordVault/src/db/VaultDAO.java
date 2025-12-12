package db;

import model.VaultItem;
import security.EncryptionUtil;

import java.sql.*;
import java.util.ArrayList;

public class VaultDAO {

    public boolean masterPasswordExists() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM master_password LIMIT 1");
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setMasterPassword(String pwd) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO master_password(password) VALUES(?)");
            ps.setString(1, EncryptionUtil.encrypt(pwd));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateMasterPassword(String pwd) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT password FROM master_password LIMIT 1");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String encrypted = rs.getString("password");
                String decrypted = EncryptionUtil.decrypt(encrypted);
                return decrypted.equals(pwd);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }

    public ArrayList<VaultItem> getAllItems() {
        ArrayList<VaultItem> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM vault_items");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new VaultItem(
                        rs.getInt("id"),
                        rs.getString("service"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertItem(VaultItem item) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO vault_items(service, username, password) VALUES(?,?,?)"
            );
            ps.setString(1, item.getService());
            ps.setString(2, item.getUsername());
            ps.setString(3, EncryptionUtil.encrypt(item.getPassword()));
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateItem(VaultItem item) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE vault_items SET service=?, username=?, password=? WHERE id=?"
            );
            ps.setString(1, item.getService());
            ps.setString(2, item.getUsername());
            ps.setString(3, EncryptionUtil.encrypt(item.getPassword()));
            ps.setInt(4, item.getId());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteItem(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM vault_items WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
