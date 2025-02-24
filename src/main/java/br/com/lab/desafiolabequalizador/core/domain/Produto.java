package br.com.lab.desafiolabequalizador.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class Produto {

    private Long id;
    private BigDecimal valor;

}
