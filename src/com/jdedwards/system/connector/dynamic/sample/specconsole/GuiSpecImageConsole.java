package com.jdedwards.system.connector.dynamic.sample.specconsole;

import com.jdedwards.system.connector.dynamic.spec.SpecFailureException;
import com.jdedwards.system.connector.dynamic.spec.dictionary.SpecDictionary;
import com.jdedwards.system.connector.dynamic.spec.dictionary.Context;
import com.jdedwards.system.connector.dynamic.spec.dictionary.ContextNotFoundException;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNMethod;
import com.jdedwards.system.connector.dynamic.util.SpecImageGenerator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.specconsole.TestGuiSpecImageConsole 
 */
public class GuiSpecImageConsole {

    // Global GUI components
    private SpecTree imageSpecTree = null;
    private SpecTree repositorySpecTree = null;
    private JTextArea xmlArea = new JTextArea();
    private JScrollPane repositorySpecPane = new JScrollPane();
    private JScrollPane imageSpecPane = new JScrollPane();
    private JFrame consoleFrame = null;
    private JSplitPane consolePane = null;

    // Spec Source
    private SpecDictionary specDictionary = null;
    private BSFNSpecSource specSource = null;

    // commands
    private SpecImageConsoleCommand command = SpecImageConsoleCommand.getInstance();


    // Dimensions
    private int consoleWidth = 800;
    private int consoleHeight = 600;
    private int specPaneWidth = (int) (consoleWidth * 0.5);
    private int specPaneHeight = consoleHeight;
    private int xmlPaneWith = specPaneWidth;
    private int xmlPaneHeight = (int) (specPaneHeight * 0.4);
    private int specImagePaneWidth = consoleWidth - specPaneWidth;
    private int specImagePaneHeight = consoleHeight - xmlPaneHeight;

    // login information
    private String userName = null;
    private String password = null;
    private String env = null;
    private String role = null;

    public GuiSpecImageConsole() {
    }

    public void initGUI() {
        consoleFrame = new JFrame();
        consoleFrame.setTitle("Spec Console");
        consoleFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent) {
                consoleFrame.dispose();
                System.exit(0);
            }
        });
        consoleFrame.setJMenuBar(createMenuBar());
        Container container = consoleFrame.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(createConsolePane(), BorderLayout.CENTER);
        container.add(createConsoleToolBar(), BorderLayout.NORTH);
        consoleFrame.pack();
        consoleFrame.setSize(consoleWidth, consoleHeight);
        consoleFrame.setVisible(true);
    }

    private JSplitPane createConsolePane() {
        consolePane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        consolePane.setLeftComponent(createLeftPanel());
        consolePane.setRightComponent(createRightPane());
        return consolePane;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(createSpecRepositoryPane(), BorderLayout.CENTER);
        leftPanel.add(createSpecTreeToolBar(), BorderLayout.EAST);
        return leftPanel;
    }

    private JPanel createSpecRepositoryPane() {
        //Create a repositorySpecTree that allows one selection at a time.
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        //Create the scroll pane and add the repositorySpecTree to it.
//        repositorySpecTree = new SpecTree(specDictionary,true);
        repositorySpecPane = new JScrollPane(new SpecTree("Spec Resource"));
        JToolBar toolBar = createSpecTreeToolBar();
        p.setMinimumSize(new Dimension(specPaneWidth, specPaneHeight));
        p.add(createSpecRepositoryButtons(), BorderLayout.NORTH);
        p.add(repositorySpecPane, BorderLayout.CENTER);
        return p;
    }

    private JPanel createSpecRepositoryButtons() {
        JPanel p = new JPanel();
        JButton openOWButton = new JButton("Open Oneworld Spec");
        openOWButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doOpenOneworldSpec(consoleFrame);
                if (command.getSpecDictionary()!=null || command.getSpecSource()!=null){
                    specDictionary = command.getSpecDictionary();
                    specSource = command.getSpecSource();
                    repositorySpecTree = new SpecTree(command.getSpecDictionary(),command.getSpecSource());
                    resetSpecTree(repositorySpecPane, repositorySpecTree);
                }
            }
        });

        JButton openSpecImageButton = new JButton("Open Image Spec");
        openSpecImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doOpenImageSpec(consoleFrame);
                if (command.getSpecDictionary()!=null || command.getSpecSource()!=null){
                    specDictionary = command.getSpecDictionary();
                    specSource = command.getSpecSource();
                    repositorySpecTree = new SpecTree(command.getSpecDictionary(),command.getSpecSource());
                    resetSpecTree(repositorySpecPane, repositorySpecTree);
                }
            }
        });
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    Context c=command.doSearch(specDictionary,consoleFrame);
                    repositorySpecTree.showContext(c);
                } catch (SpecFailureException error) {
                    JOptionPane.showMessageDialog(consoleFrame,
                            "Fail to SpecDictionary failure"+error.getMessage(),
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ContextNotFoundException error){
                    JOptionPane.showMessageDialog(consoleFrame,
                            "Context not found.",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        p.add(openOWButton);
        p.add(openSpecImageButton);
        p.add(searchButton);
        p.setBorder(BorderFactory.createTitledBorder
                (BorderFactory.createLineBorder(Color.gray, 1), "Spec Repository"));
        return p;
    }

    private JToolBar createConsoleToolBar() {
        JToolBar consoleTooBar = new JToolBar();
        return consoleTooBar;
    }

    private JPanel createRightPane() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createSpecImagePane(), BorderLayout.CENTER);
       // rightPanel.add(createSpecImageButtons(), BorderLayout.NORTH);
        return rightPanel;
    }

    /*private JPanel createSpecImageButtons() {
        JPanel p = new JPanel();
        JButton openSpecButton = new JButton("Open Spec Image");
        openSpecButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (command.getSpecDictionary()!=null || command.getSpecSource()!=null){
                    command.doOpenImageSpec(consoleFrame);
                    imageSpecTree = new SpecTree(command.getSpecDictionary(),command.getSpecSource());
                    resetSpecTree(imageSpecPane, imageSpecTree);
                }
            }

        });

        JButton newButton = new JButton("New Spec Image");
        p.add(newButton);
        p.add(openSpecButton);
        p.setBorder(BorderFactory.createTitledBorder
                (BorderFactory.createLineBorder(Color.gray, 1), "Spec Image"));
        return p;
    }*/

    private JSplitPane createSpecImagePane() {
        JSplitPane specImagePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        specImagePane.setTopComponent(new JScrollPane(createSpecImageTreePane()));
        specImagePane.setBottomComponent(createXMLPane());
        specImagePane.setOneTouchExpandable(true);
        return specImagePane;
    }

    private JScrollPane createXMLPane() {
        xmlArea = new JTextArea();
        return new JScrollPane(xmlArea);
    }

    private JScrollPane createSpecImageTreePane() {
        imageSpecTree = new SpecTree("Spec Image");
        imageSpecPane = new JScrollPane(imageSpecTree);
        imageSpecPane.setMinimumSize(new Dimension(specImagePaneWidth, specImagePaneHeight));
        return imageSpecPane;
    }

    private JToolBar createSpecTreeToolBar() {
        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);

        JButton addButton = new JButton("Add-->");
        addButton.setToolTipText("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode)repositorySpecTree.getLastSelectedPathComponent();
                if (imageSpecTree==null){
                    imageSpecTree = new SpecTree("Spec Image");
                }
                DefaultMutableTreeNode node= (DefaultMutableTreeNode) repositorySpecTree.getLastSelectedPathComponent();
                Object obj = node.getUserObject();
                if (obj instanceof Context) {
                    imageSpecTree.addContextNode(node);
                } else if (obj instanceof BSFNMethod){
                    imageSpecTree.addBSFNNode(node);
                }
            }
        });
        toolBar.add(addButton);

        //second button
        JButton genContentButton = new JButton("Gen Content");
        genContentButton.setHorizontalAlignment(SwingConstants.CENTER );
        genContentButton.setToolTipText("Generate Spec Content");
        genContentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                xmlArea.setText(command.doGenerate(repositorySpecTree,imageSpecTree, SpecImageGenerator.ImageType.SSI));
            }
        });
        toolBar.add(genContentButton);

        JButton genDictButton = new JButton("Gen Dict");
        genDictButton.setHorizontalAlignment(SwingConstants.CENTER );
        genDictButton.setToolTipText("Generate Spec Dictionary");
        genDictButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                xmlArea.setText(command.doGenerate(repositorySpecTree,imageSpecTree, SpecImageGenerator.ImageType.SDI));
            }
        });
        toolBar.add(genDictButton);

        JButton genButton = new JButton("Gen Spec Image");
        genButton.setHorizontalAlignment(SwingConstants.CENTER );
        genButton.setToolTipText("Generate both Spec content and dictionary");
        genButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String output = "";
                if (specDictionary != null) {
                    output = command.doGenerate(repositorySpecTree,imageSpecTree, SpecImageGenerator.ImageType.ALL);
                } else {
                    output = command.doGenerate(repositorySpecTree,imageSpecTree, SpecImageGenerator.ImageType.SSI);
                }
                xmlArea.removeAll();
                xmlArea.setText(output);
            }
        });
        toolBar.add(genButton);

        JButton removeButton = new JButton("<--Remove");
        removeButton.setHorizontalAlignment(SwingConstants.CENTER );
        removeButton.setToolTipText("Remove Context");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                imageSpecTree.removeCurrentNode();
            }
        });
        toolBar.add(removeButton);
        return toolBar;
    }

    private JMenuBar createMenuBar() {
        // main menu bar
        JMenuBar menuBar = new JMenuBar();

        // spec menu items
        JMenu specMenu = new JMenu("Spec Repository");

        // Open spec dictionary menu
        JMenuItem openOwSpecMenuItem = new JMenuItem("Open Oneworld Spec");
        openOwSpecMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doOpenOneworldSpec(consoleFrame);
                if (command.getSpecDictionary()!=null || command.getSpecSource()!=null){
                    specDictionary = command.getSpecDictionary();
                    specSource = command.getSpecSource();
                    repositorySpecTree = new SpecTree(command.getSpecDictionary(),command.getSpecSource());
                    resetSpecTree(repositorySpecPane, repositorySpecTree);
                }
            }
        });

        JMenuItem openImageSpecMenuItem = new JMenuItem("Open Image Spec");
        openImageSpecMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doOpenImageSpec(consoleFrame);
                if (command.getSpecDictionary()!=null || command.getSpecSource()!=null){
                    specDictionary = command.getSpecDictionary();
                    specSource = command.getSpecSource();
                    repositorySpecTree = new SpecTree(command.getSpecDictionary(),command.getSpecSource());
                    resetSpecTree(repositorySpecPane, repositorySpecTree);
                }
            }
        });


        JMenuItem closeMenuItem = new JMenuItem("Close Spec Source");
        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doCloseSpecSource();
            }
        });


        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doExit(consoleFrame);
                System.exit(0);
            }
        });


        specMenu.add(openOwSpecMenuItem);
        specMenu.add(openImageSpecMenuItem);
        specMenu.addSeparator();
        specMenu.add(closeMenuItem);
        specMenu.addSeparator();
        specMenu.add(exitMenuItem);


        // spec image menu items
        JMenu specImageMenu = new JMenu("Spec Image");
        JMenu genSpecImageMenu = new JMenu("Generate Spec Image");
        JMenuItem genDictMenuItem = new JMenuItem("Generate Dictionary only");
        genDictMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                xmlArea.setText(command.doGenerate(repositorySpecTree,imageSpecTree, SpecImageGenerator.ImageType.SDI));
            }
        });

        JMenuItem genContentMenuItem = new JMenuItem("Generate Content only");
        genContentMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                xmlArea.setText(command.doGenerate(repositorySpecTree,imageSpecTree, SpecImageGenerator.ImageType.SSI));
            }
        });

        JMenuItem genAllMenuItem = new JMenuItem("Generate both");
        genAllMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                xmlArea.setText(command.doGenerate(repositorySpecTree,imageSpecTree, SpecImageGenerator.ImageType.ALL));
            }
        });

        JMenuItem saveImageMenuItem = new JMenuItem("Save");
        saveImageMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doSaveImage(consoleFrame, xmlArea.getText());
            }
        });
        JMenuItem validateImageMenuItem = new JMenuItem("Validate...");
        validateImageMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                command.doValidate();
            }
        });
        specImageMenu.add(genSpecImageMenu);
        genSpecImageMenu.add(genContentMenuItem);
        genSpecImageMenu.add(genDictMenuItem);
        genSpecImageMenu.add(genAllMenuItem);
        specImageMenu.add(saveImageMenuItem);
        JMenuItem viewLogMenuItem = new JMenuItem("View Log");
        viewLogMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doViewErrorLog();
            }
        });
        specImageMenu.add(viewLogMenuItem);

        // help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                command.doAbout();
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(specMenu);
        menuBar.add(specImageMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

   private void resetSpecTree(JScrollPane panel, SpecTree specTree) {
            panel.getViewport().removeAll();
            panel.setViewportView(specTree);
            panel.getViewport().invalidate();
            panel.getViewport().repaint();
//            resetSpecImageTree(imageSpecTree);
            xmlArea.setText("");
    }




    public static void main(String[] args) {
        GuiSpecImageConsole console = new GuiSpecImageConsole();
        console.initGUI();
    }

}
