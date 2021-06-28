package it.polito.tdp.imdb.model;

public class Vicino implements Comparable <Vicino> {
	private Director regista;
	private int peso;
	
	public Vicino(Director regista, int peso) {
		this.regista = regista;
		this.peso = peso;
	}

	public Director getRegista() {
		return regista;
	}

	public void setRegista(Director regista) {
		this.regista = regista;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Vicino o) {
		return - (this.getPeso() - o.getPeso());
	}

	@Override
	public String toString() {
		return regista + " - #Attori condivisi: " +peso;
	}
	
	
	
	
	

}
