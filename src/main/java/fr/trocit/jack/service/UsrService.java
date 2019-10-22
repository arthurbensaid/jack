package fr.trocit.jack.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.trocit.jack.entity.Usr;
import fr.trocit.jack.repository.UsrRepository;

@Service
public class UsrService {
	
	@Autowired UsrRepository repo;
	
	private final Path rootLocation = Paths.get("src/main/resources/static");
	
	public List<Usr> getAll() {
		return repo.getAll();
	}
	
	public Usr getById(int id) {
		return repo.getById(id);
	}
	
	public int save(Usr usr) {
		Usr updatedUsr = repo.save(usr);	
		return updatedUsr.id;
	}
	
	public void delete(Usr usr) {
		repo.delete(usr);
	}
	
	public boolean existUsr(Usr usr) { 
	return repo.existsById(usr.id);
	}
	
	public void savePicture(MultipartFile file) {
		try {
			// filePath = URL du dossier ou sont sauvées les images + nom de l'image
			Path filePath = Paths.get(rootLocation + "/" + file.getOriginalFilename());
			
			// read the file as a stream and copy it to the rootLocation folder
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to save picture");
		}
		
		return;
	}
}
