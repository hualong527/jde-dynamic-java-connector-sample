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

//=================================================
// Imports from org namespace
//=================================================

/**
 * Observer for the EventApp sample for event session state changes.
 */
public interface SessionStateObserver extends EventAppObserver
{
    //=================================================
    //Public static final fields.
    //=================================================

    //=================================================
    //Methods.
    //=================================================
    
    /**
     * Called when a new EventSession is created.
     */
    void sessionOpened();
    
    /**
     * Called when the EventSession has been closed.
     */
    void sessionClosed();

    /**
     * Called when the EventSession has been started.
     */
    void sessionStarted();
    
    /**
     * Called when the EventSession has been stopped.
     */
    void sessionStopped();
}
