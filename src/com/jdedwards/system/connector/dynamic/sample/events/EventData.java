/*
 * EventData.java
 *
 * Created on August 24, 2000, 5:21 PM
 */
 
 package com.jdedwards.system.connector.dynamic.sample.events;

/** 
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestEventData 
 */
public class EventData 
{

  /** Creates new EventData */
    String m_host;
    String m_eventType;
    int    m_port;
    int    m_threads;
    int    m_calls;
    String  m_data;
    int     m_timeout;
    String  m_environ;

    public EventData(String host, String env, String type, String data, int port, int timeout, int threads, int calls)
    {
      m_host = host;
      m_eventType = type;
      m_port = port;
      m_threads = threads;
      m_calls = calls;
      m_data = data;
      m_timeout = timeout;
      m_environ = env;
      
    }
    
    public String getHost() { return m_host; }
    public void setHost(String host) { m_host = host; }
    public String getEventType() { return m_eventType; }
    public void setEventType(String type) { m_eventType = type; }
    public int getPort() { return m_port; }
    public void setPort(int port) { m_port = port; }
    public int getThreads() { return m_threads; }
    public void setThreads(int threads) { m_threads = threads; }
    public int getCalls() { return m_calls; }
    public void setCalls(int calls) { m_calls = calls; }
    public String getData() { return m_data ; }
    public void setData(String data) { m_data = data; }
    public int getTimeout() { return m_timeout; }
    public void setTimeout(int timeout) { m_timeout = timeout; }
    public String getEnviron() { return m_environ; }
    public void setEnviron(String env) { m_environ = env; }
}