package citation.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import citation.domain.Citation;
import citation.domain.CitationTag;
import citation.models.CitationDTO;
import citation.models.ShowCitationDTO;
import citation.services.CitationService;

@RestController
@RequestMapping("/citation")
public class CitationController {
	
	@Resource CitationService citationService;
	private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
	
	public CitationController() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }
	
	@PostMapping("/test")
	public String test(@RequestParam(value="text") String text, @RequestParam(value="hash") String hash){
		String replyQueueName = null;
		try {
			replyQueueName = channel.queueDeclare().getQueue();
			System.out.println(replyQueueName);
	        AMQP.BasicProperties props = new AMQP.BasicProperties
	                .Builder()
	                .correlationId(hash)
	                .replyTo(replyQueueName)
	                .build();
	        channel.basicPublish("", requestQueueName, props, text.getBytes("UTF-8"));
	  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
        return replyQueueName;
        
	}
	
	@GetMapping("/testCheck")
	public String testCheck(@RequestParam(value="queueName") String replyQueueName, @RequestParam(value="hash") String hash) {
		System.out.println(replyQueueName);
		String result = null;

		try {
			final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
			String ctag;
			ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
			    if (delivery.getProperties().getCorrelationId().equals(hash)) {
			        response.offer(new String(delivery.getBody(), "UTF-8"));
			    }
			}, consumerTag -> {});
			result = response.take();
			channel.basicCancel(ctag);
			System.out.println(result);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return result;
	}	
	
	

	@PostMapping("/getCitationByText")
	public List<CitationDTO> getCitationByText(@RequestBody String text) {
		String uri = "http://localhost:8080/searchByText";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = 
			      new HttpEntity<String>(text, headers);
	    ResponseEntity<CitationDTO[]> result = restTemplate.postForEntity(uri, request, CitationDTO[].class);
	    
	    System.out.println(result.getBody());
	    return Arrays.asList(result.getBody());
	}
	
	@GetMapping("/all")
	public List<ShowCitationDTO> getAllCitations() throws InterruptedException, ExecutionException{
		System.out.println("TEST");
		return citationService.getAllCitations();
	}
	
	@PostMapping("")
	public String addCitation(@RequestBody Citation citation) throws InterruptedException, ExecutionException {
		return citationService.saveCitation(citation);
	}
	
	@PostMapping("/addTag")
	public String addTag(@RequestBody CitationTag ctag) throws InterruptedException, ExecutionException {
		return citationService.saveTag(ctag);
	}


	
}
