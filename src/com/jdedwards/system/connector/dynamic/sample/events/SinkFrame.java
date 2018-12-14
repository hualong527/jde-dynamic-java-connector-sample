package com.jdedwards.system.connector.dynamic.sample.events;
/*
 * SinkFrame.java
 *
 * Created on August 25, 2000, 9:16 AM
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.jdedwards.system.connector.dynamic.Connector;
import com.jdedwards.system.connector.dynamic.events.EventObject;
import com.jdedwards.system.connector.dynamic.events.EventSource;

/**
 *
 * @author  KK949259
 * @version
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.events.TestSinkFrame
 */
public class SinkFrame extends javax.swing.JFrame
{
    public static final String OUTPUT_DIRECTORY = "outputDir";
    private static int m_objCount = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new SinkFrame().show();
    }
    private javax.swing.JButton BrowserButton;
    private javax.swing.JButton ClearButton;
    private javax.swing.JComboBox EnvironCombo;
    private javax.swing.JTextField EventCountSlider;
    private javax.swing.JTextField EventCountTextField;
    private javax.swing.JTextArea EventDataPane;
    private javax.swing.JButton EventsButton;
    private javax.swing.JButton EventTemplateButton;
    private javax.swing.JComboBox EventTypeCombo;
    private javax.swing.JCheckBox GroupCheckBox;
    private javax.swing.JTextField GroupField;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private int m_Access = 0;
    private Connector m_connector = null;
    private String m_env;
    private int m_EventCount = 0;
    private String m_group;
    private Listener m_listener = null;
    private EventSource m_theSource = null;
    private String m_user;
    private javax.swing.JButton newEventButton;
    private javax.swing.JButton NewListener;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JCheckBox PersistCheckBox;
    private javax.swing.JButton RegisterButton;
    private javax.swing.JTextField ServerField;
    private javax.swing.JTextField SessionIdTextField;
    private javax.swing.JCheckBox SuspendCheckBox;
    private java.awt.TextField textField1;
    private java.awt.TextField textField2;
    private javax.swing.JTextField TimestampTextField;
    private javax.swing.JButton UnRegisterButton;
    private javax.swing.JTextField UserField;

    /** Creates new form SinkFrame */
    public SinkFrame()
    {
        initComponents();
        pack();
        resize(560, 740);
        populateEventTypes();
        ++m_objCount;
    }

    public SinkFrame(Connector con, int access, EventSource source, String user, String env)
    {
        this();
        m_connector = con;
        m_Access = access;
        m_theSource = source;
        m_user = user;
        m_env = env;
        SessionIdTextField.setText(Integer.toString(m_Access));
        UserField.setText(m_user);
    }
    
    public SinkFrame(
        Connector con,
        int access,
        EventSource source,
        String user,
        String env,
        String group)
    {
        this();
        m_connector = con;
        m_Access = access;
        m_theSource = source;
        m_user = user;
        m_env = env;
        m_group = group;
        SessionIdTextField.setText(Integer.toString(m_Access));
        UserField.setText(m_user);
        UserField.setText(m_group);
    }

    private Listener createListener()
    {
        int count = getEvenCountSliderValue();

        if (count > 0 && SuspendCheckBox.isSelected())
        {
            m_listener = new CountedPersistListenerImpl(count, this);
        }
        else if (count > 0)
        {
            m_listener = new CountedListenerImpl(count, this);
        }
        else if (PersistCheckBox.isSelected() && GroupCheckBox.isSelected())
        {
            m_listener = new PersistGroupListenerImpl(GroupField.getText(), this);
        }
        else if (PersistCheckBox.isSelected())
        {
            m_listener = new PersistListenerImpl(this);
        }
        else if (GroupCheckBox.isSelected())
        {
            m_listener = new GroupListenerImpl(GroupField.getText(), this);
        }
        else
        {
            m_listener = new Listener(this);
        }

        return m_listener;
    }

    private void EnvironComboActionPerformed(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_EnvironComboActionPerformed
        // Add your handling code here:
    } //GEN-LAST:event_EnvironFieldActionPerformed

    private void EnvironFieldActionPerformed(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_EnvironFieldActionPerformed
        // Add your handling code here:
    } //GEN-LAST:event_EnvironFieldActionPerformed

    private void EventCountTextFieldActionPerformed(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_EventCountTextFieldActionPerformed
        // Add your handling code here:
    } //GEN-LAST:event_EventCountTextFieldActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt)
    { //GEN-FIRST:event_exitForm
        --m_objCount;
        if (m_objCount <= 0)
        {
            System.exit(0);
        }
    } //GEN-LAST:event_exitForm
    public String getENV()
    {
        return (String)EnvironCombo.getSelectedItem();
    }
    private int getEvenCountSliderValue()
    {
        int count = 0;
        String text = EventCountSlider.getText();
        if (text.length() > 0)
        {
            try
            {
                count = Integer.parseInt(text);
            }
            catch (Exception e)
            {
                count = 0;
            }
        }
        return count;
    }
    public int getEventCount()
    {
        return getEvenCountSliderValue();
    }

    public String getEventType()
    {
        return (String)EventTypeCombo.getSelectedItem();
    }

    private void GroupCheckBoxActionPerformed(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_GroupCheckBoxActionPerformed
        // Add your handling code here:
    } //GEN-LAST:event_GroupCheckBoxActionPerformed
    //public boolean getPause() { return SuspendCheckBox.getSel
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents()
    { //GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        EventCountTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        EventDataPane = new javax.swing.JTextArea();
        SuspendCheckBox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        NewListener = new javax.swing.JButton();
        RegisterButton = new javax.swing.JButton();
        UserField = new javax.swing.JTextField();
        ServerField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        GroupField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        EnvironCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        PasswordField = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        EventTypeCombo = new javax.swing.JComboBox();
        ClearButton = new javax.swing.JButton();
        UnRegisterButton = new javax.swing.JButton();
        newEventButton = new javax.swing.JButton();
        TimestampTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        SessionIdTextField = new javax.swing.JTextField();
        textField1 = new java.awt.TextField();
        textField2 = new java.awt.TextField();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        EventCountSlider = new javax.swing.JTextField();
        PersistCheckBox = new javax.swing.JCheckBox();
        GroupCheckBox = new javax.swing.JCheckBox();
        EventsButton = new javax.swing.JButton();
        EventTemplateButton = new javax.swing.JButton();
        BrowserButton = new javax.swing.JButton();
        getContentPane().setLayout(null);
        setTitle("Event Listener");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
        });

        jLabel1.setText("Event Type");
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 11));

        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 20, 100, 30);

        EventCountTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                EventCountTextFieldActionPerformed(evt);
            }
        });

        getContentPane().add(EventCountTextField);
        EventCountTextField.setBounds(120, 230, 100, 30);

        jLabel2.setText("Events Received");
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 11));

        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 230, 100, 30);

        jScrollPane1.setViewportView(EventDataPane);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 270, 520, 370);

        SuspendCheckBox.setLabel("Suspend");
        SuspendCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onPause(evt);
            }
        });

        getContentPane().add(SuspendCheckBox);
        SuspendCheckBox.setBounds(20, 120, 70, 20);

        jLabel4.setText("Event Count");
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 11));

        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 60, 110, 20);

        NewListener.setText("New Listener...");
        NewListener.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onNewListener(evt);
            }
        });

        getContentPane().add(NewListener);
        NewListener.setBounds(410, 10, 140, 30);

        RegisterButton.setText("Register Listener");
        RegisterButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onRegister(evt);
            }
        });

        getContentPane().add(RegisterButton);
        RegisterButton.setBounds(410, 50, 140, 30);

        getContentPane().add(UserField);
        UserField.setBounds(50, 190, 90, 30);

        getContentPane().add(ServerField);
        ServerField.setBounds(50, 160, 120, 30);

        getContentPane().add(GroupField);
        GroupField.setBounds(215, 160, 90, 30);

        EnvironCombo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                EnvironComboActionPerformed(evt);
            }
        });

        getContentPane().add(EnvironCombo);
        EnvironCombo.setBounds(390, 190, 100, 30);

        jLabel3.setText("User");
        jLabel3.setFont(new java.awt.Font("Dialog", 1, 11));
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 190, 70, 30);

        jLabel5.setText("Password");
        jLabel5.setFont(new java.awt.Font("Dialog", 1, 11));
        getContentPane().add(jLabel5);
        jLabel5.setBounds(150, 190, 70, 30);

        jLabel9.setText("Server");
        jLabel9.setFont(new java.awt.Font("Dialog", 1, 11));
        getContentPane().add(jLabel9);
        jLabel9.setBounds(10, 160, 70, 30);

        jLabel10.setText("Group");
        jLabel10.setFont(new java.awt.Font("Dialog", 1, 11));
        getContentPane().add(jLabel10);
        jLabel10.setBounds(180, 160, 70, 30);

        getContentPane().add(PasswordField);
        PasswordField.setBounds(210, 190, 90, 30);

        jLabel6.setText("Environment");
        jLabel6.setFont(new java.awt.Font("Dialog", 1, 11));

        getContentPane().add(jLabel6);
        jLabel6.setBounds(310, 190, 80, 30);

        getContentPane().add(EventTypeCombo);
        EventTypeCombo.setBounds(80, 20, 190, 20);

        ClearButton.setText("Clear Events");
        ClearButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onClearEvents(evt);
            }
        });

        getContentPane().add(ClearButton);
        ClearButton.setBounds(420, 230, 120, 30);

        UnRegisterButton.setText("Unregister");
        UnRegisterButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onUnregister(evt);
            }
        });

        getContentPane().add(UnRegisterButton);
        UnRegisterButton.setBounds(410, 90, 140, 30);

        newEventButton.setText("New Event");
        newEventButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onNewEvent1(evt);
            }
        });

        getContentPane().add(newEventButton);
        newEventButton.setBounds(410, 130, 140, 30);

        getContentPane().add(TimestampTextField);
        TimestampTextField.setBounds(100, 650, 230, 30);

        jLabel7.setText("Timestamp");
        jLabel7.setFont(new java.awt.Font("Dialog", 1, 11));

        getContentPane().add(jLabel7);
        jLabel7.setBounds(20, 650, 70, 30);

        jLabel8.setText("Session ID");
        jLabel8.setFont(new java.awt.Font("Dialog", 1, 11));

        getContentPane().add(jLabel8);
        jLabel8.setBounds(340, 650, 70, 30);

        getContentPane().add(SessionIdTextField);
        SessionIdTextField.setBounds(410, 650, 130, 30);

        textField1.setBackground(java.awt.Color.white);
        textField1.setFont(new java.awt.Font("Dialog", 0, 11));
        textField1.setForeground(java.awt.Color.black);
        textField1.setText("textField1");

        getContentPane().add(textField1);
        textField1.setBounds(90, 60, 60, -30);

        textField2.setBackground(java.awt.Color.white);
        textField2.setFont(new java.awt.Font("Dialog", 0, 11));
        textField2.setForeground(java.awt.Color.black);
        textField2.setText("textField2");

        getContentPane().add(textField2);
        textField2.setBounds(90, 60, 50, -40);

        jTextField1.setText("jTextField1");

        getContentPane().add(jTextField1);
        jTextField1.setBounds(80, 60, 120, -40);

        jTextField2.setText("jTextField2");

        getContentPane().add(jTextField2);
        jTextField2.setBounds(80, 60, 90, -30);

        getContentPane().add(EventCountSlider);
        EventCountSlider.setBounds(90, 50, 140, 30);

        PersistCheckBox.setText("Persist");
        PersistCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                PersistCheckBoxActionPerformed(evt);
            }
        });

        getContentPane().add(PersistCheckBox);
        PersistCheckBox.setBounds(20, 100, 110, 20);

        GroupCheckBox.setText("Use Group");
        GroupCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                GroupCheckBoxActionPerformed(evt);
            }
        });

        getContentPane().add(GroupCheckBox);
        GroupCheckBox.setBounds(20, 80, 110, 20);

        EventsButton.setText("Get Events");
        EventsButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onGetEvents(evt);
            }
        });

        getContentPane().add(EventsButton);
        EventsButton.setBounds(230, 90, 150, 30);

        EventTemplateButton.setText("Get Event Template");
        EventTemplateButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onGetEventTemplate(evt);
            }
        });

        getContentPane().add(EventTemplateButton);
        EventTemplateButton.setBounds(230, 130, 150, 30);

        BrowserButton.setLabel("Open Browser");
        BrowserButton.setActionCommand("Open in Browser");
        BrowserButton.setText("Open in Browser");
        BrowserButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onBrowse(evt);
            }
        });

        getContentPane().add(BrowserButton);
        BrowserButton.setBounds(260, 230, 130, 30);

    } //GEN-END:initComponents

    private void onBrowse(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onBrowse

        try
        {
            String xmlStr = EventDataPane.getText();
            File tempFile = File.createTempFile("ttt", ".xml");
            FileWriter fw = new FileWriter(tempFile);
            fw.write(xmlStr);
            fw.close();
            String cmd = "cmd /c " + tempFile.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    } //GEN-LAST:event_onBrowse

    private void onClearEvents(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onClearEvents
        m_EventCount = 0;
        EventDataPane.setText("");
        EventCountTextField.setText("");
    } //GEN-LAST:event_onClearEvents

    private void onGetEvents(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onGetEvents
        if (null != m_theSource)
        {
            try
            {
                java.util.Enumeration eventList = m_theSource.getEventTypes(m_Access);
                EventTypeCombo.removeAllItems();
                while (eventList.hasMoreElements())
                {
                    String event = (String)eventList.nextElement();
                    EventTypeCombo.addItem(event);
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

        }
    } //GEN-LAST:event_onGetEvents

    private void onGetEventTemplate(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onGetEventTemplate
        if (null != m_theSource)
        {
            EventDataPane.setText("");
            String type = getEventType();
            try
            {
                String template = m_theSource.getEventTemplate(type, m_Access);
                EventDataPane.setText(template);
                sendTemplateToFile(type, template);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }

    } //GEN-LAST:event_onGetEventTemplate

    private void onNewEvent1(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onNewEvent1
        new SinkFrame(m_connector, m_Access, m_theSource, m_user, m_env).show();
    } //GEN-LAST:event_onNewEvent1

    private void onNewListener(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onNewListener
        new SinkFrame().show();
    } //GEN-LAST:event_onNewListener

    public synchronized void onOneWorldEvent(EventObject event)
    {
        EventCountTextField.setText(Integer.toString(++m_EventCount));
        if (null != event.getData())
        {
            EventDataPane.setText(event.getData());
            sendEventToFile(event);
        }
        TimestampTextField.setText(event.getTimestamp());
    }

    private void onPause(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onPause
        m_listener.setPause(SuspendCheckBox.isSelected());
    } //GEN-LAST:event_onPause

    private void onRegister(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onRegister
        String environ = getENV();

        if (null == m_connector)
        {
            if ((m_user = UserField.getText()).length() > 0
                && PasswordField.getText().length() > 0
                && (m_env = (environ)).length() > 0)
            {
                try
                {
                    m_connector = Connector.getInstance(); //new Connector();
                    m_Access =
                        m_connector.login(UserField.getText(), PasswordField.getText(), environ);
                    m_theSource = EventSource.getInstance();
                    m_theSource.addListener(createListener(), m_Access);
                    SessionIdTextField.setText(Integer.toString(m_Access));
                }
                catch (Exception e)
                {
                    m_connector = null;
                    m_theSource = null;
                    System.out.println(e.toString());
                }
            }
            else
            {
                JOptionPane p = new JOptionPane();
                JOptionPane.showMessageDialog(this, "Enter user, pwd, and environment");
            }
        }
        else
        {
            try
            {
                System.out.println("create new listener ");
                m_theSource.addListener(createListener(), m_Access);
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
    } //GEN-LAST:event_onRegister

    private void onUnregister(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_onUnregister
        if (null != m_connector && null != m_theSource)
        {
            try
            {
                if (PersistCheckBox.isSelected())
                {
                    m_listener = new PersistListenerImpl(this);
                }
                m_theSource.removeListener(m_listener, m_Access);
                System.out.println("unregister listener");
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
    } //GEN-LAST:event_onUnregister

    private void PersistCheckBoxActionPerformed(java.awt.event.ActionEvent evt)
    { //GEN-FIRST:event_PersistCheckBoxActionPerformed
        // Add your handling code here:
    } //GEN-LAST:event_PersistCheckBoxActionPerformed
    // End of variables declaration//GEN-END:variables

    private void populateEventTypes()
    {
        String eventStr = null;
        String envStr = null;
        try
        {
            BufferedReader ini =
                new BufferedReader(new FileReader(System.getProperty("EVENTS_FILE")));
            while (null != (eventStr = ini.readLine()))
            {
                EventTypeCombo.addItem(eventStr);
            }
            BufferedReader ini2 =
                new BufferedReader(new FileReader(System.getProperty("ENV_FILE")));
            while (null != (envStr = ini2.readLine()))
            {
                EnvironCombo.addItem(envStr);
            }
            UserField.setText("");
            PasswordField.setText("");
            ServerField.setText("NOT WORKING YET");
            GroupField.setText("TestGroup");
        }
        catch (Exception e)
        {
        }
    }

    /**
     * Send the XML contents of an event to a file.
     * 
     * @param event the event
     */
    private void sendEventToFile(EventObject event)
    {
        String directory = System.getProperty(OUTPUT_DIRECTORY);
        if (directory != null)
        {
            String filename = directory + File.separatorChar + event.getType()
                              + "_" + m_EventCount + ".xml";
            try
            {
                FileWriter writer = new FileWriter(filename);
                writer.write(event.getData());
                writer.flush();
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send an event template to a file.
     * 
     * @param type the event type
     * @param contents the contents to be placed in the file
     */
    private void sendTemplateToFile(String type, String contents)
    {
        String directory = System.getProperty(OUTPUT_DIRECTORY);
        if (directory != null)
        {
            String xmlFilename = directory + File.separatorChar + type + "_template.xml";
            try
            {
                FileWriter writer = new FileWriter(xmlFilename);
                writer.write(contents);
                writer.flush();
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}