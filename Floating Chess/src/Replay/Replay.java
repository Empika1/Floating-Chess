//A class that represents a replay of a whole chess game
package Replay;

import java.io.*;
import java.util.*;
import Board.LossState;

public class Replay implements Serializable {
    public ArrayList<Move> moves = new ArrayList<Move>(); // all the moves that happened in the game, including the initial setup state
    public LossState lossState = null; // how the game was lost

    public static byte[] serialize(Replay r) { // serializes to byte[], in the same way that a move is serialized
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(r);
            return bos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public static Replay deserialize(byte[] b) { // deserializes from byte[], in the same way that a move is deserialized
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Replay) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveToFile(Replay replay, File f) throws IOException { // saves a replay to a specified file
        try (FileOutputStream fos = new FileOutputStream(f); // creats a fileoutputstream to push the replay to
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(replay); // writes replay to straem
        }
    }

    public static Replay readFromFile(File f) throws IOException, ClassNotFoundException { // reads a replay from a specified file
        try (FileInputStream fis = new FileInputStream(f); // creates a fileinputstream to pull the replay from
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Replay) ois.readObject(); // reads replay from stream
        }
    }
}