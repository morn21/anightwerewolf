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
    nobodyCount : 3,//底牌数量
    peopleCount : 5,//玩家号码牌数量
    needSelectedCount : 1,//需要选择的数量
    isSelectedSeatNumList : [],//已选中号码列表
    isDisabledSeatNumList : [],//已禁用号码列表
    ini : function(){//初始化
        skillSetting.nobodyCount = 3;
        skillSetting.peopleCount = 5;
        skillSetting.needSelectedCount = 1;
        skillSetting.isSelectedSeatNumList = [];
        skillSetting.isDisabledSeatNumList = [];
    },
    hideNobody : function(){//隐藏底牌
        skillSetting.nobodyCount = 0;
    },
    hideBody : function(){//隐藏号码牌
        skillSetting.peopleCount = 0;
    },
    isSelected : function(seatNum){//按钮是否选中
        var isSelected = false;
        skillSetting.isSelectedSeatNumList.forEach(function (v) {
            if(seatNum == v){
                isSelected = true;
            }
        });
        return isSelected;
    },
    isDisabled : function(seatNum){//按钮是否可用
        var isDisabled = false;
        skillSetting.isDisabledSeatNumList.forEach(function (v) {
            if(seatNum == v){
                isDisabled = true;
            }
        });
        return isDisabled;
    },
    addSeatNumFromIsSelectedList : function(seatNum){//添加座号到已选中列表
        if(skillSetting.isSelectedSeatNumList.length > 0 && skillSetting.isSelectedSeatNumList.length == skillSetting.needSelectedCount){
            skillSetting.isSelectedSeatNumList.splice(0);
        }
        skillSetting.isSelectedSeatNumList.push(seatNum);
    },
    removeSeatNumFromIsSelectedList : function(seatNum){//移除座号从已选中列表
        var removeSeatNumIndex = -1;//准备要移除座号 在数组中的下标
        skillSetting.isSelectedSeatNumList.forEach(function (v,index) {
            if(seatNum == v){
                removeSeatNumIndex = index;
            }
        });
        if(removeSeatNumIndex != -1){
            skillSetting.isSelectedSeatNumList.splice(removeSeatNumIndex);
        }
    }

};

//描绘技能列表
var describeSkillButtonList = function(){
    {
        var html = "";
        for(var i = -1 ; i > (-1 - skillSetting.nobodyCount) ; i--){
            var showSeatNum = 0 - i;
            var seatNum = i;
            html += basic.generateSelectButtonHtml("底牌" + showSeatNum,"clickSkillButton(" + seatNum + ")",skillSetting.isSelected(i),skillSetting.isDisabled(i));
        }
        $("#notSkill_skillButtonList_nobody").html(html);
    }
    {
        var html = "";
        for(var i = 0 ; i < skillSetting.peopleCount ; i++){
            var seatNum = i + 1;
            html += basic.generateSelectButtonHtml("座号" + seatNum,"clickSkillButton(" + seatNum + ")",skillSetting.isSelected(seatNum),skillSetting.isDisabled(seatNum));
        }
        $("#notSkill_skillButtonList_body").html(html);
    }
};

//点击技能按钮
var clickSkillButton = function(seatNum){
    if(skillSetting.isSelected(seatNum)){
        skillSetting.removeSeatNumFromIsSelectedList(seatNum);//将座号从已选中列表中移除
    } else {
        skillSetting.addSeatNumFromIsSelectedList(seatNum);//添加座号到已选中列表
    }
    describeSkillButtonList();//重新描绘技能列表
};

//初始化技能按钮列表
var iniSkillButtonList = function(myRoleId,peopleCount,mySkillStatus,mySkillExtendInfo){
    var promptMsg = "";//技能提示信息
    skillSetting.ini();//初始化技能列表设置
    skillSetting.peopleCount = peopleCount;//设置玩家人数
    if(myRoleId == roleIdJson.DOPPELGANGER){//化身幽灵

    } else if(myRoleId == roleIdJson.WEREWOLF){//狼人
        if(mySkillExtendInfo.teammateSeatNumList == undefined){//不知道有几只狼的时候
            promptMsg = "点击【确认执行技能】查看队友";
            skillSetting.hideNobody();//隐藏底牌
        } else if(mySkillExtendInfo.teammateSeatNumList.length == 0){//只有一只狼的时候
            if(mySkillExtendInfo.checkCardList == undefined){
                promptMsg = "场上只有你一只狼，请选择你要查验的一张底牌";
            } else {
                mySkillExtendInfo.checkCardList.forEach(function (obj) {
                    skillSetting.isDisabledSeatNumList.push(obj.seatNum);
                    promptMsg += "【底牌" + (0 - obj.seatNum) + "】 =》 【" + obj.roleCardName + "】";
                    promptMsg += "<br/>";
                });
                if(mySkillStatus == 0){
                    promptMsg += "你还要再查一张底牌";
                }
            }
            skillSetting.needSelectedCount = 1;
        } else {//有多只狼的时候
            mySkillExtendInfo.teammateSeatNumList.forEach(function (v) {
                promptMsg += "【" + v + "号】";
            });
            promptMsg += "是你的狼队友";
            skillSetting.hideNobody();//隐藏底牌
        }
        skillSetting.hideBody();//隐藏号码牌
    } else if(myRoleId == roleIdJson.MYSTIC_WOLF){//狼先知
    } else if(myRoleId == roleIdJson.MINION){//爪牙
    } else if(myRoleId == roleIdJson.DREAM_WOLF){//贪睡狼
    } else if(myRoleId == roleIdJson.TANNER){//皮匠
    } else if(myRoleId == roleIdJson.SEER){//预言家
    } else if(myRoleId == roleIdJson.APPRENTICE_SEER){//见习预言家
    } else if(myRoleId == roleIdJson.ROBBER){//强盗
    } else if(myRoleId == roleIdJson.WITCH){//女巫
    } else if(myRoleId == roleIdJson.TROUBLEMAKER){//捣蛋鬼
    } else if(myRoleId == roleIdJson.DRUNK){//酒鬼
    } else if(myRoleId == roleIdJson.INSOMNIAC){//失眠者
    } else if(myRoleId == roleIdJson.HUNTER){//猎人
    } else if(myRoleId == roleIdJson.MASON){//守夜人
    } else if(myRoleId == roleIdJson.VILLAGER){//村民
    }
    if(mySkillStatus == 1){//技能已执行 隐藏确认执行技能按钮
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌
        showSetting(["#notSkill_confirmExecuteSkillButtonDiv"]);//显示设置
    }
    $("#notSkill_promptMsg").html(promptMsg);//技能提示信息
    describeSkillButtonList();//重新描绘技能列表
};

//确认执行技能
var confirmExecuteSkill = function(){
    $.ajax({
        type : "POST",  //提交方式
        url : "/activity/executeSkillByMyRole.json",//路径
        data : {
            activityId : $("#activityId").html(),
            isSelectedSeatNumListStr : JSON.stringify(skillSetting.isSelectedSeatNumList),
        },
        dataType : "json",
        success :  function(result){
            if(result.success){
                ini();//调用重新初始化
            } else{
                $("#errorMsgDiv").html(result.msg);
            }
        }
    });
};

//点击确认执行技能按钮
$("#notSkill_confirmExecuteSkillButton").click(function(){
    confirmExecuteSkill();//确认执行技能
});

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
            setTimeout(executeSkillByNobody,10000);//执行技能 -- 底牌标识为已执行技能
        }
        $("#notSkill_mySeatNum").html(data.mySeatNum);//我的座号
        $("#notSkill_myRoleCardName").html(data.myRoleCard.name);//当前抽到的角色牌名称
        $("#notSkill_currentRoleName").html(data.currentRole.name);//当前执行技能的角色名称
        $("#notSkill_skillRoleCount").html(data.skillRoleCount);//已执行技能的角色数量
        $("#notSkill_roleCount").html(data.roleCount);//本房间角色的数量
        if(data.isSkillByCurrent || data.mySkillStatus == 1){//轮到当前用户执行 或 当前用户已执行时
            showSetting(["#notSkill_isSkillByCurrent_false"],["#notSkill_isSkillByCurrent_true"]);//显示设置
            iniSkillButtonList(data.myRole.id, data.peopleCount,data.mySkillStatus,data.mySkillExtendInfo);//初始化技能按钮列表
        } else{//等待其它用户执行技能
            showSetting(["#notSkill_isSkillByCurrent_true"],["#notSkill_isSkillByCurrent_false"]);//显示设置
            progressBarSetting("#notSkill_progress",data.skillRoleCount / data.roleCount);//进度条设置
        }
        if(!data.isSkillByCurrent){//不是当前用户执行时 触发轮询
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