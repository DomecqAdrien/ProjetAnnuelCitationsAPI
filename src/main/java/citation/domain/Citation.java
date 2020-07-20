package citation.domain;

import lombok.Data;

@Data
public class Citation {

	public int id;
	public int bookId;
	public String citation;	
}
