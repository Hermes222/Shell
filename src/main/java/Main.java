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
        if (!command.equals("exit")) {
            System.out.println(command + ": command not found");
            startShellCommand();
        }
    }

}
