package win;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PanelNegotiation extends JPanel{

	
public ArrayList<Object> CHROMOSOME = new ArrayList<>();
    
    public PanelNegotiation() {
    	setBounds(5, 5, 645, 500);
    	setBackground(Color.decode("#808b96"));
        
    }
        @Override
        	protected void paintComponent(final Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics;
                
                CHROMOSOME = AgentAfficheModelNegotiation.CITIES;
                if(CHROMOSOME != null) {
                	
                	drawBestChromosome(g);
                }
                repaint();
                
        	}

        private void drawBestChromosome(final Graphics2D g) {
            g.setColor(Color.black);

            for(int i = 0; i < CHROMOSOME.size(); i++) {
            	
            	City[] CT = (City[]) CHROMOSOME.get(i);
            	
            	for(int j=0 ; j<CT.length ; j++) {
            		if(j < CT.length - 1){
            			City gene = CT[j];
            			City neighbor = CT[j+1];
            			g.drawLine(gene.getX(), gene.getY(), neighbor.getX(), neighbor.getY());
            		}
            		else if(j == CT.length -1){
            			City gene = CT[j];
	           			City neighbor = CT[0];
	           			g.drawLine(gene.getX(), gene.getY(), neighbor.getX(), neighbor.getY());
           		 	}
            	}	
            }   
            g.setColor(Color.RED);
            
            for(int i=0 ; i<CHROMOSOME.size() ; i++) {
            	City[] Ct = (City[]) CHROMOSOME.get(i) ;
            	for(City gene: Ct) {
            		if(gene.verifieAgent() == 1){
                    	g.setColor(Color.decode("#566573"));
                        g.fillOval(gene.getX(), gene.getY(), 17, 17);
                    }
            		else if(gene.verifieAgent() == 2){
                    	g.setColor(Color.decode("#FA8072"));
                    	g.fillOval(gene.getX(), gene.getY(), 17, 17);
                    }
            	}
            }
        }

}
