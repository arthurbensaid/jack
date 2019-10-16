package fr.trocit.jack.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="item")
public class Item extends GenericEntity {

	private String title;
	private String photo;
	private String description;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="gl_id")
	private GiveList list;
	
	private ArrayList<String> categories = new ArrayList<String>();
	
	@ManyToMany(mappedBy = "likedItems")
	private List<Usr> likers = new ArrayList<>();

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public GiveList getlist() {
		return list;
	}

	public void setlist(GiveList giveList) {
		this.list = giveList;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	

	public List<Usr> getLikers() {
		return likers;
	}

	public void setLikers(List<Usr> likers) {
		this.likers = likers;
	}

	

	public void addLiker(Usr usr) {
		this.likers.add(usr);
	}
}
