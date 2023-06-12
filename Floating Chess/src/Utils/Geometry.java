//A class with some commonly used geometry stuff as static methods
package Utils;

public class Geometry {
    public static final double bigSlope = 1E25; //a very high number that is used as the slope of a vertical line because that doesnt have a slope

    public static double slopeBetweenTwoPoints(Vector2 point1, Vector2 point2) { //finds the slope between two points
        double slope = (point2.y - point1.y) / (point2.x - point1.x); //rise over run
        if (Double.isNaN(slope) || Double.isInfinite(slope)) //if the line is vertical
            return bigSlope; //bigslope
        return slope; //else slope
    }

    public static Vector2[] lineCircleIntersections(Vector2 linePoint, double lineSlope, Vector2 circleCenter,
            double circleRadius) { //finds the intersection points (if they exist) of a line and a circle
        //single letter variable names used so this isn't even longer than it already is
        double m = lineSlope; //the slope of the line
        double b = linePoint.y - m * linePoint.x; //one point on the line

        double w = circleCenter.x; //the x pos of the center of the circle
        double h = circleCenter.y; //the y pos of the center of the circle
        double r = circleRadius; //the radius of the circle

        //this is math translated directly into code so it's completely indecipherable lol
        double intersection1X = (w + m * h - m * b + Math.sqrt(
                -m * m * w * w + 2 * m * w * h - 2 * m * b * w + m * m * r * r + 2 * b * h + r * r - b * b - h * h))
                / (1 + m * m); //finds the x position of the first intersection point
        double intersection1Y = (b + m * w + m * m * h
                + m * Math.sqrt(
                        -h * h + 2 * b * h + m * (-m * w * w + 2 * w * h - 2 * b * w + m * r * r) + r * r - b * b))
                / (1 + m * m); //finds the y position of the first intersection point
        Vector2 intersection1 = null;
        if (!Double.isNaN(intersection1X) && !Double.isNaN(intersection1Y))
            intersection1 = new Vector2(intersection1X, intersection1Y); //if the point exists, make a vector2 for it

        double intersection2X = (w + m * h - m * b - Math.sqrt(
                -m * m * w * w + 2 * m * w * h - 2 * m * b * w + m * m * r * r + 2 * b * h + r * r - b * b - h * h))
                / (1 + m * m); //do the same thing for the 2nd intersection point
        double intersection2Y = (b + m * w + m * m * h
                - m * Math.sqrt(
                        -h * h + 2 * b * h + m * (-m * w * w + 2 * w * h - 2 * b * w + m * r * r) + r * r - b * b))
                / (1 + m * m);
        Vector2 intersection2 = null;
        if (!Double.isNaN(intersection2X) && !Double.isNaN(intersection2Y))
            intersection2 = new Vector2(intersection2X, intersection2Y);

        //return the intersections as an array. always make sure that there are either 0 or 2 intersections, because that's what some other code expects
        if (intersection1 != null && intersection2 != null)
            return new Vector2[] { intersection1, intersection2 };
        else if (intersection1 != null)
            return new Vector2[] { intersection1, intersection1 };
        else if (intersection2 != null)
            return new Vector2[] { intersection2, intersection2 };
        else
            return new Vector2[0];
    }

    public static Vector2[] lineCircleIntersections(Vector2 linePoint1, Vector2 linePoint2, Vector2 circleCenter,
            double circleRadius) { //same thing but with 2 points specified for the line instead of a line and a slope
        double lineSlope = slopeBetweenTwoPoints(linePoint1, linePoint2); //get the slope
        return lineCircleIntersections(linePoint1, lineSlope, circleCenter, circleRadius); //yeah
    }

    public static Vector2 lineLineIntersection(Vector2 line1Point, double line1Slope, Vector2 line2Point,
            double line2Slope) { //finds the intersection of two lines if they exist
        double m = line1Slope; //the slope of the first line
        double b = line1Point.y - m * line1Point.x; //the y intercept of the first line

        double a = line2Slope; //the slope of the second line
        double c = line2Point.y - a * line2Point.x; //the y intercept of the second line

        double intersectionX = (c - b) / (m - a); //find intersection
        double intersectionY = (-m * c + b * a) / (a - m);

        if (!Double.isNaN(intersectionX) && !Double.isNaN(intersectionY)) //if intersection exists
            return new Vector2(intersectionX, intersectionY); //package it in a vector2 and return
        else
            return null; //else dont
    }

    public static Vector2 lineLineIntersection(Vector2 line1Point1, Vector2 line1Point2, Vector2 line2Point1,
            Vector2 line2Point2) { //same thing but with 2 points for each line instead of point and slope
        double line1Slope = slopeBetweenTwoPoints(line1Point1, line1Point2);
        double line2Slope = slopeBetweenTwoPoints(line2Point1, line2Point2);
        return lineLineIntersection(line1Point1, line1Slope, line2Point1, line2Slope);
    }

    static final double epsilon = 0.01; //a very small number to account for floating point jank

    public static boolean isPointInRect(Vector2 oneCorner, Vector2 oppositeCorner, Vector2 point) { //checks if a point is in a rectangle
        Vector2 topLeftCorner = new Vector2(Math.min(oneCorner.x, oppositeCorner.x) - epsilon,
                Math.min(oneCorner.y, oppositeCorner.y) - epsilon); //finds the top left corner, and moves it outwards by epsilon
        Vector2 bottomRightCorner = new Vector2(Math.max(oneCorner.x, oppositeCorner.x) + epsilon,
                Math.max(oneCorner.y, oppositeCorner.y) + epsilon); //finds the bottom right corner, and moves it outwards by epsilon
        if (topLeftCorner.x <= point.x && point.x <= bottomRightCorner.x && topLeftCorner.y <= point.y
                && point.y <= bottomRightCorner.y) //checks if point is between the two corners
            return true;
        return false;
    }

    public static boolean isPointInRect(Vector2I oneCorner, Vector2I oppositeCorner, Vector2I point) { //same but with vector2is
        Vector2I topLeftCorner = new Vector2I(Math.min(oneCorner.x, oppositeCorner.x),
                Math.min(oneCorner.y, oppositeCorner.y));
        Vector2I bottomRightCorner = new Vector2I(Math.max(oneCorner.x, oppositeCorner.x),
                Math.max(oneCorner.y, oppositeCorner.y));
        if (topLeftCorner.x <= point.x && point.x <= bottomRightCorner.x && topLeftCorner.y <= point.y
                && point.y <= bottomRightCorner.y)
            return true;
        return false;
    }

    public static boolean doesCircleOverlapCircle(Vector2 pos1, double radius1, Vector2 pos2, double radius2) { //checks if a circle overlaps another circle
        Vector2 diff = pos1.subtract(pos2); //the difference in positions of the centers of the two circles
        double distanceSquared = diff.x * diff.x + diff.y * diff.y; //the squared distance between the centers of the two circles
        return distanceSquared <= (radius1 + radius2) * (radius1 + radius2); //pythagorean theorem
    }

    public static boolean doesCircleOverlapCircle(Vector2I pos1, double radius1, Vector2I pos2, double radius2) { //same but with vector2is
        return doesCircleOverlapCircle(new Vector2(pos1), radius1, new Vector2(pos2), radius2);
    }
}