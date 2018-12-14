package com.jdedwards.system.connector.dynamic.test;

import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.UserSession;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.security.SecurityToken;
import com.jdedwards.system.security.SecurityServer;
import com.jdedwards.system.security.SecurityServerResponse;
import com.jdedwards.system.security.SecurityServerInstance;
import com.jdedwards.system.connector.dynamic.callmethod.BSFNExecutionWarning;
import com.jdedwards.system.connector.dynamic.callmethod.ExecutableMethod;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.ImageBSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.SpecFailureException;
import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;
import com.jdedwards.system.connector.dynamic.spec.source.OneworldBSFNSpecSource;
import junit.framework.TestCase;
import com.jdedwards.system.lib.JdeProperty;
import com.jdedwards.system.security.SecurityServerException;

public class TestAddressbook extends TestCase {
	//static TestAddressbook abTest = new TestAddressbook("Dynamic Connector address book");
	static TestAddressbook abTest = null;
    private String username = "JDE";
    private String password = "Abcd1234";
    private String env = "JVL910";
    private String role = "*ALL";
    private BSFNSpecSource specSource = null;
    private Connector connector = Connector.getInstance();
    private Connector connectorToken = null;
    private ExecutableMethod ab = null;
    private int sessionID;
    private int connectorSessionID;
    private int multipleRun = 1;
    private long totalTime;
    private UserSession connectorSession = null;
    private SecurityToken securityToken = null;
    private BSFNSpecSource specSource2 = null;
    private ExecutableMethod ab2 = null;
	private SecurityServerResponse ssResponse = null;
	private SecurityServer securityServer = null;
	private boolean testToken = false;
	private boolean testNormalToken = false;
    private String invalidToken = "";

    public static void main(String[] args) throws Exception {
       long beginTime = System.currentTimeMillis();
       abTest = new TestAddressbook("Dynamic Connector address book");
        abTest.setUp();
        // abTest.testGetAddress();
        abTest.testGetAddressMap();
        long endTime = System.currentTimeMillis();
        abTest.totalTime = endTime - beginTime;
        System.out.println("Run " + abTest.multipleRun + " times of DC AddressBook, total time is " + abTest.totalTime);
		abTest.tearDown();
        /*if (!testToken)
            abTest.connector.logoff(abTest.sessionID);
        else {
            abTest.connector.logoff(abTest.sessionID);
            abTest.connectorToken.logoff(abTest.connectorSessionID);
        }*/
        System.exit(1);
    }

    protected void setUp() {
        try {
//            username = JdeProperty.getProperty("TEST", "username", "KL5449350");
//            password = JdeProperty.getProperty("TEST", "password", "KL5449350");
//            env = JdeProperty.getProperty("TEST", "env", "ADEVNIS2");
//            role = JdeProperty.getProperty("TEST", "role", "*ALL");
//            multipleRun = JdeProperty.getProperty("TEST", "multipleRun", 1);
//			testToken =  JdeProperty.getProperty("TEST", "testToken", false);
//    		testNormalToken = JdeProperty.getProperty("TEST", "testNormalToken", false);
//   			invalidToken = JdeProperty.getProperty("TEST", "invalidToken", "");
            System.setProperty("default_path", TestArguments.defaultPath);
            if (testToken) {
            	System.out.println("Testing login in with token");
            }
			if (invalidToken != "") {
            	System.out.println("Testing Invalid Token");
            }
           if (!testToken)
                sessionID = connector.login(username, password, env, role);
            else {
                connectorToken = Connector.getInstance();
				if (testNormalToken) {
					try {
						securityServer = SecurityServerInstance.getInstance();

                    } catch(SecurityServerException e) {
						e.printStackTrace();

                    }
					try {
					ssResponse = securityServer.login(SecurityServer.STDLOGON, username, password);
					securityToken = ssResponse.getToken();
					}	catch (SecurityServerException es) {
						es.printStackTrace();
				    }

                } else {
                	sessionID = connector.login(username, password, env, role);
                	connectorSession = connector.getUserSession(sessionID);
                	securityToken = connectorSession.getToken();
                }
                System.out.println("-->" + securityToken.toString() + "<--");
                if (invalidToken != "") {
                    //generate token
					if (invalidToken == "null")
						connectorSessionID = connectorToken.login("", env, role);
                    else {
                    SecurityToken invsecurityToken = new SecurityToken(invalidToken);
                    	// this should raise an exception
						connectorSessionID = connectorToken.login(invsecurityToken, env, role);
                		}
				}
                else
				 	connectorSessionID = connectorToken.login(securityToken, env, role);
                }

                System.out.println("login success");
                // specSource = new ImageBSFNSpecSource("JDEAddressBook.xml");
                if (testToken)
                    specSource = new OneworldBSFNSpecSource(connectorSessionID);
                else
                    specSource = new OneworldBSFNSpecSource(sessionID);
                ab = specSource.getBSFNMethod("GetEffectiveAddress").createExecutable();
                if (testToken)
                    specSource2 = new OneworldBSFNSpecSource(connectorSessionID);
                else
                    specSource2 = new OneworldBSFNSpecSource(sessionID);
                ab2 = specSource2.getBSFNMethod("GetEffectiveAddress").createExecutable();
            } catch (SystemException e) {
                e.printStackTrace();
                System.out.println("root cause is:" + e.getRootException().getMessage());
                e.getRootException().printStackTrace();
            }
        }

		protected void tearDown() {
		if (!testToken)
            abTest.connector.logoff(abTest.sessionID);
        else {
            abTest.connector.logoff(abTest.sessionID);
            abTest.connectorToken.logoff(abTest.connectorSessionID);
        }
		}
        public TestAddressbook(String s) {
            super(s);
        }
        public void testGetAddress() {
            try {
                for (int i = 0; i < multipleRun; i++) {
                    getAddress(1001);
                }
            } catch (SystemException e) {
                System.out.println(e.toString());
                e.getRootException().printStackTrace();
            }
        }
        public void testGetAddressMap() {
            try {
                for (int i = 0; i < multipleRun; i++) {
                    getAddressMap(1001);
                }
            } catch (SystemException e) {
                System.out.println(e.toString());
                e.getRootException().printStackTrace();
            }
        }
        public void getAddress(int addressNo) throws SystemException {
            System.out.println("get address book ID=" + addressNo);
            ab.resetValues();
            ab.setValue("mnAddressNumber", String.valueOf(addressNo));
            BSFNExecutionWarning warning = null;
            if (!testToken) {
                warning = ab.execute(sessionID);
            }
            else {
                warning = ab.execute(connectorSessionID);
            }
            System.out.println("szNamealpha=" + ab.getValue("szNamealpha").toString());
            System.out.println("mnAddressNumber=" + ab.getValue("mnAddressNumber").toString());
            System.out.println("szAddressLine1=" + ab.getValue("szAddressLine1").toString());
            System.out.println("szZipCodePostal=" + ab.getValue("szZipCodePostal").toString());
            if (warning != null) {
                System.out.println(warning.toString());
            }
        }
        public void getAddressMap(int addressNo) throws SystemException {
            // clear parameter set
            ab.resetValues();
            // input
            Map input = new Hashtable();
            input.put("mnAddressNumber", String.valueOf(addressNo));
            ab.setValues(input);
            // execute
            if (!testToken) {
                BSFNExecutionWarning errors = ab.execute(sessionID);
            }
            else {
                BSFNExecutionWarning errors = ab.execute(connectorSessionID);
            }
            //output
            Map output = ab.getValues();
            System.out.println("szNamealpha=" + output.get("szNamealpha").toString());
            System.out.println("mnAddressNumber=" + output.get("mnAddressNumber").toString());
            System.out.println("szAddressLine1=" + output.get("szAddressLine1").toString());
            System.out.println("szZipCodePostal=" + output.get("szZipCodePostal").toString());
            //call ab2,
            // clear parameter set
            ab2.resetValues();
            // input
            Map input2 = new Hashtable();
            input2.put("mnAddressNumber", String.valueOf(addressNo));
            ab2.setValues(input2);
            // execute
            if (!testToken) {
                BSFNExecutionWarning errors2 = ab2.execute(sessionID);
            } else {
                BSFNExecutionWarning errors2 = ab2.execute(connectorSessionID);
            }
            //output
            Map output2 = ab2.getValues();
            System.out.println("szNamealpha=" + output2.get("szNamealpha").toString());
            System.out.println("mnAddressNumber=" + output2.get("mnAddressNumber").toString());
            System.out.println("szAddressLine1=" + output2.get("szAddressLine1").toString());
            System.out.println("szZipCodePostal=" + output2.get("szZipCodePostal").toString());
        }
    }
