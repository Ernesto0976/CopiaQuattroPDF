package com.copsis.models.bexmas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;


public class BexmasSaludModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Varaibles
	private String contenido = "";
	
	public  BexmasSaludModel(String contenido) {
		this.contenido = contenido;
	}
	
	
	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		boolean cp= true;
		boolean cdi = true;
		StringBuilder newcont = new StringBuilder();
		StringBuilder newcoberturas = new StringBuilder();
		StringBuilder newcob = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales()).replace("R .F.C:", "R.F.C:");
		
		try {
		
			modelo.setTipo(3);
			modelo.setCia(98);

	
			inicio = contenido.indexOf("Nombre del Contratante:");
			if(inicio == -1) {
				inicio = contenido.indexOf("Nombre del");
			}
			fin = contenido.indexOf("Datos de los Asegurados");
			
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").replace("12:00 HORAS", "").replace("ANUAL", "CONTADO"));
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					if(newcont.toString().split("\n")[i].contains("Contratante") && newcont.toString().split("\n")[i].contains("Póliza Inicial")) {
						modelo.setCteNombre(newcont.toString().split("\n")[i+1].split("###")[0].trim());
						modelo.setPoliza(newcont.toString().split("\n")[i+1].split("###")[newcont.toString().split("\n")[i+1].split("###").length-1]);
					}
					if(modelo.getPoliza().length() == 0  && newcont.toString().split("\n")[i].contains("Póliza Inicial:")) {
						modelo.setPoliza(newcont.toString().split("\n")[i].split("Póliza Inicial:")[1].replace("###", "").trim());
					}
					if(modelo.getCteNombre().length() == 0 && newcont.toString().split("\n")[i].contains("Contratante") ) {
						if(!newcont.toString().split("\n")[i+1].contains("R.F.C:")) {
							modelo.setCteNombre(newcont.toString().split("\n")[i+1].replace("###", "").trim());
						}
					
					}
	
					if(modelo.getCteNombre().length() == 0 && newcont.toString().split("\n")[i].contains("Nombre del") && newcont.toString().split("\n")[i].contains("Póliza Inicial")  ) {
						modelo.setCteNombre(newcont.toString().split("\n")[i+1].split("###")[0].replace("###", "").trim());
						modelo.setPoliza(newcont.toString().split("\n")[i+1].split("###")[newcont.toString().split("\n")[i+1].split("###").length-1]);
					}
					
					if(newcont.toString().split("\n")[i].contains("Moneda:") && newcont.toString().split("\n")[i].contains("Forma de pago:")  && newcont.toString().split("\n")[i+1].contains("Dirección") && cp) {						
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i+1]));
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i+1]));
						cp = false;
					}
					
					if(newcont.toString().split("\n")[i].contains("Moneda") && newcont.toString().split("\n")[i].contains("Forma de Pago") && cp) {
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i]));
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
						cp = false;
					}
					if(newcont.toString().split("\n")[i].contains("Dirección") && newcont.toString().split("\n")[i].contains("NACIONAL")) {
					    String direccion  =newcont.toString().split("\n")[i+1].contains("Vigencia") ? newcont.toString().split("\n")[i+1].split("Vigencia")[0] : newcont.toString().split("\n")[i+1];
					    direccion  +=newcont.toString().split("\n")[i+2].contains("Desde") ? newcont.toString().split("\n")[i+2].split("Desde")[0] : newcont.toString().split("\n")[i+2];
					    modelo.setCteDireccion( direccion.replace("###", ""));
					    cdi =false;
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i]));
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i]));
                    }
					
					
				
					if(newcont.toString().split("\n")[i].contains("Dirección") && cdi) {
						modelo.setCteDireccion((newcont.toString().split("\n")[i+1].length() > 1  ? newcont.toString().split("\n")[i+1].split("###")[0] : newcont.toString().split("\n")[i+1]) 
						 +" " + newcont.toString().split("\n")[i+2]);
					}
					if(newcont.toString().split("\n")[i].contains("Desde") && newcont.toString().split("\n")[i].contains("Hasta") && newcont.toString().split("\n")[i+1].contains("-")) {
					   
					    if(newcont.toString().split("\n")[i+1].split("###")[0].contains("-")) {
					        modelo.setVigenciaDe(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[0].trim()));
					        modelo.setVigenciaA(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[1].replace("12:00", "").trim()));
					    }
					    if(newcont.toString().split("\n")[i+1].split("###")[1].contains("-")) {
						
							if(newcont.toString().split("\n")[i+1].split("###").length ==2){
								modelo.setVigenciaA(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[1].replace("12:00", "").trim()));
								modelo.setVigenciaDe(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[0].trim()));
							}else{
								modelo.setVigenciaA(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[2].replace("12:00", "").trim()));
								modelo.setVigenciaDe(fn.formatDateMonthCadena(newcont.toString().split("\n")[i+1].split("###")[1].trim()));
							}
                        }
						modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					if(newcont.toString().split("\n")[i].contains("C.P:")) {					  
					    if(newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[1].contains("Contratante")) {
					     modelo.setCp(newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[0].trim());
					    }else {
						  modelo.setCp(newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[1].trim());
					    }
					}

					if(newcont.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###")[1].trim() );
					}	
					
				}
								
			}
			

				
			
			
			
			List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
			inicio = contenido.indexOf("Datos de los Asegurados");
			fin = contenido.indexOf("Detalle del Seguro");

	
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
					if(newcont.toString().split("\n")[i].split("-").length > 3) {
				
						
					if(!newcont.toString().split("\n")[i].contains("Nacimiento")) {						
						if(newcont.toString().split("\n")[i].split("###")[0].length() > 20) {
							asegurado.setNombre(newcont.toString().split("\n")[i].split("###")[0].trim());
						}else {					
							if(newcont.toString().split("\n")[i-1].contains("Nacimiento")) {
								asegurado.setNombre( newcont.toString().split("\n")[i].split("###")[0].trim());
							}else {								
								if(newcont.toString().split("\n")[i-1].split("-").length > 3){
									asegurado.setNombre((newcont.toString().split("\n")[i].split("###")[0]).trim());
								}else {
									asegurado.setNombre((newcont.toString().split("\n")[i-1]  +" " +newcont.toString().split("\n")[i].split("###")[0]).trim());
								}								
							}						
						}
						asegurado.setNacimiento(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[2]));
						
						if(newcont.toString().split("\n")[i].split("###")[3].contains("-")){
						
							asegurado.setAntiguedad(fn.formatDateMonthCadena(newcont.toString().split("\n")[i].split("###")[3]));
							
							asegurado.setParentesco(fn.parentesco(newcont.toString().split("\n")[i].split("###")[4]));	
							asegurado.setEdad(fn.castInteger(newcont.toString().split("\n")[i].split("###")[5].trim()));
							asegurado.setSexo(fn.sexo(newcont.toString().split("\n")[i].split("###")[6])  ? 1 : 0);
						}						
						asegurados.add(asegurado);
					 }
				   }								
				}
				
				modelo.setAsegurados(asegurados);
			}
			
			
			
			inicio = contenido.indexOf("Detalle del Seguro");
			fin = contenido.indexOf("Cobertura Básica");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					
					if(newcont.toString().split("\n")[i].contains("%")) {
						modelo.setPlan(newcont.toString().split("\n")[i].split("###")[0]);
						modelo.setSa(newcont.toString().split("\n")[i].split("###")[1]);
						modelo.setDeducible(newcont.toString().split("\n")[i].split("###")[2]);
						modelo.setCoaseguro(newcont.toString().split("\n")[i].split("###")[3]);			
					}
				}
			}
			

			inicio = contenido.indexOf("Cobertura Básica");
			fin = contenido.indexOf("En testimonio");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
		
			}
			
	
			
			inicio = contenido.indexOf("Coberturas Adicionales");
			fin = contenido.lastIndexOf("En testimonio de");
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcoberturas = new StringBuilder();
				newcoberturas.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", "").split("En testimonio de")[0]
				        .replace("MEDICAM FUERA DEL HOSPITAL###Deducible y Coaseguro contratado para", 
				                "MEDICAM FUERA DEL HOSPITAL###Deducible y Coaseguro contratado para la Cobertura Básica###Básica")
				        .replace("C.TRATAMIENTOS NO AMPARADOS###Deducible y Coaseguro Contratado para", "C.TRATAMIENTOS NO AMPARADOS###Deducible y Coaseguro Contratado para la Cobertura Básica###500,000 M.N.")
				        .replace("EMERGENCIA MÉDICA EXTRANJERO###Deducible de 100 USD, no aplica", "EMERGENCIA MÉDICA EXTRANJERO###Deducible de 100 USD, no aplica coaseguro###100,000 USD")
				        .replace("ENFERMEDADES GRAVES###Deducible y Coaseguro contratado para", "ENFERMEDADES GRAVES EXTRANJERO###Deducible y Coaseguro contratado para la Cobertura Básica###Básica")
				        );
		
			}
			
			newcob.append(newcont +" "+ newcoberturas);

			
			List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			if(newcob.toString().length() > 50) {
				for (int i = 0; i < newcob.toString().split("\n").length; i++) {
				  
					if(
	                  !newcob.toString().split("\n")[i].contains("Coberturas Adicionales")    
							) {		
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					
						switch (newcob.toString().split("\n")[i].split("###").length) {
						case 2:
						    if(!newcob.toString().split("\n")[i].contains("coaseguro") && !newcob.toString().split("\n")[i].contains("la Cobertura Básica")) {
						        if(newcob.toString().split("\n")[i].split("###")[0].length() >5) {
						        cobertura.setNombre(newcob.toString().split("\n")[i].split("###")[0]);                      
	                            coberturas.add(cobertura);
						        }
						    }
						
							break;
						case 3:
						    if(!newcob.toString().split("\n")[i].equals("EXTRANJERO###la Cobertura Básica###Básica")){
						        if(newcob.toString().split("\n")[i].split("###")[0].length() >5) {
						        cobertura.setNombre(newcob.toString().split("\n")[i].split("###")[0]);
	                            cobertura.setSa(newcob.toString().split("\n")[i].split("###")[2]);      
	                            coberturas.add(cobertura);
						        }
						    }
					
							break;								
						default:
							break;
						}											
					}
				}
				modelo.setCoberturas(coberturas);
			}
			
			
			
			
		
		
			inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("La presente carátula");
		
			if(inicio >  -1 && fin > -1  &&  inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio,fin).replace("@@@", "").replace("\r", ""));
				
				
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
				
					if(newcont.toString().split("\n")[i].contains("Prima Neta")) {
						 modelo.setPrimaneta(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
					if(newcont.toString().split("\n")[i].contains("Fraccionado")) {
						 modelo.setRecargo(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
					if(newcont.toString().split("\n")[i].contains("Expedición")) {
						 modelo.setDerecho(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
                    if(newcont.toString().split("\n")[i].contains("I.V.A.")) {
                    	 modelo.setIva(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
                    if(newcont.toString().split("\n")[i].contains("Prima Total")) {
                    	 modelo.setPrimaTotal(fn.castBigDecimal( fn.castDouble(newcont.toString().split("\n")[i].split("###")[1])));
					}
                    if(newcont.toString().split("\n")[i].contains("Agente")) {
               
                        if( newcont.toString().split("\n")[i].split("###").length >2) {
                            modelo.setCveAgente( newcont.toString().split("\n")[i].split("###")[1].replace("###", "").trim()); 
                        }else {
                            modelo.setCveAgente( newcont.toString().split("\n")[i].split("Agente:")[1].replace("###", "").trim());    
                        }
						
					}
				}
			}
			
		    List<EstructuraRecibosModel> recibos = new ArrayList<>();
            EstructuraRecibosModel recibo = new EstructuraRecibosModel();
            if(modelo.getFormaPago() == 1) {
                recibo.setReciboId("");
                recibo.setSerie("1/1");
                recibo.setVigenciaDe(modelo.getVigenciaDe());
                recibo.setVigenciaA(modelo.getVigenciaA());
                if (recibo.getVigenciaDe().length() > 0) {
                    recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
                }
                recibo.setPrimaneta(modelo.getPrimaneta());
                recibo.setDerecho(modelo.getDerecho());
                recibo.setRecargo(modelo.getRecargo());
                recibo.setIva(modelo.getDerecho());

                recibo.setPrimaTotal(modelo.getPrimaTotal());
                recibo.setAjusteUno(modelo.getAjusteUno());
                recibo.setAjusteDos(modelo.getAjusteDos());
                recibo.setCargoExtra(modelo.getCargoExtra());
                recibos.add(recibo);

            }
            modelo.setRecibos(recibos);
			
			return modelo;
		} catch (Exception ex) {
			modelo.setError(BexmasSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
					+ ex.getCause());
			return modelo;
		}
	}


}
