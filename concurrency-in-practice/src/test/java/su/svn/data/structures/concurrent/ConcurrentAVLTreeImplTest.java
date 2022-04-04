package su.svn.data.structures.concurrent;

import org.junit.Before;
import org.junit.Test;
import su.svn.data.structures.AVLTreeImplTest;
import su.svn.enums.Environment;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

import static org.junit.Assert.*;

public class ConcurrentAVLTreeImplTest {

    ConcurrentHashMap<Integer, Object> map;
    ConcurrentAVLTreeImpl<Integer, TreeInt.NodeInt> tree;

    @Before
    public void setUp() throws Exception {
        tree = new ConcurrentAVLTreeImpl<>(TreeInt.NodeInt::new);
        map = new ConcurrentHashMap<>();
    }

    @Test
    public void find() {
    }

    @Test
    public void insert() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            final int j = i;
            tree.insert(j);
        }
//        ExecutorService exec = Executors.newFixedThreadPool(256);
//        long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.err.println("memBefore: " + memBefore);
//        for (int i = 0; i < 8192; i++) {
//            final int j = i;
//            exec.execute(new Runnable() {
//                @Override
//                public void run() {
//                    tree.insert(j);
//                }
//            });
//        }
//        exec.shutdown();
//        if (exec.awaitTermination(30, TimeUnit.SECONDS)) {
//            System.err.println("awaitTermination returned true");
//        } else {
//            System.err.println("awaitTermination returned false");
//        }
//        exec.shutdownNow();
//        long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        System.err.println("memAfter: " + memAfter);
//        tree.print(Integer.MAX_VALUE);
    }

    @Test
    public void delete() throws InterruptedException {
        Object o = new Object();
        ExecutorService exec = Executors.newFixedThreadPool(256);
        long memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.err.println("memBefore: " + memBefore);
        for (int i = 0; i < 8192; i++) {
            final int j = i;
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    map.put(j, o);
                }
            });
        }
        exec.shutdown();
        if (exec.awaitTermination(30, TimeUnit.SECONDS)) {
            System.err.println("awaitTermination returned true");
        } else {
            System.err.println("awaitTermination returned false");
        }
        exec.shutdownNow();
        long memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.err.println("memAfter: " + memAfter);
    }

    @Test
    public void getRoot() {
    }

    @Test
    public void height() {
    }

    @Test
    public void print() {
    }


    static class TreeInt extends ConcurrentAVLTreeImpl<Integer, TreeInt.NodeInt> {

        static class NodeInt implements ConcurrentAVLTree.Node<Integer> {
            private final int key;
            private int balance;
            private int height;
            private NodeInt left;
            private NodeInt parent;
            private NodeInt right;

            NodeInt(int key) {
                this.key = key;
            }

            @Override
            public Integer getKey() {
                return key;
            }

            @Override
            public int getBalance() {
                return balance;
            }

            @Override
            public void setBalance(int balance) {
                this.balance = balance;
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
            public NodeInt getParent() {
                return parent;
            }

            @Override
            public <X extends Node<Integer>> void setParent(X parent) {
                this.parent = (NodeInt) parent;
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
            super(NodeInt::new);
        }
    }
}