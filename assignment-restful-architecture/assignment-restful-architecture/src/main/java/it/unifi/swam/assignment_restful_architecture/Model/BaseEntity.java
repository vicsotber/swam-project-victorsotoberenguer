package it.unifi.swam.assignment_restful_architecture.Model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String uuid;
	
	protected BaseEntity() {}
	public BaseEntity(String uuid) {
		if(uuid == null) {
			throw new IllegalArgumentException("uuid cannot be null");
		}
		this.uuid = uuid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		if(obj==null) {
			return false;
		}
		if(!(obj instanceof BaseEntity)) {
			return false;
		}
		return uuid.equals(((BaseEntity) obj).getUuid());
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public Long getId() {
		return this.id;
	}
}