/**
 * 
 */
package com.copsis.models;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Israel_work
 *
 */
@Component
@ConfigurationProperties(prefix = "catalogo-reforma-fiscal")
@Getter
@Setter
public class CatalogoReformaFiscalProps {
	private List<Map<String, String>> regimenes;
}
