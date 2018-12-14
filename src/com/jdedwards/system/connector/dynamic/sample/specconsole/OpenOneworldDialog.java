package com.jdedwards.system.connector.dynamic.sample.specconsole;


import com.jdedwards.system.connector.dynamic.spec.dictionary.SpecDictionary;
import com.jdedwards.system.connector.dynamic.spec.dictionary.OneworldSpecDictionary;
import com.jdedwards.system.connector.dynamic.spec.source.OneworldBSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.InvalidLoginException;
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.SystemException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.specconsole.TestOpenOneworldDialog 
 */
public class OpenOneworldDialog extends JDialog {
    private JOptionPane optionPane;
    private SpecDictionary owSpecDictionary=null;
    private BSFNSpecSource owSpecSource=null;


    public BSFNSpecSource getSpecSource(){
        return owSpecSource;
    }
    public SpecDictionary getSpecDictionary() {
        if (owSpecDictionary!=null){
            owSpecDictionary.bindSpecSource(owSpecSource);
        }
        return owSpecDictionary;
    }

    public OpenOneworldDialog(final JFrame aFrame) {

        super(aFrame, true);

        setTitle("Open Oneworld Spec Source");
        final int fieldSize = 30;
        final String userName = "User Name";
        final String password = "Password";
        final String env = "Environment";
        final String role = "Role";
        final JTextField userNameField = new JTextField("QL6804780",fieldSize);
        final JPasswordField passwordField = new JPasswordField("QL6804780",fieldSize);
        final JTextField envField = new JTextField("ADEVNIS2",fieldSize);
        final JTextField roleField = new JTextField("*ALL",fieldSize);
        Object[] array = {userName, userNameField,password,passwordField,env,envField,role,roleField};

        final String loginBtnStr = "Login";
        final String cancelBtnStr = "Cancel";
        Object[] options = {loginBtnStr, cancelBtnStr};

        optionPane = new JOptionPane(array,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[0]);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                optionPane.setValue(new Integer(
                        JOptionPane.CLOSED_OPTION));
            }
        });

        userNameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionPane.setValue(loginBtnStr);
            }
        });

        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();

                if (isVisible()
                        && (e.getSource() == optionPane)
                        && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
                        prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                    Object value = optionPane.getValue();

                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        //ignore reset
                        return;
                    }

                    // Reset the JOptionPane's value.
                    // If you don't do this, then if the user
                    // presses the same button next time, no
                    // property change event will be fired.
                    optionPane.setValue(
                            JOptionPane.UNINITIALIZED_VALUE);

                    if (value.equals(loginBtnStr)) {
                        try {
                            String user = userNameField.getText();
                            String pwd = new String(passwordField.getPassword());
                            String env = envField.getText();
                            String role = roleField.getText();
                            int sessionID = Connector.getInstance().login(user,pwd,env,role);
                            owSpecDictionary= new OneworldSpecDictionary(sessionID);
                            owSpecSource= new OneworldBSFNSpecSource(sessionID);
                            setVisible(false);
                        } catch (SystemException e1) {
                            JOptionPane.showMessageDialog(aFrame,
                                    "Fail to connect to Oneworld:"+e1.getMessage(),
                                    "Connection Error",
                                    JOptionPane.ERROR_MESSAGE);
                            e1.printStackTrace();

                        } catch (InvalidLoginException e2){
                            JOptionPane.showMessageDialog(aFrame,
                                    "Invalid User,password,env or role.",
                                    "Invalid Login",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (value.equals(cancelBtnStr)){
                        setVisible(false);
                    }

                }
            }
        });
    }
}
