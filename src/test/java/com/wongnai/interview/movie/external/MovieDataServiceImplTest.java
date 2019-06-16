package com.wongnai.interview.movie.external;

import com.wongnai.interview.movie.exception.BadRequestAlertException;
import com.wongnai.interview.movie.exception.InternalServerErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class MovieDataServiceImplTest {

    @InjectMocks
    private MovieDataServiceImpl movieDataService;

    @Mock
    private RestOperations restTemplate;

    @Test
    public void testFetchAllShouldThrowInternalServerErrorWhenRestTemplateHttpError() {

        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
            .when(restTemplate).getForEntity(any(String.class), any());

        try {
            movieDataService.fetchAll();
        } catch (InternalServerErrorException ex) {
            assertEquals("httpClientErrorException", ex.getErrorKey());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testFetchAllShouldThrowInternalServerErrorWhenBadGateWay() {

        doThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY))
            .when(restTemplate).getForEntity(any(String.class), any());

        try {
            movieDataService.fetchAll();
        } catch (InternalServerErrorException ex) {
            assertEquals("httpClientErrorException", ex.getErrorKey());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }
    
    @Test
    public void testFetchAllShouldThrowBadRequestWhenErrorOccurredInMovieDatasource() {

        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST))
            .when(restTemplate).getForEntity(any(String.class), any());

        try {
            movieDataService.fetchAll();
        } catch (BadRequestAlertException ex) {
            assertEquals("datasourceError", ex.getErrorKey());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    public void testFetchAllShouldThrowDefaultInternalError() {

        doThrow(new RuntimeException())
            .when(restTemplate).getForEntity(any(String.class), any());

        try {
            movieDataService.fetchAll();
        } catch (InternalServerErrorException ex) {
            assertEquals("exception", ex.getErrorKey());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

}
