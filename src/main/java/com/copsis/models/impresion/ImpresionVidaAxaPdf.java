package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.controllers.forms.ImpresionAxaVidaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionVidaAxaPdf {
	
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorAb = new Color(203, 193, 230, 0);
	private final Color bgColorA = new Color(0, 0, 143, 0);
	private float margin = 25, yStartNewPage = 780, yStart = 780, bottomMargin = 30;
	private float fullWidth = 556;
	private float fullWidthx = 0;
	private float fullWidthy = 0;
	private float ypos ;
	private float ypos2 ;
	private Boolean acumula;

	public byte[] buildPDF(ImpresionAxaVidaForm  impresionAxaVidaForm) {
		ByteArrayOutputStream output;
		try {
			
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					
					this.setEncabezado(document, page);
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page,true , true);	    
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"Datos del Contratante",Color.white,true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);
					communsPdf.setCell(baseRow, 78,"(Es la persona que se compromete a realizar el pago de la prima)",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(0f,1f,4f,0f),bgColorA);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"El nombre completo, el RFC con homoclave y la CURP son datos necesarios para la emisión de las constancias y CFDI para la deducción de impuestos y, en su caso, para la recuperación de estos.",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"Nombre completo (como aparece en su identificación oficial)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"Nombre completo (como aparece en su identificación oficial)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"RFC con homoclave",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"CURP",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"No. de serie FIEL",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"Estado civil",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 72,"Domicilio",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 72,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"Municipio / Alcaldía",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 31,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 31,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 44,"Régimen fiscal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 15,"Clave de uso",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 19,"C.P. domicilio fiscal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 44,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 15,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 19,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
								
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 32,"Tel. particular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 32,"Tel. celular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 36,"Correo electrónico",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 32,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 32,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 36,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 23,"Actividad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 16,"Ingreso Anual",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Ciudadanía en Estados",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 36,"Residencia fiscal en el extranjero",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 23,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 16,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 36,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
				
					baseRow = communsPdf.setRow(table, 15);					
					communsPdf.setCell(baseRow, 100,"Completar si es residente fiscal en el extranjero o tiene ciudadanía o nacionalidad de Estados Unidos",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorAb);
			
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"Domicilio en el extranjero",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 12,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 12,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 12,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 12,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 13,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"País",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 23,"No. de Identificación Fiscal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"Teléfono",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 23,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);

				
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Datos del Solicitante Titular (solo si es diferente del Contratante)",Color.white,true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"(Es la persona sobre la que recaen los riesgos amparados en la póliza, si es aceptada la solicitud)",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"Nombre(s), apellido paterno, apellido materno",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);

					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado Civil",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Relación con el contratante",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Fecha de nacimiento",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"Nacionalidad(es)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"Lugar de nacimiento (país, estado, municipio)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"Domicilio",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,12,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 12,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Municipio / Alcaldía",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,25,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"Tel. celular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);									
					communsPdf.setCell(baseRow,42,"Correo electrónico",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 42,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
//					baseRow = communsPdf.setRow(table, 15);
//					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3XINojdFJ0ksYi9XZB9TmicMVpVgufnUbOqKk2FZBbnL/Logo1.png"),3,0,Color.white);
//		
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 80,"Nombre(s), apellido paterno, apellido materno",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 80,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);

					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado Civil",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Relación con el contratante",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Fecha de nacimiento",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"Nacionalidad(es)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"Lugar de nacimiento (país, estado, municipio)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"Domicilio",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,12,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 12,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Municipio / Alcaldía",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,25,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"Tel. celular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);									
					communsPdf.setCell(baseRow,42,"Correo electrónico",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 42,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);							
					table.setCellCallH(true);
										
					table.draw();
					
					
					page = new PDPage();
					document.addPage(page);
					this.setEncabezado(document, page);

				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page,true , true);	    
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15,"Plan solicitado",Color.white,true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);
					communsPdf.setCell(baseRow, 85,"(Confirmar características aplicables a cada producto)",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(0f,1f,4f,0f),bgColorA);					
				    table.setCellCallH(true);
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight();
					ypos = yStart;
					fullWidthx=303;
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidthx, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 33,"Producto",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 34,"Plazo",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"PlazoPago",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 33,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 34,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 33,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 10);
					communsPdf.setCell(baseRow, 33,"Moneda",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 34,"Incrementos de Suma Aseguradora",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,Sio4CommunsPdf.eliminaHtmlTags3("<b>Riesgos</b>")+"(universales)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					baseRow = communsPdf.setRow(table, 10);
					
					communsPdf.setCellImg(baseRow, 33, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3QBhuZYDR0g6znE1Dq9FomfXgA6Ed0EZCv4KwOUBJUbd/1.png").scale(120,30), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(2f,0f,-5f,0f), "L", "T");										
					communsPdf.setCellImg(baseRow, 34, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3ZatmmyEsaqEpSIlV68iL5eQOfwRY8is5QC8GKkWsoNK/suma.png").scale(70,20), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(2f,0f,0f,0f), "L", "vB");
					communsPdf.setCellImg(baseRow, 33, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3bt1wfbQm88by32ph67Lb9dDc8cbklj1no6f0moNvAP/Riesgos.png").scale(60,30), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(2f,0f,0f,0f), "L", "T");									
				
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCellImg(baseRow, 50, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3Y0aEaAvjG4tutl7Q4gqng1HJeNfLOZRVgFgbooAOlLE/pagos.png").scale(120,60), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), communsPdf.setPadding2(0f,0f,0f,0f), "L", "T");
					communsPdf.setCell(baseRow, 50,Sio4CommunsPdf.eliminaHtmlTags3("Aportación Adicional /\nPrima excedente"),bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,Sio4CommunsPdf.eliminaHtmlTags3("Forma de pago"),bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding2(0f,0f,0f,0f),bgColor);
					communsPdf.setCell(baseRow, 5,Sio4CommunsPdf.eliminaHtmlTags3("$"),bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.white), "", communsPdf.setPadding2(2f,0f,1f,0f),bgColor);
					communsPdf.setCell(baseRow, 40,Sio4CommunsPdf.eliminaHtmlTags3("60"),bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(0f,0f,0f,0f),bgColor);
					communsPdf.setCell(baseRow, 5,Sio4CommunsPdf.eliminaHtmlTags3(""),bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.white,Color.black,Color.white,Color.white), "", communsPdf.setPadding2(0f,0f,0f,0f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCellImg(baseRow, 50, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3UK2j1k33K4RRepXcAXlMI7C8FbIxrlyZ3PDrSnhRO/fPagos.png").scale(100,50), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), communsPdf.setPadding2(2f,0f,0f,0f), "L", "T");
					communsPdf.setCell(baseRow, 50,Sio4CommunsPdf.eliminaHtmlTags3("(la frecuencia es igual al pago de prima básica)"),bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					table.draw();

									
					
					yStart=ypos;
					ypos2 =yStart;
					table = new BaseTable(yStart, yStart, bottomMargin, (fullWidth-fullWidthx), 328, document, page,true , true);
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 100,"Estrategia de Inversión (universales)",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 13);
					communsPdf.setCell(baseRow, 48,"Fondo",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"Básico",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"Excedente",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 13);
					communsPdf.setCell(baseRow, 48,"Conservador",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 13);
					communsPdf.setCell(baseRow, 48,"Balanceado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 13);
					communsPdf.setCell(baseRow, 48,"Crecimiento",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 14);
					communsPdf.setCell(baseRow, 48,"Dólares",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 14);
					communsPdf.setCell(baseRow, 48,"Dinámico MX",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 14);
					communsPdf.setCell(baseRow, 48,"Dinámico EUA",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 14);
					communsPdf.setCell(baseRow, 48,"TOTAL",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 14);
					communsPdf.setCell(baseRow, 48,"Dinámico EUA",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"%",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
		
					baseRow = communsPdf.setRow(table, 14);
					communsPdf.setCell(baseRow, 100,"No. y monto de retiros al año:",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					
					table.draw();
			
					
					yStart -=table.getHeaderAndDataHeight()+9;
					
					

					table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Cobertura",bgColorA,true, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);
					communsPdf.setCell(baseRow, 25,"Suma Asegurada",bgColorA,true, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);
					communsPdf.setCell(baseRow, 25,"Especificar",bgColorA,true, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);			
				
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Básica (Titular / Mancomunado)",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"$",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
			
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Básica (Menor)",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"$",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Exención por Invalidez / fallecimiento",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Pago Adicional por Invalidez",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"$",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Cobertura Conyugal",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"$",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Muerte Accidental",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
			
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Cáncer",bgColorA,false, "r", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"$",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Pago Adicional por Enfermedades Graves",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"$",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Desempleo (Protección Continua / Exención)",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 50,"Gastos Funerarios",bgColorA,false, "R", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"$",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+10;
					table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Designación de Beneficiarios",Color.white,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorA);
	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 4,"",bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Solicitante",bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 41,Sio4CommunsPdf.eliminaHtmlTags3("Beneficiarios: Nombre(s), apellido paterno,"),bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);			
					communsPdf.setCell(baseRow, 13,"Fecha de nacimiento",bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Parentesco",bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"% Participación",bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);										
					table.draw();
					yStart -=table.getHeaderAndDataHeight();

					if(impresionAxaVidaForm.getBenficiarios() !=null) {
						int x=0;
						
						//while (x < impresionAxaVidaForm.getBenficiarios().size()) {
						while (x < impresionAxaVidaForm.getBenficiarios().size()) {
							 acumula = true;
							 table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
								baseRow = communsPdf.setRow(table, 15);
								communsPdf.setCell(baseRow, 4,"",bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
								communsPdf.setCell(baseRow, 14,impresionAxaVidaForm.getBenficiarios().get(x).getSolicitante(),bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
								communsPdf.setCell(baseRow, 41,impresionAxaVidaForm.getBenficiarios().get(x).getNombres(),bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);			
								communsPdf.setCell(baseRow, 13,impresionAxaVidaForm.getBenficiarios().get(x).getFecNacimiento(),bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
								communsPdf.setCell(baseRow, 14,impresionAxaVidaForm.getBenficiarios().get(x).getParentesco(),bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
								communsPdf.setCell(baseRow, 14,impresionAxaVidaForm.getBenficiarios().get(x).getPorcentaje().toString(),bgColorA,false, "c", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);																					
							    if (isEndOfPage(table)) {
	                                table.getRows().remove(table.getRows().size() - 1);
	                                table.draw();
	                                page = new PDPage();
	                                document.addPage(page);
	                            	this.setEncabezado(document, page);
	                                acumula = false;

	                            } else {
	                                table.draw();
	                                yStart -= table.getHeaderAndDataHeight();
	                            }

	                            if (acumula) {
	                                x++;
	                            }
	                            if (x > 150) {
	                                table.draw();
	                                break;
	                            }
						}
					}
					
					table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Domicilio de los Beneficiarios (solo en caso de ser distinto al domicilio del Solicitante Titular)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 20,"No. de Beneficiario",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 80,"Domicilio",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
	             
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 20,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 80,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 20,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 80,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 20,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 80,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100,"Información adicional:",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					table.draw();
					
					page = new PDPage();
					document.addPage(page);
					this.setEncabezado(document, page);

					table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,Sio4CommunsPdf.eliminaHtmlTags3("<b>Cuestionarios.</b>") +"(Si se requiere ampliar la información, agregarla en hoja anexa)",Color.white,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorA);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Otros seguros de vida (En Suma Asegurada, indique la moneda)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 17,"Solicitante",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 17,"Aseguradora",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 17,"Suma Asegurada",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 17,"Solicitante",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 16,"Aseguradora",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 16,"Suma Asegurada",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);			  					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 17,"Titular",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 17,"Aseguradora",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 17,"Suma Asegurada",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 17,"Solicitante",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 16,"Aseguradora",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 16,"Suma Asegurada",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);			  							
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Ocupación (Explicar detalladamente la actividad y lugar de trabajo)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12,"Solicitante",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 18,Sio4CommunsPdf.eliminaHtmlTags3("Profesión,\nactividad u oficio"),bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Nombre y giro de la empresa de trabajo",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 28,"Descripción de la actividad,puesto y lugar de trabajo (oficina, fábrica, taller, otro)",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 15,"Aseguradora",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"Ingreso anual",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);			  								
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12,"Titular",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 18,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 28,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 15,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);			  								
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 18,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 28,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 15,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);			  								
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+7;
					ypos = yStart;
					fullWidthx =0;
					fullWidthx =278;
					
					table = new BaseTable(yStart, yStart, bottomMargin, fullWidthx, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 65);					
					communsPdf.setCellImg(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3cMwE1TSWexyA4I3LsIS2IhCabHHRVdEbsoWCNIT5t/situaciones.png").scale(300,150), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(1f,0f,2f,0f), "L", "T");													
					table.draw();
					
					
					yStart = ypos;
					table = new BaseTable(yStart, yStart, bottomMargin, 277, 303, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"¿Quién?",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 64,"Especifique",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 24);
					communsPdf.setCell(baseRow, 36,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 64,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 24);
					communsPdf.setCell(baseRow, 36,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 64,"",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);				
					
					table.draw();
					yStart -=table.getHeaderAndDataHeight()+2;
					table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Deportes, aficiones, aviación (en caso afirmativo, especifique)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);				
					baseRow = communsPdf.setRow(table, 28);
					communsPdf.setCellImg(baseRow, 50, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3eQ7cxP9XvtH72uAHoSHyqjgtXcfNmQcM70c7Q4LYBet/pract.png").scale(300,150), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), communsPdf.setPadding2(2f,0f,0f,0f), "L", "T");
					communsPdf.setCell(baseRow, 19,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 31,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);				
					baseRow = communsPdf.setRow(table, 28);
					communsPdf.setCellImg(baseRow, 50, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3RcCTXEev5h56BU7LHMCQd0JEi46wuatAzt5Q3CKFjq/vuela.png").scale(300,150), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), communsPdf.setPadding2(2f,0f,0f,0f), "L", "T");
					communsPdf.setCell(baseRow, 19,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 31,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
			
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"En caso afirmativo, anexe el cuestionario correspondiente",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Habitos",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);				
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 13,"Solicitante",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 30,"Fuma",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 29,"Ingiere o ha ingerido bebidas alcohólicas*",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 28,"Usa o ha usado drogas o estimulantes*",bgColorA,true, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 13,"Titular",bgColorA,false, "C", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 30,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 29,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 28,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 100,"Cuestionario Médico"+ Sio4CommunsPdf.eliminaHtmlTags3("<b>(este cuestionario deberá ser contestado totalmente por cada una de las personas que solicitan cobertura de Vida Individual)</b>"),bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);				
			
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 17,"Titular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					communsPdf.setCell(baseRow, 17,"Estatura:",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					communsPdf.setCell(baseRow, 16,"Peso:",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					communsPdf.setCell(baseRow, 17,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					communsPdf.setCell(baseRow, 17,"Estatura:",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					communsPdf.setCell(baseRow, 16,"Peso:",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Indique si padece o ha padecido alguna de las siguientes enfermedad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,4f,2f,4f),bgColor);
					ArrayList<String>  preguntas =this.preguntas();
					for (int i = 0; i < 7; i++) {
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 87,preguntas.get(i),bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCellImg(baseRow, 13, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3cER2JhXgsLs1GIWfs4nVpChmwfmDQXLzIp0MohlTft/sino.png").scale(70,30), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), communsPdf.setPadding2(4f,0f,4f,0f), "L", "T");					
					}
										
					table.draw();
					
					
					page = new PDPage();
					document.addPage(page);
					this.setEncabezado(document, page);
					 preguntas =this.preguntas();
					 table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
					for (int i = 7; i < preguntas.size(); i++) {
						baseRow = communsPdf.setRow(table, 25);
						if(preguntas.get(i).equals("Mujeres:")) {
							communsPdf.setCell(baseRow, 100,preguntas.get(i),bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);							
						}else {
							communsPdf.setCell(baseRow, 87,preguntas.get(i),bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
							communsPdf.setCellImg(baseRow, 13, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3cER2JhXgsLs1GIWfs4nVpChmwfmDQXLzIp0MohlTft/sino.png").scale(70,30), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(4f,0f,4f,0f), "L", "T");	
						}
						
					
					}
					table.draw();
					
					yStart -=table.getHeaderAndDataHeight()+5;
					table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);
					baseRow = communsPdf.setRow(table, 25);
					communsPdf.setCell(baseRow, 100,"Si contestó afirmativamente, ampliar la información e indicar nombre de la institución donde se realizó el tratamiento o servicio médico.",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					baseRow = communsPdf.setRow(table, 30);
					communsPdf.setCell(baseRow, 10,"No. de pregunta",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					communsPdf.setCell(baseRow, 13,"Solicitante",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					communsPdf.setCell(baseRow, 36,"Nombre de las enfermedades, afecciones lesiones, estudios, tratamientos.",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					communsPdf.setCell(baseRow, 13,"No. de veces que las ha sufrido",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					communsPdf.setCell(baseRow, 14,"Fecha dd-mm-aa (última vez en caso de se varias)",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					communsPdf.setCell(baseRow, 14,"Estado actual",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					for (int i = 0; i < 6; i++) {
						baseRow = communsPdf.setRow(table, 20);
						communsPdf.setCell(baseRow, 10,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCell(baseRow, 13,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCell(baseRow, 36,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCell(baseRow, 13,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCell(baseRow, 14,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCell(baseRow, 14,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
			
					}
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Historia familiar",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorAb);				
				
					baseRow = communsPdf.setRow(table, 20);
					communsPdf.setCell(baseRow, 87,"¿En alguno de los solicitantes, sus padres biológicos, o alguna hermana o hermano, han sido diagnosticados antes de los 65 años de: cáncer, diabetes, hipertensión arterial, enfermedad cardíaca, accidente "							
							+ "cerebrovascular, enfermedad mental o cualquier otra afección hereditaria?",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding2(2f,0f,2f,0f),bgColor);
					communsPdf.setCellImg(baseRow, 13, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3cER2JhXgsLs1GIWfs4nVpChmwfmDQXLzIp0MohlTft/sino.png").scale(70,30), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(4f,0f,4f,0f), "L", "T");
					
					
					baseRow = communsPdf.setRow(table, 17);
					communsPdf.setCell(baseRow, 13,"Solicitante",bgColorA,true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					communsPdf.setCell(baseRow, 30,"Parentesco",bgColorA,true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					communsPdf.setCell(baseRow, 57,"Detallar enfermedad.",bgColorA,true, "C", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					for (int i = 0; i < 4; i++) {
						baseRow = communsPdf.setRow(table, 17);
						communsPdf.setCell(baseRow, 13,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCell(baseRow, 30,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
						communsPdf.setCell(baseRow, 57,"",bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					}
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Artículo 492. Esta sección deberá ser llenada por el Contratante",Color.white,true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColorA);							
					baseRow = communsPdf.setRow(table, 55);
					communsPdf.setCellImg(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3ciIsDuTiYvBi8DFgxuGC0OPo1ptGmNdrTjuxID5St/contra.png").scale(550,250), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(4f,0f,4f,4f), "L", "T");
//					baseRow = communsPdf.setRow(table, 15);
//					communsPdf.setCellImg(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3ciIsDuTiYvBi8DFgxuGC0OPo1ptGmNdrTjuxID5St/contra.png").scale(550,250), communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), communsPdf.setPadding2(4f,0f,4f,4f), "L", "T");
//				
					table.draw();
					
					
					page = new PDPage();
					document.addPage(page);
					this.setEncabezado(document, page);
					
					
					ArrayList<String>  textonotas =  this.textonotas();
					table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page,true , true);	
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,Sio4CommunsPdf.eliminaHtmlTags3("<b>Notas importantes.</b>"),Color.white,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColorA);
					baseRow = communsPdf.setRow(table, 200);
					communsPdf.setCell(baseRow, 100,textonotas.get(0),bgColorA,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding2(4f,0f,4f,0f),bgColor);
					table.draw();
					
					
					output = new ByteArrayOutputStream();
					document.save(output);									
					document.save(new File("/home/aalbanil/Documentos/AXA-SPRING-PF/AxaVida.pdf"));
					return output.toByteArray();
					
				}finally {
					document.close();
				}
				
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionVidaAxaPdf: " + ex.getMessage());
		}
	}
	private float  setEncabezado( PDDocument document, PDPage page) {	
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			BaseTable table;
			Row<PDPage> baseRow;
			 yStart =770;
			 table = new BaseTable(770, 770, bottomMargin, 300, 280, document, page, true, true);	    
			 baseRow = communsPdf.setRow(table, 10);
			 communsPdf.setCell(baseRow, 100,"Vida y Ahorro",bgColorAb,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			 baseRow = communsPdf.setRow(table, 10);
			 communsPdf.setCell(baseRow, 100,"Solicitud de Seguro de Vida Individual",bgColorA,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			 baseRow = communsPdf.setRow(table, 10);
			 communsPdf.setCell(baseRow, 100,"Personas Físicas",bgColorA,false, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	
			 table.draw();
			
			table = new BaseTable(770, 770, bottomMargin, 120, margin-22, document, page, false, true);
			baseRow = communsPdf.setRow(table, 12);
			communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2208/Polizas/2208/zeSWdRgKylw7QuyGNMQ9vc8HSPiToM8cVUnQd5e2VOIzWrci1IgN1QLcOhFEfhy/Axalogo.png"));
			table.draw();
			yStart -=table.getHeaderAndDataHeight()+15;
			

			
			return yStart;
			
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionConstanciaAntiguedad: setEncabezado " + ex.getMessage());
		}

	}
	private  ArrayList<String> preguntas() {
		ArrayList<String>  preguntas = new ArrayList<>();
		preguntas.add("1. ¿Del corazón, hipertensión arterial o de la circulación?");
		preguntas.add("2. ¿De las vías respiratorias, de los bronquios o pulmonares?");
		preguntas.add("3. ¿Del esófago, estómago, intestinos, colon, recto, vías biliares, hígado, páncreas, bazo?");
		preguntas.add("4. ¿Del sistema genitourinario: riñón, vejiga, próstata, otros?");
		preguntas.add("5. ¿Cerebrales o de cualquier otra parte del sistema nervioso?");
		preguntas.add("6. ¿De los huesos, articulaciones, de la columna vertebral, deformidades, pérdida de algún miembro? ");
		preguntas.add("7. ¿Endocrinas o metabólicas: diabetes, obesidad, tiroides, hipófisis, otras?");
		preguntas.add("8. ¿Cáncer o cualquier otro tumor?");
		preguntas.add("9. ¿Transmisibles: hepatitis, Síndrome de Inmunodeficiencia Adquirida (SIDA) o portador del Virus de Inmunodeficiencia Humana (VIH), Virus del Papiloma Humano (VPH), sífilis o cualquier otra?");
		preguntas.add("10. ¿De los ojos o de los oídos?");
		preguntas.add("11. ¿Alguna otra enfermedad, afección o lesión distinta a las señaladas anteriormente, o está sujeto a cualquier tratamiento médico, rehabilitación o terapia por alguna enfermedad o lesión?");
		preguntas.add("12. ¿Le han practicado o tiene pendientes pruebas especiales de laboratorio, radiográficas, ultrasonido, resonancias magnéticas, biopsia, otros, y/o alguna intervención quirúrgica u hospitalización por cualquier otra causa?");
		preguntas.add("Mujeres:");
		preguntas.add("13. ¿Ha padecido o padece enfermedades en los ovarios, en la matriz o en los senos?");
		preguntas.add("14. ¿Está embarazada actualmente, ha tenido abortos, complicaciones en este embarazo o complicaciones en embarazos anteriores?");
		
		return preguntas;
		
	}
	
	private  ArrayList<String> textonotas() {
		ArrayList<String>  textonotas = new ArrayList<>();
	
		textonotas.add("Notas importantes"+
				"Este documento solo constituye una solicitud de seguro y, por tanto, no representa garantía alguna"+
				"de que la misma será aceptada por la Institución de Seguros, ni de que, en caso de aceptarse,"+
				"concuerde totalmente con los términos de la solicitud."+
				"Se previene al Contratante y al Solicitante que conforme a la Ley Sobre el Contrato de Seguro, deben declarar todos los"+
				"hechos importantes para la apreciación del riesgo a que se refiere esta solicitud, tal y como los conozcan o deban conocer"+
				"en el momento de firmar la solicitud, en la inteligencia de que la no declaración o la inexacta o falsa declaración de un"+
				"hecho de los mencionados podría originar la pérdida del derecho del Asegurado o del Beneficiario en su caso. Enterado"+
				"de lo que antecede y para efecto de esta solicitud de seguro, el Contratante o Solicitante declara estar dispuesto, si fuera"+
				"necesario, a pasar un examen médico por cuenta de la Compañía, si esta lo estima conveniente."+
				"AXA Seguros, S.A. de C.V., con domicilio en Avenida Félix Cuevas número 366, piso 3, Colonia Tlacoquemécatl, Alcaldía"+
				"Benito Juárez, C.P. 03200, Ciudad de México llevará a cabo el tratamiento de sus datos personales para evaluar la solicitud de"+
				"seguro y selección de riesgo, emisión de la póliza, el cumplimiento del contrato de seguro y demás finalidades contempladas"+
				"en el aviso de privacidad integral que puede consultar en axa.mx en la sección Aviso de Privacidad.");
	
				return textonotas;
	}
	
	 private boolean isEndOfPage(BaseTable table) {

	        float currentY = yStart - table.getHeaderAndDataHeight();
	        boolean isEndOfPage = currentY <= bottomMargin;
	        return isEndOfPage;
	    }
}
