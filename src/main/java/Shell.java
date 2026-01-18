import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Shell {
    static ArrayList<String> shellBuiltinCommands = new ArrayList<>(List.of("echo","type","exit"));

    public static void startShellCommand() {
        System.out.print("$ ");
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        String[] result = splitCommand(command);

        if (!result[0].equals("exit")) {

            if(shellBuiltinCommands.contains(result[0])) {
                executeShellCommand(result);
            }else {
                commandNotFound(result[0]);
            }
        }
    }
    public static void commandNotFound(String command,Boolean type) {
        System.out.println(command + ": not found");
        startShellCommand();
    }    public static void commandNotFound(String command) {
        System.out.println(command + ": command not found");
        startShellCommand();
    }
    public static String[] splitCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String[] result = new String[2];

        String cmd = parts[0];                 // before first space
        String args = (parts.length > 1) ? parts[1] : "";   // everything after
        result[0] = cmd;
        result[1] = args;
        return result;
    }
    public static void executeShellCommand(String[] command) {
        if (command[0].equals("echo")) {
            echoCommand(command[1]);
        }
        if (command[0].equals("type")) {
            type(command);
        }
    }
    public static void type(String[] command) {
        if(shellBuiltinCommands.contains(command[1])) {
            System.out.println(command[1] +" is a shell builtin");
            startShellCommand();
        }else{
            TypeDirectoryLookUp lookUp = new TypeDirectoryLookUp();
            lookUp.getDirectory(command[1]);
        }
    }
    public static void echoCommand(String command) {
        System.out.println(command);
        startShellCommand();
    }
}
