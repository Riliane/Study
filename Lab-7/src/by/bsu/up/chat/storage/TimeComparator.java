package by.bsu.up.chat.storage;
import by.bsu.up.chat.common.models.Message;

import java.util.Comparator;

public class TimeComparator implements Comparator<Message> {
    public int compare(Message a, Message b) {
        if (a.getTimestamp() < b.getTimestamp()) return -1;
        else return 1;
    }
}