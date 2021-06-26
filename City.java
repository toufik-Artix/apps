package win;
import java.util.Objects;


import java.io.Serializable;

public class City implements Serializable{

	private String nomAgent;
	private int agent;
    private final int x;
    private final int y;
    private String nom;
    public boolean noteMyVille=false;
    
    City( int x,  int y, String nom, int agent) {
        this.x = x;
        this.y = y;
        this.agent = agent;
        this.nom = nom;
    }

    public void setNomAgent(String s) {
    	this.nomAgent = s;
    }

    String getnom() {
        return this.nom;
    }
    public int verifieAgent() {
        return this.agent;
    }
    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

    double distance( City other) {
    	
        return Math.sqrt(Math.pow(getX() - other.getX(), 2) + Math.pow(getY() - other.getY(), 2));
    	
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final City gene = (City) o;
        return this.x == gene.x &&
                this.y == gene.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
