package com.payment.system.authentication_service.unit.framework.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.payment.system.authentication_service.framework.external.CustomerWeb;
import com.payment.system.authentication_service.framework.external.CustomerWebInterface;
import com.payment.system.authentication_service.util.MessageUtil;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import com.payment.system.authentication_service.util.exceptions.ExternalInterfaceException;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerWebTest {

    @Mock
    private CustomerWebInterface customerWebInterface;

    @InjectMocks
    private CustomerWeb customerWeb;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findClientByIdSuccessfulResponseReturnsJsonNode() throws BusinessException, ExternalInterfaceException {
        Integer clientId = 1;

        JsonNode expectedResponse = mock(JsonNode.class);
        when(customerWebInterface.findById(clientId))
                .thenReturn(expectedResponse);

        JsonNode result = customerWeb.findClientById(clientId);

        assertEquals(expectedResponse, result);
        verify(customerWebInterface, times(1))
                .findById(clientId);
    }

    @Test
    void findClientByIdClientNotFoundThrowsBusinessException() {
        Integer clientId = 1;
        FeignException feignException = FeignException.errorStatus(
                "GET",
                feign.Response.builder()
                        .request(Request.create(Request.HttpMethod.GET, "/client/1", new HashMap<>(), null, StandardCharsets.UTF_8, null))
                        .status(404)
                        .build()
        );

        when(customerWebInterface.findById(clientId)).thenThrow(feignException);

        BusinessException exception = assertThrows(BusinessException.class, () -> customerWeb.findClientById(clientId));
        assertEquals(MessageUtil.getMessage("CLIENT_NOT_FOUND"), exception.getMessage());
        verify(customerWebInterface, times(1)).findById(clientId);
    }

    @Test
    void findClientByIdOtherFeignExceptionThrowsExternalInterfaceException() {
        Integer clientId = 1;
        FeignException feignException = FeignException.errorStatus(
                "GET",
                feign.Response.builder()
                        .request(Request.create(Request.HttpMethod.GET, "/client/1", new HashMap<>(), null, StandardCharsets.UTF_8, null))
                        .status(500)
                        .build()
        );

        when(customerWebInterface.findById(clientId)).thenThrow(feignException);

        ExternalInterfaceException exception = assertThrows(
                ExternalInterfaceException.class,
                () -> customerWeb.findClientById(clientId)
        );

        assertEquals(MessageUtil.getMessage("ERROR_OCCUR_DURING_ACCESS_CUSTOMER_SERVICE"), exception.getMessage());
        verify(customerWebInterface, times(1)).findById(clientId);
    }

    @Test
    void findClientByIdUnknownExceptionThrowsExternalInterfaceException() {
        Integer clientId = 1;
        when(customerWebInterface.findById(clientId)).thenThrow(new RuntimeException("Unexpected error"));

        ExternalInterfaceException exception = assertThrows(ExternalInterfaceException.class, () -> customerWeb.findClientById(clientId));
        assertEquals(MessageUtil.getMessage("UNKNOWN_ERROR_ON_ACCESS_CUSTOMER_SERVICE"), exception.getMessage());
        verify(customerWebInterface, times(1)).findById(clientId);
    }
}