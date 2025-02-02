package com.copsis.models.qualitas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class QualitasAutosUsaModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();


    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
        .replace("Hasta-To", ConstantsValue.HASTA_TO)
        .replace("PLAN:", ConstantsValue.PLAN)
        .replace("Clave, Marca, Descripción:", "Clave, Marca,Descripción:");
        try {
          
			modelo.setCia(29);			
			modelo.setTipo(1);
          
            inicio = contenido.indexOf(ConstantsValue.PLAN);
			fin = contenido.indexOf("COBERTURAS CONTRATADAS");
           
			newcontenido.append(fn.extracted(inicio, fin, contenido).replace("las###12:00 P.M.", ""));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
              
               if(newcontenido.toString().split("\n")[i].contains("TOURIST VEHICLE POLICY")){
                if(newcontenido.toString().split("\n")[i+1].split("###").length> 1){
                modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[0]);
                }else{
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("###")[1]);
                }   
                   
                
                
               }
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.PLAN)){
                  modelo.setPlan(newcontenido.toString().split("\n")[i].split(ConstantsValue.PLAN)[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("ASEGURADO")){
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[1].trim()); 
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio-Address:") && newcontenido.toString().split("\n")[i].contains(ConstantsValue.NUMEROPM)){
                  
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio-Address:")[1].split(ConstantsValue.NUMEROPM)[0].replace("###", "").trim()); 
                }

                if(newcontenido.toString().split("\n")[i].contains("C.P.-ZIP:")){
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.-ZIP:")[1].replace("###", "").trim().substring(0,5)); 
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P-ZIP:")){
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P-ZIP:")[1].replace("###", "").trim().substring(0,5)); 
                }
              
                if(newcontenido.toString().split("\n")[i].contains("Clave, Marca,Descripción:") && newcontenido.toString().split("\n")[i].split("###").length >=2){                 
                    modelo.setClave(newcontenido.toString().split("\n")[i].split("###")[1]);
                   
                        modelo.setDescripcion(newcontenido.toString().split("\n")[i].split("###")[2].trim());
                    
                    
                }
                if(newcontenido.toString().split("\n")[i].contains("Clave, Marca,Descripción:") 
                   &&   newcontenido.toString().split("\n")[i].split("###").length ==1){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
					 if(!valores.isEmpty()){
                       modelo.setClave(valores.get(0));
                        if( newcontenido.toString().split("\n")[i].split(modelo.getClave())[1].length() > -1){
                            modelo.setDescripcion(newcontenido.toString().split("\n")[i].split(modelo.getClave())[1].trim());
                        }
                     
                     }    
                
                }
                
                if(newcontenido.toString().split("\n")[i].contains("Modelo-Year:") && newcontenido.toString().split("\n")[i].contains("Ocupantes")){
             
                    modelo.setModelo(fn.castInteger(fn.obtenerListNumeros2( newcontenido.toString().split("\n")[i].split("Modelo-Year:")[1]).get(0)));
                }
                if(newcontenido.toString().split("\n")[i].contains("Serie-V.I.N:") && newcontenido.toString().split("\n")[i].contains("Motor:")){
                    modelo.setSerie(newcontenido.toString().split("\n")[i].split("Serie-V.I.N:")[1].split("Motor:")[0].replace("###", "").trim());
                }
              
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.DESDE_FROM) 
                 && newcontenido.toString().split("\n")[i].contains("Payment") 
                 &&(!fn.vigenciaUsa(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE_FROM)[1].split("###")[1]).isEmpty() 
                 || !fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE_FROM)[1].split("###")[1]).isEmpty())
                 ){                  
                  if(!fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE_FROM)[1].split("###")[1]).isEmpty()){                                 
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE_FROM)[1].split("###")[1]).get(0)));
                   modelo.setFechaEmision(modelo.getVigenciaDe());
                  }else{

                    String x = fn.vigenciaUsa(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE_FROM)[1].split("###")[1]).get(0);               
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("-")[1] +"-" + x.split("-")[0] +"-"+ x.split("-")[2]));
                   modelo.setFechaEmision(modelo.getVigenciaDe());
                  }                   

                }
                if(modelo.getVigenciaDe().isEmpty() && newcontenido.toString().split("\n")[i].contains(ConstantsValue.DESDE_FROM) 
                 && newcontenido.toString().split("\n")[i].contains("Payment")
                 && !fn.vigenciaUsa(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE_FROM)[1].split("###")[0]).isEmpty()  ){
                   
                    String x = fn.vigenciaUsa(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESDE_FROM)[1].split("###")[0]).get(0);               
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(x.split("-")[1] +"-" + x.split("-")[0] +"-"+ x.split("-")[2]));
                   modelo.setFechaEmision(modelo.getVigenciaDe());
                }
                
             
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.HASTA_TO) && newcontenido.toString().split("\n")[i].contains("Duración") &&
                (!fn.vigenciaUsa(newcontenido.toString().split("\n")[i].split(ConstantsValue.HASTA_TO)[1].split("###")[1]).isEmpty() 
                || !fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].split(ConstantsValue.HASTA_TO)[1].split("###")[1]).isEmpty())){                                     
                    if(!fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].split(ConstantsValue.HASTA_TO)[1].split("###")[1]).isEmpty()){                       
                        modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i].split(ConstantsValue.HASTA_TO)[1].split("###")[1]).get(0)));
                    }else{
                        String x = fn.vigenciaUsa(newcontenido.toString().split("\n")[i].split(ConstantsValue.HASTA_TO)[1].split("###")[1]).get(0);               
                        modelo.setVigenciaA(fn.formatDateMonthCadena(x.split("-")[1] +"-" + x.split("-")[0] +"-"+ x.split("-")[2]));
                    }
                }

            }
           if(fn.diferenciaDias(modelo.getVigenciaDe(), modelo.getVigenciaA()) < 30){
                  modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
           }


            inicio = contenido.indexOf("COBERTURAS CONTRATADAS");
			fin = contenido.indexOf("the Civil Liability");            
            fin = fin == -1 ? contenido.indexOf("Deductible:") : fin;
            fin = fin == -1 ? contenido.indexOf("Las adaptaciones") : fin;        
            getCoberturas(contenido, inicio, fin);


            inicio = contenido.indexOf("MONEDA");
			fin = contenido.indexOf("Tarifa Aplicada");
            getPrimas(contenido, inicio, fin);
       
  
      
            if(modelo.getFormaPago() ==1){
                List<EstructuraRecibosModel> recibos = new ArrayList<>();
                EstructuraRecibosModel recibo = new EstructuraRecibosModel();

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
                recibo.setIva(modelo.getIva());

                recibo.setPrimaTotal(modelo.getPrimaTotal());
                recibo.setAjusteUno(modelo.getAjusteUno());
                recibo.setAjusteDos(modelo.getAjusteDos());
                recibo.setCargoExtra(modelo.getCargoExtra());
                recibos.add(recibo);
                modelo.setRecibos(recibos);
            }
         

            return modelo;
        }catch (Exception ex){               
            modelo.setError(QualitasAutosUsaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "+ ex.getCause());
            return modelo;
        }
      
     }


    private void getCoberturas(String contenido, int inicio, int fin) {
        StringBuilder newcontenido;
        newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
    
            if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS") && !newcontenido.toString().split("\n")[i].contains("DEDUCTIBLE")){
      
                switch (newcontenido.toString().split("\n")[i].split("###").length) {
                    case 3:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;
                    case 4:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
                        coberturas.add(cobertura);
                        break;
                    default:
                        break;
                }
            }                           
        }
        modelo.setCoberturas(coberturas);
    }


    private void getPrimas(String contenido, int inicio, int fin) {
        StringBuilder newcontenido;
        newcontenido = new StringBuilder();
        newcontenido.append(fn.extracted(inicio, fin, contenido));
        if(newcontenido.toString().length() > 0){
            modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString()));
        for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
            if(newcontenido.toString().split("\n")[i].contains("MONEDA")) {
                
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
            }
            if(newcontenido.toString().split("\n")[i].contains("Prima Neta-Net")) {
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            }
            if(newcontenido.toString().split("\n")[i].contains("Gastos de Expedición de")) {
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
            }
            if(newcontenido.toString().split("\n")[i].contains("I.V.A")) {
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                if(!valores.isEmpty() ){
                        modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                }
                
            }
            if(newcontenido.toString().split("\n")[i].contains("IMPORTE TOTA")) {
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                
            }
        }
      }
    }  
}
