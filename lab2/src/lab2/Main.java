package lab2;

import com.google.gson.JsonParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            Chat.createLog();
            Chat chat = new Chat();
            chat.readMessages("messages.json");
            chat.sortByTime();
            chat.showMessages();
            System.out.println("What would you like to do?\n1: add a message\n2: Delete message\n3: view history\n4: View part of history\n5:Search by author\n6: Search by word\n7: Search by regex\n9: Write in file\n0: Exit");
            int action;
            try {
                action = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                action = -1;
            }
            while (true) {
                switch (action) {
                    case 1: {
                        try {
                            String nid;
                            String nmessage;
                            String nauthor;
                            Date ntimestamp;
                            System.out.println("Enter ID");
                            nid = sc.nextLine();
                            System.out.println("Enter message");
                            nmessage = sc.nextLine();
                            System.out.println("Enter author");
                            nauthor = sc.nextLine();
                            System.out.println("Enter date");
                            ntimestamp = Message.FORMAT.parse(sc.nextLine());
                            chat.addMessage(nid, nmessage, nauthor, ntimestamp);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format.");
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("Enter ID");
                        String id = sc.nextLine();
                        chat.deleteMessage(id);
                        break;
                    }
                    case 3: {
                        chat.showMessages();
                        break;
                    }
                    case 4: {
                        try {
                            Date start;
                            System.out.println("Enter start date");
                            start = Message.FORMAT.parse(sc.nextLine());
                            Date end;
                            System.out.println("Enter end date");
                            end = Message.FORMAT.parse(sc.nextLine());
                            chat.showMessages(start, end);
                        } catch (ParseException e) {
                            System.out.println("Invalid date format.");
                        }
                        break;
                    }
                    case 5: {
                        String author;
                        System.out.println("Enter author:");
                        author = sc.nextLine();
                        chat.searchByAuthor(author);
                        break;
                    }
                    case 6: {
                        String word;
                        System.out.println("Enter word:");
                        word = sc.nextLine();
                        chat.searchByWord(word);
                        break;
                    }
                    case 7: {
                        String regex;
                        System.out.println("Enter regex:");
                        regex = sc.nextLine();
                        chat.searchByRegex(regex);
                        break;
                    }
                    case 9: {
                        chat.WriteMessages("messages.json");
                        break;
                    }
                    case 0: {
                        return;
                    }
                    default: {
                        System.out.println("Invalid option. Please try again.");
                    }
                }

                    try {
                        action = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        action = -1;
                    }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with messages is not found");
            Chat.logwriter.println("File with messages is not found");
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing a file");
            Chat.logwriter.println("An error occurred while reading or writing a file");
        } catch (JsonParseException e) {
            System.out.println("Invalid format of JSON file");
            Chat.logwriter.println("Invalid format of JSON file");
        } finally {
            Chat.logwriter.close();
        }
    }
}
