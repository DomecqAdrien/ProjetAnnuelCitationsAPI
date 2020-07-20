package citation.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import citation.domain.Auteur;
import citation.services.AuteurService;

@RestController
@RequestMapping("/auteur")
public class AuteurController {

	@Resource AuteurService auteurService;
	
	@GetMapping("/all")
	public List<Auteur> getAllAuteurs() throws InterruptedException, ExecutionException{
		return auteurService.getAllAuteurs();
	}
    
    @GetMapping("/{id}")
    public Auteur getAuteur(@PathVariable int id) throws InterruptedException, ExecutionException {
    	return auteurService.getAuteurById(id);
    }
    

    @PostMapping("/createAuteur")
    public String createAuteur(@RequestBody Auteur Auteur ) throws InterruptedException, ExecutionException {
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
