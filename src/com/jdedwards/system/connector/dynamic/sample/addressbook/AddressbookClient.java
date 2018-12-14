package com.jdedwards.system.connector.dynamic.sample.addressbook;

import com.jdedwards.system.connector.dynamic.callmethod.*;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.OneworldBSFNSpecSource;
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.ApplicationException;
import com.jdedwards.system.connector.dynamic.sample.util.SignonDialog;
import com.jdedwards.system.connector.dynamic.sample.util.MessagePane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.addressbook.TestAddressbookClient 
 */
public class AddressbookClient {

    // GUI Components
    JFrame addressbookFrame = null;
    JTextField abnumberField;
    JTextField alphanameField;
    JTextField addressField;
    JTextField zipcodeField;
    JTextField cityField;
    JTextField countyField;
    JTextField stateField;
    JTextField countryField;
    MessagePane msgArea;

    // BSFN logic
    int sessionID = 0;
    AddressbookApplication addressBook = null;

    public static void main(String[] args) throws Exception {
        AddressbookClient abApplication = new AddressbookClient();
        abApplication.initGUI(args);
    }

    public void initGUI(String[] args) throws Exception {
        addressbookFrame = createAddressBookFrame();
        addressbookFrame.setVisible(false);
        SignonDialog dialog = new SignonDialog(addressbookFrame);
        dialog.setLocationRelativeTo(addressbookFrame);
        dialog.setVisible(true);
        sessionID = dialog.getSessionID();
        /*User existing image file*/
        //String imageFileName = (args.length == 0)?"AddressBook.xml":args[0];
        //BSFNSpecSource specSource = new ImageBSFNSpecSource(imageFileName);
        /*User OneworldBSFN Spec fetching from Enterprise Server dynamically*/
        BSFNSpecSource specSource = new OneworldBSFNSpecSource(sessionID);
        addressBook = new AddressbookApplication(sessionID, specSource);
        addressbookFrame.setVisible(true);
    }


    private void doRetrieveData() throws SystemException{
        clearAll();
        Map inputParams = addressBook.createMappedRecord();
        Map outputParams = addressBook.createMappedRecord();
        inputParams.put("mnAddressNumber", abnumberField.getText());
        BSFNExecutionWarning warnings = addressBook.getEffectiveAddress(inputParams,outputParams);
        alphanameField.setText(outputParams.get("szNamealpha").toString());
        addressField.setText(outputParams.get("szAddressLine1").toString());
        zipcodeField.setText(outputParams.get("szZipCodePostal").toString());
        cityField.setText(outputParams.get("szCity").toString());
        countyField.setText(outputParams.get("szCountyAddress").toString());
        stateField.setText(outputParams.get("szState").toString());
        countryField.setText(outputParams.get("szCountry").toString());
        if (warnings!=null) {
            msgArea.setText(" BSFN error: Invalid AddressBook Number\n" + warnings.toString());
        } else {
            msgArea.setText(" Addressbook execution finished");
        }
    }


// Create Address Book Panel
    private JFrame createAddressBookFrame() {
        final JFrame addressbookFrame = new JFrame() {
            public void processWindowEvent(WindowEvent e) {
                if (e.getID() == Event.WINDOW_DESTROY) {
                    Connector.getInstance().shutDown();
                    System.exit(0);
                }
            }
        };
        addressbookFrame.getContentPane().setLayout(null);

        Label label_abnumber = new Label("AddressBook #");
        label_abnumber.setBounds(20, 20, 100, 20);
        addressbookFrame.getContentPane().add(label_abnumber);

        abnumberField = new JTextField();
        abnumberField.setBounds(200, 20, 100, 20);
        addressbookFrame.getContentPane().add(abnumberField);

        Button button_retrieve = new Button("RETRIEVE");
        button_retrieve.setBounds(400, 20, 100, 20);
        addressbookFrame.getContentPane().add(button_retrieve);
        button_retrieve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    doRetrieveData();
                } catch (SystemException e1) {
                    System.out.println("Fatal error when execute the Addressbook application:"+e1.getMessage());
                    e1.printStackTrace();
                    System.out.println("root cause is:"+e1.getRootException());
                    e1.getRootException().printStackTrace();
                    System.exit(1);
                }
                catch (ApplicationException ae){
                    msgArea.setText(ae.getMessage());
                    if (ae instanceof RequiredParameterNotFoundException){
                        String errorParam= ((RequiredParameterNotFoundException)ae).getParamName();
                        if (errorParam.equalsIgnoreCase("mnAddressNumber")){
                                JOptionPane.showMessageDialog(addressbookFrame,"Addressbook# is required");
                                abnumberField.requestFocus();
                            }
                        }
                    }
                }
        });


        Label label_name = new Label("Alpha Name");
        label_name.setBounds(20, 60, 100, 20);
        addressbookFrame.getContentPane().add(label_name);

        alphanameField = new JTextField();
        alphanameField.setBounds(200, 60, 400, 20);
        addressbookFrame.getContentPane().add(alphanameField);

        Label label_address = new Label("Address");
        label_address.setBounds(20, 90, 100, 20);
        addressbookFrame.getContentPane().add(label_address);

        addressField = new JTextField();
        addressField.setBounds(200, 90, 400, 20);
        addressbookFrame.getContentPane().add(addressField);

        Label label_zipcode = new Label("Zip Code");
        label_zipcode.setBounds(20, 120, 100, 20);
        addressbookFrame.getContentPane().add(label_zipcode);

        zipcodeField = new JTextField();
        zipcodeField.setBounds(200, 120, 200, 20);
        addressbookFrame.getContentPane().add(zipcodeField);

        Label label_city = new Label("City");
        label_city.setBounds(20, 150, 100, 20);
        addressbookFrame.getContentPane().add(label_city);

        cityField = new JTextField();
        cityField.setBounds(200, 150, 200, 20);
        addressbookFrame.getContentPane().add(cityField);

        Label label_county = new Label("County");
        label_county.setBounds(20, 180, 100, 20);
        addressbookFrame.getContentPane().add(label_county);

        countyField = new JTextField();
        countyField.setBounds(200, 180, 200, 20);
        addressbookFrame.getContentPane().add(countyField);

        Label label_state = new Label("State");
        label_state.setBounds(20, 210, 100, 20);
        addressbookFrame.getContentPane().add(label_state);

        stateField = new JTextField();
        stateField.setBounds(200, 210, 100, 20);
        addressbookFrame.getContentPane().add(stateField);

        Label label_country = new Label("Country");
        label_country.setBounds(20, 240, 100, 20);
        addressbookFrame.getContentPane().add(label_country);

        countryField = new JTextField();
        countryField.setBounds(200, 240, 200, 20);
        addressbookFrame.getContentPane().add(countryField);

        msgArea = new MessagePane();
        msgArea.setBounds(20, 300, 600, 80);
        addressbookFrame.getContentPane().add(msgArea);
        addressbookFrame.setSize(800, 480);
        addressbookFrame.setResizable(true);
        return addressbookFrame;
    }

    void clearAll() {
        alphanameField.setText("");
        addressField.setText("");
        zipcodeField.setText("");
        cityField.setText("");
        countyField.setText("");
        stateField.setText("");
        countryField.setText("");
        msgArea.setText("");

    }

}
