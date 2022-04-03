package su.svn.data.structures;

import java.util.function.Function;

public class AVLTreeImpl<K, N extends AVLTree.Node<K>> implements AVLTree<K, N> {

    private N root;

    private final Function<K, N> supplier;

    private AVLTreeImpl() {
        supplier = null;
    }

    protected AVLTreeImpl(Function<K, N> supplier) {
        this.supplier = supplier;
    }

    public AVLTreeImpl(Class<N> c, Class<K> keyClass) {
        this.supplier = k -> {
            try {
                return c.getConstructor(keyClass).newInstance(k);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public N find(K key) {
        N current = root;
        while (current != null) {
            if (current.equal(key)) {
                break;
            }
            current = current.less(key) ? current.getRight() : current.getLeft();
        }
        return current;
    }

    @Override
    public void insert(K key) {
        root = insert(root, key);
    }

    @Override
    public void delete(K key) {
        root = delete(root, key);
    }

    @Override
    public N getRoot() {
        return root;
    }

    public int height() {
        return root == null ? -1 : root.getHeight();
    }

    private N insert(N node, K key) {
        if (node == null) {
            return supplier.apply(key);
        } else if (node.more(key)) {
            node.setLeft(insert(node.getLeft(), key));
        } else if (node.less(key)) {
            node.setRight(insert(node.getRight(), key));
        }
        return rebalance(node);
    }

    private N delete(N node, K key) {
        if (node == null) {
            return node;
        } else if (node.more(key)) {
            node.setLeft(insert(node.getLeft(), key));
        } else if (node.less(key)) {
            node.setRight(insert(node.getRight(), key));
        } else {
            if (node.getLeft() == null || node.getRight() == null) {
                node = (node.getLeft() == null) ? node.getRight() : node.getLeft();
            } else {
                N oldLeft = node.getLeft();
                N oldRight = node.getRight();
                node = mostLeftChild(node.getRight());
                node.setRight(delete(oldRight, node.getKey()));
                node.setLeft(oldLeft);
            }
        }
        if (node != null) {
            node = rebalance(node);
        }
        return node;
    }

    private N mostLeftChild(N node) {
        N current = node;
        /* loop down to find the leftmost leaf */
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    private N rebalance(N node) {
        updateHeight(node);
        int balance = getBalance(node);
        if (balance > 1) {
            if (height(node.getRight().getRight()) > height(node.getRight().getLeft())) {
                node = rotateLeft(node);
            } else {
                node.setRight(rotateRight(node.getRight()));
                node = rotateLeft(node);
            }
        } else if (balance < -1) {
            if (height(node.getLeft().getLeft()) > height(node.getLeft().getRight())) {
                node = rotateRight(node);
            } else {
                node.setLeft(rotateLeft(node.getLeft()));
                node = rotateRight(node);
            }
        }
        return node;
    }

    private N rotateRight(N node) {
        N x = node.getLeft();
        N z = x.getRight();
        x.setRight(node);
        node.setLeft(z);
        updateHeight(node);
        updateHeight(x);
        return x;
    }

    private N rotateLeft(N node) {
        N x = node.getRight();
        N z = x.getLeft();
        x.setLeft(node);
        node.setRight(z);
        updateHeight(node);
        updateHeight(x);
        return x;
    }

    private void updateHeight(N n) {
        n.setHeight(1 + Math.max(height(n.getLeft()), height(n.getRight())));
    }

    private int height(N n) {
        return n == null ? -1 : n.getHeight();
    }

    private int getBalance(N n) {
        return (n == null) ? 0 : height(n.getRight()) - height(n.getLeft());
    }




    public void print(K key) {
        N current = root;
        while (current != null) {
            System.out.println("current = " + current.getKey());
            if (current.equal(key)) {
                break;
            }
            current = current.less(key) ? current.getRight() : current.getLeft();
        }
    }
}
