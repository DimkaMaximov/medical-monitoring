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

    Logger log = LoggerFactory.getLogger(RabbitMqListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicalAnalyzerService medicalAnalyzerService;

    @Loggable
    @RabbitListener(queues = "patient-alert")
    public void processAppQueue(String message) {

        // реализовать сохранение в БД случаев на которые должны реагировать
        // отслеживать скорую, которая освобождается, и передавать информацию на вызов

        // создать очередь для скорой и передавать ей показатели и инфу пользователя
        //передавать данные пациента и медкарты
        //из person servise get person data и address по ownerid
        //String [] ownerId = message.split("\\D+");
        PatientServiceFeignClient feignClient =
                Feign.builder()
                        .contract(new SpringMvcContract())
                        .encoder(new JacksonEncoder())
                        .decoder(new JacksonDecoder())
                        .target(PatientServiceFeignClient.class, "http://localhost:8021");

        try {
            DeviceIdentificationDto deviceInfo = objectMapper.readValue(message, DeviceIdentificationDto.class);
            Object list = feignClient.getPatient(deviceInfo.getOwnerId());
            log.info("Получено сообщение о необходимости оказания первой помощи: {}", message);
            log.info("Получены данные пациента: {}", list.toString());
            log.info("Информация передана скорой помощи");
            medicalAnalyzerService.analyze(deviceInfo);
        } catch (JsonProcessingException e) {
            log.info("Error while parsing incoming message {}", e.getMessage());
        }

        // передавать в сервис логирования все поступающие показания

        log.info("Получены данные пациента " + message);
    }
}