package com.copsis.models.bexmas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class BexmasAutosModel {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();

	private String contenido = "";

	public BexmasAutosModel(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		int inicio = 0;
		int fin = 0;
		StringBuilder newcont = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

		try {
			modelo.setTipo(1);
			modelo.setCia(98);

			inicio = contenido.indexOf("AUTOMÓVILES Y CAMIONES");
			fin = contenido.indexOf("Seguros Ve por Más");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {

					if (newcont.toString().split("\n")[i].contains("Póliza")
							&& newcont.toString().split("\n")[i].contains("Endoso")
							&& newcont.toString().split("\n")[i + 1].contains("ASEGURADO")) {
						modelo.setPoliza(newcont.toString().split("\n")[i + 2].split("###")[0]);
					}
					if (newcont.toString().split("\n")[i].contains("Nombre:")
							&& newcont.toString().split("\n")[i].contains("Agente")
							&& newcont.toString().split("\n")[i].contains("Moneda")) {
						modelo.setCteNombre(newcont.toString().split("\n")[i].split("Nombre:")[1].split("Agente")[0]
								.replace("###", "").trim());
						modelo.setMoneda(fn.buscaMonedaEnTexto(newcont.toString().split("\n")[i + 1]));
					}
					if (newcont.toString().split("\n")[i].contains("Dirección:")) {
						modelo.setCteDireccion(
								newcont.toString().split("\n")[i].split("Dirección:")[1].split("###")[0]);
					}
					if (newcont.toString().split("\n")[i].contains("Formas de Pago")) {
						modelo.setFormaPago(fn.formaPagoSring(newcont.toString().split("\n")[i + 1]));
					}
					if (newcont.toString().split("\n")[i].contains("C.P:")) {
						modelo.setCp(newcont.toString().split("\n")[i].split("C.P:")[1].split("###")[0].trim());
					}

					if (newcont.toString().split("\n")[i].contains("R.F.C:")) {
						modelo.setRfc(newcont.toString().split("\n")[i].split("R.F.C:")[1].split("###")[0].trim());
					}

					if (newcont.toString().split("\n")[i].split("-").length > 3) {
						modelo.setVigenciaDe(fn.formatDate(fn.formatDateMonthCadena(
								newcont.toString().split("\n")[i].split("###")[0].replace("12:00 Horas", "").trim())));
						modelo.setVigenciaA(fn.formatDate(fn.formatDateMonthCadena(
								newcont.toString().split("\n")[i].split("###")[1].replace("12:00 Horas", "").trim())));
						if (modelo.getVigenciaDe().length() > 0) {
							modelo.setFechaEmision(modelo.getVigenciaDe());
						}
					}

				}
			}

			inicio = contenido.indexOf("DESCRIPCIÓN DEL VEHÍCULO");
			fin = contenido.indexOf("DESGLOSE DE COBERTURAS");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					;
					if (newcont.toString().split("\n")[i].contains("Clave")
							&& newcont.toString().split("\n")[i].contains("Marca")
							&& newcont.toString().split("\n")[i].contains("Zona")) {
						if (newcont.toString().split("\n")[i].split("Marca:")[1].split("Zona")[0].trim()
								.replace("###", "").length() > 0) {
							modelo.setMarca(newcont.toString().split("\n")[i].split("Marca:")[1].split("Zona")[0].trim()
									.replace("###", "").split(" ")[1]);
							modelo.setClave(newcont.toString().split("\n")[i].split("Marca:")[1].split("Zona")[0].trim()
									.replace("###", "").split(" ")[0]);
						}

					}

					if (newcont.toString().split("\n")[i].contains("Descripción:")) {
						modelo.setDescripcion(
								newcont.toString().split("\n")[i].split("Descripción:")[1].replace("###", "").trim());
					}
				}
			}
			
			

			inicio = contenido.indexOf("Prima neta:");
			fin = contenido.indexOf("En testimonio de lo cual la institución");
			if (inicio > -1 && fin > -1 && inicio < fin) {

				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					
					if(newcont.toString().split("\n")[i].contains("Prima neta:")) {
						modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("neta:")[1].replace("###", "").trim())));
					}
					if(newcont.toString().split("\n")[i].contains("Recargos:")) {
					
						modelo.setRecargo(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Recargos:")[1].replace("###", "").trim())));
					}
					
					if(newcont.toString().split("\n")[i].contains("Derechos:")){
						modelo.setDerecho(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Derechos:")[1].replace("###", "").trim())));
					}
					
					if(newcont.toString().split("\n")[i].contains("I.V.A:")){		
						modelo.setIva(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("I.V.A:")[1].replace("###", "").trim())));
					}
					if(newcont.toString().split("\n")[i].contains("Prima total:")){		
						modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Prima total:")[1].replace("###", "").trim())));
					}
					if(newcont.toString().split("\n")[i].contains("1er Recibo")){		
						modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("1er Recibo")[1].replace("###", "").trim())));
					}
					if(newcont.toString().split("\n")[i].contains("Subsecuentes:")){		
						modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(newcont.toString().split("\n")[i].split("Subsecuentes:")[1].replace("###", "").trim())));
					}
				}
			}
			
			


			inicio = contenido.indexOf("DESGLOSE DE COBERTURAS");
			fin = contenido.indexOf("Prima neta:");
			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcont = new StringBuilder();
				newcont.append(contenido.substring(inicio, fin).replace("@@@", "").replace("\r", ""));
				for (int i = 0; i < newcont.toString().split("\n").length; i++) {
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
					if (!newcont.toString().split("\n")[i].contains("COBERTURAS")
							&& !newcont.toString().split("\n")[i].contains("Responsabilidad")
							&& !newcont.toString().split("\n")[i].contains("Observaciones")
							&& !newcont.toString().split("\n")[i].contains("Coberturas Amparada")) {

						switch (newcont.toString().split("\n")[i].split("###").length) {
						case 2:
							cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1]);						
							coberturas.add(cobertura);
							break;
						case 3:case 4:
							cobertura.setNombre(newcont.toString().split("\n")[i].split("###")[0]);
							cobertura.setSa(newcont.toString().split("\n")[i].split("###")[1]);
							cobertura.setDeducible(newcont.toString().split("\n")[i].split("###")[2]);
							coberturas.add(cobertura);										
							break;
						
						default:
							break;
						}


					}
				}
				modelo.setCoberturas(coberturas);
			}

			return modelo;
		} catch (Exception e) {
			return modelo;
		}

	}

}