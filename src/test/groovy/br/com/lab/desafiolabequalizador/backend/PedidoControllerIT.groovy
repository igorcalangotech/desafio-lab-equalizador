package br.com.lab.desafiolabequalizador.backend

import br.com.lab.desafiolabequalizador.api.dto.UsuarioDTO
import br.com.lab.desafiolabequalizador.api.v1.PedidoRestService
import br.com.lab.desafiolabequalizador.support.ITSupport
import com.fasterxml.jackson.core.type.TypeReference
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Unroll

import java.time.LocalDate

import static java.time.LocalDate.of
import static java.time.Month.OCTOBER

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
        List<UsuarioDTO> usuarios = fromJsonResult(result, new TypeReference<List<UsuarioDTO>>() {})
        then:
        usuarios != null
        usuarios.size() == 100
    }

    def "Deve converter um arquivo legado com sucesso usando filtros de pesquisa"() {
        given:
        def url = PedidoRestService.PATH + PedidoRestService.CONVERTER_ARQUIVO_LEGADO
        def arquivo = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                new FileInputStream(segundoArquivo.getFile()).readAllBytes()
        )

        when:
        def result = httpPost(url, arquivo)
        List<UsuarioDTO> usuarios = fromJsonResult(result, new TypeReference<List<UsuarioDTO>>() {})
        then:
        usuarios != null
        usuarios.size() == 200
    }

    @Unroll
    def "Deve converter um arquivo legado com sucesso usando filtro idPedido"() {
        given:
        def url = buildUrlWIthParameters(idPedido, dataInicio, dataFim)
        def arquivo = new MockMultipartFile(
                "file",
                "fileName.txt",
                "text/plain",
                new FileInputStream(segundoArquivo.getFile()).readAllBytes()
        )

        when:
        def result = httpPost(url, arquivo)
        List<UsuarioDTO> usuarios = fromJsonResult(result, new TypeReference<List<UsuarioDTO>>() {})

        then:
        usuarios != null
        usuarios.size() == tamanho

        where:
        idPedido | dataInicio           | dataFim               | tamanho
        354      | null                 | null                  | 1
        null     | of(2021, OCTOBER, 1) | of(2021, OCTOBER, 31) | 114
    }

    private static String buildUrlWIthParameters(Long idPedido, LocalDate dataInicio, LocalDate dataFim) {
        def baseUrl = PedidoRestService.PATH + PedidoRestService.CONVERTER_ARQUIVO_LEGADO
        def queryParams = [:]

        if (idPedido) queryParams["orderId"] = idPedido
        if (dataInicio) queryParams["beginDate"] = dataInicio
        if (dataFim) queryParams["endDate"] = dataFim

        return queryParams ? "${baseUrl}?${queryParams.collect { k, v -> "${k}=${v}" }.join('&')}" : baseUrl
    }

    ResultActions httpPost(String path, MockMultipartFile file) {
        mvc.perform(
                MockMvcRequestBuilders.multipart(path)
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
    }

}
