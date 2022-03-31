package liga.medical.medicalmonitoring.core.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liga.medical.DeviceIdentificationDto;
import liga.medical.medicalmonitoring.core.annotations.Loggable;
import liga.medical.medicalmonitoring.core.api.MedicalAnalyzerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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

        try {
            DeviceIdentificationDto deviceInfo = objectMapper.readValue(message, DeviceIdentificationDto.class);
            medicalAnalyzerService.analyze(deviceInfo);
            log.info("Получено сообщение о необходимости оказания первой помощи: {}", message);
        } catch (JsonProcessingException e) {
            log.info("Error while parsing incoming message {}", e.getMessage());
        }

        // TODO: передавать в сервис логирования все поступающие показания

        log.info("Получены данные пациента " + message);
    }

    // тестовый метод имитирующие уведомления скорой помощи
    @RabbitListener(queues = "ambulance-alert")
    public void processAmbulanceQueue(String message) {
        log.info("Уведомление скорой помощи: НЕОБХОДИМА ПОМОЩЬ " + message);
    }
}