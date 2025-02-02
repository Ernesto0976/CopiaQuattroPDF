package com.copsis.models.multiva;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;


public class MultivaModels {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;

	public MultivaModels(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		try {
			switch (fn.tipoPoliza(contenido)) {
			case 1:// Autos
				modelo  = new MultivaAutosModel(fn.caratula(1, 2, stripper, doc)).procesar();				
				break;
			case 2:// Salud
				modelo  = new MultivaSaludModel(fn.caratula(1, 2, stripper, doc)).procesar();				
				break;
			case 4:// Diversos
				modelo  = new MultivaDiversosModel(fn.caratula(1, 2, stripper, doc)).procesar();				
				break;
	
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MultivaModels.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}

}
