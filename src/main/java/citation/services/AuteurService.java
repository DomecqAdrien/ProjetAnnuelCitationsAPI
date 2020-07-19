package citation.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import citation.domain.Auteur;

@Service
public class AuteurService {

	public static final String COL_NAME="Auteur";
	
	

    public String saveAuteurDetails(Auteur auteur) throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(auteur.getNom()+auteur.getPrenom()).set(auteur);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Auteur getAuteurDetails(String id) throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        

        DocumentSnapshot document = future.get();
        //System.out.println(document.get("nom"));
        Auteur auteur = null;

        if(document.exists()) {
        	auteur = document.toObject(Auteur.class);
            return auteur;
        }else {
            return null;
        }
    }
    
    public List<Auteur> getAllAuteurs() throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
    	CollectionReference docs = dbFirestore.collection(COL_NAME);
    	List<Auteur> auteurs = new ArrayList<>();
 
    	for(DocumentReference doc : docs.listDocuments()) {
    		auteurs.add(doc.get().get().toObject(Auteur.class));
    	}
		return auteurs;
	}
    
    

    public String updateAuteurDetails(Auteur auteur) throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(auteur.getNom()).set(auteur);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteAuteur(String name) {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COL_NAME).document(name).delete();
        return "Document with Auteur ID "+name+" has been deleted";
    }

}
