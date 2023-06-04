package Replay;

import java.io.*;
import java.util.*;
import Board.LossState;

public class Replay implements Serializable{
    public ArrayList<Move> moves = new ArrayList<Move>();
    public LossState lossState = null;

    public static byte[] serialize(Replay r) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(r);
            return bos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public static Replay deserialize(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Replay) ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
