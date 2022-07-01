package it.unifi.swam.assignment_restful_architecture.controller_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import it.unifi.swam.assignment_restful_architecture.Controllers.TaskController;
import it.unifi.swam.assignment_restful_architecture.DAOs.TaskDAO;
import it.unifi.swam.assignment_restful_architecture.Model.ModelFactory;
import it.unifi.swam.assignment_restful_architecture.Model.Game.Task;

public class TaskControllerTest {
	private TaskController taskController;
	private TaskDAO taskDao;
	private Task fakeTask;
	
	@Before
	public void setup() throws IllegalAccessException {
		taskController = new TaskController();
		
		taskDao = mock(TaskDAO.class); //Mock instance (defines behaviour manually)
		
		fakeTask =  ModelFactory.task();
		fakeTask.setDescription("fake task description");
		fakeTask.setPlace("fake task place");
		fakeTask.setTime(30);
		
		FieldUtils.writeField(taskController, "taskDao", taskDao, true); //Mock instance injected in the controller manually
	}
	
	@Test
	public void testGetTask() {
		when(taskDao.findById(1L)).thenReturn(fakeTask);
		
		Task retrievedTask = taskController.getById(1L);
		assertEquals(retrievedTask.getDescription(), fakeTask.getDescription());
		assertEquals(retrievedTask.getPlace(), fakeTask.getPlace());
		assertEquals(retrievedTask.getTime(), fakeTask.getTime());
		assertEquals(retrievedTask, fakeTask);
	}
	
	@Test
	public void testAccomplishTask() {
		taskController.accomplish(fakeTask);
		
		assertTrue(fakeTask.getAccomplished());
	}
	
	@Test
	public void testGetAllTask() {
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(fakeTask);
		when(taskDao.searchAllTasks()).thenReturn(tasks);
		
		List<Task> retrievedTasks = taskController.getAllTasks();
		assertEquals(retrievedTasks, tasks);
		assertEquals(retrievedTasks.get(0), fakeTask);
	}
	
	@Test
	public void testUpdateTask() {
		Task newTask = ModelFactory.task();
		newTask.setDescription("Updated task description");
		newTask.setPlace("Updated task place");
		
		taskController.updateTask(fakeTask, newTask);
		assertEquals(fakeTask.getDescription(), newTask.getDescription());
		assertEquals(fakeTask.getPlace(), newTask.getPlace());
		assertEquals(fakeTask.getDescription(), "Updated task description");
	}
}
