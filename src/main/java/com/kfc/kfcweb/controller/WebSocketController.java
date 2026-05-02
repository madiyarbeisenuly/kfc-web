package com.kfc.kfcweb.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * WebSocket контроллер (критерий: Socket programming)
 */
@Controller
public class WebSocketController {

    @MessageMapping("/order")
    @SendTo("/topic/status")
    public String sendStatus(String message) {
        return message;
    }
}
