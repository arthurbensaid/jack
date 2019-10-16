package fr.trocit.jack.dto;

import java.util.ArrayList;
import java.util.List;

import fr.trocit.jack.entity.Item;
import fr.trocit.jack.entity.Usr;

public class UsrDto {
	
	public int id;
	public String username;
	public String password;
	public String avatar;
	public String email;
	public String phone;
	public String town;
	public GiveListDto giveList;
	public List<ItemDto> likedItems;
	
	public UsrDto() {
		super();
	}
	
	public UsrDto(Usr usr) {
		this.id = usr.id;
		this.username = usr.getUsername();
		this.password = usr.getPassword();
		this.avatar = usr.getAvatar();
		this.email = usr.getEmail();
		this.phone = usr.getPhone();
		this.town = usr.getTown();
		this.giveList = new GiveListDto(usr.getGiveList());
		
		this.likedItems = new ArrayList<ItemDto>();
		
		List<Item> trueLikedItems = usr.getLikedItems();
		
		for(Item item:trueLikedItems) {
			if(item!=null) {
				likedItems.add(new ItemDto(item));
			}
		}
		
	}
}
