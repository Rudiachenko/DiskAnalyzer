package diskAnalyzer.main;

import diskAnalyzer.service.DiskAnalyzer;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

@AllArgsConstructor
public class App {
    private static final String EXIT = "e";
    private static final String YES = "y";
    private final DiskAnalyzer diskAnalyzer;
    private static final String PATH_TO_PACKAGE = "C:\\Users\\UX502033\\IdeaProjects";

    public static void main(String[] args) {
        App app = new App(new DiskAnalyzer());

        HashMap<Integer, Object> strategies = app.getStrategies();
        app.callFunction(strategies);
    }

    public void callFunction(HashMap<Integer, Object> strategies) {
        String command;
        boolean temp;
        do {
            System.out.println("Please, print  number of command (from 1 to 4) to get result or press 'e' to exit:" +
                    "\n1.Search for the file name with the maximum number of letters ‘s’ in the name" +
                    "\n2.Print Top-5 largest files (by size in bytes)." +
                    "\n3.The average file size (in bytes) in the specified directory or any its subdirectory." +
                    "\n4.The number of files and folders, divided by the first letters of the alphabet");

            Scanner in = new Scanner(System.in);
            command = in.nextLine();
            if (isCorrectCommand(command)) {
                System.out.println("Executing command with number " + command);

                Object functionResult = strategies.get(Integer.parseInt(command));

                System.out.println("Result of called function: \n" + functionResult);
            } else if (command.equals(EXIT)) {
                break;
            } else {
                System.out.println("Incorrect number of command. Please, try again");
            }

            System.out.println("Do you want to continue? Press 'y' if yes, or 'e' to exit");
            command = in.nextLine();
            temp = Objects.equals(command, YES);
        }
        while (temp);
    }

    private boolean isCorrectCommand(String command) {
        return StringUtils.isNumeric(command) && ((Integer.parseInt(command) >= 1 && Integer.parseInt(command) <= 4));
    }

    private HashMap<Integer, Object> getStrategies() {
        HashMap<Integer, Object> strategies = new HashMap<>();
        strategies.put(1, diskAnalyzer.getPathWithMaximumNumberLetterS(PATH_TO_PACKAGE));
        strategies.put(2, diskAnalyzer.getTopLargestFilesInBytes(PATH_TO_PACKAGE));
        strategies.put(3, diskAnalyzer.getAverageFileSize(PATH_TO_PACKAGE));
        strategies.put(4, diskAnalyzer.getNumberOfFilesAndFoldersDividedByA(PATH_TO_PACKAGE));
        return strategies;
    }
}
