package com.kw.objectrewriter.reader;

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RestApiConsumerTest {

    @Test
    void shouldRetrieveResponseBody() throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);
        RestApiConsumer<?> restApiConsumer = new RestApiConsumer<>(httpClient, mock(InputStreamBodySubscriberProvider.class));
        HttpRequest httpRequest = mock(HttpRequest.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        Optional<?> expected = Optional.of(new Object());
        when(httpResponse.body()).thenReturn(expected);
        when(httpClient.send(eq(httpRequest), any())).thenReturn(httpResponse);

        Optional<?> result = restApiConsumer.retrieveResponseBody(httpRequest);

        verify(httpClient, times(1)).send(eq(httpRequest), any());
        assertThat(result).isEqualTo(expected);
    }

}