/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.master.resource;

import com.sdm.core.mysql.dao.AdminDAO;
import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.core.mysql.dao.ObjectDAO;
import com.sdm.core.mysql.dao.RestDAO;
import com.sdm.core.mysql.model.ObjectModel;
import com.sdm.core.response.ListResponse;
import com.sdm.core.response.MapResponse;
import com.sdm.core.response.PaginationResponse;
import com.sdm.master.request.object.QueryRequest;
import com.sdm.master.request.object.AddPropertyRequest;
import com.sdm.master.request.object.CloneRequest;
import com.sdm.master.request.object.RenameRequest;
import com.sdm.master.request.object.CreateRequest;
import com.sdm.master.request.object.EditPropertyRequest;
import com.sdm.master.request.object.RemovePropertyRequest;
import com.sdm.master.request.object.RemoveRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Path("mysql/object")
public class ObjectResource extends DefaultResource {

    private static final Logger LOG = Logger.getLogger(ObjectResource.class.getName());

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public IBaseResponse getPaging() throws SQLException, IOException, ClassNotFoundException {
        AdminDAO dao = new AdminDAO();
        List<ObjectModel> objects = dao.fetchObjects();
        return new DefaultResponse(new ListResponse(objects));
    }

    @GET
    @Path("query/{table}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse insert(@PathParam("table") String table,
            @DefaultValue("1") @QueryParam("page") int pageId,
            @DefaultValue("10") @QueryParam("size") int pageSize) throws SQLException, IOException, ClassNotFoundException {
        QueryRequest request = new QueryRequest(table);
        request.setPage(pageId);
        request.setSize(pageSize);
        return query(request);
    }

    @POST
    @Path("insert/{table}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse insert(@PathParam("table") String table,
            @DefaultValue("false") @QueryParam("auto") boolean isAutoGeneratedID,
            Map<String, Object> data) throws SQLException, IOException, ClassNotFoundException {
        RestDAO dao = new RestDAO();
        dao.insert(table, data, isAutoGeneratedID);
        MapResponse<String, Object> response = new MapResponse<>();
        response.putAll(data);
        return new DefaultResponse(response);
    }

    @POST
    @Path("update/{table}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse update(@PathParam("table") String table,
            @DefaultValue("false") @QueryParam("auto") boolean isAutoGeneratedID,
            Map<String, Object> data) throws SQLException, IOException, ClassNotFoundException {
        RestDAO dao = new RestDAO();
        dao.insert(table, data, isAutoGeneratedID);
        MapResponse<String, Object> response = new MapResponse<>();
        response.putAll(data);
        return new DefaultResponse(response);
    }

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
        PaginationResponse response = new PaginationResponse(data, total, request.getPage(), request.getSize(), request);
        return new DefaultResponse(response);
    }

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
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Create a new object <" + request.getName() + "> successful.");
        return new DefaultResponse(message);
    }

    @POST
    @Path("rename")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse rename(RenameRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }
        ObjectDAO dao = new ObjectDAO();
        dao.rename(request.getOldName(), request.getNewName());
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", request.getOldName() + " object changed to new name"
                + request.getNewName() + ".");
        return new DefaultResponse(message);
    }

    @POST
    @Path("clone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse clone(CloneRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }
        ObjectDAO dao = new ObjectDAO();
        dao.clone(request.getSource(), request.getDestination(), request.isTemporary(), request.isDataInclude());
        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Cloned object to " + request.getDestination()
                + " from " + request.getSource() + ".");
        return new DefaultResponse(message);
    }

    @POST
    @Path("remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse remove(RemoveRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        ObjectDAO dao = new ObjectDAO();
        dao.remove(request.getName(), request.isTemporary());

        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Remove object <" + request.getName() + "> successful.");
        return new DefaultResponse(message);
    }

    @POST
    @Path("property/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse addProperty(AddPropertyRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        ObjectDAO dao = new ObjectDAO();
        dao.addProperty(request.getObjectName(), request.getProperty(), request.getAfter(), request.isFirst());

        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Add new property in " + request.getObjectName() + " has been successful.");
        return new DefaultResponse(message);
    }

    @POST
    @Path("property/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse editProperty(EditPropertyRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        ObjectDAO dao = new ObjectDAO();
        dao.editProperty(request.getObjectName(), request.getOldName(),
                request.getProperty(), request.getAfter(), request.isFirst());

        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Edit property in " + request.getObjectName() + " has been successful.");
        return new DefaultResponse(message);
    }

    @POST
    @Path("property/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public IBaseResponse editProperty(RemovePropertyRequest request) throws SQLException, IOException, ClassNotFoundException {
        if (!request.isValid()) {
            return new ErrorResponse(request.getErrors());
        }

        ObjectDAO dao = new ObjectDAO();
        dao.dropProperty(request.getObjectName(), request.getPropertyName());

        MessageResponse message = new MessageResponse(200, ResponseType.SUCCESS,
                "MYSQL_SUCCESS", "Remove property in " + request.getObjectName() + " has been successful.");
        return new DefaultResponse(message);
    }
}
