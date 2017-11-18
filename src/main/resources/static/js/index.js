var roleCardList = [];//角色卡列表

//获取缺省房间名称 格式：房间YYYYMMDDHH
/*var getDefaultRoomName = function(){
    var myDate = new Date();
    var seperator1 = "";
    var year = myDate.getFullYear();    //获取完整的年份(4位,1970-????)
    var month = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
    var date = myDate.getDate();        //获取当前日(1-31)
    var hour = myDate.getHours();       //获取当前小时数(0-23)
    var minute = myDate.getMinutes();     //获取当前分钟数(0-59)
    var second = myDate.getSeconds();     //获取当前秒数(0-59)
    if (month > 0 && month <= 9) {
        month = "0" + month;
    }
    if (date >= 0 && date <= 9) {
        date = "0" + date;
    }
    if (hour >= 0 && hour <= 9) {
        hour = "0" + hour;
    }
    if (minute >= 0 && minute <= 9) {
        minute = "0" + minute;
    }
    if (second >= 0 && second <= 9) {
        second = "0" + second;
    }
    return "房间" + year + seperator1 + month + seperator1 + date + seperator1 + hour + seperator1 + minute + seperator1 + second;
};*/

//点击角色卡按钮
var clickRoleCardButton = function(key){
    $.each(roleCardList,function(i,obj){
        if(obj.key == key){
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
        html += "' onclick='clickRoleCardButton(\"" + obj.key + "\")'>";
        html += obj.name;
        if(obj.peopleCount == 2){
            html += "*2";
        }
        html += "</button> ";
    });
    $("#roleCardList").html(html);
    $("#peopleCount").html(peopleCount - 3);
};

var ini = function () {
    //$("#roomName").val(getDefaultRoomName());//用当前日期当默认房间名
    $.ajax({
        type : "POST",  //提交方式
        url : "/roleCard/getAllList.json",//路径
        data : {},
        dataType : "json",
        success :  function(result){
            console.info("result:" + result);
            if(result.success){
                roleCardList = result.data;
                describeRoleCardList();//描绘角色卡列表
            }
        }
    });
};

//点击确认创建房间按钮事件
$("#confirmCreateRoomButton").click(function(){

});

//初始化
$(function () {
    ini();
});