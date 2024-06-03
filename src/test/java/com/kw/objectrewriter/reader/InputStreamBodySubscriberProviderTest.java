package com.kw.objectrewriter.reader;

import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InputStreamBodySubscriberProviderTest {

    @Test
    void shouldReturnBodySubscriberWithGivenMapper() {
        InputStreamMapper<?> inputStreamMapper = mock(InputStreamMapper.class);

        new InputStreamBodySubscriberProvider<>(inputStreamMapper).getBodySubscriber(null).getBody();

        verify(inputStreamMapper, times(1)).mapToObject(any());
    }

}