package liga.medical.medicalmonitoring.core;

import liga.medical.personservice.core.PersonServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EntityScan("liga.medical.medicalmonitoring.*")
//@EnableFeignClients(clients = PersonServiceApplication.class)
public class MedicalMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalMonitoringApplication.class, args);
    }
}
