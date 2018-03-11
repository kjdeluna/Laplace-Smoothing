import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main{
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = (int) SCREEN_SIZE.getWidth() - 75;
	public static final int HEIGHT = (int) SCREEN_SIZE.getHeight() - 75;
	// public static final int WIDTH = 720;
	// public static final int HEIGHT = 360;
	public static void main(String[] args){
		// Create frame
		JFrame frame = new JFrame("Bag Of Words");
		BagOfWords spam = new BagOfWords();
		BagOfWords ham = new BagOfWords();
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SolutionPanel sp = new SolutionPanel(ham, spam);
		frame.add(sp);
		frame.pack();
		frame.setVisible(true);
    }
}