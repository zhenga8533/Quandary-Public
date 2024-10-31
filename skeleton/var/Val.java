package var;

public class Val extends Q {
    public Q value;

    public Val(Q value) {
        this.value = value;
    }

    public Q getValue() {
        return value;
    }

    public String toString() {
        return value == null ? "nil" : value.toString();
    }
}
