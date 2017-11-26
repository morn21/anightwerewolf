
//返回首页按钮
$("#roomPublic_returnIndexButton").click(function(){
    window.location.href = "/index";
});

//执行初始化
var iniRoomPublic = function () {
    $.ajax({
        type : "POST",  //提交方式
        url : "/room/loadRoom.json",//路径
        data : {},
        dataType : "json",
        success :  function(result){
            if(result.success){
                var user = result.data.user;
                var room = result.data.room;
                var roleCardList = result.data.roleCardList;

                $("#roomPublic_userName").html(" 用户：" + user.name);
                $("#roomPublic_roomName").html(" 房间：" + room.name);
                $("#roomPublic_peopleCount").html(room.peopleCount);
                var html = "";
                roleCardList.forEach(function (obj) {
                    html += "<span class='label label-info'>" + obj.name + "</span> ";
                });
                $("#roomPublic_roleCardList").html(html);
            } else{
                window.location.href = "/index";
            }
        }
    });
};

//初始化
$(function () {
    iniRoomPublic();
});