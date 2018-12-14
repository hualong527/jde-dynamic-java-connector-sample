package com.jdedwards.system.connector.dynamic.sample.manualCommit;

import com.jdedwards.system.connector.dynamic.ApplicationException;
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.callmethod.BSFNExecutionWarning;
import com.jdedwards.system.connector.dynamic.callmethod.RequiredParameterNotFoundException;
import com.jdedwards.system.connector.dynamic.sample.util.OrderHeaderPane;
import com.jdedwards.system.connector.dynamic.sample.util.SignonDialog;
import com.jdedwards.system.connector.dynamic.sample.util.OrderDetailTableModel;
import com.jdedwards.system.connector.dynamic.sample.util.MessagePane;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.OneworldBSFNSpecSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.manualCommit.TestPurchaseOrderEntryClient 
 */
public class PurchaseOrderEntryClient {
    Connector connectorProxy = Connector.getInstance();

    JFrame orderFrame;
    final OrderDetailTableModel orderDetailModel = new OrderDetailTableModel();
    final JTable orderDetail = new JTable(orderDetailModel);
    MessagePane messageArea;
    OrderHeaderPane orderHeader;
    PurchaseOrderEntryApplication orderApp = null;
    int sessionID = 0;
    boolean hasBeginDocCalled=false;

    public void PurchaseOrderEntryApplication() {
    }

    public void init(String[] args) throws Exception {
        orderFrame = createOrderEntryFrame();
        orderFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                doExit();
            }
        });
        SignonDialog signOn = new SignonDialog(orderFrame);
        signOn.setLocationRelativeTo(orderFrame);
        signOn.setVisible(true);
        sessionID = signOn.getSessionID();
        /*User existing image file*/
        //String imageFileName = (args.length == 0)?"PurchaseOrderEntry.xml":args[0];
        //BSFNSpecSource specSource = new ImageBSFNSpecSource(imageFileName);
        /*User OneworldBSFN Spec fetching from Enterprise Server dynamically*/
        BSFNSpecSource specSource = new OneworldBSFNSpecSource(sessionID);
        orderFrame.setVisible(true);
        orderApp = new PurchaseOrderEntryApplication(sessionID, specSource,true);
    }



    private JFrame createOrderEntryFrame() {
        JFrame orderFrame = new JFrame();
        orderFrame.getContentPane().setLayout(null);
        orderFrame.setSize(900, 550);

        //add the Purchase Order Entry form
        orderHeader = new OrderHeaderPane("OP");
        orderHeader.setBounds(10, 80, 800, 200);
        orderFrame.getContentPane().add(orderHeader);

        orderDetailModel.addTableModelListener(orderDetail);
        orderDetail.setPreferredScrollableViewportSize(new Dimension(800, 100));

        orderDetail.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if(!hasBeginDocCalled){
                    if(executeBeginDoc()){
                        hasBeginDocCalled=true;
                    }else {
                        hasBeginDocCalled=false;
                        orderHeader.addressNo.requestFocus();
                    }
                }
            }
        }
        );

        orderDetail.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_DOWN) ||
                    ((e.getKeyCode() == KeyEvent.VK_TAB) &&
                     (orderDetail.getSelectedColumn() + 1 == 8))) {
                    checkForExecuteEditLine();
                }
            }
        });

//Create the scroll pane and add the orderDetail to it.
        JScrollPane scrollPane = new JScrollPane(orderDetail);
        scrollPane.setBounds(10, 300, 800, 100);

//Add the scroll pane to this window.
//POEFrame.add(scrollPane, BorderLayout.SOUTH);
        orderFrame.getContentPane().add(scrollPane);
        JToolBar toolBar = createToolBar();
        toolBar.setBounds(10, 10, 80, 40);
        orderFrame.getContentPane().add(toolBar);

        messageArea = new MessagePane();
        messageArea.setBounds(10, 400, 800, 100);
        orderFrame.getContentPane().add(messageArea);
        return orderFrame;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (checkForExecuteEditLine())
                {
                    executeEndDoc();
                }
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });

        toolBar.setMargin(new Insets(5, 5, 5, 5));
        toolBar.addSeparator();
        toolBar.add(saveButton);
        toolBar.add(exitButton);
        toolBar.setBorder(BorderFactory.createLineBorder(Color.gray));
        return toolBar;

    }

    private boolean executeBeginDoc() {
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();
        messageArea.setText("");

        String szOrderType = orderHeader.type.getText();
        if (szOrderType.length() == 0) {
            szOrderType = "OP";
            orderHeader.type.setText("OP");
        }
        inputParams.put("szOrderType", szOrderType);

        String szBusinessUnit = "            ";
        szBusinessUnit = szBusinessUnit.substring(0, 12 - orderHeader.branchPlant.getText().length()) + orderHeader.branchPlant.getText();
        inputParams.put("szBranchPlant", szBusinessUnit);

        String jdOrderDate = orderHeader.orderDate.getText();
        if (jdOrderDate.length() == 0) {
            SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
            jdOrderDate = sf.format(new Date());
            orderHeader.orderDate.setText(jdOrderDate);
        }
        inputParams.put("jdOrderDate", jdOrderDate);
        inputParams.put("mnSupplierNumber", orderHeader.addressNo.getText());
        inputParams.put("szUserID", Connector.getInstance().getUserSession(sessionID).getUserName().toUpperCase());
        try {
            orderApp.beginTransaction();
            BSFNExecutionWarning warning = orderApp.executeBeginDoc(inputParams, outputParams);
            if (warning!=null) {
                messageArea.setText(warning.toString());
            } else {
                messageArea.setText("BeginDoc execution Finished");
            }
        }
        catch (ApplicationException e) {
            messageArea.setText(e.toString());
            e.printStackTrace();
            if (e instanceof RequiredParameterNotFoundException){
                String paramName = ((RequiredParameterNotFoundException)e).getParamName();
                showError(paramName);
            }
            return false;
        } catch (SystemException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }

    private void executeEditLine() {
        messageArea.setText("");
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();

        inputParams.put("szUnformattedItemNumber", ((orderDetailModel.getValueAt(orderDetail.getSelectedRow(), 0))));
        inputParams.put("mnQuantityOrdered", ((orderDetailModel.getValueAt(orderDetail.getSelectedRow(), 2))));
        inputParams.put("mnUnitPrice", ((orderDetailModel.getValueAt(orderDetail.getSelectedRow(), 3))));
        inputParams.put("szLineType",((orderDetailModel.getValueAt(orderDetail.getSelectedRow(), 5))));
        inputParams.put("szTransactionUoM",((orderDetailModel.getValueAt(orderDetail.getSelectedRow(), 6))));

        try {
            BSFNExecutionWarning warning = orderApp.executeEditLine(inputParams, outputParams);
            if (warning!=null) {
                messageArea.setText(warning.toString());
            }
            orderDetailModel.setValueAt(outputParams.get("szDescription1"), orderDetail.getSelectedRow(), 1);
            orderDetailModel.setValueAt(outputParams.get("mnQuantityOrdered"), orderDetail.getSelectedRow(), 2);
            orderDetailModel.setValueAt(outputParams.get("mnUnitPrice"), orderDetail.getSelectedRow(), 3);
            orderDetailModel.setValueAt(outputParams.get("mnExtendedPrice"), orderDetail.getSelectedRow(), 4);
            orderDetailModel.setValueAt(outputParams.get("szLineType"), orderDetail.getSelectedRow(), 5);
            orderDetailModel.setValueAt(outputParams.get("szTransactionUoM"), orderDetail.getSelectedRow(), 6);

            orderDetailModel.update(orderDetail.getSelectedRow());
//            orderHeader.orderTotal.setText(outputParams.get("mnWKOrderTotal").toString());

        } catch (ApplicationException e) {
            messageArea.setText(e.toString());
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void executeEndDoc() {
        messageArea.setText("");
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();
        try {
            BSFNExecutionWarning warning = orderApp.executeEndDoc(inputParams, outputParams);

            int choice = JOptionPane.showConfirmDialog(orderFrame, "Want rollback?", "Please choose rollback or not", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(choice == JOptionPane.YES_OPTION){
                orderApp.rollbackTransaction();
            }else{
                orderApp.commitTransaction();
            }

            if (warning!=null) {
                messageArea.setText(warning.toString());
            }
            orderHeader.orderNo.setText(outputParams.get("mnOrderNumberAssigned").toString());
            orderHeader.previousOrderNo.setText(orderHeader.orderNo.getText());
            orderHeader.previousCo.setText(orderHeader.co.getText());
            orderHeader.previousType.setText(orderHeader.type.getText());
            reset();
        } catch (ApplicationException e) {
            messageArea.setText(e.toString());
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void executeClearWF() {
        messageArea.setText("");
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();
        try {
            BSFNExecutionWarning warning = orderApp.executeClearWF(inputParams, outputParams);
        } catch (ApplicationException e) {
            messageArea.setText(e.getMessage());
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void reset() {
        messageArea.setText("");
        orderDetailModel.reset();
//clean up panel
        orderHeader.reset();
        orderDetail.clearSelection();
        hasBeginDocCalled=false;
    }

    private void doExit() {
        String msg1 = "Exit the Purchase Order Entry?";
        String msg2 = "Exit confirmation";
        int c = JOptionPane.showConfirmDialog(orderFrame, msg1, msg2,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (c == JOptionPane.YES_OPTION) {
            executeClearWF();
            Connector.getInstance().shutDown();
            System.exit(0);
        }
    }


    private void showError(String paramName){
        String msg="";
        Component focusComp = null;
        if (paramName.equalsIgnoreCase("mnAddressNumber")){
            msg="Sold to is required";
            focusComp = orderHeader.addressNo;
        }else if (paramName.equals("szBusinessUnit")){
            msg="Branch/Plant is required";
            focusComp = orderHeader.branchPlant;
        }
        if (focusComp!=null){
            JOptionPane.showMessageDialog(orderFrame,msg);
            focusComp.requestFocus();
        }
    }

    public static void main(String[] args) throws Exception {
        PurchaseOrderEntryClient soe = new PurchaseOrderEntryClient();
        soe.init(args);
    }

    private boolean checkForExecuteEditLine()
    {
        boolean success = false;
        int i = 0;
        
        i = orderDetailModel.update(orderDetail.getSelectedRow());
        if (i == 1)
        {
            JOptionPane.showMessageDialog(orderDetail, "Item number field is required");
        }
        else if (i == 2)
        {
            JOptionPane.showMessageDialog(orderDetail, "Quantity ordered field is required");
        }
        else if (i == 3)
        {
            success = true;
        }
        else if (i == 0)
        {
            executeEditLine();
            success = true;
        }
        
        return success;
    }
}
