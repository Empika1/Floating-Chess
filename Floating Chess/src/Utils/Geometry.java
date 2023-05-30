package Utils;

public class Geometry {
    public static final double bigSlope = 1E25;

    public static double slopeBetweenTwoPoints(Vector2 point1, Vector2 point2) {
        double slope = (point2.y - point1.y) / (point2.x - point1.x);
        if (Double.isNaN(slope) || Double.isInfinite(slope))
            return bigSlope;
        return slope;
    }

    public static Vector2[] lineCircleIntersections(Vector2 linePoint, double lineSlope, Vector2 circleCenter,
            double circleRadius) {
        double m = lineSlope;
        double b = linePoint.y - m * linePoint.x;

        double w = circleCenter.x;
        double h = circleCenter.y;
        double r = circleRadius;

        double intersection1X = (w + m * h - m * b + Math.sqrt(
                -m * m * w * w + 2 * m * w * h - 2 * m * b * w + m * m * r * r + 2 * b * h + r * r - b * b - h * h))
                / (1 + m * m);
        double intersection1Y = (b + m * w + m * m * h
                + m * Math.sqrt(
                        -h * h + 2 * b * h + m * (-m * w * w + 2 * w * h - 2 * b * w + m * r * r) + r * r - b * b))
                / (1 + m * m);
        Vector2 intersection1 = null;
        if (!Double.isNaN(intersection1X) && !Double.isNaN(intersection1Y))
            intersection1 = new Vector2(intersection1X, intersection1Y);

        double intersection2X = (w + m * h - m * b - Math.sqrt(
                -m * m * w * w + 2 * m * w * h - 2 * m * b * w + m * m * r * r + 2 * b * h + r * r - b * b - h * h))
                / (1 + m * m);
        double intersection2Y = (b + m * w + m * m * h
                - m * Math.sqrt(
                        -h * h + 2 * b * h + m * (-m * w * w + 2 * w * h - 2 * b * w + m * r * r) + r * r - b * b))
                / (1 + m * m);
        Vector2 intersection2 = null;
        if (!Double.isNaN(intersection2X) && !Double.isNaN(intersection2Y))
            intersection2 = new Vector2(intersection2X, intersection2Y);

        if (intersection1 != null && intersection2 != null)
            return new Vector2[] { intersection1, intersection2 };
        else if (intersection1 != null)
            return new Vector2[] { intersection1 };
        else if (intersection2 != null)
            return new Vector2[] { intersection2 };
        else
            return new Vector2[0];
    }

    public static Vector2[] lineCircleIntersections(Vector2 linePoint1, Vector2 linePoint2, Vector2 circleCenter,
            double circleRadius) {
        double lineSlope = slopeBetweenTwoPoints(linePoint1, linePoint2);
        return lineCircleIntersections(linePoint1, lineSlope, circleCenter, circleRadius);
    }

    public static Vector2 lineLineIntersection(Vector2 line1Point, double line1Slope, Vector2 line2Point,
            double line2Slope) {
        double m = line1Slope;
        double b = line1Point.y - m * line1Point.x;

        double a = line2Slope;
        double c = line2Point.y - a * line2Point.x;

        double intersectionX = (c - b) / (m - a);
        double intersectionY = (-m * c + b * a) / (a - m);

        if (!Double.isNaN(intersectionX) && !Double.isNaN(intersectionY))
            return new Vector2(intersectionX, intersectionY);
        else
            return null;
    }

    public static Vector2 lineLineIntersection(Vector2 line1Point1, Vector2 line1Point2, Vector2 line2Point1,
            Vector2 line2Point2) {
        double line1Slope = slopeBetweenTwoPoints(line1Point1, line1Point2);
        double line2Slope = slopeBetweenTwoPoints(line2Point1, line2Point2);
        return lineLineIntersection(line1Point1, line1Slope, line2Point1, line2Slope);
    }

    static final double epsilon = 0.001;

    public static boolean isPointInRect(Vector2 oneCorner, Vector2 oppositeCorner, Vector2 point) {
        Vector2 topLeftCorner = new Vector2(Math.min(oneCorner.x, oppositeCorner.x) - epsilon,
                Math.min(oneCorner.y, oppositeCorner.y) - epsilon);
        Vector2 bottomRightCorner = new Vector2(Math.max(oneCorner.x, oppositeCorner.x) + epsilon,
                Math.max(oneCorner.y, oppositeCorner.y) + epsilon);
        if (topLeftCorner.x <= point.x && point.x <= bottomRightCorner.x && topLeftCorner.y <= point.y
                && point.y <= bottomRightCorner.y)
            return true;
        return false;
    }

    public static boolean isPointInRect(Vector2I oneCorner, Vector2I oppositeCorner, Vector2I point) {
        Vector2I topLeftCorner = new Vector2I(Math.min(oneCorner.x, oppositeCorner.x),
                Math.min(oneCorner.y, oppositeCorner.y));
        Vector2I bottomRightCorner = new Vector2I(Math.max(oneCorner.x, oppositeCorner.x),
                Math.max(oneCorner.y, oppositeCorner.y));
        if (topLeftCorner.x <= point.x && point.x <= bottomRightCorner.x && topLeftCorner.y <= point.y
                && point.y <= bottomRightCorner.y)
            return true;
        return false;
    }
}
