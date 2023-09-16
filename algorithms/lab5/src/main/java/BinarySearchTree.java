public class BinarySearchTree {
    private static class Node {
        private Node parent;
        private Node left;
        private Node right;
        private Student data;

        public Node(Student data) {
            this.data = data;
            this.parent = this.left = this.right = null;
        }

        public Student getData() {
            return data;
        }

        public Node getParent() {
            return parent;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public void setData(Student data) {
            this.data = data;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Direction getDirection() {
            if (parent == null) {
                return Direction.NONE;
            }
            if (this == parent.getLeft()) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        }

    }

    private enum Direction {
        LEFT, RIGHT, NONE
    }

    private Node root;

    public BinarySearchTree() {
        this.root = null;
    }

    private void rotateLeft(Node node) {
        var parent = node.getParent();
        var left = node.getLeft();
        var parent_parent = node.getParent().getParent();

        switch (parent.getDirection()) {
            case LEFT -> parent_parent.setLeft(node);
            case RIGHT -> parent_parent.setRight(node);
        }
        node.setParent(parent_parent);

        node.setLeft(parent);
        parent.setParent(node);

        parent.setRight(left);
        if (left != null) {
            left.setParent(parent);
        }
    }

    private void rotateRight(Node node) {
        var parent = node.getParent();
        var right = node.getRight();
        var parent_parent = node.getParent().getParent();

        switch (parent.getDirection()) {
            case LEFT -> parent_parent.setLeft(node);
            case RIGHT -> parent_parent.setRight(node);
        }
        node.setParent(parent_parent);

        node.setRight(parent);
        parent.setParent(node);

        parent.setLeft(right);
        if (right != null) {
            right.setParent(parent);
        }
    }

    private void rotate(Node node) {
        if (node != null && node.getParent() != null) {
            switch (node.getDirection()) {
                case LEFT -> rotateRight(node);
                case RIGHT -> rotateLeft(node);
            }
        }
    }

    private boolean insertInternal(Student data, Node sub_root, Node parent, Direction dir) {
        if (sub_root == null) {
            var node = new Node(data);
            node.setParent(parent);
            switch (dir) {
                case RIGHT -> parent.setRight(node);
                case LEFT -> parent.setLeft(node);
            }
            while (node.getParent() != null) {
                rotate(node);
            }
            root = node;
            return true;
        } else {
            if (data.getName().equals(sub_root.getData().getName())) {
                return false;
            } else if (data.getName().compareTo(sub_root.getData().getName()) > 0) {
                return insertInternal(data, sub_root.getRight(), sub_root, Direction.RIGHT);
            } else {
                return insertInternal(data, sub_root.getLeft(), sub_root, Direction.LEFT);
            }
        }
    }

    public boolean insert(Student data) {
        return insertInternal(data, root, null, Direction.NONE);
    }

    public Student search(String key) {
        var node = root;

        while (node != null) {
            if (key.equals(node.getData().getName())) {
                return node.getData();
            } else if (key.compareTo(node.getData().getName()) > 0) {
                node = node.getRight();
            } else {
                node = node.getLeft();
            }
        }

        return null;
    }

    private String toStringInternal(Node sub_root) {
        return (sub_root != null) ? String.format("%s%s%s\n%s", toStringInternal(sub_root.getLeft()), sub_root.getData().toString(), sub_root == root ? " (Root)" : "", toStringInternal(sub_root.getRight())) : "";
    }

    public String toString() {
        return root != null ? toStringInternal(root) : "Empty\n";
    }
}
