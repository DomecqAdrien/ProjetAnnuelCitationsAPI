package citation.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import citation.domain.Citation;
import citation.domain.CitationTag;
import citation.models.CitationDTO;
import citation.models.ShowCitationDTO;
import citation.services.CitationService;

@RestController
@RequestMapping("/citation")
public class CitationController {
	
	@Resource CitationService citationService;

	@PostMapping("/getCitationByText")
	public CitationDTO getCitationByText(@RequestBody String text) {
		String uri = "http://localhost:8080/";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = 
			      new HttpEntity<String>(text, headers);
	    ResponseEntity<CitationDTO> result = restTemplate.postForEntity(uri, request, CitationDTO.class);
	    
	    System.out.println(result.getBody());
	    return result.getBody();
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
