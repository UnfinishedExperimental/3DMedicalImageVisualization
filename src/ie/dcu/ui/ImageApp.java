package ie.dcu.ui;

import javax.swing.*;

public class ImageApp {
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		/*
		 * If Nimbus is not available, stay with the default look and feel.
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ImageViewer.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		/* Create and display the swingUI */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ImageViewer().setVisible(true);
			}
		});
	}
}
