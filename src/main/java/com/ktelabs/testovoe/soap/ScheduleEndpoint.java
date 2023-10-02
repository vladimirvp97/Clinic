package com.ktelabs.testovoe.soap;

import com.ktelabs.testovoe.service.TicketService;
import com.ktelabs.testovoe.soap.dto.GetScheduleRequest;
import com.ktelabs.testovoe.soap.dto.GetScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class ScheduleEndpoint {

    private final TicketService ticketService;

    private static final String NAMESPACE_URI = "http://www.ktelabs.com/testovoe/vladimirvp97";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getScheduleRequest")
    @ResponsePayload
    public GetScheduleResponse getSchedule(@RequestPayload GetScheduleRequest request) {
        ticketService.setSessionDuration(request.getAmountMinutesOnSession());
        ticketService.initTickets(request);
        GetScheduleResponse response = new GetScheduleResponse();
        response.setResponse("OK");
        return response;
    }
}
