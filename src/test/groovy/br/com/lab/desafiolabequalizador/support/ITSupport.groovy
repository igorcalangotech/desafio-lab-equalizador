package br.com.lab.desafiolabequalizador.support

import br.com.lab.desafiolabequalizador.DesafioLabEqualizadorApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification


@SpringBootTest(classes = DesafioLabEqualizadorApplication.class)
abstract class ITSupport extends Specification {

    @Autowired
    protected MockMvc mvc

    ResultActions httpPost(String path, MockMultipartFile file) {
        mvc.perform(
                MockMvcRequestBuilders.multipart(path)
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
    }

}
