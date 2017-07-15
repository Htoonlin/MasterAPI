<%-- 
    Document   : auth-message
    Created on : 11-Jan-2017, 13:56:37
    Author     : Htoonlin
--%>

<%@page import="com.sdm.core.Globalizer"%>
<%@page import="com.sdm.core.Setting"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Forget Password</title>
</head>
<body>
	<div
		style="box-shadow: 3px 3px 5px #666; border: 1px solid #dabb74; border-radius: 3px; margin: 28px; font-family: arial;">
		<div
			style="color: #c35a00; background-color: #fbf2db; border-bottom: 1px solid #c5a456; padding: 20px 50px; text-align: center;">
			<h1
				style="margin: 20px 50px 10px; font-size: 21px; border-bottom: 3px solid #c97800; padding-bottom: 20px; font-weight: 500;">Forget
				Password Request</h1>
			<p>Have you requested to reset password?</p>
		</div>
		<div
			style="margin: 20px 50px; padding: 28px 50px; background: #FFF; font-size: 11pt;">
			<p>
				Hi <i>${user}</i>,
			</p>
			<p>Have you requested to reset password?</p>
			<p style="">
				<strong>No!</strong> : Delete this email because someone is trying
				to hack your account.
			</p>
			<p>
				<strong>Yes!</strong> : Click the following link to reset password
				your account:
			</p>
			<p style="margin: 25px 0px;">
				<a
					href="<%= Globalizer.getBasePath(request) %>/reset-password.jsp?token=${token}"
					style="text-decoration: none; color: #fff; padding: 8px 20px; background: #FF7500; border: 1px solid #EA9D2A; font-weight: bold;">
					Click here to reset your account now! </a>
			</p>
			<p>
				This reset button will be expired in next <b>${expire}</b> minutes.
			</p>
			<code style="color: #f55735;">"Never give up to be a warrior."</code>
		</div>
		<div
			style="color: #bfbbb9; background-color: #fbf2db; border-top: 1px solid #c5a456; padding: 20px 50px; text-align: center; font-size: 10pt;">
			<p>
				Copyright Â© 2011 - ${current_year} by <a
					style="color: #71bcff; text-decoration: none; font-size: 10pt;"
					href="http://www.sundewmyanmar.com">SUNDEW MYANMAR</a>. <br>
				All rights reserved.
			</p>
		</div>
	</div>
</body>
</html>