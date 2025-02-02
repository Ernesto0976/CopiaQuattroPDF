	package com.copsis.models.allians;
	
	import java.util.ArrayList;
	import java.util.List;
	
	import com.copsis.models.DataToolsModel;
	import com.copsis.models.EstructuraBeneficiariosModel;
	import com.copsis.models.EstructuraCoberturasModel;
	import com.copsis.models.EstructuraJsonModel;
	
	public class AlliansVidaBModel {
		private DataToolsModel fn = new DataToolsModel();
		private EstructuraJsonModel modelo = new EstructuraJsonModel();
		
		public EstructuraJsonModel procesar( String contenido) {
			StringBuilder newcontenido = new StringBuilder();
			StringBuilder newdirec = new StringBuilder();
			int inicio = 0;
			int fin = 0;
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
					.replace("B E N E F I C I A R I O S", "BENEFICIARIOS");
			
			try {
				modelo.setTipo(5);		
				modelo.setCia(4);
				modelo.setMoneda(1);
	
				inicio = contenido.indexOf("Asegurado");
				fin  = contenido.indexOf("BENEFICIARIOS");	
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
					
					if(newcontenido.toString().split("\n")[i].contains("Asegurado") && newcontenido.toString().split("\n")[i].contains("Póliza")) {
						modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
						modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[1]);
					}
					if(newcontenido.toString().split("\n")[i].contains("Domicilio")) {
						newdirec.append(newcontenido.toString().split("\n")[i+1]+" ");
						newdirec.append(newcontenido.toString().split("\n")[i+2].split("###")[0] +" ");
						newdirec.append(newcontenido.toString().split("\n")[i+3]);
					}
					if(newcontenido.toString().split("\n")[i].contains("C.P.")) {
						modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0,5));
						if(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###").length > 2) {
							modelo.setRfc(newcontenido.toString().split("\n")[i].split("C.P.")[1].split("###")[1].replace(" ", ""));
						}
					}
				
					if(newcontenido.toString().split("\n")[i].contains("Fecha de Inicio") && newcontenido.toString().split("\n")[i].contains("Fin de Vigencia")
					&& newcontenido.toString().split("\n")[i+1].contains("Edad Actual")		) {                                  
					   if(newcontenido.toString().split("\n")[i+2].split("###").length == 5) {
						   modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[3].replace(" ", "-")));
						   modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[4].replace(" ", "-")));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					   }
						
					}
					
					if(newcontenido.toString().split("\n")[i].contains("Inicio de Vigencia") 
					&& newcontenido.toString().split("\n")[i].contains("Fin de Vigencia")
					&& newcontenido.toString().split("\n")[i+1].contains("Día Mes")) { 					                              
						
					   if(newcontenido.toString().split("\n")[i+2].split("###").length == 4) {
						   modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[2].replace(" ", "-")));
						   modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[3].replace(" ", "-")));
						modelo.setFechaEmision(modelo.getVigenciaDe());
					   }
					   
					   if(newcontenido.toString().split("\n")[i+2].split("###").length == 5) {						
						modelo.setVigenciaDe(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[3].replace(" ", "-")));
						modelo.setVigenciaA(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i+2].split("###")[4].replace(" ", "-")));
					 modelo.setFechaEmision(modelo.getVigenciaDe());
					}
					}										
					if(newcontenido.toString().split("\n")[i].contains("Forma de Pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+1]));
					}
					if(newcontenido.toString().split("\n")[i].contains("Fecha Inicio de Vigencia") 
					&& newcontenido.toString().split("\n")[i].contains("Fecha Fin de Vigencia")
					&& newcontenido.toString().split("\n")[i+1].contains("Día Mes Año")) { 	
						List<String> valores = fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2].replace("###", "-"));						
						if(!valores.isEmpty() && valores.size() == 3){
							modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(1)));
							modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(2)));
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
						
					}
				}
				
				modelo.setCteDireccion(newdirec.toString());
				
				
				if(modelo.getVigenciaDe().length()> 0 && modelo.getVigenciaA().length()> 0) {
					if(fn.diferencia(modelo.getVigenciaDe(), modelo.getVigenciaA()) > 3) {
						modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
					}
				}
				
	
				inicio = contenido.indexOf("COBERTURAS CONTRATADAS");
				fin  = contenido.indexOf("Beneficio por Fallecimiento");
				if(fin == -1){
					fin  = contenido.indexOf("Coberturas Adicionales");	
				}

				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido).trim());
				if(newcontenido.length()> 50){
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS") && !newcontenido.toString().split("\n")[i].contains("Beneficio Básico")
					&& !newcontenido.toString().split("\n")[i].contains("Suma Asegurada")	) {
					
						cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0].trim());
						
						if(newcontenido.toString().split("\n")[i].split("###")[0].trim().contains("Fallecimiento") || 
						newcontenido.toString().split("\n")[i].split("###")[0].trim().contains("Accidental")){
							List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);							
							modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(valores.get(1))));
							modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(valores.get(1))));
						}
						
						if(newcontenido.toString().split("\n")[i].trim().contains("Renta Variable Dólares") ){							
							List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i].replace(",", ""));							
							modelo.setPrimaneta(fn.castBigDecimal(fn.cleanString(valores.get(0))));
							modelo.setPrimaTotal(modelo.getPrimaneta());
						}
						cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1].trim());
						coberturas.add(cobertura);
					}
				}
				modelo.setCoberturas(coberturas);
			}
				
				
				
				inicio = contenido.indexOf("BENEFICIARIOS");
				fin  = contenido.indexOf("Advertencias");	
				fin  = fin == -1 ? contenido.indexOf("En cumplimiento a"):fin;	
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido));
				if(newcontenido.length() > 0){
					List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
					for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
						EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
						if(!newcontenido.toString().split("\n")[i].contains("Nombre") && !newcontenido.toString().split("\n")[i].contains("BENEFICIARIOS")
							&&	!newcontenido.toString().split("\n")[i].contains("Fallecimiento") 		) {						
							beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
							beneficiario.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[1]));						
							beneficiarios.add(beneficiario);
						}
					}
					modelo.setBeneficiarios(beneficiarios);
				}
			
	
				inicio = contenido.indexOf("Aportaciones Comprometidas");
				fin  = contenido.indexOf("BENEFICIARIOS");	
				newcontenido = new StringBuilder();
				newcontenido.append(fn.extracted(inicio, fin, contenido));
				for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {	
					 if(newcontenido.toString().split("\n")[i].contains("Periodicidad")){
						modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));					
						List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);						
						modelo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(valores.get(0))));
					 }
				}
			
				
				return modelo;
			} catch (Exception ex) {
			
				modelo.setError(AlliansVidaBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "+ ex.getCause());
				return modelo;
			}
			
		}
	}
	