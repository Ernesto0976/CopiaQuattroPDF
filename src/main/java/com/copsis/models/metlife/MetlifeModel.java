package com.copsis.models.metlife;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;


public class MetlifeModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private Integer pagIni =0;
	private Integer pagFin =0;
	
	public MetlifeModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido.replace("GMM", "GASTOS M");
	}
	public EstructuraJsonModel procesar() {
		try {

			switch (fn.tipoPoliza(contenido)) {
			case 2:// Salud
				modelo  = new MetlifeSaludModel(fn.caratula(1, 3, stripper, doc)).procesar();
				
				break;
			case 5:// Vida
				modelo  = new MetlifeVidaModel(fn.caratula(1, 6, stripper, doc)).procesar();

			}

			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					MetlifeModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
		
	}
	
}
