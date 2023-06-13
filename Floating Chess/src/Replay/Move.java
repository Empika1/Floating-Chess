//a record that represents a single move (or board state) of a chess game.
package Replay;

import java.io.*;
import java.util.*;
import Pieces.*;

public record Move(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces, ArrayList<Piece> whitePiecesCaptured,
        ArrayList<Piece> blackPiecesCaptured, int whiteTimeLeft, int blackTimeLeft, int turnNumber)
        implements Serializable { // stores everything required to store a full board state
    public static byte[] serialize(Move m) { // serializes a move to a byte[]
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); // creates a stream to push bytes to
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(m); // writes the move to the stream
            return bos.toByteArray(); // converts the stream to a byte array and returns
        } catch (Exception e) {
            return null; // if anything screwed up, no byte array for you
        }
    }

    public static Move deserialize(byte[] b) { // deserializes a byte[] back to a move
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b); // creates a stream to pull an object from
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Move) ois.readObject(); // reads the next object from the stream and casts it to a move
        } catch (Exception e) {
            return null; // if anything screwed up, no move for you
        }
    }

    public static Move copy(Move m) { // creates a deep copy of a move by serializing then deserializing it
        return Move.deserialize(Move.serialize(m));
    }

    public Move copy() { // same but not static
        return copy(this);
    }
}