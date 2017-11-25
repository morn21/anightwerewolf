var roleCardList = [];//角色卡列表

//导航1被点击时
$("#nav1").click(function(){
    $("#nav1").attr("class","active");
    $("#nav2").attr("class","");
    $("#navDiv1").css("display","block");
    $("#navDiv2").css("display","none");
});

//导航2被点击时
$("#nav2").click(function(){
    $("#nav1").attr("class","");
    $("#nav2").attr("class","active");
    $("#navDiv1").css("display","none");
    $("#navDiv2").css("display","block");
});

//点击进入房间按钮
$("#intoRoomButton").click(function(){
    $.ajax({
        type : "POST",  //提交方式
        url : "/room/intoRoom.json",//路径
        data : {
            name : $("#intoRoomName").val(),
            password : $("#intoRoomPassword").val()
        },
        dataType : "json",
        success :  function(result){
            if(result.success){
                window.location.href = "/room";
            } else{
                $("#errorMsgDiv").html(result.msg);
            }
        }
    });
});

//点击角色卡按钮
var clickRoleCardButton = function(id){
    $.each(roleCardList,function(i,obj){
        if(obj.id == id){
            if(obj.isSelected == 1){
                obj.isSelected = 0;
            } else {
                obj.isSelected = 1;
            }
        }
    });
    describeRoleCardList();//描绘角色卡列表
};

//描绘角色卡列表
var describeRoleCardList = function(){
    var html = "";
    var peopleCount = 0;
    $.each(roleCardList,function(i,obj){
        html += "<button type='button' class='btn marginTop10 ";
        if(obj.isSelected == 0){
            html += "btn-default";
        } else{
            html += "btn-primary";
            peopleCount += obj.peopleCount;//累计人数
        }
        html += "' onclick='clickRoleCardButton(\"" + obj.id + "\")'>";
        html += obj.name;
        if(obj.peopl$eCount == 2){
            html += "*2";
        }
        html += "</button> ";
    });
    $("#roleCardList").html(html);
    $("#peopleCount").html(peopleCount - 3);
};

//执行初始化
var ini = function () {
    $.ajax({
        type : "POST",  //提交方式
        url : "/roleCard/getAllList.json",//路径
        data : {},
        dataType : "json",
        success :  function(result){
            if(result.success){
                roleCardList = result.data;
                describeRoleCardList();//描绘角色卡列表
            }
        }
    });
};

//点击确认创建房间按钮事件
$("#confirmCreateRoomButton").click(function(){
    $.ajax({
        type : "POST",  //提交方式
        url : "/room/createRoom.json",//路径
        data : {
            password : $("#roomPassword").val(),
            roleCardListStr : JSON.stringify(roleCardList)
        },
        dataType : "json",
        success :  function(result){
            if(result.success){
                window.location.href = "/room";
            } else{
                $("#errorMsgDiv").html(result.msg);
            }
        }
    });
});

//初始化
$(function () {
    ini();
});