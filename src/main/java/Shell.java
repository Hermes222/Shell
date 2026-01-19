import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Shell {
    static File currentDir = new File(System.getProperty("user.dir"));

    static ArrayList<String> shellBuiltinCommands = new ArrayList<>(List.of("echo","type","exit","pwd","cd"));

    public static void startShellCommand() {
        System.out.print("$ ");
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        String[] result = splitCommand(command);

        if (result[0].equals("exit")) {
            System.exit(0);
        }

        if (shellBuiltinCommands.contains(result[0])) {
            executeShellCommand(result);
        } else {
            runExternalCommand(result);
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
        if (command[0].equals("pwd")) {
            getPwd();
        }
        if(command[0].equals("cd")) {
            cd(command[1]);
        }
    }

    private static void cd(String path) {
        if(!path.startsWith("/")) {
            startShellCommand();
        }
        File newDir = new File(path);
        if(newDir.exists() && newDir.isDirectory()) {
            currentDir = newDir;
        }else{
            System.out.println("cd: "+path+": No such file or directory");
        }
        startShellCommand();
    }

    private static void getPwd() {
        String pwd = currentDir.getAbsolutePath();
        System.out.println(pwd);
        startShellCommand();
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
    public static void runExternalCommand(String[] command) {
        String program = command[0];
        String argsString = command[1];
        List<String> args = new ArrayList<>();
        args.add(program);
        if(!argsString.isEmpty()){
            String[] parts = argsString.split("\\s+");
            args.addAll(Arrays.asList(parts));
        }
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.inheritIO();
        try{
            Process process = processBuilder.start();
            process.waitFor();
        }catch (java.io.IOException e){
            commandNotFound(program);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }finally {
            startShellCommand();
        }


    }

}
