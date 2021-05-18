package it.polito.tdp.borders.model;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();

		System.out.println("TestModel -- TODO");
		
//		System.out.println("Creo il grafo relativo al 2000");
//		model.createGraph(2000);
		model.creaGrafo(2000);
		System.out.println("Creato il grafo con "+model.NVertici()+" vertici e "+model.NArchi()+" archi");
		
//		List<Country> countries = model.getCountries();
		model.getConfinanti();
		System.out.println(model.getConfinanti());
//		System.out.format("Trovate %d nazioni\n", countries.size());
		

//		System.out.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents());
		System.out.println(model.getComponentiConnesse());
//		Map<Country, Integer> stats = model.getCountryCounts();
//		for (Country country : stats.keySet())
//			System.out.format("%s %d\n", country, stats.get(country));		
	}

}
