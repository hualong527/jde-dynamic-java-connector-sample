package com.jdedwards.system.connector.dynamic.sample.specconsole;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.specconsole.TestSearchDialog 
 */
public class SearchDialog extends JDialog {
    private JOptionPane optionPane;
    private String contextToFound;

    public String getContextToFound() {
        return contextToFound;
    }

    public SearchDialog(final JFrame aFrame) {
        super(aFrame, true);
        setTitle("Search Context");
        final int fieldSize = 30;
        final String contextName = "Context Name";
        final JTextField contextNameField = new JTextField(fieldSize);
        Object[] array = {contextName, contextNameField};

        final String searchBtnStr = "Search";
        final String cancelBtnStr = "Cancel";
        Object[] options = {searchBtnStr, cancelBtnStr};

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

        contextNameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionPane.setValue(searchBtnStr);
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

                    if (value.equals(searchBtnStr)) {
                        contextToFound = contextNameField.getText();
                        setVisible(false);
                    }
                    if (value.equals(cancelBtnStr)) {
                        setVisible(false);
                    }

                }
            }
        });
    }
}
