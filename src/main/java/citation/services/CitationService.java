package citation.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.util.Optional;

import citation.domain.Citation;
import citation.domain.CitationTag;
import citation.domain.Tag;
import citation.models.ShowCitationDTO;

@Service
public class CitationService {

	public static final String COL_NAME="Citation";
	private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    
    public CitationService() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
        //channel.queueDeclare(RPC_REPLY_QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(requestQueueName, false, false, false, null);
    }
	
	
    public List<ShowCitationDTO> getAllCitations() throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
    	CollectionReference docs = dbFirestore.collection(COL_NAME);
    	CollectionReference docCTags = dbFirestore.collection("citation_tag");
    	CollectionReference docTags = dbFirestore.collection("Tag");
    	List<ShowCitationDTO> citations = new ArrayList<>();
    	
    	List<Tag> tags = new ArrayList<>();
    	for(DocumentReference docTag : docTags.listDocuments()) {
    		Tag tag = docTag.get().get().toObject(Tag.class);
    		tags.add(tag);
    	}
    	
    	List<CitationTag> ctags = new ArrayList<>();
    	for(DocumentReference docCTag : docCTags.listDocuments()) {
    		CitationTag ctag = docCTag.get().get().toObject(CitationTag.class);
    		ctags.add(ctag);
    	}
 
    	for(DocumentReference doc : docs.listDocuments()) {
    		ShowCitationDTO citation = doc.get().get().toObject(ShowCitationDTO.class);
    		List<CitationTag> cts = ctags.stream().filter(tag -> tag.getCitationId() == citation.getId()).collect(Collectors.toList());
    		for(CitationTag ct : cts) {
    		Optional<Tag> tag = tags.stream().filter(t -> t.getId() == ct.getTagId()).findFirst();
    			citation.getTags().add(tag.get());
    		}
    		
    		citations.add(citation);
    	}
		return citations;
	}
    

    public String saveCitation(Citation citation) throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document().set(citation);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }
    
    public String saveTag(CitationTag ctag) throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("citation_tag").document().set(ctag);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }
    
    public String searchCitationsByText(String text) {
		return publish(text, "citations"); 
	}


	public String getCitation(String citation) {
		return publish(citation, "citation"); 
	}
    
    public String publish(String text, String type) {
    	System.out.println(type);
    	String replyQueueName = null;
    	String message = type+" ::: "+text;
		try {
			
			replyQueueName = channel.queueDeclare().getQueue();
			System.out.println(replyQueueName);
	        BasicProperties props = new BasicProperties
	                .Builder()
	                .replyTo(replyQueueName)
	                .build();
	        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));
	  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return replyQueueName;   
    }

	public String getQueueResponse(String replyQueueName) {
		System.out.println(replyQueueName);
		String result = null;
		try {	
			long countQueue = channel.messageCount(replyQueueName);
			System.out.println(countQueue);
			if(countQueue == 0) {
				result = "{\"statut\": \"Pas de nouveau message\"}";
			}else {
				while(countQueue != 0) {
					GetResponse chResponse = channel.basicGet(replyQueueName, true);
					if(chResponse != null) {
						 byte[] body = chResponse.getBody();
					     result = new String(body);
					     System.out.println(result);
					     countQueue--;
					}
				}
			}
			
			
			
			System.out.println("m: "+result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


    

    
    
}
