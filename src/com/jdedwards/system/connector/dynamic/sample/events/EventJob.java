package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * EventJob.java
 *
 * Created on August 24, 2000, 6:35 PM
 */
 
import java.io.IOException;

/** 
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestEventJob 
 */
public class EventJob extends Thread {

  EventData m_data;
  /** Creates new EventJob */
  public EventJob(EventData data) 
  {
    m_data = data;    
  }
  
  public void run()
  {
    
    for( int i = 0; i < m_data.getCalls(); i++ )
    {
      try
      {
        TRequest req = new TRequest(m_data.getHost(), m_data.getEnviron(), m_data.getPort(), m_data.getData(), m_data.getEventType(), m_data.getTimeout()) ;
        req.send();    
      }
      catch(IOException e)
      {
        System.out.println(e.toString());
      }
    }
    
  }
  
}