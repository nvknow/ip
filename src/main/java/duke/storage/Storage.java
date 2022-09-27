package duke.storage;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static duke.task.TaskList.*;


public class Storage {
    private static ArrayList<Task> tasks;
    private File f;
    public Storage(String filePath){
        f = new File(filePath); // create a File for the given file path
    }
    public ArrayList<Task> load() throws FileNotFoundException {
        tasks = new ArrayList<Task>();
        Scanner s = new Scanner(f); // create a Scanner using the File as the source
        int numberOfLine = 0;
        while (s.hasNext()) {
            String line = s.nextLine();
            String[] parsedLine = line.split(" \\| ");
            numberOfLine += 1;
            switch (parsedLine[0]) {
            case "T":
                addTodo(tasks, parsedLine[2]);
                break;
            case "D":
                String deadline = parsedLine[2] + " /by " + parsedLine[3];
                addDeadline(tasks, deadline);
                break;
            case "E":
                String event = parsedLine[2] + " /at " + parsedLine[3];
                addEvent(tasks, event);
                break;
            }
            switch (parsedLine[1]) {
            case "1":
                markTask(tasks, numberOfLine);
                break;
            case "0":
                unmarkTask(tasks, numberOfLine);
                break;
            }
        }
        return tasks;
    }

    public static void write(String filePath, ArrayList<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        for (int i = 0; i < tasks.size(); i++) {
            String line = "";
            switch (tasks.get(i).getClass().getSimpleName()) {
            case "Todo":
                line = line + "T | ";
                break;
            case "Deadline":
                line = line + "D | ";
                break;
            case "Event":
                line = line + "E | ";
                break;
            }
            switch (tasks.get(i).getStatusIcon()) {
            case "X":
                line = line + "1 | ";
                break;
            case " ":
                line = line + "0 | ";
                break;
            }
            line = line + tasks.get(i).getDescription();
            switch (tasks.get(i).getClass().getSimpleName()) {
            case "Deadline":
                Deadline d = (Deadline) tasks.get(i);
                line = line + " | " + d.getBy();
                break;
            case "Event":
                Event e = (Event) tasks.get(i);
                line = line + " | " + e.getAt();
                break;
            }
            line = line + System.lineSeparator();
            fw.write(line);
        }
        fw.close();
    }
}
