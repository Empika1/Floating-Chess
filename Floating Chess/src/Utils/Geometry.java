package Utils;

public class Geometry {
    public static final double bigSlope = 1E25;
    public static Vector2[] LineCircleIntersections(Vector2 linePoint, double lineSlope, Vector2 circleCenter, double circleRadius) {
        double m = lineSlope;
        double b = linePoint.y - m*linePoint.x;

        double w = circleCenter.x;
        double h = circleCenter.y;
        double r = circleRadius;

        double intersection1X = (w + m*h - m*b + Math.sqrt(-m*m*w*w + 2*m*w*h - 2*m*b*w + m*m*r*r + 2*b*h + r*r - b*b - h*h)) / (1 + m*m);
        double intersection1Y = (b + m*w + m*m*h + m*Math.sqrt(-h*h + 2*b*h + m*(-m*w*w + 2*w*h - 2*b*w + m*r*r) + r*r - b*b)) / (1 + m*m);
        Vector2 intersection1 = null;
        if(!Double.isNaN(intersection1X) && !Double.isNaN(intersection1Y))
            intersection1 = new Vector2(intersection1X, intersection1Y);

        double intersection2X = (w + m*h - m*b - Math.sqrt(-m*m*w*w + 2*m*w*h - 2*m*b*w + m*m*r*r + 2*b*h + r*r - b*b - h*h)) / (1 + m*m);
        double intersection2Y = (b + m*w + m*m*h - m*Math.sqrt(-h*h + 2*b*h + m*(-m*w*w + 2*w*h - 2*b*w + m*r*r) + r*r - b*b)) / (1 + m*m);
        Vector2 intersection2 = null;
        if(!Double.isNaN(intersection2X) && !Double.isNaN(intersection2Y))
            intersection2 = new Vector2(intersection2X, intersection2Y);

        if(intersection1 != null && intersection2 != null)
            return new Vector2[] {intersection1, intersection2};
        else if(intersection1 != null)
            return new Vector2[] {intersection1};
        else if(intersection2 != null)
            return new Vector2[] {intersection2};
        else 
            return new Vector2[0];
    } 

    public static Vector2 LineLineIntersection(Vector2 line1Point, double line1Slope, Vector2 line2Point, double line2Slope) {
        double m = line1Slope;
        double b = line1Point.y - m*line1Point.x;

        double a = line2Slope;
        double c = line2Point.y - a*line2Point.x;

        double intersectionX = (c-b)/(m-a);
        double intersectionY = (-m*c + b*a) / (a-m);

        if(!Double.isNaN(intersectionX) && !Double.isNaN(intersectionY))
            return new Vector2(intersectionX, intersectionY);
        else
            return null;
    }
}
