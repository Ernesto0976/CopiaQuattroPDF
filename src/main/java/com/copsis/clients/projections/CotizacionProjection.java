package com.copsis.clients.projections;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CotizacionProjection {
	private String producto;
	private List<ProspectoProjection> prospecto;
	private String plan;
	private String moneda;
	private String plazoPagos;
	private String primaAnual;
	private String aportacion;
	private String primaMensual;
	private String valorUdi;
	private String primaAnualex;
	private String aportacionex;
	private String primaMensualex;
	private String  primaUdiTitular;
	private String  primaUdiMenor;
	private boolean vida;

}
