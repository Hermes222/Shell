import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
       startShellCommand();
        //System.out.println(Command + ": command not found");
    }
    public static void startShellCommand() {
        System.out.print("$ ");
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();

        ArrayList<String> shellCommands = new ArrayList<>(List.of("echo"));

        if (!command.equals("exit")) {
            String[] result = checkCommand(command);
            if(shellCommands.contains(result[0])) {
                executeShellCommand(result);
            }else {
                System.out.println(command + ": command not found");
                startShellCommand();
            }
        }
    }
    public static String[] checkCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String[] result = new String[2];

        String cmd = parts[0];                 // before first space
        String args = (parts.length > 1) ? parts[1] : "";   // everything after
        result[0] = cmd;
        result[1] = args;
        return result;
    }
    public static void executeShellCommand(String[] command) {
        if (command.equals("echo")) {
            echoCommand(command[1]);
        }
    }
    public static void echoCommand(String command) {
        System.out.println(command);
        startShellCommand();
    }

}
