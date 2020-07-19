package citation.domain;

import java.util.List;

import lombok.Data;

@Data
public class Citation {

	public int id;
	public Book book;
	public String citation;
	public List<Tag> tags;
	public List<Citation> citationConnexes;
	
}
