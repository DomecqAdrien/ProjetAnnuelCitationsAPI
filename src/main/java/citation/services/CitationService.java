package citation.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import java.util.Optional;

import citation.domain.Citation;
import citation.domain.CitationTag;
import citation.domain.Tag;
import citation.models.ShowCitationDTO;

@Service
public class CitationService {

	public static final String COL_NAME="Citation";
	
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


    

    
    
}
