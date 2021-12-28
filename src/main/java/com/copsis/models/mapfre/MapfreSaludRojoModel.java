package com.copsis.models.mapfre;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class MapfreSaludRojoModel {
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";

	public MapfreSaludRojoModel(String contenido) {
		this.contenido = contenido;
	}
	public EstructuraJsonModel procesar() {
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("COBERTURAS###Y###SERVICIOS", "COBERTURAS Y SERVICIOS")
				.replace("FECHA###DE###EMISIÓN", "FECHA DE EMISIÓN")
				.replace("LAS###12:00###HRS.###DEL:", "")
				.replace("PLAN###CONTRATADO:", "PLAN CONTRATADO:")
				.replace("ZONA###DE###CONTRATACIÓN", "ZONA DE CONTRATACIÓN")
				.replace("FORMA###DE###PAGO:", "FORMA DE PAGO:")
				.replace("PRIMA###NETA", "PRIMA NETA")
				.replace("PRIMA###TOTAL:", "PRIMA TOTAL:");
		String newcontenido = "";
		int inicio = 0;
		int fin = 0;
		try {
			   String aed = "";
	            if (contenido.contains("Endoso Número")) {
	                aed = "Endoso Número";
	            } else {
	                aed = "Endoso";
	            }
			
			modelo.setTipo(3);
			modelo.setCia(22);

			inicio = contenido.indexOf("PROTECCION MEDICA");
			fin = contenido.indexOf("COBERTURAS Y SERVICIOS");
			if(inicio == -1 && fin == -1) {
				inicio = contenido.indexOf("GASTOS MÉDICOS MAYORES");
				fin = contenido.indexOf("ASEGURADO###TITULAR");
			}
			

			

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "").replace("######","###");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					if(newcontenido.split("\n")[i].contains("PÓLIZA-ENDOSO")  && newcontenido.split("\n")[i].split("PÓLIZA-ENDOSO")[1].replace("###", "").trim().contains("-")) {
						
							 modelo.setPoliza(newcontenido.split("\n")[i].split("PÓLIZA-ENDOSO")[1].replace("###", "").trim().replace("-", "/"));							 
										
					}
					if(newcontenido.split("\n")[i].contains("FECHA DE EMISIÓN")) {
						modelo.setFechaEmision(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("FECHA DE EMISIÓN")[1].replace("###", "").trim()));
						if(newcontenido.split("\n")[i+1].contains("AGENTE:")) {
							modelo.setAgente(newcontenido.split("\n")[i+1].split("AGENTE:")[1].replace("###", "").trim());
						}
						if(newcontenido.split("\n")[i+2].contains("CLAVE DE AGENTE:")) {
							modelo.setCveAgente(newcontenido.split("\n")[i+2].split("AGENTE:")[1].replace("###", "").trim());
						}
					}
					if(newcontenido.split("\n")[i].contains("DESDE") && newcontenido.split("\n")[i].contains("TIPO")) {
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.split("\n")[i].split("DESDE")[1].split("TIPO")[0].replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("HASTA") && newcontenido.split("\n")[i].contains("CLIENTE")) {
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.split("\n")[i].replace("HASTA","HASTA:").replace("::", ":").split("HASTA:")[1].split("CLIENTE")[0].replace("###", "")));
					}
					if(newcontenido.split("\n")[i].contains("PLAN CONTRATADO:")){
						modelo.setPlan((newcontenido.split("\n")[i].split("PLAN CONTRATADO:")[1] +" "+ newcontenido.split("\n")[i+1]).replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("CONTRATANTE:") &&  newcontenido.split("\n")[i].contains("ZONA DE CONTRATACIÓN:")){
						modelo.setCteNombre(newcontenido.split("\n")[i].split("CONTRATANTE:")[1].split("ZONA")[0].replace("###", "").trim());
					}
					if(modelo.getCteNombre().isEmpty() && newcontenido.split("\n")[i].contains("CONTRATANTE:") &&  newcontenido.split("\n")[i+1].contains("DOMICILIO:")) {
						modelo.setCteNombre(newcontenido.split("\n")[i].split("CONTRATANTE:")[1].replace("###", "").trim());
					}
					
					if(newcontenido.split("\n")[i].contains("DOMICILIO:")){
						modelo.setCteDireccion((newcontenido.split("\n")[i].split("DOMICILIO:")[1] +" "+newcontenido.split("\n")[i+1]).replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("R.F.C:")){
						modelo.setRfc(newcontenido.split("\n")[i].split("R.F.C:")[1].replace("###", "").trim());
					}
					if(newcontenido.split("\n")[i].contains("C.P:")){
						modelo.setCp(newcontenido.split("\n")[i].split("C.P:")[1].replace("###", "").trim());
					}
							
					
				}
			}
			
			modelo.setMoneda(1);
	
			inicio = contenido.indexOf("FORMA DE PAGO:");
			fin = contenido.indexOf("PRÁCTICA DE DEPORTE");
			if(fin == -1) {
				fin = contenido.indexOf("ENDOSO DEL FACTOR");
			}
			if(fin == -1) {
				fin = contenido.indexOf("Av.###Revolución");
			}
			
			if(fin < inicio) {
				fin = contenido.lastIndexOf("Av.###Revolución");
			}


			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				modelo.setFormaPago(fn.formaPagoSring(newcontenido));
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					if(newcontenido.split("\n")[i].contains("PRIMA NETA:")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("PRIMA NETA:")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("EXPEDICIÓN")) {
						modelo.setDerecho(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("EXPEDICIÓN:")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("FRACCIONADO") && newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("RENOVACIÓN") && newcontenido.split("\n")[i].contains("MONTO") && newcontenido.split("\n")[i].contains("I.V.A.")) {
						modelo.setIva(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("I.V.A.")[1].replace("###", "").trim())));
					}
					if(newcontenido.split("\n")[i].contains("FRACCIONADO:") && newcontenido.split("\n")[i].contains("PRIMA TOTAL:")) {		
						if(newcontenido.split("\n")[i].split("FRACCIONADO:")[1].split("PRIMA")[0].replace("###", "").trim().contains("|")) {						
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("FRACCIONADO:")[1].split("PRIMA")[0].replace("|", "###").split("###")[2].trim())));	
						}else {
							modelo.setRecargo(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("FRACCIONADO:")[1].split("PRIMA")[0].replace("###", "").trim())));
						}											
						modelo.setPrimaTotal(fn.castBigDecimal(fn.preparaPrimas(newcontenido.split("\n")[i].split("TOTAL:")[1].replace("###", "").trim())));
					}						
				}
			}

			
			 if (contenido.contains("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD") || contenido.contains("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD") && contenido.contains("Av.###Revolución")) {
	                newcontenido = contenido.split("NOMBRE###EDAD###SEXO###EXTRAPRIMA###ANTIGÜEDAD###ANTIGÜEDAD")[1].split("Av.###Revolución#")[0];
	                List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
	                String fecha;
	                for (String a : newcontenido.split("\r\n")) {
	                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
	                    int sp = a.split("###").length;
	        
	                  
	                    if (sp == 6) {
	                
	                    	asegurado.setNombre(a.split("###")[0].replace("@@@", "").trim());
	                    	asegurado.setSexo(fn.sexo(a.split("###")[2]) ? 1:0);
	                    	fecha = a.split("###")[a.split("###").length-1].trim();
	                    	if(fecha.length() == 8) {	                    		
	                              asegurado.setAntiguedad(fecha.substring(4,8) +"-"+fecha.substring(2,4) +"-"+fecha.substring(0,2));		
	                    	}
	                    	asegurados.add(asegurado);
	                       
	                    }
	                    if (sp == 5) {
	                   
	                    	asegurado.setNombre(a.split("###")[0].replace("@@@", "").trim());
	                    	asegurado.setSexo(fn.sexo(a.split("###")[2]) ? 1 : 0);
	                    	fecha = a.split("###")[a.split("###").length-1].trim();
	                    	if(fecha.length() == 8) {	                    		
	                              asegurado.setAntiguedad(fecha.substring(4,8) +"-"+fecha.substring(2,4) +"-"+fecha.substring(0,2));		
	                    	}
	                     
	                        asegurados.add(asegurado);
	                    }
	                }
	                modelo.setAsegurados(asegurados);
	            }
			
			 if(modelo.getAsegurados().isEmpty()) {
				 newcontenido = contenido.split("RIESGO###NOMBRES###SEXO###EDAD###PARENTESCO###FECHA DE###ANTIGÜEDAD")[1].split("FECHAS###DE###ANTIGÜEDAD")[0];
				 
				 List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
				 for (String a : newcontenido.split("\r\n")) {
	                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
	                    if(a.split("-").length > 3) {
	                    	 int sp = a.split("###").length;
	                         if (sp == 7) {	         	                
	 	                    	asegurado.setNombre(a.split("###")[1].replace("@@@", "").trim());
	 	                    	asegurado.setSexo(fn.sexo(a.split("###")[2]) ? 1:0);
	 	                    	asegurado.setParentesco(fn.parentesco(a.split("###")[4]));
	 	                    	asegurado.setNacimiento(fn.formatDateMonthCadena(a.split("###")[5]));
	 	                    	asegurado.setFechaAlta(fn.formatDateMonthCadena(a.split("###")[6]));
	 	                    	asegurados.add(asegurado);
	 	                       
	 	                    }

	                    }
	                    
				 }
				 modelo.setAsegurados(asegurados);
			 }
			
			
			
			 newcontenido ="";
			 for (int i = 0; i < contenido.split("COBERTURAS Y SERVICIOS").length; i++) {
	                if (contenido.split("COBERTURAS Y SERVICIOS")[i].contains("VER ANEXOS")) {
	                    newcontenido += contenido.split("COBERTURAS Y SERVICIOS")[i].split("VER ANEXOS")[0];
	                }
	                if (contenido.split("COBERTURAS Y SERVICIOS")[i].contains("CONCEPTOS###ECONÃ“MICOS")) {
	                    newcontenido += contenido.split("COBERTURAS Y SERVICIOS")[i].split("CONCEPTOS###ECONÃ“MICOS")[0];
	                }
	            }
	            newcontenido = fn.gatos(newcontenido).replace("### ### ### ", "").replace("@@@", aed).replace("EndosoElemental", "Elemental")
	            		.replace("EndosoAsistencia", "Asistencia")
	            		.replace("ReducciÃ³n de deducible por", "ReducciÃ³n de deducible por accidente");
	          	            
	            if(newcontenido.contains("R.F.C:") && newcontenido.contains("SEXO") &&  newcontenido.contains("EDAD")) {
	            	newcontenido ="";
	            	inicio = contenido.indexOf("COBERTURAS###SUMA###DEDUCIBLE###COAS");
	            	fin = contenido.indexOf("LAS###ANTERIORES");
	            	if(inicio  > -1 && fin > -1 && inicio < fin ) {
	            		newcontenido = contenido.substring(inicio,fin).replace("@@@COBERTURA", "COBERTURA")
		            			.replace("INCREMENTO DE", "INCREMENTO DE HON-QUIRÚRGICOS")
		            			.replace("ELIM.DE DED.", "ELIM.DE DED. ACCIDENTE Cob. Nal.");		
	            	}	                        	
	            }
	            

	            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
	            for (String x : newcontenido.split("\r\n")) {
	               
	                int sp = x.split("###").length;

	                if (!x.contains("ASEGURADA") && !x.contains("COBERTURAS") && !x.contains("COASEGURO") && !x.contains("a###a") && !x.contains("ContinuaciÃ³n")
	                		&& !x.contains("EXTRANJERO###URO")
	                		) {	                
	                	 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
	              
	                    if (sp == 7 || sp == 6 || sp ==5) {
	                        cobertura.setNombre(x.split("###")[0]);
	                        cobertura.setSa(x.split("###")[1].replace("\r", ""));
	                        cobertura.setDeducible(x.split("###")[2]);
	                        cobertura.setCoaseguro(x.split("###")[3]);
	                        coberturas.add(cobertura);	
	                    }
	                    if (sp == 3 || sp == 2) {
	                        cobertura.setNombre(x.split("###")[0]);
	                        cobertura.setSa(x.split("###")[1].replace("\r", ""));
	                        coberturas.add(cobertura);	
	                    }
	                }

	            }
	            modelo.setCoberturas(coberturas);
			
			
			
			return modelo;
		} catch (Exception e) {
			return modelo;
		}
	}

}