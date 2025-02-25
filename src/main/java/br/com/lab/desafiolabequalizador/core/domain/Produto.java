package br.com.lab.desafiolabequalizador.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class Produto extends SerializableEntity {

    private BigDecimal valor;

    public Produto(Long id, BigDecimal valor) {
        super(id);
        this.valor = valor;
    }
}
