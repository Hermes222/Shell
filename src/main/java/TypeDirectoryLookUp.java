import java.io.File;

public class TypeDirectoryLookUp {
    public void getDirectory(String command) {
        String path = System.getenv("PATH");
        String[] directories = path.split(File.pathSeparator);
        for (String directoryName : directories) {
            File directory = new File(directoryName);
            if(directory.exists() && directory.canExecute()){
                System.out.println(command + " is " + directory.getAbsolutePath());
                Shell.startShellCommand();
            }
        }
        Shell.commandNotFound(command);
    }
}
