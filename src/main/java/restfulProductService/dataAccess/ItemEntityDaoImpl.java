package restfulProductService.dataAccess;

import java.util.*;
import java.util.logging.Logger;

import javax.persistence.*;
import javax.ejb.*;

import restfulProductService.objectModel.Item;

@Stateless
public class ItemEntityDaoImpl {

	@PersistenceContext
	private EntityManager em;
	
	private static Logger log = Logger.getLogger(ItemEntityDaoImpl.class.getName());


	public void setEm(EntityManager em) {
		this.em = em;
	}


	/**
	 * Persists an Item in the database using JPA
	 */
	public Item create(Item entity) throws Exception {
		
		log.finest("Creating Item: " + entity.getName());
		em.persist(entity);
		log.fine("Item Created: {" + entity.getId() + "} " + entity.getName());
		return entity;
	}
	
	
	public Item read(Long primaryKey) throws Exception {
		log.finest("Reading Item: " +primaryKey);
		Item item = em.find(Item.class, primaryKey);
		log.fine("Item Read.");
		return item;
	}
	
	public Item update(Item entity) throws Exception {
		log.finest("Upgating Item: " + entity.getName());
        Item attached = em.find(Item.class, entity.getId());
        // this can be done with Commons BeanUtils 
        attached.setName(entity.getName());
        attached.setCost(entity.getCost());
        attached.setPrice(entity.getPrice());
        em.merge(attached);
        log.fine("Item Updated.");
        return attached;
    }

	public void delete(Item entity) throws Exception {
		
		log.finest("Deleting Item: " + entity.getName());
		long id = entity.getId();
		em.remove(entity);
		em.flush();
		log.fine("Item '"+id+"' Deleted.");
	}
	
	

	@SuppressWarnings("unchecked")
	public List<Item> readAll(int offset, int limit) throws Exception {
		
		log.finest("ReadAll Items: " + offset + ", " + limit);
		if (offset < 0) {
			offset = 0;
		}
		if (limit <= 0 || limit > 50) {
			limit = 50;
		}

		Query query;
		query = em.createNativeQuery("SELECT * FROM "
				+ Item.class.getSimpleName() + " it", Item.class);
		query.setFirstResult(offset);
		query.setMaxResults(offset + limit);
		List<Item> items = query.getResultList();
		
		log.fine("ReadAll Items ["+items.size()+" Items] - Done.");
		return items;
	}



	public long count() throws Exception {
		
		log.fine("Count Items.");
		Query query = em.createQuery("SELECT COUNT(i.id) FROM " + Item.class.getSimpleName()+ " i");
		Number countResult = (Number) query.getSingleResult();
		long countValue = countResult.longValue();
		log.finest("Count of Items ["+countValue+" Items] - Done.");
		
		return countValue;
	}

}
