import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import java.io.File;

public class BagOfWordsPanel extends JPanel{
    private String category;
    private BagOfWords bagOfWords;
    private JTable table;
    private JScrollPane tableScrollPane;
    private JLabel totalWordsLabel;
    private FolderChooser folderChooserButton;
    public BagOfWordsPanel(String category, BagOfWords bagOfWords){
        // Initialization of attributes
        this.category = category;
        this.bagOfWords = bagOfWords;
        // Setting panel properties
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(Main.WIDTH / 3, Main.HEIGHT));

        // Initialization of Components
            // Label
        this.totalWordsLabel = new JLabel("Total Words in " + this.category + ": 0");
            // Table
		String[] columnNames = {
			"Word",
			"Frequency" 
		};
        Object[][] rowData = new Object[0][0];
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        this.table = new JTable(model);
            // ScrollPane
        this.tableScrollPane = new JScrollPane(this.table);
        this.tableScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
            // FolderChooser
        this.folderChooserButton = new FolderChooser(this.category);
        this.folderChooserButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                File[] directoryListing = folderChooserButton.openFileChooser();
                bagOfWords.setFileCount(directoryListing.length);
                if(directoryListing != null){
                    bagOfWords.resetAll();
                    for(File child : directoryListing){
                        // Get absolute path and loop its contents
                        bagOfWords.readFile(folderChooserButton.getAbsolutePath()+ "/" + child.getName());
                        System.out.println("Loaded " + folderChooserButton.getAbsolutePath()+ "/" + child.getName());
                    }
                    convertToTable();
                    SolutionPanel.updateTotalWords();
                    SolutionPanel.updateDictionarySize();
                }
            }
        });
        // Put elements to this panel
        this.add(this.folderChooserButton, BorderLayout.NORTH);
        this.add(this.tableScrollPane, BorderLayout.CENTER);
        this.add(this.totalWordsLabel, BorderLayout.SOUTH);
    }

	private void convertToTable(){
		DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
		tableModel.setRowCount(0);
        // Update Total Words
		this.totalWordsLabel.setText("Total Words in " + this.category + ": " + Integer.toString(this.bagOfWords.getWordCount()));
        // Iterate over the hashmap, bag of words
		for(String key : this.bagOfWords.getDict().keySet()){
            // Get data >>> key : frequency
			String[] data = new String[2];
			data[0] = key;
			data[1] = Integer.toString(this.bagOfWords.getDict().get(key));
            // Add a row in table model
			tableModel.addRow(data);
		}
        // Update the model of the table
		this.table.setModel(tableModel);
        // Force it to update
		tableModel.fireTableDataChanged();
	}
}