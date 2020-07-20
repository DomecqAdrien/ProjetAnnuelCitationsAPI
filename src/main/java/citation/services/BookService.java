package citation.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import citation.domain.Book;

@Service
public class BookService {
	
	public static final String COL_NAME="Book";
	
	public List<Book> getAllBooks() throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
    	CollectionReference docs = dbFirestore.collection(COL_NAME);
    	List<Book> books = new ArrayList<>();
 
    	for(DocumentReference doc : docs.listDocuments()) {
    		books.add(doc.get().get().toObject(Book.class));
    	}
		return books;
	}
	
	 public Book getBookById(int id) throws InterruptedException, ExecutionException {
	    	List<Book> auteurs = getAllBooks();
	    	Optional<Book> auteur = auteurs.stream().filter(a -> a.getId() == id).findFirst();
	    	if(auteur.isPresent()) {
	    		return auteur.get();
	    	}
	    	else {
	    		return null;
	    	}
	    }

	public String addBook(Book book) throws InterruptedException, ExecutionException {
    	Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document().set(book);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }


}
