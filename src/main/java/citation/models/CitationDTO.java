package citation.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import citation.domain.Tag;

public class CitationDTO {
	public int id;
	public int bookId;
	public BookDTO book;
	public String citation;
	public List<Tag> tags;
	@JsonIgnoreProperties({"citationsConnexes"})
	public List<CitationDTO> citationsConnexes;
	public int tauxRessemblance;
}
