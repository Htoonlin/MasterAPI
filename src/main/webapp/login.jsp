<%-- 
    Document   : login.jsp
    Created on : 05-Oct-2016, 11:34:24
    Author     : Htoonlin
--%>

<%@page import="com.sdm.core.Setting"%>
<%@page import="com.sdm.Constants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
        final String client_id = Setting.getInstance().get(Setting.FB_API_ID);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MASTER LOGIN</title>
        <link
            href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
            rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-1.12.4.min.js"
                integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ="
        crossorigin="anonymous"></script>
        <script type="text/javascript"
        src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <link
            href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
            type="text/css" rel="stylesheet" />
        <link href="./css/auth.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="container">
            <div class="col-md-6 col-md-offset-3">
                <form class="form" role="form" id="frmLogin">
                    <div id="auth-box" class="panel panel-default">
                        <div class="panel-heading">
                            <h1 class="panel-title text-center">SUNDEW MASTER API</h1>
                            <div class="text-center">
                                <a href="register.jsp">Are you a new user? Sign up!</a>
                            </div>
                        </div>
                        <div class="panel-body">
                            <div id="loading-box">
                                <img src="./img/loading.svg" alt="loading"
                                     style="margin: 350px auto 0; display: block;" />
                            </div>
                            <div id="server-message"></div>
                            <div class="form-group">
                                <label class="control-label">E-mail :</label> <span
                                    class="label label-danger" id="msgEmail"></span>
                                <div class="input-group">
                                    <span class="input-group-addon"> <span
                                            class="glyphicon glyphicon-envelope"></span>
                                    </span> <input type="email" id="txtEmail" class="form-control"
                                                   placeholder="E-mail" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label">Password :</label> <span
                                    class="label label-danger" id="msgPassword"></span>
                                <div class="input-group">
                                    <span class="input-group-addon"> <span
                                            class="glyphicon glyphicon-lock"></span>
                                    </span> <input type="password" id="txtPassword" class="form-control"
                                                   placeholder="Password" />
                                </div>
                            </div>
                            <div class="form-group">
                                <a href="reset-password.jsp">Forget your password?</a>
                                <div class="pull-right">
                                    <div class="fb-login-button" data-max-rows="1" data-size="medium" 
                                         data-button-type="continue_with" data-show-faces="false" 
                                         data-scope="<%= Constants.Facebook.AUTH_SCOPE %>"
                                         data-auto-logout-link="false" onlogin="facebook_login()" 
                                         data-use-continue-as="true"></div>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <button type="submit" class="btn btn-success btn-block">
                                Go to login <span class="glyphicon glyphicon-log-in"></span>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Login process -->
        <script type="text/javascript">
            //Facebook Init
            (function (d, s, id) {
                var js, fjs = d.getElementsByTagName(s)[0];
                if (d.getElementById(id))
                    return;
                js = d.createElement(s);
                js.id = id;
                js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=<%= Constants.Facebook.API_VERSION %>&appId=<%= client_id %>";
                fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk'));

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

            //Facebook Login
            function facebook_login() {
                FB.getLoginStatus(function (response) {
                    console.log(response);
                    if (response.status === "connected" && response.authResponse.accessToken) {
                        var data = {
                            "access_token": response.authResponse.accessToken,
                            "device_id": navigator.userAgent,
                            "timestamp": (new Date()).getTime()
                        };

                        call_api("api/auth/facebook/", "post", data, function (res) {
                            window.location.href = "index.jsp";
                        }, function (res) {
                            var json = res.responseJSON;
                            $("#server-message").addClass("alert alert-danger").html(json.content.message);
                        });
                    }
                });
            }

            //Email Auth
            $('#frmLogin').submit(function (event) {
                event.preventDefault();
                $("#server-message").removeClass('alert alert-danger').html('');
                var data = {
                    "email": $('#txtEmail').val(),
                    "password": $('#txtPassword').val(),
                    "device_id": navigator.userAgent,
                    "timestamp": (new Date()).getTime()
                };
                call_api("api/auth/", "post", data, function (res) {
                    window.location.href = "index.jsp";
                }, function (res) {
                    var json = res.responseJSON;
                    if (json.status != 400) {
                        $("#server-message").addClass("alert alert-danger")
                                .html(json.content.message);
                    } else {
                        $.each(json.content, function (prop, value) {
                            if (prop === "email") {
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
