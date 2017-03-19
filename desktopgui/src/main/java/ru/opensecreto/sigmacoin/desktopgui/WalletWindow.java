package ru.opensecreto.sigmacoin.desktopgui;

import javax.swing.*;
import java.awt.*;

public class WalletWindow extends JFrame {

    public WalletWindow() throws HeadlessException {
        super(DesktopGuiConfig.TITLE);
        setBounds(10, 10, 800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
