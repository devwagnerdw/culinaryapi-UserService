package com.culinaryapi.culinaryapi_user_Service.publishers;

import com.culinaryapi.culinaryapi_user_Service.dtos.UserServiceEventDto;
import com.culinaryapi.culinaryapi_user_Service.enums.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {


    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Value(value="${Culinary.broker.exchange.userServiceEventExchange}" )
    private String exchangeUserServiceEvent;

    public void publishUserEvent(UserServiceEventDto userServiceEventDto, ActionType actionType){
        userServiceEventDto.setActionType(actionType.toString());
        rabbitTemplate.convertAndSend(exchangeUserServiceEvent,"user.service.event",userServiceEventDto);
    }
}
