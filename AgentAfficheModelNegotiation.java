package win;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgentAfficheModelNegotiation extends Agent{

	public static ArrayList<Object> CITIES = new ArrayList<>();
	
	@Override
	protected void setup() {
		
		MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				ACLMessage msg =myAgent.receive(modele);
				if (msg != null) {			
					try {
						City[] Cities = (City[]) msg.getContentObject();	
						CITIES.add(Cities);
						
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
			}
			}
		});
	}

}