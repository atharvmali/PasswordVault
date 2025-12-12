package utils;

import ui.LoginScreen;
import javax.swing.*;

public class AutoLockThread extends Thread {

    private long lastActiveTime = System.currentTimeMillis();
    private JFrame vaultFrame;

    public AutoLockThread(JFrame vaultFrame) {
        this.vaultFrame = vaultFrame;
    }

    public void resetTimer() {
        lastActiveTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (true) {
            long now = System.currentTimeMillis();

            if (now - lastActiveTime >= 60000) { // 60 seconds
                vaultFrame.dispose();
                new LoginScreen();
                break;
            }

            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }
}
