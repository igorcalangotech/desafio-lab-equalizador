package br.com.lab.desafiolabequalizador.support

import br.com.lab.desafiolabequalizador.DesafioLabEqualizadorApplication
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification


@SpringBootTest(classes = DesafioLabEqualizadorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class ITSupport extends Specification {

    @Autowired
    protected MockMvc mvc

    @Autowired
    protected ObjectMapper objectMapper

    ResultActions httpPost(String path, MockMultipartFile file) {
        mvc.perform(
                MockMvcRequestBuilders.multipart(path)
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
    }

    def <T> T fromJsonResult(ResultActions result, Class<T> type) {
        objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), type) as T
    }

    def <T> T fromJsonResult(ResultActions result, TypeReference ref) {
        objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ref) as T
    }


}
