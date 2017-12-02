
//返回首页按钮
$("#roomPublic_returnIndexButton").click(function(){
    window.location.href = "/index";
});

var roomPublic_showQrCodeDiv_display = false;
//显示二维码按钮
$("#roomPublic_showQrCodeButton").click(function(){
    if(roomPublic_showQrCodeDiv_display){
        $("#roomPublic_showQrCodeDiv").css("display","none");
        roomPublic_showQrCodeDiv_display = false;
    } else{
        $("#roomPublic_showQrCodeDiv").css("display","block");
        roomPublic_showQrCodeDiv_display = true;
    }
});

//生成二维码地址
var generateQrCodeUrl = function(roomId){
    var host = window.location.host;
    return "http://" + host + "/intoRoom?id=" + roomId;
};

//执行初始化
var iniRoomPublic = function () {
    $.ajax({
        type : "POST",  //提交方式
        url : "/room/loadRoom.json",//路径
        data : {},
        dataType : "json",
        success :  function(result){
            if(result.success){
                var room = result.data.room;
                var user = result.data.user;
                var roleCardList = result.data.roleCardList;
                $("#roomPublic_roomName").html(" " + room.name);
                $("#roomPublic_userName").html(" " + user.name);
                $("#roomPublic_roomQrCodeDiv").qrcode({
                    render: "table", //table方式
                    width: 200, //宽度
                    height:200, //高度
                    text: generateQrCodeUrl(room.id)//生成二维码地址
                });
                $("#roomPublic_peopleCount").html(room.peopleCount);
                var html = "";
                roleCardList.forEach(function (obj) {
                    html += "<span class='label label-info'>";
                    html += obj.name;
                    html += "</span> ";
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