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


    }

    private enum Direction {
        LEFT, RIGHT, NONE
    }

    private Node root;

    public BinarySearchTree() {
        this.root = null;
    }

    private boolean insertInternal(Student data, Node sub_root, Node parent, Direction dir) {
        if (sub_root == null) {
            sub_root = new Node(data);
            sub_root.setParent(parent);
            switch (dir) {
                case RIGHT -> parent.setRight(sub_root);
                case LEFT -> parent.setLeft(sub_root);
            }
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
        if (root == null) {
            root = new Node(data);
            return true;
        }
        return insertInternal(data, root, null, Direction.NONE);
    }
}
