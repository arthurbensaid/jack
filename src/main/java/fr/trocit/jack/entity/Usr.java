package fr.trocit.jack.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
	
	public ObjectNode toJsonNode() {
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode usrNode = mapper.createObjectNode();
		
		usrNode.put("id", this.id);
		usrNode.put("username", this.username);
		usrNode.put("password", this.password);
		usrNode.put("avatar", this.avatar);
		usrNode.put("email", this.email);
		usrNode.put("phone", this.phone);
		usrNode.put("town", this.town);
		
		ArrayNode giveListArrayNode = usrNode.putArray("giveList");
		giveListArrayNode.add(this.giveList.toJsonNode());
		
		ArrayNode likedItemArrayNode = usrNode.putArray("likedItems");
		
		for (Item item:this.likedItems) {
			if(item!=null) {
				likedItemArrayNode.add(item.toJsonNode());
			}
		}
		
		return usrNode;
	}
	
	public void addLikedItem(Item item) {
		this.likedItems.add(item);
	}
}
