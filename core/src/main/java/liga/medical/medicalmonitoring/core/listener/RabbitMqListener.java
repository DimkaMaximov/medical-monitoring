package liga.medical.medicalmonitoring.core.listener;

import api.PatientServiceFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import liga.medical.DeviceIdentificationDto;
import liga.medical.medicalmonitoring.core.annotations.Loggable;
import liga.medical.medicalmonitoring.core.api.MedicalAnalyzerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {

     /**
     * in common-module in PatientServiceFeignClient remake method to this kind,
     * like it is in person-service module:
     *
     *     @GetMapping("/patient/{id}")
     *     Object getPatient(@PathVariable ("id") Long id);
     */

    Logger log = LoggerFactory.getLogger(RabbitMqListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicalAnalyzerService medicalAnalyzerService;

    @Loggable
    @RabbitListener(queues = "patient-alert")
    public void processAppQueue(String message) {

        PatientServiceFeignClient feignClient =
                Feign.builder()
                        .contract(new SpringMvcContract())
                        .encoder(new JacksonEncoder())
                        .decoder(new JacksonDecoder())
                        .target(PatientServiceFeignClient.class, "http://localhost:8021");

        try {
            DeviceIdentificationDto deviceInfo = objectMapper.readValue(message, DeviceIdentificationDto.class);
            Object patient = feignClient.getPatient(deviceInfo.getOwnerId());
            log.info("Получено сообщение о необходимости оказания первой помощи: {}", message);
            log.info("Информация передана скорой помощи. Данные пациента: {}. Показатели: {}.", patient.toString(), message);
            medicalAnalyzerService.analyze(deviceInfo, patient);
        } catch (JsonProcessingException e) {
            log.info("Error while parsing incoming message {}", e.getMessage());
        }

        // передавать в сервис логирования все поступающие показания
    }
}