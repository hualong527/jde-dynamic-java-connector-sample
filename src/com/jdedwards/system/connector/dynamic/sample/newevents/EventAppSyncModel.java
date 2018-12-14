//============================================================================
//
// Copyright © [2004] 
// PeopleSoft, Inc.  
// All rights reserved. PeopleSoft Proprietary and Confidential.
// PeopleSoft, PeopleTools and PeopleBooks are registered trademarks of PeopleSoft, Inc.
//
//============================================================================

package com.jdedwards.system.connector.dynamic.sample.newevents;

//=================================================
// Imports from java namespace
//=================================================

//=================================================
// Imports from javax namespace
//=================================================

//=================================================
// Imports from com namespace
//=================================================
import com.jdedwards.system.connector.dynamic.newevents.EventObject;
import com.jdedwards.system.connector.dynamic.newevents.EventSession;
import com.jdedwards.system.connector.dynamic.newevents.SyncEventSession;

//=================================================
// Imports from org namespace
//=================================================

/**
 * EventAppModel for synchronous EventSessions.
 */
public class EventAppSyncModel extends EventAppModel
{
    //=================================================
    // Non-public static class fields.
    //=================================================

    //=================================================
    // Public static final fields.
    //=================================================

    //=================================================
    // Instance member fields.
    //=================================================

    //=================================================
    // Constructors.
    //=================================================

    /**
     * {@inheritDoc}
     */
    public EventAppSyncModel(int sessionID, boolean isAutoAck)
    {
        super(sessionID, isAutoAck);
    }

    //=================================================
    // Methods.
    //=================================================

    /**
     * {@inheritDoc}
     */
    public void open()
    {
        // check to see if an EventSession has already been started
        if (getEventSession() != null && getEventSession().isStarted())
        {
            notifyErrorObservers("An Event Session has already been started.");
            return;
        }
        
        try
        {
            // start the appropriate type of EventSession
            if (isAutoAck())
            {
                setEventSession(getEventService().getSyncEventSession(getSessionID(),
                                EventSession.AUTO_ACKNOWLEDGE));
            }
            else
            {
                setEventSession(getEventService().getSyncEventSession(getSessionID(),
                                EventSession.CLIENT_ACKNOWLEDGE));
            }
        }
        catch (Exception e)
        {
            notifyErrorObservers(e);
            return;
        }
        
        // notify observers
        notifySessionStateObservers(SESSION_OPENED);
    }

    /**
     * Receives the next message produced for this message consumer. 
     * 
     * <p>This call blocks indefinitely until a message is produced. 
     * 
     * @return the next message produced for this message consumer
     */
    public EventObject receive()
    {
        try
        {
            return ((SyncEventSession)getEventSession()).receive();
        }
        catch (Exception e)
        {
            notifyErrorObservers(e);
            return null;
        }
    }
    
    /**
     * Receives the next message that arrives within the specified timeout interval. 
     * This call blocks until a message arrives or the timeout expires. A timeout of zero never
     * expires, and the call blocks indefinitely.
     * 
     * @param timeout the timeout value (in milliseconds)
     * @return the next message produced for this message consumer, or null if the timeout
     *         expires
     */
    public EventObject receive(long timeout)
    {
        try
        {
            return ((SyncEventSession)getEventSession()).receive(timeout);
        }
        catch (Exception e)
        {
            notifyErrorObservers(e);
            return null;
        }
    }
    
    /**
     * Receives the next message if one is immediately available.
     * 
     * @return the next message produced for this message consumer, or null if one is not
     *         available
     */
    public EventObject receiveNoWait()
    {
        try
        {
            return ((SyncEventSession)getEventSession()).receiveNoWait();
        }
        catch (Exception e)
        {
            notifyErrorObservers(e);
            return null;
        }
    }
}
