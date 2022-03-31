package liga.medical.medicalmonitoring.core.api;

import liga.medical.DeviceIdentificationDto;

public interface MedicalAnalyzerService {

    void analyze(DeviceIdentificationDto deviceInfo);
}
