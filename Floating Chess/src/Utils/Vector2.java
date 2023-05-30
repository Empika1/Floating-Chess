package Utils;

public class Vector2 {
    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2I from) {
        x = from.x;
        y = from.y;
    }

    public Vector2() {
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public double getSquaredLength() {
        return x * x + y * y;
    }
    
    public Vector2 scale(double scale) {
        return new Vector2(x * scale, y * scale);
    }

    public double getLength() {
        return Math.sqrt(getSquaredLength());
    }

    public Vector2 setLength(double desiredlength) {
        double currentLength = Math.sqrt(x * x + y * y);
        return new Vector2(x / currentLength * desiredlength, y / currentLength * desiredlength);
    }

    public Vector2 normalize() {
        return setLength(1);
    }

    public Vector2 inverse() {
        return new Vector2(-x, -y);
    }

    public Vector2 add(Vector2 b) {
        return new Vector2(x + b.x, y + b.y);
    }

    public Vector2 subtract(Vector2 b) {
        return new Vector2(x - b.x, y - b.y);
    }

    public double dot(Vector2 b) {
        return x * b.x + y * b.y;
    }

    public double cross(Vector2 b) {
        return x * b.y - y * b.x;
    }

    public String toString() {
        return x + " " + y;
    }
}