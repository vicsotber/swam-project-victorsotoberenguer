package it.unifi.swam.assignment_restful_architecture.model_test;

import it.unifi.swam.assignment_restful_architecture.Model.BaseEntity;

//solves the problem of having BaseEntity abstract
public class FakeBaseEntity extends BaseEntity {
	public FakeBaseEntity (String uuid) {
		super(uuid);
	}
}