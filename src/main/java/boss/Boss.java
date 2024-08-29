package boss;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;

/**
 * Welcome! This class represents the main class of the Boss Chatbot.
 */
public class Boss {
    private Storage storage;
    private TaskList tasks;

    private Ui ui;

    public enum Types {
        TODO, DEADLINE, EVENT, NONE
    }

    /**
     * Starts the Chatbot.
     */
    public void run() {
        System.out.println("Hello! I'm the boss.");
        System.out.println("What can I do for you?");
        Scanner myObj = new Scanner(System.in);

        Parser parser = new Parser(storage, tasks);

        String task = myObj.nextLine();
        while (!task.equals("bye")) {
            try {
                parser.handleCommand(task);
            } catch (FileNotFoundException e) {
                ui.showLoadingError();
            } catch (IOException e) {
                ui.showLoadingError();
            }
            task = myObj.nextLine();
        }
        System.out.println("Bye. Hope to see you again soon!");

    }


    /**
     * Creates a Boss Object
     * @param filePath The location of the file that the chatbot
     *                 will read and write data from and to respectively.
     */

    public Boss(String filePath) {
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
        ui = new Ui();
    }


    public static void main(String[] args) {
        new Boss("src/main/data/boss.txt").run();
    }


}
