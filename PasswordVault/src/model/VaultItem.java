package model;

public class VaultItem {
    private int id;
    private String service;
    private String username;
    private String password;

    public VaultItem(int id, String service, String username, String password) {
        this.id = id;
        this.service = service;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getService() { return service; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setService(String s) { this.service = s; }
    public void setUsername(String u) { this.username = u; }
    public void setPassword(String p) { this.password = p; }
}
