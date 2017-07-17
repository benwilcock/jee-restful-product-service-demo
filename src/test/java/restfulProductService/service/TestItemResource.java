package restfulProductService.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import restfulProductService.dataAccess.ItemEntityDaoImpl;
import restfulProductService.objectModel.Item;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class TestItemResource extends TestCase {

	private Item item = null;
	private ItemResource resource = null;
	private ItemEntityDaoImpl dao = null;
	private String id = "1";
	private String count = "1";
	private List<Item> results;
	
	public TestItemResource() {
		super();
	}
	
	@Before
	public void setUp(){
		
		this.item = new Item();
		this.item.setId(Long.valueOf(this.id));
		this.item.setName("Wrench");
		this.item.setCost(2);
		this.item.setPrice(2);

		this.resource = new ItemResource();
		this.dao = mock(ItemEntityDaoImpl.class);
		resource.setItemDao(dao);
		results = new ArrayList<Item>();
		results.add(this.item);
	}
	
	@Test
	public void testCreate() throws Exception{
		when(dao.create(this.item)).thenReturn(new Item());
		resource.create(this.item);
		verify(dao).create(this.item);
	}

	@Test
	public void testRead() throws Exception{
		
		when(dao.read(Long.valueOf(id))).thenReturn(this.item);
		resource.read(id);
		verify(dao).read(Long.valueOf(id));
	}
	
	@Test
	public void testUpdate() throws Exception{
		when(dao.update(this.item)).thenReturn(this.item);
		resource.update(this.item);
		verify(dao).update(this.item);
	}
	
	@Test
	public void testDelete() throws Exception{
		when(dao.read(Long.valueOf(id))).thenReturn(this.item);
		resource.delete(id);
		verify(dao).read(Long.valueOf(id));
		verify(dao).delete(this.item);
	}
	
	@Test
	public void testList() throws Exception{
		when(dao.readAll(0, 0)).thenReturn(this.results);
		resource.read(0, 0);
		verify(dao).readAll(0, 0);
	}
	
	@Test
	public void testCount() throws Exception{
		when(dao.count()).thenReturn(Long.valueOf(this.count));
		String count = resource.count();
		verify(dao).count();
		assertEquals("Current number of Item Representations: " + this.count + ".", count);
	}
}
