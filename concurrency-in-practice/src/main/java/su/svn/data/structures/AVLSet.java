package su.svn.data.structures;

public class AVLSet<T extends Comparable<T>> extends AVLTreeImpl<T, AVLSet.Node<T>> {

    static class Node<T extends Comparable<T>> implements AVLTree.Node<T> {
        private final T key;
        private int height;
        private Node<T> left;
        private Node<T> right;

        Node(T key) {
            this.key = key;
        }

        @Override
        public T getKey() {
            return key;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Node<T> getLeft() {
            return left;
        }

        @Override
        public <X extends AVLTree.Node<T>> void setLeft(X left) {
            //noinspection unchecked
            this.left = (Node<T>) left;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Node<T> getRight() {
            return right;
        }

        @Override
        public <X extends AVLTree.Node<T>> void setRight(X right) {
            //noinspection unchecked
            this.right = (Node<T>) right;
        }

        @Override
        public boolean more(T key) {
            return this.key.compareTo(key) < 0;
        }

        @Override
        public boolean less(T key) {
            return this.key.compareTo(key) > 0;
        }

        @Override
        public boolean equal(T key) {
            return this.key.compareTo(key) == 0;
        }

    }

    public AVLSet() {
        super(Node::new);
    }
}
