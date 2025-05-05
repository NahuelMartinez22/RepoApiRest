package com.martinez.dentist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    public String getValue(String clave) {
        return configRepository.findByClave(clave)
                .orElseThrow(() -> new RuntimeException("No se encontró la clave: " + clave))
                .getValor();
    }
}
