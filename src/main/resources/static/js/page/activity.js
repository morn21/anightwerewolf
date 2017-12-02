var roleIdJson = {
    DOPPELGANGER : "DOPPELGANGER",//化身幽灵 orderNum:1
    WEREWOLF : "WEREWOLF",//狼人 orderNum:2
    MYSTIC_WOLF : "MYSTIC_WOLF",//狼先知 orderNum:4
    MINION : "MINION",//爪牙 orderNum:5
    DREAM_WOLF : "DREAM_WOLF",//贪睡狼 orderNum:6
    TANNER : "TANNER",//皮匠 orderNum:7
    SEER : "SEER",//预言家 orderNum:8
    APPRENTICE_SEER : "APPRENTICE_SEER",//见习预言家 orderNum:9
    ROBBER : "ROBBER",//强盗 orderNum:10
    WITCH : "WITCH",//女巫 orderNum:11
    TROUBLEMAKER : "TROUBLEMAKER",//捣蛋鬼 orderNum:12
    DRUNK : "DRUNK",//酒鬼 orderNum:13
    INSOMNIAC : "INSOMNIAC",//失眠者 orderNum:14
    HUNTER : "HUNTER",//猎人 orderNum:15
    MASON : "MASON",//守夜人 orderNum:16
    VILLAGER : "VILLAGER"//村民 orderNum:18
};

//获得get参数
var getGetQueryString = function (name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};

//进度条设置
var progressBarSetting = function(domId,progressValue){
    var percentage = parseInt(progressValue * 100) + "%";
    $(domId).css("width", percentage);
    $(domId).html(percentage);
};

//显示设置
var showSetting = function(hiddenDomIds,showDomIds){
    hiddenDomIds.forEach(function(value){
        $(value).css("display","none");
    });
    if(showDomIds != undefined){
        showDomIds.forEach(function(value){
            $(value).css("display","block");
        });
    }
};

//执行技能 -- 底牌标识为已执行技能
var executeSkillByNobody = function () {
    $.ajax({
        type : "POST",  //提交方式
        url : "/activity/executeSkillByNobody.json",//路径
        data : {
            activityId : $("#activityId").html()
        },
        dataType : "json",
        success : function(result){
            if(!result.success){
                $("#errorMsgDiv").html(result.msg);
            }
        }
    });
};

//技能设置
var skillSetting = {
    peopleCount : 3,

    disableSeatNumList : [],
    needSelectedCount : 1,
    SelectedSeatNumList : []
};

//描绘技能列表
var describeSkillList = function(){
    var html = "";
    for(var i = 0 ; i < 3 ; i++){

    }
};

//执行技能 -- 当前角色技能
var executeSkillByCurrent = function(data){
    for(var key in roleIdJson){
        showSetting(["#notSkill_" + key]);//显示设置
    }
    if(data.myRole.id == roleIdJson.DOPPELGANGER){//化身幽灵

    } else if(data.myRole.id == roleIdJson.WEREWOLF){//狼人

    }
};

//处理场次状态显示内容
var processActivityStatusShowContent = function(data){
    if(data.status == "NOT_JOIN"){//当前用户没有参与本场次
        showSetting(["#notBigin","#notSkill"]);//显示设置
        $("#errorMsgDiv").html("你并没有参与本场次");
    } else if(data.status == "NOT_BEGIN"){//未开始
        showSetting(["#notBigin","#notSkill"],["#notBigin"]);//显示设置
        $("#notBigin_mySeatNum").html(data.mySeatNum);//我的座号
        $("#notBigin_waitingPeopleCount").html(data.peopleCount - data.lockPeopleCount);//等待锁定座号人数
        progressBarSetting("#notBigin_progress",data.lockPeopleCount / data.peopleCount);//进度条设置
        setTimeout(askActivityStatus,1000);
    } else if(data.status == "NOT_SKILL"){//未执行技能
        showSetting(["#notBigin","#notSkill"],["#notSkill"]);//显示设置
        if(data.isNobody){//当前正在执行技能的角色是否存在于底牌当中
            setTimeout(executeSkillByNobody,3000);//执行技能 -- 底牌标识为已执行技能
        }
        $("#notSkill_mySeatNum").html(data.mySeatNum);//我的座号
        $("#notSkill_myRoleCardName").html(data.myRoleCard.name);//当前抽到的角色牌名称
        $("#notSkill_currentRoleName").html(data.currentRole.name);//当前执行技能的角色名称
        $("#notSkill_skillRoleCount").html(data.skillRoleCount);//已执行技能的角色数量
        $("#notSkill_roleCount").html(data.roleCount);//本房间角色的数量
        if(data.isSkillByCurrent){//是否轮到当前用户执行
            showSetting(["#notSkill_isSkillByCurrent_false"],["#notSkill_isSkillByCurrent_true"]);//显示设置
            executeSkillByCurrent(data);//执行技能 -- 当前角色技能
        } else{//等待其它用户执行技能
            showSetting(["#notSkill_isSkillByCurrent_true"],["#notSkill_isSkillByCurrent_false"]);//显示设置
            progressBarSetting("#notSkill_progress",data.skillRoleCount / data.roleCount);//进度条设置
            setTimeout(askActivityStatus,1000);
        }
    } else if(data.status == "NOT_VOTE"){//未投票
        /////////////////////////////////////////////////
    } else if(data.status == "END"){//结束
        /////////////////////////////////////////////////
    }
};

//询问场次状态
var askActivityStatus = function(){
    $.ajax({
        type : "POST",  //提交方式
        url : "/activity/askActivityStatus.json",//路径
        data : {
            activityId : $("#activityId").html()
        },
        dataType : "json",
        success :  function(result){
            //debugger;
            if(result.success){
                var data = result.data;
                processActivityStatusShowContent(data);//处理场次状态显示内容
            } else{
                $("#errorMsgDiv").html(result.msg);
            }
        }
    });
};

//执行初始化
var ini = function(){
    var activityId = getGetQueryString("id");
    $("#activityId").html(activityId);
    askActivityStatus();//询问场次状态
};

//初始化
$(function () {
    ini();
});