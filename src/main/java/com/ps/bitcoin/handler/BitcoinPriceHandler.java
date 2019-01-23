package com.ps.bitcoin.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.bitcoin.integration.CoindeskWebClient;
import com.ps.bitcoin.vo.BitcoinRequestVO;
import com.ps.bitcoin.vo.BitcoinResponseVO;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BitcoinPriceHandler {


    public Mono<ServerResponse> getPriceForDate(ServerRequest request) {


        Mono<MultiValueMap<String, String>> data = request.formData();
        Map map =
                (Map) data.map(input -> {
                    Map dataMap = (Map) input;
                    return dataMap;

                }).block();


        CoindeskWebClient client = new CoindeskWebClient();
        Mono<ClientResponse> apiResponse = client.getHistoricalPrice(map.get("startDate"),map.get("endDate"));

        String response = apiResponse.flatMap(res -> res.bodyToMono(String.class)).block();

        Map<String, Double> priceMap = getPriceMap(response);
        double maxVal = Collections.max(priceMap.values());

        BitcoinResponseVO responseVO = new BitcoinResponseVO();
        responseVO.setPriceMap(priceMap);
        responseVO.setMaxValue(maxVal);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(responseVO));


    }

    public Map<String, Double> getPriceMap(String response) {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(response);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        JsonNode bpi = actualObj.get("bpi");

        Map<String, Double> pMap = mapper.convertValue(bpi, Map.class);
        Map<String, Double> priceMap = new LinkedHashMap<String, Double>();

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

       pMap.forEach((k,v)-> {

           try {
               Date date = originalFormat.parse(k);
               priceMap.put(targetFormat.format(date),v);

           } catch (ParseException e) {
               System.out.println(e.getMessage());
           }

       });
        return priceMap;


    }
}
