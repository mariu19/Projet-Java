package JeuDeCartes;

public class Carte {
	
	 private Couleur couleur;
	  private Nom nom;
	  private int valeur;
	  
	public Carte(Couleur couleur, Nom nom) {
		this.couleur = couleur;
		this.nom = nom;
		//this.valeur = this.valeurDefaut(nom);
		switch (nom){
		case Deux:
			this.valeur = 2;
			break;
		case Trois:
			this.valeur = 3;
			break;
		case Quatre:
			this.valeur = 4;
			break;
		case Cinq:
			this.valeur = 5;
			break;
		case Six: 
			this.valeur = 6;
			break;
		case Sept:
			this.valeur = 7;
			break;
		case Huit:
			this.valeur = 8;
			break;
		case Neuf:
			this.valeur = 9;
			break;
		case As:
			this.valeur = 11;
			break;
		default: 
			this.valeur = 10;
			break;
			}
	}

	public Couleur getCouleur() {
		return couleur;
	}


	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}


	public Nom getNom() {
		return nom;
	}


	public void setNom(Nom nom) {
		this.nom = nom;
	}
	
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	
	
   public int getValeur() {
		return valeur;
	}

	public String toString() { return nom + " de " + couleur + " valeur :  "+ valeur; }

	

}
