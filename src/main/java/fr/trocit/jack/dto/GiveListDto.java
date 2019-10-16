package fr.trocit.jack.dto;

import java.util.ArrayList;
import java.util.List;

import fr.trocit.jack.entity.GiveList;
import fr.trocit.jack.entity.Item;

public class GiveListDto {
	
	public int id;
	public int ownerId;
	public List<ItemDto> usrItems;
	
	public GiveListDto() {
		super();
	}
	
	public GiveListDto(GiveList giveList) {
		this.id = giveList.id;
		this.ownerId = giveList.getOwner().id;
		this.usrItems = new ArrayList<ItemDto>();
		
		List<Item> trueItems = giveList.getItems();
		
		for(Item item:trueItems) {
			if(item!=null) {
				this.usrItems.add(new ItemDto(item));
			}
		}
	}

}
