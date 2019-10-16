package fr.trocit.jack.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="givelist")
public class GiveList extends GenericEntity {

	@OneToOne
	@JoinColumn(name="usr_id")
	private Usr owner;

	@OneToMany(mappedBy="list")
	private List<Item> usrItems = new ArrayList<>();

	public Usr getOwner() {
		return owner;
	}

	public void setOwner(Usr owner) {
		this.owner = owner;
	}

	public List<Item> getItems() {
		return usrItems;
	}

	public void setItems(List<Item> items) {
		this.usrItems = items;
	}
	
}
	
