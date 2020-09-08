package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RestTemplateUtil {

    /**
     * restTemplate exchange
     *
     * @param httpMethod
     * @param url
     * @param jsonMessage
     * @return HashMap<String, Object> result
     */
    public HashMap<String, Object> restTemplateExchange(HttpMethod httpMethod, String url, String jsonMessage) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(jsonMessage.toString(), header);
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), httpMethod, entity, Map.class);

            result.put("statusCode", resultMap.getStatusCodeValue());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body", e.getMessage());
        }

        return result;

    }

    /**
     * restTemplate exchange
     *
     * @param httpMethod
     * @param url
     * @param jsonMessage
     * @param header
     * @return HashMap<String, Object> result
     */
    public HashMap<String, Object> restTemplateExchange(HttpMethod httpMethod, String url, String jsonMessage, HttpHeaders header) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpEntity<String> entity = new HttpEntity<String>(jsonMessage.toString(), header);
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), httpMethod, entity, Map.class);

            result.put("statusCode", resultMap.getStatusCodeValue());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body", e.getMessage());
        }

        return result;

    }
}
