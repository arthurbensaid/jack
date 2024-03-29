package fr.trocit.jack.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.trocit.jack.dto.UsrDto;
import fr.trocit.jack.entity.GiveList;
import fr.trocit.jack.entity.Item;
import fr.trocit.jack.entity.Usr;
import fr.trocit.jack.repository.AbstractUsrRepository;
import fr.trocit.jack.service.GiveListService;
import fr.trocit.jack.service.ItemService;
import fr.trocit.jack.service.UsrService;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("users")
public class UsrController {
	
	@Autowired UsrService serv;
	@Autowired GiveListService listServ;
	@Autowired ItemService iServ;
	@Autowired AbstractUsrRepository usrRepo;
	
	@GetMapping("")
	public ResponseEntity<List<UsrDto>> getAll() {
		List<UsrDto> displayList = new ArrayList<UsrDto>();
		List<Usr> listAll = serv.getAll();
		
		for(Usr usr:listAll) {
			if(usr!=null) {
				displayList.add(new UsrDto(usr));
			}
		}
		
		return new ResponseEntity<List<UsrDto>>(displayList, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsrDto> getById(@PathVariable int id) {
		Usr usr = serv.getById(id);
		if(usr==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(new UsrDto(usr), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Integer> createUsrPicture(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("avatar") MultipartFile avatar,
			@RequestParam("email") String email,
			@RequestParam("phone") String phone,
			@RequestParam("town") String town) throws IOException {
		try {
			// Instanciation d'un nouvel item qu'on va remplir avec les données du formulaire
			Usr usr = new Usr();
			
			GiveList list = new GiveList();
			
			list.setItems(new ArrayList<Item>());
			
			usr.setUsername(username);
			usr.setPassword(password);
			usr.setAvatar(avatar.getOriginalFilename());
			usr.setEmail(email);
			usr.setPhone(phone);
			usr.setTown(town);
			usr.setGiveList(list);
			usr.setLikedItems(new ArrayList<Item>());
			
			int id = serv.save(usr);
			
			list.setOwner(usr);
			
			listServ.save(list);
			
			// Send the picture to the service to save them in a server folder
			
			serv.savePicture(avatar);
			
			return new ResponseEntity<Integer>(id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Integer>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Integer> updateUsr(@RequestBody Usr newUsr, @PathVariable int id) {
		if(serv.getById(id)==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		Usr currentUsr = serv.getById(id);
		
		currentUsr.setUsername(newUsr.getUsername());
		currentUsr.setPassword(newUsr.getPassword());
		currentUsr.setAvatar(newUsr.getAvatar());
		currentUsr.setEmail(newUsr.getEmail());
		currentUsr.setPhone(newUsr.getPhone());
		currentUsr.setTown(newUsr.getTown());
		
		serv.save(currentUsr);
		
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUsr(@PathVariable int id) {
		Usr currentUsr = serv.getById(id);
		if(!serv.existUsr(currentUsr)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		if(!currentUsr.getGiveList().getItems().isEmpty()) {
			for (Item item:currentUsr.getGiveList().getItems()) {
				iServ.delete(item);
			}
		}
		
		listServ.delete(currentUsr.getGiveList());
		
		serv.delete(currentUsr);
		return new ResponseEntity<>("L'utilisateur a bien été supprimmé", HttpStatus.OK);
	}
	
	@GetMapping("/validateLogin")
	public ResponseEntity<Integer> authenticateUsr(
			@RequestParam("username") String username,
			@RequestParam("password") String password
			) {
		Usr validUsr = usrRepo.validateLogin(username, password);
		
		if(validUsr==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<Integer>(validUsr.id, HttpStatus.OK);
	}
}
