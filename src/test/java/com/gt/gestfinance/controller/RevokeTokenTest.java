package com.gt.gestfinance.controller;

import com.gt.gestfinance.AbstractControllerTest;
import com.gt.gestfinance.exception.CustomException;
import com.gt.gestfinance.util.CustomMockMvc;
import com.gt.gestfinance.util.UrlConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RevokeTokenTest extends AbstractControllerTest {

    private ConsumerTokenServices consumerTokenServices;

    @Before
    public void setup() {
        consumerTokenServices = mock(ConsumerTokenServices.class);
        RevokeTokenEndpoint revokeTokenEndpoint = new RevokeTokenEndpoint();
        revokeTokenEndpoint.setTokenServices(consumerTokenServices);
        super.setup(revokeTokenEndpoint);
    }

    @Test
    public void testRevokeToken() throws CustomException {

        doReturn(Boolean.TRUE).when(consumerTokenServices)
                .revokeToken(anyString());

        CustomMockMvc.CustomResultActions customResultActions = restSampleMockMvc
                .perform(delete(UrlConstants.SLASH + "oauth/token")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(customResultActions).isNotNull();

        customResultActions = restSampleMockMvc.perform(delete(UrlConstants.SLASH + "oauth/token")
                .header("token", "xxxxx")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(customResultActions).isNotNull();
    }

}
