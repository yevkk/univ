package a;

import java.util.concurrent.atomic.AtomicBoolean;

public class UnitHandler implements Runnable {
    enum State {
        L, R;

        public State turn() {
            return switch (this) {
                case L -> R;
                case R -> L;
            };
        }
    }

    private State[] current;
    private State left;
    private State right;
    private  CustomBarrier barrier;
    private final AtomicBoolean done;
    private boolean modified;

    public UnitHandler(int length,  AtomicBoolean done) {
        this.done = done;
        this.current = new State[length];
        for (int i = 0; i < this.current.length; i++) {
            this.current[i] = (Math.random() < 0.5) ? State.L : State.R;
        }
        this.modified = false;
        this.left = this.right = null;
    }

    public void setBarrier(CustomBarrier barrier) {
        this.barrier = barrier;
    }

    public void setLeft(State left) {
        this.left = left;
    }

    public void setRight(State right) {
        this.right = right;
    }

    public State getLeft() {
        return current[0];
    }

    public State getRight() {
        return current[current.length - 1];
    }

    public String getLineString() {
        var strBuilder = new StringBuilder();
        for (var item : current) {
            strBuilder.append(item.toString());
        }
        return strBuilder.toString();
    }

    public boolean getModified() {
        return modified;
    }

    @Override
    public void run() {
        try {
            while (!done.get()) {
                var next = new State[current.length];
                modified = false;
                for (int i = 1; i < current.length - 1; i++) {
                    next[i] = current[i];
                    if ((current[i] == State.L && current[i - 1] == State.R) || (current[i] == State.R && current[i + 1] == State.L)) {
                        next[i] = current[i].turn();
                        modified = true;
                    }
                }

                next[0] = current[0];
                switch (current[0]) {
                    case L:
                        if (left != null && left == State.R) {
                            next[0] = current[0].turn();
                            modified = true;
                        }
                        break;
                    case R:
                        if (current[1] == State.L) {
                            next[0] = current[0].turn();
                            modified = true;
                        }
                        break;
                }

                next[next.length - 1] = current[current.length - 1];
                switch (current[current.length - 1]) {
                    case L:
                        if (current[current.length - 2] == State.R) {
                            next[next.length - 1] = current[current.length - 1].turn();
                            modified = true;
                        }
                        break;
                    case R:
                        if (right != null && right == State.L) {
                            next[next.length - 1] = current[current.length - 1].turn();
                            modified = true;
                        }
                        break;
                }

                current = next;
                barrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
