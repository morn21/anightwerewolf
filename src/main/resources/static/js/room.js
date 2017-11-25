
//执行初始化
var ini = function () {
    $.ajax({
        type : "POST",  //提交方式
        url : "/room/loadRoom.json",//路径
        data : {},
        dataType : "json",
        success :  function(result){
            if(result.success){
                //roleCardList = result.data;
                //describeRoleCardList();//描绘角色卡列表
                console.log(result);
            }
        }
    });
};

//初始化
$(function () {
    ini();
});