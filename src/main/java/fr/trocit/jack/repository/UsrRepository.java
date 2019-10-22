package fr.trocit.jack.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import fr.trocit.jack.entity.Usr;

@Repository
public class UsrRepository extends CommonRepository<Usr> {

	public UsrRepository(EntityManager entityManager) {
		super(Usr.class, entityManager);
	}

}
