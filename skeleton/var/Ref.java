package var;

public class Ref extends Q {
    public Q left;
    public Q right;

    public Ref(Q left, Q right) {
        this.left = left;
        this.right = right;
    }

    public Q getLeft() {
        return left;
    }

    public void setLeft(Q left) {
        this.left = left;
    }

    public Q getRight() {
        return right;
    }

    public void setRight(Q right) {
        this.right = right;
    }

    public String toString() {
        if (left == null && right == null) {
            return "nil";
        }
        
        String l = left == null ? "nil" : left.toString();
        String r = right == null ? "nil" : right.toString();
        return "(" + l + " . " + r + ")";
    }
}
