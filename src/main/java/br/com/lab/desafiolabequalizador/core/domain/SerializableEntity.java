package br.com.lab.desafiolabequalizador.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class SerializableEntity {

    protected Long id;

    public SerializableEntity(Long id) {
        this.id = id;
    }
}
