package com.payment.system.authentication_service.integration.framework.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.payment.system.authentication_service.TestUtils;
import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.framework.db.UserRepository;
import com.payment.system.authentication_service.framework.external.CustomerWeb;
import com.payment.system.authentication_service.interfaceadapters.presenters.dto.UserDto;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import com.payment.system.authentication_service.util.pagination.PagedResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@EnableWebMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserWebTest extends TestUtils {


    private static final String MOCK_PATH = "src/test/java/com/payment/system/authentication_service/integration/framework/web/mocks/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private CustomerWeb customerWeb;

    private static final String PATH_ENDPOINT = "/usuarios";

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ADMIN")
    void findAll() throws Exception {
        List<User> users = super.objectMapper.readValue(super.getMock(MOCK_PATH + "clients-to-insert.json"), new TypeReference<List<User>>() {
        });

        userRepository.saveAll(users);

        MockHttpServletResponse response = mockMvc.perform(
                        get(PATH_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageSize", "10")
                                .param("initialPage", "0")
                ).andExpect(status().isOk())
                .andReturn().getResponse();

        PagedResponse<UserDto> content = objectMapper.readValue(
                response.getContentAsString(StandardCharsets.UTF_8),
                new TypeReference<PagedResponse<UserDto>>() {
                }
        );

        PagedResponse<UserDto> expected = objectMapper.readValue(
                super.getMock(MOCK_PATH + "find-all.json"),
                new TypeReference<PagedResponse<UserDto>>() {
                }
        );

        assertEquals(expected.getPage().getPage(), content.getPage().getPage());
        assertEquals(expected.getPage().getPageSize(), content.getPage().getPageSize());
        assertEquals(expected.getPage().getTotalPages(), content.getPage().getTotalPages());
        assertEquals(expected.getData().size(), content.getData().size());
        super.assertJsonEquals(
                super.objectMapper.writeValueAsString(expected.getData()),
                super.objectMapper.writeValueAsString(content.getData())
        );
    }

    @DisplayName("Erro no scopo do usuário")
    @Test
    @WithMockUser(authorities = "SCOPE_BASIC")
    void findAllWithoutPermissions() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        get(PATH_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageSize", "10")
                                .param("initialPage", "0")
                ).andExpect(status().isForbidden())
                .andReturn().getResponse();

        JsonNode content = objectMapper.readTree(response.getContentAsString(StandardCharsets.UTF_8));

        assertEquals("Acesso Bloqueado", content.get("error").asText());
        assertEquals("Access Denied", content.get("message").asText());
    }

    @DisplayName("Inserir usuário básico")
    @Test
    @WithMockUser
    void addBasicUser() throws Exception {
        when(customerWeb.findClientById(any(Integer.class)))
                .thenReturn(super.objectMapper.createObjectNode());

        LoginDto dto = new LoginDto("admin", "strong");

        mockMvc.perform(
                post(PATH_ENDPOINT + "/basic/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(super.objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());
    }

    @DisplayName("Inserir usuário básico com cliente não cadastrado")
    @Test
    @WithMockUser
    void addBasicUserWithoutClient() throws Exception {
        when(customerWeb.findClientById(any(Integer.class)))
                .thenThrow(new BusinessException("CLIENT_NOT_FOUND"));

        LoginDto dto = new LoginDto("admin", "strong");

        MockHttpServletResponse response = mockMvc.perform(
                        post(PATH_ENDPOINT + "/basic/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(super.objectMapper.writeValueAsString(dto))
                ).andExpect(status().isForbidden())
                .andReturn().getResponse();

        JsonNode content = objectMapper.readTree(response.getContentAsString(StandardCharsets.UTF_8));

        assertEquals("Bloqueado por regra de negócio", content.get("error").asText());
        assertEquals("Cliente não encontrado! Verifique o cadastro do cliente!", content.get("message").asText());
    }

    @DisplayName("Inserir usuário admin")
    @Test
    @WithMockUser(authorities = "SCOPE_ADMIN")
    void addAdminUser() throws Exception {
        when(customerWeb.findClientById(any(Integer.class)))
                .thenReturn(super.objectMapper.createObjectNode());

        LoginDto dto = new LoginDto("admin", "strong");

        mockMvc.perform(
                post(PATH_ENDPOINT + "/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(super.objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());
    }
}