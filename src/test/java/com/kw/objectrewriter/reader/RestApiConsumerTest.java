package com.kw.objectrewriter.reader;

import com.kw.objectrewriter.LogMessageVerifier;
import org.testng.annotations.DataProvider;
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

    @DataProvider
    private Object[] exceptionProvider() {
        return new Object[][]{
                {IOException.class, "Could not read response due to following error: null"},
                {InterruptedException.class, "Request failed due to thread interruption: null"},
        };
    }

    @Test(dataProvider = "exceptionProvider")
    void shouldReturnEmptyOptionalWhenErrorOccured(Class<? extends Throwable> exception, String message) throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpRequest httpRequest = mock(HttpRequest.class);
        when(httpClient.send(eq(httpRequest), any())).thenThrow(exception);
        RestApiConsumer<?> restApiConsumer = new RestApiConsumer<>(httpClient, mock(InputStreamBodySubscriberProvider.class));

        try (LogMessageVerifier verifier = new LogMessageVerifier(RestApiConsumer.class)) {
            Optional<?> result = restApiConsumer.retrieveResponseBody(httpRequest);

            assertThat(result).isEmpty();
            verifier.assertErrorMessageInLogs(message);
        }
    }

}