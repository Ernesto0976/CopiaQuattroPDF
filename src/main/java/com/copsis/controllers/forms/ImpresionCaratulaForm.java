package com.copsis.controllers.forms;

import java.util.List;

import com.copsis.clients.projections.AseguradosGrupoProjection;
import com.copsis.clients.projections.AseguradosProjection;
import com.copsis.clients.projections.BeneficiarioProjection;
import com.copsis.clients.projections.ClienteExtraCaratProjection;
import com.copsis.clients.projections.CoberturaBasicaProjection;
import com.copsis.clients.projections.CoberturaProjection;
import com.copsis.clients.projections.ContratanteCaraProjection;
import com.copsis.clients.projections.InvolucradosProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.clients.projections.UbicacionesProjection;
import com.copsis.clients.projections.VehiculoProjection;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ImpresionCaratulaForm {
   
    private ContratanteCaraProjection contrantante;
    private ClienteExtraCaratProjection clientExtra;
    private List<InvolucradosProjection> involucrados;
    private List<CoberturaProjection> coberturas;
    private VehiculoProjection vehiculo;
    private SocioDirecProjection socio;
    private List<BeneficiarioProjection> beneficiarios;
    private List<AseguradosProjection> asegurados;
    private CoberturaBasicaProjection  coberturaBasica;
    private List<UbicacionesProjection> ubicaciones;
    private List<AseguradosGrupoProjection> aseguradosGrupo;
    
}
