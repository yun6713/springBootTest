package com.bonc.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.bonc.controller.WebSocketController.ChatMessage;

/**
 * 基于STOMP协议的websocket服务
 * @author litianlin
 * @date   2019年8月12日上午11:32:15
 * @Description TODO
 */
@Configuration
@EnableWebSocketMessageBroker//开启基于中介者的消息服务
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	//注册服务
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")//注册服务点
        .withSockJS();//用来为不支持websocket的浏览器启用后备选项，使用SockJS
    }
    //配置消息中介
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //消息中介内部路由策略
    	//路由到消息处理方法，@Messaging标记的方法；确定路由对象后(方法、消息中介)移除对应前缀
    	registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
        //使用rabbitmq作为消息中介
//        registry.enableStompBrokerRelay("/topic")
//	        .setRelayHost("localhost")
//	        .setRelayPort(5672)
//	        .setClientLogin("guest")
//	        .setClientPasscode("guest");//路由到消息代理        
    }
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
    /**
     * 三种类型事件：connect、subscribe、disconnect
     */
    @Autowired//消息操作，参考RabbitMQ RabbitTemplate
    private SimpMessageSendingOperations messagingTemplate;
    /**
     * 连接、断开事件监听；基于ApplicationEvent
     * @param event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}