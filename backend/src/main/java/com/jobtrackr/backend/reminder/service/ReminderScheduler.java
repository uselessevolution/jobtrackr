package com.jobtrackr.backend.reminder.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "jobtrackr.reminder.scheduler.enabled",
        havingValue = "true"
)
public class ReminderScheduler {

    private final ReminderProcessingService processingService;

    private final int batchSize;

    public ReminderScheduler(
            ReminderProcessingService processingService,
            org.springframework.core.env.Environment environment) {

        this.processingService =
                processingService;

        this.batchSize =
                Integer.parseInt(
                        environment.getProperty(
                                "jobtrackr.reminder.scheduler.batch-size",
                                "100"));
    }

    @Scheduled(
            fixedDelayString =
                    "${jobtrackr.reminder.scheduler.fixed-delay-ms:60000}"
    )
    public void processDueReminders() {

        processingService.processDueReminders(
                batchSize);
    }
}