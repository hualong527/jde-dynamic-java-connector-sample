package com.jdedwards.system.connector.dynamic.sample.salesorder;

import com.jdedwards.system.connector.dynamic.ApplicationException;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.sample.DynConApplication;
import com.jdedwards.system.connector.dynamic.callmethod.ExecutableMethod;
import com.jdedwards.system.connector.dynamic.callmethod.BSFNExecutionWarning;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;

import java.util.*;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.salesorder.TestSalesOrderEntryApplication 
 */
public class SalesOrderEntryApplication extends DynConApplication{
    private boolean isBegindocCalled = false;
    private boolean isEditlineCalled = false;
    private ExecutableMethod soeBeginDoc = null;
    private ExecutableMethod soeEditLine = null;
    private ExecutableMethod soeEndDoc = null;
    private ExecutableMethod soeClearWF = null;


    public SalesOrderEntryApplication(int sessionID, BSFNSpecSource specSource) {
        super(sessionID, specSource);
    }


    public BSFNExecutionWarning executeBeginDoc(Map inputParams, Map outputParams) throws SystemException {
        soeBeginDoc = getBSFNMethod("F4211FSBeginDoc");
        // check the necessary settings

        // set the user define values
        soeBeginDoc.setValues(inputParams);

        // set default value
        soeBeginDoc.setValue("cCMDocAction", "A");
        soeBeginDoc.setValue("cCMProcessEdits", "1");
        soeBeginDoc.setValue("cCMUpdateWriteToWF", "2");
        soeBeginDoc.setValue("szCMProgramID", "CORBA");
        soeBeginDoc.setValue("szCMVersion", "ZJDE0001");
        soeBeginDoc.setValue("cMode", "F");
        soeBeginDoc.setValue("cRetrieveOrderNo", "1");
        soeBeginDoc.setValue("szCMComputerID", getComputerName());
        if (isEmpty(inputParams.get("szOrderType"))) {
            soeBeginDoc.setValue("szOrderType","SO");
        }

        if (isEmpty(inputParams.get("jdOrderDate"))) {
            soeBeginDoc.setValue("jdOrderDate", getCurrentDate());
        }

        BSFNExecutionWarning warning = soeBeginDoc.execute(sessionID);
        setOutput(outputParams,soeBeginDoc.getValueStrings());
        isBegindocCalled= true;
        return warning;
    }



    public BSFNExecutionWarning executeEditLine(Map inputParams, Map outputParams) throws SystemException {
        // Edit Line
        if (!isBegindocCalled) {
            throw new ApplicationException("BeginDoc must be called before editline");
        }
        soeEditLine = getBSFNMethod("F4211FSEditLine");

        // set user input values
        soeEditLine.setValues(inputParams);

        // set default values
        soeEditLine.setValue("mnCMJobNo", soeBeginDoc.getValue("mnCMJobNumber"));
        soeEditLine.setValue("mnOrderNo", soeBeginDoc.getValue("mnOrderNo"));
        soeEditLine.setValue("szBusinessUnit", soeBeginDoc.getValue("szBusinessUnit"));
        soeEditLine.setValue("szCMComputerID", soeBeginDoc.getValue("szCMComputerID"));
        soeEditLine.setValue("cCMWriteToWFFlag", "2");
        soeEditLine.setValue("szOrderType", soeBeginDoc.getValue("szOrderType"));

        BSFNExecutionWarning warning = soeEditLine.execute(sessionID);
        setOutput(outputParams,soeEditLine.getValueStrings());
        isEditlineCalled = true;
        return warning;
    }


    public BSFNExecutionWarning executeEndDoc(Map inputParams, Map outputParams) throws SystemException {
        if (!isBegindocCalled) {
            throw new ApplicationException("BeginDoc must be called before EndDoc");
        }
        soeEndDoc = getBSFNMethod("F4211FSEndDoc");
        soeEndDoc.setValues(inputParams);
        soeEndDoc.setValue("mnCMJobNo", soeBeginDoc.getValue("mnCMJobNumber").toString());
        soeEndDoc.setValue("mnSalesOrderNo", soeBeginDoc.getValue("mnOrderNo").toString());
        soeEndDoc.setValue("szOrderType", soeBeginDoc.getValue("szOrderType"));
        soeEndDoc.setValue("szCMComputerID", getComputerName());
        soeEndDoc.setValue("cCMUseWorkFiles", "2");

        BSFNExecutionWarning warning = soeEndDoc.execute(sessionID);
        isBegindocCalled = false;
        isEditlineCalled = false;
        setOutput(outputParams,soeEndDoc.getValueStrings());
        return warning;
    }

    public BSFNExecutionWarning executeClearWF(Map inputParams, Map outputParams) throws SystemException {
        if (isBegindocCalled) {
            soeClearWF = getBSFNMethod("F4211ClearWorkFile");
            soeClearWF.setValues(inputParams);
            soeClearWF.setValue("cClearDetailWF", "2");
            if (isEditlineCalled) soeClearWF.setValue("cClearHeaderWF", "2");
            soeClearWF.setValue("mnJobNo", soeBeginDoc.getValue("mnCMJobNumber"));
            soeClearWF.setValue("szComputerID", getComputerName());
            soeClearWF.setValues(inputParams);
            BSFNExecutionWarning warning = soeClearWF.execute(sessionID);
            setOutput(outputParams,soeClearWF.getValueStrings());
            return warning;
        }
        return null;
    }



}
