package Replay;

import java.io.*;
import java.util.*;
import Pieces.*;

public record Move(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces, ArrayList<Piece> whitePiecesCaptured,
        ArrayList<Piece> blackPiecesCaptured, int whiteTimeLeft, int blackTimeLeft, int turnNumber) implements Serializable {
    public static byte[] serialize(Move m) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(m);
            return bos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public static Move deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Move)ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static Move copy(Move m) {
        return Move.deserialize(Move.serialize(m));
    }

    public Move copy() {
        return copy(this);
    }
}