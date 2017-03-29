/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdm.mysql.resource;

import com.sdm.core.resource.DefaultResource;
import com.sdm.core.response.DefaultResponse;
import com.sdm.core.response.ErrorResponse;
import com.sdm.core.response.IBaseResponse;
import com.sdm.core.response.MessageResponse;
import com.sdm.core.response.ResponseType;
import com.sdm.mysql.dao.ObjectDAO;
import com.sdm.mysql.request.object.AddPropertyRequest;
import com.sdm.mysql.request.object.CloneRequest;
import com.sdm.mysql.request.object.RenameRequest;
import com.sdm.mysql.request.object.CreateRequest;
import com.sdm.mysql.request.object.EditPropertyRequest;
import com.sdm.mysql.request.object.RemovePropertyRequest;
import com.sdm.mysql.request.object.RemoveRequest;
import java.io.IOException;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Htoonlin
 */
@Path("mysql/object")
public class ObjectResource extends DefaultResource {

    private static final Logger LOG = Logger.getLogger(ObjectResource.class.getName());

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
