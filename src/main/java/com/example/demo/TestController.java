package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	private SimpMessagingTemplate simp;

	@GetMapping("/send/{id}")
	public void sendData(@PathVariable("id") String id) {
		simp.convertAndSend("/queue/output", id);
	}

	@MessageMapping("/input")
	public void websocketTest(String input) {
		System.out.println(input);
	}
}
