var seatNumList = [];//座号列表

//描绘座号列表
var describeSeatNumList = function(){
    var html = "";
    seatNumList.forEach(function(obj){
        html += "<button type='button' class='btn marginTop10 ";
        if(obj.isSelectedByOtherUser || obj.isSelected){
            html += "btn-primary";
        } else {
            html += "btn-default";
        }
        html += "' ";
        if(obj.isSelectedByOtherUser){
            html += "disabled='false'";
        } else {
            html += "onclick='clickSeatNumButton(" + obj.seatNum + ")'";
        }
        html += ">";
        html += "座号" + obj.seatNum;
        html += "</button> ";
    });
    $("#seatNumList").html(html);
};

//点击座号按钮
var clickSeatNumButton = function(seatNum){
    seatNumList.forEach(function(obj){
        if(seatNum == obj.seatNum){
            obj.isSelected = true;
        } else{
            obj.isSelected = false;
        }
    });
    describeSeatNumList();//描绘座号列表
};

//执行初始化
var ini = function(isNotCleanErrorMsg) {
    $.ajax({
        type : "POST",  //提交方式
        url : "/room/loadActivity.json",//路径
        data : {},
        dataType : "json",
        success :  function(result){
            if(result.success){
                var data = result.data;
                $("#activityId").html(data.activity.id);//设置显示场次ID
                if(data.isLockSeatNum){//当前用户已在本场次中锁定座号
                    window.location.href = "/activity?id=" + data.activity.id;
                } else{//当前用户还没有在本场次中锁定座号
                    seatNumList = [];//置空内存中的座号列表
                    var peopleCount = data.room.peopleCount;//房间人数
                    var activityDetailList = data.activityDetailList;//场次明细列表
                    for(var i = 0 ; i < peopleCount ; i++){
                        var seatNum = i + 1;
                        var isSelectedByOtherUser = false;
                        activityDetailList.forEach(function(obj){
                            if(seatNum == obj.seatNum){
                                isSelectedByOtherUser = true;
                            }
                        });
                        seatNumList.push({
                            seatNum : seatNum,//座号
                            isSelectedByOtherUser : isSelectedByOtherUser,//是否已被其它用户选择
                            isSelected : false//当前用户是否选择
                        });
                    }
                    describeSeatNumList();//描绘座号列表
                    if(isNotCleanErrorMsg == undefined || !isNotCleanErrorMsg){
                        $("#errorMsgDiv").html("");
                    }
                }
            }
        }
    });
};

//锁定座号按钮
$("#lockSeatNumButton").click(function(){
    var selectedSeatNum = 0;
    seatNumList.forEach(function(obj){
        if(obj.isSelected){
            selectedSeatNum = obj.seatNum;
        }
    });
    if(selectedSeatNum == 0){
        $("#errorMsgDiv").html("别闹了，快选一个座号！");
    } else {
        var activityId = $("#activityId").html();
        $.ajax({
            type : "POST",  //提交方式
            url : "/room/lockSeatNum.json",//路径
            data : {
                activityId : activityId,
                seatNum : selectedSeatNum
            },
            dataType : "json",
            success :  function(result){
                if(result.success){
                    window.location.href = "/activity?id=" + activityId;
                } else {
                    ini(true);
                    $("#errorMsgDiv").html(result.msg);
                }
            }
        });
    }
});

//点击刷新座号列表按钮
$("#refreshSeatNumListButton").click(function(){
    ini();
});

//初始化
$(function () {
    ini();
});