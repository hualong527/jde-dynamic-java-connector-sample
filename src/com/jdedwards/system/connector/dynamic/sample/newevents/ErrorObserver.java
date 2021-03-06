//============================================================================
//
// Copyright � [2004] 
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
 * Description of the class.
 */
public interface ErrorObserver extends EventAppObserver
{
    //=================================================
    //Public static final fields.
    //=================================================

    //=================================================
    //Methods.
    //=================================================
    
    /**
     * Indicates that an error has been generated by an Observable object.
     * 
     * @param message the error message
     */
    void errorGenerated(String message);
}
