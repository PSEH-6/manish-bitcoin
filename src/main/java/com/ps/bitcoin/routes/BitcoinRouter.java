package com.ps.bitcoin.routes;

import com.ps.bitcoin.handler.BitcoinPriceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class BitcoinRouter {

    @Bean
    public RouterFunction<ServerResponse> route(BitcoinPriceHandler priceHandler) {

        return RouterFunctions.route(RequestPredicates.POST("/getPriceForDate").and(RequestPredicates.contentType(MediaType.ALL)),priceHandler::getPriceForDate);
    }


}
