package br.com.lab.desafiolabequalizador.mapper;

import br.com.lab.desafiolabequalizador.api.dto.UsuarioDTO;
import br.com.lab.desafiolabequalizador.domain.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario usuario);
    List<UsuarioDTO> toDto(List<Usuario> usuarios);

}
