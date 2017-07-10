<%-- 
    Document   : login.jsp
    Created on : 05-Oct-2016, 11:34:24
    Author     : Htoonlin
--%>

<%@page import="com.sdm.core.Globalizer"%>
<%@page import="com.sdm.master.request.ActivateRequest"%>
<%@page import="com.sdm.core.util.SecurityManager;"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    boolean isReset = false;
    ActivateRequest activate = null;
    try {
        String tokenString = SecurityManager.base64Decode(request.getParameter("token"));
        activate = Globalizer.jsonMapper().readValue(tokenString, ActivateRequest.class);
        isReset = (activate != null);
    } catch (Exception e) {
        isReset = false;
    }
%>
<!DOCTYPE html>
<html>
    <head>        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MASTER LOGIN</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>  
        <link href="./css/auth.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="container">
            <div class="col-md-6 col-md-offset-3">
                <form class="form" role="form" id="frmResetPassword">     
                    <div id="auth-box" class="panel panel-default">
                        <div class="panel-heading">
                            <h1 class="panel-title text-center">Forget Password!</h1>                            
                            <div class="text-center">
                                <a href="login.jsp">Remember your password? Go to login!</a>                         
                            </div>
                        </div>
                        <div class="panel-body">    
                            <div id="loading-box">
                                <img src="./img/loading.svg" alt="loading" style="margin: 350px auto 0;display:block;" />
                            </div>
                            <% if (isReset) {%>
                            <div id="server-message"></div>
                            <div class="form-group">
                                <label class="control-label">New Password :</label> <span class="label label-danger" id="msgPassword"></span>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-lock"></span>
                                    </span>
                                    <input type="password" id="txtPassword" class="form-control" placeholder="Password"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label">Confirm-Password :</label> <span class="label label-danger" id="msgConfirmPassword"></span>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-lock"></span>
                                    </span>
                                    <input type="password" id="txtCPassword" class="form-control" placeholder="Confirm password"/>
                                </div>
                            </div>
                            <% } else { %>
                            <div class="form-group">
                                <label class="control-label">E-mail :</label> <span class="label label-danger" id="msgEmail"></span>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-envelope"></span>
                                    </span>
                                    <input type="email" id="txtEmail" class="form-control" placeholder="E-mail"/>
                                </div>
                            </div>
                            <% }%>
                        </div>
                        <div class="panel-footer">                                                            
                            <button type="submit" class="btn btn-warning btn-block">
                                <%= isReset ? "Change Password <span class=\"glyphicon glyphicon-ok\"></span>"
                                        : "Request Now <span class=\"glyphicon glyphicon-send\"></span>"%>
                            </button>                            
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <script type="text/javascript">
            $('document').ready(function () {
                $('#loading-box').hide();
            });
            function call_api(url, method, data, callback, error) {
                var ajax_request = {
                    url: url,
                    method: method,
                    data: (data != null) ? JSON.stringify(data) : null,
                    dataType: 'json',
                    beforeSend: function (xhr) {
                        $('#loading-box').show();
                        xhr.setRequestHeader('Content-Type', 'application/json');
                    },
                    success: callback,
                    error: error,
                    complete: function () {
                        $('#loading-box').hide();
                    }
                };

                $.ajax(ajax_request);
            }
            $('#frmResetPassword').submit(function (event) {
                event.preventDefault();
            <% if (isReset) {%>
                var new_pass = $("#txtPassword").val();
                var c_pass = $("#txtCPassword").val();
                if (new_pass != c_pass) {
                    $("#msgPassword").html("Password and confirm password must be match!");
                    $("#msgConfirmPassword").html("Password and confirm password must be match!");
                    return;
                }

                var data = {
                    "email": "<%= activate.getEmail()%>",
                    "old_password": "<%= activate.getDeviceId()%>",
                    "new_password": new_pass,
                    "timestamp": (new Date()).getTime(),
                    "extra": {"token": "<%= activate.getToken()%>"}
                };

                var url = "api/auth/resetPassword/";
                call_api(url, 'post', data, function (res) {
                    alert(res.content.message);
                    window.location.href = "login.jsp";
                }, function (res) {
                    var json = res.responseJSON;
                    $("#server-message").addClass("alert alert-danger")
                            .html('<strong>' + json.status + '</strong>' + json.content.message);
                });
            <% } else {%>
                var url = "api/auth/forgetPassword/" + $("#txtEmail").val();
                call_api(url, 'get', null, function (res) {
                    alert(res.content.message);
                    window.location.href = "login.jsp";
                }, function (res) {
                    var content = res.responseJSON.content;
                    $('#msgEmail').html(content);
                });
            <% }%>
            });
        </script>
    </body>
</html>
