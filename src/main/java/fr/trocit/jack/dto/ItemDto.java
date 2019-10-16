package fr.trocit.jack.dto;

import java.util.ArrayList;
import java.util.List;

import fr.trocit.jack.entity.Item;
import fr.trocit.jack.entity.Usr;


public class ItemDto {
	
	public int id;
	public String title;
	public String photo;
	public String description;
	public Integer listId;	
	public ArrayList<String> categories;
	public List<Integer> likersId;
	
	public ItemDto() {
		super();
	}
	
	public ItemDto(Item item) {
		this.id = item.id;
		this.title = item.getTitle();
		this.photo = item.getPhoto();
		this.description = item.getDescription();
		this.listId = item.getlist().id;
		this.categories = item.getCategories();
		
		this.likersId = new ArrayList<Integer>();
		
		List<Usr> likers = item.getLikers();
		
		for(Usr usr:likers) {
			if(usr!=null) {
				this.likersId.add(usr.id);
			}
		}
	}
	
}
