package cviceni2;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;

import ij.ImagePlus;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWindow {
	private int q;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	 private void initialize() {
 		ImagePlus originalImage = new ImagePlus("lena_std.jpg");
 		Process process = new Process(originalImage);
 		//process.showOriginal();


 		frame = new JFrame();
 		frame.setBounds(100, 100, 725, 529);
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 		JMenuBar menuBar = new JMenuBar();
 		frame.setJMenuBar(menuBar);

 		JMenu mnFile = new JMenu("File");
 		menuBar.add(mnFile);

 		JMenuItem mntmClose = new JMenuItem("close");
 		mnFile.add(mntmClose);
 		frame.getContentPane().setLayout(null);

 		JButton redButton = new JButton("RED");
 		redButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.showColor("Red");
 			}
 		});
 		redButton.setBounds(82, 23, 89, 23);
 		frame.getContentPane().add(redButton);

 		JButton greenButton = new JButton("GREEN");
 		greenButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.showColor("Green");
 			}
 		});
 		greenButton.setBounds(181, 23, 89, 23);
 		frame.getContentPane().add(greenButton);

 		JButton blueButton = new JButton("BLUE");
 		blueButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.showColor("Blue");
 			}
 		});
 		blueButton.setBounds(280, 23, 89, 23);
 		frame.getContentPane().add(blueButton);

 		JButton yButton = new JButton("Y");
 		yButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.showSlozka("Y");
 			}
 		});
 		yButton.setBounds(82, 69, 89, 23);
 		frame.getContentPane().add(yButton);

 		JButton cbButton = new JButton("Cb");
 		cbButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.showSlozka("Cb");
 			}
 		});
 		cbButton.setBounds(181, 69, 89, 23);
 		frame.getContentPane().add(cbButton);

 		JButton crButton = new JButton("Cr");
 		crButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.showSlozka("Cr");
 			}
 		});
 		crButton.setBounds(280, 69, 89, 23);
 		frame.getContentPane().add(crButton);

 		JButton butt422 = new JButton("4:2:2");
 		butt422.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.downsample(8);
 			}
 		});
 		butt422.setBounds(181, 137, 89, 23);
 		frame.getContentPane().add(butt422);

 		JButton butt420 = new JButton("4:2:0");
 		butt420.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.downsample(9);
 			}
 		});
 		butt420.setBounds(280, 137, 89, 23);
 		frame.getContentPane().add(butt420);

 		JButton butt411 = new JButton("4:1:1");
 		butt411.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.downsample(10);
 			}
 		});
 		butt411.setBounds(381, 137, 89, 23);
 		frame.getContentPane().add(butt411);

 		JButton butt444 = new JButton("4:4:4");
 		butt444.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.downsample(7);
 			}
 		});
 		butt444.setBounds(82, 137, 89, 23);
 		frame.getContentPane().add(butt444);

 		JButton overSamButton = new JButton("OverSample");
 		overSamButton.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.oversample();
 			}
 		});
 		overSamButton.setBounds(172, 185, 111, 23);
 		frame.getContentPane().add(overSamButton);

 		JTextPane mseField = new JTextPane();
 		mseField.setText("0");
 		mseField.setBounds(145, 241, 101, 23);
 		frame.getContentPane().add(mseField);

 		JTextPane psnrField = new JTextPane();
 		psnrField.setText("0");
 		psnrField.setBounds(268, 241, 101, 23);
 		frame.getContentPane().add(psnrField);




 		JButton btnQuality = new JButton("Quality");
 		btnQuality.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				mseField.setText(Double.toString(process.calculateMSE()));
 				psnrField.setText(Double.toString(process.calculatePsnr()));
 			}
 		});
 		btnQuality.setBounds(29, 241, 89, 23);
 		frame.getContentPane().add(btnQuality);



 		JLabel lblMse = new JLabel("MSE");
 		lblMse.setBounds(145, 219, 46, 14);
 		frame.getContentPane().add(lblMse);

 		JLabel lblPsnr = new JLabel("Psnr");
 		lblPsnr.setBounds(266, 219, 46, 14);
 		frame.getContentPane().add(lblPsnr);
 		
 		JButton transform44 = new JButton("4x4");
 		transform44.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent arg0) {
 				process.transformation(1);
 			}
 		});
 		transform44.setBounds(29, 313, 89, 23);
 		frame.getContentPane().add(transform44);
 		
 		JButton transform88 = new JButton("8x8");
 		transform88.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.transformation(2);
 			}
 		});
 		transform88.setBounds(145, 313, 89, 23);
 		frame.getContentPane().add(transform88);
 		
 		JButton transform1616 = new JButton("16x16");
 		transform1616.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.transformation(3);
 			}
 		});
 		transform1616.setBounds(259, 313, 89, 23);
 		frame.getContentPane().add(transform1616);
 		
 		JButton detrans44 = new JButton("4x4");
 		detrans44.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.transformation(4);
 			}
 		});
 		detrans44.setBounds(29, 363, 89, 23);
 		frame.getContentPane().add(detrans44);
 		
 		JButton detrans88 = new JButton("8x8");
 		detrans88.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.transformation(5);
 			}
 		});
 		detrans88.setBounds(145, 363, 89, 23);
 		frame.getContentPane().add(detrans88);
 		
 		JButton btnDetransX = new JButton("16x16");
 		btnDetransX.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent e) {
 				process.transformation(6);
 			}
 		});
 		btnDetransX.setBounds(259, 363, 89, 23);
 		frame.getContentPane().add(btnDetransX);
 		
 		JSlider kvant_slider = new JSlider();
 		kvant_slider.addChangeListener(new ChangeListener() {
 			public void stateChanged(ChangeEvent arg0) {
 				q = kvant_slider.getValue();
 			}
 		});
 		kvant_slider.setMinimum(1);
 		kvant_slider.setBounds(420, 185, 200, 26);
 		frame.getContentPane().add(kvant_slider);
 		
 		JButton btnVsetko = new JButton("Q - v\u0161e");
 		btnVsetko.addActionListener(new ActionListener() {
 			public void actionPerformed(ActionEvent arg0) {
 				process.all(q);
 			}
 		});
 		btnVsetko.setBounds(145, 412, 89, 23);
 		frame.getContentPane().add(btnVsetko);
 		
 		JLabel label = new JLabel("50");
 		label.setBounds(514, 219, 21, 14);
 		frame.getContentPane().add(label);
 		
 		JLabel label_1 = new JLabel("100");
 		label_1.setBounds(603, 219, 46, 14);
 		frame.getContentPane().add(label_1);
 		
 		JLabel label_2 = new JLabel("0");
 		label_2.setBounds(420, 219, 12, 14);
 		frame.getContentPane().add(label_2);




 		

	}
}