/******************************************************************************
 * class SignonDialog.java
 *
 * Modification Log
 *   Date             Name               Description
 *   Jan 8, 2002       William Lin        Created.
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

import com.jdedwards.system.connector.dynamic.ServerFailureException;
import com.jdedwards.system.connector.dynamic.InvalidLoginException;
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.lib.JdeProperty;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.util.TestSignonDialog
 */
public class SignonDialog extends JDialog{
    // Create a sign-on panel
    int fieldSize=15;
    JTextField usernameField = new JTextField("",fieldSize);
    JPasswordField pwdField=new JPasswordField("",fieldSize);
    JTextField envField = new JTextField("",fieldSize);
    JTextField roleField = new JTextField("*ALL",fieldSize);
    JCheckBox saveLogInInfo = new JCheckBox("Remember Sign On Info");
    boolean writeXMLToDisk = false;
    int sessionID=0;
    JFrame parentFrame=null;

    public int getSessionID() {
        return sessionID;
    }

    public String getUser() {
        return usernameField.getText();
    }

    public String getPassword(){
        return pwdField.getText();
    }

    public String getEnv(){
        return envField.getText();
    }

    public String getRole(){
        return roleField.getText();
    }
    
    private void readInfoFromIni()
    {
		try
		{
			String eventStr;
			BufferedReader ini =
				new BufferedReader(new FileReader("jcevents.ini"));
			if (null != (eventStr = ini.readLine()))
			{
				usernameField.setText(eventStr);								
			}
			if (null != (eventStr = ini.readLine()))
			{
				pwdField.setText(eventStr);								
			}
			if (null != (eventStr = ini.readLine()))
			{
				envField.setText(eventStr);								
			}
			if (null != (eventStr = ini.readLine()))
			{
				roleField.setText(eventStr);								
			}
			if (null != (eventStr = ini.readLine()))
			{
				if(eventStr.equals("true"))
					saveLogInInfo.setSelected(true);
				else
					saveLogInInfo.setSelected(false);				
			}
			if (null != (eventStr = ini.readLine()))
			{
				if(eventStr.equals("true"))
					writeXMLToDisk = true;
				else
					writeXMLToDisk = false;				
			}			
		}
		catch (Exception e)
		{
		}
    }
    
	public void writeInfoToIni()
	{
		try
		{
			String eventStr;
			FileWriter fw = new FileWriter("jcevents.ini");
			//System.out.println("Writing INI info");
			fw.write(usernameField.getText());
			fw.write("\n");
			fw.write(pwdField.getText());
			fw.write("\n");
			fw.write(envField.getText());
			fw.write("\n");
			fw.write(roleField.getText());
			fw.write("\n");
			if(saveLogInInfo.isSelected() == true)
				fw.write("true\n");
			else
				fw.write("false\n");
			if(writeXMLToDisk == true)
				fw.write("true\n");
			else
				fw.write("false\n");
			fw.close();
		}
		catch (Exception e)
		{
			System.out.println("Unable to write info to file.");
			System.out.println(e);
		}
	}

    public SignonDialog (final JFrame parentFrame) {
		// Create a sign-on panel
		super(parentFrame,true);
		this.parentFrame = parentFrame;

    	// read INI
    	readInfoFromIni();
    	
        getContentPane().setSize(150,120);
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        addTextField("User Name",usernameField);
        addTextField("Password", pwdField);
        addTextField("Environment", envField);
        addTextField("Role",roleField);
        addCheckBox("",saveLogInInfo);
        addButtons();
        this.pack();
    }

    private void addButtons(){
        JPanel p = new JPanel();
        JButton OKBtn = new JButton("OK");
		OKBtn.setMnemonic('O');
        OKBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    sessionID = Connector.getInstance().login(
                            usernameField.getText(),
                            pwdField.getText(),
                            envField.getText().toUpperCase(),
                            roleField.getText().toUpperCase());
					if(saveLogInInfo.isSelected() == true && 0 != sessionID)
					{
						writeInfoToIni();
					}
                    setVisible(false);
                } catch (ServerFailureException r) {
                    System.out.println("Cannot connect to oneworld:"+r.getMessage());
                    r.printStackTrace();
                    System.exit(1);
                } catch (InvalidLoginException loginFail) {
                    JOptionPane.showMessageDialog(parentFrame,"Fail to login oneworld, please retry your name, password, env or role again");
                }
            }
        });
        getContentPane().add(OKBtn);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setMnemonic('C');
        getContentPane().add(cancelBtn);
        cancelBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                parentFrame.dispose();
                System.exit(0);
            }
        });
        p.add(OKBtn);
        p.add(cancelBtn);
        getContentPane().add(p);
    }

    private void addTextField(String labelStr, JTextField textField){
        JPanel p = new JPanel(new GridLayout(1,2));
        JLabel label = new JLabel(labelStr+":");
        p.add(label);
        p.add(textField);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setAlignmentY(Component.LEFT_ALIGNMENT);
        getContentPane().add(p);
    }
    
	private void addCheckBox(String labelStr, JCheckBox checkBox){
		JPanel p = new JPanel(new GridLayout(1,2));
		JLabel label = new JLabel(labelStr+":");
		p.add(label);
		p.add(checkBox);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		p.setAlignmentY(Component.LEFT_ALIGNMENT);
		getContentPane().add(p);
	}
    
    public static void main(String[] args) {
        JFrame aFrame= new JFrame("Dialog testing");
        JTextField text = new JTextField();
        aFrame.getContentPane().add(text);
        aFrame.pack();
        SignonDialog signOn = new SignonDialog(aFrame);
        signOn.setVisible(true);
        aFrame.setVisible(true);
        text.setText(String.valueOf(signOn.getSessionID()));
    }
	/**
	 * @return
	 */
	public boolean getSaveLogInInfo() {
		return saveLogInInfo.isSelected();
	}

	/**
	 * @param field
	 */
	public void setEnvField(String field) {
		envField.setText(field);
	}

	/**
	 * @param field
	 */
	public void setPwdField(String field) {
		pwdField.setText(field);
	}

	/**
	 * @param field
	 */
	public void setRoleField(String field) {
		roleField.setText(field);
	}

	/**
	 * @param box
	 */
	public void setSaveLogInInfo(boolean box) {
		saveLogInInfo.setSelected(box);
	}

	/**
	 * @param field
	 */
	public void setUsernameField(String field) {
		usernameField.setText(field);
	}

	/**
	 * @return
	 */
	public boolean isWriteXMLToDisk() {
		return writeXMLToDisk;
	}

	/**
	 * @param b
	 */
	public void setWriteXMLToDisk(boolean b) {
		writeXMLToDisk = b;
	}

}
