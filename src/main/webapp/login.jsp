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
        <title>MASTER LOGIN</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>  
        <link href="./css/auth.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="container">
            <div class="col-md-6 col-md-offset-3">
                <form class="form" role="form">     
                    <div id="auth-box" class="panel panel-default">
                        <div class="panel-heading">
                            <h1 class="panel-title text-center">SUNDEW MASTER API</h1>                            
                            <div class="text-center">
                                <a href="register.jsp">Are you a new user? Sign up!</a>                         
                            </div>
                        </div>
                        <div class="panel-body">                       
                            <div class="form-group">
                                <label class="control-label">E-mail :</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-envelope"></span>
                                    </span>
                                    <input type="email" class="form-control" placeholder="E-mail"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label">Password :</label>
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-lock"></span>
                                    </span>
                                    <input type="password" class="form-control" placeholder="Password" />
                                </div>
                            </div>
                            <div class="form-group">
                                <a href="reset-password.jsp">Forget your password?</a>   
                            </div>
                        </div>
                        <div class="panel-footer">                                                            
                            <button type="submit" class="btn btn-success btn-block">
                                Go to login
                                <span class="glyphicon glyphicon-log-in"></span>
                            </button>                            
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <script type="text/javascript">
            
        </script>
    </body>
</html>
