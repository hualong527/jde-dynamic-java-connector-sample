//============================================================================
//
//Copyright © [2004] 
//PeopleSoft, Inc.  
//All rights reserved. Oracle USA Proprietary and Confidential.
//Peoplesoft, PeopleTools and PeopleBooks are registered trademarks of Oracle USA.
//
//============================================================================

package com.jdedwards.system.connector.dynamic.jmstopic;

//=================================================
// Imports from java namespace
//=================================================
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Properties;

//=================================================
//Imports from javax namespace
//=================================================
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

//=================================================
// Imports from com namespace
//=================================================


//=================================================
// Imports from org namespace
//=================================================


public class JMSTopicClientApp implements MessageListener
{
	
	//=================================================
	// Non-public class fields.
	//=================================================
	private TopicConnectionFactory topicConnectionFactory = null;
	private Topic topicToPublish = null;
	private InitialContext context = null;
	private TopicConnection conn = null;
	private TopicSession session = null;
	private TopicSubscriber subscriber = null;
	private Topic topic = null;
	private TextMessage message = null;
	
	private String topicConnFact = null;
	private String topicName = null;
	private String subsName = null;
	private String initialCtxFactory = null;
	private String providerURL = null;
	private String persistence = null;
	private String outputDir = null;

	//=================================================
	// Public static final fields.
	//=================================================
	private static int eventCount = 0;
	

	//=================================================
	// Constructors.
	//=================================================
	public JMSTopicClientApp(){
	
		Properties properties = new Properties();
		//Read the Properties File
		try {
			properties.load(new FileInputStream("jms.properties"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		topicConnFact = properties.getProperty("topicconnectionfactory").toString().trim();
		topicName = properties.getProperty("topic").toString().trim();
		subsName = properties.getProperty("subscriberID").toString().trim();
		initialCtxFactory = properties.getProperty("initialcontextfactory").toString().trim();
		providerURL = properties.getProperty("providerurl").toString().trim();
		persistence = properties.getProperty("persistence").toString().trim();
		outputDir = properties.getProperty("outputdir").toString().trim();
		System.out.println("Properties read from jms.properties file");
		
		//Set the Initial Context Factory and Provider URL
		properties.put(Context.INITIAL_CONTEXT_FACTORY, initialCtxFactory);
		properties.put(Context.PROVIDER_URL, providerURL);


		try {
			//Create InitialContext
			context = new InitialContext(properties);
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		try {
			//Get the environment variables on the app server
			Hashtable ht = context.getEnvironment();
			System.out.println(ht.toString());

			Object o = context.lookup(topicConnFact);
			System.out.println(o.getClass().getName());
			topicConnectionFactory = (TopicConnectionFactory)o;
			System.out.println("Topic Connection Facotry Created \n");

			conn = topicConnectionFactory.createTopicConnection();
			System.out.println("Connection Created ");

			conn.start();
			System.out.println("Connection Started ");

			session = conn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("Session Created \n");

			topic = (Topic) context.lookup(topicName);
			System.out.println("Found Topic \n");

			subscriber = session.createDurableSubscriber(topic, subsName);
			subscriber.setMessageListener(this);
			System.out.println("Subscriber Created \n");
			System.out.println("Session opened successfully. Count"+eventCount);
			System.out.println("Events data will be saved in the logs directory");

		} catch (NamingException e2) {
			e2.printStackTrace();
		} catch (JMSException e3) {
			e3.printStackTrace();
		} catch (Exception e4) {
		e4.printStackTrace();
	}
		
		
	}

	//=================================================
	// Methods.
	//=================================================
	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message jmsMessage) {
		try {
			eventCount++;
			System.out.println("JMSTopic Message Recieved. Count: " + eventCount);

			if(persistence.equals("true") ){
				String fileName = outputDir + File.separatorChar + "Event_" + eventCount + ".xml";
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
				message = (TextMessage) jmsMessage;
				String xmlPayLoad = null;
				if(message!=null){
					xmlPayLoad = message.getText();
				} else {
					xmlPayLoad = "Null message recieved in onMessage";
				}
				out.write(xmlPayLoad);
				out.close();
				}
				catch (IOException e) {
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
	public void closeSubscriber() {
		try {
			System.out.println("Closing subscriber");
			subscriber.close();
		} catch (JMSException e) {
			System.err.println("Exception occurred: " +
				e.toString());
		}
	}

	public void cleanUp() {
		if (conn != null) {
			try {
				System.out.println("Closing Connection ");
				conn.close();
			} catch (JMSException e) {
				System.err.println("Exception occurred: " +
					e.toString());
					}
		}
	}
	public static void main(String[] args) {
		char userInput = 'r';
		InputStreamReader  inputStreamReader = null;
		JMSTopicClientApp jmsDriver = new JMSTopicClientApp();
		inputStreamReader = new InputStreamReader(System.in);
		System.out.println("To Stop Listening to Events, type q, " + "and hit Enter");
		while(!(userInput == 'q')){
		try {
			userInput = (char)inputStreamReader.read();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}
	jmsDriver.closeSubscriber();
	jmsDriver.cleanUp();
	System.out.println("Stopped JMS Topic Driver");
}


}
