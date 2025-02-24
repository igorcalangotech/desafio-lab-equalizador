package br.com.lab.desafiolabequalizador.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static br.com.lab.desafiolabequalizador.utils.PedidoUtils.singleOrThrow;

@Getter
@NoArgsConstructor
public class Usuario extends SerializableEntity {

    private String nome;
    private List<Pedido> pedidos;

    public Usuario(List<PedidoLegado> pedidosLegado) {
        super(pedidosLegado.stream().map(PedidoLegado::getIdUsuario).findFirst().orElse(null));
        this.nome = extrairNome(pedidosLegado);
        this.pedidos = mapearPedidos(pedidosLegado);
    }

    private List<Pedido> mapearPedidos(List<PedidoLegado> pedidosLegado) {
        return pedidosLegado.stream()
                .collect(Collectors.groupingBy(PedidoLegado::getIdPedido))
                .values()
                .stream()
                .map(Pedido::new)
                .collect(Collectors.toList());
    }

    private String extrairNome(List<PedidoLegado> pedidos) {
        List<String> nomes = pedidos.stream()
                .map(PedidoLegado::getNome)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return singleOrThrow(
                nomes,
                () -> new RuntimeException("Há nomes diferentes nos pedidos para o mesmo usuário: " + nomes)
        );
    }
}
