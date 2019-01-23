package com.ps.bitcoin.integration;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class CoindeskWebClient {


/*
     https://api.coindesk.com/v1/bpi/historical/close.json?start=<VALUE>&end=<VALUE>
 */
    public Mono<ClientResponse> getHistoricalPrice(Object pStartDate, Object pEndDate) {


        SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String startDateStr = null;
        String endDateStr = null;
        Date startDate = null;
        Date endDate  = null;

        try {
            if (!((LinkedList<String>) pStartDate).get(0).isEmpty()) {
                startDate = originalFormat.parse(((LinkedList<String>) pStartDate).get(0));
                startDateStr = targetFormat.format(startDate);
            }
            else startDateStr = "2019-01-01";
            if (!((LinkedList<String>) pEndDate).get(0).isEmpty()) {
                endDate = originalFormat.parse(((LinkedList<String>) pEndDate).get(0));
                endDateStr = targetFormat.format(endDate);
            }
            else endDateStr = "2019-01-30";
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        WebClient coindeskClient = WebClient.create("https://api.coindesk.com");
        StringBuilder uri = new StringBuilder("/v1/bpi/historical/close.json?");
        uri.append("start=").append(startDateStr).append("&end=").append(endDateStr);

        return coindeskClient.get().uri(uri.toString()).accept(MediaType.TEXT_PLAIN).exchange();

    }


}
