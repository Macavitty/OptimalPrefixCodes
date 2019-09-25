package primat;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private T node = null;
    private double probability = 0.0;
    private List< Node<T>> children = new ArrayList<>();
    private Node<T> parent = null;

    public Node() { }

    public Node(T node, double probability) {
        this.node = node;
        this.probability = probability;
    }

    public Node<T> addChild(Node<T> child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node<T>> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<Node<T>> getChildren() {
        return children;
    }


    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }
}