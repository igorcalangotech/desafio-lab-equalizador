package br.com.lab.desafiolabequalizador.backend

import br.com.lab.desafiolabequalizador.DesafioLabEqualizadorApplication
import br.com.lab.desafiolabequalizador.api.v1.PedidoRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Ignore
import spock.lang.Specification

@SpringBootTest(classes = DesafioLabEqualizadorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PedidoControllerIT extends Specification {

    Resource primeiroArquivo
    Resource segundoArquivo

    @Autowired
    MockMvc mvc

    def setup() {
        primeiroArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_1.txt"))
        segundoArquivo = new UrlResource(getClass().getClassLoader().getResource("arquivos/data_2.txt"))
    }

    @Ignore
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

    ResultActions httpPost(String path, MockMultipartFile file) {
        mvc.perform(
                MockMvcRequestBuilders.multipart(path)
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
    }

}
