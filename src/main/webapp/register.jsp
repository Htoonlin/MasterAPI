<%-- 
    Document   : login.jsp
    Created on : 05-Oct-2016, 11:34:24
    Author     : Htoonlin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REGISTRATION</title>
        <link
            href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
            rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-1.12.4.min.js"
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>
        <script type="text/javascript"
        src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <link href="./css/auth.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="container">
            <div class="col-md-6 col-md-offset-3">
                <form class="form" id="frmRegister" role="form">
                    <div id="auth-box" class="panel panel-default">
                        <div class="panel-heading">
                            <h1 class="panel-title text-center">REGISTRATION FORM</h1>
                            <div class="text-center">
                                <a href="login.jsp">Already register? Go to Login!</a>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div id="loading-box">
                                <img src="./img/loading.svg" alt="loading"
                                     style="margin: 350px auto 0; display: block;" />
                            </div>
                            <div class="form-group">
                                <label class="control-label">Name : </label> <span
                                    class="label label-danger" id="msgName"></span>
                                <div class="input-group">
                                    <span class="input-group-addon"> <span
                                            class="glyphicon glyphicon-user"></span>
                                    </span> <input type="text" id="txtFullName" class="form-control"
                                                   placeholder="Full name" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label">User Name : </label> <span
                                    class="label label-danger" id="msgUserName"></span>
                                <div class="input-group">
                                    <span class="input-group-addon"> <span
                                            class="glyphicon glyphicon-envelope"></span>
                                    </span> <input type="text" id="txtUserName" class="form-control"
                                                   placeholder="User Name" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label">E-mail : </label> <span
                                    class="label label-danger" id="msgEmail"></span>
                                <div class="input-group">
                                    <span class="input-group-addon"> <span
                                            class="glyphicon glyphicon-envelope"></span>
                                    </span> <input type="email" id="txtEmail" class="form-control"
                                                   placeholder="E-mail" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label">Password : </label> <span
                                    class="label label-danger" id="msgPassword"></span>
                                <div class="input-group">
                                    <span class="input-group-addon"> <span
                                            class="glyphicon glyphicon-lock"></span>
                                    </span> <input type="password" id="txtPassword" class="form-control"
                                                   placeholder="Password" />
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <button type="submit" class="btn btn-block btn-success">
                                Register Now <span class="glyphicon glyphicon-pencil"></span>
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
            $('#frmRegister').submit(function (event) {
                event.preventDefault();
                var data = {
                    "display_name": $('#txtFullName').val(),
                    "user_name":$('#txtUserName').val(),
                    "email": $('#txtEmail').val(),
                    "password": $('#txtPassword').val(),
                    "timestamp": (new Date()).getTime()
                };
                call_api("api/auth/register/", "post", data, function (res) {
                    alert(res.content.message);
                    window.location.href = "login.jsp";
                }, function (res) {
                    var json = res.responseJSON;
                    if (json.code == 200) {
                        $("#server-message").addClass("alert alert-danger")
                                .html('<strong>' + json.status + '</strong>' + json.content.message);
                    } else {
                        $.each(json.content, function (prop, value) {
                            if (prop === "display_name") {
                                $("span#msgName").html(value.message);
                            } else if (prop === "email") {
                                $("span#msgEmail").html(value.message);
                            } else if (prop === "password") {
                                $("span#msgPassword").html(value.message);
                            }
                        });
                    }
                });
            });
        </script>
    </body>
</html>
