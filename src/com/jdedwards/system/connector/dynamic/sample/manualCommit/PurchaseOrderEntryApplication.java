package com.jdedwards.system.connector.dynamic.sample.manualCommit;

import com.jdedwards.system.connector.dynamic.*;
import com.jdedwards.system.connector.dynamic.sample.DynConApplication;
import com.jdedwards.system.connector.dynamic.callmethod.ExecutableMethod;
import com.jdedwards.system.connector.dynamic.callmethod.BSFNExecutionWarning;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;

import java.util.Map;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.manualCommit.TestPurchaseOrderEntryApplication 
 */
public class PurchaseOrderEntryApplication extends DynConApplication {

    private final String WRITE_FLAG = "2";

    private ExecutableMethod poeBeginDoc;
    private ExecutableMethod poeEndDoc;
    private ExecutableMethod poeEditLine;
    private ExecutableMethod poeClearWF;
    private boolean isBegindocCalled = false;
    private boolean isEditLineCalled = false;
    private OneworldTransaction transaction = null;


    public PurchaseOrderEntryApplication(int sessionID, BSFNSpecSource specSource, boolean isManualCommit) {
        super(sessionID, specSource);
        UserSession userSession = Connector.getInstance().getUserSession(sessionID);
        transaction=userSession.createOneworldTransaction(isManualCommit);
    }


    public BSFNExecutionWarning executeBeginDoc(Map inputParams, Map outputParams) throws SystemException {

        poeBeginDoc = getBSFNMethod("F4311FSBeginDoc");

        poeBeginDoc.setValues(inputParams);

        poeBeginDoc.setValue("cHeaderActionCode", "A");
        poeBeginDoc.setValue("cProcessEdits", "1");
        poeBeginDoc.setValue("cUpdateOrWriteToWorkFile", WRITE_FLAG);
        poeBeginDoc.setValue("szProgramID", "CORBA");


        poeBeginDoc.setValue("szPurchaseOrderPrOptVersion", "ZJDE0001");
        poeBeginDoc.setValue("szComputerID", getComputerName());
        if (isEmpty(inputParams.get("jdOrderDate"))) {
            poeBeginDoc.setValue("jdOrderDate", getCurrentDate());
        }
        if (isEmpty(inputParams.get("szOrderType"))) {
            poeBeginDoc.setValue("szOrderType", "OP");
        }

        //transaction.begin();
        BSFNExecutionWarning warning = poeBeginDoc.execute(transaction);
        isBegindocCalled = true;
        setOutput(outputParams, poeBeginDoc.getValueStrings());
        return warning;
    }


    public BSFNExecutionWarning executeEditLine(Map inputParams, Map outputParams) throws SystemException {
        if (!isBegindocCalled) {
            throw new ApplicationException("BeginDoc must be called before editline");
        }
        poeEditLine = getBSFNMethod("F4311EditLine");
        poeEditLine.setValues(inputParams);
        poeEditLine.setValue("mnJobNumber", poeBeginDoc.getValue("mnJobNumber"));
        poeEditLine.setValue("szBranchPlant", poeBeginDoc.getValue("szBranchPlant"));
        poeEditLine.setValue("szComputerID", poeBeginDoc.getValue("szComputerID"));
        poeEditLine.setValue("cUpdateOrWriteWorkFile", WRITE_FLAG);
        poeEditLine.setValue("szOrderType", poeBeginDoc.getValue("szOrderType"));
        poeEditLine.setValue("cDetailActionCode", "1");

        /** SAR 5702263 */
        poeEditLine.setValue("mnProcessID", poeBeginDoc.getValue("mnProcessID"));
        poeEditLine.setValue("mnTransactionID", poeBeginDoc.getValue("mnTransactionID"));
        poeEditLine.setValue("mnSupplierNumber", poeBeginDoc.getValue("mnSupplierNumber"));
        /*End SAR 5702263 */
        BSFNExecutionWarning warning = poeEditLine.execute(transaction);
        setOutput(outputParams, poeEditLine.getValueStrings());
        isEditLineCalled = true;
        return warning;
    }



    public BSFNExecutionWarning executeEndDoc(Map inputParams, Map outputParams) throws SystemException{
        if (!isBegindocCalled){
            throw new ApplicationException("BeginDoc must be called before EndDoc");
        }
        poeEndDoc = getBSFNMethod("F4311EndDoc");
        poeEndDoc.setValues(inputParams);
        poeEndDoc.setValue("mnJobNumber",poeBeginDoc.getValue("mnJobNumber"));
        poeEndDoc.setValue("mnOrderNumberAssigned",poeBeginDoc.getValue("mnOrderNumber"));
        poeEndDoc.setValue("szRelatedOrderType",poeBeginDoc.getValue("szOrderType"));
        poeEndDoc.setValue("szComputerID",poeBeginDoc.getValue("szComputerID"));
        poeEndDoc.setValue("cUseWorkFiles",WRITE_FLAG);
        /** SAR 5702263 */
        poeEndDoc.setValue("mnProcessID",poeBeginDoc.getValue("mnProcessID"));
        poeEndDoc.setValue("mnTransactionID",poeBeginDoc.getValue("mnTransactionID"));
        BSFNExecutionWarning warning=poeEndDoc.execute(transaction);
        //transaction.commit();
        //transaction.rollback();
        setOutput(outputParams,poeEndDoc.getValueStrings());
        return warning;
/*End SAR 5702263*/
    }

    public void beginTransaction() throws ServerFailureException{
        this.transaction.begin();
    }
    public void commitTransaction() throws ServerFailureException{
        this.transaction.commit();
    }

    public void rollbackTransaction() throws ServerFailureException{
        this.transaction.rollback();
    }

    public BSFNExecutionWarning executeClearWF(Map inputParams, Map outputParams) throws SystemException {
        if (isBegindocCalled){
            poeClearWF = getBSFNMethod("F4311ClearWorkFiles");
            poeClearWF.setValue("cClearDetailFile", WRITE_FLAG);
            if (isEditLineCalled) poeClearWF.setValue("cClearHeaderFile", WRITE_FLAG);
            poeClearWF.setValue("mnJobNumber", poeBeginDoc.getValue("mnJobNumber"));
            poeClearWF.setValue("szComputerID", poeBeginDoc.getValue("szComputerID"));
            poeClearWF.execute(sessionID);
        }
        return null;
    }
}