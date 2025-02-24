package br.com.lab.desafiolabequalizador.service

import br.com.lab.desafiolabequalizador.core.service.PedidoServiceImpl
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class PedidoServiceTest extends Specification {

    Resource primeiroArquivo
    Resource segundoArquivo
    PedidoServiceImpl service = new PedidoServiceImpl()

    def setup() {
        primeiroArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_1.txt"))
        segundoArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_2.txt"))
    }

    def "Deve gerar usuarios do modelo legado para o novo modelo"() {
        given:
        def file = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                new FileInputStream(primeiroArquivo.getFile()).readAllBytes()
        )
        when:
        def usuarios = service.converterParaNovoModelo(file)

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
        service.converterParaNovoModelo(file)

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
        service.converterParaNovoModelo(file)

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
        def result = service.converterParaNovoModelo(file)

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
        service.converterParaNovoModelo(file)

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
        service.converterParaNovoModelo(file)

        then:
        def e = thrown(RuntimeException)
        e.getMessage() == "For input string: \"00000000AA\""
    }

    def "Deve dar erro ao tentar converter arquivo"() {
        given:
        def file = Mock(MultipartFile)
        file.getInputStream() >> { throw new IOException("Converter Erro") }

        when:
        service.converterParaNovoModelo(file)

        then:
        def e = thrown(RuntimeException)
        e.message.contains("Erro ao converter arquivo")
    }
}

