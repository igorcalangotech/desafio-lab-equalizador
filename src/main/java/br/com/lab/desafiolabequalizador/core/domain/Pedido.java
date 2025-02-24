package br.com.lab.desafiolabequalizador.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.com.lab.desafiolabequalizador.utils.PedidoUtils.singleOrThrow;

@Getter
@NoArgsConstructor
public class Pedido extends SerializableEntity {

    private BigDecimal valor;
    private LocalDate data;
    private List<Produto> produtos;

    public Pedido(List<PedidoLegado> pedidoLegados) {
        super(pedidoLegados.stream().map(PedidoLegado::getIdPedido).findFirst().orElse(null));
        this.produtos = mapearProdutos(pedidoLegados);
        this.valor = calcularValorTotal(this.produtos);
        this.data = extrairData(pedidoLegados);
    }

    private static List<Produto> mapearProdutos(List<PedidoLegado> pedidoLegados) {
        return pedidoLegados.stream()
                .map(item -> new Produto(item.getIdProduto(), item.getValor()))
                .collect(Collectors.toList());
    }

    private BigDecimal calcularValorTotal(List<Produto> produtos) {
        return produtos.stream()
                .map(Produto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LocalDate extrairData(List<PedidoLegado> itens) {
        List<LocalDate> datas = itens.stream()
                .map(PedidoLegado::getData)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return singleOrThrow(
                datas,
                () -> new RuntimeException("Há datas diferentes no mesmo pedido: " + datas)
        );
    }
}
