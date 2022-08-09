// Vartan Artyunyan
// Martikelnummer 5120007


import java.awt.image.BufferedImage;
import javax.swing.*;

public class Main {
	
	
	

	public static void main(String[] args) {
		
		Bergsteiger bg = new Bergsteiger();
		
		bg.findPath();
	
		
		Chain ch = new Chain();
		
		for(int i = 0; i < 10; i++) {
			ch.addBlock();
		}
		
		QTree qt = new QTree(1);
		
		Knoten root = qt.createTree("Bild.jpg");
		
					 	
		BufferedImage bi = qt.showLayer(root, 111);
			 	
			 	
		        ImageIcon imageIcon = new ImageIcon(bi);
		        JFrame jFrame = new JFrame();

		        
		        
		        jFrame.setSize(1024,1024);
		        JLabel jLabel = new JLabel();

		        jLabel.setIcon(imageIcon);
		        jFrame.add(jLabel);
		        jFrame.setVisible(true);

		        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		      
	}

}
