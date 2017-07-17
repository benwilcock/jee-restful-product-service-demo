package restfulProductService.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import javax.persistence.*;

import org.junit.Before;
import org.junit.Test;

import restfulProductService.objectModel.*;
import restfulProductService.service.ItemResource;

public class TestItemEntityDaoImpl extends TestCase {
	
	private Item item = null;
	private ItemEntityDaoImpl dao = null;
	private EntityManager mockem = null;
	private Query mockQry = null;
	private List<Item> results = null;
	private String id = "1";
	
	@Before
	public void setUp(){
		
		this.item = new Item();
		this.item.setId(Long.valueOf(this.id));
		this.item.setName("Wrench");
		this.item.setCost(2);
		this.item.setPrice(4);
		this.results = new ArrayList<Item>();
		this.results.add(this.item);

		this.mockem = mock(EntityManager.class);
		this.mockQry = mock(Query.class);
		
		this.dao = new ItemEntityDaoImpl();
		this.dao.setEm(this.mockem);
	}

	@Test
	public void testDaoCreate() throws Exception{

		dao.create(this.item);
		verify(mockem).persist(this.item);
	}
	
	@Test
	public void testDaoRead() throws Exception
	{	
		when(mockem.find(Item.class, Long.valueOf(id))).thenReturn(item);
		dao.read(Long.valueOf(id));
		verify(mockem).find(Item.class, Long.valueOf(id));
	}
	
	@Test
	public void testDaoUpdate() throws Exception{
		
		Item updatedItem = new Item();
		updatedItem.setId(1);
		updatedItem.setName("Wrench");
		updatedItem.setCost(2);
		updatedItem.setPrice(2);
		Item mockItem = mock(Item.class);
		
		when(mockem.find(Item.class, updatedItem.getId())).thenReturn(mockItem);
		dao.update(updatedItem);
		verify(mockItem).setName(updatedItem.getName());
		verify(mockItem).setCost(updatedItem.getCost());
		verify(mockItem).setPrice(updatedItem.getPrice());
		verify(mockem).merge(mockItem);
		
	}
	
	@Test
	public void testDaoDelete() throws Exception{
		dao.delete(item);
		verify(mockem).remove(item);
		verify(mockem).flush();
	}
	
	@Test
	public void testReadAllOutOfBounds() throws Exception
	{
		int offset = -1;
		int limit = 51;
		when(mockem.createNativeQuery("SELECT * FROM Item it", Item.class)).thenReturn(mockQry);
		when(mockQry.getResultList()).thenReturn(this.results);
		dao.readAll(offset, limit);
		offset = 0;
		limit = 50;
		verify(mockem).createNativeQuery("SELECT * FROM Item it", Item.class);
		verify(mockQry).setFirstResult(offset);
		verify(mockQry).setMaxResults(offset+limit);
		verify(mockQry).getResultList();
	}
	
	@Test
	public void testReadAllOutOfBounds2() throws Exception
	{
		int offset = 1000;
		int limit = -1;
		when(mockem.createNativeQuery("SELECT * FROM Item it", Item.class)).thenReturn(mockQry);
		when(mockQry.getResultList()).thenReturn(this.results);
		dao.readAll(offset, limit);
		limit = 50;
		verify(mockem).createNativeQuery("SELECT * FROM Item it", Item.class);
		verify(mockQry).setFirstResult(offset);
		verify(mockQry).setMaxResults(offset+limit);
		verify(mockQry).getResultList();
	}
	
	@Test
	public void testReadAllInBounds() throws Exception
	{
		int offset = 0;
		int limit = 50;
		when(mockem.createNativeQuery("SELECT * FROM Item it", Item.class)).thenReturn(mockQry);
		when(mockQry.getResultList()).thenReturn(this.results);
		dao.readAll(offset, limit);
		verify(mockem).createNativeQuery("SELECT * FROM Item it", Item.class);
		verify(mockQry).setFirstResult(offset);
		verify(mockQry).setMaxResults(offset+limit);
		verify(mockQry).getResultList();
	}
	
	@Test
	public void testCount() throws Exception{
		
		Long count = new Long(1);
		
		when(mockem.createQuery("SELECT COUNT(i.id) FROM Item i")).thenReturn(mockQry);
		when(mockQry.getSingleResult()).thenReturn(count);
		dao.count();
		verify(mockem).createQuery("SELECT COUNT(i.id) FROM Item i");
		verify(mockQry).getSingleResult();
	}
}
