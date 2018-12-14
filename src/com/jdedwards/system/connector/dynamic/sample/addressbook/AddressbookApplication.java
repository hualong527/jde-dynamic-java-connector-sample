/******************************************************************************
 * class AddressbookApplication.java
 *
 * Modification Log
 *   Date             Name               Description
 *   Jan 14, 2002       William Lin        Created.
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
package com.jdedwards.system.connector.dynamic.sample.addressbook;

import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.callmethod.*;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.sample.DynConApplication;

import java.util.Map;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.addressbook.TestAddressbookApplication 
 */
public class AddressbookApplication extends DynConApplication{
    private ExecutableMethod getEffectiveAddress;

    public AddressbookApplication(int sessionID, BSFNSpecSource specSource) {
        super(sessionID,specSource);

    }


    /**
     * @exception SpecFailureException
     * @exception InvalidBSFNMethodArgumentException
     * @exception ApplicationException
     */
    public BSFNExecutionWarning getEffectiveAddress(Map inputParams,Map outputParams) throws SystemException{
        getEffectiveAddress = getBSFNMethod("GetEffectiveAddress");
        getEffectiveAddress.setValues(inputParams);
        BSFNExecutionWarning warning = getEffectiveAddress.execute(sessionID);
        setOutput(outputParams,getEffectiveAddress.getValueStrings());
        return warning;
    }

    /**
     * @throw SystemException
     * @throw ApplicationException
     */
    public BSFNExecutionWarning getEffectiveAddress(Map inputParams) throws SystemException{
        getEffectiveAddress = getBSFNMethod("GetEffectiveAddress");
        getEffectiveAddress.setValues(inputParams);
        BSFNExecutionWarning warning = getEffectiveAddress.execute(sessionID);
        inputParams.clear();
        inputParams.putAll(getEffectiveAddress.getValueStrings());
        return warning;
    }


    public D0100033 createEffectiveAddressParameterSet(){
        return new D0100033();
    }

    public BSFNExecutionWarning getEffectiveAddress(D0100033 d0100033) throws SystemException{
        getEffectiveAddress = getBSFNMethod("GetEffectiveAddress");
        getEffectiveAddress.setValues(d0100033.getParameterValues());
        BSFNExecutionWarning warning = getEffectiveAddress.execute(sessionID);
        d0100033.getParameterValues().clear();
        d0100033.getParameterValues().putAll(getEffectiveAddress.getValues());
        return warning;
    }
}
