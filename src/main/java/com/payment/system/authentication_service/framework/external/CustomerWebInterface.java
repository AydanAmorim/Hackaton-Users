package com.payment.system.authentication_service.framework.external;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("customer-service")
interface CustomerWebInterface {

    @GetMapping(value = "/cliente/{id}")
    JsonNode findById(@PathVariable Integer id);
}
