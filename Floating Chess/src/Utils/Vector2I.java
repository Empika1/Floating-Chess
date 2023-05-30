package Utils;

public class Vector2I {
    public int x;
    public int y;

    public Vector2I(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2I(Vector2 from) {
        x = (int) from.x;
        y = (int) from.y;
    }

    public Vector2I() {
    }

    public boolean equals(Vector2I b) {
        return x == b.x && y == b.y;
    }

    public Vector2I copy() {
        return new Vector2I(x, y);
    }

    public double getSquaredLength() {
        return x * x + y * y;
    }

    public Vector2I scale(double scale) {
        return new Vector2I((int)(x * scale), (int)(y * scale));
    }

    public double getLength() {
        return Math.sqrt(getSquaredLength());
    }

    public Vector2I setLength(double desiredlength) {
        double currentLength = Math.sqrt(x * x + y * y);
        return new Vector2I((int) (x / currentLength * desiredlength), (int) (y / currentLength * desiredlength));
    }

    public Vector2I normalize() {
        return setLength(1);
    }

    public Vector2I inverse() {
        return new Vector2I(-x, -y);
    }

    public Vector2I add(Vector2I b) {
        return new Vector2I(x + b.x, y + b.y);
    }

    public Vector2I subtract(Vector2I b) {
        return new Vector2I(x - b.x, y - b.y);
    }

    public double dot(Vector2I b) {
        return x * b.x + y * b.y;
    }

    public double cross(Vector2I b) {
        return x * b.y - y * b.x;
    }

    public String toString() {
        return x + " " + y;
    }
}