package nia.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import nia.tasks.Deadline;
import nia.tasks.Event;
import nia.tasks.Task;
import nia.tasks.TaskList;
import nia.tasks.Todo;
import nia.ui.Printer;

/**
 * Saves and loads a TaskList to/from a flat text file, so tasks survive between runs.
 * Each line is one task in Task#toSaveFormat()'s pipe-delimited format, e.g.
 * "D|~|1|~|finish math homework|~|Wednesday 6PM". The whole file is rewritten on every
 * save rather than patched incrementally - simplest correct approach at this scale.
 */
public class Storage {
    private static final String DATA_FILE_PATH = "./data/nia.txt";
    private static final Pattern SPLIT_PATTERN = Pattern.compile(Pattern.quote(Task.SAVE_DELIMITER));

    /** Loads tasks from disk into a new TaskList. Never throws: a missing file yields
     * an empty list, and a corrupted line is skipped with a warning. */
    public static TaskList load() {
        TaskList taskList = new TaskList();
        Path path = Path.of(DATA_FILE_PATH);

        if (!Files.exists(path)) {
            return taskList;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                Task task = parseLine(line);
                if (task != null) {
                    taskList.add(task);
                }
            }
        } catch (IOException e) {
            Printer.printIndentedError("Couldn't read saved tasks (" + e.getMessage() + "), starting fresh.");
        }

        return taskList;
    }

    /** Overwrites the save file with every task currently in taskList. */
    public static void save(TaskList taskList) {
        try {
            Path path = Path.of(DATA_FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            List<String> lines = new ArrayList<>();
            for (int i = 1; i <= taskList.getSize(); i++) {
                lines.add(taskList.getTask(i).toSaveFormat());
            }
            Files.write(path, lines);
        } catch (IOException e) {
            Printer.printIndentedError("Couldn't save tasks: " + e.getMessage());
        }
    }

    /** Parses one save-file line into a Task, or returns null (and warns) if it's malformed. */
    private static Task parseLine(String line) {
        if (line.isBlank()) {
            return null;
        }

        String[] fields = SPLIT_PATTERN.split(line, -1);
        try {
            String tag = fields[0];
            boolean isDone = parseDoneFlag(fields[1]);
            String description = fields[2];

            Task task = switch (tag) {
                case "T" -> new Todo(description);
                case "D" -> new Deadline(description, fields[3]);
                case "E" -> new Event(description, fields[3], fields[4]);
                default -> throw new IllegalArgumentException("unknown task tag '" + tag + "'");
            };

            if (isDone) {
                task.markAsDone();
            }
            return task;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            Printer.printIndentedError("Skipping corrupted save line: " + line);
            return null;
        }
    }

    private static boolean parseDoneFlag(String field) {
        if (field.equals("1")) {
            return true;
        }
        if (field.equals("0")) {
            return false;
        }
        throw new IllegalArgumentException("done flag must be 0 or 1, was '" + field + "'");
    }
}
