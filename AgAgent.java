package win;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgAgent extends Agent{
	
    private Population population;
    private AtomicInteger generation;

    AlgoGenetique ag;
    
    public City[] E;
    public int index;
    public City[] listvilleagents;
    public City[] myCity = new City[1000];
    public int nbMyVille;
    
    int nbville = 0;
    int nbagent = 0;
    
    @Override
    protected void setup() {
        Object[] args = getArguments();
        E = (City[])args[0];
        index = (int)args[1];
        listvilleagents = (City[])args[2];  
        
        nbville = E.length;
        nbMyVille=nbville;
        nbagent = listvilleagents.length;
        
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
            	ArrayList<City> arrayIntermediare = new ArrayList<>();

            	int i = 0;
            	arrayIntermediare.add(new City(listvilleagents[index].getX(),listvilleagents[index].getY(),listvilleagents[index].getnom(),listvilleagents[index].verifieAgent()));
            	chargeMyVille();
            	
            	while (myCity[i] != null) {
					
            		arrayIntermediare.add(new City(myCity[i].getX() ,myCity[i].getY() ,myCity[i].getnom() ,myCity[i].verifieAgent() ));
            		i++;
    				}
            	City[] localCity = new City[arrayIntermediare.size()];
            	for(int j=0;j<arrayIntermediare.size();j++) {
            		int  x = arrayIntermediare.get(j).getX();
            		int y = arrayIntermediare.get(j).getY();
            		String nom = arrayIntermediare.get(j).getnom();
            		int ag = arrayIntermediare.get(j).verifieAgent();
            		
            		localCity[j] = new City(x,y,nom,ag);
            	}
            	//fffffffff.toArray(new City[0]);
           /*ACLMessage message= new ACLMessage(ACLMessage.INFORM);
message.addReceiver(new AID("Agent2", AID.ISLOCALNAME));
message.setContent("Today it’s raining");
send(msg);*/
            	
            	//Applique AG
            	initArguments(localCity);
            	City[] CityFinal =  algoGen();            	
            	ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            	msg.addReceiver(new AID("AgentAfficher", AID.ISLOCALNAME));
            	try {
					msg.setContentObject(CityFinal);
				} catch (IOException e) {
					e.printStackTrace();
				}
            	send(msg);

            
            }
        });
    }
    
 public void supprimerVille() {
	 int j=0;
	 while(j<nbMyVille) {
		 if(myCity[j].noteMyVille) {
			 for(int pos=j;pos<nbMyVille-1;pos++) {
				 myCity[pos]=myCity[pos+1];
			 }
			 myCity[nbMyVille-1]=null;
			 nbMyVille--;
		 }
		 else j++;
	 
	 }
 }
 
 public void chargeMyVille() {

    	
    	double mydistance;
        
    	
        //creation d'une copie locale de liste des villes
        for(int i=0;i<nbville;i++) {
        	myCity[i]=new City(E[i].getX(),E[i].getY(),E[i].getnom(),E[i].verifieAgent());
        	// System.out.print("agent"+index+"  "+myCity[i]+"  ");
        }
        System.out.println();
        //System.out.println("agent"+index+"  dans la ville "+listvilleagents[index].getnom());
    	
        for(int i=0;i<nbville;i++) {
    		
    		mydistance = Math.sqrt(Math.pow(listvilleagents[index].getX() - E[i].getX(), 2) + Math.pow(listvilleagents[index].getY() - E[i].getY(), 2));
    		int j=0;
    		boolean poursuite=true;
    		
    		while(j<nbagent && poursuite ) {
    			if(j != index) {
    			
    			double f = Math.sqrt(Math.pow(listvilleagents[j].getX() - E[i].getX(), 2) + Math.pow(listvilleagents[j].getY() - E[i].getY(), 2));
    			if(f<mydistance) {
    				myCity[i].noteMyVille=true;
    				
    				poursuite=false;
    			}
    			
    			}
    			j++;
    		}
    	}
        supprimerVille();
   }
    
    
    public City[] chemain(int taille) {
    
    	City[] sendCity = new City[taille];
    
    	final java.util.List<City> chromosome = this.population.getAlpha().getChromosome();
      //  g.setColor(Color.black);
        for(int i = 0; i < chromosome.size(); i++) {
        	
        	int x = chromosome.get(i).getX();
        	int y = chromosome.get(i).getY();
        	String nom = chromosome.get(i).getnom();
        	int verifyAgent = chromosome.get(i).verifieAgent();
        
        	sendCity[i] = new City(x ,y ,nom ,verifyAgent);
        	}
        return sendCity;
    }
    
   
    public void initArguments(City[] data) {
    	ag=new  AlgoGenetique(data);
        this.population = new Population(ag,ag.CITIES, 9000);
        this.generation = new AtomicInteger(0);
        
        try {
			Thread.sleep(3);
			this.population.update();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public City[] algoGen(){
    	final List<City> chromosome = this.population.getAlpha().getChromosome();
    	if(this.generation.incrementAndGet() <= 40) {
            for(int i = 0; i < chromosome.size(); i++) {
                if(i < chromosome.size() - 1){
                    City gene = chromosome.get(i);
                    City neighbor = chromosome.get(i + 1);
//                    g.drawLine(gene.getX(), gene.getY(), neighbor.getX(), neighbor.getY());
                }
                else if(i == chromosome.size() -1){
                    City gene = chromosome.get(i);
                    City neighbor = chromosome.get(0);
//                    g.drawLine(gene.getX(), gene.getY(), neighbor.getX(), neighbor.getY());
                }
            }
            // fin for
            
    		
    	}
    	return chromosome.toArray(new City[0]);
    }
    
    
    
    
}