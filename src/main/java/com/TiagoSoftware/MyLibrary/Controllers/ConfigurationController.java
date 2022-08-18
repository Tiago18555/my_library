package com.TiagoSoftware.MyLibrary.Controllers;

import com.TiagoSoftware.MyLibrary.Models.DTO.ConfigurationDTO;
import com.TiagoSoftware.MyLibrary.Services.ConfigurationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/configuration")
public class ConfigurationController {

    @Autowired
    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostMapping()
    @ApiOperation(value = "Adiciona uma nova configuração")
    public ResponseEntity AddNewConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        var response = configurationService.ChangeConfiguration(configurationDTO);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Retorna a configuração com o id fornecido")
    public ResponseEntity GetConfigurationById(@PathVariable UUID id) {
        var response = configurationService.GetConfigurationById(id);

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping("latest")
    @ApiOperation(value = "Retorna a ultima configuração salva")
    public ResponseEntity LoadNewConfiguration() {
        var response = configurationService.LoadNewConfiguration();

        return new ResponseEntity<>(response, response.getHttpstatus());
    }

    @GetMapping()
    @ApiOperation(value = "Retorna o histórico de configurações")
    public ResponseEntity LoadAllConfigurations() {
        var response = configurationService.LoadAllConfigurations();

        return new ResponseEntity<>(response, response.getHttpstatus());
    }
}
