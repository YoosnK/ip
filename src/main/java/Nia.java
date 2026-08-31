/**
 * Entry point for Nia, a CLI task-tracking assistant.
 * Runs a read-parse-process loop: InputReader reads a line, Parser sanitizes it into
 * canonical words, and Processor executes the corresponding command.
 */
public class Nia {

    /** Reads commands from stdin in a loop until a "bye"-aliased command is entered. */
    public static void main(String[] args) {
        Printer.printBar();
        VoicelineManager.printStarter();

        TaskList taskList = new TaskList(100);

        boolean running = true;
        while (running) {
            Printer.printBar();
            Printer.printPrompt();
            String rawInput = InputReader.getUserInput();
            Printer.printBar();

            String[] words = Parser.parse(rawInput);
            if (words.length == 0) {
                continue;
            }

            running = Processor.process(words, taskList);
        }
        VoicelineManager.printEnding();
    }
}
