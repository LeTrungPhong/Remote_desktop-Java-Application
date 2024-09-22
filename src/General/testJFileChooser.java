package General;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class testJFileChooser extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testJFileChooser frame = new testJFileChooser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public testJFileChooser() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		int width = 400;
        int height = 300;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Điền hình ảnh với màu xanh dương (RGB code: 0x0000FF)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, 0x0000FF); // Màu xanh dương
            }
        }

        // Tạo FileDialog để mở giao diện lưu file của hệ điều hành
        FileDialog fileDialog = new FileDialog(this, "Chọn nơi lưu ảnh", FileDialog.SAVE);

        // Hiển thị FileDialog
        fileDialog.setVisible(true);

        // Kiểm tra xem người dùng có chọn file không
        String directory = fileDialog.getDirectory();
        String fileName = fileDialog.getFile();

        if (directory != null && fileName != null) {
            // Tạo đối tượng file từ đường dẫn và tên file mà người dùng chọn
            File fileToSave = new File(directory, fileName + ".png");

            try {
                // Ghi ảnh dưới định dạng PNG
                ImageIO.write(image, "png", fileToSave);
                JOptionPane.showMessageDialog(null, "Ảnh đã được lưu thành công!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi lưu ảnh!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Người dùng đã hủy thao tác lưu.");
        }

		setContentPane(contentPane);
	}

}
