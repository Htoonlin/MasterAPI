<%-- 
    Document   : index
    Created on : 23-Jul-2016, 14:09:03
    Author     : Htoonlin
--%>

<%@page import="com.sdm.core.Globalizer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String accessToken = "";
    try{
        accessToken = session.getAttribute(Globalizer.SESSION_USER_TOKEN).toString();
    }catch(Exception e){
        accessToken = "";
    }
    
    if(accessToken.trim().length() <= 0){
        response.sendRedirect("login.jsp");
    }

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SUNDEW MASTER</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha256-ZosEbRLbNQzLpnKIkEdrPv7lOy9C27hHQ+Xp8a4MxAQ=" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href='http://mmwebfonts.comquas.com/fonts/?font=myanmar3' type="text/css"/>
        <style type="text/css">
            .item-box td, input, textarea{
                font-family: myanmar3;
            }
            .item-box{
                margin-bottom: 50px;
            }
            .item-box > .inner-body{
                height: 300px;
                min-height: 300px;
                max-height: 300px;
                overflow: auto;
                margin-bottom: 10px;
                border: 1px solid #AAA5AB;
                border-radius: 5px;
                box-shadow: 0 2px 3px 0 #999;
                padding: 10px 5px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>SUNDEW MASTER</h1>            
            <div class="input-group">
                <span class="input-group-addon">ACCESS TOKEN :</span>
                <input type="text" readonly="readonly" class="form-control" id="txtAccessToken" value="Bearer <%= accessToken %>" />
            </div>
            <hr />
            <div id="messageBox"></div>
            <div class="panel panel-info" id="mainPanel">
                <div class="panel-heading">
                    <div class="form-group row">
                        <div class="col-md-10">
                            <div class="input-group">
                                <span class="input-group-addon">API URL :</span>
                                <input type="url" class="form-control" id="txtAPIURL" />
                            </div>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-primary btn-block" id="btnNew" disabled="disabled" type="button" onclick="show_modal(0)">
                                <span class="glyphicon glyphicon-plus"></span> New
                            </button>
                        </div>
                    </div>                    
                    <div class="row">
                        <div class="col-md-3">
                            <div class="input-group">
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-search"></span>
                                </span>
                                <input type="text" class="form-control" id="txtFilter"  />                                
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="input-group">
                                <span class="input-group-addon">Page</span>
                                <input type="number" class="form-control" id="txtPage" value="0" />                                
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="input-group">
                                <span class="input-group-addon">Size</span>
                                <input type="number" class="form-control" id="txtSize" value="10" />                                
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="input-group">
                                <span class="input-group-addon">Sort</span>
                                <input type="text" class="form-control" id="txtSort" />                                
                            </div>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-info btn-block" type="button" onclick="select_data()">
                                <span class="glyphicon glyphicon-play"></span> Run
                            </button>
                        </div>
                    </div>

                </div>
                <div class="panel-body">                    
                    <div id="dataList"></div>
                    <div style="clear: both"></div>
                </div>                
            </div>
            <div class="modal fade" id="manageModal" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="modal-title">Data Manager</h4>                            
                        </div>
                        <div class="modal-body">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" id="btnSave" onclick="save_data()">
                                <span class="glyphicon glyphicon-floppy-disk"></span> Save
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div>
            <script type="text/javascript">
                var hide_columns = ['search', 'version', 'created_by', 'modified_by', 'created_at', 'modified_at'];
                var active_object = {};
                function show_message(type, title, message) {
                    var html = '<strong>' + title + '</strong>' + message;
                    $("div#messageBox").attr('class', 'alert alert-' + type).html(html);
                }
                function call_api(url_suffix, method, data, callback) {
                    var url = "<%= Globalizer.getBasePath(request) %>/api/";
                    url += $('input#txtAPIURL').val() + url_suffix;
                    $('label#lblWaiting').html("CONNECTING:" + url);
                    var request = {
                        url: url,
                        method: method,
                        data: JSON.stringify(data),
                        dataType: 'json',
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader('Authorization', $('input#txtAccessToken').val());
                            xhr.setRequestHeader('Content-Type', 'application/json');
                        },
                        success: callback,
                        error: function (data) {
                            var json = data.responseJSON;
                            $("div#mainPanel").attr('class', 'panel panel-danger');
                            show_message('danger', json.status, json.content.message);
                        }
                    };
                    if (method.toLowerCase() === 'get') {
                        delete request.data;
                    }
                    $.ajax(request);
                }
                function init() {
                    $('div#messageBox').removeAttr('class').html('');
                    $('div#dataList').html('');
                    build_structure();
                }
                function build_item(data) {
                    var html = '<div class="col-md-6 item-box">';
                    html += '<div class="inner-body"><table class = "table table-condensed table-striped">';
                    $.each(data, function (property, value) {
                        if ($.inArray(property, hide_columns) === -1) {
                            html += '<tr><th class="text-right">' + property + ' : </th><td>' + value + '</td></tr>';
                        }
                    });
                    html += '</table></div>';
                    html += '<div class="btn-group btn-group-justified">';
                    var id = 0;
                    if (data.id) {
                        id = data.id;
                    }
                    html += '<div class="btn-group"><button type="button" onclick="show_modal(' + id + ')" class="btn btn-warning">';
                    html += '<span class="glyphicon glyphicon-pencil"></span> Edit';
                    html += '</button></div>';
                    html += '<div class="btn-group"><button type="button" onclick="remove_data(' + id + ')" class="btn btn-danger">';
                    html += '<span class="glyphicon glyphicon-trash"></span> Remove';
                    html += '</button></div>';
                    html += '</div></div>';
                    return html;
                }
                function select_data() {
                    init();
                    var query = '?size=' + $('input#txtSize').val();
                    query += '&page=' + $('input#txtPage').val();

                    var filter = $('input#txtFilter').val();
                    if (filter.trim().length > 0) {
                        query += '&filter=' + filter;
                    }

                    var sort = $('input#txtSort').val();
                    if (sort.trim().length > 0) {
                        query += '&sort=' + sort;
                    }
                    call_api(query, 'get', null, function (data) {
                        var content = data.content;
                        $.each(content, function (index, item) {
                            $('div#dataList').append(build_item(item));
                        });
                    });
                }
                function build_structure() {
                    call_api('struct', 'get', null, function (response) {
                        $('div#manageModal div.modal-body').html('');
                        var numbers = ['int', 'integer', 'long', 'float', 'double', 'number'];
                        var boolean = ['bool', 'boolean'];
                        var date = ['date', 'datetime'];
                        var content = response.content;
                        $.each(content, function (index, property) {
                            if (property.request_name && $.inArray(property.request_name, hide_columns) === -1) {
                                var form_group = $('<div class="form-group">');
                                if ($.inArray(property.type.toLowerCase(), boolean) > -1) {
                                    form_group.append('<label><input type="checkbox" id="' + property.request_name + '"/> ' + property.label + '?</label>');
                                } else {
                                    form_group.append('<label for="' + property.request_name + '">' + property.label + ' : </label>');
                                    var input = $('<input type="text" class="form-control" id="' + property.request_name + '" placeholder="Enter ' + property.label + '"/>');
                                    if ($.inArray(property.type.toLowerCase(), numbers) > -1) {
                                        input.attr('type', 'number');
                                    } else if ($.inArray(property.type.toLowerCase(), date) > -1) {
                                        input.attr('type', 'date');
                                    } else if (property.type.endsWith('Entity')) {
                                        input.attr('type', 'text');
                                        input.attr('placeholder', 'Enter ' + property.name + ' Identity');
                                    }
                                    if (property.read_only === true) {
                                        input.attr('readonly', 'readonly');
                                    }
                                    form_group.append(input);
                                }
                                $('div#manageModal div.modal-body').append(form_group);
                            }
                        });

                        $('button#btnNew').prop('disabled', false);
                    });
                }
                function save_data() {
                    var processId = parseInt(active_object.id);
                    var process_data = {};
                    $.each(active_object, function (property, value) {
                        var input = $('div#manageModal div.modal-body #' + property);
                        if (input && ($.inArray(property, hide_columns) === -1)) {
                            if (input.prop('type') === 'checkbox') {
                                process_data[property] = input.prop('checked');
                            } else {
                                process_data[property] = input.val();
                            }
                        }
                    });
                    process_data['timestamp'] = (new Date()).getTime();
                    if (processId > 0) {
                        call_api(processId, 'put', process_data, function (response) {
                            if (response.code && response.code === 202) {
                                select_data();
                                $('div#manageModal').modal('hide');
                            }
                        });
                    } else {
                        call_api('', 'post', process_data, function (response) {
                            if (response.code && response.code === 201) {
                                select_data();
                                $('div#manageModal').modal('hide');
                            }
                        });
                    }
                }
                function remove_data(id) {
                    if (confirm('Are you sure to delete?')) {
                        call_api(id, 'delete', null, function (response) {
                            if (response.code && response.code === 202) {
                                select_data();
                                $('div#manageModal').modal('hide');
                            }
                        });
                    }
                }
                function show_modal(id) {
                    call_api(id, 'get', null, function (response) {
                        if (response && response.code && response.code === 200) {
                            active_object = response.content;
                            $('div#manageModal div.modal-body input').val('');
                            $('div#manageModal div.modal-body textarea').val('');
                            $('div#manageModal div.modal-body input[type="checkbox"]').prop('checked', false);

                            $.each(active_object, function (property, value) {
                                var input = $('div#manageModal div.modal-body #' + property);
                                if (input && ($.inArray(property, hide_columns) === -1)) {
                                    if (input.prop('type') === 'checkbox') {
                                        input.prop('checked', value);
                                    } else if (typeof value === "object") {
                                        input.val(value.id);
                                    } else {
                                        input.val(value);
                                    }
                                }
                            });
                        }
                    });

                    $('div#manageModal').modal('show');
                }

                $('document').ready(function () {
                    var url = $('input#txtAPIURL').val();
                    if (url.trim().length > 0) {
                        select_data();
                    }

                    $('input#txtAPIURL').keyup(function (e) {
                        if (e.keyCode === 13)
                        {
                            select_data();
                        }
                    });
                });
            </script>
        </div>
    </body>
</html>
