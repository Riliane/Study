package lab2;

import java.util.Comparator;

public class TimeComparator implements Comparator<Message> {
    public int compare(Message a, Message b) { return (a.timestamp.compareTo(b.timestamp));}
}
