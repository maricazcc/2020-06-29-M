package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private Graph<Director, DefaultWeightedEdge>grafo;
	private ImdbDAO dao;
	private Map <Integer, Director> idMap;
	private List <Director> registiAffini;
	
	public Model() {
		this.dao = new ImdbDAO();
	}
	
	public List<Integer> getAnni() {
		List<Integer> anni = new ArrayList<>();
		anni.add(2004);
		anni.add(2005);
		anni.add(2006);
		return anni;		
	}
	
	public String creaGrafo(int anno) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);		
		
		//Aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(anno));
		
		this.idMap = new HashMap<>();		
		for(Director d : grafo.vertexSet())
			idMap.put(d.getId(), d);
		
		//Aggiungo archi
		for (Adiacenza a : dao.getAdiacenze(anno, idMap))
			Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
	
		
		return String.format("Grafo creato con %d vertici e %d archi.\n", 
				this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public Graph<Director, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<Director> listaVertici() {
		
		List<Director> vertici = new ArrayList<>();
		
		for (Director d : grafo.vertexSet()) 
			vertici.add(d);

		Collections.sort(vertici);
		return vertici;
	}
	
	public List<Vicino> registiAdiacenti(Director regista) {
		List<Director> vicini = Graphs.neighborListOf(this.grafo, regista);
		List<Vicino> result = new ArrayList<Vicino>();

		for(Director d : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(regista, d));
			Vicino v = new Vicino(d, (int) peso);
			result.add(v);
		}
		Collections.sort(result);
		return result;		
	}
	
	public List<Director> getRegistiAffini (Director regista, Integer pesoMax) {
		this.registiAffini = new ArrayList<Director>();
		List<Director> parziale = new ArrayList<Director>();
		parziale.add(regista);
		cerca(parziale, pesoMax);
		return registiAffini;
	}

	private void cerca(List<Director> parziale, Integer pesoMax) {
		//caso terminale
		if(calcoloPeso(parziale)>=pesoMax)
			return;
		
		if(parziale.size()>registiAffini.size())
			registiAffini = new ArrayList<Director> (parziale);
		
		
		//caso generale
		Director ultimo = parziale.get(parziale.size()-1);
		
		for(Director d : Graphs.neighborListOf(this.grafo, ultimo)) {
			if(!parziale.contains(d)) {
				parziale.add(d);
				cerca(parziale, pesoMax);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}

	private Integer calcoloPeso(List<Director> parziale) {
		int peso = 0;
		for(int i=0; i<parziale.size()-1; i++) {
			Director d1 = parziale.get(i);
			Director d2 = parziale.get(i+1);
			peso+=this.grafo.getEdgeWeight(this.grafo.getEdge(d1, d2));
		}
		return peso;
	}
	
	

}
