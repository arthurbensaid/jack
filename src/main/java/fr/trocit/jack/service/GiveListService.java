package fr.trocit.jack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.trocit.jack.entity.GiveList;
import fr.trocit.jack.repository.GiveListRepository;

@Service
public class GiveListService {
	
	@Autowired GiveListRepository repo;
	
	public List<GiveList> getAll() {
		return repo.getAll();
	}
	
	public GiveList getById(int id) {
		return repo.getById(id);
	}
	
	public int save(GiveList list) {
		GiveList updatedList = repo.save(list);
		return updatedList.id;
	}
	
	public void delete(GiveList list) {
		repo.delete(list);
	}

}
