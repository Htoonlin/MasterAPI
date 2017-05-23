/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.core.resource;

import com.sdm.core.mysql.dao.AdminDAO;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.mysql.dao.ObjectDAO;
import com.sdm.core.mysql.dao.RestDAO;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.MapResponse;
import com.sdm.core.response.PaginationResponse;
import com.sdm.core.mysql.request.data.DataInsertRequest;
import com.sdm.core.request.QueryRequest;
import com.sdm.core.mysql.request.structure.PropertyRequest;
import com.sdm.core.mysql.request.structure.CloneRequest;
import com.sdm.core.mysql.request.structure.CreateRequest;
import com.sdm.core.mysql.request.data.DataRemoveRequest;
import com.sdm.core.mysql.request.data.DataUpdateRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 * Mysql Management by Rest.
 *
 * @author Htoonlin
 */
@Path("mysql")
public class MySQLResource extends DefaultResource {

    private static final Logger LOG = Logger.getLogger(MySQLResource.class.getName());

    @PathParam("table")
    private String table;

    /**
     * Retrieve table list.
     *
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getObjects() throws SQLException, IOException, ClassNotFoundException {
        AdminDAO dao = new AdminDAO();
        List<Map<String, Object>> objects = dao.fetchObjects();
        dao.closeConnection();
        return new DefaultResponse(new ListResponse(objects));
    }

    /**
     * Querying data from all table.
     *
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @POST
    @Path("query")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse query(QueryRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        RestDAO dao = new RestDAO();
        long total = dao.fetchTotal(request);
        List data = dao.fetch(request);
        dao.closeConnection();
        PaginationResponse response = new PaginationResponse(data, total, request.getPage(), request.getSize(), request);
        return new DefaultResponse(response);
    }

    /**
     * Default Pagination.
     *
     * @param pageId
     * @param pageSize
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @GET
    @Path("{table}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse insert(@DefaultValue("1") @QueryParam("page") int pageId,
            @DefaultValue("10") @QueryParam("size") int pageSize) throws SQLException, IOException, ClassNotFoundException {
        QueryRequest request = new QueryRequest(table);
        request.setPage(pageId);
        request.setSize(pageSize);
        return query(request);
    }

    /**
     * Insert new data.
     *
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @POST
    @Path("{table}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse insert(DataInsertRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }
        RestDAO dao = new RestDAO();
        dao.insert(table, request.getData(), request.isGeneratedId());
        dao.closeConnection();
        MapResponse<String, Object> response = new MapResponse<>();
        response.putAll(request.getData());
        return new DefaultResponse(response);
    }

    /**
     * Update data by conditions.
     *
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @PUT
    @Path("{table}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse update(DataUpdateRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }
        RestDAO dao = new RestDAO();
        int effected = dao.update(table, request.getData(), request.getConditions());
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Effected " + effected + " rows.");
        return new DefaultResponse(message);
    }

    /**
     * Delete data by condition.
     *
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @DELETE
    @Path("{table}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse update(DataRemoveRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }
        RestDAO dao = new RestDAO();
        int effected = 0;
        if (request.isTruncate()) {
            dao.truncate(table);
        } else {
            effected = dao.remove(table, request.getConditions());
        }
        dao.closeConnection();
        
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Effected " + effected + " rows.");
        return new DefaultResponse(message);
    }

    /**
     * Create new object
     *
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
        public IBaseResponse create(CreateRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        ObjectDAO dao = new ObjectDAO();
        dao.create(request);
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Create a new object <" + request.getName() + "> successful.");
        return new DefaultResponse(message);
    }

    /**
     * Change object name.
     *
     * @param newName
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @PUT
    @Path("{table}/rename/{newName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse rename(@PathParam("newName") String newName) throws SQLException, IOException, ClassNotFoundException {
        ObjectDAO dao = new ObjectDAO();
        dao.rename(table, newName);
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", table + " object changed to new name " + newName + ".");
        return new DefaultResponse(message);
    }

    /**
     * Clone object.
     *
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @POST
    @Path("{table}/clone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse clone(CloneRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }
        ObjectDAO dao = new ObjectDAO();
        dao.clone(table, request.getDestName(), request.isTemporary(), request.isDataInclude());
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Cloned object to " + request.getDestName()
                + " from " + table + ".");
        return new DefaultResponse(message);
    }

    /**
     * Remove Object
     *
     * @param isTemporary
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @DELETE
    @Path("{table}/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse remove(@DefaultValue("false") @QueryParam("temp") boolean isTemporary) throws SQLException, IOException, ClassNotFoundException {
        ObjectDAO dao = new ObjectDAO();
        dao.remove(table, isTemporary);
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Remove object <" + table + "> successful.");
        return new DefaultResponse(message);
    }

    /**
     * Retrieve property list.
     *
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @GET
    @Path("{table}/property")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse descObject() throws SQLException, IOException, ClassNotFoundException {
        AdminDAO dao = new AdminDAO();
        List properties = dao.descObject(table);
        dao.closeConnection();
        return new DefaultResponse(new ListResponse(properties));
    }

    /**
     * Add new property inn object
     *
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @POST
    @Path("{table}/property")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse addProperty(PropertyRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        ObjectDAO dao = new ObjectDAO();
        dao.addProperty(table, request.getProperty(), request.getAfter(), request.isFirst());
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Add new property in " + table + " has been successful.");
        return new DefaultResponse(message);
    }

    /**
     * Edit property from object
     *
     * @param property
     * @param request
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @PUT
    @Path("{table}/property/{property}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse editProperty(
            @PathParam("property") String property,
            PropertyRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        ObjectDAO dao = new ObjectDAO();
        dao.editProperty(table, property,
                request.getProperty(), request.getAfter(), request.isFirst());
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Edit property in " + table + " has been successful.");
        return new DefaultResponse(message);
    }

    /**
     * Remove property from object
     *
     * @param property
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @DELETE
    @Path("{table}/property/{property}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse editProperty(@PathParam("property") String property) throws SQLException, IOException, ClassNotFoundException {
        ObjectDAO dao = new ObjectDAO();
        dao.dropProperty(table, property);
        dao.closeConnection();
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Remove property in " + table + " has been successful.");
        return new DefaultResponse(message);
    }    
}
