

//返回房间按钮
$("#backRoomButtom").click(function () {
    window.location.href = "/room";
});

//显示结果列表
var showResultList = function (resultList) {
    var html = "";
    resultList.forEach(function (obj) {
        html += "<li class='list-group-item'>";
        var seatNum = obj.seatNum;//座号
        if(seatNum < 0){
            html += "底牌" + (0 - seatNum);
        } else if(seatNum > 0){
            html += "座号" + seatNum;
        }
        html += "：";
        html += "【" + obj.initialRoleCardName + "】";//起始角色牌
        html += "=>";
        html += "【" + obj.finalRoleCardName + "】";//最终角色牌
        if(seatNum > 0){
            html += "，";
            var voteNum = obj.voteNum;//投票号
            if(voteNum < 0){
                html += "投了【底牌" + (0 - voteNum) + "】";
            } else if(voteNum > 0){
                html += "投了【座号" + voteNum + "】";
            } else if(voteNum ==0){
                html += "弃票";
            }
        }
        html += "</li>";
    });
    $("#resultList").html(html);
};

//执行初始化
var ini = function(){
    var activityId = basic.getGetQueryString("id");
    debugger;
    $("#activityId").html(activityId);
    $.ajax({
        type : "POST",  //提交方式
        url : "/activity/findResultList.json",//路径
        data : {
            activityId : $("#activityId").html()
        },
        dataType : "json",
        success :  function(result){
            if(result.success){
                showResultList(result.data);//显示结果列表
            } else{
                $("#errorMsgDiv").html(result.msg);
            }
        }
    });
};

//初始化
$(function () {
    ini();
});