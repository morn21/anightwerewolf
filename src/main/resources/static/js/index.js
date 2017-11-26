var roleCardList = [];//角色卡列表

//导航1按钮
$("#nav1").click(function(){
    $("#nav1").attr("class","active");
    $("#nav2").attr("class","");
    $("#navDiv1").css("display","block");
    $("#navDiv2").css("display","none");
});

//导航2按钮
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

//描绘角色卡列表
var describeRoleCardList = function(){
    var html = "";
    var cardCount = 0;
    roleCardList.forEach(function (obj) {
        html += "<button type='button' class='btn marginTop10 ";
        if(obj.isSelected == 0){
            html += "btn-default";
        } else {
            html += "btn-primary";
            cardCount += obj.cardCount;//累计人数
        }
        html += "' onclick='clickRoleCardButton(\"" + obj.id + "\")'>";
        html += obj.name;
        if(obj.cardCount > 1){
            html += "*" + obj.cardCount;
        }
        html += "</button> ";
    });
    $("#roleCardList").html(html);
    $("#peopleCount").html(cardCount - 3);
};

//点击角色卡按钮
var clickRoleCardButton = function(id){
    roleCardList.forEach(function(obj){
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

//执行初始化
var ini = function () {
    $.ajax({
        type : "POST",  //提交方式
        url : "/roleCard/loadRoleCardList.json",//路径
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

//点击确认创建房间按钮
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