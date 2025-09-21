package com.ivoovi.meter.controller;

import com.ivoovi.meter.dto.SubscriptionDto;
import com.ivoovi.meter.service.SubscriptionService;
import com.ivoovi.meter.utility.JsonFilterUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SubscriptionControllerTest {

    private static final String ICAO_KJFK = "KJFK";

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private JsonFilterUtil jsonFilterUtil;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // create

    @Test
    void createSubscription_returns201_andBody() throws Exception {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIcaoCode(ICAO_KJFK);
        when(subscriptionService.createSubscription(ICAO_KJFK)).thenReturn(dto);

        ResponseEntity<?> response = subscriptionController.createSubscription(ICAO_KJFK, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(dto);
        verify(jsonFilterUtil, never()).filterObject(any(), anyString());
    }

    @Test
    void createSubscription_appliesFieldsFilter_whenProvided() throws Exception {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIcaoCode(ICAO_KJFK);
        when(subscriptionService.createSubscription(ICAO_KJFK)).thenReturn(dto);
        Map<String, Object> filtered = Map.of("icaoCode", ICAO_KJFK);
        when(jsonFilterUtil.filterObject(dto, "icaoCode")).thenReturn(filtered);

        ResponseEntity<?> response = subscriptionController.createSubscription(ICAO_KJFK, "icaoCode");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(filtered);
        verify(jsonFilterUtil).filterObject(dto, "icaoCode");
    }

    // getAll - single exact icaoCode

    @Test
    void getAll_withExactIcao_returnsDto() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIcaoCode(ICAO_KJFK);
        when(subscriptionService.getSubscription(ICAO_KJFK)).thenReturn(dto);

        ResponseEntity<?> response = subscriptionController.getAllSubscriptions(ICAO_KJFK, null, null, null, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getAll_withExactIcao_andActiveMismatch_returnsNullBody() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIcaoCode(ICAO_KJFK);
        dto.setActive(false);
        when(subscriptionService.getSubscription(ICAO_KJFK)).thenReturn(dto);

        ResponseEntity<?> response = subscriptionController.getAllSubscriptions(ICAO_KJFK, null, true, null, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getAll_withExactIcao_appliesFieldsFilter() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIcaoCode(ICAO_KJFK);
        when(subscriptionService.getSubscription(ICAO_KJFK)).thenReturn(dto);
        Map<String, Object> filtered = Map.of("icaoCode", ICAO_KJFK);
        when(jsonFilterUtil.filterObject(dto, "icaoCode")).thenReturn(filtered);

        ResponseEntity<?> response = subscriptionController.getAllSubscriptions(ICAO_KJFK, null, null, "icaoCode", pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(filtered);
    }

    // getAll - like

    @Test
    void getAll_withIcaoLike_returnsPagedResponse() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        Page<SubscriptionDto> page = new PageImpl<>(
                List.of(new SubscriptionDto(null, true, null), new SubscriptionDto(null, false, null)),
                pageable,
                4
        );
        when(subscriptionService.getSubscriptionsByIcaoCodeLike("K", pageable, null)).thenReturn(page);

        ResponseEntity<?> response = subscriptionController.getAllSubscriptions(null, "K", null, null, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getAll_withIcaoLike_appliesFieldsFilterForEachItem() throws Exception {
        Pageable pageable = PageRequest.of(0, 1);
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIcaoCode(ICAO_KJFK);
        Page<SubscriptionDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(subscriptionService.getSubscriptionsByIcaoCodeLike("K", pageable, true)).thenReturn(page);
        Map<String, Object> filtered = Map.of("icaoCode", ICAO_KJFK);
        when(jsonFilterUtil.filterObject(dto, "icaoCode")).thenReturn(filtered);

        ResponseEntity<?> response = subscriptionController.getAllSubscriptions(null, "K", true, "icaoCode", pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(jsonFilterUtil).filterObject(dto, "icaoCode");
    }

    // getAll - all subscriptions

    @Test
    void getAll_withoutFilters_returnsPagedResponse() throws Exception {
        Pageable pageable = PageRequest.of(0, 1);
        Page<SubscriptionDto> page = new PageImpl<>(List.of(new SubscriptionDto()), pageable, 1);
        when(subscriptionService.getAllSubscriptions(pageable, null)).thenReturn(page);

        ResponseEntity<?> response = subscriptionController.getAllSubscriptions(null, null, null, null, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getAll_withoutFilters_appliesFieldsFilter() throws Exception {
        Pageable pageable = PageRequest.of(0, 1);
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIcaoCode(ICAO_KJFK);
        Page<SubscriptionDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(subscriptionService.getAllSubscriptions(pageable, false)).thenReturn(page);
        Map<String, Object> filtered = Map.of("icaoCode", ICAO_KJFK);
        when(jsonFilterUtil.filterObject(dto, "icaoCode")).thenReturn(filtered);

        ResponseEntity<?> response = subscriptionController.getAllSubscriptions(null, null, false, "icaoCode", pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(jsonFilterUtil).filterObject(dto, "icaoCode");
    }

    // update

    @Test
    void updateSubscriptionStatus_subscribes_whenActiveTrue() {
        subscriptionController.updateSubscriptionStatus(ICAO_KJFK, new SubscriptionDto(ICAO_KJFK, true, null));
        verify(subscriptionService).subscribe(ICAO_KJFK, true);
        verify(subscriptionService, never()).unsubscribe(anyString(), anyBoolean());
    }

    @Test
    void updateSubscriptionStatus_unsubscribes_whenActiveFalse() {
        subscriptionController.updateSubscriptionStatus(ICAO_KJFK, new SubscriptionDto(ICAO_KJFK, false, null));
        verify(subscriptionService).unsubscribe(ICAO_KJFK, false);
        verify(subscriptionService, never()).subscribe(anyString(), anyBoolean());
    }

    // delete

    @Test
    void deleteSubscription_returnsServiceResult() {
        when(subscriptionService.deleteSubscription(ICAO_KJFK)).thenReturn(true);

        ResponseEntity<Boolean> response = subscriptionController.deleteSubscription(ICAO_KJFK);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isTrue();
    }
}