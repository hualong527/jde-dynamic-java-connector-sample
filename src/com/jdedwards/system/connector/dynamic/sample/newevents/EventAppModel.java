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
import java.util.LinkedList;
import java.util.List;

//=================================================
// Imports from javax namespace
//=================================================

//=================================================
// Imports from com namespace
//=================================================
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.SystemException;
import com.jdedwards.system.connector.dynamic.newevents.EventService;
import com.jdedwards.system.connector.dynamic.newevents.EventSession;

//=================================================
// Imports from org namespace
//=================================================

/**
 * Description of the class.
 */
public abstract class EventAppModel implements EventAppObservable
{
    //=================================================
    // Non-public static class fields.
    //=================================================
    
    /** Connector instance. */
    private static final Connector CON = Connector.getInstance();
    
    /** EventService instance. */
    private static final EventService EVENT_SVC = EventService.getInstance();
    
    //=================================================
    // Public static final fields.
    //=================================================

    // session states
    
    /** Indicates an opened session. */
    public static final int SESSION_OPENED  = 0;
    /** Indicates a closed session. */
    public static final int SESSION_CLOSED  = 1;
    /** Indicates a started session. */
    public static final int SESSION_STARTED = 2;
    /** Indicates a stopped session. */
    public static final int SESSION_STOPPED = 3;

    //=================================================
    // Instance member fields.
    //=================================================
    
    /** The Connector session ID. */
    private int mSessionID = 0;
    
    /** The EventSession. */
    private EventSession mEventSession = null;
    
    /** The acknowledgement mode. */
    private boolean mIsAutoAck = false;
    
    /** The list of Observers. */
    private List mObserverList = new LinkedList(); 

    //=================================================
    // Constructors.
    //=================================================

    /**
     * Creates an EventAppModel with the associated Connector sessionID and acknowledgement mode..
     * 
     * @param sessionID the Connector session ID
     * @param isAutoAck whether the acknowledgment mode is AUTO
     */
    public EventAppModel(int sessionID, boolean isAutoAck)
    {
        mSessionID = sessionID;
        mIsAutoAck = isAutoAck;
    }

    //=================================================
    // Methods.
    //=================================================
    
    /**
     * Creates a new EventSession.
     */
    public abstract void open();
    
    /**
     * Closes the EventSession.
     */
    public void close()
    {
        if (mEventSession != null && !mEventSession.isClosed())
        {
            try
            {
                mEventSession.close();
            }
            catch (SystemException e)
            {
                notifyErrorObservers(e);
                return;
            }
        }
        
        // notify observers
        notifySessionStateObservers(SESSION_CLOSED);
    }
    
    /**
     * Starts message delivery on the EventSession.
     */
    public void start()
    {
        if (mEventSession != null && !mEventSession.isStarted())
        {
            try
            {
                mEventSession.start();
            }
            catch (Exception e)
            {
                notifyErrorObservers(e);
                return;
            }
        }
        
        // notify observers
        notifySessionStateObservers(SESSION_STARTED);
    }
    
    /**
     * Stops message delivery on the EventSession.
     */
    public void stop()
    {
        if (mEventSession != null && mEventSession.isStarted())
        {
            try
            {
                mEventSession.stop();
            }
            catch (Exception e)
            {
                notifyErrorObservers(e);
                return;
            }
        }
        
        // notify observers
        notifySessionStateObservers(SESSION_STOPPED);
    }

    /**
     * Shuts down the model.
     */
    public void shutdown()
    {
        // properly shut down the EventSession if not already done so
        if (mEventSession != null && !mEventSession.isClosed())
        {
            try
            {
                if (mEventSession.isStarted())
                {
                    mEventSession.stop();
                }
                mEventSession.close();
            }
            catch (Exception e)
            {
                notifyErrorObservers(e);
                
                // print errors to console window, as views are probably shutting down also
                e.printStackTrace();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void addObserver(EventAppObserver observer)
    {
        mObserverList.add(observer);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteObserver(EventAppObserver observer)
    {
        mObserverList.remove(observer);
    }

    /**
     * Notifies SessionStateObservers of a change in EventSession state.
     * 
     * @param sessionState the EventSession state
     */
    protected void notifySessionStateObservers(int sessionState)
    {
        Iterator iter = mObserverList.iterator();
        while (iter.hasNext())
        {
            EventAppObserver observer = (EventAppObserver)iter.next();
            if (observer instanceof SessionStateObserver)
            {
                SessionStateObserver sessionOb = (SessionStateObserver)observer;
                switch(sessionState)
                {
                    case SESSION_OPENED:
                        sessionOb.sessionOpened();
                        break;
                    case SESSION_CLOSED:
                        sessionOb.sessionClosed();
                        break;
                    case SESSION_STARTED:
                        sessionOb.sessionStarted();
                        break;
                    case SESSION_STOPPED:
                        sessionOb.sessionStopped();
                        break;
                    default:
                        // do nothing
                }
            }
        }
    }
    
    /**
     * Notifies all ErrorObservers that an error has occurred.
     * 
     * @param errorMessage the error message
     */
    protected void notifyErrorObservers(String errorMessage)
    {
        Iterator iter = mObserverList.iterator();
        while (iter.hasNext())
        {
            EventAppObserver observer = (EventAppObserver)iter.next();
            if (observer instanceof ErrorObserver)
            {
                ((ErrorObserver)observer).errorGenerated(errorMessage);
            }
        }
    }
    
    /**
     * Notifies all ErrorObservers that an Exception has occurred.
     * 
     * @param e the Exception
     */
    protected void notifyErrorObservers(Exception e)
    {
        this.notifyErrorObservers(e.toString());
    }

    /**
     * @return the Event Session
     */
    protected EventSession getEventSession()
    {
        return mEventSession;
    }

    /**
     * @param session the EventSession
     */
    protected void setEventSession(EventSession session)
    {
        mEventSession = session;
    }

    /**
     * @return auto acknowledgement indicator
     */
    protected boolean isAutoAck()
    {
        return mIsAutoAck;
    }

    /**
     * @return Connector session ID
     */
    protected int getSessionID()
    {
        return mSessionID;
    }
    
    
    /**
     * @return the EventService object
     */
    protected static EventService getEventService()
    {
        return EVENT_SVC;
    }

    /**
     * @return the Observer list
     */
    protected List getObserverList()
    {
        return mObserverList;
    }
}
