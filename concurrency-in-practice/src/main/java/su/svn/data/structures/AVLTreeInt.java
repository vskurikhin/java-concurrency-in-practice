package su.svn.data.structures;

public class AVLTreeInt extends AVLTreeImpl<Integer, AVLTreeInt.NodeInt> {

    static class NodeInt implements AVLTree.Node<Integer> {
        private final int key;
        private int height;
        private NodeInt left;
        private NodeInt right;

        NodeInt(int key) {
            this.key = key;
        }

        @Override
        public Integer getKey() {
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
        public NodeInt getLeft() {
            return left;
        }

        @Override
        public <X extends Node<Integer>> void setLeft(X left) {
            this.left = (NodeInt) left;
        }

        @Override
        @SuppressWarnings("unchecked")
        public NodeInt getRight() {
            return right;
        }

        @Override
        public <X extends Node<Integer>> void setRight(X right) {
            this.right = (NodeInt) right;
        }

        @Override
        public boolean more(Integer key) {
            return this.key > key;
        }

        @Override
        public boolean less(Integer key) {
            return this.key < key;
        }

        @Override
        public boolean equal(Integer key) {
            return this.key == key;
        }
    }

    public AVLTreeInt() {
        super(NodeInt::new);
    }
}
