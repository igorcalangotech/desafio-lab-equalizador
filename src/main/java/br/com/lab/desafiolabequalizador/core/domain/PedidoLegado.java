package br.com.lab.desafiolabequalizador.core.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class PedidoLegado {

    private final Long idUsuario;
    private final String nome;
    private final Long idPedido;
    private final Long idProduto;
    private final BigDecimal valor;
    private final LocalDate data;

    public PedidoLegado(String linha) {

        this.idUsuario = Long.parseLong(linha.substring(0, 10).trim());
        this.nome = linha.substring(10, 55).trim();
        this.idPedido = Long.parseLong(linha.substring(55, 65).trim());
        this.idProduto = Long.parseLong(linha.substring(65, 75).trim());
        this.valor = new BigDecimal(linha.substring(75, 87).trim());
        this.data = LocalDate.parse(linha.substring(87, 95).trim(), DateTimeFormatter.ofPattern("yyyyMMdd"));

    }
}
