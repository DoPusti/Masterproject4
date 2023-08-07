package com.example.Masterproject4.ProduktAnforderung;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest
class XMLStructureFullObjectTest {

    @Test
    public void restCall() {
        /*
        try {

            URL url = new URL("http://localhost:8070/getPayload");//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }

         */

    }
    @Test
    public void getAllTypes()  {
        /*
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> call= restTemplate.getForEntity("http://localhost:8070/getPayload?AssetId=123",String.class);
        System.out.println(call.getBody());

         */

    }


}