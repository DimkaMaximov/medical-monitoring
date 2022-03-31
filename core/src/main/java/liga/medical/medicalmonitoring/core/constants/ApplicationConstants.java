package liga.medical.medicalmonitoring.core.constants;

public enum ApplicationConstants {

    AMBULANCE_ALERT("ambulance-alert");

    private String medicalQueue;

    ApplicationConstants(String queueName) {
        this.medicalQueue = queueName;
    }

    public String getMedicalQueue() {
        return medicalQueue;
    }
}
