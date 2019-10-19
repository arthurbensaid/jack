package fr.trocit.jack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.trocit.jack.entity.Item;

@Repository
public interface AbstractItemRepository extends JpaRepository<Item, Integer> {
	
	@Query("SELECT i FROM Item i WHERE gl_id<>?1")
	public List<Item> displayOtherItems(int myGiveListId);

}
