import java.awt.*;
import com.jhlabs.awt.*;
import javax.swing.*;

public class LayoutTest {

	public static void main(String[] args) {
		paragraphLayout();
		packerLayout();
		gridLayoutPlus();
		basicGridLayout();
		clockLayout();
	}
	
	public static void paragraphLayout() {
		JFrame jf = new JFrame("ParagraphLayout");
		Container f = jf.getContentPane();
		f.setLayout(new ParagraphLayout());
		JButton b1 = new JButton("One");
		JButton b2 = new JButton("Two");
		JButton b3 = new JButton("Three");
		JButton b4 = new JButton("Four");
		JButton b5 = new JButton("Five");
		JButton b6 = new JButton("Six");
		JButton b7 = new JButton("Seven");
		JButton b8 = new JButton("Eight");
		JTextField t1 = new JTextField(4);
		JTextField t2 = new JTextField(20);
		JTextArea t3 = new JTextArea(5, 30);

		b2.setFont(new Font("serif", Font.PLAIN, 24));
		f.add(new JLabel("Some buttons:"), ParagraphLayout.NEW_PARAGRAPH);
		f.add(b1);
		f.add(new JLabel("A long label:"), ParagraphLayout.NEW_PARAGRAPH);
		f.add(b2);
		f.add(b3);
		f.add(new JLabel("Short label:"), ParagraphLayout.NEW_PARAGRAPH);
		f.add(b4);
		f.add(b5, ParagraphLayout.NEW_LINE);
		f.add(b6);
		f.add(b7);
		f.add(b8, ParagraphLayout.NEW_LINE);
		f.add(new JLabel("Text:"), ParagraphLayout.NEW_PARAGRAPH);
		f.add(t1);
		f.add(new JLabel("More text:"), ParagraphLayout.NEW_PARAGRAPH);
		f.add(t2);
		f.add(new JLabel("miles"));
		f.add(new JLabel("A text area:"), ParagraphLayout.NEW_PARAGRAPH_TOP);
		f.add(t3);
		jf.pack();
		jf.show();
	}

	public static void packerLayout() {
		JFrame jf = new JFrame("PackerLayout");
		Container f = jf.getContentPane();
		f.setLayout(new PackerLayout());
		JButton b1 = new JButton("One");
		JButton b2 = new JButton("Two");
		JButton b3 = new JButton("Three");
		JButton b4 = new JButton("Four");
		JButton b5 = new JButton("Five");
		JButton b6 = new JButton("Six");

		b2.setFont(new Font("serif", Font.PLAIN, 24));
		f.add(b1);
		f.add(b2, PackerLayout.LEFT_CENTER);
		f.add(b3, PackerLayout.BOTTOM_CENTER_FILL);
		f.add(b4, PackerLayout.TOP_CENTER_FILL);
		f.add(b5, PackerLayout.TOP_LEFT);
		f.add(b6, PackerLayout.RIGHT_CENTER);
		jf.pack();
		jf.show();
	}

	public static void gridLayoutPlus() {
		JFrame jf = new JFrame("GridLayoutPlus");
		Container f = jf.getContentPane();
		GridLayoutPlus glp = new GridLayoutPlus(0, 3, 10, 10);
		glp.setColWeight(1, 2);
		glp.setColWeight(2, 1);
		glp.setRowWeight(2, 1);
		f.setLayout(glp);
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 3; c++) {
				f.add(new JButton(r+","+c));
			}
		}
		jf.pack();
		jf.show();
	}

	public static void basicGridLayout() {
		JFrame jf = new JFrame("BasicGridLayout");
		Container f = jf.getContentPane();
		BasicGridLayout l = new BasicGridLayout(0, 3, 10, 10);
		l.setColWeight(1);
		l.setRowWeight(1);
		l.setIncludeInvisible(false);
		f.setLayout(l);
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 3; c++) {
				JButton b;
				f.add(b = new JButton(r+","+c));
				b.setVisible((r+c)%4 != 0);
			}
		}
		jf.pack();
		jf.show();
	}

	public static void clockLayout() {
		JFrame jf = new JFrame("ClockLayout");
		Container f = jf.getContentPane();
		f.setLayout(new ClockLayout());
		for (int r = 0; r < 12; r++) {
			f.add(new JButton(r+""));
		}
		jf.pack();
		jf.show();
	}

}
