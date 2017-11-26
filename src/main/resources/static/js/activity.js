
//获得get参数
var getGetQueryString = function (name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};

//执行初始化
var ini = function(){
    var activityId = getGetQueryString("id");
    $("#activityId").html(activityId);
};

//初始化
$(function () {
    ini();
});