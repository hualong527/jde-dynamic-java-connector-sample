/******************************************************************************
 * class DynConApplication.java
 *
 * Modification Log
 *   Date             Name               Description
 *   Jan 15, 2002       William Lin        Created.
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
package com.jdedwards.system.connector.dynamic.sample;

import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.spec.SpecFailureException;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.callmethod.ExecutableMethod;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.TestDynConApplication 
 */
public class DynConApplication {
    // private helper methods
    protected BSFNSpecSource specSource = null;
    protected int sessionID = 0;
    protected static String computerName = null;

    public DynConApplication(int sessionID,BSFNSpecSource specSource){
        this.specSource = specSource;
        this.sessionID = sessionID;
    }

    public Map createMappedRecord(){
        return new HashMap();
    }

    protected boolean isEmpty(Object obj) {
        return (obj == null || obj.toString().length() == 0);
    }

    protected static String getComputerName() throws SystemException{
        if (computerName == null) {
            try {
                computerName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                throw new SystemException("Cannot get the local host name:" + e.getMessage(), e);
            }
        }
        return computerName;
    }


    protected ExecutableMethod getBSFNMethod(String methodName) throws SpecFailureException {
        return (ExecutableMethod)(specSource.getBSFNMethod(methodName)).createExecutable();
    }

    protected String getCurrentDate() {
        SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
        String jdOrderDate = sf.format(new Date());
        return jdOrderDate;
    }

    protected void setOutput(Map params, Map values){
        params.clear();
        params.putAll(values);
    }
}
