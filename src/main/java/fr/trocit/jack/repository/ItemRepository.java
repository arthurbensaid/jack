package fr.trocit.jack.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import fr.trocit.jack.entity.Item;

@Repository
public class ItemRepository extends CommonRepository<Item> {

	public ItemRepository(EntityManager entityManager) {
		super(Item.class, entityManager);
	}

}
