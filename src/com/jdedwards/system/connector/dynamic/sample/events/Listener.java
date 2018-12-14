package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * Listener.java
 *
 * Created on August 25, 2000, 1:44 PM
 */
 
import com.jdedwards.system.connector.dynamic.events.*;

/** 
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestListener 
 */
public class Listener implements EventListener 
{
  SinkFrame m_Frame;
  boolean   m_paused = false;
  
  /** Creates new Listener */
  public Listener(SinkFrame frame) 
  {
    m_Frame = frame;
  }
  
  
  public synchronized String getEventType() 
  {
    return m_Frame.getEventType();
  }
  public synchronized void onOneWorldEvent(EventObject p1)
  {
    System.out.println("Received event: " + p1.getType());
    m_Frame.onOneWorldEvent(p1) ;
  }
  public void setEventType(String p1)
  {
  }
  
  public synchronized boolean isPaused()
  {
      return m_paused;
  }
  public synchronized void setPause(boolean pause)
  {
      m_paused = pause;
  }
  
}