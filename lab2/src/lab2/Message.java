package lab2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    String id;
    String message;
    String author;
    Date timestamp;
    final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    Message(String nid, String nmessage, String nauthor, Date ntimestamp){
        id=nid;
        message=nmessage;
        author=nauthor;
        timestamp=ntimestamp;
    }
    @Override
    public String toString() {
        return (id + " " + FORMAT.format(timestamp) + " " + author + ": " + message);
    }
}
