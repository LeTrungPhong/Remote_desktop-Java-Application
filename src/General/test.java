package General;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class test extends JFrame {
	
	public static void main(String[] args) throws IOException {
		try {
            // Sử dụng dịch vụ checkip.amazonaws.com để lấy IP công khai
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            
            String publicIP = br.readLine();
            System.out.println("Địa chỉ IP công khai của bạn là: " + publicIP);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
