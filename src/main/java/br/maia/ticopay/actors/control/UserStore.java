package br.maia.ticopay.actors.control;

import br.maia.ticopay.actors.entity.Actor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@Dependent
public class UserStore {

    @Inject
    EntityManager em;

    public Actor findById(Long id) {
        return em.find(Actor.class, id);
    }
}
