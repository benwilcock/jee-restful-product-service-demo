package restfulProductService.service;

import java.util.*;
import java.util.logging.Logger;

import javax.ejb.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import restfulProductService.dataAccess.*;
import restfulProductService.objectModel.*;
import static javax.ws.rs.core.MediaType.*;

@Stateless
@Path("/item")
public class ItemResource {

	@EJB
	private ItemEntityDaoImpl itemDao;
	
	private static Logger log = Logger.getLogger(ItemResource.class.getName());
	
	
	public void setItemDao(ItemEntityDaoImpl itemDao) {
		this.itemDao = itemDao;
	}
	

	@Produces( { APPLICATION_XML, APPLICATION_JSON })
	@Consumes( { APPLICATION_XML, APPLICATION_JSON })
	@POST
	@Path("/")
	public Item create(Item newItem) throws Exception{
		
		log.info("Creating Item: "+ newItem.getName());
		Item item = itemDao.create(newItem);
		log.info("Create finished.");
		return item;
	}
	
	@GET
	@Produces( { APPLICATION_XML, APPLICATION_JSON })
	@Path("/{id}")
	public Item read(@PathParam("id") String id) throws Exception{
		log.finest("Reading Item for Id: " +id);
		Item item = itemDao.read(new Long(id));
		log.fine("Reading Item ["+id+"] Done.");
		return item;
	}
	
	@Produces( { APPLICATION_XML, APPLICATION_JSON })
	@Consumes( { APPLICATION_XML, APPLICATION_JSON })
	@PUT
	@Path("/{id}")
	public Item update(Item item) throws Exception {
		log.finest("Updating Item: " + item.getName());
		item = itemDao.update(item);
		log.fine("Updating Item ["+item.getId()+"] Done.");
		return item;
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") String id) throws Exception {
		
		log.finest("Deleting Item: " + id);
		Item item = itemDao.read(new Long(id));
		
		if(null != item){
			log.fine("Deleting Item: " + id);
			itemDao.delete(item);
		}	
		
		log.fine("Deleting Item ["+id+"] Done.");
		return;
	}
	
	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("list/{offset}/{limit}")
	public List<Item> read(@PathParam("offset") int offset,
			@PathParam("limit") int limit) throws Exception{
		
		log.finest("Reading all Items: Offset." +offset+ " limit."+limit);
		List<Item> results = itemDao.readAll(offset, limit);
		log.fine("Reading all Items ["+results.size()+"] Done.");
		return results;
	}

	@GET
	@Produces( { MediaType.TEXT_PLAIN })
	@Path("count")
	public String count() throws Exception{
		
		log.finest("Performing Count");
		StringBuffer count = new StringBuffer();
		count.append("Current number of Item Representations: ");
		count.append(itemDao.count());
		count.append(".");
		log.fine("Returning Count of Items ["+count+"]");
		return count.toString();
	}
}
