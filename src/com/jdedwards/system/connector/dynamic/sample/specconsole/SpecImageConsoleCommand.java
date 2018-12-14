/******************************************************************************
 * class SpecImageConsoleCommand.java
 *
 * Modification Log
 *   Date             Name               Description
 *   Jan 3, 2002       William Lin        Created.
 *******************************************************************************
 *
 * Copyright (c) 2001
 * J.D. Edwards & Company
 *
 * This unpublished material is proprietary to J.D. Edwards & Company.
 * All rights reserved.  The methods and techniques described herein are
 * considered trade secrets and/or confidential.  Reproduction or
 * distribution, in whole or in part, is forbidden except by express
 * written permission of J.D. Edwards & Company.
 *
 ******************************************************************************/
package com.jdedwards.system.connector.dynamic.sample.specconsole;

import com.jdedwards.system.connector.dynamic.spec.SpecFailureException;
import com.jdedwards.system.connector.dynamic.spec.dictionary.Context;
import com.jdedwards.system.connector.dynamic.spec.dictionary.SpecDictionary;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNMethod;
import com.jdedwards.system.connector.dynamic.spec.source.BSFNSpecSource;
import com.jdedwards.system.connector.dynamic.spec.source.ImageBSFNSpecSource;
import com.jdedwards.system.connector.dynamic.util.SpecImageGenerator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.util.Enumeration;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.specconsole.TestSpecImageConsoleCommand 
 */
public class SpecImageConsoleCommand {
    // Actions
    protected static SpecImageConsoleCommand commandInstance = null;
    private SpecDictionary specDictionary;
    private BSFNSpecSource specSource;

    public SpecDictionary getSpecDictionary() {
        return specDictionary;
    }

    public BSFNSpecSource getSpecSource() {
        return specSource;
    }

    protected SpecImageConsoleCommand() {
    }

    public static SpecImageConsoleCommand getInstance() {
        if (commandInstance == null) {
            commandInstance = new SpecImageConsoleCommand();
        }
        return commandInstance;
    }

    public BSFNSpecSource doOpenImageSpecSource(JFrame consoleFrame) {
        BSFNSpecSource specSource = null;
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showDialog(consoleFrame, "Open Spec Source Image");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String fileName = selectedFile.getAbsolutePath();
                specSource = new ImageBSFNSpecSource(fileName);
            } catch (Exception e) {
                System.err.println("Error open the SpecImage:" + e.toString());
                e.printStackTrace();
            }
        }
        return specSource;
    }

    public void doOpenImageSpec(JFrame consoleFrame) {
        //open xml file
        OpenSpecImageDialog dialog = new OpenSpecImageDialog(consoleFrame);
        dialog.setLocationRelativeTo(consoleFrame);
        dialog.pack();
        dialog.setVisible(true);
        specDictionary = dialog.getSpecDictionary();
        specSource = dialog.getSpecSource();
    }

    public void doOpenOneworldSpec(JFrame aFrame) {

        OpenOneworldDialog dialog = new OpenOneworldDialog(aFrame);
        dialog.setLocationRelativeTo(aFrame);
        dialog.pack();
        dialog.setVisible(true);
        specDictionary = dialog.getSpecDictionary();
        specSource = dialog.getSpecSource();
    }

    public void doOpenPrevious(String commandStr) {
//        try {
//            int l = fileList.indexOf(commandStr);
//            if (l != -1) {
//                fileList.remove(l);
//                fileList.add(0, commandStr);
//            }
//            buildMenuPreviousFiles(fileList);
//            openFile = new File(commandStr);
//            createTab(openFile);
//        } catch (Exception exception2) {
//            JOptionPane.showMessageDialog(consoleFrame, exception2.getMessage() + " while opening file: " + commandStr);
//        }
    }

    public void doAbout() {
        (new AboutDialog()).show();
    }

    public void doViewErrorLog() {
    }

    public void doSaveImage(JFrame consoleFrame, String xmlStream) {
        JFileChooser fileChooser = new JFileChooser();
        int k = fileChooser.showSaveDialog(consoleFrame);
        if (k == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                writer.print(xmlStream);
                writer.close();
            } catch (IOException e) {
                System.out.println("error saving the XML file");
            }
        }
    }


    public void doValidate() {
    }

    public void doExit(JFrame consoleFrame) {
        consoleFrame.dispose();
    }

    public void doCloseSpecSource() {

    }

    public Context doSearch(SpecDictionary specDictionary, JFrame frame) throws SpecFailureException {
        SearchDialog dialog = new SearchDialog(frame);
        dialog.setLocationRelativeTo(frame);
        dialog.pack();
        dialog.setVisible(true);
        String contextName = dialog.getContextToFound();
        if (contextName != null && contextName.length() != 0) {
            return specDictionary.lookupContext(contextName.trim());
        } else {
            return null;
        }

    }

    public String doGenerate(SpecTree sourceTree, SpecTree imageTree, SpecImageGenerator.ImageType imageType) {
        try {
            SpecImageGenerator generator = new SpecImageGenerator(sourceTree.getSpecDictionary(), sourceTree.getSpecSource());
            DefaultMutableTreeNode rootNode = imageTree.getRootNode();
            Enumeration childNodes = rootNode.breadthFirstEnumeration();
            while (childNodes.hasMoreElements()) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childNodes.nextElement();
                Object obj = childNode.getUserObject();
                if (obj instanceof BSFNMethod) {
                    generator.importBSFNSpec(((BSFNMethod) obj).getName());
                } else if (obj instanceof Context) {
                    Context c = (Context) obj;
                    if (childNode.getChildCount() == 0 || (!c.hasSubcontexts())) {
                        generator.importContext(c.getNameInNamespace());
                    }
                }
            }
            Writer writer = new StringWriter();
            generator.generateImage(writer, imageType);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Generation error:" + e.toString();
        }
    }


}
