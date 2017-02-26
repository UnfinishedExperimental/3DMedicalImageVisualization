package ie.dcu.visualisation;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

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
    //create a label
    JLabel jlab = new JLabel();
    // Slider variables
    static int SLIDE_MIN = 0;
    static int SLIDE_MAX = 30;
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
      
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        imageMenu.setText("File");
        openImageExplorer.setText("Open");
        openImageExplorer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openImageExplorerActionPerformed(evt);
            }
        });
        //Image slider.
        imageSlider = new JSlider(JSlider.HORIZONTAL, SLIDE_MIN, SLIDE_MAX, SLIDE_INIT);
        imageMenu.add(openImageExplorer);
        clearImageArea.setText("Clear");
        clearImageArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearImageAreaActionPerformed(evt);
            }
        });
        imageMenu.add(clearImageArea);
        imageMenuBar.add(imageMenu);
        setJMenuBar(imageMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(25, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(imageScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
                        )
                    .addComponent(imageSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    )
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(25, Short.MAX_VALUE)
                    .addComponent(imageScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGap(143, 143, 143))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(imageSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    )
            );
        pack();
    }

    private void openImageExplorerActionPerformed(java.awt.event.ActionEvent evt) {
        //create file chooser to select medical images
    	JFileChooser chooser = new JFileChooser();
    	chooser.setMultiSelectionEnabled(true);
        if(chooser.showOpenDialog(imageMenu) == JFileChooser.APPROVE_OPTION){
            //get selected image files
            File[] fileSelections = chooser.getSelectedFiles();
            SLIDE_MAX = fileSelections.length;
            for(File f : fileSelections) {
            	System.out.println(f.getName());
            }
            ImageIcon imageIcon = new ImageIcon(fileSelections[0].toString()); // load the image to a imageIcon
            Image image = imageIcon.getImage(); // transform it 
            if(image.getWidth(null) > 1200 && image.getHeight(null)> 600) {
            	Image newimg = image.getScaledInstance(1100, 640,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
            	imageIcon = new ImageIcon(newimg);  // transform it back
            }
            //set icon
            jlab.setIcon(imageIcon);
            //alignment
            jlab.setHorizontalAlignment(JLabel.CENTER);
            //add jLabel to scroll pane
            imageScroller.getViewport().add(jlab);
            //imageScroller.getViewport().add(imageSlider);
        }
    }

    private void clearImageAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearImageAreaActionPerformed
        jlab.setIcon(null);
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        /* If Nimbus is not available, stay with the default look and feel.
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
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