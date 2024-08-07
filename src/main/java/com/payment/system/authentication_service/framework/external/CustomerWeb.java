package com.payment.system.authentication_service.framework.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.payment.system.authentication_service.util.MessageUtil;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import com.payment.system.authentication_service.util.exceptions.ExternalInterfaceException;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class CustomerWeb {

    private final CustomerWebInterface customerWebInterface;

    public CustomerWeb(CustomerWebInterface customerWebInterface) {
        this.customerWebInterface = customerWebInterface;
    }

    public JsonNode findClientById(Integer clientId) throws ExternalInterfaceException, BusinessException {
        try {
            return customerWebInterface.findById(clientId);
        } catch (FeignException exception) {
            int statusCode = exception.status();

            if (statusCode == 404) {
                throw new BusinessException("CLIENT_NOT_FOUND");
            }

            throw new ExternalInterfaceException(MessageUtil.getMessage("ERROR_OCCUR_DURING_ACCESS_CUSTOMER_SERVICE"));
        } catch (Exception exception) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("UNKNOWN_ERROR_ON_ACCESS_CUSTOMER_SERVICE"));
        }
    }
}
