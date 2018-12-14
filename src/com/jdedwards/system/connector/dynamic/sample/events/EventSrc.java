package com.jdedwards.system.connector.dynamic.sample.events;

import com.jdedwards.system.connector.dynamic.sample.events.EventData;
import com.jdedwards.system.connector.dynamic.sample.events.EventJob;

/*
 * EventSrc.java
 *
 * Created on August 24, 2000, 3:50 PM
 */
 


/** 
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestEventSrc 
 */
public class EventSrc extends Object {

  EventData m_data;
  /** Creates new EventSrc */
  public EventSrc( EventData data) 
  {
    m_data = data;
    
    // log onto the server
  }
  
  public void fireEvent()
  {
    for( int i = 0 ; i < m_data.getThreads(); i++ )
    {
      new EventJob(m_data).start();
    }
  }
  
}