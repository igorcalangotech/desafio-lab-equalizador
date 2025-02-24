package br.com.lab.desafiolabequalizador.service

import br.com.lab.desafiolabequalizador.core.domain.PedidoLegado
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Month
import java.util.stream.Collectors

import static java.math.BigDecimal.valueOf
import static java.time.LocalDate.of

class ConversorUsuarioLegadoTest extends Specification {

    Resource arquivoTesteUm;

    def setup() {
        arquivoTesteUm = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_1.txt"))
    }

    @Unroll
    def "deve converter uma linha em pedido"() {
        when: "converte uma linha em um pedido"
        def pedido = new PedidoLegado(linha)

        then: "os campos devem ser corretamente extraidos e convertidos"
        pedido.idUsuario == idUsuario
        pedido.nome == nome
        pedido.idPedido == idPedido
        pedido.idProduto == produto
        pedido.valor == valor
        pedido.data == data

        where:
        [linha, idUsuario, nome, idPedido, produto, valor, data] << [
                [
                        "0000000070                              Palmer Prosacco00000007530000000003     1836.7420210308",
                        70,
                        "Palmer Prosacco",
                        753,
                        3,
                        valueOf(1836.74),
                        of(2021, Month.MARCH, 8)
                ],
                [
                        "0000000070I                             Palmer Prosacco10000007531000000003     1836.7420210308",
                        70,
                        "I                             Palmer Prosacco",
                        1000000753,
                        1000000003,
                        valueOf(1836.74),
                        of(2021, Month.MARCH, 8)
                ]
        ]
    }

    def "deve carregar o conteúdo do arquivo via @Value"() {
        expect:
        arquivoTesteUm != null
    }

    def "deve realizar a conversao de um arquivo em uma lista de pedidos com sucesso"() {
        given:
        def file = new MockMultipartFile(
                "file",
                new FileInputStream(arquivoTesteUm.getFile()).readAllBytes()
        )
        def result = Collections.emptyList()
        when:
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            result = reader.lines()
                    .map { string -> string.trim() }
                    .filter { linha -> !linha.isEmpty() }
                    .map { algo -> return new PedidoLegado(algo) }
                    .collect(Collectors.toList())

        }
        then:
        result.size() == 2352
    }
}
