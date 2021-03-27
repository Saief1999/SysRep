package zones_textes_2;

import java.io.*;

public class MessageOperations {


    public static MessageObject deserialize(byte[] byteArray) {
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        try {
            ObjectInputStream is = new ObjectInputStream(in);

            return (MessageObject) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] getByteArray(MessageObject O) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ObjectOutputStream objOut = new ObjectOutputStream(os);
            objOut.writeObject(O);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
