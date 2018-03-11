import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
public class SolutionPanel extends JPanel{
	public static int totalWordsCount = 0;
	public static int dictionarySize = 0;
	public static BagOfWords ham;
	public static BagOfWords spam;
	private static JLabel pathLabel;
	private BagOfWordsPanel hamPanel;
	private BagOfWordsPanel spamPanel;
	private static JLabel dictionarySizeLabel;
	private static JLabel totalWordsLabel;
	private FolderChooser classifyFolderChooser;
	private JButton filterButton;
	private JTable outputTable;
	private JScrollPane outputTableScrollPane;
	private File[] directoryListing;
	private JTextArea kTextArea;
	public SolutionPanel(BagOfWords ham, BagOfWords spam){
		// Initializing properties
		this.ham = ham;
		this.spam = spam;
		// Setting the panel
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));

		// Initializing ham and spam panels
		this.hamPanel = new BagOfWordsPanel("Ham", ham);
		this.spamPanel = new BagOfWordsPanel("Spam", spam);
			// Labels
        this.dictionarySizeLabel = new JLabel("Dictionary Size: 0");
        this.totalWordsLabel = new JLabel("Total Words: 0");
		this.pathLabel = new JLabel("");
			// Folder Chooser
		this.classifyFolderChooser = new FolderChooser("Classify");
			// Button
		this.filterButton = new JButton("Filter");
		this.filterButton.setFocusable(false);
			// Table
        String[] outputTableColumnNames = {
            "Filename",
            "Class",
            "P(Spam)"
        };
        Object[][] rowData = new Object[0][0];
        DefaultTableModel model = new DefaultTableModel(rowData, outputTableColumnNames);
        this.outputTable = new JTable(model);
			// ScrollPane
        this.outputTableScrollPane = new JScrollPane(this.outputTable);
        this.outputTableScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		// Adding components to panel
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.setPreferredSize(new Dimension(Main.WIDTH/3, 50));
			// Dictionary Size and Total number of words
			JPanel labelWrapper = new JPanel(new GridLayout(1, 3));
			labelWrapper.add(dictionarySizeLabel);
			labelWrapper.add(totalWordsLabel);
			labelWrapper.add(pathLabel);
			// Folder Chooser and Filter Button
			JPanel buttonWrapper = new JPanel(new GridLayout(1, 3));
			this.kTextArea = new JTextArea();
			buttonWrapper.add(classifyFolderChooser);
			buttonWrapper.add(kTextArea);
			buttonWrapper.add(filterButton);
			// Add Folder Chooser Button Action Listener
		rightPanel.add(buttonWrapper, BorderLayout.NORTH);
		rightPanel.add(outputTableScrollPane, BorderLayout.CENTER);
		rightPanel.add(labelWrapper, BorderLayout.SOUTH);
		this.add(hamPanel, BorderLayout.WEST);
		this.add(spamPanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.EAST);
		this.filterButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int k;
				try{
					FileWriter output = new FileWriter("classify.txt");
					DefaultTableModel tableModel = (DefaultTableModel) outputTable.getModel();
					tableModel.setRowCount(0);
					ProbabilitySolver ps = new ProbabilitySolver();
					try{
						k = Integer.parseInt(kTextArea.getText());
					} catch (Exception ex) {
						k = 1;
					}
					ps.setK(k); // lagay dito yung value sa text field
					for(File child : directoryListing){
						ps.setFolder(classifyFolderChooser.getAbsolutePath().toString(), child.getName());
						ps.setCountNewWords(); // count mo rito yung new words yung wala sa spam at ham
						addToTable(ps.getOutput(), output);
					}
					output.close();
				} catch(IOException ie){}
			}
		});
		classifyFolderChooser.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// Store the array of files inside the folder to directoryListing
				directoryListing = classifyFolderChooser.openFileChooser();
				pathLabel.setText(classifyFolderChooser.getAbsolutePath().toString());
				pathLabel.setToolTipText(classifyFolderChooser.getAbsolutePath().toString());
			}
		});
	}

	private void addToTable(String[] row, FileWriter output){
		DefaultTableModel tableModel = (DefaultTableModel) this.outputTable.getModel();
            // Add a row in table model
		tableModel.addRow(row);
        // Update the model of the table
		this.outputTable.setModel(tableModel);
        // Force it to update
		tableModel.fireTableDataChanged();
		try{
			output.write(row[0] + " " + row[1] + " " + row[2] + "\n");
		} catch(IOException e){}
	}

	public static void updateTotalWords(){
		totalWordsCount = ham.getWordCount() + spam.getWordCount();
		totalWordsLabel.setText("Total Words: " + Integer.toString(totalWordsCount));
	}
	public static void updateDictionarySize(){
		HashMap<String, Integer> tempHam = ham.getDict();
		HashMap<String, Integer> tempSpam = spam.getDict();
		// Initialize the count to the size of ham
		int count = tempHam.keySet().size();
		// Now, we do not care for the ham keys
		// Check the unique keys in spam
		for(String key : tempSpam.keySet()){
			if(!tempHam.containsKey(key)) count++;
		}
		dictionarySize = count;
		dictionarySizeLabel.setText("Dictionary Size: " + Integer.toString(dictionarySize));
			// The following code is not correct for the reason that it does not consider intersected keys
		// dictionarySize = ham.getDict().keySet().size() + spam.getDict().keySet().size();
		// dictionarySizeLabel.setText("Dictionary Size: " + Integer.toString(dictionarySize));
	}
}