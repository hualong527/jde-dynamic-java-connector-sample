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
 * Indicates an object that has state that can be observed.
 */
public interface EventAppObservable
{
    //=================================================
    // Public static final fields.
    //=================================================

    //=================================================
    // Methods.
    //=================================================
    
    /**
     * Adds an Observer to the list of Observers.
     * 
     * @param observer the Observer to add
     */
    void addObserver(EventAppObserver observer);
    
    /**
     * Deletes an Observer from the list of Observers.
     * 
     * @param observer the Observer to delete
     */
    void deleteObserver(EventAppObserver observer);
}
