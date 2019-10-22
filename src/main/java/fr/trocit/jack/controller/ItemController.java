package fr.trocit.jack.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
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

import fr.trocit.jack.dto.ItemDto;
import fr.trocit.jack.entity.GiveList;
import fr.trocit.jack.entity.Item;
import fr.trocit.jack.entity.Usr;
import fr.trocit.jack.repository.AbstractItemRepository;
import fr.trocit.jack.service.EmailService;
import fr.trocit.jack.service.ItemService;
import fr.trocit.jack.service.UsrService;

@RestController
@CrossOrigin
@RequestMapping("") // TODO 
public class ItemController {
	
	@Autowired ItemService serv;
	@Autowired UsrService usrServ;
	@Autowired AbstractItemRepository iRepo;
	@Autowired EmailService emailServ;

	
	@GetMapping("items")
	public ResponseEntity<List<ItemDto>> getAll() {
		List<ItemDto> displayList = new ArrayList<ItemDto>();
		List<Item> listAll = serv.getAll();
		
		for(Item item:listAll) {
			displayList.add(new ItemDto(item));
		}
		
		return new ResponseEntity<List<ItemDto>>(displayList, HttpStatus.OK);
	}
	
	
	@GetMapping("items/{id}")
	public ResponseEntity<ItemDto> getById(@PathVariable int id) {
		Item item = serv.getById(id);
		if(item==null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ItemDto(item), HttpStatus.OK);
	}
	
	@PutMapping("items")
	public ResponseEntity<String> like(@RequestBody String ids) {
		
		JsonParser springParser = JsonParserFactory.getJsonParser(); // Parsing du request Body
		Map<String, Object> map = springParser.parseMap(ids);
		
		int itemId = Integer.parseInt(map.get("itemId").toString()); // Extraction des id depuis l'objet JSON
		int usrId = Integer.parseInt(map.get("userId").toString());
		
		Item currentItem = serv.getById(itemId);; // Récupération des objets à modifier
		Usr currentUsr = usrServ.getById(usrId);
		
		currentItem.addLiker(currentUsr); // Modification des attributs de mes objets
		currentUsr.addLikedItem(currentItem);
		
		serv.save(currentItem); // Persistance des changements
		usrServ.save(currentUsr);
		
		Usr usrNotified = currentItem.getlist().getOwner();
		
		emailServ.sendSimpleMessage(usrNotified.getEmail(), "Blah", currentUsr.getUsername() + " a liké votre " + currentItem.getTitle());
		
		return new ResponseEntity<String>(currentUsr.getUsername() + " a liké " + currentItem.getTitle(), HttpStatus.OK);
	}
	
	@PostMapping("items")
	public ResponseEntity<Boolean> isMatch(@RequestBody String ids) {
		JsonParser springParser = JsonParserFactory.getJsonParser(); // Parsing du request Body
		Map<String, Object> map = springParser.parseMap(ids);
		
		int itemId = Integer.parseInt(map.get("itemId").toString()); // Extraction des id depuis l'objet JSON
		int usrId = Integer.parseInt(map.get("userId").toString());
		
		Usr currentUsr = usrServ.getById(usrId);
		Usr otherUsr = serv.getById(itemId).getlist().getOwner();
		
		String otherItem = currentUsr.isMatch(otherUsr);
		if(otherItem != null) {
			String currentItem = serv.getById(itemId).getTitle();
			
			emailServ.sendSimpleMessage(currentUsr.getEmail(), "Genial, un match !", 
					otherUsr.getUsername() + " a liké votre " + otherItem + "et vous son/sa " + currentItem);
			emailServ.sendSimpleMessage(otherUsr.getEmail(), "Genial, un match !", 
					currentUsr.getUsername() + " a liké votre " + currentItem + "et vous son/sa " + otherItem);
		}
		
		return new ResponseEntity<Boolean>(otherItem != null, HttpStatus.OK);
	}
	
	
	@GetMapping("users/{usrId}/items")
	public ResponseEntity<List<ItemDto>> getMyItems(@PathVariable int usrId) {
		List<ItemDto> displayList = new ArrayList<ItemDto>();
		
		GiveList myList = usrServ.getById(usrId).getGiveList();
		
		for(Item item:myList.getItems()) {
			displayList.add(new ItemDto(item));
		}
		
		return new ResponseEntity<List<ItemDto>>(displayList, HttpStatus.OK);
	}
	
	@GetMapping("users/{usrId}/myLikedItems")
	public ResponseEntity<List<ItemDto>> getMyLikedItems(@PathVariable int usrId) {
		List<ItemDto> displayList = new ArrayList<ItemDto>();
		
		List<Item> likedItems = usrServ.getById(usrId).getLikedItems();
		
		for(Item item:likedItems) {
			displayList.add(new ItemDto(item));
		}
		
		return new ResponseEntity<List<ItemDto>>(displayList, HttpStatus.OK);
	}
	
	@GetMapping("{usrId}/items")
	public ResponseEntity<List<ItemDto>> displayOtherItems(@PathVariable int usrId){
		int idGiveList = usrServ.getById(usrId).getGiveList().id;
		
		System.out.println(idGiveList);
		
		List<ItemDto> displayList = new ArrayList<ItemDto>();
		List<Item> listAll = iRepo.displayOtherItems(idGiveList);
		
		for(Item item:listAll) {
			displayList.add(new ItemDto(item));
		}
		
		return new ResponseEntity<List<ItemDto>>(displayList, HttpStatus.OK);
	}
	
	
	@PostMapping("users/{usrId}/items")
	public ResponseEntity<Integer> createItemPicture(@PathVariable int usrId,
			@RequestParam("title") String title,
			@RequestParam("photo") MultipartFile photo,
			@RequestParam("description") String description) throws IOException {
		try {
			// Instanciation d'un nouvel item qu'on va remplir avec les données du formulaire
			Item item = new Item();
			
			GiveList giveList = usrServ.getById(usrId).getGiveList();
			
			item.setlist(giveList);
			
			item.setTitle(title);
			
			item.setDescription(description);
			
			item.setPhoto(photo.getOriginalFilename());
			
			item.setLikers(new ArrayList<Usr>());
			
			int id = serv.save(item);
			
			List<Item> updatingList = giveList.getItems();
			
			updatingList.add(item);
			
			giveList.setItems(updatingList);
			
			// Send the picture to the service to save them in a server folder
			
			serv.savePicture(photo);
			
			return new ResponseEntity<Integer>(id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Integer>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	@PutMapping("users/{usrId}/items/{id}")
	public ResponseEntity<Integer> updateItemPicture(@PathVariable int id,
			@RequestParam("title") String title,
			@RequestParam("photo") MultipartFile photo,
			@RequestParam("description") String description) throws IOException {
		try {
			// Récupération de l'item qu'on va update avec les données du formulaire
			Item item = serv.getById(id);
			
			item.setTitle(title);
			
			item.setDescription(description);
			
			item.setPhoto(photo.getOriginalFilename());
			
			serv.save(item);
			
			// Send the picture to the service to save them in a server folder
			
			serv.savePicture(photo);
			
			return new ResponseEntity<Integer>(id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Integer>(HttpStatus.EXPECTATION_FAILED);
		}
		
	}
	
	@DeleteMapping("users/{usrId}/items/{id}")
	public ResponseEntity<String> deleteItem(@PathVariable int usrId, @PathVariable int id) {
		Item currentItem = serv.getById(id);
		if(!serv.existItem(currentItem)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		for(Usr usr:currentItem.getLikers()) {
			usr.removeLikedItem(currentItem);
			usrServ.save(usr);
		}
		serv.delete(currentItem);
		return new ResponseEntity<>("L'item a bien été supprimmé", HttpStatus.OK);
	}

}
