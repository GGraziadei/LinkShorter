package it.erroridiprezzo.ErroriDiPrezzoShort.services;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class CheckUrlService {

    public boolean checkUrl( String url) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet(url));
        int statusCode = response.getStatusLine().getStatusCode();
        //assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        System.out.println(statusCode);
        return statusCode != HttpStatus.SC_OK;
    }

}
