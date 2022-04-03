package su.svn.data.structures.concurrent;

import java.util.concurrent.locks.ReadWriteLock;

public interface ConcurrentAVLTree<K, N extends ConcurrentAVLTree.Node<K>> {

    N find(K key);

    void insert(K key) ;

    void delete(K key);

    N getRoot();

    public interface Node<K> {
        boolean more(K key);
        boolean less(K key);
        boolean equal(K key);
        K getKey();
        int getHeight();
        void setHeight(int height);
        ReadWriteLock getRWLock();
        <X extends ConcurrentAVLTree.Node<K>> X getLeft();
        <X extends ConcurrentAVLTree.Node<K>> void setLeft(X left);
        <X extends ConcurrentAVLTree.Node<K>> X getRight();
        <X extends ConcurrentAVLTree.Node<K>> void setRight(X right);
    }
}
