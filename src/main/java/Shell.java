import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.*;

public class Shell {
    static File currentDir = new File(System.getProperty("user.dir"));

    static ArrayList<String> shellBuiltinCommands = new ArrayList<>(List.of("echo","type","exit","pwd","cd"));

    public static void startShellCommand() {
        System.out.print("$ ");
        Scanner input = new Scanner(System.in);
        String command = input.nextLine();
        List<String> result = parseCommand(command);

        if (result.getFirst().equals("exit")) {
            System.exit(0);
        }

        if (shellBuiltinCommands.contains(result.getFirst())) {
            executeShellCommand(result);
        } else {
            runExternalCommand(result);
        }
    }
    public static void commandNotFound(String command) {
        System.out.println(command + ": command not found");
        startShellCommand();
    }
    public static List<String> parseCommand(String command) {
        List<String> result = new ArrayList<>();
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);
            if (c == '\'') {
                if(!inDoubleQuotes) {
                    inSingleQuotes = !inSingleQuotes;
                    continue;
                }
            }
            if (c == '\"') {
                if(!inSingleQuotes) {
                    inDoubleQuotes = !inDoubleQuotes;
                    continue;
                }
            }
            if (!inSingleQuotes && !inDoubleQuotes && Character.isWhitespace(c)) {
                if(!buffer.isEmpty()) {
                    result.add(buffer.toString());
                    buffer.setLength(0);
                }
                continue;
            }
         buffer.append(c);
        }
        if(!buffer.isEmpty()) {
            result.add(buffer.toString());
        }
        return result;
    }
    public static void executeShellCommand(List<String> command) {
        String name = command.getFirst();
        List<String> args = command.subList(1, command.size());
        switch (name) {
            case "echo" -> echoCommand(args);
            case "type" -> type(args);
            case "pwd"  -> getPwd();
            case "cd"    -> cd(args);
            default -> commandNotFound(name);
        }
    }

    private static void cd(List<String> pathList) {
        String path = pathList.getFirst();
        File target;
        if(path.startsWith("/")) {
            target = new File(path);

        }else if(path.startsWith("~")) {
            String home = System.getenv("HOME");
            target = new File(home);
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

    public static void type(List<String> targetOrig) {
        String target = targetOrig.getFirst();
        if(shellBuiltinCommands.contains(target)) {
            System.out.println(target +" is a shell builtin");
            startShellCommand();
        }else{
            TypeDirectoryLookUp lookUp = new TypeDirectoryLookUp();
            String fullPath = lookUp.getDirectory(target);
            if(fullPath != null) {
                System.out.println(target + " is " + fullPath);
            }else{
                System.out.println(target + ": not found");
            }

        }
        startShellCommand();
    }
    public static void echoCommand(List<String> command) {
        System.out.println(String.join(" ", command));
        startShellCommand();
    }
    public static void runExternalCommand(List<String> args) {
        if (args.isEmpty()) {
            startShellCommand();
            return;
        }
        String program = args.getFirst();
        ProcessBuilder processBuilder = new ProcessBuilder(new ArrayList<>(args));
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
