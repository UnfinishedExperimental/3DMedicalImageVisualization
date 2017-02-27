package ie.dcu.visualisation;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** This class is an entry point of the simple image viewer.
 */
public class ImageViewer extends JFrame {
    // Variables declaration
    private JMenuItem clearImageArea;
    private JMenuItem openImageExplorer;
    private JMenu imageMenu;
    private JMenuBar imageMenuBar;
    private JScrollPane imageScroller;
    private JSlider imageSlider;
    private File[] fileSelections;
    //create a label
    JLabel jlab = new JLabel();
    // Slider variables
    static int SLIDE_MIN = 0;
    static int SLIDE_MAX = 100;
    static int SLIDE_INIT = 0;
    // End of variables declaration
    /**
     * ImageViewer constructor which initialize the image frame with components
     */
    public ImageViewer() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
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
        //Image slider.
        imageSlider = new JSlider(JSlider.HORIZONTAL, SLIDE_MIN, SLIDE_MAX, SLIDE_INIT);
        imageSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
            	changeSliderActionPerformed(evt);
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

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(imageScroller, GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
                    .addComponent(imageSlider, GroupLayout.PREFERRED_SIZE, 1200, GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                    .addContainerGap(25, Short.MAX_VALUE)
                    .addComponent(imageScroller, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGap(50, 50, 50)
                    .addComponent(imageSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
            );
        pack();
    }

    private void changeSliderActionPerformed(ChangeEvent evt) {
    	int imageSliderNum = imageSlider.getValue();
    	System.out.println("Max Value: " + SLIDE_MAX + " and value here: " + imageSliderNum);
    	ImageIcon imageIcon = new ImageIcon(fileSelections[imageSliderNum].toString());
    	scaleAndSetImage(imageIcon);
	}
    
    private void openImageExplorerActionPerformed(ActionEvent evt) {
        //create file chooser to select medical images
    	JFileChooser chooser = new JFileChooser();
    	chooser.setMultiSelectionEnabled(true);
        if(chooser.showOpenDialog(imageMenu) == JFileChooser.APPROVE_OPTION){
            //get selected image files
            fileSelections = chooser.getSelectedFiles();
            SLIDE_MAX = fileSelections.length;
            imageSlider.setMaximum(SLIDE_MAX-1);
            ImageIcon imageIcon = new ImageIcon(fileSelections[0].toString()); // load the image to a imageIcon
            scaleAndSetImage(imageIcon);
        }
    }

	private void scaleAndSetImage(ImageIcon imageIcon) {
		Image image = imageIcon.getImage(); // transform it 
		if(image.getWidth(null) > 1200 && image.getHeight(null)> 600) {
			Image newimg = image.getScaledInstance(1100, 640,  Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
		}
		//set icon
		jlab.setIcon(imageIcon);
		//alignment
		jlab.setHorizontalAlignment(JLabel.CENTER);
		//add jLabel to scroll pane
		imageScroller.getViewport().add(jlab);
	}

    private void clearImageAreaActionPerformed(ActionEvent evt) {//GEN-FIRST:event_clearImageAreaActionPerformed
        jlab.setIcon(null);
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        /* If Nimbus is not available, stay with the default look and feel.
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the swingUI */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ImageViewer().setVisible(true);
            }
        });
    }

}