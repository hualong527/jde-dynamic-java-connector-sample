package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * CountedPersistListenerImpl.java
 *
 * Created on October 10, 2000, 11:27 AM
 */

import com.jdedwards.system.connector.dynamic.events.*;

/**
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestCountedPersistListenerImpl 
 */
public class CountedPersistListenerImpl extends CountedListenerImpl implements PersistentListener
{

    /** Creates new CountedPersistListenerImpl */
    public CountedPersistListenerImpl(int count, SinkFrame frame)  
    {
        super(count, frame);
    }

}
