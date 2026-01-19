import java.io.File;

public class TypeDirectoryLookUp {
    public String getDirectory(String command) {
        String path = System.getenv("PATH");
        if (path == null || path.isEmpty()) {
            return null;
        }
        String[] directories = path.split(File.pathSeparator);
        for (String directoryName : directories) {
            File candidate = new File(directoryName,command);
            if(candidate.exists() && candidate.canExecute()){
               return candidate.getAbsolutePath();
            }
        }
       return null;
    }
}
