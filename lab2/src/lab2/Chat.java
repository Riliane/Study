package lab2;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Chat {
    static File log;
    static PrintWriter logwriter;
    static void createLog() throws IOException {
        log = new File("logfile.txt");
        if (!log.exists()) {
            log.createNewFile();
        }
        logwriter = new PrintWriter(log.getAbsoluteFile());
        logwriter.println("Log created "+ Message.FORMAT.format(new Date()));
    }
    ArrayList<Message> messages;
    File msgFile;
    PrintWriter msgWriter;
    Reader reader;
    void readMessages(String filename) throws IOException, JsonParseException{
        messages = new ArrayList<>();
        File msgFile = new File(filename);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();
        if (!msgFile.exists()) {
            throw new FileNotFoundException();
        }
        reader = new InputStreamReader(new FileInputStream(msgFile));
        Message[] array = gson.fromJson(reader, Message[].class);
        messages = new ArrayList<Message>(Arrays.asList(array));
        logwriter.println("File " + filename + " read. " + messages.size() + " messages found.");
    }
    void sortByTime(){
        Comparator comp = new TimeComparator();
        Collections.sort(messages, comp);
        Chat.logwriter.println("Sorted messages by time.");
    }
    void showMessages(){
        for (Message msg : messages){
            System.out.println(msg.toString());
        }
    }
    void showMessages(Date start, Date end){
        for (Message msg : messages){
            if (start.compareTo(end)>0){
                System.out.println("The end date cannot precede the start date!");
            } else if (msg.timestamp.compareTo(start)>=0 && msg.timestamp.compareTo(end)<=0) {
                System.out.println(msg.toString());
            }
        }
    }
    void searchByAuthor(String author){
        int count=0;
        for (Message msg: messages){
            if (msg.author.equalsIgnoreCase(author)){
                count++;
                System.out.println(msg.toString());
            }
        }
        System.out.println("Total " + count + " messages found");
        Chat.logwriter.println("Search of messages by author" + author + "performed. Total " + count + " messages found");
    }
    void searchByWord(String word){
        int count=0;
        for (Message msg: messages){
            if (msg.message.contains(word)){
                count++;
                System.out.println(msg.toString());
            }
        }
        System.out.println("Total " + count + " messages found");
        Chat.logwriter.println("Search of messages by word" + word + "performed. Total " + count + " messages found");
    }
    void searchByRegex(String regex){
        int count=0;
        for (Message msg: messages){
            if (msg.message.matches(regex)){
                count++;
                System.out.println(msg.toString());
            }
        }
        System.out.println("Total " + count + " messages found");
        Chat.logwriter.println("Search of messages by regex" + regex + "performed. Total " + count + " messages found");
    }
    void addMessage(String nid, String nmessage, String nauthor, Date ntimestamp){
        if (nmessage.length()>140){
            Chat.logwriter.println("Warning: a very long message is being added.");
        }
        if (nmessage.length()==0){
            Chat.logwriter.println("Warning: an empty message is being added.");
        }
        messages.add(new Message(nid, nmessage, nauthor, ntimestamp));
        System.out.println("Done!");
        Chat.logwriter.println("Message with ID" + nid + "was added.");
        sortByTime();
    }
    void deleteMessage(String id){
        boolean hasDeleted = false;
        Iterator<Message> iter = messages.iterator();
        while (iter.hasNext()){
            if (iter.next().id.equalsIgnoreCase(id)){
                iter.remove();
                hasDeleted = true;
            }
        }
        if (hasDeleted){
            System.out.println("Done!");
            Chat.logwriter.println("Message with ID" + id + "was deleted.");
        } else{
            System.out.println("Not found");
            Chat.logwriter.println("Tried to delete message with ID" + id + ". Message with such ID was not found.");
        }
    }
    void WriteMessages (String filename)throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(messages, writer);
            logwriter.println("Messages written in file.");
        }
    }
}
