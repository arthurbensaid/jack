package fr.trocit.jack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.trocit.jack.entity.Usr;

@Repository
public interface AbstractUsrRepository extends JpaRepository<Usr, Integer> {

	@Query("SELECT u FROM Usr u WHERE u.username=?1 AND u.password=?2")
	public Usr validateLogin(String username, String password);
	
}
