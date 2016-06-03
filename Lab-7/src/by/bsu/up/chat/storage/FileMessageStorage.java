package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import com.google.gson.*;

import java.io.*;
import java.util.*;

public class FileMessageStorage implements MessageStorage {
    private List<Message> messages;
    private Reader reader;
    private static final Logger logger = Log.create(InMemoryMessageStorage.class);
    private static String histFilename="messages.json";
    private Gson gson;
    public FileMessageStorage(){
        try {readMessages();}
        catch (IOException e){
            logger.error("Cannot create file for history", e);}
        for (Message message:messages){
        logger.info(String.format("Read message from user: %s", message));}
    }

    void readMessages() throws IOException{
        messages = new ArrayList<>();
        File msgFile = new File(histFilename);
        gson = new GsonBuilder().create();
        if (!msgFile.exists()) {
            msgFile.createNewFile();
            messages = new ArrayList<>();
        }
        else {
            reader = new InputStreamReader(new FileInputStream(msgFile));
            try{
                Message[] array = gson.fromJson(reader, Message[].class);
                if (array==null){messages = new ArrayList<>();}
                else{
                messages = new ArrayList<Message>(Arrays.asList(array));
                logger.info("File read. " + messages.size() + " messages found.");
                sortByTime();}}
            catch (JsonParseException e){
                logger.info("File has invalid format");
                messages = new ArrayList<>();
            }
        }
    }
    void sortByTime(){
        Comparator comp = new TimeComparator();
        Collections.sort(messages, comp);
    }

    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    public void addMessage(Message message) {
        messages.add(message);
        writeMessages();
    }

    public boolean removeMessage(String id){
        boolean hasDeleted = false;
        Iterator<Message> iter = messages.iterator();
        while (iter.hasNext()){
            if (iter.next().getId().equalsIgnoreCase(id)){
                iter.remove();
                hasDeleted = true;
                break;
            }
        }
        if (hasDeleted){
            logger.info("Message " + id + " deleted");
            writeMessages();
        } else{
            logger.info("Such message not found");
        }
        return hasDeleted;
    }
    public boolean updateMessage(Message message) {
        boolean hasEdit = false;
        for (Message msg:messages){
            if (msg.getId().equalsIgnoreCase(message.getId())){
                msg.setText(message.getText());
                hasEdit = true;
                break;
            }
        }
        if (hasEdit){
            logger.info("Message " + message.getId() + " was edited to \"" + message.getText() + "\"");
            writeMessages();
        } else{
            logger.info("Such message not found");
        }
        return hasEdit;
    }


    void writeMessages (){
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(histFilename), "UTF-8")) {
            gson.toJson(messages, writer);
        }
        catch (IOException e){
            logger.error("An error occurred while trying to write to file", e);
        }
    }
    public int size() {
        return messages.size();
    }
}
