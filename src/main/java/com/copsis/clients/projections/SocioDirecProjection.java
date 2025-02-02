package com.copsis.clients.projections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SocioDirecProjection {
    private String calle;
    private String colonia;
    private String estado;
    private String avatar;
    private String nombSocio;
}
