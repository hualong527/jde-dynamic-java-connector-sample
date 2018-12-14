package com.jdedwards.system.connector.dynamic.sample.specconsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.specconsole.TestAboutDialog 
 */
public class AboutDialog extends JDialog {
    static final String title = new String("Spec Image Console v1.0");
    static final String copyRight = new String("J.D.Edwards (c) 2002");
    static final String authorName = new String("William Lin");
    static final String OK_ACTION = new String("OK_ACTION");

    public AboutDialog() {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel copyRightLabel = new JLabel(copyRight);
        copyRightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel authorLabel = new JLabel(authorName);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton OKbutton = new JButton("OK");
        OKbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        OKbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                   dispose();
            }
        });
        this.setTitle(title);
        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.add(titleLabel);
        container.add(Box.createVerticalGlue());
        container.add(copyRightLabel);
        container.add(authorLabel);
        container.add(Box.createVerticalGlue());
        container.add(OKbutton);
        setSize(200, 120);
        setLocation(new Point(50, 200));
        setResizable(false);
        setVisible(true);
    }

}