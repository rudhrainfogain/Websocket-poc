package com.example.demo;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
public class WebSocketClientConnection {

	private StompSession stompSession;

	public void connect() throws InterruptedException, ExecutionException {
		WebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());
		stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
		StompSessionHandler sessionHandler = new StompSessionHandler() {

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				System.out.println("Websocket client data received:");
				System.out.println(payload);
			}

			@Override
			public Type getPayloadType(StompHeaders headers) {
				return String.class;
			}

			@Override
			public void handleTransportError(StompSession session, Throwable exception) {

			}

			@Override
			public void handleException(StompSession session, StompCommand command, StompHeaders headers,
					byte[] payload, Throwable exception) {
			}

			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				session.subscribe("/queue/output", this);
				System.out.println(session.isConnected());
			}
		};

		stompSession = stompClient.connect("ws://localhost:8080/poc", sessionHandler).get();
	}
}
