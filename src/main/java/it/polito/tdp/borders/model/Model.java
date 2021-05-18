package it.polito.tdp.borders.model;

import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	private BordersDAO dao;
	private Map<Integer,Country> idMap;
	private Graph<Country,DefaultEdge> grafo;
	
	public Model() {
		this.dao=new BordersDAO();
		this.idMap=new TreeMap<Integer,Country>();
		dao.loadAllCountries(idMap);
		
	}
	
	public void creaGrafo(int anno) {
		this.grafo=new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		
		//riempimento vertici
		for(Border b: dao.getCountryPairs(anno)) {
			grafo.addVertex(idMap.get(b.getState1no()));
			grafo.addVertex(idMap.get(b.getState2no()));
			grafo.addEdge(idMap.get(b.getState1no()), idMap.get(b.getState2no()));
			
		}
	}
	
	public Map<Country,Integer> getConfinanti(){
		Map<Country,Integer> m=new HashMap<Country,Integer>();
		
		//per ogni country(vertice) guardo quanti sono i nodi adiacenti
		//Stampare l’elenco degli stati
		//indicando per ciascuno il numero di stati confinanti
		for(Country c: grafo.vertexSet()) {
			m.put(c, Graphs.successorListOf(this.grafo,c).size());
		}
		return m;
	}
	
	public int getComponentiConnesse() {
		ConnectivityInspector<Country,DefaultEdge> c=new ConnectivityInspector<Country,DefaultEdge>(this.grafo);
		return c.connectedSets().size();
	}
	
	public int NVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int NArchi() {
		return this.grafo.edgeSet().size();
	}
	
	//Algoritmo visita in AMPIEZZA per ricerca percorso
	public List<Country> getStatiRaggiungibili(Country c){
			
			final List<Country> result=new LinkedList<>();
			
			BreadthFirstIterator<Country,DefaultEdge> it=new BreadthFirstIterator<>(grafo,c);
			
			it.addTraversalListener(new TraversalListener<Country, DefaultEdge>() {
				
				@Override
				public void vertexTraversed(VertexTraversalEvent<Country> e) {
					
					if(!result.contains(e.getVertex())) {
						result.add(e.getVertex());
					}
					
				}
				
				@Override
				public void vertexFinished(VertexTraversalEvent<Country> e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			while(it.hasNext()) {
				it.next();
				
			}
			
			
			return result;
	}
	
	public List<Country> getStatiRaggiungibili1(Country c1){ //con getParent, solo se chiede come li hai raggiunti
			
			final List<Country> result=new LinkedList<>();
			
			BreadthFirstIterator<Country,DefaultEdge> it=new BreadthFirstIterator<>(grafo,c1);
						
			while(it.hasNext()) {
				result.add(it.next());
			}
			
			return result;
	}
	
	//Algoritmo RICORSIVO per la ricerca del percorso
	public List<Country> getStatiRaggiungibili2(Country c){
		List<Country> parziale=new LinkedList<>();
		parziale.add(c);
		
		trovaPercorso(parziale);
		
		return parziale;
		
	}
	public void trovaPercorso(List<Country> parziale) {
		//Non c'e' condizione di terminazione: devi attraversare tutto il grafo attraverso le componenti connesse
		
		for(Country vicino:Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				trovaPercorso(parziale);
				//non fai backtracking perche' non cerco percorso, sto attraversando tutto il grafo
			}
		}
		
	}
	
	//Algoritmo ITERATIVO per la ricerca del percorso
	public List<Country> getStatiRaggiungibili3(Country c) {
		List<Country> daVisitare=new LinkedList<Country>();
		List<Country> visitati=new LinkedList<Country>();
		List<Country> prova=new LinkedList<Country>();
		
		daVisitare.add(c);
		
		Country c2;
		
		while(daVisitare.size()>0) {
			
			c2=daVisitare.get(0);
			
			for(Country vicino:Graphs.neighborListOf(this.grafo,c2)){
				if(!prova.contains(vicino)) {
					prova.add(vicino);
					daVisitare.add(vicino);
				}
			}
			daVisitare.remove(c2);
			visitati.add(c2);
		}
		
		return visitati;
	}
	
	
	
	
	

}
