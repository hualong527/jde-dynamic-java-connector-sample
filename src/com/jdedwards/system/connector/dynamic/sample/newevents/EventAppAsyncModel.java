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
import java.util.Iterator;

import com.jdedwards.system.connector.dynamic.newevents.AsyncEventSession;
import com.jdedwards.system.connector.dynamic.newevents.EventErrorListener;
import com.jdedwards.system.connector.dynamic.newevents.EventObject;
import com.jdedwards.system.connector.dynamic.newevents.EventSession;

//=================================================
// Imports from org namespace
//=================================================

/**
 * EventAppModel for asynchronous EventSessions.
 */
public class EventAppAsyncModel extends EventAppModel implements EventErrorListener
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
    public EventAppAsyncModel(int sessionID, boolean isAutoAck)
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
                setEventSession(getEventService().getAsyncEventSession(getSessionID(),
                                EventSession.AUTO_ACKNOWLEDGE));
            }
            else
            {
                setEventSession(getEventService().getAsyncEventSession(getSessionID(),
                                EventSession.CLIENT_ACKNOWLEDGE));
            }
        }
        catch (Exception e)
        {
            notifyErrorObservers(e);
            return;
        }
        
        // register as Connector event listener
        try
        {
            ((AsyncEventSession)getEventSession()).registerListener(this);
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
     * {@inheritDoc}
     */
    public void onEvent(EventObject message)
    {
        Iterator iter = getObserverList().iterator();
        while (iter.hasNext())
        {
            EventAppObserver observer = (EventAppObserver)iter.next();
            if (observer instanceof AsyncEventObserver)
            {
                ((AsyncEventObserver)observer).messageReceived(message);
            }
        }
    }

    /**
     * {@inheritdoc}
     */
    public void onError(String errorMsg, Throwable e)
    {
        
        Iterator iter = getObserverList().iterator();
        while (iter.hasNext())
        {
            EventAppObserver observer = (ErrorObserver)iter.next();
            if (observer instanceof ErrorObserver)
            {
                ((ErrorObserver)observer).errorGenerated(errorMsg);
            }
        }
        
    }
    
    
}
