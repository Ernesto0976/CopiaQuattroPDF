package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PaqueteCoberturaProjection {
	private String nombres;
	private Integer Incluido;
	
	public PaqueteCoberturaProjection() {
		
	}
	public PaqueteCoberturaProjection(String nombres, Integer incluido) {
		super();
		this.nombres = nombres;
		this.Incluido = incluido;
	}

}