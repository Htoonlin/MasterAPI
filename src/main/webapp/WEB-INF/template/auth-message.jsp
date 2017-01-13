<%-- 
    Document   : auth-message
    Created on : 11-Jan-2017, 13:56:37
    Author     : Htoonlin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${title}</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>  
        <link href="https://bot.sundewmyanmar.com/MasterAPI/css/auth.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div class="container">
            <div class="col-md-8 col-md-offset-2">                
                <div id="auth-box" class="panel panel-danger">
                    <div class="panel-heading">
                        <h1 class="panel-title text-center">${title}</h1>                            
                    </div>
                    <div style="font-size:15pt; padding:38px 20px 25px;" class="panel-body">                       
                        ${message}
                    </div>
                    <div class="panel-footer">                                                            
                        <p align="center">
                            Copyright © 2011 - ${current_year} by 
                            <a style="color: #71bcff;text-decoration:none; font-size:10pt;" href="http://www.sundewmyanmar.com">SUNDEW MYANMAR</a>. <br>
                            All rights reserved.
                        </p>                          
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>