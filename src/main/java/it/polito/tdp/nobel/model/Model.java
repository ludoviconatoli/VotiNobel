package it.polito.tdp.nobel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	//private Set<Esame> soluzioneMigliore;
	private List<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		//Set<Esame> parziale = new HashSet<>();
		List<Esame> parziale = new ArrayList<>();
		soluzioneMigliore = new ArrayList<Esame>();
		mediaSoluzioneMigliore = 0;
		
		cerca1(parziale, 0, numeroCrediti);
		return soluzioneMigliore;	
	}

	// COMLESSITA': N! non va bene, soluzione migliore sotto
	private void cerca1(List<Esame> parziale, int L, int m) {
		//CASI TERMINALI
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		
		if(crediti == m) {
			double media = calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new ArrayList<>(parziale);
				this.mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		//sicuramente, crediti < m
		// 1) L = N -> non ci sono più esami da raggiungere
		if(L == partenza.size()) {
			return;
		}
		
		//GENERARE I SOTTO-PROBLEMI
		/*for(Esame e: partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e);
			}
		}*/
		
		//ALTERNATIVA
		//Per non considerare entrambi i casi {e1, e2} {e2, e1}
		//anche se non funziona sempre
		/*for(int i=0; i<partenza.size(); i++) {
			if(!parziale.contains(partenza.get(i)) && i>=L) {
				parziale.add(partenza.get(i));
				cerca1(parziale, L+1, m);
				parziale.remove(partenza.get(i));
			}
		}*/
		
		//ALTERNATIVA MIGLIORE usando le List al posto dei Set
		//si tiene traccia dell'ultimo indice per far sì che il sistema di prima funzioni sempre
		int lastIndex = 0;
		if(parziale.size() > 0)
			lastIndex = partenza.indexOf(parziale.get(parziale.size()-1));
		
		for(int i=lastIndex; i<partenza.size(); i++) {
			if(!parziale.contains(partenza.get(i)) && i>=L) {
				parziale.add(partenza.get(i));
				cerca1(parziale, L+1, m);
				parziale.remove(partenza.get(i));
			}
		}
	}

	public double calcolaMedia(List<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(List<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

	//COMPLESSITA' 2^N meglio di prima
	private void cerca2(List<Esame> parziale, int L, int m) {
		//CASI TERMINALI
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		
		if(crediti == m) {
			double media = calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new ArrayList<>(parziale);
				this.mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		if(L == partenza.size()) {
			return;
		}
		
		//GENERO SOTTO-PROBLEMI
		parziale.add(partenza.get(L));
		cerca2(parziale, L+1, m);
		parziale.remove(partenza.get(L));
		cerca2(parziale, L+1, m);
	}
}
