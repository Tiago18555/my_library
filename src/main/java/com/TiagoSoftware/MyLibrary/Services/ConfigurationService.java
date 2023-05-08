package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.DTO.ConfigurationDTO;
import com.TiagoSoftware.MyLibrary.Models.Entity.Configuration;
import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import com.TiagoSoftware.MyLibrary.Repositories.ConfigurationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConfigurationService {

    @Autowired
    private final ConfigurationRepository dbset;

    public ConfigurationService(ConfigurationRepository dbset) {
        this.dbset = dbset;
    }

    @Transactional
    public ResponseModel ChangeConfiguration(ConfigurationDTO configurationDTO) {
        Configuration configuration = new Configuration();

        var foundConfig = dbset.findAll();

        if(foundConfig.isEmpty()) {
            BeanUtils.copyProperties(configurationDTO, configuration);
            if(
                    configurationDTO.getAssessment() == 0 ||
                    configurationDTO.getTolerance() == 0 ||
                    configurationDTO.getBorrowingLimit() == 0
            ) {
                return new ResponseModel("There's no previous config register, please put all 3 config properties on request", HttpStatus.OK);
            }
        } else {

            var latestConfig = foundConfig
                    .stream()
                    .skip(foundConfig.stream().count() - 1)
                    .limit(1)
                    .collect(Collectors.toList());

            configuration.setAssessment(
                    configurationDTO.getAssessment() == null
                            ? latestConfig.get(0).getAssessment()
                            : configurationDTO.getAssessment()
            );

            configuration.setTolerance(
                    configurationDTO.getTolerance() == null
                            ? latestConfig.get(0).getTolerance()
                            : configurationDTO.getTolerance()
            );

            configuration.setBorrowingLimit(
                    configurationDTO.getBorrowingLimit() == null
                            ? latestConfig.get(0).getBorrowingLimit()
                            : configurationDTO.getBorrowingLimit()
            );
        }



        try{
            configuration.setStartedAt(new Date(System.currentTimeMillis()));
            System.out.println(configuration);
            var data = dbset.save(configuration);
            return new ResponseModel(data, HttpStatus.OK);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @return a configuração mais recente salva
     * */
    public ResponseModel LoadNewConfiguration() {
        try {
            var foundConfig = dbset.findAll();
            var data = foundConfig.stream().skip(foundConfig.stream().count() - 1).limit(1);
            return new ResponseModel(
                    data,
                    HttpStatus.OK
            );
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public ResponseModel LoadAllConfigurations() {
        try {
            return new ResponseModel(dbset.findAll(), HttpStatus.OK);
        }
        catch(Exception ex) {
            throw ex;
        }
    }

    public ResponseModel GetConfigurationById(UUID id) {
        try {
            var foundConfig = dbset.findById(id);
            if (foundConfig.isEmpty()) {
                return new ResponseModel("Configuration not found!", HttpStatus.NOT_FOUND);
            }
            return new ResponseModel(foundConfig.get(), HttpStatus.OK);
        }
        catch (Exception ex) {
            throw ex;
        }
    }
}
