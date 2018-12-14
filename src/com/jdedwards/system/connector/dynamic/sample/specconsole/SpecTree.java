package com.jdedwards.system.connector.dynamic.sample.specconsole;

/*
 * This code is based on an example provided by Richard Stanford,
 * a tutorial reader.
 */

import com.jdedwards.system.connector.dynamic.spec.SpecFailureException;
import com.jdedwards.system.connector.dynamic.spec.dictionary.Context;
import com.jdedwards.system.connector.dynamic.spec.dictionary.InvalidBindingException;
import com.jdedwards.system.connector.dynamic.spec.dictionary.SpecDictionary;
import com.jdedwards.system.connector.dynamic.spec.source.*;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.specconsole.TestSpecTree 
 */
public class SpecTree extends JTree {
    protected DefaultMutableTreeNode rootNode = null;
    protected DefaultTreeModel treeModel = null;
    protected Toolkit toolkit = Toolkit.getDefaultToolkit();
    protected SpecDictionary specDictionary;
    protected BSFNSpecSource specSource;


    public SpecTree(String rootNodeName) {
        this(null, null, rootNodeName);
    }

    public SpecTree(SpecDictionary specDictionary, BSFNSpecSource specSource) {
        this(specDictionary, specSource, null);
    }

    public SpecTree(SpecDictionary specDictionary, BSFNSpecSource specSource, String rootName) {
        super();
        if (specDictionary == null && specSource == null) {
            if (rootName != null) {
                rootNode = new DefaultMutableTreeNode(rootName);
            } else {
                rootNode = new DefaultMutableTreeNode();
            }
        } else {
            this.specDictionary = specDictionary;
            this.specSource = specSource;
            rootNode = getInitialTree(specDictionary, specSource);
        }
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new SpecImageTreeModelListener());
        setModel(treeModel);
        addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
                if (node == null || node.getUserObject() == null)
                    return;
                else {
                    try {
                        Object obj = node.getUserObject();
                        if (obj instanceof Context) {
                            Context nodeContext = (Context) obj;
                            if (nodeContext.hasSubcontexts()) {
                                Context subContexts[] = nodeContext.getSubcontexts();
                                for (int i = 0; i < subContexts.length; i++) {
                                    node.add(new DefaultMutableTreeNode(subContexts[i]));
                                }
                            } else if (nodeContext.hasBoundSpec()) {
                                showParameters((BSFNMethod) nodeContext.getBoundSpec(), node);
                            }
                        } else if (obj instanceof BSFNMethod) {
                            showParameters((BSFNMethod) obj, node);
                        }
                    } catch (SpecFailureException e1) {
                        System.out.println(e1.getMessage());
                        e1.printStackTrace();
                    } catch (BSFNSpecNotFoundException e2) {
                        DefaultMutableTreeNode errorNode = (new DefaultMutableTreeNode("!!!Error--Cannot get the parameters from SpecSource"));
                        node.add(errorNode);
                    }
                }
            }

        });
        setEditable(false);

        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        setShowsRootHandles(true);
    }

    public SpecTree(BSFNSpecSource specSource) {
        this.specSource = specSource;
    }

    protected void showParameters(BSFNMethod bsfnMethod, DefaultMutableTreeNode node) throws SpecFailureException {
        DefaultMutableTreeNode templateNode = new DefaultMutableTreeNode(bsfnMethod.getDSTemplateName());
        node.add(templateNode);
        BSFNParameter[] params = bsfnMethod.getParameters();
        for (int i=0;i<params.length;i++) {
            BSFNParameter param = params[i];
            DefaultMutableTreeNode paramNode = new DefaultMutableTreeNode(param.getName() + " (" + param.getDataType() + ")");
            templateNode.add(paramNode);
        }


    }

    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }

    public void showContext(Context c) {
        clear();
        if (c != null) {
            try {
                expandContextNode(getRootNode(), c.getNameInNamespace(), specDictionary.getInitialContext());
            } catch (SpecFailureException e) {
                e.printStackTrace();
                e.getRootException().printStackTrace();
            }
        }
        this.repaint();
    }

    public void expandContextNode(DefaultMutableTreeNode parentNode, String path, Context context) throws SpecFailureException {
        String subcontextName = path;
        int index = path.indexOf(SpecDictionary.PATH_DELIMITER);
        if (index != -1) {
            subcontextName = path.substring(0, index);
        }
        DefaultMutableTreeNode childNode = null;
        Enumeration e = parentNode.children();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            Object obj = node.getUserObject();
            if ((obj instanceof Context) && ((Context) obj).getName().equals(subcontextName)) {
                childNode = node;
                break;
            }
        }
        Context subcontext = context.getSubcontext(subcontextName);
        if (childNode == null) {
            childNode = new DefaultMutableTreeNode(subcontext);
            parentNode.add(childNode);
            treeModel.insertNodeInto(childNode, parentNode, 0);
            scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        if (index != -1) {
            path = path.substring(index + 1);
            expandContextNode(childNode, path, subcontext);
        }
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                    (currentSelection.getLastPathComponent());
            Context currentContext = (Context) currentNode.getUserObject();
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        }

// Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    private DefaultMutableTreeNode lookupParentNode(DefaultMutableTreeNode node, Object[] contextPath, int index) {
// first find whether this node has already been inserted into the tree
        if (index >= contextPath.length) {
            return node;
        }
        Context currentContext = (Context) contextPath[index];
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (((Context) childNode.getUserObject()).getName().equals(currentContext.getName())) {
                return lookupParentNode(childNode, contextPath, index + 1);
            }
        }
        return node;
    }

    public void addContextNode(DefaultMutableTreeNode newNode) {
        if (newNode == null) return;
        Object[] contextPath = newNode.getUserObjectPath();
        DefaultMutableTreeNode parentNode = lookupParentNode(rootNode, contextPath, 1);
        int index = parentNode.getLevel() + 1;
        for (int i = index; i < contextPath.length; i++) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(contextPath[i]);
            treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
            scrollPathToVisible(new TreePath(childNode.getPath()));
            parentNode = childNode;
        }
    }


    public SpecDictionary
            getSpecDictionary() {
        return specDictionary;
    }

    public SpecSource getSpecSource() {
        return specSource;
    }

    public DefaultMutableTreeNode getInitialTree(SpecDictionary specDictionary, BSFNSpecSource specSource) {
        try {
            if (specDictionary == null && specSource == null) {
                rootNode = new DefaultMutableTreeNode();
            } else if (specDictionary != null) {
                try {
                    specDictionary.bindSpecSource(specSource);
                } catch (InvalidBindingException e) {
                }
                Context initContext = specDictionary.getInitialContext();
                rootNode = new DefaultMutableTreeNode(specDictionary.getName());
                Context subContexts[] = initContext.getSubcontexts();
                for (int i = 0; i < subContexts.length; i++) {
                    DefaultMutableTreeNode contextNode = new DefaultMutableTreeNode(subContexts[i]);
                    rootNode.add(contextNode);
                }
            } else {
                rootNode = new DefaultMutableTreeNode(specSource.getName());
                BSFNMethod[] bsfnMethodList = specSource.getBSFNMethods();
                for (int i=0;i<bsfnMethodList.length;i++) {
                    BSFNMethod bsfnMethod = bsfnMethodList[i];;
                    DefaultMutableTreeNode bsfnNode = new DefaultMutableTreeNode(bsfnMethod);
                    rootNode.add(bsfnNode);
                }
            }
        } catch (SpecFailureException e) {
            rootNode = new DefaultMutableTreeNode(e.getMessage());
            e.printStackTrace();
            e.getRootException().printStackTrace();
        }
        return rootNode;
    }

    public void addBSFNNode(DefaultMutableTreeNode newNode) {
        Object obj = newNode.getUserObject();
        if (obj instanceof BSFNMethod) {
            treeModel.insertNodeInto(newNode, rootNode, 0);
        }
    }

    protected class SpecImageTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)
                    (e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                        (node.getChildAt(index));
            } catch (NullPointerException exc) {
            }

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }

        public void treeNodesInserted(TreeModelEvent e) {
        }

        public void treeNodesRemoved(TreeModelEvent e) {
        }

        public void treeStructureChanged(TreeModelEvent e) {
        }
    }

}
