package com.copsis.models.chubb.autos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

import lombok.Data;

@Data
public class ChubbAutosModel {
	// Clases
	DataToolsModel fn = new DataToolsModel();
	EstructuraJsonModel modelo = new EstructuraJsonModel();

	// Variables
	private String contenido;
	private String recibos = "";
	private int inicio = 0;
	private int fin = 0;
	String newcontenido = "";
	BigDecimal restoPrimaTotal = BigDecimal.ZERO;
	BigDecimal restoDerecho = BigDecimal.ZERO;
	BigDecimal restoIva = BigDecimal.ZERO;
	BigDecimal restoRecargo = BigDecimal.ZERO;
	BigDecimal restoPrimaNeta = BigDecimal.ZERO;
	BigDecimal restoAjusteUno = BigDecimal.ZERO;
	BigDecimal restoAjusteDos = BigDecimal.ZERO;
	BigDecimal restoCargoExtra = BigDecimal.ZERO;

	public EstructuraJsonModel procesar() {
		try {

			// Variables
			/*
			 * String F_DEPAGO; BigDecimal r_prima_neta; BigDecimal r_recargo; BigDecimal
			 * r_derecho; BigDecimal r_iva; BigDecimal r_prima_total; BigDecimal r_ajuste2;
			 * BigDecimal r_ajuste1; int numero_recibo;
			 */

			/* int donde = 0; */
			// String resultado = "";
			/*
			 * float restoPrimaTotal = 0; float restoDerecho = 0; float restoIva = 0; float
			 * restoRecargo = 0; float restoPrimaNeta = 0; float restoAjusteUno = 0; float
			 * restoAjusteDos = 0; float restoCargoExtra = 0;
			 */
			String separador = "###";
			String saltolinea = "\r\n";
			List<String> conceptos;
			List<String> conceptosFin;

			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
			contenido = contenido.replace("Prima neta", "Prima Neta");

			// tipo
			modelo.setTipo(1);

			// aseguradora
			modelo.setCia(1);

			// Ramo
			modelo.setRamo("Autos");

			// Moneda
			modelo.setMoneda(
					fn.moneda(contenido.split("Moneda:")[1].split("Forma de pago:")[0].replace("###", "").trim()));

			// Renovacion
			conceptos = Arrays.asList("Póliza anterior:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Póliza anterior:###":
						inicio = inicio + 19;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setRenovacion(newcontenido.split("Moneda")[0].replace("###", "").trim());
						break;
					}
				}
			}

			// Plan
			conceptos = Arrays.asList("Paquete:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Paquete:###":
						inicio = inicio + 11;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setPlan(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// FormaPago
			conceptos = Arrays.asList("Forma de pago:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Forma de pago:":
						inicio = inicio + 14;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setFormaPago(fn.formaPago(newcontenido.split(saltolinea)[0].trim()));
						break;
					}
				}
			}

			// poliza
			conceptos = Arrays.asList("Póliza:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Póliza:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setPoliza(newcontenido.split(separador)[0].trim() + " "
								+ newcontenido.split(separador)[1].trim());
						break;
					}
				}
			}

			// endoso
			conceptos = Arrays.asList("Endoso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Endoso:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setEndoso(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// Contratante
			conceptos = Arrays.asList("Propietario-Contratante:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Propietario-Contratante:###":
						inicio = inicio + 27;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setContratante(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// CteNombre
			conceptos = Arrays.asList("Datos del asegurado y-o propietario");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Datos del asegurado y-o propietario":
						inicio = inicio + 35;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setCteNombre(newcontenido.split(saltolinea)[1].split(separador)[1].trim());
						break;
					}
				}
			}

			// CteDireccion
			conceptos = Arrays.asList("Domicilio:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Domicilio:###":
						inicio = inicio + 13;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setCteDireccion(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// rfc
			conceptos = Arrays.asList("R.F.C.:", "RFC:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "R.F.C.:":
					case "RFC:###":
						inicio = inicio + 7;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						modelo.setRfc(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// Inciso
			conceptos = Arrays.asList("Inciso:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Inciso:###":
						inicio = inicio + 10;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						if (NumberUtils.isParsable(newcontenido.split(separador)[0].trim())) {
							modelo.setInciso(Integer.parseInt(newcontenido.split(separador)[0].trim()));
						}
						break;
					}
				}
			}

			// Primaneta
			conceptos = Arrays.asList("Prima Neta###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Prima Neta###":
						inicio = inicio + 13;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setPrimaneta(
									Float.parseFloat(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// PrimaTotal
			conceptos = Arrays.asList("Prima total###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Prima total###":
						inicio = inicio + 14;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setPrimaTotal(
									Float.parseFloat(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// iva
			conceptos = Arrays.asList("I.V.A.###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "I.V.A.###":
						inicio = inicio + 9;
						newcontenido = contenido.substring(inicio, (inicio + 100));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setIva(Float.parseFloat(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// CveAgente
			conceptos = Arrays.asList("Clave interna del agente:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Clave interna del agente:":
						inicio = inicio + 25;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setCveAgente(newcontenido.contains("-") ? newcontenido.split("-")[0].trim() : "");
						break;
					}
				}
			}

			// Agente
			conceptos = Arrays.asList("Clave interna del agente:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Clave interna del agente:":
						inicio = inicio + 25;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setAgente(newcontenido.split(saltolinea)[0].contains("-")
								? newcontenido.split(saltolinea)[0].split("-")[2].trim()
								: "");
						break;
					}
				}
			}

			// VigenciaDe
			conceptos = Arrays.asList("Vigencia:###Del###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Vigencia:###Del###":
						inicio = inicio + 18;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setVigenciaDe(fn.formatDate_MonthCadena(newcontenido.split(separador)[0]));
						break;
					}
				}
			}

			// VigenciaA
			conceptos = Arrays.asList("horas al###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "horas al###":
						inicio = inicio + 11;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setVigenciaA(fn.formatDate_MonthCadena(newcontenido.split(separador)[0].trim()));
						break;
					}
				}
			}
			// Cp
			conceptos = Arrays.asList("C.P:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "C.P:###":
						inicio = inicio + 7;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setCp(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// Recargo
			conceptos = Arrays.asList("por pago fraccionado###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "por pago fraccionado###":
						inicio = inicio + 23;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setRecargo(
									Float.parseFloat(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// Derecho
			conceptos = Arrays.asList("Gastos de expedición###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Gastos de expedición###":
						inicio = inicio + 23;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setDerecho(
									Float.parseFloat(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}
						break;
					}
				}
			}

			// Descripcion
			conceptos = Arrays.asList("Descripción del vehículo*:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Descripción del vehículo*:":
						inicio = inicio + 26;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setDescripcion(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// Clave
			conceptos = Arrays.asList("Clave vehicular:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Clave vehicular:":
						inicio = inicio + 16;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setClave(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// Modelo
			conceptos = Arrays.asList("Modelo:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Modelo:":
						inicio = inicio + 7;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						if (NumberUtils.isParsable(newcontenido.split(separador)[0].trim())) {
							modelo.setModelo(Integer.parseInt(newcontenido.split(separador)[0].trim()));
						}
						break;
					}
				}
			}

			// Serie
			conceptos = Arrays.asList("Serie:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Serie:":
						inicio = inicio + 6;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setSerie(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// Motor
			conceptos = Arrays.asList("Motor:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Motor:":
						inicio = inicio + 6;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setMotor(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// Placas
			conceptos = Arrays.asList("Placas:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Placas:":
						inicio = inicio + 7;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setPlacas(newcontenido.split(saltolinea)[0].trim());
						break;
					}
				}
			}

			// FechaEmision
			conceptos = Arrays.asList("Fecha de emisión:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Fecha de emisión:###":
						inicio = inicio + 20;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						newcontenido = newcontenido.split(separador)[0].trim().replace(" DE ", "-");
						modelo.setFechaEmision(fn.formatDate_MonthCadena(newcontenido));
						break;
					}
				}
			}

			// Marca
			conceptos = Arrays.asList("Marca:");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Marca:":
						inicio = inicio + 6;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setMarca(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// IdCliente
			conceptos = Arrays.asList("Asegurado:###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Asegurado:###":
						inicio = inicio + 13;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						modelo.setIdCliente(newcontenido.split(separador)[0].trim());
						break;
					}
				}
			}

			// AjusteUno
			conceptos = Arrays.asList("Otros descuentos###");
			for (String x : conceptos) {
				inicio = contenido.indexOf(x);
				if (inicio > -1) {
					switch (x) {
					case "Otros descuentos###":
						inicio = inicio + 19;
						newcontenido = contenido.substring(inicio, (inicio + 150));
						if (NumberUtils.isParsable(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim()))) {
							modelo.setAjusteUno(
									Float.parseFloat(fn.preparaPrimas(newcontenido.split(saltolinea)[0].trim())));
						}

						break;
					}
				}
			}

			// coberturas
			List<EstructuraCoberturasModel> coberturas = new ArrayList<EstructuraCoberturasModel>();
			conceptos = Arrays.asList("Suma asegurada###Deducible###Prima");
			conceptosFin = Arrays.asList("@@@Prima Neta###");
			inicio = contenido.indexOf(conceptos.get(0));
			fin = contenido.indexOf(conceptosFin.get(0));
			if (inicio > -1 && fin > -1) {
				inicio = inicio + 34;
				newcontenido = contenido.substring(inicio, fin);
				int i = 0;
				for (String dato : newcontenido.split("\n")) {
					if (dato.split("###").length >= 3) {
						// Clases
						EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
						cobertura.setNombre(dato.split("###")[0].trim());
						cobertura.setDeducible(dato.split("###")[2].trim());
						cobertura.setSa(dato.split("###")[1].trim());
						i++;
						cobertura.setIdx(i);
						coberturas.add(cobertura);
					}
				}

			}
			modelo.setCoberturas(coberturas);

			// recibos
			List<EstructuraRecibosModel> recibosList = new ArrayList<>();

			if (!recibos.equals("")) {
				recibosList = recibosExtract();
			}
			
			switch (modelo.getFormaPago()) {
			case 1:
				if (recibosList.size() == 0) {
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
					recibo.setReciboId("");
					recibo.setSerie("1/1");
					recibo.setVigenciaDe(modelo.getVigenciaDe());
					recibo.setVigenciaA(modelo.getVigenciaA());
					if (recibo.getVigenciaDe().length() > 0) {
						recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
					}
					recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta()));
					recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho()));
					recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo()));
					recibo.setIva(fn.castBigDecimal(modelo.getIva()));
					recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal()));
					recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno()));
					recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos()));
					recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra()));
					recibosList.add(recibo);
				}
				break;
			case 2:
				if (recibosList.size() == 1) {
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
					recibo.setReciboId("");
					recibo.setSerie("2/2");
					recibo.setVigenciaDe(recibosList.get(0).getVigenciaA());
					recibo.setVigenciaA(modelo.getVigenciaA());
					recibo.setVencimiento("");
					recibo.setPrimaneta(restoPrimaNeta);
					recibo.setPrimaTotal(restoPrimaTotal);
					recibo.setRecargo(restoRecargo);
					recibo.setDerecho(restoDerecho);
					recibo.setIva(restoIva);
					recibo.setAjusteUno(restoAjusteUno);
					recibo.setAjusteDos(restoAjusteDos);
					recibo.setCargoExtra(restoCargoExtra);
					recibosList.add(recibo);
				}
				break;
			case 3:
			case 4:
			case 5:
			case 6: // NINGUN PDF TRAE RECIBOS SE QUEDA PENDIENTE ESTE CASO
				if (recibosList.size() >= 1) {
					BigDecimal restoRec = fn.castBigDecimal(fn.getTotalRec(modelo.getFormaPago()) - recibosList.size());
					int totalRec = fn.getTotalRec(modelo.getFormaPago());
					for (int i = recibosList.size(); i <= restoRec.intValue(); i++) {
						EstructuraRecibosModel recibo = new EstructuraRecibosModel();
						recibo.setSerie(i + 1 + "/" + totalRec);
						recibo.setReciboId("");
						recibo.setVigenciaDe(recibosList.get(i - 1).getVigenciaA());
						recibo.setVigenciaA("");
						recibo.setVencimiento("");
						recibo.setPrimaneta(restoPrimaNeta.divide(restoRec));
						recibo.setPrimaTotal(restoPrimaTotal.divide(restoRec));
						recibo.setRecargo(restoRecargo.divide(restoRec));
						recibo.setDerecho(restoDerecho.divide(restoRec));
						recibo.setIva(restoIva.divide(restoRec));
						recibo.setAjusteUno(restoAjusteUno.divide(restoRec));
						recibo.setAjusteDos(restoAjusteDos.divide(restoRec));
						recibo.setCargoExtra(restoCargoExtra.divide(restoRec));
						recibosList.add(recibo);
					}
				}
				break;

			}
			modelo.setRecibos(recibosList);

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					ChubbAutosModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}

	private ArrayList<EstructuraRecibosModel> recibosExtract() {
		List<EstructuraRecibosModel> recibosLis = new ArrayList<>();
		try {
			//System.out.println(recibos);
			recibos = fn.remplazarMultiple(recibos, fn.remplazosGenerales());
			int index = 0;
			int totalRec = fn.getTotalRec(modelo.getFormaPago());
			int recibosSerie = 1;

			ArrayList<String> series = new ArrayList<String>();
			for (String a : recibos.split("AVISO DE COBRO")) {

				if (index > 0 && a.contains("De recibo:")) {
					EstructuraRecibosModel recibo = new EstructuraRecibosModel();
					//
					// recibo_id
					inicio = a.indexOf("No. De recibo:");
					String actualSerie = "";
					fin = a.indexOf("Endoso:");
					if (inicio > -1 && fin > inicio) {
						newcontenido = fn.gatos(a.substring(inicio + 14, fin)).trim();
						actualSerie = recibo.getReciboId();
					}

					if (index == 1 || !series.contains(actualSerie)) {

						recibo.setSerie(actualSerie);
						recibo.setSerie(recibosSerie + "/" + totalRec);

						inicio = a.indexOf("No. De recibo:");
						fin = a.indexOf("Endoso:");
						if (inicio > -1 && fin > inicio) {
							newcontenido = fn.gatos(a.substring(inicio + 14, fin)).trim();

						}

						if (a.contains("Vigencia") && a.contains("Inciso")) {
							recibo.setVigenciaDe(fn.formatDate(
									a.split("Del")[1].split("horas")[0].replace("12:00", "").replace("###", "").trim(),
									"yyy-mm-dd"));
							recibo.setVigenciaA(fn.formatDate(
									a.split("al")[1].split("horas")[0].replace("12:00", "").replace("###", "").trim(),
									"yyyy-mm-dd"));
						}

						inicio = a.indexOf("Total a pagar:");
						if (inicio > -1) {
							if (a.split("Total a pagar:")[1].contains("I.V.A.")) {
								if (fn.cleanString(a.split("Total a pagar:")[1].split("I.V.A.")[0].replace("###", "")
										.replace("$", "").trim()).contains("ABA")) {

									recibo.setPrimaTotal(fn.castBigDecimal(fn.cleanString(fn
											.cleanString(a.split("Total a pagar:")[1].split("\n")[1].split("###")[3])),
											2));
									restoPrimaTotal = fn.castBigDecimal(modelo.getPrimaTotal())
											.subtract(recibo.getPrimaTotal());
								} else {

									recibo.setPrimaTotal(fn.castBigDecimal(
											fn.cleanString(a.split("Total a pagar:")[1].split("I.V.A.")[0]
													.replace("###", "").replace("$", "").trim()),
											2));
									restoPrimaTotal = fn.castBigDecimal(modelo.getPrimaTotal())
											.subtract(recibo.getPrimaTotal());
								}
							} else {
								recibo.setPrimaTotal(fn.castBigDecimal(
										fn.cleanString(
												a.split("Total a pagar:")[1].split("###")[2].split("\r\n")[0].trim()),
										2));
								restoPrimaTotal = fn.castBigDecimal(modelo.getPrimaTotal())
										.subtract(recibo.getPrimaTotal());
							}
						}

						if (a.contains("Gastos de expedición")) {
							if (a.split("Gastos de expedición")[1].contains("pago fraccioTnoadtoal")) {
								recibo.setDerecho(fn.castBigDecimal(fn.cleanString(
										a.split("Gastos de expedición")[1].split("pago fraccioTnoadtoal")[0]
												.replace("###", "").replace("$", "").trim()),
										2));
								restoDerecho = fn.castBigDecimal(modelo.getDerecho()).subtract(recibo.getDerecho());
							} else {
								recibo.setDerecho(fn.castBigDecimal(fn.cleanString(
										a.split("Gastos de expedición")[1].split("###")[2].split("\r\n")[0].trim()),
										2));
								restoDerecho = fn.castBigDecimal(modelo.getDerecho()).subtract(recibo.getDerecho());
							}
						}

						if (a.contains("I.V.A")) {
							recibo.setIva(fn.castBigDecimal(
									fn.cleanString(a.split("I.V.A.")[1].split("###")[2].split("\r\n")[0].trim())));
							restoIva = fn.castBigDecimal(modelo.getIva()).subtract(recibo.getIva());
						}
						System.out.println("aqui");
						if (a.contains("Financiamiento")) {
							recibo.setRecargo(fn.castBigDecimal(fn.cleanString(
									a.split("Financiamiento")[1].split("###")[2].split("\r\n")[0].trim())));
							restoRecargo = fn.castBigDecimal(modelo.getRecargo()).subtract(recibo.getRecargo());
						}
						System.out.println("aqui");
						if (a.contains("Prima Neta")) {
							recibo.setPrimaneta(fn.castBigDecimal(
									fn.cleanString(a.split("Prima Neta")[1].split("###")[2].split("\r\n")[0].trim()),
									2));
							restoPrimaNeta = fn.castBigDecimal(modelo.getPrimaneta()).subtract(recibo.getPrimaneta());
						}
						System.out.println("aqui");
						recibosLis.add(recibo);
						series.add(actualSerie);
					}
				}
				index++;
			}
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		} catch (Exception ex) {
			System.out.println("DatosChubb.getRecibos " + ex.getMessage() + " | " + ex.getCause());
			return (ArrayList<EstructuraRecibosModel>) recibosLis;
		}
	}
}
