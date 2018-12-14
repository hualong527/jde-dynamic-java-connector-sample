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
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

//=================================================
// Imports from javax namespace
//=================================================
import javax.swing.JFrame;

//=================================================
// Imports from com namespace
//=================================================
import com.jdedwards.system.connector.dynamic.sample.util.SignonDialog;

//=================================================
// Imports from org namespace
//=================================================

/**
 * Utility class to launch the Event sample application.
 */
public class EventApp
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
     * A private, default constructor to prevent users from creating instances of this class.
     */
    private EventApp()
    {
    }
    
    //=================================================
    // Methods.
    //=================================================
    
    /**
     * Launches the Event sample application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        // show centered signon dialog first
        GraphicsConfiguration gc = 
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        JFrame emptyFrame = new JFrame(gc);
        SignonDialog dialog = new SignonDialog(emptyFrame);
        dialog.setLocationRelativeTo(emptyFrame);
        dialog.setVisible(true);
        int sessionID = dialog.getSessionID();
        
        // create view and center on screen
        EventAppView app = new EventAppView(sessionID, dialog);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = app.getSize();
        app.setLocation(Math.max(0,(screenSize.width -windowSize.width)/2), 
                        Math.max(0,(screenSize.height-windowSize.height)/2));
        app.setVisible(true);
    }
}
