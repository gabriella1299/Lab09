
package it.polito.tdp.borders;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    private ComboBox<Country> CmbNaz;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	CmbNaz.getItems().clear();
    	int anno=0;
    	
    	try {
    		anno=Integer.parseInt(txtAnno.getText());
    		if(anno<1816||anno>2016) {
    			txtResult.appendText("Inserire un anno compreso tra 1816 e 2016!");
    			return;
    		}
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("Inserire un numero intero per l'anno!");
    		return;
    	}
    	
    	try {
    		
    		model.creaGrafo(anno);
        	
        	//stampare il numero di componenti connesse nel grafo
    		txtResult.appendText("Numero di componenti connesse: "+model.getComponentiConnesse()+"\n");
    		
    		//stampare l’elenco degli stati, indicando per ciascuno il numero di stati confinanti
    		for(Country c:model.getConfinanti().keySet()) {
    			txtResult.appendText(c.getStateNme()+" "+model.getConfinanti().get(c)+"\n");
    		}
    		
    		CmbNaz.getItems().addAll(model.getConfinanti().keySet());
        	
    	}catch(RuntimeException e) {
    		txtResult.setText("Errore: " + e.getMessage() + "\n");
			return;
    	}
    	
    }
    
    @FXML
    void doStatiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	
    	if (CmbNaz.getItems().isEmpty()) {
			txtResult.setText("Il grafo e' vuoto: inserire anno e fare click su Calcola confini");
			return;
		}
    	
    	Country c=CmbNaz.getValue();
    	if(c==null) {
    		txtResult.appendText("Scegliere una nazione!");
    		return;
    	}
    	
    	try {
    		
    		List<Country> raggiungibili=new LinkedList<>();
    		raggiungibili=model.getStatiRaggiungibili(c);
        	for(Country cc:raggiungibili) {
        		txtResult.appendText(cc.toString());
        	}
    		
    	}catch (RuntimeException e) {
			// If the countries are inserted in the ComboBox when the graph is created,
			// this should never happen.
			txtResult.setText("La Nazione selezionata non e' nel grafo!");
			return;
		}
    		
    	
    		
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert CmbNaz != null : "fx:id=\"CmbNaz\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    }
}
