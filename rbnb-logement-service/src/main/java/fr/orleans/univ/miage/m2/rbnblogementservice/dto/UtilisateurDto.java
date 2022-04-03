package fr.orleans.univ.miage.m2.rbnblogementservice.dto;

public class UtilisateurDto {

    private String id;
    private String nom;
    private String prenom;
//    private String urlPhotoDeProfil;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
