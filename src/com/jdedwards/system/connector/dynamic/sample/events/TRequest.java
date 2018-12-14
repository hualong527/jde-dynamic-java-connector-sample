package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * TRequest.java
 *
 * Created on August 24, 2000, 5:32 PM
 */
 
import java.io.IOException;
import com.jdedwards.system.net.*;


/** 
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestTRequest 
 */
public class TRequest
{

  String  m_xmlRequestDocument;
  long    m_timeout ;
  String  m_host ;
  String  m_msgType ;
  int     m_port ;
  String  m_environ;
  
  /** Creates new TRequest */
  public TRequest(String host, String env, int port, String doc, String msgType, int timeout) {
    m_host = host;
    m_port = port;
    m_xmlRequestDocument = doc;
    m_msgType = msgType;
    m_timeout = timeout;
    m_environ = env;
  }


  public void send() throws IOException
  {
    JdeSocket soc = null;
    boolean socketOk = true;

    

   
    /**
     * Constructe a message of the correct type
     */
    JdeMsg aMsg = new JdeMsg(1, 0, JdeNetUtil.NET_DEFAULT_PRIORITY);


    /**
     * Create the packets
     */
    String msgHeader = m_msgType + ":" + m_environ;
      aMsg.addUnicodeData(msgHeader);
    aMsg.addData(m_xmlRequestDocument.getBytes("UTF8"));



    /**
     * Send/recv the JdeMsg
     */
    try {
      soc = JdeConnectionManager.getManager().checkout(m_host, m_port, m_timeout);
      soc.setTimeout(m_timeout);
      aMsg.send(soc);
    } catch (JdeNetConnectionClosedException e) {
      /**
       * We might be able to through away this socket and get another
       * one to try again.  But for now, we just return the error
       * to the caller.
       */
      socketOk = false;
      throw new IOException(e.toString());
    } catch (JdeNetTimeoutException e) {
      /**
       * Timeout errors are fine. Don't log them
       */
      throw new IOException(e.toString());
    } catch (IOException e)	{
      /**
       * We might be able to through away this socket and get another
       * one to try again.  But for now, we just return the error
       * to the caller.
       */
      socketOk = false;
      throw e;
    } finally {
      /**
       * If the connection is BAD, notify the pool that this connection is bad.
       */
      if  (soc != null) {
        if (socketOk)
        JdeConnectionManager.getManager().checkin(soc);
        else
        JdeConnectionManager.getManager().checkinBad(soc);
      }
    }

  }

}  