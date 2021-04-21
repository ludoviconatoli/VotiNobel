package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		Set<Esame> parziale = new HashSet<>();
		soluzioneMigliore = new HashSet<Esame>();
		mediaSoluzioneMigliore = 0;
		
		cerca2(parziale, 0, numeroCrediti);
		return soluzioneMigliore;	
	}

	// COMLESSITA': N! non va bene, soluzione migliore sotto
	private void cerca1(Set<Esame> parziale, int L, int m) {
		//CASI TERMINALI
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		
		if(crediti == m) {
			double media = calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale);
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
		for(Esame e: partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e);
			}
		}
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

	//COMPLESSITA' 2^N meglio di prima
	private void cerca2(Set<Esame> parziale, int L, int m) {
		//CASI TERMINALI
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		
		if(crediti == m) {
			double media = calcolaMedia(parziale);
			if(media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale);
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
