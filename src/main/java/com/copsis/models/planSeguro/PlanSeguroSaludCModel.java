package com.copsis.models.planSeguro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PlanSeguroSaludCModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(3);
            modelo.setCia(25);

            inicio = contenido.indexOf("Número de Póliza");
            fin = contenido.indexOf("Lista de Asegurados");        
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
           
             if(newcontenido.toString().split("\n")[i].contains("Número de Póliza") && newcontenido.toString().split("\n")[i+1].contains("Desde")){                            
                modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
                modelo.setPoliza(newcontenido.toString().split("\n")[i+2].split("###")[0]);                  
                modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+2]).get(0)));
                modelo.setFechaEmision(modelo.getVigenciaDe());
            }

             if(newcontenido.toString().split("\n")[i].contains("Datos del Contratante") && newcontenido.toString().split("\n")[i+1].contains("Nombre:")
             &&newcontenido.toString().split("\n")[i+1].contains("RFC:")){
                modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("Nombre:")[1].split("RFC:")[0].replace("###", "").trim());
               
                if(newcontenido.toString().split("\n")[i+1].split("RFC:")[1].contains("Teléfono:")){
                    modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("RFC:")[1].split("Teléfono:")[0].replace("###", "").trim());      
                }else{
                    modelo.setRfc(newcontenido.toString().split("\n")[i+1].split("RFC:")[1].replace("###", "").trim());      
                }
                      
             }
             
             if(newcontenido.toString().split("\n")[i].contains("Dirección:")){
                modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Dirección:")[1].replace("###", "").trim());
             } 

              
             if(newcontenido.toString().split("\n")[i].contains("C.P") && newcontenido.toString().split("\n")[i].contains("Teléfono:")){
                modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("C.P")[1].replace("###", "").trim().substring(0,5));
             }
             if(newcontenido.toString().split("\n")[i].contains("C.P.") && newcontenido.toString().split("\n")[i].contains("Correo electrónico:")){
                modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].replace("###", "").trim().substring(0,5));
             }

            if(newcontenido.toString().split("\n")[i].contains("Contratado") && newcontenido.toString().split("\n")[i].contains("Pago")){                          
               modelo.setPlan((newcontenido.toString().split("\n")[i+1]+" Óptimo").replace("###", " "));
               modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i+2]));
            }

            }
            modelo.setMoneda(1);

 
            inicio = contenido.indexOf("Lista de Asegurados");
            fin = contenido.indexOf("Conceptos Económicos"); 
            newcontenido = new StringBuilder();       
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
                EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                if(newcontenido.toString().split("\n")[i].split("-").length > 3){
                    asegurado.setNombre( newcontenido.toString().split("\n")[i].split("###")[0]);               
                    asegurado.setNacimiento(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(0)));
                    asegurado.setAntiguedad(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(1)));
                    asegurados.add(asegurado);
                }
            }
            modelo.setAsegurados(asegurados);
         
            inicio = contenido.indexOf("Conceptos Económicos");
            fin = contenido.indexOf("Cobertura básica"); 
            fin= fin ==-1  ?contenido.indexOf("Coberturas básicas"):fin;
      
            newcontenido = new StringBuilder();       
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
                if(newcontenido.toString().split("\n")[i].contains("Expedición")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                     modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                        modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                        modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(5))));		       		  
                        modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(6))));   
               }
              
            
            }

           
            inicio = contenido.indexOf("Cobertura básica");
           
            fin = contenido.indexOf("Clave de Agente"); 
            newcontenido = new StringBuilder();       
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            if(!newcontenido.isEmpty()){

            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                  if(!newcontenido.toString().split("\n")[i].contains("Cobertura básica")                  
                  && !newcontenido.toString().split("\n")[i].contains("Tabla de Honorarios")
                  && !newcontenido.toString().split("\n")[i].contains("condiciones generales")
                  && !newcontenido.toString().split("\n")[i].contains("Mensual")  ){                
                    switch (newcontenido.toString().split("\n")[i].split("###").length ) {
                        case 2:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                            break;                    
                        default:
                        if(!newcontenido.toString().split("\n")[i].contains("Coaseguro contratada")){
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                            coberturas.add(cobertura);
                        }                       
                            break;
                    }
                  }
            }
            modelo.setCoberturas(coberturas);
        }


            if(modelo.getCoberturas().isEmpty()){
                inicio= contenido.indexOf("Coberturas básicas");
                fin = contenido.indexOf("Clave de Agente"); 
              
                newcontenido = new StringBuilder();       
                newcontenido.append(fn.extracted(inicio, fin, contenido)
                .replace("Cámara###hiperbárica", "Cámara###hiperbárica")
                .replace("Rehabilitación###y###fisioterapia","Rehabilitación y fisioterapia")
                .replace("Complicaciones###del###embarazo", "Complicaciones del embarazo")
                .replace("Parto###o###cesárea","Parto o cesárea" )
                .replace("Prótesis,###aparatos","Prótesis, aparatos" )
                );
               List<String> listParentescos = Arrays.asList("Ozonoterapia","Rehabilitación y fisioterapia","Cámara hiperbárica","Complicaciones del embarazo"
               ,"Circuncisión","Prótesis, aparatos");
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                     EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    
                    for (int x = 0; x < listParentescos.size(); x++) {
                        if(newcontenido.toString().split("\n")[i].contains( listParentescos.get(x))){
                          
                            if(newcontenido.toString().split("\n")[i].contains("Hasta") && newcontenido.toString().split("\n")[i].contains("UMAM")){
                                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("Hasta")[0].replace("###", " ").trim());
                                    cobertura.setSa("Hasta "+newcontenido.toString().split("\n")[i].split("Hasta")[1].replace("###", " ").trim());
                                   coberturas.add(cobertura);
                            }else{
                                cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                                cobertura.setSa(newcontenido.toString().split("\n")[i].split(newcontenido.toString().split("\n")[i].split("###")[0])[1]
                                .replace("###", " ").trim());
                                coberturas.add(cobertura);
                                
                            }
                        }
                    }
                  
                  
                }
                 modelo.setCoberturas(coberturas);
            }
			
            


            return modelo;
        } catch (Exception ex) {
            modelo.setError(PlanSeguroSaludCModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }
    }
}
