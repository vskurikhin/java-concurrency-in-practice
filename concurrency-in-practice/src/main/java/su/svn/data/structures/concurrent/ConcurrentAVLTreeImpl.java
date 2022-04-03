package su.svn.data.structures.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Function;

public class ConcurrentAVLTreeImpl<K, N extends ConcurrentAVLTree.Node<K>> implements ConcurrentAVLTree<K, N> {

    private volatile N root;

    private final Function<K, N> supplier;

    private ConcurrentAVLTreeImpl() {
        supplier = null;
    }

    protected ConcurrentAVLTreeImpl(Function<K, N> supplier) {
        this.supplier = supplier;
    }

    public ConcurrentAVLTreeImpl(Class<N> c, Class<K> keyClass) {
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
            ReadWriteLock lock;
            synchronized (this) {
                lock = current.getRWLock();
                lock.readLock().lock();
            }
            if (current.equal(key)) {
                break;
            }
            current = current.less(key) ? current.getRight() : current.getLeft();
            lock.readLock().unlock();
        }
        return current;
    }

    @Override
    public void insert(K key) {
        N r = insert(root, key);
        synchronized (this) {
            root = r;
        }
    }

    @Override
    public void delete(K key) {
        N r = delete(root, key);
        synchronized (this) {
            root = r;
        }
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
            node.getRWLock().writeLock().lock();
            node.setLeft(insert(node.getLeft(), key));
            node.getRWLock().writeLock().unlock();
        } else if (node.less(key)) {
            node.getRWLock().writeLock().lock();
            node.setRight(insert(node.getRight(), key));
            node.getRWLock().writeLock().unlock();
        }
        return rebalance(node);
    }

    private N delete(N node, K key) {
        if (node == null) {
            return node;
        } else if (node.more(key)) {
            node.getRWLock().writeLock().lock();
            node.setLeft(insert(node.getLeft(), key));
            node.getRWLock().writeLock().unlock();
        } else if (node.less(key)) {
            node.getRWLock().writeLock().lock();
            node.setRight(insert(node.getRight(), key));
            node.getRWLock().writeLock().unlock();
        } else {
            if (node.getLeft() == null || node.getRight() == null) {
                ReadWriteLock lock;
                synchronized (this) {
                    lock = node.getRWLock();
                    lock.writeLock().lock();
                }
                node = (node.getLeft() == null) ? node.getRight() : node.getLeft();
                lock.writeLock().unlock();
            } else {
                ReadWriteLock lock;
                synchronized (this) {
                    lock = node.getRWLock();
                    lock.writeLock().lock();
                }
                N oldLeft = node.getLeft();
                N oldRight = node.getRight();
                node = mostLeftChild(node.getRight());
                node.setRight(delete(oldRight, node.getKey()));
                node.setLeft(oldLeft);
                lock.writeLock().unlock();
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
                ReadWriteLock lock;
                synchronized (this) {
                    lock = node.getRWLock();
                    lock.writeLock().lock();
                }
                node = rotateLeft(node);
                lock.writeLock().unlock();
            } else {
                ReadWriteLock lock;
                synchronized (this) {
                    lock = node.getRWLock();
                    lock.writeLock().lock();
                }
                node.setRight(rotateRight(node.getRight()));
                node = rotateLeft(node);
                lock.writeLock().unlock();
            }
        } else if (balance < -1) {
            if (height(node.getLeft().getLeft()) > height(node.getLeft().getRight())) {
                ReadWriteLock lock;
                synchronized (this) {
                    lock = node.getRWLock();
                    lock.writeLock().lock();
                }
                node = rotateRight(node);
                lock.writeLock().unlock();
            } else {
                ReadWriteLock lock;
                synchronized (this) {
                    lock = node.getRWLock();
                    lock.writeLock().lock();
                }
                node.setLeft(rotateLeft(node.getLeft()));
                node = rotateRight(node);
                lock.writeLock().unlock();
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
