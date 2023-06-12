//A class represenging a 2d Vector, aka 2 doubles, with common operations in vector math
package Utils;

import java.io.Serializable;

public class Vector2 implements Serializable {
    public double x; //the x component of this vector 
    public double y; //the y component

    public Vector2(double x, double y) { //constructor from 2 doubles
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2I from) { //converts a vector2i to a normal vector2
        x = from.x;
        y = from.y;
    }

    public Vector2() { //creates a blank vector2
    }

    public boolean equals(Vector2I b) { //if the two components are equal, then the vectors are equal
        return x == b.x && y == b.y;
    }

    public Vector2 copy() { //creates a deep copy of a vector2
        return new Vector2(x, y);
    }

    public double getSquaredLength() { //gets the squared length of this vector, with pythagorean theorem
        return x * x + y * y;
    }
    
    public Vector2 scale(double scale) { //scales this vector by a scalar
        return new Vector2(x * scale, y * scale);
    }

    public double getLength() { //gets the non-squared length of this vector
        return Math.sqrt(getSquaredLength());
    }

    public Vector2 setLength(double desiredlength) { //sets the length of this vector to a desired length
        double currentLength = getLength(); //gets the current length
        return new Vector2(x / currentLength * desiredlength, y / currentLength * desiredlength); //divides it by its current length to normalize then scales by the desired length
    }

    public Vector2 normalize() { //sets it to a length of 1
        return setLength(1);
    }

    public Vector2 inverse() { //gets the inverse of this vector
        return new Vector2(-x, -y);
    }

    public Vector2 add(Vector2 b) { //adds the corresponding components of two vectors together
        return new Vector2(x + b.x, y + b.y);
    }

    public Vector2 subtract(Vector2 b) { //subtracts two vectors
        return add(b.inverse()); //adds one to the inverse of another
    }

    public double dot(Vector2 b) { //gets the dot product of two vectors
        return x * b.x + y * b.y;
    }

    public double cross(Vector2 b) { //gets the cross product of two vectors
        return x * b.y - y * b.x;
    }

    public String toString() { //converts to a string
        return x + " " + y;
    }
}