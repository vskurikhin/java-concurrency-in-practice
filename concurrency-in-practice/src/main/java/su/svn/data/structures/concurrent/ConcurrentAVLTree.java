package su.svn.data.structures.concurrent;

import javax.annotation.Nullable;
import java.util.concurrent.locks.ReadWriteLock;

public interface ConcurrentAVLTree<K, N extends ConcurrentAVLTree.Node<K>> {

    @Nullable N find(K key);

    boolean insert(K key) ;

    void delete(K key);

    @Nullable N getRoot();

    public interface Node<K> {
        boolean more(K key);
        boolean less(K key);
        boolean equal(K key);
        K getKey();
        int getBalance();
        void setBalance(int balance);
        int getHeight();
        void setHeight(int height);
        <X extends ConcurrentAVLTree.Node<K>> X getLeft();
        <X extends ConcurrentAVLTree.Node<K>> void setLeft(X left);
        <X extends ConcurrentAVLTree.Node<K>> X getParent();
        <X extends ConcurrentAVLTree.Node<K>> void setParent(X parent);
        <X extends ConcurrentAVLTree.Node<K>> X getRight();
        <X extends ConcurrentAVLTree.Node<K>> void setRight(X right);
    }
}
