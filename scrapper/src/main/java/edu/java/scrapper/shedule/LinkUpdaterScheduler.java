package edu.java.scrapper.shedule;

import edu.java.scrapper.api.service.LinkUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "app.scheduler.enable", matchIfMissing = true)
@Slf4j
public class LinkUpdaterScheduler {
    private final LinkUpdater updater;

    @Autowired
    public LinkUpdaterScheduler(LinkUpdater updater) {
        this.updater = updater;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        updater.update();
    }
}
