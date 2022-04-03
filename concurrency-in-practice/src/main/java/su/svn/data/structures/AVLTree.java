package su.svn.data.structures;

public interface AVLTree<K, N extends AVLTree.Node<K>> {

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
        <X extends AVLTreeImpl.Node<K>> X getLeft();
        <X extends AVLTreeImpl.Node<K>> void setLeft(X left);
        <X extends AVLTreeImpl.Node<K>> X getRight();
        <X extends AVLTreeImpl.Node<K>> void setRight(X right);
    }
}
