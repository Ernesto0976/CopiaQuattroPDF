package com.copsis.models.bupa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.panAmerican.PanAmericanSaludModel;

public class BupaModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	
	public BupaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;		
		this.doc = pdDoc;
		this.contenido = contenido;
	}
	
	public EstructuraJsonModel procesar() {
		int  pagIni=0;
		int  pagFin=0;
		try {

			int tipo = fn.tipoPoliza(contenido);
		    if(tipo == 0) {
			   pagIni = fn.pagFinRango(stripper, doc, "Contratante");
			   pagFin = fn.pagFinRango(stripper, doc, "Advertencia");
			   if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {				
			     contenido = fn.caratula(pagIni, pagFin, stripper, doc);
			   }
			   tipo = fn.tipoPoliza(contenido);
			 }

			switch (tipo) {
			case 2:
				 if(pagIni > 0 && pagFin > 0 && pagFin >= pagIni) {		
					 modelo =new  BupaSaludModel().procesar(fn.caratula(pagIni, pagIni+1, stripper, doc),fn.caratula(pagIni, pagIni+5, stripper, doc));
				 }else {
					 modelo =new  BupaSaludModel().procesar(fn.caratula(0, 3, stripper, doc),"");
				 }
				
				break;

			default:
				break;
			}
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					BupaModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
}
