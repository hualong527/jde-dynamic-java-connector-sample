package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * CountedListenerImpl.java
 *
 * Created on August 30, 2000, 2:55 PM
 */
import com.jdedwards.system.connector.dynamic.events.*;


/**
 *
 * @author  KK949259
 * @version 
 */
class CountedListenerImpl extends Listener implements CountedListener
{

    int m_count;
    /** Creates new CountedListenerImpl */
    public CountedListenerImpl(int count, SinkFrame frame)
    {
        super(frame);
        m_count = count;
    }
    
    public synchronized void decCallCount() {
        --m_count;
    }

    public synchronized int getCallCount() {
            return m_count;
    }

}
