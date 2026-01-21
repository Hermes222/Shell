import java.io.File;
import java.io.IOException;
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
    public static void commandNotFound(String command) {
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
        String name = command[0];
        String args = command[1];
        switch (name) {
            case "echo" -> echoCommand(args);
            case "type" -> type(args);
            case "pwd"  -> getPwd();
            case "cd"    -> cd(args);
            default -> commandNotFound(name);
        }
    }

    private static void cd(String path) {
        File target;
        if(path.startsWith("/")) {
            target = new File(path);

        }else{
            target = new File(currentDir, path);
        }
        try{
            target = target.getCanonicalFile();
        }catch(Exception e){
            System.out.println("cd: "+path+": No such file or directory");
            startShellCommand();
            return;
        }

        if(target.exists() && target.isDirectory()) {
            currentDir = target;
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

    public static void type(String target) {
        if(shellBuiltinCommands.contains(target)) {
            System.out.println(target +" is a shell builtin");
            startShellCommand();
        }else{
            TypeDirectoryLookUp lookUp = new TypeDirectoryLookUp();
            String fullPath = lookUp.getDirectory(target);
            if(fullPath == null) {
                System.out.println(target + " is " + fullPath);
            }else{
                System.out.println(target + ": not found");
            }

        }
        startShellCommand();
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
        processBuilder.directory(currentDir);
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
