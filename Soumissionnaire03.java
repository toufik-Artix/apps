package win;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Soumissionnaire03 extends Agent{

	public static ArrayList<AgentDistance> ListVileReponde = new ArrayList<>();
	City[] ListeVilles;
	City[] ListeAgents;
	private int indexVilleEnvoye = 0;

	//pour Soumissionnaire
	

	private static final String STATE_A = "envoyé ville";
	private static final String STATE_B = "recuperi Distance";
	private static final String STATE_C = "Installation Ville";
		
	@Override
	protected void setup() {

		Object[] args = getArguments();
		ListeVilles = (City[])args[0];
		ListeAgents = (City[])args[1];
		//Creation les Agents
		ContainerController cc;
		AgentController ac;
		cc= getContainerController();
		try {
			for(int i=0; i<ListeAgents.length ;i++) {
				ac = cc.acceptNewAgent("Agent-Mod-2-"+i, new AgentMod02());
				ac.start();
			}
		} catch (StaleProxyException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fin creation des agents
		
		FSMBehaviour fsm = new FSMBehaviour();
		// Register state A (first state)
		fsm.registerFirstState(new EnvoyeVille(), STATE_A);
		// Register state B
		fsm.registerState(new recuperi_Distance(), STATE_B);
		// Register state C
		fsm.registerState(new InstallationVille(), STATE_C);
		
		// Register the transitions
		fsm.registerDefaultTransition(STATE_A, STATE_B);
		fsm.registerTransition(STATE_B, STATE_B,1);
		fsm.registerTransition(STATE_B, STATE_C,0);
		fsm.registerTransition(STATE_C, STATE_A, 1);
	
		addBehaviour(fsm);
	}
	
	
	private class EnvoyeVille extends OneShotBehaviour{
		
		
		@Override
		public void action() {
			ListVileReponde.removeAll(ListVileReponde);
			try {
				if(indexVilleEnvoye < ListeVilles.length) {
					City cityEnvoye = new City(ListeVilles[indexVilleEnvoye].getX(), ListeVilles[indexVilleEnvoye].getY(), ListeVilles[indexVilleEnvoye].getnom(), ListeVilles[indexVilleEnvoye].verifieAgent() );
					indexVilleEnvoye++;
					ACLMessage msg= new ACLMessage(ACLMessage.INFORM);
					msg.setContentObject(cityEnvoye);
					for(int i =0; i<ListeAgents.length ;i++) {
						msg.addReceiver(new AID("Agent-Mod-2-"+i, AID.ISLOCALNAME));
						send(msg);
					}
				}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("EnvoyeVille");
		}
		@Override
		public int onEnd() {
			return 0;
		}
	}
	// fin class Envoyé ville
	private class recuperi_Distance extends Behaviour{
		ACLMessage message;
		
		
		@Override
		public void action() {
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			message = myAgent.receive(modele);
			if (message != null){
				System.out.println("msg not null");
				try {
					AgentDistance ad = (AgentDistance) message.getContentObject();
					System.out.println(ad.dist + "  "+ ad.nomAgent);
					ListVileReponde.add(ad);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}//fin condition if
			else {
				block();
			}
			System.out.println("recuperi_Distance");
		}//fin action
		
		@Override
		public boolean done() {
			if(ListVileReponde.size() < ListeAgents.length)
				return false;
			else 
				return true;
		}
		
		@Override
		public int onEnd() {
				if(ListVileReponde.size() < ListeAgents.length) {
					return 1;//repetition
				}
				else
					return 0;
		}
		
	}
	//fin class
	
	
	private class InstallationVille extends OneShotBehaviour{
		@Override
		public void action() {
			Collections.sort(ListVileReponde);// trié les distance
			
			for(int i= 0;i<ListeAgents.length;i++) {
				ACLMessage message= new ACLMessage(ACLMessage.INFORM);
				message.addReceiver(new AID("Agent-Mod-2-"+i, AID.ISLOCALNAME));
				if("Agent-Mod-2-"+i == ListVileReponde.get(0).nomAgent) {
						message.setContent("ok");
						send(message);
				}
				else {			
						message.setContent("annule");
						send(message);
					
				}
			}//fin for
			System.out.println("InstallationVille");
		}//fin action
		@Override
		public int onEnd() {
			return 0;
		}
	}
	
	
}