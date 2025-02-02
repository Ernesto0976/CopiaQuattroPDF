package com.copsis.services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.copsis.clients.QuattroExternalApiClient;
import com.copsis.clients.QuattroUtileriasApiClient;
import com.copsis.clients.projections.QuattroExternalApiEstructuraFiscalesProjection;
import com.copsis.clients.projections.QuattroUtileriasApiQrProjection;
import com.copsis.controllers.forms.DatosSatForm;
import com.copsis.controllers.forms.PdfForm;
import com.copsis.controllers.forms.PdfNegocioForm;
import com.copsis.models.CardSettings;
import com.copsis.models.EstructuraConstanciaSatModel;
import com.copsis.models.RegimenFiscalPropsDto;
import com.copsis.models.constancia.ConstanciaModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class IndentificaConstanciaService {
	private final ConstanciaModel constanciaModel;
	private final WebhookService webhookService;

	private final QuattroUtileriasApiClient quattroUtileriasApiClient;

	private final QuattroExternalApiClient quattroExternalApiClient;

	private final RegimenFiscalService regimenFiscalService;

	public EstructuraConstanciaSatModel indentificaConstancia(PdfForm pdfForm) throws IOException {

		EstructuraConstanciaSatModel constancia = new EstructuraConstanciaSatModel();
		PDDocument documentToBeParsed = null;
		COSDocument cosDoc = null;
		PDDocument pdDoc = null;

		try {

			final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
			documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
			PDFTextStripper pdfStripper = new PDFTextStripper();
			cosDoc = documentToBeParsed.getDocument();
			pdDoc = new PDDocument(cosDoc);
			pdfStripper.setStartPage(1);
			pdfStripper.setEndPage(1);
			String contenido = pdfStripper.getText(pdDoc);
			if (contenido.contains("CONSTANCIA DE SITUACIÓN FISCAL") || contenido.contains("CÉDULA DE IDENTIFICACIÓN FISCAL ")) {
				constancia = constanciaModel.procesar(pdfStripper, pdDoc, contenido, pdfForm);
				return constancia;
			}

			String errorMessage = "Documento de tipo no reconocido.";

			constancia.setError(errorMessage);
			return constancia;

		} catch (IOException e) {
			constancia.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + e.getMessage() + " | " + e.getCause());
			return constancia;
		} catch (Exception ex) {
			constancia.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return constancia;
		} finally {
			if(documentToBeParsed != null) documentToBeParsed.close();
			if(cosDoc != null) cosDoc.close();
			if(pdDoc != null) pdDoc.close();

		}

	}

	private void sendWebhookMessage(PdfForm pdfForm, String errorMessage) {
		try {
			CardSettings cardSettings = CardSettings.builder()
					.fileUrl(pdfForm.getUrl())
					.sourceClass(IndentificaConstanciaService.class.getName())
					.exceptionMessage(errorMessage)
					.build();
			webhookService.send(cardSettings);
		} catch (Exception e) {
			// do nothing
		}
	}

	public EstructuraConstanciaSatModel negocioValidaDatosFiscales(PdfNegocioForm pdfNegocioForm, HttpHeaders httpHeaders) throws Exception {
		
		EstructuraConstanciaSatModel estructuraConstanciaSatModel = new EstructuraConstanciaSatModel();
		try {
			PdfForm pdfForm = new PdfForm();
			
			switch (pdfNegocioForm.getTipoValidacion()) {
			case 1: // Valida datos CFDI y retorna si es posible, si hay error Lee Qr de imagen, Valida datos CFDI y retorna
				pdfForm.setUrl(pdfNegocioForm.getUrl());

				// intentamos por leer pagina del SAT	
				estructuraConstanciaSatModel = procesoObtenerJsonByImagenQR(pdfNegocioForm, false, httpHeaders);
				estructuraConstanciaSatModel.setRegimenFiscal(regimenesAxa(estructuraConstanciaSatModel.getRegimenFiscal()));

				// Valida estructura
				estructuraConstanciaSatModel = validaciones(estructuraConstanciaSatModel,pdfForm, false);
				//Como ultima opcion leemos PDF
				if (estructuraConstanciaSatModel.getError() != null) {
					// vamos a leer PDF
					estructuraConstanciaSatModel = indentificaConstancia(pdfForm);
					estructuraConstanciaSatModel.setRegimenFiscal(regimenesAxa(estructuraConstanciaSatModel.getRegimenFiscal()));

					// Valida estructura
					estructuraConstanciaSatModel = validaciones(estructuraConstanciaSatModel,pdfForm, true); 
					
				}
				break;
			case 2://Lee Qr de imagen, Valida datos CFDI y retorna
				pdfForm.setUrl(pdfNegocioForm.getUrl());
				
				estructuraConstanciaSatModel = procesoObtenerJsonByImagenQR(pdfNegocioForm, true,httpHeaders);
				estructuraConstanciaSatModel.setRegimenFiscal(regimenesAxa(estructuraConstanciaSatModel.getRegimenFiscal()));

				// Valida estructura
				estructuraConstanciaSatModel = validaciones(estructuraConstanciaSatModel,pdfForm, true);
				break;
			default:
				break;
			}
			
			return estructuraConstanciaSatModel;
			
		} catch (Exception ex) {
			PdfForm pdfForm = new PdfForm();
			pdfForm.setUrl(pdfNegocioForm.getUrl());
			sendWebhookMessage(pdfForm, ex.getMessage());
			throw ex;
		}
	}

	private EstructuraConstanciaSatModel validaciones(EstructuraConstanciaSatModel estructuraConstanciaSatModel, PdfForm pdfForm, boolean webhookMessage) {
		try {
			if(estructuraConstanciaSatModel.getRfc().equals("")) {
				String publicErrorMessage = "No se encontraron datos en el rango de búsqueda";
				estructuraConstanciaSatModel.setError(publicErrorMessage);
				if(webhookMessage) {
					String privateErrorMessage = String.format(publicErrorMessage.concat(": '%s' y '%s'"), "Identificación del Contribuyente", "Datos del domicilio registrado");
					sendWebhookMessage(pdfForm, privateErrorMessage);	
				}
				return estructuraConstanciaSatModel;
			}
			
			if(estructuraConstanciaSatModel.getTipoPersona().equals("Física")) {
				// validaciones persona física
				if(estructuraConstanciaSatModel.getNombre().equals("") || estructuraConstanciaSatModel.getApellidoP().equals("") || estructuraConstanciaSatModel.getCp().equals("")) {
					String publicErrorMessage = "No fué posible leer alguno de los datos: nombre(s), apellído paterno, Código postal";
					if(webhookMessage) {
						sendWebhookMessage(pdfForm, publicErrorMessage);	
					}
					estructuraConstanciaSatModel.setError(publicErrorMessage);
					return estructuraConstanciaSatModel;
				}
			} else {
				// validaciones persona moral
				if(estructuraConstanciaSatModel.getRazonSocial().equals("") || estructuraConstanciaSatModel.getRegimenDeCapital().equals("") || estructuraConstanciaSatModel.getCp().equals("")) {
					String publicErrorMessage = "No fué posible leer alguno de los datos: Razón Social, Régimen Capital, Código postal";
					if(webhookMessage) {
						sendWebhookMessage(pdfForm, publicErrorMessage);
					}
					estructuraConstanciaSatModel.setError(publicErrorMessage);
					return estructuraConstanciaSatModel;
				}
			}
			
			if(estructuraConstanciaSatModel.getRegimenFiscal().isEmpty()) {
				String errorRegimenes = "No se logro extraer informacion de los regímenes";
				estructuraConstanciaSatModel.setError(errorRegimenes);
				 if(webhookMessage) {
					sendWebhookMessage(pdfForm, errorRegimenes); 
				 }	
			}
			
			return estructuraConstanciaSatModel;
		} catch (Exception ex) {
			if(webhookMessage) {
				sendWebhookMessage(pdfForm, ex.getMessage());	
			}
			estructuraConstanciaSatModel.setError(IndentificaConstanciaService.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
			return estructuraConstanciaSatModel;
		}
	}
	
	private List<RegimenFiscalPropsDto> regimenesAxa(List<RegimenFiscalPropsDto> listado){
		List<RegimenFiscalPropsDto> regimenes = new ArrayList<>() ;
		try {
			listado
			.stream()
			.forEach(itemRegimen ->{
				RegimenFiscalPropsDto regimen = regimenFiscalService.get(itemRegimen.getDescripcion());
				if(regimen.getDescripcion() != null && !regimen.getDescripcion().equals("")) { 
					RegimenFiscalPropsDto regimenDto = new RegimenFiscalPropsDto();
					regimenDto.setClave(regimen.getClave());
					regimenDto.setDescripcion(regimen.getDescripcion());
					regimenes.add(regimenDto);	
				}
			});
			return regimenes;
		} catch (Exception ex) {
			return regimenes;
		}
	}
	
	private EstructuraConstanciaSatModel procesoObtenerJsonByImagenQR(PdfNegocioForm pdfNegocioForm, boolean imagen, HttpHeaders httpHeaders){
		try {
			//Llenamos Form
			DatosSatForm datosSatForm = new DatosSatForm();
			datosSatForm.setUrl(pdfNegocioForm.getUrl());
			QuattroUtileriasApiQrProjection quattroUtileriasApiQrProjection;
			EstructuraConstanciaSatModel eCsf = new EstructuraConstanciaSatModel();
			// extrae url de QR que esta en la constancia
			try {
				if(imagen) {
					quattroUtileriasApiQrProjection = quattroUtileriasApiClient.getExtraeUrlImagenQr(datosSatForm, httpHeaders);	
				}else {
					quattroUtileriasApiQrProjection = quattroUtileriasApiClient.getExtraeUrl(datosSatForm, httpHeaders);
				}
			} catch (Exception ex) {
				if(!imagen){
					eCsf.setError("Error en Lectura QR");
					return eCsf;
				}else{
					throw ex;
				}
			}
			//Llenamos Form con la nueva info[URL SAT]
			datosSatForm.setUrl(quattroUtileriasApiQrProjection.getResult());
			QuattroExternalApiEstructuraFiscalesProjection quattroExternalApiEstructuraFiscalesProjection;
			
			try {
				// Va a formar estructura con datos de pagina
				quattroExternalApiEstructuraFiscalesProjection = quattroExternalApiClient.extraeDatosPaginaSat(datosSatForm,httpHeaders);
			} catch (Exception ex) {
				throw ex;
			}
			
			//compara con el catalogo AXA de regimenes
			return quattroExternalApiEstructuraFiscalesProjection.getResult();
		}catch (Exception ex) {
			throw ex;
		}
	}
}
