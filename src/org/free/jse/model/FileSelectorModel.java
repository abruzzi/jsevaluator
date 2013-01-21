package org.free.jse.model;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileSelectorModel implements TreeModel {

    private FileNode root;

    /**
    * the constructor defines the root.
    */
    public FileSelectorModel(String directory) {
        root = new FileNode(directory);
    }

    public Object getRoot() {
        return root;
    }

    /**
    * returns the <code>parent</code>'s child located at index <code>index</code>.
    */
    public Object getChild(Object parent, int index) {
        FileNode parentNode = (FileNode) parent;
        List<File> children = getSortedChildren(parentNode);

        return new FileNode(parentNode,
                            children.get(index).getName());
    }

    /**
    * returns the number of child.  If the node is not a directory, or its list of children
    * is null, returns 0.  Otherwise, just return the number of files under the current file.
    */
    public int getChildCount(Object parent) {
        FileNode parentNode = (FileNode) parent;
        if (parent == null
                || !parentNode.isDirectory()
                || parentNode.listFiles() == null) {
            return 0;
        }

        return parentNode.listFiles().length;
    }

    /**
    * returns true if {{@link #getChildCount(Object)} is 0.
    */
    public boolean isLeaf(Object node) {
        return (getChildCount(node) == 0);
    }

    /**
    * return the index of the child in the list of files under <code>parent</code>.
    */
    public int getIndexOfChild(Object parent, Object child) {
        FileNode parentNode = (FileNode) parent;
        FileNode childNode = (FileNode) child;

        List<File> children = getSortedChildren(parentNode);

        return children.indexOf(childNode);
    }

    private List<File> getSortedChildren(File node) {
        List<File> children = Arrays.asList(node.listFiles());
        Collections.sort(children, new Comparator<File>() {

            public int compare(File o1, File o2) {
                if (o1.isDirectory() == o2.isDirectory()) {
                    return o1.getName().compareTo(o2.getName());
                }

                if (o1.isDirectory()) {
                    return -1;
                }

                return 1;
            }
        });

        return children;
    }

    // The following methods are not implemented, as we won't need them for this example.

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }
}