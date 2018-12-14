///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2002 J.D. Edwards World Source Company
// This unpublished material is proprietary to J.D. Edwards World Source
// Company. All rights reserved.  The methods and techniques described
// herein are considered trade secrets and/or confidential.  Reproduction
// or distribution, in whole or in part, is forbidden except by express
// written permission of J.D. Edwards World Source Company.
///////////////////////////////////////////////////////////////////////////////

package com.jdedwards.system.connector.dynamic.test;

import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.callmethod.BSFNExecutionWarning;
import com.jdedwards.system.connector.dynamic.callmethod.ExecutableMethod;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import java.util.Map;
import java.util.Hashtable;
import com.jdedwards.system.connector.dynamic.spec.source.OneworldBSFNSpecSource;
import junit.framework.TestCase;
import com.jdedwards.system.lib.JdeProperty;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNCacheMapKey;
import junit.framework.TestSuite;

public class TestSpecCache extends TestCase {
    private String username = "";
    private String password = "";
    private String env = "";
    private String role = "*ALL";

    public Connector connector = Connector.getInstance();

    public int sessionID;
    private BSFNSpecSource specSource1 = null;
    private ExecutableMethod ab1 = null;

    //different specSource that share the same sessionID with specSource1
    private BSFNSpecSource specSource2 = null;
    private ExecutableMethod ab2 = null;

	 //different specSource that has different sessionID with specSource1
	 public int sessionID3;
	 private BSFNSpecSource specSource3 = null;
    private ExecutableMethod ab3 = null;

    //Cache Map key used for this test
    private BSFNCacheMapKey cacheMapKey;

   public static void main(String[] args){
   	try{
			junit.textui.TestRunner.run(new TestSuite(TestSpecCache.class));
  		}catch(Exception ex){
      	fail("test failed:"+ex);
   	}
  		System.exit(0);
    }

    protected void setUp() {
        try {
            username = JdeProperty.getProperty("TEST", "username", "KL5449350");
            password = JdeProperty.getProperty("TEST", "password", "KL5449350");
            env = JdeProperty.getProperty("TEST", "env", "ADEVNIS2");
            role = JdeProperty.getProperty("TEST", "role", "*ALL");
            System.setProperty("default_path", TestArguments.defaultPath);

            sessionID = connector.login(username, password, env, role);
            specSource1 = new OneworldBSFNSpecSource(sessionID);
				specSource2 = new OneworldBSFNSpecSource(sessionID);

				sessionID3 =  connector.login(username, password, env, role);
				specSource3 = new OneworldBSFNSpecSource(sessionID3);
            System.out.println("set up successfully");
        } catch (SystemException e) {
            e.printStackTrace();
            System.out.println("root cause is:" + e.getRootException().getMessage());
            e.getRootException().printStackTrace();
        }
    }

    public TestSpecCache(String s) {
        super(s);
    }

    /**
     * In jasdebug.log file, "Get template request document" should be called only three times.
     * The test failed if it is called more than three times or any BSFN execution failed.
     */
    public void testGetAddressMap() {
        try {
        		this.getAddressMap(1001);
				this.clearCache();
            System.out.println("-----After clear BSFN cache----");
            this.getAddressMap(1001);
          //  System.out.println("-----Call getAddress using different Env----");
          //  this.getAddressMapUsingDifferentEnv(1001);
        } catch (SystemException e) {
            System.out.println(e.toString());
            e.getRootException().printStackTrace();
            fail("testGetAddressMap failed");
        }
    }

    private void getAddressMap(int addressNo) throws SystemException {
		  //Test Step 1: a new OneWorldBSFNSpecSource. BSFN spec should be fetched and cached
        // clear parameter set
        ab1 = specSource1.getBSFNMethod("GetEffectiveAddress").createExecutable();
        ab1.resetValues();
        // input
        Map input = new Hashtable();
        input.put("mnAddressNumber", String.valueOf(addressNo));
        ab1.setValues(input);
        // execute
        BSFNExecutionWarning errors = ab1.execute(sessionID);
        //output
        Map output = ab1.getValues();
        System.out.println("szNamealpha=" + output.get("szNamealpha").toString());
        System.out.println("mnAddressNumber=" + output.get("mnAddressNumber").toString());
        System.out.println("szAddressLine1=" + output.get("szAddressLine1").toString());
        System.out.println("szZipCodePostal=" + output.get("szZipCodePostal").toString());

        //Test Step 2: Test creating a new OneWorldBSFNSpecSource with the same sessionID in test step 1
        //The BSFN spec should be feteched from cache.
        // clear parameter set
        ab2 = specSource2.getBSFNMethod("GetEffectiveAddress").createExecutable();
        ab2.resetValues();
        // input
        Map input2 = new Hashtable();
        input2.put("mnAddressNumber", String.valueOf(addressNo));
        ab2.setValues(input2);
        // execute
        BSFNExecutionWarning errors2 = ab2.execute(sessionID);
        //output
        Map output2 = ab2.getValues();
        System.out.println("szNamealpha=" + output2.get("szNamealpha").toString());
        System.out.println("mnAddressNumber=" + output2.get("mnAddressNumber").toString());
        System.out.println("szAddressLine1=" + output2.get("szAddressLine1").toString());
        System.out.println("szZipCodePostal=" + output2.get("szZipCodePostal").toString());

        //Test Step 3: Test create a new OneWorldBSFNSpecSource with different sessionID in step 1
        //tHE same BSFN spec should be fetched from cache.
        // clear parameter set
        ab3 = specSource3.getBSFNMethod("GetEffectiveAddress").createExecutable();
        ab3.resetValues();
        // input
        Map input3 = new Hashtable();
        input3.put("mnAddressNumber", String.valueOf(addressNo));
        ab3.setValues(input3);
        // execute
        BSFNExecutionWarning errors3 = ab3.execute(sessionID3);
        //output
        Map output3 = ab3.getValues();
        System.out.println("szNamealpha=" + output3.get("szNamealpha").toString());
        System.out.println("mnAddressNumber=" + output3.get("mnAddressNumber").toString());
        System.out.println("szAddressLine1=" + output3.get("szAddressLine1").toString());
        System.out.println("szZipCodePostal=" + output3.get("szZipCodePostal").toString());
   }

    private void clearCache(){
      try{
			specSource1.refresh();
      }catch(Exception ex){
			fail("Failed to refresh OneWorldBSFNSpecSource BSFN Cache");
      }
    }

    /**
     * 
     */
    private void getAddressMapUsingDifferentEnv(int addressNo) throws SystemException {
		  int sessionID4 = connector.login(username, password, env, role);
        OneworldBSFNSpecSource specSource4 = new OneworldBSFNSpecSource(sessionID4);
		  // clear parameter set
        ExecutableMethod ab4 = specSource4.getBSFNMethod("GetEffectiveAddress").createExecutable();
        ab4.resetValues();
        // input
        Map input = new Hashtable();
        input.put("mnAddressNumber", String.valueOf(addressNo));
        ab4.setValues(input);
        // execute
        BSFNExecutionWarning errors = ab4.execute(sessionID4);
        //output
        Map output = ab4.getValues();
        System.out.println("szNamealpha=" + output.get("szNamealpha").toString());
        System.out.println("mnAddressNumber=" + output.get("mnAddressNumber").toString());
        System.out.println("szAddressLine1=" + output.get("szAddressLine1").toString());
        System.out.println("szZipCodePostal=" + output.get("szZipCodePostal").toString());
    }

    protected void tearDown() {
        connector.logoff(this.sessionID);
        connector.logoff(this.sessionID3);
    }
}

