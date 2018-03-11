import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.io.File;

public class FolderChooser extends JButton{
    private BagOfWords bagOfWords;
    private String category;
    private JFileChooser folderChooser;
    public FolderChooser(String category){
        this.category = category;
        // Initialize button
        this.setText("Select " + this.category + " Folder");
        this.setFocusable(false);
        // Initialize folderChooser
        this.folderChooser = new JFileChooser();
        this.folderChooser.setCurrentDirectory(new File("."));
        this.folderChooser.setDialogTitle("Open " + this.category + " Folder");
        this.folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.folderChooser.setAcceptAllFileFilterUsed(false);
    }

    public File getAbsolutePath(){
        // Returns the absolute path of the folder selected
        return this.folderChooser.getSelectedFile();
    }
    public File[] openFileChooser(){
        // Opens the file chooser then returns an array of files
        if(this.folderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            return this.folderChooser.getSelectedFile().listFiles();
        }
        return null;
    }
}

