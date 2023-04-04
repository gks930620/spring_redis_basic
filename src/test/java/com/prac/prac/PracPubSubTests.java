package com.prac.prac;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;

@SpringBootTest
class PracPubSubTests {
	@Autowired
	private  RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private  RedisMessageListenerContainer redisMessageListenerContainer;
	//MessageListener를 저장한 container,   여기에 channel과 구독자를 설정할 수 있따.

	private final ObjectMapper mapper = new ObjectMapper();

	@Test // 방에 추가된 사람마다 System.out.println()이 잘 됐는지만 확인하겠습니다.
	public void pubSubTest(){
		ChannelTopic topic1=new ChannelTopic("topic1");
		MessageListener subscriber1=new MessageListener() {
			@Override
			public void onMessage(Message message, byte[] pattern) {
				try {
					String publishedMessage = mapper.readValue(message.getBody(), String.class);
					System.out.println("I'm subscriber1 and the message i received is : '" + publishedMessage +"'");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
		MessageListener subscriber2=(message,pattern)->{
			try{
				String publishedMessage = mapper.readValue(message.getBody(), String.class);
				System.out.println("I'm subscriber2 and the message i received is : '" + publishedMessage + "'");
			}catch (IOException e) {throw new RuntimeException(e);}
		};

		//topic1에 2명의 subscriber 등록
		redisMessageListenerContainer.addMessageListener(subscriber1,topic1 );
		redisMessageListenerContainer.addMessageListener(subscriber2,topic1);
		redisTemplate.convertAndSend(topic1.getTopic(), " publish Message ");

		ChannelTopic topic2=new ChannelTopic("topic2");
		redisTemplate.convertAndSend(topic2.getTopic()," There is no subscriber");

	}

}

//test

