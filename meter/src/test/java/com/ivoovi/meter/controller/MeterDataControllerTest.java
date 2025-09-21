package com.ivoovi.meter.controller;

import com.ivoovi.meter.scheduler.MeterDataScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class MeterDataControllerTest {

    private static final String SUCCESS_MESSAGE = "✅ METAR fetch job triggered!";

    @Mock
    private MeterDataScheduler meterDataScheduler;

    @InjectMocks
    private MeterDataController meterDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldTriggerMetarFetchJob() {
        String result = whenFetchIsTriggered();
        thenSchedulerJobIsInvoked();
        thenSuccessMessageIsReturned(result);
    }

    private String whenFetchIsTriggered() {
        return meterDataController.fetchAndPersistMeterData();
    }

    private void thenSchedulerJobIsInvoked() {
        verify(meterDataScheduler).fetchAndPersistMetar();
    }

    private void thenSuccessMessageIsReturned(String result) {
        assertThat(result).isEqualTo(SUCCESS_MESSAGE);
    }
}