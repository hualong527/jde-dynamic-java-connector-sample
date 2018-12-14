package com.jdedwards.system.connector.dynamic.test;

import com.jdedwards.system.connector.dynamic.ApplicationException;
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.OneworldTransaction;
import com.jdedwards.system.connector.dynamic.callmethod.BSFNExecutionWarning;
import com.jdedwards.system.connector.dynamic.sample.purchaseorder.PurchaseOrderEntryApplication;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.OneworldBSFNSpecSource;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import junit.framework.TestCase;
import com.jdedwards.system.lib.JdeProperty;

public class TestPurchaseOrderEntry extends TestCase {
    Connector connectorProxy = Connector.getInstance();
    PurchaseOrderEntryApplication orderApp;
    OneworldTransaction transaction;
    boolean printResult = true;
    int sessionID = 0;
    private String username = "";
    private String password = "";
    private String env = "";
    private String role = "*ALL";
    private int multipleRun = 1;
    private long totalTime;
    private boolean manual = false;
    private boolean rollback = false;

    public TestPurchaseOrderEntry(String s) {
        super(s);
    }

    public static void main(String[] args) throws Exception {
        long beginTime = System.currentTimeMillis();
        TestPurchaseOrderEntry soe = new TestPurchaseOrderEntry("Purchase Order");
        soe.setUp();
        soe.testPurchaseOrderEntry();
        long endTime = System.currentTimeMillis();
        soe.totalTime = endTime - beginTime;
        System.out.println("Run " + soe.multipleRun + " times of DC PurchaseOrderEntry, total time is " + soe.totalTime);
        System.exit(1);
    }

    protected void setUp() throws Exception {
        username =  JdeProperty.getProperty("TEST", "username", "KL5449350");
        password =  JdeProperty.getProperty("TEST", "password", "KL5449350");
        env =  JdeProperty.getProperty("TEST", "env", "ADEVNIS2");
        role =  JdeProperty.getProperty("TEST", "role", "*ALL");
        multipleRun =  JdeProperty.getProperty("TEST", "multipleRun", 1);
        manual =  JdeProperty.getProperty("TEST", "manualTrans", false);
        rollback =  JdeProperty.getProperty("TEST", "rollback", false);
        sessionID = connectorProxy.login(username, password, env, role);
        System.out.println("login success");
        transaction = connectorProxy.getUserSession(sessionID).createOneworldTransaction(manual);
        //        BSFNSpecSource specSource = new ImageBSFNSpecSource("SalesOrderEntry.xml");
        BSFNSpecSource specSource = new OneworldBSFNSpecSource(sessionID);
        orderApp = new PurchaseOrderEntryApplication(sessionID, specSource);
        System.out.println("Setup PurchaseOrderEntry successfully");
    }

    public void testPurchaseOrderEntry() {
        for (int i = 0; i < multipleRun; i++) {
            try {
                transaction.begin();
                executeBeginDoc(transaction);
                executeEditLine(transaction);
                executeEndDoc(transaction);
                if (rollback) {
                    System.out.println("Begin Rollback");
                    transaction.rollback();
                    System.out.println("Rollback succeeded");
                } else {
                    System.out.println("Begin manual commit");
                    transaction.commit();
                    System.out.println("Commit succeeded");
                }
                executeClearWF();
            } catch (SystemException e) {
                e.printStackTrace();
                fail(e.getMessage());
            } catch (ApplicationException e) {
                System.err.println("Application exception:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void executeBeginDoc(OneworldTransaction transaction) throws SystemException {
        System.out.println("running executeBeginDoc");
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();
        inputParams.put("szOrderType", "OP");
        inputParams.put("szBranchPlant", "         M30");
        SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
        String jdOrderDate = sf.format(new Date());
        inputParams.put("jdOrderDate", jdOrderDate);
        inputParams.put("mnSupplierNumber", "4242");
        inputParams.put("szUserID", Connector.getInstance().getUserSession(sessionID).getUserName().toUpperCase());
        BSFNExecutionWarning warning = orderApp.executeBeginDoc(inputParams, outputParams);
        if (printResult) {
            System.out.println("beginDoc finished");
        }
    }

    private void executeEditLine(OneworldTransaction transaction) throws SystemException {
        System.out.println("running executeEditLine");
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();
        inputParams.put("szUnformattedItemNumber", "1001");
        inputParams.put("mnQuantityOrdered", "1");
        inputParams.put("mnUnitPrice", "");
        inputParams.put("szLineType", "");
        inputParams.put("szTransactionUoM", "");
        BSFNExecutionWarning warning = orderApp.executeEditLine(inputParams, outputParams);
        if (printResult) {
            System.out.println("end line finished");
        }
    }

    private void executeEndDoc(OneworldTransaction transaction) throws SystemException {
        System.out.println("running executeEndDoc");
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();
        BSFNExecutionWarning warning = orderApp.executeEndDoc(inputParams, outputParams);
        if (printResult) {
            System.out.println("order number=" + outputParams.get("mnOrderNumberAssigned").toString());
        }
    }

    private void executeClearWF() throws SystemException {
        System.out.println("running executeClearWF");
        Map inputParams = orderApp.createMappedRecord();
        Map outputParams = orderApp.createMappedRecord();
        BSFNExecutionWarning warning = orderApp.executeClearWF(inputParams, outputParams);
    }
}
