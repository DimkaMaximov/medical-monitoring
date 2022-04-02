package liga.medical.medicalmonitoring.core.listener;

import liga.medical.medicalmonitoring.core.annotations.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqAmbulanceListener {

    /** тестовый Listener имитирующий уведомления скорой помощи */

    Logger log = LoggerFactory.getLogger(RabbitMqListener.class);

    @Loggable
    @RabbitListener(queues = "ambulance-alert")
    public void processAppQueue(String message) {
        log.info("Уведомление скорой помощи: НЕОБХОДИМА ПОМОЩЬ " + message);
    }
}