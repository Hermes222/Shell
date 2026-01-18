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
            if(shellCommands.contains(command)) {
                executeShellCommand(command);
            }else {
                System.out.println(command + ": command not found");
                startShellCommand();
            }
        }
    }
    public static void executeShellCommand(String command) {
        if (command.equals("echo")) {
            echoCommand(command);
        }
    }
    public static void echoCommand(String command) {
        System.out.println(command);
        startShellCommand();
    }

}
