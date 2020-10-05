package citation.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import citation.domain.Citation;
import citation.domain.CitationTag;
import citation.models.ShowCitationDTO;
import citation.services.CitationService;

@RestController
@RequestMapping("/citation")
public class CitationController {
	
	@Resource CitationService citationService;
	
	@GetMapping("/searchCitationsByText")
	public String searchCitationsByText(@RequestParam(value="text") String text){
		return citationService.searchCitationsByText(text);
	}
	
	@GetMapping("/getCitation")
	public String getCitation(@RequestParam(value="citation") String citation){
		return citationService.getCitation(citation);
	}
	
	@GetMapping("/getQueueResponse")
	public String getQueueResponse(@RequestParam(value="queueName") String replyQueueName) {
		return citationService.getQueueResponse(replyQueueName);
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
