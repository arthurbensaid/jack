package fr.trocit.jack.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="usr")
public class Usr extends GenericEntity {

	private String username;
	private String password;
	private String avatar;
	private String email;
	private String phone;
	private String town;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="gl_id")
	private GiveList giveList;
	
	@ManyToMany
	@JoinTable(
			name = "crush",
			joinColumns = @JoinColumn(name = "usr_id"),
			inverseJoinColumns = @JoinColumn(name = "item_id"))
	private List<Item> likedItems;
	
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public GiveList getGiveList() {
		return giveList;
	}

	public void setGiveList(GiveList giveList) {
		this.giveList = giveList;
	}

	public List<Item> getLikedItems() {
		return likedItems;
	}

	public void setLikedItems(List<Item> likedItems) {
		this.likedItems = likedItems;
	}
	
	
	public void addLikedItem(Item item) {
		this.likedItems.add(item);
	}
	
	public void removeLikedItem(Item item) {
		this.likedItems.remove(item);
	}
	
//	public Boolean isMatch(Usr otherUsr) {
//		for(Item ourItem:this.giveList.getItems()) {
//			if(ourItem!=null) {
//				for(Usr liker:ourItem.getLikers()) {
//					if(liker.equals(otherUsr)) return true;
//				}
//			}
//		}
//		return false;
//	}
	
	public String isMatch(Usr otherUsr) {
		for(Item ourItem:this.giveList.getItems()) {
			if(ourItem!=null) {
				for(Usr liker:ourItem.getLikers()) {
					if(liker.equals(otherUsr)) return ourItem.getTitle();
				}
			}
		}
		return null;
	}
}
