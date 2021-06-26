package win;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Environnement extends JPanel implements MouseListener{
    public  ArrayList<City> cities = new ArrayList<>();
    
    public ArrayList<City> listAgents = new ArrayList<>();
    public static int nbListAgent = 0; 
    public static int w = 1;
    int nom = 0;
    
    public Environnement(){
        setBounds(5, 5, 850, 520);
       // setBackground(Color.decode("#A5DCDC"));

        setBackground(Color.decode("#808b96"));
        addMouseListener(this);
    }
    @Override
    protected void paintComponent(Graphics g) {
    	
    	
        super.paintComponent(g); 
        if(cities.size()>= 0){
            for(int i=0; i<cities.size() ; i++){
                //if(cities.get(i).verifieAgent() == 1){
                    //g.setColor(Color.red);
            	g.setColor(Color.decode("#566573"));
                    g.fillOval(cities.get(i).getX(), cities.get(i).getY(), 17, 17);
                  //  }
                //else {
                    //g.setColor(Color.green);
                  //  g.fillOval(cities.get(i).getX(), cities.get(i).getY(), 10, 10);
                //}
                    ff(g);
            }
            
        }
        repaint();
    }
    
    public void ff(Graphics g) {
    	for(int i=0; i<listAgents.size() ; i++){
    		//getGraphics().setColor(Color.green);
    		g.setColor(Color.decode("#FA8072"));
    		g.fillOval(listAgents.get(i).getX(), listAgents.get(i).getY(), 17, 17);
    	}
    }
    

    @Override
    public void mousePressed(MouseEvent e) {
        
        if(w == 1){
            City ct = new City(e.getX(), e.getY(), "v"+nom,1);
            nom++;
            cities.add(ct);
        }else if(w == 2){
            City ct = new City(e.getX(), e.getY(),"a"+nom ,2);
            //cities.add(ct);
            listAgents.add(ct);
            nbListAgent++;
            nom++;
        }
        
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
}
