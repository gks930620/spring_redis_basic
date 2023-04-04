package com.prac.prac;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PracApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void StringTest(){
		ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();


		stringValueOperations.set("testKey", "value");
		String getValue=stringValueOperations.get("testKey");

		assertThat(getValue).isEqualTo( "value");

		//삭제
		redisTemplate.delete("testKey");
		String getValueAfterDelete = stringValueOperations.get("testKey");
		assertThat(redisTemplate.hasKey("testKey")).isFalse();
		assertThat(getValueAfterDelete).isNullOrEmpty();
	}


	@Test
	public void listTest() {
		ListOperations listOperations = redisTemplate.opsForList();
		listOperations.rightPush("fruit", "apple");
		listOperations.rightPush("fruit", "grape");
		listOperations.rightPush("fruit", "strawberry");  //fruit이라는 키로 계속 push하면 list형태로 저장됨.  왼쪽에서도 넣을 수 있다.

		String apple = (String)listOperations.leftPop("fruit");   //한 개씩 뺄 수 있다.  오른쪽에서도 뺄 수 있다.
		String grape = (String)listOperations.leftPop("fruit");
		String strawberry = (String)listOperations.leftPop("fruit");

		assertThat(apple).isEqualTo("apple");
		assertThat(grape).isEqualTo("grape");
		assertThat(strawberry).isEqualTo("strawberry");

		//삭제
		redisTemplate.delete("fruit");
		assertThat(redisTemplate.hasKey("fruit") ).isFalse();

	}


	@Test
	public void listTes2() {
		ListOperations listOperations = redisTemplate.opsForList();
		listOperations.rightPush("fruit", "apple");
		listOperations.rightPush("fruit", "grape");
		listOperations.rightPush("fruit", "strawberry");  //fruit이라는 키로 계속 push하면 list형태로 저장됨.  왼쪽에서도 넣을 수 있다.

		List<String> list = listOperations.range("fruit", 0, listOperations.size("fruit") - 1);

		assertThat(list).contains("apple","grape","strawberry");

		//삭제
		redisTemplate.delete("fruit");
		assertThat(redisTemplate.hasKey("fruit") ).isFalse();
	}

	@Test
	public void listTest3() {
		ListOperations listOperations = redisTemplate.opsForList();
		List<String> fruitList=new ArrayList<>();
		fruitList.add("apple");
		fruitList.add("grape");
		fruitList.add("strawberry");
		listOperations.rightPushAll("fruit",fruitList);   //list를 한번에 넣을 수도 있다.

		List<String> list = listOperations.range("fruit", 0, listOperations.size("fruit") - 1);

		assertThat(list).contains("apple","grape","strawberry");

		//삭제
		redisTemplate.delete("fruit");
		assertThat(redisTemplate.hasKey("fruit") ).isFalse();
	}


	//set도 list와 방식은 비슷하다. 단지 저장되는 자료구조가 set일뿐.
	// list의  pushALl 처럼 한번에 Set을 저장하는 방법은 없나보다. 반복문으로 add해야될
	// store메소드의 파라미터를 잘 보면 Collection으로 key값들을 넘긴다.   Set을 자료구조에 있는 키랑 합치는게 아님..
	@Test
	public void setTest(){
		SetOperations setOperations = redisTemplate.opsForSet();

		setOperations.add("fruit","apple");
		setOperations.add("fruit","apple");
		setOperations.add("fruit","grape");

		Set<String> fruit = setOperations.members("fruit");
		long size=setOperations.size("fruit");
		Set<String> setFromRedis=setOperations.members("fruit");

		assertThat(size).isEqualTo(2);
		assertThat(setFromRedis).contains("apple","grape");

		//삭제
		redisTemplate.delete("fruit");
		assertThat(redisTemplate.hasKey("fruit") ).isFalse();
	}

	@Test
	public void setUnionAndStoreTest(){
		SetOperations setOperations = redisTemplate.opsForSet();

		setOperations.add("fruit1","apple");
		setOperations.add("fruit1","apple");
		setOperations.add("fruit1","grape");

		setOperations.add("fruit2","melon");
		setOperations.add("fruit2","watermelon");
		setOperations.unionAndStore("fruit1","fruit2","fruit");


		long size=setOperations.size("fruit");
		Set<String> setFromRedis=setOperations.members("fruit");

		assertThat(size).isEqualTo(4);
		assertThat(setFromRedis).contains("apple","grape","melon","watermelon");
		//삭제
		redisTemplate.delete("fruit");
		assertThat(redisTemplate.hasKey("fruit") ).isFalse();
	}


	@Test
	public void sortedSetTest(){
		ZSetOperations zSetOperations = redisTemplate.opsForZSet();  //zSet= sortedSet
		zSetOperations.add("fruit","apple",0);
		zSetOperations.add("fruit","apple",1);  //이미 있으므로 저장안됨. apple은 score가 0
		zSetOperations.add("fruit","grape",1);

		long size= zSetOperations.size("fruit");
		Set<String> setFromRedis = zSetOperations.range("fruit", 0, zSetOperations.size("fruit") - 1);

		assertThat(size).isEqualTo(2);
		assertThat(setFromRedis).containsExactly("apple","grape");  //순서보장해준다. 0,1 순

		//삭제
		redisTemplate.delete("fruit");
		assertThat(redisTemplate.hasKey("fruit") ).isFalse();
	}



	@Test
	public void hashTest(){
		HashOperations hashOperations = redisTemplate.opsForHash();

		Map<String, Integer> fruit= new HashMap<>();  //내가 하루에 먹는 과일 개수
		fruit.put("apple",5);
		fruit.put("grape",2);
		fruit.put("banana",30);
		hashOperations.putAll("fruit",fruit);

		Map<String, Integer> mapFromRedis = hashOperations.entries("fruit");// {apple,5} {grape,2}   {banana , 30}  의 entry 3쌍
		int size= mapFromRedis.size();

		assertThat(size).isEqualTo(3);
		assertThat(mapFromRedis).containsKeys("apple","grape","banana");
		assertThat(mapFromRedis).containsValues(5,2,30);
		//삭제
		redisTemplate.delete("fruit");
		assertThat(redisTemplate.hasKey("fruit") ).isFalse();
	}




}

