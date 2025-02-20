package br.com.lab.desafiolabequalizador.api.dto;

import java.math.BigDecimal;

public record ProdutoDTO(
        Long id,
        BigDecimal valor
) {
}
