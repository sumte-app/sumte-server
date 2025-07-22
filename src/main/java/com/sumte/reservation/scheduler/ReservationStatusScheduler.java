package com.sumte.reservation.scheduler;

import com.sumte.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStatusScheduler {
    private final ReservationService reservationService;

    @Scheduled(cron = "0 0 * * * *")
    public void autoUpdateCompletedReservations() {
        reservationService.updateCompletedReservations();
    }
}

