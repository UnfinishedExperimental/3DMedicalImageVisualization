package ie.dcu.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ie.dcu.process.ImageProcessUtil;
import ie.dcu.process.MCPolygons;

public class ImageViewer extends JFrame {
	private static final long serialVersionUID = 1L;
	public boolean running = false;			// Is animation running?
	public Timer timer;						// Chart update timer
	// Variables declaration
	private JMenuItem clearImageArea;
	private JMenuItem openImageExplorer;
	private JMenu imageMenu;
	private JMenuBar imageMenuBar;
	private JButton animButton;
	private JScrollPane imageScroller;
	private JSlider imageSlider;
	private JButton mcButton;
	private File[] fileSelections;
	// create a label
	JLabel jlab = new JLabel();
	// Path of input raw file data
	List<File> rawFilesSorted;
	ImageProcessUtil imageProcess = new ImageProcessUtil();
	ArrayList<String> sliceDataSaved[];
	/**
	 * ImageViewer constructor which initialize the image frame with components
	 */
	public ImageViewer() {
		initComponents();
	}

	private void initComponents() {
		imageScroller = new JScrollPane();
		imageMenuBar = new JMenuBar();
		imageMenu = new JMenu();
		openImageExplorer = new JMenuItem();
		clearImageArea = new JMenuItem();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		imageMenu.setText("File");
		openImageExplorer.setText("Open");
		openImageExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				openImageExplorerActionPerformed(evt);
			}


		});
		// Image slider.
		imageSlider = new JSlider(JSlider.HORIZONTAL, ImageConstants.SLIDE_MIN, ImageConstants.SLIDE_MAX,
				ImageConstants.SLIDE_INIT);
		imageSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				changeSliderActionPerformed(evt);
			}

			private void changeSliderActionPerformed(ChangeEvent evt) {
				int imageSliderNum = imageSlider.getValue();
				//System.out.println("-------------" + imageSliderNum);
				// System.out.println("Max Value: " + ImageConstants.SLIDE_MAX + " and
				// value here: " + imageSliderNum);
				ImageIcon imageIcon = imageProcess.imageFileProcess(true, imageSliderNum, fileSelections[imageSliderNum].toString());
				scaleAndSetImage(imageIcon);
			}


		});

		animButton = new JButton("Slideshow");
		animButton.setEnabled(false);
		animButton.setFocusable(false);
		animButton.setBackground(new Color(200,240,200));
		animButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				slideShowPerformed(evt);
			}
		});
		timer = new Timer(300, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 1; i < fileSelections.length; i++) {
					ImageIcon imageIcon = imageProcess.imageFileProcess(true, (i + 1), fileSelections[i].toString());
					scaleAndSetImage(imageIcon);
				}
			}
		}); // end timer construction
		
		mcButton = new JButton("Open .CTM file");
		mcButton.setEnabled(false);
		mcButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Desktop desktop = Desktop.getDesktop();
				String currentDir = System.getProperty("user.dir");
				try {
					desktop.open(new File(currentDir));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		imageMenu.add(openImageExplorer);
		clearImageArea.setText("Clear");
		clearImageArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clearImageAreaActionPerformed(evt);
			}
		});
		imageMenu.add(clearImageArea);
		imageMenuBar.add(imageMenu);
		setJMenuBar(imageMenuBar);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(animButton).addComponent(mcButton)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(imageScroller, GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE).addComponent(
								imageSlider, GroupLayout.PREFERRED_SIZE, 1200, GroupLayout.PREFERRED_SIZE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(animButton)
						.addComponent(mcButton).addGroup(GroupLayout.Alignment.LEADING,
								layout.createSequentialGroup().addContainerGap(25, Short.MAX_VALUE)
										.addComponent(imageScroller, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
										.addGap(50, 50, 50).addComponent(imageSlider, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(25, Short.MAX_VALUE))));

		pack();
	}
	
	public void openImageExplorerActionPerformed(ActionEvent evt) {
		String currentDir = System.getProperty("user.dir");
		// create file chooser to select medical images
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(imageMenu) == JFileChooser.APPROVE_OPTION) {
			// get selected image files
			fileSelections = chooser.getSelectedFiles();
			ImageConstants.SLIDE_MAX = fileSelections.length;
			Arrays.sort(fileSelections, new Comparator<File>() {
		        public int compare(File f1, File f2) {
					return Integer.parseInt(f1.getName())-Integer.parseInt(f2.getName());
		        }
			});
			String[] pathDataFolder = chooser.getCurrentDirectory().toString().split("/");
			String dataFolder = pathDataFolder[pathDataFolder.length-1];
			if(dataFolder == ImageConstants.BUNNY_DATA_CT) {
				ImageConstants.ROWS = 512;
				ImageConstants.COLUMNS = 512;
			} else if(dataFolder == ImageConstants.HEAD_DATA_CT) {
				ImageConstants.ROWS = 256;
				ImageConstants.COLUMNS = 256;
			}
			imageSlider.setMaximum(ImageConstants.SLIDE_MAX - 1);
			imageProcess.imageFileProcess(true, 0, fileSelections[0].toString());
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(this, "Would you like to create the obj file ? (This will take a while)", "Generate Obj", dialogButton);
			if(dialogResult == 0) {
				MCPolygons marchingCube= new MCPolygons();
				marchingCube.initiateMCProcess(fileSelections, currentDir, pathDataFolder[pathDataFolder.length-1], true);
			} 
		}
		animButton.setEnabled(true);
		mcButton.setEnabled(true);
	}

	public void scaleAndSetImage(ImageIcon imageIcon) {
		Image image = imageIcon.getImage(); // transform it
		if (image.getWidth(null) > 1200 && image.getHeight(null) > 600) {
			Image newimg = image.getScaledInstance(1100, 640, Image.SCALE_SMOOTH); // scale
																					// it
																					// the
																					// smooth
																					// way
			imageIcon = new ImageIcon(newimg); // transform it back
		}
		// set icon
		jlab.setIcon(imageIcon);
		// alignment
		jlab.setHorizontalAlignment(JLabel.CENTER);
		// add jLabel to scroll pane
		imageScroller.getViewport().add(jlab);
	}


	public void clearImageAreaActionPerformed(ActionEvent evt) {// GEN-FIRST:event_clearImageAreaActionPerformed
		jlab.setIcon(null);
	}
	
	public void slideShowPerformed(ActionEvent evt) {// event_slideShowPerformed
		if (running == true) {
			running = false;
			animButton.setBackground(new Color(200, 240, 200));
			animButton.setText("RUN");
			timer.stop();
		} else {
			running = true;
			animButton.setBackground(new Color(240, 200, 200));
			animButton.setText("STOP");
			timer.start();
		}

	}
}
