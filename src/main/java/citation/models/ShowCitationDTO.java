package citation.models;

import java.util.ArrayList;
import java.util.List;

import citation.domain.Tag;
import lombok.Data;

@Data
public class ShowCitationDTO {
	public int id;
	public int bookId;
	public String citation;
	public List<Tag> tags = new ArrayList<>();
}
