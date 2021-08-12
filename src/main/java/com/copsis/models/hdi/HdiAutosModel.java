package com.copsis.models.hdi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;


public class HdiAutosModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	private String newcontenido = "";
	private String resultado = "";
	private String resultadoCbo = "";
	private int inicio = 0;
	private int fin = 0;
	private int donde = 0;
	private Boolean primas = true;

	public HdiAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("las 12:00 hrs. del", "")
				.replace("Prima neta", "Prima Neta")
				.replace("SEMESTRAL EFECTIVO", "SEMESTRAL")
				.replace("Individual", "")
				.replace("C l a v e : ", "Clave:");
		try {
			// tipo
			modelo.setTipo(1);
			// cia
			modelo.setCia(14);


			
			inicio = contenido.indexOf("PÓLIZA DE SEGURO");
			fin = contenido.indexOf("Prima Neta");
			

			if (inicio > 0 & fin > 0 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
			
					if (newcontenido.split("\n")[i].contains("responsabilidad máxima")) {
						if(newcontenido.split("\n")[i + 1].trim().contains("Datos Generales de la Póliza")) {
							modelo.setCteNombre(newcontenido.split("\n")[i + 2].split("###")[0].trim());
						}else {
							modelo.setCteNombre(newcontenido.split("\n")[i + 1].trim());	
						}
						
					}

					if (newcontenido.split("\n")[i].contains("RFC:")) {
						modelo.setRfc(newcontenido.split("\n")[i].split("RFC:")[1].trim());
						if(newcontenido.split("\n")[i + 1].contains("Formea de Pago")) {
							modelo.setCteDireccion(newcontenido.split("\n")[i + 1].split("Forma de Pago")[0].replace("###", "").trim());
							modelo.setFormaPago(fn.formaPago(newcontenido.split("\n")[i + 1].split("Forma de Pago:")[1].trim()));
						}else {
							modelo.setCteDireccion(newcontenido.split("\n")[i + 1]);
						}
						
					}
					if (newcontenido.split("\n")[i].contains("C.P") && newcontenido.split("\n")[i].contains("Tel:")) {
						modelo.setCp(newcontenido.split("\n")[i].split("C.P")[1].split("Tel:")[0].trim());
					}
					if (newcontenido.split("\n")[i].contains("Póliza:")&& newcontenido.split("\n")[i].contains("Vigencia")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split("Póliza:")[1].split("Vigencia")[0]
								.replace("###", "").replace("-", "").trim());
						modelo.setPolizaGuion(newcontenido.split("\n")[i].split("Póliza:")[1].split("Vigencia")[0].replace("###", "").trim());
						if(newcontenido.split("\n")[i].split("-").length > 3){
							
							modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Desde")[1].split("Hasta")[0].replace("###", "").trim()));
							modelo.setFechaEmision(modelo.getVigenciaDe());
							modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("Hasta")[1].replace("###", "").trim()));
						}
						
					}
					if(modelo.getVigenciaA().length() == 0  && modelo.getVigenciaDe().length() == 0 ) {
						if(newcontenido.split("\n")[i].split("-").length > 3){
							modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("###")[0].replace("###", "").trim()));
							modelo.setFechaEmision(modelo.getVigenciaDe());
							modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split("\n")[i].split("###")[1].replace("###", "").trim()));
						}
					}
					
					if (newcontenido.split("\n")[i].contains("Agente:")) {
						if (newcontenido.split("\n")[i].split("###").length > 1) {
							if(newcontenido.split("\n")[i].contains("Folio")) {
								modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].trim().split(" ")[0]);
								modelo.setAgente(newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].split("Folio")[0].replace("###", "").trim());
							}
						} else {
							modelo.setCveAgente(newcontenido.split("\n")[i].split("Agente:")[1].trim().split(" ")[0]);
							modelo.setAgente(newcontenido.split("\n")[i].split(modelo.getCveAgente())[1].trim());
						}
					}
					
					if (newcontenido.split("\n")[i].contains("Clave:")&& newcontenido.split("\n")[i].contains("Puertas:")) {
						modelo.setDescripcion(newcontenido.split("\n")[i].split("Clave:")[0].replace("###", "").trim());
						modelo.setClave(newcontenido.split("\n")[i].split("Clave:")[1].split("Puertas:")[0]
								.replace("###", "").trim());
						if(modelo.getDescripcion().contains(",")) {
							modelo.setMarca(modelo.getDescripcion().split(",")[0]);
							modelo.setModelo(Integer.parseInt(fn.numTx(modelo.getDescripcion())));
						}
						
					}
					if(modelo.getClave().length() == 0 ) {
						
						if (newcontenido.split("\n")[i].contains("Clave:") && newcontenido.split("\n")[i].contains("Descripción")){
						 modelo.setClave( newcontenido.split("\n")[i].split("Clave:")[1].split("Descripción")[0].replace(" ", "").replace("###", "").trim());
						}						
					}
				
					if(modelo.getModelo() == 0 ) {

							if(newcontenido.split("\n")[i].contains("Modelo:")) {
									modelo.setModelo(Integer.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].trim()));			
							}
							if(newcontenido.split("\n")[i].contains("Condiciones Particulares")) {
								modelo.setDescripcion(newcontenido.split("\n")[i+1].split("###")[0]);
								if(modelo.getDescripcion().contains(",")) {
									modelo.setMarca(modelo.getDescripcion().split(",")[0]);								
								}
							}
						
					}
					
		
					if (newcontenido.split("\n")[i].contains("Serie:")
							&& newcontenido.split("\n")[i].contains("Cilindros:")) {
						modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].split("Cilindros")[0]
								.replace("###", "").trim());
					}
					if(modelo.getSerie().length() == 0) {
						if (newcontenido.split("\n")[i].contains("Serie:")) {
							modelo.setSerie(newcontenido.split("\n")[i].split("Serie:")[1].split("###")[0].trim());
						}
						
					}
					
					if (newcontenido.split("\n")[i].contains("Paquete:")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("Paquete:")[1].split("###")[0].trim());

					}
					if (newcontenido.split("\n")[i].contains("ANUAL")) {
						modelo.setFormaPago(1);
					}
					if (newcontenido.split("\n")[i].contains("MENSUAL")) {
						modelo.setFormaPago(4);
					}
					if (newcontenido.split("\n")[i].contains("SEMESTRAL")) {
						modelo.setFormaPago(2);
					}
				}

			}

			modelo.setMoneda(1);

			// Primas
			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Art. ###25");
			
			if(fin <  inicio) {
				fin = contenido.indexOf("Página");
			}

		
			if (inicio > 0 & fin > 0 & inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {					
					if (newcontenido.split("\n")[i].contains("Fraccionado") && newcontenido.split("\n")[i].contains("Total a Pagar")) {
						
						if(newcontenido.split("\n")[i].split("###").length ==  5) {
							primas = false;
							modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[0])));
							modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[3])));
							modelo.setRecargo( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[5])));
							modelo.setIva( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[6])));
							modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i+1].split("###")[7])));							
						}
					}						
				}

				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(primas == true  ) {
						if (newcontenido.split("\n")[i].contains("Prima Neta")) {
							modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Prima Neta")[1].split("###")[1])));
						}
						if (newcontenido.split("\n")[i].contains("fraccionado")) {
						
							modelo.setRecargo( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Recargo por pago fraccionado")[1].split("###")[1])));
						}
						if (newcontenido.split("\n")[i].contains("Derecho de póliza")) {
							modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Derecho de póliza")[1].split("###")[1])));
						}
						if (newcontenido.split("\n")[i].contains("I.V.A.")) {
							modelo.setIva( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("I.V.A.")[1].split("###")[1])));
						}
						if (newcontenido.split("\n")[i].contains("Total a pagar")) {
							modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcontenido.split("\n")[i].split("Total a pagar")[1].split("###")[1])));
						}
					}
					
				}
			}
			
		
			inicio = contenido.indexOf("Descripción###Límite de Responsabilidad");
			fin = contenido.indexOf("Prima de");

			if (inicio > 0 & fin > 0 & inicio < fin) {
				  List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("\r", "").replace("@@@", "")
						.replace("Número de motor: ###", "")
						.replace("S e r v i c io", "Servicio");
				
				
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					int sp  = newcontenido.split("\n")[i].split("###").length;
					if(newcontenido.split("\n")[i].contains("Deducible")) {						
					}else {
						if (sp > 1) {
							
							switch (sp) {
							case 2:
								
								cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
								cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
								coberturas.add(cobertura);
								break;
							case 3:
;
								if(newcontenido.split("\n")[i].split("###")[0].contains("Placas")
								|| newcontenido.split("\n")[i].split("###")[0].contains("Servicio")
								|| newcontenido.split("\n")[i].split("###")[0].contains("Circulación")
								|| newcontenido.split("\n")[i].split("###")[0].contains("Acondicionado")
								|| newcontenido.split("\n")[i].split("###")[0].contains("Ocupantes")
								|| newcontenido.split("\n")[i].split("###")[0].contains("Transmisión")
								|| newcontenido.split("\n")[i].split("###")[0].contains("Puertas")
								|| newcontenido.split("\n")[i].split("###")[0].contains("Remolque")
										) {
									cobertura.setNombre(newcontenido.split("\n")[i].split("###")[1]);
									cobertura.setSa(newcontenido.split("\n")[i].split("###")[2]);
								
									coberturas.add(cobertura);
								}else {
									cobertura.setNombre(newcontenido.split("\n")[i].split("###")[0]);
									cobertura.setSa(newcontenido.split("\n")[i].split("###")[1]);
									cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2]);
									coberturas.add(cobertura);
								}
								
								break;
							}

						}
						modelo.setCoberturas(coberturas);						
					}				
				}
				
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(HdiAutosModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}

	}
}
