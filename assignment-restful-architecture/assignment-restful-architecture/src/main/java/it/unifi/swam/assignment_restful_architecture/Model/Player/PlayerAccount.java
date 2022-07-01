package it.unifi.swam.assignment_restful_architecture.Model.Player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.springframework.security.crypto.bcrypt.BCrypt;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;

@Entity
public class PlayerAccount extends BaseEntity{
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="friends",joinColumns=@JoinColumn(name="playerId"),inverseJoinColumns=@JoinColumn(
	        name = "playerFriendId", 
	        foreignKey = @ForeignKey(
	            name = "FK_PLAYERFRIENDID",
	            foreignKeyDefinition = "FOREIGN KEY (playerFriendId) REFERENCES playeraccount(id) ON DELETE CASCADE"
	    )))
	@OrderBy
	private List<PlayerAccount> friends;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(
	        name = "playerCharacter", 
	        foreignKey = @ForeignKey(
	            name = "FK_PLAYERCHARACTER",
	            foreignKeyDefinition = "FOREIGN KEY (playerCharacter) REFERENCES playercharacter(id) ON DELETE SET NULL"
	    ), unique = true)
	private Character character;
	
	
	PlayerAccount () {}
	
	public PlayerAccount(String uuid) {
		super(uuid);
		this.username = null;
		this.email = null;
		this.password = null;
		this.friends = new ArrayList<PlayerAccount>();
		this.character = null;
	}
	
	public PlayerAccount(String uuid, String username, String email, String password) {
		super(uuid);
		this.username = username;
		this.email = email;
		this.password = password;
		this.friends = new ArrayList<PlayerAccount>();
		this.character = null;
	}

	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}


	public Character getCharacter() {
		return this.character;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}

	public String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
		this.password = hashedPassword;
	}
	
	public void setPassword(String password, Boolean encrypted) {
		if(encrypted) {
			this.password = password;
		}else {
			setPassword(password);
		}
	}

	public List<PlayerAccount> getFriends() {
		return friends;
	}

	public void setFriends(List<PlayerAccount> friends) {
		this.friends = friends;
	}
	
	public void followFriend(PlayerAccount playeracc) {
		this.friends.add(playeracc);
	}
	
	public void unfollowFriend(PlayerAccount playeracc) {
		this.friends.remove(playeracc);
	}
}