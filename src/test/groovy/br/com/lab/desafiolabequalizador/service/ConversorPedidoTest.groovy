package br.com.lab.desafiolabequalizador.service

import br.com.lab.desafiolabequalizador.domain.Pedido
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Month

import static java.math.BigDecimal.valueOf
import static java.time.LocalDate.of

class ConversorPedidoTest extends Specification {

    @Unroll
    def "deve converter uma linha em pedido"() {
        when: "converte uma linha em um pedido"
        def pedido = new Pedido(linha)

        then: "os campos devem ser corretamente extraidos e convertidos"
        pedido.idUsuario == idUsuario
        pedido.nome == nome
        pedido.idPedido == idPedido
        pedido.idProduto == produto
        pedido.valor == valor
        pedido.data == data

        where:
        linha                                                                                             | idUsuario | nome                                            | idPedido   | produto    | valor            | data
        "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308" | 70        | "Palmer Prosacco"                               | 753        | 3          | valueOf(1836.74) | of(2021, Month.MARCH, 8)
        "0000000070I                             Palmer Prosacco10000007531000000003     1836.7420210308" | 70        | "I                             Palmer Prosacco" | 1000000753 | 1000000003 | valueOf(1836.74) | of(2021, Month.MARCH, 8)
    }

    def "deve realizar a conversao de um arquivo em uma lista de pedidos com sucesso"() {

    }
}
