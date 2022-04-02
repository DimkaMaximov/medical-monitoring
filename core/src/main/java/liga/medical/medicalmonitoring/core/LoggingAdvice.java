package liga.medical.medicalmonitoring.core;

import liga.medical.medicalmonitoring.core.model.LogEntity;
import liga.medical.medicalmonitoring.core.repository.LogEntityRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAdvice {

    @Autowired
    LogEntityRepository repository;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(liga.medical.medicalmonitoring.core.annotations.Loggable)")
    public void loggableMethod() {
    }

    @Around("loggableMethod()")
    public Object processAppQueueLogging(ProceedingJoinPoint pjp) {

        LocalDateTime eventTime = LocalDateTime.now();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();

        LogEntity logEntity = LogEntity.builder()
                .eventTime(eventTime)
                .methodName(methodName)
                .className(className)
                .build();

        LogEntity newEntity = repository.save(logEntity);
        log.info("Зафиксирован случай ухудшения состояния: Log#{} {} {}:{}()", newEntity.getId(), eventTime, className, methodName);

        Object object = null;
        try {
            object = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return object;
    }
}