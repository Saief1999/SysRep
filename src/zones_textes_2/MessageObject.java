package zones_textes_2;

import java.io.Serializable;

public class MessageObject implements Serializable {

    private String message;
    private int length ;
    private int offset ;
    private int operationType  ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public MessageObject(String message, int length, int offset, int operationType) {
        this.message = message;
        this.length = length;
        this.offset = offset;
        this.operationType = operationType;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
}
