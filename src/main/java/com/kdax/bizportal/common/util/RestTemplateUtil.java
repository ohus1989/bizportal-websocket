package com.kdax.bizportal.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
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

@Slf4j
@UtilityClass
public class RestTemplateUtil {

    private static final String STATUS_CODE = "statusCode";
    private static final String HEADER = "header";
    private static final String BODY = "body";

    /**
     * restTemplate exchange
     *
     * @param httpMethod
     * @param url
     * @param jsonMessage
     * @return HashMap<String, Object> result
     */
    public HashMap<String, Object> restTemplateExchange(HttpMethod httpMethod, String url, String jsonMessage, HttpHeaders header) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        log.info("restTemplateExchange url::{},jsonMessage::{}",url,jsonMessage);
        try {
            RestTemplate restTemplate = createRestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            //HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(jsonMessage.toString(), header);
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).build();

            ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), httpMethod, entity, Map.class);

            result.put(STATUS_CODE, resultMap.getStatusCodeValue());
            result.put(HEADER, resultMap.getHeaders());
            result.put(BODY, resultMap.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put(STATUS_CODE, e.getRawStatusCode());
            result.put(BODY, e.getStatusText());
        } catch (Exception e) {
            result.put(STATUS_CODE, "999");
            result.put(BODY, e.getMessage());
        }

        return result;

    }

    public HashMap<String, Object> restTemplateExchange(HttpMethod httpMethod, String url, String jsonMessage) {
        HttpHeaders header = new HttpHeaders();
        return restTemplateExchange(httpMethod,url,jsonMessage,header);
    }

    /**
     * restTemplate exchange
     * httpMethod get
     *
     * @param httpMethod
     * @param url
     * @param bodyParam
     * @param header
     * @return HashMap<String, Object> result
     */
    public HashMap<String, Object> restTemplateExchange(HttpMethod httpMethod, String url, Object bodyParam, HttpHeaders header) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        log.info("bodyParam {}",bodyParam);
        try {
            RestTemplate restTemplate = createRestTemplate();
            ResponseEntity<Map> resultMap = null;
            HttpEntity<String> entity;
            UriComponents uri;
            switch (httpMethod){
                case GET:
                    uri = UriComponentsBuilder.fromHttpUrl(url)
                            .queryParams((MultiValueMap<String, String>) bodyParam)
                            .build()
                            .encode(StandardCharsets.UTF_8);

                    entity = new HttpEntity<String>(header);
                    resultMap = restTemplate.exchange(uri.toUri(), httpMethod, entity, Map.class);
                    break;
                default:
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.disableHtmlEscaping();
                    Gson gson = gsonBuilder.create();

                    String jsonMessage = gson.toJson(bodyParam);

                    entity = new HttpEntity<String>(jsonMessage.toString(), header);
                    uri = UriComponentsBuilder.fromHttpUrl(url).build();

                    resultMap = restTemplate.exchange(uri.toString(), httpMethod, entity, Map.class);

                    break;
            }

            if(resultMap !=null){
                result.put(STATUS_CODE, resultMap.getStatusCodeValue());
                result.put(HEADER, resultMap.getHeaders());
                result.put(BODY, resultMap.getBody());
            }else{
                throw new Exception();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put(STATUS_CODE, e.getRawStatusCode());
            result.put(BODY, e.getStatusText());
        } catch (Exception e) {
            result.put(STATUS_CODE, "999");
            result.put(BODY, e.getMessage());
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

//        // Streams the response instead of loading it all in memory
//        ResponseExtractor<Void> responseExtractor = response -> {
//            // Here I write the response to a file but do what you like
//            String filename = response.getHeaders().getContentDisposition().getFilename();
//            Path path = Paths.get(destPath);
//            Files.copy(response.getBody(), path);
//            return null;
//        };

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

    public static ClientHttpResponse downloadGet(String url) {

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

        ClientHttpResponse clientHttpResponse = restTemplate.execute(URI.create(url), HttpMethod.GET, requestCallback, response -> {
            return response;
        });

        return clientHttpResponse;

    }

    public HashMap<String, Object> doPOST(String url, Map<String, String> parameters, HttpHeaders headers) {
        log.info("doPost url{}\nheaders{}\nparameters{}\n", url, headers, parameters);

        HashMap<String, Object> result = new HashMap<String, Object>();
        try {
            RestTemplate restTemplate = createRestTemplate();
            ResponseEntity<Map> resultMap = null;

            HttpEntity formEntity = new HttpEntity(parameters, headers);

            resultMap = restTemplate.exchange(url, HttpMethod.POST, formEntity, Map.class);

            log.info("doPOST resultMap\n{}", resultMap);
            if (resultMap != null) {
                result.put(STATUS_CODE, resultMap.getStatusCodeValue());
                result.put(HEADER, resultMap.getHeaders());
                result.put(BODY, resultMap.getBody());
            } else {
                throw new Exception();
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put(STATUS_CODE, e.getRawStatusCode());
            result.put(BODY, e.getStatusText());
        } catch (Exception e) {
            result.put(STATUS_CODE, "999");
            result.put(BODY, e.getMessage());
        }
        return result;
    }

}
