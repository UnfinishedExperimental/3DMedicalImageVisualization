package ie.dcu.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ie.dcu.model.MCPolygons;
import ie.dcu.process.ImageProcessUtil;

public class ImageViewer extends JFrame {
	private static final long serialVersionUID = 1L;
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
	private File rawFileFolder;
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
		
		mcButton = new JButton("Start Marching Cube Processing.");
		mcButton.setEnabled(false);
		mcButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				initMarchingCuberActionPerformed(evt);
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
		// create file chooser to select medical images
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(imageMenu) == JFileChooser.APPROVE_OPTION) {
			// get selected image files
			fileSelections = chooser.getSelectedFiles();
			ImageConstants.SLIDE_MAX = fileSelections.length;
			sliceDataSaved = new ArrayList[fileSelections.length];
			Arrays.sort(fileSelections, new Comparator<File>() {
		        public int compare(File f1, File f2) {
					return Integer.parseInt(f1.getName())-Integer.parseInt(f2.getName());
		        }
			});
			imageSlider.setMaximum(ImageConstants.SLIDE_MAX - 1);
			ImageProcessUtil.gridSlicesData = new int[ImageConstants.DIMENTION][ImageConstants.DIMENTION][fileSelections.length];
			long start = System.currentTimeMillis();
			for (int i = 0; i < fileSelections.length; i++) {
				imageProcess.imageFileProcess(false, i, fileSelections[i].toString());
			}
			long end = System.currentTimeMillis();
			System.out.println("Time taken : " + (end - start));
/*			 System.out.println(Arrays.deepToString(ImageProcessUtil.gridSlicesData));
			for (int a = 0; a < 512; a++) {
				for (int b = 0; b < 512; b++) {
					for (int j = 0; j < fileSelections.length; j++) {
						System.out.println(ImageProcessUtil.gridSlicesData[a][b][j]);
					}
				}
			}*/
			MCPolygons mc = new MCPolygons();
			mc.initiateMCProcess(imageProcess.gridSlicesData, fileSelections.length);
			// JOptionPane.showMessageDialog(null, "Data is written in the array");
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

	public void initMarchingCuberActionPerformed(ActionEvent evt) {// GEN-FIRST:event_clearImageAreaActionPerformed
		File[] rawFilesSorted = rawFileFolder.listFiles();
		Arrays.sort(rawFilesSorted, new Comparator<File>() {
	        public int compare(File f1, File f2) {
				return Integer.parseInt(f1.getName())-Integer.parseInt(f2.getName());
	        }
		}); 
	}

	public void clearImageAreaActionPerformed(ActionEvent evt) {// GEN-FIRST:event_clearImageAreaActionPerformed
		jlab.setIcon(null);
	}
}
