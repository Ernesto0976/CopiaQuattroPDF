package com.copsis.models.ana;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.copsis.constants.ConstantsValue;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.utils.ErrorCode;

public class AnaAutosModelRoja {

	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	private String contenido = "";
	private static final String FORMATDATE="yyyy-MM-dd";


	public AnaAutosModelRoja(String contenido) {
		this.contenido = contenido;
	}

	public EstructuraJsonModel procesar() {
		String newcontenido = "";
		StringBuilder direccion = new StringBuilder();
		String vigencias = "";
		int inicio = 0;
		int fin = 0;
		boolean cp = true;

		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
		contenido = contenido.replace("Fecha de Exp.", "Fecha de Expedición")
				.replace("Mens. prorrat", "Mensual")
				.replace("Pagos Subsec", "Pagos Subsecuentes").replace("For. de Pago", ConstantsValue.FORMA_PAGO)
                .replace("Forma de Pago:", ConstantsValue.FORMA_PAGO)
				.replace("Forma###de###pago:", ConstantsValue.FORMA_PAGO)
				.replace("Subsec:", ConstantsValue.SUBSECUENTE).replace("Prima Net a :",ConstantsValue.PRIMA_NETA3)
				.replace("Prima Tota l:", ConstantsValue.PRIMA_TOTAL )
				.replace("###8T0e0l", "Tel 800")
				.replace("PÓLIZA###DE###SEGURO###AUTOMÓVILES", "PÓLIZA DE SEGURO AUTOMÓVILES")
				.replace("Prima###Total:", ConstantsValue.PRIMA_TOTAL)
				.replace("A.N.A.###Compañía###de###Seguros", "A.N.A. Compañía de Seguros")
				.replace("@@@Coberturas###Amparadas", ConstantsValue.COBERTURAS_AMPARADAS2);

		try {
			modelo.setTipo(1);
			// cia
			modelo.setCia(7);

			inicio = contenido.indexOf("PÓLIZA DE SEGURO AUTOMÓVILES");
			fin = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);
			

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
                 
					if (newcontenido.split("\n")[i].contains("Póliza") && newcontenido.split("\n")[i].contains("Inciso")
							&& newcontenido.split("\n")[i].contains("Endoso")) {
						modelo.setPoliza(newcontenido.split("\n")[i].split(ConstantsValue.POLIZA_ACENT2)[1].split("Inciso")[0]
								.replace("###", "").trim());
						modelo.setEndoso(newcontenido.split("\n")[i].split("Endoso:")[1].replace("###", "").trim());
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC) && newcontenido.split("\n")[i].contains(ConstantsValue.NO_CLIENTE)
							&& newcontenido.split("\n")[i].contains("Pague")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("No.Cliente")[0]
								.replace("###", "").trim());
						modelo.setIdCliente(newcontenido.split("\n")[i].split(ConstantsValue.NO_CLIENTE)[1].split("Pague")[0]
								.replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains(ConstantsValue.RFC) && newcontenido.split("\n")[i].contains(ConstantsValue.NO_CLIENTE)
							&& newcontenido.split("\n")[i].contains("Clave Agente")) {
						modelo.setRfc(newcontenido.split("\n")[i].split(ConstantsValue.RFC)[1].split("No.Cliente")[0]
								.replace("###", "").trim());
						modelo.setIdCliente(newcontenido.split("\n")[i].split(ConstantsValue.NO_CLIENTE)[1].split("Clave Agente")[0]
								.replace("###", "").trim());
					}

					if (newcontenido.split("\n")[i].contains("Nombre")
							&& newcontenido.split("\n")[i].contains("Contratante")
							&& newcontenido.split("\n")[i].contains("Dirección")) {
						modelo.setCteNombre(newcontenido.split("\n")[i + 1].replace("###", " ").trim());

						if (newcontenido.split("\n")[i + 3].contains("Duración:")) {
							direccion.append(newcontenido.split("\n")[i + 4]);

							if (newcontenido.split("\n")[i + 5].contains("Fecha de Expedición")) {
								direccion.append(newcontenido.split("\n")[i + 4].split("Fecha")[0]);
							}
							modelo.setCteDireccion(direccion.toString().replace("###", " ").replace("Fecha de Expedición Desde Hasta", "").trim());
						}
					} else {
						if (newcontenido.split("\n")[i].contains("Nombre")
								&& newcontenido.split("\n")[i].contains("Dirección")) {
							modelo.setCteNombre(newcontenido.split("\n")[i + 1].replace("###", "").trim());

							if (newcontenido.split("\n")[i + 2].contains("Duración:")) {
								direccion.append(newcontenido.split("\n")[i + 3].split("Fecha")[0]);

								if (newcontenido.split("\n")[i + 4].contains("C.P.")) {
									direccion.append(" " + newcontenido.split("\n")[i + 4].split("###")[0] + "  "
											+ newcontenido.split("\n")[i + 4].split("###")[1]);
								}
								modelo.setCteDireccion(direccion.toString().replace("###", ""));
							}
						}

					}
					
					if ( cp && newcontenido.split("\n")[i].contains("C.P.")  ) {
						
						List<String> valores = fn.obtenerListNumeros2(newcontenido.split("\n")[i]);
					

						if(valores.size()==4){
							modelo.setCp(valores.get(1));
						}
						if(valores.size()==1 && valores.get(0).length()> 3){
							modelo.setCp(valores.get(0));
						}
						if(valores.size()==2 && valores.get(1).length()> 3){
							modelo.setCp(valores.get(1));
						}
						if(valores.size()==3 && valores.get(2).length()> 3){
							modelo.setCp(valores.get(2));
						}
						if(modelo.getCteDireccion().isEmpty() && !modelo.getCp().isEmpty() ){
                          modelo.setCteDireccion(newcontenido.split("\n")[i].split("C.P.")[0].replace("###", " ").trim());
						}
						
						cp =false;
					}
					if ( cp && newcontenido.split("\n")[i].contains("C.P.") && fn.isNumeric(newcontenido.split("\n")[i].replace("C.P.", "C.P.").split("C.P.")[1].substring(0, 5).trim()) ) {												
							modelo.setCp(newcontenido.split("\n")[i].replace("C.P.", "C.P.").split("C.P.")[1].substring(0, 5));		
							cp =false;
					}
					
					if (newcontenido.split("\n")[i].contains("Expedición")
							&& newcontenido.split("\n")[i].contains("Desde")
							&& newcontenido.split("\n")[i].contains("Hasta")) {
							
							
						if(newcontenido.split("\n")[i + 1].length() > 50) {
							vigencias = fn.gatos(newcontenido.split("\n")[i +1].replace("###", "").replace("D", "###").replace("M", "###").replace("A", "###"));	

							if(vigencias.split("###").length > 7 && (vigencias.split("###")[6].trim().length() < 4 && vigencias.split("###")[6].trim().length() == 3)){
								vigencias = fn.gatos(newcontenido.split("\n")[i +2].replace("###", "").replace("D", "###").replace("M", "###").replace("A", "###"));	
							}
							if(vigencias.contains("C.P.")){
								vigencias = fn.gatos(newcontenido.split("\n")[i + 2].replace("###", "").replace("D", "###").replace("M", "###").replace("A", "###"));	
							}							
						}else {
							vigencias = fn.gatos(newcontenido.split("\n")[i + 2].replace("###", "").replace("D", "###").replace("M", "###").replace("A", "###"));	
						}

						int sp = vigencias.split("###").length;
						int to  = sp - 9;

						if (sp == 12) {
							vigencias = fn.gatos(vigencias.split(vigencias.split("###")[2])[1]);
							sp = vigencias.split("###").length;
						} 
						if (sp > 9) {
							vigencias = fn.gatos(vigencias.split(vigencias.split("###")[to - 1])[1].replace(" ", "").trim());
							sp = vigencias.split("###").length;
						}
						
						if (sp == 9) {		
									
							modelo.setVigenciaA((vigencias.split("###")[8] + "-" + vigencias.split("###")[7] + "-"
									+ vigencias.split("###")[6]).replace(" ", "").trim());
							modelo.setVigenciaDe((vigencias.split("###")[5] + "-" + vigencias.split("###")[4] + "-"
									+ vigencias.split("###")[3]).replace(" ", "").trim());
							modelo.setFechaEmision((vigencias.split("###")[2] + "-" + vigencias.split("###")[1] + "-"+ vigencias.split("###")[0]).replace(" ", "").trim());
						}
						
					}

					if (newcontenido.split("\n")[i].contains("Cobertura:")
							&& newcontenido.split("\n")[i].contains("Pagos Subsecuentes")
							&& newcontenido.split("\n")[i].contains("Recargos")) {
						modelo.setPlan(newcontenido.split("\n")[i].split("Cobertura:")[1].split("Pagos")[0]
								.replace("###", "").trim());
						modelo.setRecargo(fn.castBigDecimal(fn
								.preparaPrimas(newcontenido.split("\n")[i].split("Recargos:")[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains("Moneda:")
							&& newcontenido.split("\n")[i].contains("Inicial:")
							&& newcontenido.split("\n")[i].contains("Gastos:")) {
						    modelo.setDerecho(fn.castBigDecimal(
							fn.preparaPrimas(newcontenido.split("\n")[i].split("Gastos:")[1].replace("###", ""))));
							List<String> valores = fn.obtenerListNumeros(newcontenido.split("\n")[i]);								
						    modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));

					}
								
					if (newcontenido.split("\n")[i].contains("pago:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.SUBSECUENTE)
							&& newcontenido.split("\n")[i].contains("I.V.A:")) {								
								
						modelo.setFormaPago(fn
								.formaPago(newcontenido.split("\n")[i].split(ConstantsValue.FORMA_PAGO)[1].split(ConstantsValue.SUBSECUENTE)[0]
										.replace("###", "").replace(".", "").trim()));
						if(modelo.getFormaPago()==0){
                            modelo.setFormaPago( fn.formaPagoSring(newcontenido.split("\n")[i]));
						}				
						modelo.setIva(fn.castBigDecimal(
								fn.preparaPrimas(newcontenido.split("\n")[i].split("I.V.A:")[1].replace("###", ""))));
								List<String> valores = fn.obtenerListNumeros(newcontenido.split("\n")[i]);
								modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
							
					}

					
					if (newcontenido.split("\n")[i].contains("Prima Neta:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PRIMA_TOTAL)) {
							
						modelo.setPrimaneta(fn.castBigDecimal(fn
								.preparaPrimas(newcontenido.split("\n")[i].split("Prima Neta:")[1].split(ConstantsValue.PRIMA_TOTAL)[0]
										.replace("###", ""))));
					
						
						modelo.setPrimaTotal(fn.castBigDecimal(
								fn.preparaPrimas(newcontenido.split("\n")[i].split(ConstantsValue.PRIMA_TOTAL)[1].replace("###", ""))));
					}
					if (newcontenido.split("\n")[i].contains(ConstantsValue.DESCRIPCIONPT)) {
						modelo.setDescripcion(
								newcontenido.split("\n")[i].split(ConstantsValue.DESCRIPCIONPT)[1].replace("###", " ").trim());
					}
					if (newcontenido.split("\n")[i].contains("No.Motor:")
							&& newcontenido.split("\n")[i].contains("Capacidad:")
							&& newcontenido.split("\n")[i].contains("Modelo:")) {
						modelo.setMotor(newcontenido.split("\n")[i].split("No.Motor:")[1].split("Capacidad")[0]
								.replace("###", ""));
						modelo.setModelo(Integer
								.parseInt(newcontenido.split("\n")[i].split("Modelo:")[1].replace("###", "").trim()));
					}

					if (newcontenido.split("\n")[i].contains("Serie:")
							&& newcontenido.split("\n")[i].contains(ConstantsValue.PLACAS)) {
						modelo.setSerie(
								newcontenido.split("\n")[i].split("Serie:")[1].split(ConstantsValue.PLACAS)[0].replace("###", ""));

						if (newcontenido.split("\n")[i].split("Placa")[1].length() > 7) {
							modelo.setPlacas(newcontenido.split("\n")[i].split(ConstantsValue.PLACAS)[1].replace("###", "").trim());
						}
					}
				}
			}

			modelo.setMoneda(1);

			if(modelo.getFormaPago() == 0 && modelo.getVigenciaDe().length() == 10 && modelo.getVigenciaA().length() == 10 && contenido.contains(ConstantsValue.FORMA_PAGO2) ) {
				long diasVigencia = calculaDiasVigencia(modelo.getVigenciaDe(), modelo.getVigenciaA());
				String textoFormaPago = fn.gatos(contenido.split(ConstantsValue.FORMA_PAGO2)[1].replace(":", "").trim());
				textoFormaPago = textoFormaPago.split("###")[0].trim();
			
				if(diasVigencia>27 && diasVigencia<32 && textoFormaPago.equalsIgnoreCase("Domiciliada")) {					
					modelo.setFormaPago(fn.formaPago("CONTADO"));
				}
				if(textoFormaPago.equalsIgnoreCase("Dxn debito")) {
					modelo.setFormaPago(1);
				}
			}
			
			inicio = contenido.indexOf("Canal de Venta, Agente:");
			fin = contenido.indexOf("Tel");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");

				for (int i = 0; i < newcontenido.split("\n").length; i++) {

					if (newcontenido.split("\n")[i].contains(ConstantsValue.AGENTE)) {
						if (newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1].length() > 10) {
							modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1]
									.replace("###", "").substring(0, 5));
							modelo.setAgente(newcontenido.split("\n")[i].split(modelo.getCveAgente())[1]
									.replace("###", "").trim());
						} else {
							modelo.setCveAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[1]);
							modelo.setAgente(newcontenido.split("\n")[i].split(ConstantsValue.AGENTE)[1].split("###")[2]);
						}
					}
				}
			}

			inicio = contenido.indexOf(ConstantsValue.COBERTURAS_AMPARADAS2);
			fin = contenido.indexOf("A.N.A. Compañía de Seguros");

			if (inicio > -1 && fin > -1 && inicio < fin) {
				List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
				newcontenido = contenido.substring(inicio, fin).replace("@@@", "").replace("\r", "");
				newcontenido = newcontenido.replace("Responsabilidad Civil Catastrófica por Muerte a Terceras Daños\n a Terceros en sus Personas", "Responsabilidad Civil Catastrófica por Muerte a Terceras Daños a Terceros en sus Personas")
						.replace("Responsabilidad Civil Motociclista, Ciclista y-o Conductor\n Carrito de Golf", "Responsabilidad Civil Motociclista, Ciclista y-o Conductor Carrito de Golf")
						.replace("1###Daños###Materiales", "Daños Materiales")
						.replace("2###Robo###Total", "Robo Total")
						.replace("3###Responsabilidad###Civil###Daños###a###Terceros", "Responsabilidad Civil Daños a Terceros")
						.replace("4###Gastos###Médicos###al###Conductor", "Gastos Médicos al Conductor")
						.replace("5###Defensa###Juridica###y###Asistencia###Legal", "Defensa Juridica y Asistencia Legal")
						.replace("14###ANA###Asistencia", "ANA Asistencia")
						.replace("16###Responsabilidad###Civil###Viajero", "Responsabilidad Civil Viajero")
						.replace("27###Desbielamiento###por###Penetración###de###Agua###al###Motor", "Desbielamiento por Penetración de Agua al Motor")
						.replace("31###Responsabilidad###Civil###Catastrófica###por###Muerte###a###Terceras###Daños ", "Responsabilidad Civil Catastrófica por Muerte a Terceras Daños")
						.replace("a###Terceros en sus###Personas###", "a Terceros en sus Personas")
						.replace("Extensión de###Responsabilidad###Civil", "Extensión de Responsabilidad Civil")
						.replace("Responsabilidad###Civil###del###Hijo###Meno", "Responsabilidad Civil del Hijo Menor")
						.replace("Responsabilidad###Civil###por###Remolque", "Responsabilidad Civil por Remolque")
						.replace("Responsabilidad###Civil###Motociclista,###Ciclista###y-o###Conductor", 
						"Responsabilidad Civil Motociclista,Ciclista y-o Conductor");
				for (int i = 0; i < newcontenido.split("\n").length; i++) {
					
					EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();

					if (!newcontenido.split("\n")[i].contains("Unidad de Medida") &&
						!newcontenido.split("\n")[i].contains("www.anaseguros") &&
						!newcontenido.split("\n")[i].contains("Valor Comercial") && 
						!newcontenido.split("\n")[i].contains("Las sumas aseguradas de RC") && 
						!newcontenido.split("\n")[i].contains("Aplican condiciones generales") && 
						!newcontenido.split("\n")[i].contains("Coberturas Amparadas")) {

						int sp = newcontenido.split("\n")[i].split("###").length;
						
						switch (sp) {
						case 2:case 3:
							cobertura.setNombre(eliminaNumeroDeNombreCobertura(newcontenido.split("\n")[i].split("###")[0]).trim());
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[1].trim());
							coberturas.add(cobertura);
							break;
						case 4:
							cobertura.setNombre(eliminaNumeroDeNombreCobertura(newcontenido.split("\n")[i].split("###")[0]).trim());
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[1].trim());
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[2].trim());
							coberturas.add(cobertura);
							break;
						case 5:
							cobertura.setNombre(eliminaNumeroDeNombreCobertura(newcontenido.split("\n")[i].split("###")[1]).trim());
							cobertura.setDeducible(newcontenido.split("\n")[i].split("###")[2].trim());
							cobertura.setSa(newcontenido.split("\n")[i].split("###")[3].trim());
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
		} catch (Exception ex) {
			modelo.setError(
					AnaAutosModelRoja.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}
	}
	
	private long calculaDiasVigencia(String vigenciaDe, String vigenciaA) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE);
        Date dateVigenciaDe;
        Date dateVigenciaA;
        long diferencia = 0;
		try {
			dateVigenciaDe = sdf.parse(vigenciaDe);
			dateVigenciaA = sdf.parse(vigenciaA);

			long diferenciaMilli = Math.abs(dateVigenciaA.getTime() - dateVigenciaDe.getTime());
		     diferencia = TimeUnit.DAYS.convert(diferenciaMilli, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, e.getMessage());
		}
		return diferencia;
	}
	
	private String eliminaNumeroDeNombreCobertura(String nombre) {
		String resultado = nombre.trim();
		if(fn.isNumeric(resultado.split(" ")[0])){
			resultado = resultado.replace(resultado.split(" ")[0], "");
		}
		return resultado;
	}
}
