public class BinaryTree {
    public static class Node {
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

    private Node root;

    public BinaryTree() {
        this.root = null;
    }

    private boolean insertInternal(Student data, Node sub_root, Node parent) {
        if (sub_root == null) {
            sub_root = new Node(data);
            sub_root.setParent(parent);
            return true;
        } else {
            if (data.getStudentCardNo() == sub_root.getData().getStudentCardNo()) {
                return false;
            } else if (data.getStudentCardNo() > sub_root.getData().getStudentCardNo()) {
                return insertInternal(data, sub_root.getRight(), sub_root);
            } else {
                return insertInternal(data, sub_root.getLeft(), sub_root);
            }
        }
    }
}
