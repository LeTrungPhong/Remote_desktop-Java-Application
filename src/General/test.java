package General;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class test extends JFrame {
	
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("deprecation")
		Process process = Runtime.getRuntime().exec("powershell \"Get-Process "
				+ "| Where-Object { $_.MainWindowTitle -ne '' } "
				+ "| Select-Object ProcessName\"");
         
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        
        while ((line = reader.readLine()) != null) {
        	System.out.println(line);
        }
	}
}
