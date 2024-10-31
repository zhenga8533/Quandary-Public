package var;

public class Int extends Q {
    public long value;

    public Int(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public String toString() {
        return Long.toString(value);
    }
}
