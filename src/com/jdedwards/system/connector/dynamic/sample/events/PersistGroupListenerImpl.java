package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * GroupListenerImpl.java
 *
 * Created on August 25, 2000, 1:44 PM
 */

import com.jdedwards.system.connector.dynamic.events.*;

/**
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestPersistGroupListenerImpl
 */

public class PersistGroupListenerImpl extends Listener implements PersistentListener, GroupListener
{
    String m_groupID;
    /** Creates new GroupListenerImpl */
    public PersistGroupListenerImpl(String groupID, SinkFrame frame)
    {
        super(frame);
        m_groupID= groupID;
    }
    public void setGroupID(String groupID)
    {
    }

    public String getGroupID()
    {
    return m_groupID;
    }
}
