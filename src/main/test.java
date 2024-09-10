package main;

import java.awt.Dimension;
import java.awt.Toolkit;

public class test {
	
	public test() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println("height: " + screenSize.height + ", width : " + screenSize.width);
	}
	
	public static void main(String[] args) {
		new test();
	}
}
