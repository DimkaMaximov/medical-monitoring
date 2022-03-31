package liga.medical.medicalmonitoring.core.service;

import liga.medical.DeviceIdentificationDto;
import liga.medical.medicalmonitoring.core.api.MedicalAnalyzerService;
import liga.medical.medicalmonitoring.core.constants.ApplicationConstants;
import liga.medical.medicalmonitoring.core.utils.ModelConverter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmbulanceAnalyzerServiceImpl implements MedicalAnalyzerService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void analyze(DeviceIdentificationDto deviceInfo) {
        if (deviceInfo == null) return;
        String message = ModelConverter.convertToMessage(deviceInfo);
        amqpTemplate.convertAndSend(ApplicationConstants.AMBULANCE_ALERT.getMedicalQueue(), message);
    }
}