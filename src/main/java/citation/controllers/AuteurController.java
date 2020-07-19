package citation.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import citation.domain.Auteur;
import citation.services.AuteurService;

@RestController
public class AuteurController {

	@Resource AuteurService auteurService;
	
	@GetMapping("/all")
	public List<Auteur> getAllAuteurs() throws InterruptedException, ExecutionException{
		return auteurService.getAllAuteurs();
	}

    @GetMapping("/getAuteurDetails")
    public Auteur getAuteur(@RequestParam String name ) throws InterruptedException, ExecutionException{
    	String uri = "http://localhost:8080/";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = 
			      new HttpEntity<String>(name, headers);
	    String result = restTemplate.postForObject(uri, request, String.class);
    	//System.out.println(name);
        return auteurService.getAuteurDetails(name);
    }

    @PostMapping("/createAuteur")
    public String createAuteur(@RequestBody citation.domain.Auteur Auteur ) throws InterruptedException, ExecutionException {
        return auteurService.saveAuteurDetails(Auteur);
    }

    @PutMapping("/updateAuteur")
    public String updateAuteur(@RequestBody Auteur Auteur  ) throws InterruptedException, ExecutionException {
        return auteurService.updateAuteurDetails(Auteur);
    }

    @DeleteMapping("/deleteAuteur")
    public String deleteAuteur(@RequestParam String name){
        return auteurService.deleteAuteur(name);
    }
}
