package com.jdedwards.system.connector.dynamic.sample.specconsole;


import com.jdedwards.system.connector.dynamic.spec.dictionary.SpecDictionary;
import com.jdedwards.system.connector.dynamic.spec.dictionary.ImageSpecDictionary;
import com.jdedwards.system.connector.dynamic.spec.SpecFailureException;
import com.jdedwards.system.connector.dynamic.spec.source.ImageBSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.InvalidLoginException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.specconsole.TestOpenSpecImageDialog 
 */
public class OpenSpecImageDialog extends JDialog {
    private JOptionPane optionPane;
    private SpecDictionary specDictionary=null;
    private BSFNSpecSource specSource = null;
    private JFrame aFrame = null;

    public SpecDictionary getSpecDictionary() {
        return specDictionary;
    }

    public BSFNSpecSource getSpecSource() {
        return specSource;
    }

    private String findFile(String title) {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showDialog(this, title);
        if (returnVal == JFileChooser.APPROVE_OPTION ) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }else {
            return "";
        }
    }

    public OpenSpecImageDialog(final JFrame aFrame) {
        super(aFrame, true);
        this.aFrame = aFrame;
        setTitle("Open Spec Image Source");
        final int fieldSize = 30;
        final String dictionaryStr = "Spec Dictionary";
        final JTextField dictionaryField = new JTextField(fieldSize);
        final JButton openDictionaryBtn = new JButton("Open Spec Dictionary");
        openDictionaryBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                dictionaryField.setText(findFile("Open Spec Source"));
            }
        });
        final String contentStr = "Spec Content";
        final JTextField contentField = new JTextField(fieldSize);
        final JButton openContentBtn = new JButton("Open Spec Content Source");
        openContentBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                contentField.setText(findFile("Open Spec Content Source"));
            }
        });
        Object[] array = {dictionaryStr, dictionaryField,openDictionaryBtn,contentStr,contentField,openContentBtn};

        final String openBtnStr = "Open";
        final String cancelBtnStr = "Cancel";
        Object[] options = {openBtnStr, cancelBtnStr};

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

        dictionaryField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionPane.setValue(openBtnStr);
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

                    if (value.equals(openBtnStr)) {
                        try {
                            String dictName=dictionaryField.getText();
                            if (dictName!=null && dictName.length()!=0){
                                specDictionary= new ImageSpecDictionary(dictName);
                            }else {
                                specDictionary=null;
                            }
                            String sourceName = contentField.getText();
                            if (sourceName!=null && sourceName.length()!=0){
                                specSource = new ImageBSFNSpecSource(sourceName);
                            }else {
                                specSource=null;
                            }
                            setVisible(false);
                        } catch (SpecFailureException e1) {
                            JOptionPane.showMessageDialog(aFrame,
                                    "Fail to Open Spec.",
                                    "Connection Error",
                                    JOptionPane.ERROR_MESSAGE);

                        } catch (InvalidLoginException e2){
                            JOptionPane.showMessageDialog(aFrame,
                                    "Invalid User,contentStr,env or role.",
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
