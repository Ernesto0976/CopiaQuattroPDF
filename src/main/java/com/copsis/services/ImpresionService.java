package com.copsis.services;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.copsis.clients.QuattroUploadClient;
import com.copsis.clients.projections.CaractulaProjection;
import com.copsis.clients.projections.CaractulaPrudentialProjection;
import com.copsis.clients.projections.CertificadoProjection;
import com.copsis.clients.projections.CotizacionProjection;
import com.copsis.clients.projections.ImpresionReclamacionProjection;
import com.copsis.clients.projections.PolizaAutosProjection;
import com.copsis.controllers.forms.AdjuntoForm;
import com.copsis.controllers.forms.AmortizacionPdfForm;
import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.controllers.forms.ImpresionAxaVidaForm;
import com.copsis.controllers.forms.ImpresionBienvenidadForm;
import com.copsis.controllers.forms.ImpresionCaratulaForm;
import com.copsis.controllers.forms.ImpresionCertificadoAxaForm;
import com.copsis.controllers.forms.ImpresionFiscalForm;
import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.controllers.forms.MovimientosForm;
import com.copsis.dto.SURAImpresionEmsionDTO;
import com.copsis.encryptor.SiO4EncryptorAES;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.ImpresionVitro.ImpresionVitrCBienvenida;
import com.copsis.models.impresion.ImpresionAmortizacionesPdf;
import com.copsis.models.impresion.ImpresionCaractulaPrudential;
import com.copsis.models.impresion.ImpresionCertificadoAfirme;
import com.copsis.models.impresion.ImpresionCertificadoArgos;
import com.copsis.models.impresion.ImpresionCertificadoAxaPdf;
import com.copsis.models.impresion.ImpresionCertificadoChubbPdf;
import com.copsis.models.impresion.ImpresionCertificadoHogarPdf;
import com.copsis.models.impresion.ImpresionConsultaMovimientos;
import com.copsis.models.impresion.ImpresionFiscalPdf;
import com.copsis.models.impresion.ImpresionPolizaAutosInter;
import com.copsis.models.impresion.ImpresionPrudPdf;
import com.copsis.models.impresion.ImpresionReclamacionPdf;
import com.copsis.models.impresion.ImpresionVidaAxaPdf;
import com.copsis.models.impresionAxa.ImpresionCartaAntiguedad;
import com.copsis.models.impresionAxa.ImpresionCertificadoPdf;
import com.copsis.models.impresionAxa.ImpresionConstanciaAntiguedad;
import com.copsis.models.impresionAxa.ImpresionCotizacionVida;
import com.copsis.models.impresionAxa.ImpresionCredencialPdf;
import com.copsis.models.impresionAxa.ImpresionEndosoPdf;
import com.copsis.models.impresionEndoso.ImpresionEndoso;
import com.copsis.models.impresioncaratula.ImpresionCaratulaColectividasAutos;
import com.copsis.models.impresioncaratula.ImpresionCaratulaVidaGrupo;
import com.copsis.models.impresioncaratula.ImpresionCaratulaAutos;
import com.copsis.models.impresioncaratula.ImpresionCaratulaDiversos;
import com.copsis.models.impresioncaratula.ImpresionCaratulaSalud;
import com.copsis.models.impresioncaratula.ImpresionCaratulaSaludGrupo;
import com.copsis.models.impresioncaratula.ImpresionCaratulaVida;
import com.copsis.utils.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImpresionService {

    private final QuattroUploadClient quattroUploadClient;

    public ImpresionForm impresionServicePdf(ImpresionForm impresionForm, HttpHeaders headers) {
        ImpresionTipoService impresioneTipoService = new ImpresionTipoService(impresionForm);
        AdjuntoForm adjuntoForm = new AdjuntoForm();

        if (impresionForm.getTipoImpresion() == 100 && impresionForm.getSiniestroDocumentoID() > 0) {
            adjuntoForm.setEntidadID(impresionForm.getSiniestroDocumentoID());
            adjuntoForm.setEntidadTipo(21);
        }
        byte[] byteArrayPDF = impresioneTipoService.getByteArrayPDF();

        if (impresionForm.getTiporespuesta() == 1) {
            Date date = new Date();

            String nombrePdf = "";
            if (impresionForm.getNombreOriginal() != null && !impresionForm.getNombreOriginal().equals("")) {
                nombrePdf = impresionForm.getNombreOriginal();
            } else {
                nombrePdf = SiO4EncryptorAES.encrypt("Consolidado_" + date,
                        com.copsis.encryptor.utils.Constants.ENCRYPTION_KEY);
            }
            adjuntoForm.setB64(Base64.encodeBase64String(byteArrayPDF));
            adjuntoForm.setFolder(impresionForm.getFolder());
            adjuntoForm.setBucket(impresionForm.getBucket());
            adjuntoForm.setNombreOriginal(nombrePdf.length() > 50 ? nombrePdf.substring(0, 50) : nombrePdf);
            adjuntoForm.setConcepto(nombrePdf.length() > 50 ? nombrePdf.substring(0, 50) : nombrePdf);
            adjuntoForm.setD(impresionForm.getD());
            quattroUploadClient.getUploadAndAdjuntoByteArray(adjuntoForm, headers).getResult();

        } else {
            impresionForm.setUrls(null);
            impresionForm.setByteArrayPDF(byteArrayPDF);
        }

        return impresionForm;
    }

    public byte[] impresionAmortizacion(AmortizacionPdfForm amortizacionForm) {
        try {
            ImpresionAmortizacionesPdf impresionAmortizacionesPdf = new ImpresionAmortizacionesPdf();
            return impresionAmortizacionesPdf.buildPDF(amortizacionForm);
        } catch (ValidationServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionReclamacion(ImpresionReclamacionProjection impresionReclamacionProjection) {
        try {
            ImpresionReclamacionPdf impresionReclamacionPdf = new ImpresionReclamacionPdf();

            return impresionReclamacionPdf.buildPDF(impresionReclamacionProjection);

        } catch (ValidationServiceException e) {
            throw e;
        } catch (Exception ex) {

            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionCertificado(SURAImpresionEmsionDTO suraImpresionEmsionDTO) {
        try {
            ImpresionCertificadoHogarPdf impresionReclamacionPdf = new ImpresionCertificadoHogarPdf();

            return impresionReclamacionPdf.buildPDF(suraImpresionEmsionDTO);

        } catch (ValidationServiceException e) {
            throw e;
        } catch (Exception ex) {

            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionFiscal(ImpresionFiscalForm impresionFiscalForm,boolean texto) {
        try {
            ImpresionFiscalPdf impresionFiscalPdf = new ImpresionFiscalPdf();

            return impresionFiscalPdf.buildPDF(impresionFiscalForm, texto);

        } catch (ValidationServiceException e) {
            throw e;
        } catch (Exception ex) {

            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionAxa(ImpresionAxaForm impresionAxa) {
        try {
            byte[] byteArrayPDF = null;
            switch (impresionAxa.getTipoImpresion()) {
                case 107:
                    byteArrayPDF = new ImpresionCertificadoPdf().buildPDF(impresionAxa);
                    break;
                case 108:
                    byteArrayPDF = new ImpresionCredencialPdf().buildPDF(impresionAxa);
                    break;
                case 109:
                    byteArrayPDF = new ImpresionEndosoPdf().buildPDF(impresionAxa);
                    break;
                case 110:
                    byteArrayPDF = new ImpresionConstanciaAntiguedad().buildPDF(impresionAxa);
                    break;
                case 111:
                    byteArrayPDF = new ImpresionCartaAntiguedad().buildPDF(impresionAxa);
                    break;

                default:
                    break;
            }

            return byteArrayPDF;

        } catch (ValidationServiceException e) {
            throw e;
        } catch (Exception ex) {

            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionCotizacion(CotizacionProjection cotizacionProjection) {
        try {
            ImpresionCotizacionVida impresionCotizacionVida = new ImpresionCotizacionVida();

            return impresionCotizacionVida.buildPDF(cotizacionProjection);

        } catch (ValidationServiceException e) {
            throw e;
        } catch (Exception ex) {

            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionAxaVida(ImpresionAxaVidaForm impresionAxaVidaForm) {
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionVidaAxaPdf().buildPDF(impresionAxaVidaForm);

            return byteArrayPDF;
        } catch (ValidationServiceException e) {
            throw e;
        } catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }
    public byte[] ImpresionCertificadoAutos(CertificadoProjection  certificadoProjection){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCertificadoChubbPdf().buildPDF(certificadoProjection);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionCaractulaPrudential(CaractulaProjection  caractulaProjection){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaractulaPrudential().buildPDF(caractulaProjection);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

        public byte[] impresionCertificadoVida(CaractulaProjection  caractulaProjection){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCertificadoAfirme().buildPDF(caractulaProjection);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

      public byte[] impresionCertificadoArgos(CaractulaProjection  caractulaProjection){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCertificadoArgos().buildPDF(caractulaProjection);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

      public byte[] impresionPolizAutos(PolizaAutosProjection  polizaAutosProjection){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionPolizaAutosInter().buildPDF(polizaAutosProjection);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }


     public byte[] impresionCaratulaAutos(ImpresionCaratulaForm  impresionCaratulaForm){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaratulaAutos().buildPDF(impresionCaratulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }
     public byte[] impresionCaratulaSalud(ImpresionCaratulaForm  impresionCaratulaForm){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaratulaSalud().buildPDF(impresionCaratulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

     public byte[] impresionMovimiento(MovimientosForm movimientosForm) {
         try {
        	 ImpresionConsultaMovimientos impresionConsultaMovimientos = new ImpresionConsultaMovimientos();
             return impresionConsultaMovimientos.buildPDF(movimientosForm);
         } catch (ValidationServiceException e) {
             throw e;
         } catch (Exception ex) {
             throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
         }
     }


     public byte[] impresionCaratulaVida( ImpresionCaratulaForm  impresionCaractulaForm  ){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaratulaVida().buildPDF(impresionCaractulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }
    public byte[] impresionCaratulaDiversos(ImpresionCaratulaForm  impresionCaratulaForm){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaratulaDiversos().buildPDF(impresionCaratulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionCaratulaColeAutos( ImpresionCaratulaForm  impresionCaractulaForm  ){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaratulaColectividasAutos().buildPDF(impresionCaractulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }
    
    
      public byte[] impresionCaratulaSaludGrupo(ImpresionCaratulaForm  impresionCaratulaForm){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaratulaSaludGrupo().buildPDF(impresionCaratulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

      public byte[] impresionCaratulaVidaGrupo(ImpresionCaratulaForm  impresionCaratulaForm){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCaratulaVidaGrupo().buildPDF(impresionCaratulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }



    public byte[] impresionEndoso( ImpresionCaratulaForm  impresionCaractulaForm  ){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionEndoso().buildPDF(impresionCaractulaForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }



    public byte[] impresionSolicitudPrudential(CaractulaPrudentialProjection  caractulaPrudentialProjection){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionPrudPdf().buildPDF(caractulaPrudentialProjection);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionVitroCaractula(ImpresionBienvenidadForm  impresionBienvenidadForm){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionVitrCBienvenida().buildPDF(impresionBienvenidadForm);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }

    public byte[] impresionCertificadoIndAxa(ImpresionCertificadoAxaForm  impresionCertificadoAxa){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionCertificadoAxaPdf().buildPDF(impresionCertificadoAxa);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }
    
    
}
