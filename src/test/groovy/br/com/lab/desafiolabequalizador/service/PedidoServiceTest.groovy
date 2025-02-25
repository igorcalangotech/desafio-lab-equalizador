package br.com.lab.desafiolabequalizador.service

import br.com.lab.desafiolabequalizador.core.service.PedidoServiceImpl
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

class PedidoServiceTest extends Specification {

    Resource primeiroArquivo
    Resource segundoArquivo
    PedidoServiceImpl service = new PedidoServiceImpl()

    def setup() {
        primeiroArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_1.txt"))
        segundoArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_2.txt"))
    }

    def "Deve gerar usuarios do modelo legado para o novo modelo com sucesso"() {
        given:
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                new FileInputStream(primeiroArquivo.getFile()).readAllBytes()
        )
        when:
        def usuarios = service.converterParaNovoModelo(file, null, null, null)

        then:
        usuarios.size() == 100
        usuarios.unique { [it.id, it.nome] }.size() == usuarios.size()
        usuarios.each { usuario ->
            usuario.pedidos.unique { it.id }.size() == usuario.pedidos.size()
            usuario.pedidos.each {
                pedido ->
                    pedido.valor == pedido.produtos.valor.inject(BigDecimal.ZERO) {
                        acumulador, valor ->
                            acumulador + valor
                    }
            }
        }
    }

    def "Deve dar erro ao converter arquivo quando possui um mesmo idUsuario com nome diferente"() {
        given:
        def arquivo = "0000000085                                   Jama Block00000009080000000002     1872.9420210306\n" +
                "0000000085                                  IJama Block00000009080000000002     1872.9420210306"
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                arquivo.bytes
        )

        when:
        service.converterParaNovoModelo(file, null, null, null)

        then:
        def e = thrown(RuntimeException)
        e.getMessage() == "Há nomes diferentes nos pedidos para o mesmo usuário: [Jama Block, IJama Block]"
    }

    def "Deve dar erro ao converter arquivo quando possui um mesmo pedido com datas diferentes"() {
        given:
        def arquivo = "0000000085                                   Jama Block00000009080000000002     1872.9420210306\n" +
                "0000000085                                   Jama Block00000009080000000002     1872.9420210307"
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                arquivo.bytes
        )

        when:
        service.converterParaNovoModelo(file, null, null, null)

        then:
        def e = thrown(RuntimeException)
        e.getMessage() == "Há datas diferentes no mesmo pedido: [2021-03-06, 2021-03-07]"
    }

    def "Deve converter arquivo quando possuir uma linha vazia"() {
        given:
        def arquivo = "0000000085                                   Jama Block00000009080000000002     1872.9420210306\n" +
                "\n" +
                "0000000085                                   Jama Block00000009080000000002     1872.9420210306"
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                arquivo.bytes
        )

        when:
        def result = service.converterParaNovoModelo(file, null, null, null)

        then:
        result.size() == 1
    }

    def "Deve dar erro ao converter arquivo legada quando formato diferente"() {
        given:
        def arquivo = "0000000085                                   Jama Block00000009080000000002     1872.9420210306\n" +
                "00000000AA                                   Jama Block00000009080000000002     1872.9420210306"
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                arquivo.bytes
        )

        when:
        service.converterParaNovoModelo(file, null, null, null)

        then:
        def e = thrown(RuntimeException)
        e.getMessage() == "For input string: \"00000000AA\""
    }

    def "Deve dar erro ao ler arquivo legado"() {
        given:
        def arquivo = "0000000085                                   Jama Block00000009080000000002     1872.9420210306\n" +
                "00000000AA                                   Jama Block00000009080000000002     1872.9420210306"
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                arquivo.bytes
        )

        when:
        service.converterParaNovoModelo(file, null, null, null)

        then:
        def e = thrown(RuntimeException)
        e.getMessage() == "For input string: \"00000000AA\""
    }

    def "Deve dar erro ao tentar converter arquivo"() {
        given:
        def file = Mock(MultipartFile)
        file.getInputStream() >> { throw new IOException("Converter Erro") }

        when:
        service.converterParaNovoModelo(file, null, null, null)

        then:
        def e = thrown(RuntimeException)
        e.message.contains("Erro ao converter arquivo")
    }

    def "Deve gerar usuarios do modelo legado para o novo modelo com filtros de data inicio e fim"() {
        given:
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                new FileInputStream(primeiroArquivo.getFile()).readAllBytes()
        )

        def startDate = LocalDate.of(2021, Month.MAY, 1)
        def endDate = LocalDate.of(2021, Month.MAY, 20)
        when:
        def usuarios = service.converterParaNovoModelo(file, null, startDate, endDate)

        then:
        usuarios.size() == 51
        usuarios.unique { [it.id, it.nome] }.size() == usuarios.size()
        usuarios.each { usuario ->
            usuario.pedidos.unique { it.id }.size() == usuario.pedidos.size()
            usuario.pedidos.each {
                pedido ->
                    pedido.valor == pedido.produtos.valor.inject(BigDecimal.ZERO) {
                        acumulador, valor ->
                            acumulador + valor
                    }
            }
        }
    }

    def "Deve gerar usuarios do modelo legado para o novo modelo com filtro de idPedido"() {
        given:
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                new FileInputStream(primeiroArquivo.getFile()).readAllBytes()
        )

        when:
        def usuarios = service.converterParaNovoModelo(file, 112, null, null)

        then:
        usuarios.size() == 1
        usuarios.get(0).pedidos.size() == 1
        usuarios.get(0).nome == "Ms. Maryland Klocko"

    }
}

