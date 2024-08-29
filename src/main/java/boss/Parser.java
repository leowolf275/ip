package boss;

import java.io.IOException;

public class Parser {
    private Storage storage;
    private TaskList tasks;

    public Parser(Storage storage, TaskList tasks) {
        this.storage = storage;
        this.tasks = tasks;
    }

    public void handleCommand(String task) throws IOException {
        if (task.equals("list")) {
            storage.printFileContents();
        } else if (task.contains("unmark")) {
            String newFileData = tasks.unmark(task);
            storage.writeToFile(newFileData, false);
        }
        // need to ensure that the string contains a number!!!!
        else if (task.contains("mark")) {
            String newFileData = tasks.mark(task);
            storage.writeToFile(newFileData, false);

        } else if (task.contains("delete")) {
            String newFileData = tasks.delete(task);
            storage.writeToFile(newFileData, false);
        }
        // it must be a command to create a task (of some type)
        else {
            Boss.Types taskType;
            if (task.contains("todo")) {
                taskType = Boss.Types.TODO;
            } else if (task.contains("deadline")) {
                taskType = Boss.Types.DEADLINE;
            } else if (task.contains("event")) {
                taskType = Boss.Types.EVENT;
            } else {
                taskType = Boss.Types.NONE;
            }

            switch (taskType) {
                case TODO:
                    try {
                        String[] string = task.split(" ");
                        if (string.length == 1) {
                            throw new BossException("The description of todo cannot be empty!");
                        }
                        Task item = new Todo(task);
                        tasks.addTask(item);
                        int i = storage.numofTasks();
                        tasks.printabstraction(i);

                        storage.writeToFile(item + System.lineSeparator(), true);

                    } catch (BossException e) {
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Something went wrong: " + e.getMessage());
                    }
                    break;
                case DEADLINE:
                    try {
                        String[] string = task.split("/by ");
                        if (string.length == 1) {
                            throw new BossException("Please specify a deadline date!");
                        }
                        Task item = Deadline.of(string[0], string[1], false);
                        tasks.addTask(item);
                        int i = storage.numofTasks();
                        tasks.printabstraction(i);

                        storage.writeToFile(item + System.lineSeparator(), true);
                    } catch (BossException e) {
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Something went wrong: " + e.getMessage());
                    }
                    break;

                case EVENT:
                    try {
                        String[] string = task.split("/");
                        if (!(string.length == 3 && string[1].contains("from") && string[2].contains("to"))) {
                            throw new BossException("Wrong input! You must specify a description, start and end date for an event!");
                        }
                        String[] description = string[0].split(" ");
                        String from = string[1].split("from")[1];
                        String to = string[2].split("to")[1];
                        if (description.length == 1 || from.length() == 1 || to.length() == 1) {
                            throw new BossException("Invalid input!");
                        }
                        Task item = new Event(string[0], from, to, false);
                        tasks.addTask(item);
                        int i = storage.numofTasks();
                        tasks.printabstraction(i);

                        storage.writeToFile(item + System.lineSeparator(), true);
                    } catch (BossException e) {
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Something went wrong: " + e.getMessage());
                    }
                    break;
                default:
                    try {
                        System.out.println("added: " + task);
                        Task item = new Task(task);
                        tasks.addTask(item);

                        storage.writeToFile(item + System.lineSeparator(), true);
                    } catch (IOException e) {
                        System.out.println("Something went wrong: " + e.getMessage());
                    }
                    break;
            }
        }
    }

}
