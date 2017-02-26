import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/** This class is an entry point of the simple image viewer.
 */
public class ImageViewer extends javax.swing.JFrame {
    // Variables declaration
    private javax.swing.JMenuItem clearImageArea;
    private javax.swing.JMenuItem openImageExplorer;
    private javax.swing.JMenu imageMenu;
    private javax.swing.JMenuBar imageMenuBar;
    private javax.swing.JScrollPane imageScroller;
    //create a label
    JLabel jlab = new JLabel();
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
        imageScroller = new javax.swing.JScrollPane();
        imageMenuBar = new javax.swing.JMenuBar();
        imageMenu = new javax.swing.JMenu();
        openImageExplorer = new javax.swing.JMenuItem();
        clearImageArea = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        imageMenu.setText("File");
        openImageExplorer.setText("Open");
        openImageExplorer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openImageExplorerActionPerformed(evt);
            }
        });
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
            .addComponent(imageScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
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
            for(File f : fileSelections) {
            	System.out.println(f.getName());
            }
            
            //set icon
            jlab.setIcon(new ImageIcon(fileSelections[0].toString()));
            //alignment
            jlab.setHorizontalAlignment(JLabel.CENTER);
            //add jLabel to scroll pane
            imageScroller.getViewport().add(jlab);
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