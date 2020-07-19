package citation.domain;

import lombok.Data;

@Data
public class Auteur {
	
	public int id;
	public String prenom;
	public String nom;
	public String dateNaissance;
}