<%-- 
    Document   : create-user
    Created on : 28-Mar-2017, 10:40:07
    Author     : Htoonlin
--%>

<%@page import="com.sdm.core.Setting"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Activate Account</title>
    </head>
    <body>
        <div
            style="box-shadow: 3px 3px 5px #666; border: 1px solid #dabb74; border-radius: 3px; margin: 28px; font-family: arial;">
            <div
                style="color: #c35a00; background-color: #fbf2db; border-bottom: 1px solid #c5a456; padding: 20px 50px; text-align: center;">
                <h1
                    style="margin: 20px 50px 10px; font-size: 21px; border-bottom: 3px solid #c97800; padding-bottom: 20px; font-weight: 500;">Welcome</h1>
            </div>
            <div
                style="margin: 20px 50px; padding: 28px 50px; background: #FFF; font-size: 11pt;">
                <p>
                    Hi <i>${name}</i>,
                </p>
                <p>Site admin created the account for you.</p>
                <p style="margin: 25px 0px;">
                    E-mail : <b>${email}</b> <br /> Password : <b>${password}</b>
                </p>
                <code style="color: #f55735;">"Never give up to be a warrior."</code>
            </div>
            <div
                style="color: #bfbbb9; background-color: #fbf2db; border-top: 1px solid #c5a456; padding: 20px 50px; text-align: center; font-size: 10pt;">
                <p>
                    Copyright © 2011 - ${current_year} by <a
                        style="color: #71bcff; text-decoration: none; font-size: 10pt;"
                        href="http://www.sundewmyanmar.com">SUNDEW MYANMAR</a>. <br>
                    All rights reserved.
                </p>
            </div>
        </div>
    </body>
</html>