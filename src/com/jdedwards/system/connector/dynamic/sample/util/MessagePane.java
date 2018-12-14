/******************************************************************************
 * class MessagePane.java
 *
 * Modification Log
 *   Date             Name               Description
 *   Jan 15, 2002       William Lin        Created.
 *******************************************************************************
 *
 * Copyright (c) 2001
 * J.D. Edwards & Company
 *
 * This unpublished material is proprietary to J.D. Edwards & Company.
 * All rights reserved.  The methods and techniques described herein are
 * considered trade secrets and/or confidential.  Reproduction or
 * distribution, in whole or in part, is forbidden except by express
 * written permission of J.D. Edwards & Company.
 *
 ******************************************************************************/
package com.jdedwards.system.connector.dynamic.sample.util;

import javax.swing.*;
import java.awt.*;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.util.TestMessagePane 
 */
public class MessagePane extends JScrollPane{
    private JTextArea msgArea;
    public MessagePane(){
        super();
        msgArea = new JTextArea();
        msgArea.setLineWrap(true);
        msgArea.setEditable(false);
        msgArea.setForeground(Color.black);
        msgArea.setBackground(Color.lightGray);
        setViewportView(msgArea);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    public void setText(String text){
        msgArea.setText(text);
    }

    public String getText() {
        return msgArea.getText();
    }
}
