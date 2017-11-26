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
var ini = function () {
    $.ajax({
        type : "POST",  //提交方式
        url : "/room/loadSeatNum.json",//路径
        data : {},
        dataType : "json",
        success :  function(result){
            if(result.success){
                var data = result.data;
                $("#activityId").html(data.activity.id);//设置显示场次ID
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
            }
        }
    });
};

//锁定座号按钮
$("#lockSeatNumButton").click(function(){
    //未完待续。。。
});

//点击刷新座号列表按钮
$("#refreshSeatNumListButton").click(function(){
    ini();
});

//初始化
$(function () {
    ini();
});