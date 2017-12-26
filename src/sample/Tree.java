package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
The tree is composed of at least one Node: root.
 */
class Tree implements Serializable{
    Node root;
    private static final long serialVersionUID = 42L;

    Tree(String rootName){
        root = new Node(rootName);
    }

    void addNode(String childName, String parentName){
        Node parentNode = getNode(root, parentName);
        parentNode.childrenList.add( new Node(childName, parentNode) );
    }

    void removeNode(String nodeName){
        // Only deletes given Node, not Node's sub-Nodes
        Node node = getNode(root, nodeName);
        Node parent = node.getParentNode();
        for (Node n : node.childrenList){
            n.setParent(parent); // otherwise, attribute parent of Node n will still point to removed node
            parent.childrenList.add( n );
        }
        parent.childrenList.remove( node ); // Return a boolean
    }

    private Node getNode(Node startNode, String nodeName){
        Node returnNode = null;
        for (Node node : startNode.childrenList){
            // The order of the if statements matters
            if ( !node.childrenList.isEmpty() ){
                returnNode = getNode(node, nodeName);
                if (returnNode != null){ break; }
            }
            if ( nodeName.equals(node.getName()) ){
                returnNode = node;
                break;
            }
        }
        // If childrenList is empty and one wants to populate root Node
        if (nodeName.equals(root.getName())){
            returnNode = root;
        }
        return returnNode;
    }

    void outputTree(){ //Function exists in order to have root Node as the starting point
        recursiveOutputTree(root);
    }

    private void recursiveOutputTree(Node start){
        for (Node n : start.childrenList){
            System.out.println(n.getName() + " ---  parent: " + n.getParentNode().getName());
            if ( !n.childrenList.isEmpty() ){
                recursiveOutputTree(n);
            }
        }
    }

    /*
    Each Node consists of three attributes:
    1. an indefinitely long list of children Nodes, these are added manually through text input in the UI
    2. the NodeName, which is the name entered in the text input
    3. a reference to the parent Node
     */
    class Node implements Serializable{
        private static final long serialVersionUID = 43L;

        List<Node> childrenList = new ArrayList<>();
        private String nodeName;
        private Node parent;

        // Constructor only used for root Node, which has no parent
        Node(String name){
            nodeName = name;
            parent = null;
        }

        Node(String name, Node parentNode){
            nodeName = name;
            parent = parentNode;
        }

        String getName(){
            return nodeName;
        }

        Node getParentNode(){
            return parent;
        }

        void setParent(Node newParent){
            parent = newParent;
        }

    }


}