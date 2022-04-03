package su.svn.data.structures;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class AVLTreeImplTest {

    TreeInt tree;

    @Before
    public void setUp() throws Exception {
        tree = new TreeInt();
    }

    @Test
    public void find() {
    }

    @Test
    public void insert() {
        long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.err.println("memBefore: " + memBefore);
        for (int i = 0; i < Integer.MAX_VALUE / 512; i++) {
            tree.insert(i);
        }
        long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.err.println("memAfter: " + memAfter);
        tree.print(Integer.MAX_VALUE);
    }

    @Test
    public void delete() {
    }

    @Test
    public void getRoot() {
    }

    @Test
    public void height() {
    }

    static class TreeInt extends AVLTreeImpl<Integer, TreeInt.NodeInt> {

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

            @Override
            public String toString() {
                return "NodeInt{" +
                        "key=" + key +
                        ", height=" + height +
                        ", left=" + left +
                        ", right=" + right +
                        '}';
            }
        }

        public TreeInt() {
            super(TreeInt.NodeInt::new);
        }
    }
}