package com.kdax.bizportal.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
            RestTemplate restTemplate = createRestTemplate();

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
     * httpMethod get
     *
     * @param httpMethod
     * @param url
     * @param requestParamMap
     * @param header
     * @return HashMap<String, Object> result
     */
    public HashMap<String, Object> restTemplateExchange(HttpMethod httpMethod, String url, Map requestParamMap, HttpHeaders header) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
            RestTemplate restTemplate = createRestTemplate();

            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParams((MultiValueMap<String, String>) requestParamMap)
                    .build()
                    .encode(StandardCharsets.UTF_8);

            HttpEntity<String> entity = new HttpEntity<String>(header);
            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toUri(), httpMethod, entity, Map.class);

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

    private RestTemplate createRestTemplate(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }

    public static String downloadGet(String url, String destPath) {

        RestTemplate restTemplate = createRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Encoding", "gzip, deflate, sdch");

        // Optional Accept header
        RequestCallback requestCallback = request -> {
            request.getHeaders().addAll(headers);
            request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            formHttpMessageConverter.setCharset(Charset.forName("EUC-KR"));
        };

        // Streams the response instead of loading it all in memory
        ResponseExtractor<Void> responseExtractor = response -> {
            // Here I write the response to a file but do what you like
            String filename = response.getHeaders().getContentDisposition().getFilename();
            Path path = Paths.get(destPath);
            Files.copy(response.getBody(), path);
            return null;
        };

        String filename = restTemplate.execute(URI.create(url), HttpMethod.GET, requestCallback,  response -> {
            // Here I write the response to a file but do what you like
            String name = "";
            if (response.getHeaders().getContentDisposition().getFilename()!=null){
                name = response.getHeaders().getContentDisposition().getFilename();
            }
            Path path = Paths.get(destPath);
            Files.copy(response.getBody(), path);
            return name;
        });

        return filename;

    }

}
