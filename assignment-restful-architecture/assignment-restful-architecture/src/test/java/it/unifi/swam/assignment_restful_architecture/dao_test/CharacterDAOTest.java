package it.unifi.swam.assignment_restful_architecture.dao_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.DAOs.CharacterDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Character;

public class CharacterDAOTest extends JPATest {
	private Character character;
	private CharacterDAO characterDao;

	//concrete init() method
	@Override
	protected void init() throws IllegalAccessException {
		System.out.println("Start init custom for CharacterDAOTest");
		character = ModelFactory.character();
		character.setLevel(55);
		entityManager.persist(character); //Persisted manually, without using the DAO
		//this is done to test the retrieve afterwards
		characterDao = new CharacterDAO();
		FieldUtils.writeField(characterDao,"entityManager",entityManager,true);
	}
	
	//retieve test
	//check that the entity retrieved is the same as the one defined in the init()
	@Test
	public void testFindById() {
		System.out.println("Perform testFindById in CharacterDAOTest");
		Character result = characterDao.findById(character.getId()); //It has an ID since it has been persisted in the init()
		assertEquals(character.getId(), result.getId());
		assertEquals(character.getLevel(), result.getLevel());
	}
	
	
	//In this case we check persisting the data through the DAO
	@Test
	public void testSave() {
		System.out.println("Perform testSave in CharacterDAOTest");
		Character characterToPersist = ModelFactory.character();
		characterToPersist.setLevel(20);
		characterDao.save(characterToPersist);
		Character manuallyRetrievedCharacter =  entityManager.
				createQuery("FROM Character WHERE uuid = :uuid", Character.class)
				.setParameter("uuid", characterToPersist.getUuid())
				.getSingleResult();
		assertEquals(characterToPersist, manuallyRetrievedCharacter);
	}
	
	@Test
	public void testDelete() {
		System.out.println("Perform testDelete in CharacterDAOTest");
		Character characterToDelete = ModelFactory.character();
		characterToDelete.setLevel(16);;
		entityManager.persist(characterToDelete);
		characterDao.delete(characterToDelete);
		List<Character> manuallyRetrievedCharacter =  entityManager.
				createQuery("FROM Character WHERE uuid = :uuid", Character.class)
				.setParameter("uuid", characterToDelete.getUuid())
				.getResultList();
		assertTrue(manuallyRetrievedCharacter.isEmpty());
	}
}