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

//=================================================
// Imports from org namespace
//=================================================

/**
 * Observer for the EventApp sample for receipt of asynchronous messages.
 */
public interface AsyncEventObserver extends EventAppObserver
{
    //=================================================
    //Public static final fields.
    //=================================================

    //=================================================
    //Methods.
    //=================================================
    
    /**
     * Called when a message is received.
     * 
     * @param message the event message
     */
    void messageReceived(EventObject message);
}
