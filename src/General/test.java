package General;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class test extends JFrame {
	
	public test() {
		
	}
	
	public static void main(String[] args) {
		 
		 JFileChooser fileChooser = new JFileChooser();
	     fileChooser.setDialogTitle("Chọn nơi lưu ảnh");
	}
}
