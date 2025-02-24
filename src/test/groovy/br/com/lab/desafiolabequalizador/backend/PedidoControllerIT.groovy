package br.com.lab.desafiolabequalizador.backend

import br.com.lab.desafiolabequalizador.api.v1.PedidoRestService
import br.com.lab.desafiolabequalizador.support.ITSupport
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.mock.web.MockMultipartFile

class PedidoControllerIT extends ITSupport {

    Resource primeiroArquivo
    Resource segundoArquivo

    def setup() {
        primeiroArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_1.txt"))
        segundoArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_2.txt"))
    }

    def "Deve converter um arquivo legado com sucesso"() {
        given:
        def url = PedidoRestService.PATH + PedidoRestService.CONVERTER_ARQUIVO_LEGADO
        def arquivo = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                new FileInputStream(primeiroArquivo.getFile()).readAllBytes()
        )

        when:
        def result = httpPost(url, arquivo)

        then:
        result != null
    }

}
