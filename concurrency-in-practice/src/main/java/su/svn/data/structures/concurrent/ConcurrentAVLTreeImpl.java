package su.svn.data.structures.concurrent;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

@ThreadSafe
public class ConcurrentAVLTreeImpl<K, N extends ConcurrentAVLTree.Node<K>> implements ConcurrentAVLTree<K, N> {

    private final AtomicReference<N> root;

    private final ReadWriteLock lock;

    private final Function<K, N> supplier;

    private ConcurrentAVLTreeImpl() {
        root = null;
        lock = null;
        supplier = null;
    }

    protected ConcurrentAVLTreeImpl(Function<K, N> supplier) {
        this.root = new AtomicReference<>();
        this.lock = new ReentrantReadWriteLock();
        this.supplier = supplier;
    }

    public ConcurrentAVLTreeImpl(Class<N> nodeClass, Class<K> keyClass) {
        this(key -> {
            try {
                return nodeClass.getConstructor(keyClass).newInstance(key);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public N find(K key) {
        try {
            lock.readLock().lock();
            N current = this.root.get();
            while (current != null) {
                if (current.equal(key)) {
                    break;
                }
                current = current.less(key) ? current.getRight() : current.getLeft();
            }
            return current;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean insert(K key) {
        try {
            lock.writeLock().lock();
            N node = this.root.get();
            if (node == null) {
                N n = create(key, null);
                this.root.compareAndSet(null, n);
                return true;
            }

            while (true) {
                if (node.getKey().equals(key))
                    return false;

                N parent = node;

                boolean goLeft = node.more(key);
                node = goLeft ? node.getLeft() : node.getRight();

                if (node == null) {
                    if (goLeft) {
                        parent.setLeft(create(key, parent));
                    } else {
                        parent.setRight(create(key, parent));
                    }
                    rebalance(parent);
                    break;
                }
            }
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public N getRoot() {
        return this.root.get();
    }

    private N create(K key, N parent) {
        N node = supplier.apply(key);
        node.setParent(parent);
        return node;
    }

    @Override
    public void delete(K key) {
        try {
            lock.writeLock().lock();
            if (this.root.get() == null)
                return;

            N child = this.root.get();
            while (child != null) {
                N node = child;
                child = node.less(key) || node.equal(key) ? node.getRight() : node.getLeft();
                if (node.equal(key)) {
                    delete(node);
                    return;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void delete(N node) {

        if (node.getLeft() == null && node.getRight() == null) {
            if (node.getParent() == null) {
                this.root.set(null);
            } else {
                N parent = node.getParent();
                if (parent.getLeft().equal(node.getKey())) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
                rebalance(parent);
            }
            return;
        }

        if (node.getLeft() != null) {
            N child = node.getLeft();
            while (child.getRight() != null) child = child.getRight();
            // node.key = child.key;
            replace(node, child);
            delete(child);
        } else {
            N child = node.getRight();
            while (child.getLeft() != null) child = child.getLeft();
            // node.key = child.key;
            replace(node, child);
            delete(child);
        }
    }

    private void replace(N node, N child) {
        N parent = node.getParent();
        N replace = create(child.getKey(), parent);
        replace.setLeft(node.getLeft());
        replace.setRight(node.getRight());
        if (parent.getLeft().equal(node.getKey())) {
            parent.setLeft(replace);
        } else {
            parent.setRight(replace);
        }
    }

    private void rebalance(N node) {
        setBalance(node);

        if (node.getBalance() == -2) {
            if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight()))
                node = rotateRight(node);
            else
                node = rotateLeftThenRight(node);
        } else if (node.getBalance() == 2) {
            if (height(node.getRight().getRight()) >= height(node.getRight().getLeft()))
                node = rotateLeft(node);
            else
                node = rotateRightThenLeft(node);
        }

        if (node.getParent() != null) {
            rebalance(node.getParent());
        } else {
            N root = this.root.get();
            this.root.compareAndSet(root, node);
        }
    }

    private N rotateLeftThenRight(N node) {
        node.setLeft(rotateLeft(node.getLeft()));
        return rotateRight(node);
    }

    private N rotateRightThenLeft(N node) {
        node.setRight(rotateRight(node.getRight()));
        return rotateLeft(node);
    }

    private N rotateLeft(N node) {

        N up = node.getRight();
        up.setParent(node.getParent());

        node.setRight(up.getLeft());

        if (node.getRight() != null)
            node.getRight().setParent(node);

        up.setLeft(node);
        node.setParent(up);

        if (up.getParent() != null) {
            if (up.getParent().getRight() == node) {
                up.getParent().setRight(up);
            } else {
                up.getParent().setLeft(up);
            }
        }

        setBalance(node, up);

        return up;
    }

    private N rotateRight(N node) {

        N up = node.getLeft();
        up.setParent(node.getParent());

        node.setLeft(up.getRight());

        if (node.getLeft() != null)
            node.getLeft().setParent(node);

        up.setRight(node);
        node.setParent(up);

        if (up.getParent() != null) {
            if (up.getParent().getRight() == node) {
                up.getParent().setRight(up);
            } else {
                up.getParent().setLeft(up);
            }
        }

        setBalance(node, up);

        return up;
    }

    @SafeVarargs
    private void setBalance(N... nodes) {
        for (N node : nodes) {
            reheight(node);
            node.setBalance(height(node.getRight()) - height(node.getLeft()));
        }
    }

    private void reheight(N node) {
        if (node != null) {
            node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));
        }
    }

    private int height(N n) {
        if (n == null)
            return -1;
        return n.getHeight();
    }
}
