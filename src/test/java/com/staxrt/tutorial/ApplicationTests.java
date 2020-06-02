package com.staxrt.tutorial;

import com.staxrt.tutorial.model.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetAllUsers() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/tasks",
				HttpMethod.GET, entity, String.class);

		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void testGetUserById() {
		Task task = restTemplate.getForObject(getRootUrl() + "/tasks/1", Task.class);
		Assert.assertNotNull(task);
	}

	@Test
	public void testCreateUser() {
		Task task = new Task();
		task.setTitle("Test Title");
		task.setDescription("Test description");
		task.setDate(new Date());

		ResponseEntity<Task> postResponse = restTemplate.postForEntity(getRootUrl() + "/tasks", task, Task.class);
		Assert.assertNotNull(postResponse);
		Assert.assertNotNull(postResponse.getBody());
	}

	@Test
	public void testUpdatePost() {
		int id = 1;
		Task task = restTemplate.getForObject(getRootUrl() + "/tasks/" + id, Task.class);
		task.setTitle("Test Title");
		task.setDescription("Test description");
		task.setDate(new Date());

		restTemplate.put(getRootUrl() + "/tasks/" + id, task);

		Task updatedUser = restTemplate.getForObject(getRootUrl() + "/tasks/" + id, Task.class);
		Assert.assertNotNull(updatedUser);
	}

	@Test
	public void testDeletePost() {
		int id = 2;
		Task task = restTemplate.getForObject(getRootUrl() + "/tasks/" + id, Task.class);
		Assert.assertNotNull(task);

		restTemplate.delete(getRootUrl() + "/tasks/" + id);

		try {
			task = restTemplate.getForObject(getRootUrl() + "/tasks/" + id, Task.class);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}

}
