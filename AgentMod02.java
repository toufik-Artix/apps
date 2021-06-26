package win;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentMod02 extends Agent{
	
	private Population population;
    private AtomicInteger generation;
    AlgoGenetique ag;
	
	
	ArrayList<City> ArCity = new ArrayList<>();
	public AgentDistance dist;

	@Override
	protected void setup() {
		FSMBehaviour fsm = new FSMBehaviour(this);
		fsm.registerState(new AgentMod03_1(), "STATE_1");
		fsm.registerState(new AgentMod03_2(), "STATE_2");
		fsm.registerState(new AgentMod03_3(), "STATE_3");
		fsm.registerState(new AgentMod03_4(), "STATE_4");
		
		
		fsm.registerTransition("STATE_1", "STATE_1",1);
		fsm.registerTransition("STATE_1", "STATE_2",0);
		fsm.registerTransition("STATE_2", "STATE_3",0);
		fsm.registerTransition("STATE_3", "STATE_4",0);
		fsm.registerTransition("STATE_4", "STATE_4",1);
		addBehaviour(fsm);
	}



	private class AgentMod03_1 extends OneShotBehaviour {

		ACLMessage message;
		
		@Override
			public void action() {

				ACLMessage message = receive();
				if(message!= null){
					try {
						City ct = (City) message.getContentObject();
						System.out.println(ct.getnom());
						ArCity.add(ct);
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
								
			}
			@Override
			public int onEnd() {
				if(message != null)
					return 0;
				else
					return 1;//repiti
		}
	}// fin class AgentMod03_1

	private class AgentMod03_2 extends OneShotBehaviour {
		//calculé la distance
			@Override
			public void action() {
				City[] cityCalcule = ArCity.toArray(new City[0]);
				double ds = CalculeDistances(cityCalcule);
				String name = myAgent.getLocalName();
				dist = new AgentDistance(ds, name);
				System.out.println(ds+ " "+name);
			}
			@Override
			public int onEnd() {
				return 0;
			}
		}//fin class AgentMod03_2

	private class AgentMod03_3 extends OneShotBehaviour{
		@Override
			public void action() {
				ACLMessage msgDist= new ACLMessage(ACLMessage.INFORM);
				msgDist.addReceiver(new AID("semissionnaire", AID.ISLOCALNAME));
				try {
					msgDist.setContentObject(dist);
					send(msgDist);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			@Override
			public int onEnd() {
				return 0;
			}
		}// fin class AgentMod03_3
	private class AgentMod03_4 extends OneShotBehaviour{
		ACLMessage message;
		@Override
			public void action() {

				message = receive();
				if(message != null) {
					
						String msg = (String) message.getContent();
						if(msg == "annule") {//remve dernier elements
							int indx = ArCity.size() - 1;
							ArCity.remove(indx);
							System.out.println(myAgent.getLocalName() +" remove element");
						}
					
				}
				
			}
			@Override
			public int onEnd() {
				if(message != null)
					return 0;
				else
					return 1;//repiti
			}
		}// fin class AgentMod03_4
	
	public double CalculeDistances(City[] data) {
		 double total = 0.0f;
		if(data.length == 1)
			return 0;
		else if(data.length == 2) {
			total += data[0].distance(data[1]);
			total =  2*total;
		}
		else {
			initArguments(data);
			City[] CT = algoGen();
			for(int i = 0; i < CT.length - 1; i++) {
	            total += CT[i].distance(CT[i+1]);
	        }
			total += CT[CT.length-1].distance(CT[0]);
		}

       return total;

	}
	public void initArguments(City[] data) {
    	ag=new  AlgoGenetique(data);
        this.population = new Population(ag,ag.CITIES, 9000);
        this.generation = new AtomicInteger(0);
        
        try {
			Thread.sleep(1);
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