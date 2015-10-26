package com.limn.android.tool;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.android.ddmlib.RawImage;
import com.limn.tool.common.Print;

public class test extends JFrame {

	private ImageIcon ii = null;

	public test(RawImage rawScreen) {
		super("xxx");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int screenHeight = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		setBounds((int) ((screenWidth - 1000) * 0.5), (int) ((screenHeight - 600) * 0.5), 1000, 600);

		setLayout(null);

		if (rawScreen != null) {
			Boolean landscape = false;
			int width2 = landscape ? rawScreen.height : rawScreen.width;
			int height2 = landscape ? rawScreen.width : rawScreen.height;

			Print.log("width2:" + width2, 1);
			Print.log("height2:" + height2, 1);
			BufferedImage image = new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);

			int index = 0;
			int indexInc = rawScreen.bpp >> 3;
			for (int y = 0; y < rawScreen.height; y++) {
				for (int x = 0; x < rawScreen.width; x++, index += indexInc) {
					int value = rawScreen.getARGB(index);
					if (landscape) {
						image.setRGB(y, rawScreen.width - x - 1, value);
					} else {
						image.setRGB(x, y, value);
					}
				}
			}

//			BufferedImage image2 = new BufferedImage(width2/2 , height2/2 , BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(image.getScaledInstance(width2 , height2 , Image.SCALE_SMOOTH), 0, 0,
					null);

			ii = new ImageIcon(image);

			// ImageIO.write((RenderedImage) image, "PNG", new File(
			// "/Users/limengnan/Downloads/a.png"));

		}

		// ImageIcon ii1 = new
		// ImageIcon("/Users/limengnan/Downloads/690x420.jpg");
		// ii1.setImage(ii.getImage());

		JPanel jp = new JPanel();
		JLabel label = new JLabel();

		label.setIcon(ii);
		// label.setText("xxxxxxxxxxs");
		label.setBounds(0, 0, 1000, 600);
		jp.setLayout(null);
		jp.setBounds(0, 0, 1000, 600);

		jp.add(label);
		setContentPane(jp);
		// 基本设置

		setResizable(false);
		setVisible(true);

	}

}
