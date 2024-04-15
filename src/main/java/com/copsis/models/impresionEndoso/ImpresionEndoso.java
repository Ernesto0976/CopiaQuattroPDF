package com.copsis.models.impresionEndoso;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.clients.projections.InvolucradosProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.controllers.forms.ImpresionCaratulaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionEndoso {
    private Color blue = new Color(40, 76, 113);
    private Color black = new Color(0, 0, 0);
    private Color gray = new Color(229, 234, 237);
    private float yStart = 780, bottomMargin = 30, fullWidth = 590, margin = 10;
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();

    private boolean acumula2;

    public byte[] buildPDF(ImpresionCaratulaForm caractula) {
        try {
            ByteArrayOutputStream output;
            try (PDDocument document = new PDDocument()) {
                try {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    BaseTable table;
                    Row<PDPage> baseRow;

                    this.setEncabezado(document, page,caractula);
                    table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 58, "Movimiento", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                    communsPdf.setCell(baseRow, 42, "Contratante", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray);                                
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 58,caractula.getContrantante().getTipoPoliza(), blue, true, "L", 13,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                    communsPdf.setCell(baseRow, 42,caractula.getContrantante().getContrantante(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 14,"Vigencia:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 44,caractula.getContrantante().getVigencia(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 42,caractula.getContrantante().getRfc(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 14,"Emisión:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 44,caractula.getContrantante().getFechaEmision(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 42,caractula.getContrantante().getCteCalle(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 14,"Subramo:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 44,caractula.getContrantante().getSubRamo(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 42,caractula.getContrantante().getCteColonia(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 14,"Forma de Pago:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 44,caractula.getContrantante().getFormaPago(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    //El codigo comentado,es si mas adelante se llevara a ocupar,para lo demas modelos.
                    // communsPdf.setCell(baseRow, 9,"CURP:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                    // communsPdf.setCell(baseRow, 30, caractula.getClientExtra()!=null ?  caractula.getClientExtra().getCurp():"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                     baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 14,"Moneda:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 44,caractula.getContrantante().getMoneda(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    // communsPdf.setCell(baseRow, 7,"Email:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    // communsPdf.setCell(baseRow, 30, caractula.getClientExtra()!=null ? caractula.getClientExtra().getEmail():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                    table.remoBordes(true, 1);
                    table.draw();

                    yStart -=table.getHeaderAndDataHeight()+10;
                    table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table);
                    communsPdf.setCell(baseRow, 58, "Agente:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                    communsPdf.setCell(baseRow, 42, "Grupo:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray);                                
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 14,"Aseguradora:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 44,caractula.getContrantante().getAseguradora(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 42,caractula.getContrantante().getGrupo(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 14,"Clave:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    communsPdf.setCell(baseRow, 44,caractula.getContrantante().getClaveAngente(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                                   
                    table.remoBordes(true, 1);
                    table.draw();


                     yStart -=table.getHeaderAndDataHeight()+4;
                    List<InvolucradosProjection> involucrados = caractula.getInvolucrados();
                  
                    table = new BaseTable(yStart, yStart, bottomMargin, 285, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table,15);
                    communsPdf.setCell(baseRow, 100,"Usuarios Involucrados:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray); 
                    baseRow = communsPdf.setRow(table,13);
                    communsPdf.setCell(baseRow, 25,"Creación:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                    if(!involucrados.isEmpty() && involucrados.size() == 4 && !involucrados.get(0).getIniciales().isEmpty() && !involucrados.get(0).getFechaCreacion().isEmpty() && !involucrados.get(0).getHoraCreacion().isEmpty()){
                       communsPdf.setCell(baseRow, 15,involucrados.get(0).getIniciales(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 45,involucrados.get(0).getFechaCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                       communsPdf.setCell(baseRow, 20,involucrados.get(0).getHoraCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }else{
                       communsPdf.setCell(baseRow, 20,"N/A", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 55,"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }
                     baseRow = communsPdf.setRow(table,13);
                    communsPdf.setCell(baseRow, 25,"Proceso:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                     if(!involucrados.isEmpty() && involucrados.size() == 4 && involucrados.get(1).getIniciales() !=null && involucrados.get(1).getFechaCreacion() !=null && involucrados.get(1).getHoraCreacion() !=null){
                       communsPdf.setCell(baseRow, 15,involucrados.get(1).getIniciales(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 45,involucrados.get(1).getFechaCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                       communsPdf.setCell(baseRow, 20,involucrados.get(1).getHoraCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }else{
                       communsPdf.setCell(baseRow, 20,"N/A", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 55,"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }

                    baseRow = communsPdf.setRow(table,13);
                    communsPdf.setCell(baseRow, 25,"Registro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                     if(!involucrados.isEmpty() && involucrados.size() == 4 && involucrados.get(2).getIniciales() !=null && involucrados.get(2).getFechaCreacion()  !=null && involucrados.get(2).getHoraCreacion()  !=null){
                       communsPdf.setCell(baseRow, 15,involucrados.get(2).getIniciales(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 45,involucrados.get(2).getFechaCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                       communsPdf.setCell(baseRow, 20,involucrados.get(2).getHoraCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }else{
                       communsPdf.setCell(baseRow, 20,"N/A", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 55,"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }

                    baseRow = communsPdf.setRow(table,13);
                    communsPdf.setCell(baseRow, 25,"Impresión:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                     if(!involucrados.isEmpty() && involucrados.size() == 4 && involucrados.get(3).getIniciales()  !=null && involucrados.get(3).getFechaCreacion()  !=null && involucrados.get(3).getHoraCreacion()  !=null){
                       communsPdf.setCell(baseRow, 15,involucrados.get(3).getIniciales(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 45,involucrados.get(3).getFechaCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                       communsPdf.setCell(baseRow, 20,involucrados.get(3).getHoraCreacion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }else{
                       communsPdf.setCell(baseRow, 20,"N/A", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                       communsPdf.setCell(baseRow, 55,"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);   
                    }
             
                    table.remoBordes(true, 1);
                    table.draw();

                            
                    yStart -=table.getHeaderAndDataHeight()+5;
                    table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                    baseRow = communsPdf.setRow(table, 15);
                    communsPdf.setCell(baseRow, 100, "Descripción del Endoso", black, true, "L", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f), gray);
                    table.draw();
                    yStart -=table.getHeaderAndDataHeight();
                    if (caractula.getContrantante() != null && caractula.getContrantante().getDescripcion().length() > 0) {

                        String dato = Sio4CommunsPdf.eliminaHtmlTags3(caractula.getContrantante().getDescripcion());
                        String datos[] = dato.split("<br>|<br/>|</br>");

                        int y = 0;
                        while (y < datos.length) {
                            acumula2 = true;
                            table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, false, true);
                            baseRow = communsPdf.setRow(table, 13);
                            communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(datos[y]), black, false, "L", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f), gray);
                            if (isEndOfPage(table)) {
                                table.getRows().remove(table.getRows().size() - 1);
                                table.draw();
                                page = new PDPage();
                                document.addPage(page);
                                this.setEncabezado(document, page, caractula);
                                acumula2 = false;
                            } else {
                                table.remoBordes(true, 1);
                                table.draw();
                                yStart -= table.getHeaderAndDataHeight();
                            }
                            if (acumula2) {
                                y++;
                            }
                            if (y > 100) {
                                table.draw();
                                break;

                            }
                        }
                    }


                    output = new ByteArrayOutputStream();
                    document.save(output);
                    //Esta linea permite guardar el docuemto en local.
                   // document.save(new File("/home/aalbanil/Vídeos/endoso.pdf"));
                    return output.toByteArray();
                } finally {
                    document.close();
                }

            }

        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresionEndoso: " + ex.getMessage());
        }

    }

      private void setEncabezado(PDDocument document, PDPage page,ImpresionCaratulaForm caratula) {
        try (PDPageContentStream conten = new PDPageContentStream(document,page)){
            yStart = 780;
            BaseTable table;
            Row<PDPage> baseRow;
            table = new BaseTable(yStart, yStart, bottomMargin, 300, margin, document, page, true, true);
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 41, "Póliza", black, true, "C", 11,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
            communsPdf.setCell(baseRow, 18, "OT", black, true, "C", 11,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray); 
            communsPdf.setCell(baseRow, 41, "Endoso", black, true, "C", 11,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray);                                                       
            baseRow = communsPdf.setRow(table);
            communsPdf.setCell(baseRow, 41,caratula.getContrantante().getNoPoliza(), blue, false, "C", 12,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
            communsPdf.setCell(baseRow, 18, caratula.getContrantante().getPolizaID(), blue, false, "C", 12,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
            communsPdf.setCell(baseRow, 41,caratula.getContrantante().getNoEndoso(), blue, false, "C", 12,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                                
            table.draw();
            SocioDirecProjection socio = caratula.getSocio();
            if(socio !=null){       
            yStart -=table.getHeaderAndDataHeight();
            table = new BaseTable(775, 775, bottomMargin, 150, 460, document, page, false, true);
            baseRow = communsPdf.setRow(table, 12);
            communsPdf.setCell(baseRow, 100, ImageUtils.readImage(socio.getAvatar()).scale(130, 130));
            table.draw();
             }

            

           
            table = new BaseTable(yStart, yStart, bottomMargin, 415, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table,8);
            communsPdf.setCell(baseRow, 100, socio !=null ? socio.getCalle() :"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
            baseRow = communsPdf.setRow(table,8);
            communsPdf.setCell(baseRow, 100,socio !=null ? socio.getColonia():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
            baseRow = communsPdf.setRow(table,8);
            communsPdf.setCell(baseRow, 100, socio !=null ? socio.getEstado():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);         
            table.draw();
            yStart -=table.getHeaderAndDataHeight()+20;
          this.setFooter(document, page,caratula.getInvolucrados().isEmpty() ?  " " : caratula.getInvolucrados().get(3).getFirma());//quitar valor
            
        } catch (Exception ex) {
            ex.printStackTrace();
         throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio setEncabezado: " + ex.getMessage());
        }

    }

    private void setFooter(PDDocument document, PDPage page,String firma) {
        try {
            BaseTable table;
            Row<PDPage> baseRow;
            table = new BaseTable(30, 30, 10, fullWidth, margin, document, page, false, true);
            baseRow = communsPdf.setRow(table,15);
            communsPdf.setCell(baseRow, 33,"",blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
            communsPdf.setCell(baseRow, 33,"Powered by <b>quattroCRM</b>",Color.black, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
            communsPdf.setCell(baseRow, 33,"<b>FIRMA:</b> " + firma,blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(0f), Color.white); 
            table.draw();
            
        } catch (Exception ex) {
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio setFooter: " + ex.getMessage());
        }
    }


    private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        boolean isEndOfPage = currentY <= bottomMargin;
        return isEndOfPage;
    }
}