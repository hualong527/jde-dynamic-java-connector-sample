package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * PersistListenerImpl.java
 *
 * Created on October 10, 2000, 11:20 AM
 */

import com.jdedwards.system.connector.dynamic.events.*;
import com.jdedwards.system.connector.dynamic.sample.events.Listener;
import com.jdedwards.system.connector.dynamic.sample.events.SinkFrame;

/**
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestPersistListenerImpl 
 */
public class PersistListenerImpl extends Listener implements PersistentListener
{

    /** Creates new PersistListenerImpl */
    public PersistListenerImpl(SinkFrame frame)
    {
        super(frame);
    }

}
