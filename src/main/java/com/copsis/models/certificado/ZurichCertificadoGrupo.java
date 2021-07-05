package com.copsis.models.certificado;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.banorte.BanorteAutosModel;

import netscape.javascript.JSObject;

public class ZurichCertificadoGrupo {

	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// variables
	private String contenido = "";
	private String newcontenido = "";
	private String recibosText = "";
	private String resultado = "";
	private int inicio = 0;
	private int fin = 0;

	public ZurichCertificadoGrupo(String contenido) {
		this.contenido = contenido;
		// this.recibosText = recibos;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Empleados ###y ###sus", "Empleados y sus");
			
		try {
			inicio = contenido.indexOf("Categoria");
			if (inicio > 0) {
				newcontenido = contenido.split("Categoria")[1].split("\n")[0].replace("@@@", "").replace("###", "")
						.replace("\r", "").trim();
				modelo.setCategoria(newcontenido);
			}

			inicio = contenido.indexOf("Subgrupo");
			if (inicio > 0) {
				newcontenido = contenido.split("Subgrupo")[1].split("\n")[0].replace("@@@", "").replace("###", "")
						.replace("\r", "").trim();
				modelo.setSubgrupo(newcontenido);
			}
			inicio = contenido.indexOf("Plan:");
			if (inicio > 0) {
				newcontenido = contenido.split("Plan:")[1].split("\n")[0].replace("@@@", "").replace("###", "")
						.replace("\r", "").trim();
				modelo.setPlan(newcontenido);
			}
			
			for (int i = 0; i < contenido.split("Asegurado").length; i++) {

				if (contenido.split("Asegurado")[i].contains("Empleados y sus")) {
					newcontenido += contenido.split("Asegurado")[i].split("Empleados y sus")[0].replace("@@@", "").replace("ITULAR", "###TITULAR")
							.replace("ONYUGE", "###CONYUGE").replace("###T###", "###")
							.replace("\r", "");
				}
			}

			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			JSONObject jdatos = new JSONObject();
			for (int i = 0; i < newcontenido.split("\n").length; i++) {
	
				EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();

				if (newcontenido.split("\n")[i].split("-").length > 3) {

					asegurado.setAntiguedad(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
							.split("###")[newcontenido.split("\n")[i].split("###").length - 1]));
					asegurado.setFechaAlta(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
							.split("###")[newcontenido.split("\n")[i].split("###").length - 2]));			
						asegurado.setNacimiento(fn.formatDate_MonthCadena(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 3]));
						asegurado.setEdad(Integer.parseInt( newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 4].trim()));
					if (newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length - 5].contains("TITULAR")) {
						asegurado.setParentesco(1);
						asegurado.setNombre(
								newcontenido.split("\n")[i].split("TITULAR")[0].replace("@@@", "").replace("###", " ").trim());
					}else if (newcontenido.split("\n")[i].split("###")[newcontenido.split("\n")[i].split("###").length - 5]
							.contains("CONYUGE")) {
						asegurado.setParentesco(2);
						asegurado.setNombre(
								newcontenido.split("\n")[i].split("CONYUGE")[0].replace("@@@", "").replace("###", " ").trim());
					}else {
						asegurado.setParentesco(3);		
						asegurado.setNombre(newcontenido.split("\n")[i].split(newcontenido.split("\n")[i]
								.split("###")[newcontenido.split("\n")[i].split("###").length - 4])[0].replace("@@@", "").replace("###", " ").trim());
					}
					asegurados.add(asegurado);
				} 

			}
			modelo.setAsegurados(asegurados);

			return modelo;
		} catch (Exception ex) {

			modelo.setError(ZurichCertificadoGrupo.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}

}