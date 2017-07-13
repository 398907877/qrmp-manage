<%@page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.commons.lang3.math.NumberUtils"%>
<%@ page import="com.yanyan.core.util.TelnetAgent" %>
<%
  request.setCharacterEncoding("UTF-8");
  String uri = (String)request.getAttribute("javax.servlet.forward.request_uri");
  if(uri==null||uri.isEmpty()){
    uri = request.getRequestURI();
  }
  final String telnet_name = uri;//request.getRequestURI();

  TelnetAgent agent = TelnetAgent.getAgent(request.getSession().getId());

  if (request.getParameter("Receive") != null) {
    if (agent != null) {
      out.print(agent.receive());
    } else {
      out.print("[END]Connection disconnected, please refresh the page and relogin");
    }
    return;
  }

  if (request.getParameter("Send") != null) {
    if (agent != null) {
      agent.send(request.getParameter("command"));
    } else {
      out.print("[END]Connection disconnected, please refresh the page and relogin");
    }
    return;
  }
%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="robots" content="noindex">
<meta http-equiv="expires" content="0">
<meta http-equiv="pragma" content="no-cache">
<style type="text/css">
input.button {
  background-color: #c0c0c0;
  color: #666666;
  border: 1px solid #999999;
  margin: 5px 1px 5px 1px;
}

input.textfield {
  margin: 5px 1px 5px 1px;
}

input.button:Hover {
  color: #444444
}

table.filelist {
  background-color: #666666;
  width: 100%;
  border: 0px none #ffffff
}

.formular {
  margin: 1px;
  background-color: #ffffff;
  padding: 1em;
  border: 1px solid #000000;
}

.formular2 {
  margin: 1px;
}

th {
  background-color: #c0c0c0
}

tr.mouseout {
  background-color: #ffffff;
}

tr.mousein {
  background-color: #eeeeee;
}

tr.checked {
  background-color: #cccccc
}

tr.mousechecked {
  background-color: #c0c0c0
}

td {
  font-family: Verdana, Arial, Helvetica, sans-serif;
  font-size: 8pt;
  color: #666666;
}

td.message {
  background-color: #FFFF00;
  color: #000000;
  text-align: center;
  font-weight: bold
}

td.error {
  background-color: #FF0000;
  color: #000000;
  text-align: center;
  font-weight: bold
}

A {
  text-decoration: none;
}

A:Hover {
  color: Red;
  text-decoration: underline;
}

BODY {
  font-family: Verdana, Arial, Helvetica, sans-serif;
  font-size: 8pt;
  color: #666666;
}
</style>
<script src="../../scripts/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function input(event){
  if(event.keyCode==13){
    var command = $("#command").text();    
    send(command);    
  } else if(event.ctrlKey) {
    var command = String.fromCharCode(17) + String.fromCharCode(event.keyCode);    
    if(event.keyCode==67){//ctrl+c
      send(command);
    }
  }else{
    return true;
  }
  return false;
}

var commandQueue = {
  commands: [],
  offer: function(command){
    commands = new Array(command).concat(commands);
  },
  
  poll: function(){
    return commands.pop();
  },
  
  size: function(){
    return commands.length;
  }
};

function send(command){
  //commandQueue.size()
  $.ajax({
    type : "POST",          
    async : false,
    url : "<%=telnet_name%>?Send",
    data : {id : $("#id").val(), ip : $("#ip").val(), port: $("#port").val(), command : command},
    processData : true,
    cache : false,
    contentType : "application/x-www-form-urlencoded; charset=UTF-8",
    dataType : "text",
    success : function(data, textStatus, jqXHR) {
      receive();
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(textStatus);
    }
  });   
}

var wait = 0;
function receive(){
  $.ajax({
    type : "GET",          
    async : false,
    url : "<%=telnet_name%>?Receive",
    data : {id : $("#id").val(), ip : $("#ip").val(), port: $("#port").val()},
    processData : true,
    cache : false,
    contentType : "application/x-www-form-urlencoded; charset=UTF-8",
    dataType : "text",
    success : function(data, textStatus, jqXHR) {
      if($.trim(data).length==0){//没返回，继续接收        
        if(wait++<100){
          setTimeout(receive(), 1000);
        }
      }else{
        wait = 0;
        var resp = data.replace("\r\n", "\n");
        resp = resp.replace("\r", "\n");
        var aResp = resp.split("\n");
        var container = $("<div></div>");
        var div = null;
        for(var i=0;i<aResp.length;i++){   
          div = $("<div></div>");
          div.text(aResp[i]);
          if(i==aResp.length-1){
            var par = $("#command").parent();              
            par.html(par.text());//command remove
            div.append('<div id="command" style="display: inline;border: red" onkeydown="input(event)" contenteditable="true"></div>');
          }          
          $("#terminal").append(container.append(div));
          if(i==aResp.length-1){
            $("#command").focus();
          }
        }
        receive();
      }
    },
    error : function(XMLHttpRequest, textStatus, errorThrown) {
      alert(textStatus);
    }
  });
}
</script>
<body>
  <%    
    if ("Connect".equalsIgnoreCase(request.getParameter("Submit"))) {
      String ip = request.getParameter("ip");
      int port = NumberUtils.toInt(request.getParameter("port"), 23);
      String charset = request.getParameter("charset");
      agent = TelnetAgent.createAgent();
      agent.setCharset(charset);
      agent.connect(ip, port);
      TelnetAgent.putAgent(request.getSession().getId(), agent);      
  %>
  <form class="formular" action="<%=telnet_name%>" method="Post" name="receive">
    IP：<input type="text" id="ip" name="ip" value="<%=ip%>" readonly="readonly"> 端口：<input type="text" id="port" name="port" value="<%=port%>"> 编码：<input type="text" id="charset" name="charset" value="<%=charset%>"> <input type="submit" name="Submit" value="Disconnect"> <input type="button" name="focus" value="Command Input Focus" onclick="$('#command').focus()">
  </form>
  <div id="terminal" class="formular" style="height:100%;overflow-y:scroll"></div>
  <script>receive();</script>
  <%
    } else {
      if("Disconnect".equalsIgnoreCase(request.getParameter("Submit"))){
        if(agent!=null){
          agent.disconnect();
        }
        agent = null;
      }      
  %>
  <form class="formular" action="<%=telnet_name%>" method="Post" name="send">
    IP：<input type="text" id="ip" name="ip" value=""> 端口：<input type="text" id="port" name="port" value="23"> 编码：<input type="text" id="charset" name="charset" value="UTF-8"> <input type="submit" name="Submit" value="Connect">
  </form>
  <%
    }
  %>
</body>
</html>