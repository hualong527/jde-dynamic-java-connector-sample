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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Properties;
import java.util.Hashtable;

//=================================================
// Imports from javax namespace
//=================================================
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//=================================================
// Imports from com namespace
//=================================================
import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.newevents.EventObject;
import com.jdedwards.system.connector.dynamic.newevents.EventService;
import com.peoplesoft.pt.e1.common.events.EventTypeDefinition;
import com.peoplesoft.pt.e1.common.events.connectorsvc.IConnectorService;
import com.peoplesoft.pt.e1.common.events.SubscribedToEventType;
import com.peoplesoft.pt.e1.common.events.Subscription;
import com.jdedwards.base.util.IniFileLoader;
import com.jdedwards.base.logging.JdeLog;

//=================================================
// Imports from org namespace
//=================================================
import com.jdedwards.system.connector.dynamic.sample.util.SignonDialog;

/**
 * Description of the class.
 */
public class EventAppView extends JFrame implements ErrorObserver, SessionStateObserver,
    AsyncEventObserver
{
    //=================================================
    // Non-public static class fields.
    //=================================================

    /** Connector instance. */
    private static final Connector CON = Connector.getInstance();

    /**
     * Max number of events to hold onto before the oldest is discarded when a new one
     * is received.
     */
    private static final int MAX_MESSAGES = 100;

    /**
     * System property to set if it is desired that every event received
     * be sent to an XML file.  This property should be set to a directory but
     * not end with a slash (separator character).
     */
    public static final String OUTPUT_DIRECTORY = "outputDir";

    //=================================================
    // Public static final fields.
    //=================================================

    //=================================================
    // Instance member fields.
    //=================================================

    private int mSessionID = 0;
    private int mMessageCount = 0;
    private EventAppModel mModel = null;
    private Map mMessageMap = new HashMap();

    // panes
    private JPanel mMainPane = null;
    private JPanel mSessionOpsPane = null;
    private JPanel mSyncOpsPane = null;
    private JPanel mMessageOpsPane = null;
    private JPanel mIntrospectOpsPane = null;
    private JPanel mErrorMessagePane = null;

    // radio buttons
    private JRadioButton mAsyncButton = null;
    private JRadioButton mSyncButton = null;
    private JRadioButton mAutoAckButton = null;
    private JRadioButton mClientAckButton = null;
	private JCheckBox mWriteXMLToDisk = null;

    // buttons
    private JButton mOpenButton = null;
    private JButton mCloseButton = null;
    private JButton mStartButton = null;
    private JButton mStopButton = null;
    private JButton mReceiveWaitButton = null;
    private JButton mReceiveTimeoutButton = null;
    private JButton mReceiveNoWaitButton = null;
    private JButton mAckSelectedButton = null;
    private JButton mEventListButton = null;
    private JButton mEventListEnvButton = null;
    private JButton mEventTemplateButton = null;
    private JButton mSubscriptButton = null;
    private JButton mClearErrorButton = null;

    // text fields
    private JTextField mTimeoutField = null;
    private JTextField mMessageCountField = null;
    private JTextField mEventListEnvField = null;
    private JTextField mEventTemplTypeField = null;
    private JTextField mEventTemplEnvField = null;

    // text areas
    private JTextArea mMessageDataArea = null;
    private JTextArea mErrorMessageArea = null;

    // lists
    private JList mMessageList = null;
    private JList mEventTemplCatList = null;

    // list models
    private DefaultListModel mMessageListModel = null;
    
    private SignonDialog sDlg = null;

    //=================================================
    // Constructors.
    //=================================================

    /**
     * Constructs the Event application associated with the Connector session ID.
     *
     * @param sessionID the Connector session ID
     */
    public EventAppView(int sessionID, SignonDialog dlg)
    {
        super("Event Delivery");
        mSessionID = sessionID;
        sDlg = dlg;
        initialize();
    }

    //=================================================
    // Methods.
    //=================================================

    /**
     * {@inheritDoc}
     */
    public void sessionOpened()
    {
        // disable buttons
        mAsyncButton.setEnabled(false);
        mSyncButton.setEnabled(false);
        mAutoAckButton.setEnabled(false);
        mClientAckButton.setEnabled(false);
        mOpenButton.setEnabled(false);

        // enable buttons
        mCloseButton.setEnabled(true);
        mStartButton.setEnabled(true);
    }

    /**
     * {@inheritDoc}
     */
    public void sessionClosed()
    {
        // disable buttons
        mCloseButton.setEnabled(false);
        mStartButton.setEnabled(false);
        mStopButton.setEnabled(false);
        mReceiveWaitButton.setEnabled(false);
        mReceiveTimeoutButton.setEnabled(false);
        mReceiveNoWaitButton.setEnabled(false);
        mAckSelectedButton.setEnabled(false);

        // enable buttons
        mAsyncButton.setEnabled(true);
        mSyncButton.setEnabled(true);
        mAutoAckButton.setEnabled(true);
        mClientAckButton.setEnabled(true);
        mOpenButton.setEnabled(true);
    }

    /**
     * {@inheritDoc}
     */
    public void sessionStarted()
    {
        // disable buttons
        mStartButton.setEnabled(false);

        // enable buttons
        mStopButton.setEnabled(true);
        if (mSyncButton.isSelected())
        {
            mReceiveWaitButton.setEnabled(true);
            mReceiveTimeoutButton.setEnabled(true);
            mReceiveNoWaitButton.setEnabled(true);
        }
        if (this.mClientAckButton.isSelected())
        {
            mAckSelectedButton.setEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sessionStopped()
    {
        // disable buttons
        mStopButton.setEnabled(false);
        mReceiveWaitButton.setEnabled(false);
        mReceiveTimeoutButton.setEnabled(false);
        mReceiveNoWaitButton.setEnabled(false);
        mAckSelectedButton.setEnabled(false);

        // enable buttons
        mStartButton.setEnabled(true);
    }

    /**
     * {@inheritDoc}
     */
    public void messageReceived(EventObject message)
    {
        addMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    public void errorGenerated(String message)
    {
        mErrorMessageArea.append(message + "\n");
    }

    /***********************************************
     * GUI Setup Section
     ***********************************************/

    /**
     * Initializes the GUI frame.
     *
     */
    private void initialize()
    {
        setContentPane(getMainPane());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                if (mModel != null)
                {
                    mModel.shutdown();
                }

                // logout the user and shut down the Connector
                CON.logoff(mSessionID);
                CON.shutDown();

                // close all windows
                System.exit(0);
            }
        }
        );
        String username = CON.getUserSession(mSessionID).getUserName();
        setTitle("Event Delivery for " + username);
        pack();
        setResizable(false);
    }

    /**
     * This method initializes and returns the main pane.
     *
     * @return the initialized pane
     */
    private JPanel getMainPane()
    {
        if (mMainPane == null)
        {
            mMainPane = new JPanel();
            mMainPane.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            // add Session Operations pane
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            mMainPane.add(getSessionOpsPane(), c);

            // add Synchronous Operations pane
            c.gridy = 1;
            mMainPane.add(getSyncOpsPane(), c);

            // add Message Operations pane
            c.gridy = 2;
            mMainPane.add(getMessageOpsPane(), c);

            // add Introspection Operations pane
            c.gridy = 3;
            mMainPane.add(getIntrospectOpsPane(), c);

            // add Error Message pane
            c.gridy = 4;
            mMainPane.add(getErrorMessagePane(), c);
        }
        return mMainPane;
    }

    /**
     * This method initializes and returns the Session Operations pane.
     *
     * @return the initialized pane
     */
    private JPanel getSessionOpsPane()
    {
        if (mSessionOpsPane == null)
        {
            mSessionOpsPane = new JPanel();
            mSessionOpsPane.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();

            c1.gridx = 0;
            c1.gridy = 0;
            c1.anchor = GridBagConstraints.WEST;
            c1.insets = new Insets(0, 30, 0, 30);
            mSessionOpsPane.add(new JLabel("Session Type:"), c1);

            c1.gridy = 1;
            mAsyncButton = new JRadioButton("Asynchronous");
            mAsyncButton.setSelected(true);
            mSessionOpsPane.add(mAsyncButton, c1);

            c1.gridy = 2;
            mSyncButton = new JRadioButton("Synchronous");
            mSessionOpsPane.add(mSyncButton, c1);

            ButtonGroup syncGroup = new ButtonGroup();
            syncGroup.add(mAsyncButton);
            syncGroup.add(mSyncButton);

            c1.gridx = 1;
            c1.gridy = 0;
            mSessionOpsPane.add(new JLabel("Acknowledgement Mode:"), c1);

            c1.gridy = 1;
            mAutoAckButton = new JRadioButton("Auto");
            mAutoAckButton.setSelected(true);
            mSessionOpsPane.add(mAutoAckButton, c1);

            c1.gridy = 2;
            mClientAckButton = new JRadioButton("Client");
            mSessionOpsPane.add(mClientAckButton, c1);

            ButtonGroup ackGroup = new ButtonGroup();
            ackGroup.add(mAutoAckButton);
            ackGroup.add(mClientAckButton);

            c1.gridx = 2;
            c1.gridy = 0;
            c1.fill = GridBagConstraints.HORIZONTAL;
            mSessionOpsPane.add(new JLabel("Session Creation:"), c1);

            c1.gridy = 1;
            mOpenButton = new JButton("Open Session");
            mOpenButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    createModel();
                }
            }
            );
            mSessionOpsPane.add(mOpenButton, c1);

            c1.gridy = 2;
            mCloseButton = new JButton("Close Session");
            mCloseButton.setEnabled(false);
            mCloseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    mMessageListModel.clear();
                    mMessageMap.clear();
                    mMessageDataArea.setText(null);
                    mModel.close();
                    mMessageCount = 0;
                    updateMessageCountField();
                }
            }
            );
            mSessionOpsPane.add(mCloseButton, c1);

            c1.gridx = 3;
            c1.gridy = 0;
            mSessionOpsPane.add(new JLabel("Event Delivery:"), c1);

            c1.gridy = 1;
            mStartButton = new JButton("Start");
            mStartButton.setEnabled(false);
            mStartButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    mModel.start();
                }
            }
            );
            mSessionOpsPane.add(mStartButton, c1);

            c1.gridy = 2;
            mStopButton = new JButton("Stop");
            mStopButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    mModel.stop();
                }
            }
            );
            mStopButton.setEnabled(false);
            mSessionOpsPane.add(mStopButton, c1);

            TitledBorder sessionBorder = BorderFactory.createTitledBorder(
                                             BorderFactory.createLoweredBevelBorder(),
                                             "Session Operations");
            mSessionOpsPane.setBorder(sessionBorder);
        }
        return mSessionOpsPane;
    }

    /**
     * This method initializes and returns the Synchronous Operations pane.
     *
     * @return the initialized pane
     */
    private JPanel getSyncOpsPane()
    {
        if (mSyncOpsPane == null)
        {
            mSyncOpsPane = new JPanel();
            mSyncOpsPane.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();

            c1.gridx = 0;
            c1.gridy = 0;
            c1.anchor = GridBagConstraints.NORTH;
            c1.insets = new Insets(0, 30, 0, 30);
            mReceiveWaitButton = new JButton("ReceiveAndWait");
            mReceiveWaitButton.setEnabled(false);
            mReceiveWaitButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    EventObject message = ((EventAppSyncModel)mModel).receive();
                    if (message != null)
                    {
                        addMessage(message);
                    }
                    else
                    {
                        mErrorMessageArea.append("No event received.\n");
                    }
                }
            }
            );
            mSyncOpsPane.add(mReceiveWaitButton, c1);

            c1.gridx = 1;
            c1.gridy = 0;
            c1.gridwidth = 2;
            c1.fill = GridBagConstraints.HORIZONTAL;
            mReceiveTimeoutButton = new JButton("Receive");
            mReceiveTimeoutButton.setEnabled(false);
            mReceiveTimeoutButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // validate input
                    String timeoutText = mTimeoutField.getText();
                    if (timeoutText == null || timeoutText.equals(""))
                    {
                        JOptionPane.showMessageDialog(null,
                            "Please enter a timeout value first.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    long timeout = 0;
                    try
                    {
                        timeout = Long.parseLong(timeoutText);
                    }
                    catch (NumberFormatException e1)
                    {
                        JOptionPane.showMessageDialog(null,
                            "You must enter a numeric value for the timeout that is equal to "
                            + "or greater than zero.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (timeout < 0)
                    {
                        JOptionPane.showMessageDialog(null,
                            "You must enter a numeric value for the timeout that is equal to "
                            + "or greater than zero.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // get event
                    EventObject message = ((EventAppSyncModel)mModel).receive(timeout);
                    if (message != null)
                    {
                        addMessage(message);
                    }
                    else
                    {
                        mErrorMessageArea.append("No event received.\n");
                    }
                }
            }
            );
            mSyncOpsPane.add(mReceiveTimeoutButton, c1);

            c1.gridy = 1;
            c1.gridwidth = 1;
            c1.fill = GridBagConstraints.NONE;
            c1.insets = new Insets(0, 30, 0, 0);
            mSyncOpsPane.add(new JLabel("Timeout(ms):"), c1);

            c1.gridx = 2;
            c1.insets = new Insets(0, 0, 0, 30);
            mTimeoutField = new JTextField(6);
            mSyncOpsPane.add(mTimeoutField, c1);

            // add Receive (no wait) button
            c1.gridx = 3;
            c1.gridy = 0;
            c1.insets = new Insets(0, 30, 0, 30);
            mReceiveNoWaitButton = new JButton("ReceiveNoWait");
            mReceiveNoWaitButton.setEnabled(false);
            mReceiveNoWaitButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    EventObject message = ((EventAppSyncModel)mModel).receiveNoWait();
                    if (message != null)
                    {
                        addMessage(message);
                    }
                    else
                    {
                        mErrorMessageArea.append("No event received.\n");
                    }
                }
            }
            );
            mSyncOpsPane.add(mReceiveNoWaitButton, c1);

            TitledBorder titledBorder = BorderFactory.createTitledBorder(
                                            BorderFactory.createLoweredBevelBorder(),
                                            "Synchronous Operations");
            mSyncOpsPane.setBorder(titledBorder);
        }
        return mSyncOpsPane;
    }

    /**
     * This method initializes and returns the Message Operations pane.
     *
     * @return the initialized pane
     */
    private JPanel getMessageOpsPane()
    {
        if (mMessageOpsPane == null)
        {
            mMessageOpsPane = new JPanel();
            mMessageOpsPane.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();

            // add Message List pane
            c1.gridx = 0;
            c1.gridy = 0;
            c1.gridwidth = 2;
            c1.anchor = GridBagConstraints.WEST;
            c1.insets = new Insets(0, 5, 0, 10);
            mMessageOpsPane.add(new JLabel("Event List:"), c1);

            c1.gridy = 1;
            mMessageListModel = new DefaultListModel();
            mMessageList = new JList(mMessageListModel);
            mMessageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            mMessageList.setVisibleRowCount(6);
            mMessageList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting())
                    {
                        int index = mMessageList.getSelectedIndex();
                        if (index == -1)
                        {
                            return;
                        }
                        showMessage(index);
                    }
                }
            }
            );
            JScrollPane listScrollPane = new JScrollPane(mMessageList,
                                                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            mMessageOpsPane.add(listScrollPane, c1);

            c1.gridy = 2;
            c1.fill = GridBagConstraints.HORIZONTAL;
            mAckSelectedButton = new JButton("Acknowledge Selected Event");
            mAckSelectedButton.setEnabled(false);
            mAckSelectedButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (mMessageListModel.isEmpty())
                    {
                        JOptionPane.showMessageDialog(null,
                            "There are no events to acknowledge.", "No Events",
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    int index = mMessageList.getSelectedIndex();
                    if (index == -1)
                    {
                        JOptionPane.showMessageDialog(null,
                            "Please select an event first.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ackMessage(index);

                    // refresh message with new acknowledgement status
                    showMessage(index);
                }
            }
            );
            mMessageOpsPane.add(mAckSelectedButton, c1);

            c1.gridx = 0;
            c1.gridy = 3;
            c1.gridwidth = 1;
            c1.anchor = GridBagConstraints.WEST;
            mMessageOpsPane.add(new JLabel("Events Received:"), c1);

            c1.gridx = 1;
            c1.fill = GridBagConstraints.HORIZONTAL;
            mMessageCountField = new JTextField();
            mMessageCountField.setHorizontalAlignment(JTextField.RIGHT);
            updateMessageCountField();
            mMessageOpsPane.add(mMessageCountField, c1);

			// add option to enable/disable file write to disk
			c1.gridx = 2;
			c1.gridy = 3;
			mWriteXMLToDisk = new JCheckBox("Write Event to disk");
			mWriteXMLToDisk.setSelected(sDlg.isWriteXMLToDisk());
			mWriteXMLToDisk.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					sDlg.setWriteXMLToDisk(mWriteXMLToDisk.isSelected());
					sDlg.writeInfoToIni();
				}
			}
			);
			mMessageOpsPane.add(mWriteXMLToDisk, c1);

            // add Message Data pane
            c1.gridx = 2;
            c1.gridy = 0;
            c1.insets = new Insets(0, 8, 0, 4);
            mMessageOpsPane.add(new JLabel("Event Data:"), c1);

            c1.gridy = 1;
            c1.gridheight = 2;
            c1.fill = GridBagConstraints.VERTICAL;
            mMessageDataArea = new JTextArea(7, 40);
            JScrollPane dataScrollPane = new JScrollPane(mMessageDataArea,
                                                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            mMessageDataArea.setEditable(false);
            mMessageOpsPane.add(dataScrollPane, c1);

            TitledBorder titledBorder = BorderFactory.createTitledBorder(
                                            BorderFactory.createLoweredBevelBorder(),
                                            "Event Operations");
            mMessageOpsPane.setBorder(titledBorder);
        }
        return mMessageOpsPane;
    }

    /**
     * This method initializes and returns the Introspection Operations pane.
     *
     * @return the initialized pane
     */
    private JPanel getIntrospectOpsPane()
    {
        if (mIntrospectOpsPane == null)
        {
            mIntrospectOpsPane = new JPanel();
            mIntrospectOpsPane.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();

            c1.gridx = 0;
            c1.gridy = 0;
            c1.gridheight = 4;
            c1.anchor = GridBagConstraints.NORTH;
            c1.insets = new Insets(0, 20, 0, 20);
            c1.fill = GridBagConstraints.HORIZONTAL;
            mEventListButton = new JButton("Event List");
            mEventListButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    String output = getEventList();
                    showModalDialog("Event List", output);
                }
            }
            );
            mIntrospectOpsPane.add(mEventListButton, c1);

            c1.gridx = 1;
            c1.gridwidth = 2;
            c1.gridheight = 1;
            mEventListEnvButton = new JButton("Event List");
            mEventListEnvButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    // validate input
                    JTextField envField = mEventListEnvField;
                    if (envField == null
                        || envField.getText().equals(""))
                    {
                        JOptionPane.showMessageDialog(null,
                            "Please enter an environment first.", "Insufficient Input",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // display results
                    String output = getEventList(envField.getText());
                    showModalDialog("Event List for " + envField.getText(), output);
                }
            }
            );

            mIntrospectOpsPane.add(mEventListEnvButton, c1);

            c1.gridy = 1;
            c1.gridwidth = 1;
            c1.gridheight = 3;
            c1.fill = GridBagConstraints.NONE;
            c1.insets = new Insets(0, 20, 0, 0);
            mIntrospectOpsPane.add(new JLabel("Environment:"), c1);

            c1.gridx = 2;
            c1.insets = new Insets(0, 0, 0, 20);
            mEventListEnvField = new JTextField(8);
            mEventListEnvField.setText(sDlg.getEnv());
            mIntrospectOpsPane.add(mEventListEnvField, c1);

            c1.gridx = 3;
            c1.gridy = 0;
            c1.gridheight = 1;
            c1.gridwidth = 2;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.insets = new Insets(0, 20, 0, 20);
            mEventTemplateButton = new JButton("Event Template");
            mEventTemplateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    // validate input
                    JList catList = mEventTemplCatList;
                    JTextField typField = mEventTemplTypeField;
                    JTextField envField = mEventTemplEnvField;
                    if (typField.getText() == null || typField.getText().equals("")
                        || envField.getText() == null || envField.getText().equals(""))
                    {
                        JOptionPane.showMessageDialog(null,
                            "Please select a category and enter a type and environment first.",
                            "Insufficient Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (catList.isSelectionEmpty())
                    {
                        JOptionPane.showMessageDialog(null,
                            "Please select a category first.",
                            "Insufficient Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // display results
                    String category = (String)catList.getSelectedValue();
                    String template = getEventTemplate(category,
                                      typField.getText(), envField.getText());
                    showModalDialog("Event Template", template);
                }
            }
            );
            mIntrospectOpsPane.add(mEventTemplateButton, c1);

            c1.gridy = 1;
            c1.gridwidth = 1;
            c1.anchor = GridBagConstraints.WEST;
            c1.insets = new Insets(0, 20, 0, 0);
            mIntrospectOpsPane.add(new JLabel("Category:"), c1);

            c1.gridx = 4;
            c1.insets = new Insets(0, 0, 0, 20);
            String[] cats = new String[] {IConnectorService.CATEGORY_REALTIME,
                                          IConnectorService.CATEGORY_XAPI,
                                          IConnectorService.CATEGORY_ZFILE
            };
            mEventTemplCatList = new JList(cats);
            mEventTemplCatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            mEventTemplCatList.setVisibleRowCount(2);
            JScrollPane listScrollPane = new JScrollPane(mEventTemplCatList,
                                                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            mIntrospectOpsPane.add(listScrollPane, c1);

            c1.gridx = 3;
            c1.gridy = 2;
            c1.insets = new Insets(0, 20, 0, 0);
            mIntrospectOpsPane.add(new JLabel("Type:"), c1);

            c1.gridx = 4;
            c1.insets = new Insets(0, 0, 0, 20);
            mEventTemplTypeField = new JTextField(8);
            mIntrospectOpsPane.add(mEventTemplTypeField, c1);

            c1.gridx = 3;
            c1.gridy = 3;
            c1.insets = new Insets(0, 20, 0, 0);
            mIntrospectOpsPane.add(new JLabel("Environment:"), c1);

            c1.gridx = 4;
            c1.insets = new Insets(0, 0, 0, 20);
            mEventTemplEnvField = new JTextField(8);
            mEventTemplEnvField.setText(sDlg.getEnv());
            mIntrospectOpsPane.add(mEventTemplEnvField, c1);

            c1.gridx = 5;
            c1.gridy = 0;
            c1.gridheight = 4;
            c1.anchor = GridBagConstraints.NORTH;
            c1.fill = GridBagConstraints.HORIZONTAL;
            c1.insets = new Insets(0, 20, 0, 20);
            mSubscriptButton = new JButton("Subscriptions");
            mSubscriptButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    String output = getSubscriptions();
                    showModalDialog("Subscriptions", output);
                }
            }
            );
            mIntrospectOpsPane.add(mSubscriptButton, c1);

            TitledBorder titledBorder =
                BorderFactory.createTitledBorder(
                    BorderFactory.createLoweredBevelBorder(),
                    "Introspection Operations");
            mIntrospectOpsPane.setBorder(titledBorder);
        }
        return mIntrospectOpsPane;
    }

    /**
     * This method initializes and returns the Error Message pane.
     *
     * @return the initialized pane
     */
    private JPanel getErrorMessagePane()
    {
        if (mErrorMessagePane == null)
        {
            mErrorMessagePane = new JPanel();
            mErrorMessagePane.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();

            c1.gridx = 0;
            c1.gridy = 0;
            c1.gridwidth = 2;
            c1.anchor = GridBagConstraints.WEST;
            mErrorMessagePane.add(new JLabel("Error Messages:"), c1);

            c1.gridx = 2;
            c1.gridwidth = 1;
            c1.anchor = GridBagConstraints.EAST;
            mClearErrorButton = new JButton("Clear Error Messages");
            mClearErrorButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    mErrorMessageArea.setText(null);
                }
            }
            );
            mErrorMessagePane.add(mClearErrorButton, c1);

            c1.gridx = 0;
            c1.gridy = 1;
            c1.gridwidth = 3;
            c1.anchor = GridBagConstraints.CENTER;
            mErrorMessageArea = new JTextArea(6, 68);
            JScrollPane scrollPane = new JScrollPane(mErrorMessageArea,
                                                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            mErrorMessageArea.setEditable(false);
            mErrorMessagePane.add(scrollPane, c1);
        }
        return mErrorMessagePane;
    }

    /**
     * Creates a model appropriate to the user's selection.
     */
    private void createModel()
    {
        if (this.mAsyncButton.isSelected())
        {
            mModel = new EventAppAsyncModel(mSessionID, mAutoAckButton.isSelected());
        }
        else
        {
            mModel = new EventAppSyncModel(mSessionID, mAutoAckButton.isSelected());
        }

        mModel.addObserver(this);
        mModel.open();
    }

    /**
     * Creates a model dialog window (one that must be closed before any other
     * window can be accessed), displaying the given text.
     *
     * @param text the text to display
     */
    private void showModalDialog(String title, String text)
    {
        // create dialog
        JDialog dialog = new JDialog(this, title, true);
        JTextArea area = new JTextArea(text, 10, 80);
        area.setLineWrap(true);
        area.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dialog.getContentPane().add(scrollPane);
        dialog.pack();

        // center dialog on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = dialog.getSize();
        dialog.setLocation(Math.max(0,(screenSize.width -windowSize.width)/2),
                           Math.max(0,(screenSize.height-windowSize.height)/2));

        dialog.setVisible(true);
    }

    /**
     * Adds a message to the message list control and the message map.
     *
     * @param message the message to add
     */
    private void addMessage(EventObject message)
    {         
        String directory = null;
        
        // dump message to xml file if system property is set
        if (System.getProperty(OUTPUT_DIRECTORY) == null)
        {
            try
            {
                BufferedReader propReader =
                    IniFileLoader.getAbsoluteFileName_NoConfig("jdelog.properties");
                Properties props = JdeLog.loadPropertiesFromfile(propReader);
                Hashtable subHash = (Hashtable)props.get("[E1LOG]");
                String filePath = (String)subHash.get("FILE");
                directory = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
           }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            directory = System.getProperty(OUTPUT_DIRECTORY);
        }
        
        if (directory != null && sDlg.isWriteXMLToDisk() == true)
        {
            String xmlFilename = directory + File.separatorChar + message.getType()
                                 + "_" + message.getSequenceNumber() + ".xml";
            try
            {
                java.io.FileOutputStream fos = new java.io.FileOutputStream(xmlFilename);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
                osw.write(message.getXMLPayload());
                osw.close();
            }
            catch (IOException e)
            {
                errorGenerated(e.toString());
            }
        }

        // update number of messages received
        mMessageCount++;
        updateMessageCountField();

        // check for too many messages in the list, and remove one first
        if (mMessageMap.size() >= MAX_MESSAGES)
        {
            Long key = (Long)mMessageListModel.firstElement();
            mMessageListModel.removeElement(key);
            mMessageMap.remove(key);
        }

        // add new message to the list and map
        try
        {
            Object messageKey = new Long(message.getSequenceNumber());
            mMessageMap.put(messageKey, message);
            mMessageListModel.addElement(messageKey);
        }
        catch (UnsupportedOperationException e)
        {
            mErrorMessageArea.setText(e.getMessage());
            sessionClosed();
        }
    }

    /**
     * Shows the message with the list index in the Message Data text area.
     *
     * @param index the list index for the message
     */
    private void showMessage(int index)
    {
        EventObject message = getMessageAtListIndex(index);
        if (message != null)
        {
            mMessageDataArea.setText(null);
            mMessageDataArea.append(message.toString());
        }
    }

    /**
     * Acknowledges the message at the index in the message list.
     *
     * @param index the selected message in the list
     */
    private void ackMessage(int index)
    {
        EventObject message = getMessageAtListIndex(index);
        try
        {
            message.acknowledge();
        }
        catch (Exception e)
        {
            mErrorMessageArea.append("Could not acknowledge event: " + e);
        }
    }

    /**
     * Returns the EventObject at the given message list index.
     *
     * @param index the message list index
     * @return the EventObject
     */
    private EventObject getMessageAtListIndex(int index)
    {
        Long key = (Long)mMessageListModel.getElementAt(index);
        return (EventObject)mMessageMap.get(key);
    }

    /**
     * Updates the message count field with the latest message count.
     */
    private void updateMessageCountField()
    {
        mMessageCountField.setText("" + mMessageCount);
    }

    /**
     * Retrieves the list of all active and inactive event types
     * from all EnterpriseOne environments.
     *
     * @return a String representation of the event types
     */
    private String getEventList()
    {
        // retrieve event list
        List list = null;
        try
        {
            list = new LinkedList(EventService.getEventList(mSessionID));
        }
        catch (Exception e)
        {
            mErrorMessageArea.append(e.toString() + "\n");
            return null;
        }

        // check for results
        StringBuffer output = new StringBuffer();
        if (list.isEmpty())
        {
            output.append("No events returned in call to getEventList()");
            return output.toString();
        }

        // assemble results
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            output.append((String)iter.next() + " ");
        }
        return output.toString();
    }

    /**
     * Retrieves the list of event types, both active and inactive,
     * that are valid for a given environment.
     *
     * @param environment the EnterpriseOne environment corresponding to the event
     * @return a String representation of the list of
     *         com.peoplesoft.pt.e1.common.events.connectorsvc.EventTypeDefinition objects
     */
    private String getEventList(String environment)
    {
        // retrieve event list
        List list = null;
        try
        {
            list = new LinkedList(EventService.getEventList(mSessionID, environment));
        }
        catch (Exception e)
        {
            mErrorMessageArea.append(e.toString() + "\n");
            return null;
        }

        // check for results
        StringBuffer output = new StringBuffer();
        if (list.isEmpty())
        {
            output.append("No events returned in call to getEventList()");
            return output.toString();
        }

        // assemble output
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            EventTypeDefinition def = (EventTypeDefinition)iter.next();
            output.append(def.toString() + "\n");
        }

        return output.toString();
    }

    /**
     * Retrieves the event template for the given event category, event type,
     * and EnterpriseOne environment.
     *
     * @param category the event category (one of the categories defined as static members of the
     *                 com.peoplesoft.pt.e1.common.events.connectorsvc.IConnectorService
     *                 interface)
     * @param type the event type (such as "RTSOOUT")
     * @param environment the EnterpriseOne environment corresponding to the event
     * @return the event template in XML format
     */
    private String getEventTemplate(String category, String type, String environment)
    {
        try
        {
            return EventService.getEventTemplate(mSessionID, category, type, environment);
        }
        catch (Exception e)
        {
            mErrorMessageArea.append(e.toString() + "\n");
            return null;
        }
    }

    /**
     * Retrieves all the subscriptions for the user associated with this
     * EventSession instance.
     *
     * @return a String representation of the list of
     *         com.peoplesoft.pt.e1.common.events.connectorsvc.Subscription objects
     */
    private String getSubscriptions()
    {
        List list = null;
        try
        {
            list = new LinkedList(EventService.getSubscriptions(mSessionID));
        }
        catch (Exception e)
        {
            mErrorMessageArea.append(e.toString() + "\n");
            return null;
        }

        // check for results
        StringBuffer output = new StringBuffer();
        if (list.isEmpty())
        {
            output.append("No events returned in call to getEventList()");
            return output.toString();
        }

        // assemble output
        output.append("Subscriptions:\n");
        Iterator iter = list.iterator();
        while (iter.hasNext())
        {
            Subscription sub = (Subscription)iter.next();
            output.append(sub.getName() + " (" + sub.getDescription() + ")  ");
            Set envSet = sub.getEnvironments();
            if (envSet != null && !envSet.isEmpty())
            {
                output.append("Env: ");
                Iterator envIter = envSet.iterator();
                while (envIter.hasNext())
                {
                    String env = (String)envIter.next();
                    output.append(env + " ");
                }
                output.append(" ");
            }
            Set typeSet = sub.getSubscribedToEventTypes();
            if (typeSet != null && !typeSet.isEmpty())
            {
                output.append("SubscribedToEventTypes: ");
                Iterator typeIter = typeSet.iterator();
                while (typeIter.hasNext())
                {
                    SubscribedToEventType type = (SubscribedToEventType)typeIter.next();
                    output.append(type.toString() + " ");
                }
            }
            output.append("\n");
        }

        return output.toString();
    }
}