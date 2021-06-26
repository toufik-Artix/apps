package win;

import java.io.Serializable;

public class AgentDistance implements Serializable, Comparable<AgentDistance> {

	public double dist;
	public String nomAgent;
	
	public AgentDistance(double dist, String nomAgent) {
		this.dist = dist;
		this.nomAgent = nomAgent;
	}
	
	public double getDist() {
		return this.dist;
	}
	public String getNomAgent() {
		return this.nomAgent;
	}

	@Override
	public int compareTo(AgentDistance ad) {
		return (int) (this.dist - ad.dist);
	}
}
