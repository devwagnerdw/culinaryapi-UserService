package com.culinaryapi.culinaryapi_user_Service.consumers;


import com.culinaryapi.culinaryapi_user_Service.dtos.UserEventDto;
import com.culinaryapi.culinaryapi_user_Service.enums.ActionType;
import com.culinaryapi.culinaryapi_user_Service.services.UserService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    private final UserService userService;

    public UserConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${Culinary.broker.queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${Culinary.broker.exchange.userEventExchange}", type = ExchangeTypes.DIRECT, ignoreDeclarationExceptions = "true"),
            key = "user.service.event") // Routing key
    )
    public void listenUserEvent(@Payload UserEventDto userEventDto) {
        try {
            var userModel = userEventDto.convertToUserModel();

            switch (ActionType.valueOf(userEventDto.getActionType())) {
                case CREATE:
                case UPDATE:
                    userService.save(userModel);
                    break;
                case DELETE:
                    userService.delete(userEventDto.getUserId());
                    break;
            }
        } catch (Exception e) {
            // Logar o erro ou tratar de outra forma
            System.err.println("Erro ao processar evento: " + e.getMessage());
        }
    }
}