package fr.orleans.univ.miage.m2.rbnbmonolithique.service;

import fr.orleans.univ.miage.m2.rbnbmonolithique.entity.Commentaire;
import fr.orleans.univ.miage.m2.rbnbmonolithique.service.exceptions.LogementIntrouvableException;

import java.util.Collection;

public interface CommentaireService {
    Collection<Commentaire> getAllCommentairesByLogement(Long idLogement) throws LogementIntrouvableException;

    Commentaire createCommentaire(Commentaire commentaire);

    void updateCommentaire(Commentaire commentaire);

}
