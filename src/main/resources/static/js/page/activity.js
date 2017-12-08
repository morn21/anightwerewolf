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
        debugger;
        if(skillSetting.isSelectedSeatNumList.length > 0 && skillSetting.isSelectedSeatNumList.length == skillSetting.needSelectedCount){
            skillSetting.isSelectedSeatNumList.splice(0,1);
        }
        skillSetting.isSelectedSeatNumList.push(seatNum);
    },
    removeSeatNumFromIsSelectedList : function(seatNum){//移除座号从已选中列表
        debugger;
        var removeSeatNumIndex = -1;//准备要移除座号 在数组中的下标
        skillSetting.isSelectedSeatNumList.forEach(function (v,index) {
            if(seatNum == v){
                removeSeatNumIndex = index;
            }
        });
        if(removeSeatNumIndex != -1){
            skillSetting.isSelectedSeatNumList.splice(removeSeatNumIndex,1);
        }
    }

};

//描绘技能列表
var describeSkillButtonList = function(nobodyHtmlId,bodyHtmlId){
    {
        var html = "";
        for(var i = -1 ; i > (-1 - skillSetting.nobodyCount) ; i--){
            var showSeatNum = 0 - i;
            var seatNum = i;
            html += basic.generateSelectButtonHtml("底牌" + showSeatNum,"clickSkillButton('" + nobodyHtmlId + "','" + bodyHtmlId + "'," + seatNum + ")",skillSetting.isSelected(i),skillSetting.isDisabled(i));
        }
        $(nobodyHtmlId).html(html);
    }
    {
        var html = "";
        for(var i = 0 ; i < skillSetting.peopleCount ; i++){
            var seatNum = i + 1;
            html += basic.generateSelectButtonHtml("座号" + seatNum,"clickSkillButton('" + nobodyHtmlId + "','" + bodyHtmlId + "'," + seatNum + ")",skillSetting.isSelected(seatNum),skillSetting.isDisabled(seatNum));
        }
        $(bodyHtmlId).html(html);
    }
};

//点击技能按钮
var clickSkillButton = function(nobodyHtmlId,bodyHtmlId,seatNum){
    if(skillSetting.isSelected(seatNum)){
        skillSetting.removeSeatNumFromIsSelectedList(seatNum);//将座号从已选中列表中移除
    } else {
        skillSetting.addSeatNumFromIsSelectedList(seatNum);//添加座号到已选中列表
    }
    describeSkillButtonList(nobodyHtmlId,bodyHtmlId);//重新描绘技能列表
};

//初始化技能按钮列表
var iniSkillButtonList = function(myRoleId,peopleCount,mySeatNum,mySkillStatus,mySkillExtendInfo){
    //showSetting([],["#notSkill_confirmExecuteSkillButtonDiv"]);//显示【确认执行技能】按钮
    var promptMsg = "";//技能提示信息
    var resultMsg = "";//技能结果信息
    skillSetting.ini();//初始化技能列表设置
    skillSetting.peopleCount = peopleCount;//设置玩家人数
    if(myRoleId == roleIdJson.DOPPELGANGER){//化身幽灵

    } else if(myRoleId == roleIdJson.WEREWOLF){//狼人
        if(mySkillExtendInfo.teammateSeatNumList == undefined){//不知道有几只狼的时候
            skillSetting.needSelectedCount = 0;
            promptMsg = "点击【确认执行技能】查看狼队友";
            skillSetting.hideNobody();//隐藏底牌
        } else if(mySkillExtendInfo.teammateSeatNumList.length == 0){//只有一只狼的时候
            if(mySkillExtendInfo.checkCardList == undefined){
                promptMsg = "场上只有你一只狼，请选择你要查验的一张底牌";
            } else {
                mySkillExtendInfo.checkCardList.forEach(function (obj,index) {
                    if(index > 0){
                        resultMsg += "<br/>";
                    }
                    skillSetting.isDisabledSeatNumList.push(obj.seatNum);
                    resultMsg += "【底牌" + (0 - obj.seatNum) + "】 =》 【" + obj.roleCardName + "】";
                });
                if(mySkillStatus == 0){
                    promptMsg += "你还要再查一张底牌";
                }
            }
        } else {//有多只狼的时候
            mySkillExtendInfo.teammateSeatNumList.forEach(function (v) {
                resultMsg += "【" + v + "号】";
            });
            resultMsg += "是我的狼队友";
            skillSetting.hideNobody();//隐藏底牌
        }
        skillSetting.hideBody();//隐藏号码牌
    } else if(myRoleId == roleIdJson.MYSTIC_WOLF){//狼先知
    } else if(myRoleId == roleIdJson.MINION){//爪牙
        skillSetting.needSelectedCount = 0;
        var checkCardList = mySkillExtendInfo.checkCardList;
        if(checkCardList == undefined){
            promptMsg = "点击【确认执行技能】查看狼队友";
        } else {
            checkCardList.forEach(function (obj) {
                resultMsg += "【" + obj.seatNum + "号】";
            });
            resultMsg += "是我的狼队友";
        }
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌

    } else if(myRoleId == roleIdJson.DREAM_WOLF){//贪睡狼
    } else if(myRoleId == roleIdJson.TANNER){//皮匠
        skillSetting.needSelectedCount = 0;
        var checkCard = mySkillExtendInfo.checkCard;
        if(checkCard == undefined){
            promptMsg = "点击【确认执行技能】自证，进入下一状态";
        } else {
            resultMsg = "昨晚我什么也没干";
        }
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌
    } else if(myRoleId == roleIdJson.SEER){//预言家
        skillSetting.isDisabledSeatNumList.push(mySeatNum);//禁用自己的座号
        if(mySkillExtendInfo.checkCardList == undefined){
            promptMsg = "请选择你要查验的一张牌";
        } else {
            mySkillExtendInfo.checkCardList.forEach(function (obj,index) {
                if(index > 0){
                    resultMsg += "<br/>";
                }
                skillSetting.isDisabledSeatNumList.push(obj.seatNum);
                resultMsg += "我查到了";
                if(obj.seatNum < 0 ){
                    resultMsg += "【底牌" + (0 - obj.seatNum) + "】";
                } else{
                    resultMsg += "【" + obj.seatNum + "号】";
                }
                resultMsg += "是【" + obj.roleCardName + "】";
            });
            skillSetting.hideBody();//隐藏号码牌
        }
    } else if(myRoleId == roleIdJson.APPRENTICE_SEER){//见习预言家
    } else if(myRoleId == roleIdJson.ROBBER){//强盗
        skillSetting.isDisabledSeatNumList.push(mySeatNum);//禁用自己的座号
        var checkCard = mySkillExtendInfo.checkCard;
        if(checkCard == undefined){
            promptMsg = "请选择你要抢夺的一张牌";
        } else {
            resultMsg = "我抢走了【" + checkCard.seatNum + "号】的【" + checkCard.roleCardName + "】";
        }
        skillSetting.hideNobody();//隐藏底牌
    } else if(myRoleId == roleIdJson.WITCH){//女巫
    } else if(myRoleId == roleIdJson.TROUBLEMAKER){//捣蛋鬼
        skillSetting.needSelectedCount = 2;//设置可选两张牌
        skillSetting.isDisabledSeatNumList.push(mySeatNum);//禁用自己的座号
        if(mySkillExtendInfo.checkCardList == undefined){
            promptMsg = "请选择你要交换的两张牌";
        } else {
            resultMsg += "我交换了";
            mySkillExtendInfo.checkCardList.forEach(function (obj,index) {
                if(index > 0){
                    resultMsg += "和";
                }
                resultMsg += "【" + obj.seatNum + "号】";
            });
        }
        skillSetting.hideNobody();//隐藏底牌
    } else if(myRoleId == roleIdJson.DRUNK){//酒鬼
        var checkCard = mySkillExtendInfo.checkCard;
        if(checkCard == undefined){
            promptMsg = "请选择你换走的一张底牌";
        } else {
            resultMsg = "我换走了【底牌" + (0 - checkCard.seatNum) + "】";
        }
        skillSetting.hideBody();//隐藏号码牌
    } else if(myRoleId == roleIdJson.INSOMNIAC){//失眠者
        skillSetting.needSelectedCount = 0;
        var checkCard = mySkillExtendInfo.checkCard;
        if(checkCard == undefined){
            promptMsg = "点击【确认执行技能】查看自己的底牌";
        } else {
            resultMsg = "我现在的牌是【" + checkCard.roleCardName +"】";
        }
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌
    } else if(myRoleId == roleIdJson.HUNTER){//猎人
        skillSetting.needSelectedCount = 0;
        var checkCard = mySkillExtendInfo.checkCard;
        if(checkCard == undefined){
            promptMsg = "点击【确认执行技能】自证，进入下一状态";
        } else {
            resultMsg = "昨晚我什么也没干";
        }
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌
    } else if(myRoleId == roleIdJson.MASON){//守夜人
        skillSetting.needSelectedCount = 0;
        var checkCard = mySkillExtendInfo.checkCard;
        if(checkCard == undefined){
            promptMsg = "点击【确认执行技能】查看你的守夜人同伴";
        } else {
            if(checkCard.seatNum < 0){
                resultMsg = "场上没有我的守夜人同伴";
            } else {
                resultMsg = "【" + checkCard.seatNum + "号】是我的守夜人同伴";
            }
        }
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌
    } else if(myRoleId == roleIdJson.VILLAGER){//村民
        skillSetting.needSelectedCount = 0;
        var checkCard = mySkillExtendInfo.checkCard;
        if(checkCard == undefined){
            promptMsg = "点击【确认执行技能】自证，进入下一状态";
        } else {
            resultMsg = "昨晚我什么也没干";
        }
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌
    }
    /*if(mySkillStatus == 1){//技能已执行 隐藏确认执行技能按钮
        skillSetting.hideNobody();//隐藏底牌
        skillSetting.hideBody();//隐藏号码牌
        showSetting(["#notSkill_confirmExecuteSkillButtonDiv"]);//隐藏【确认执行技能】按钮
    }*/
    $("#notSkill_promptMsg").html(promptMsg);//技能提示信息
    $("#mySkillMessage_result").html(resultMsg);//技能结果信息
    describeSkillButtonList("#notSkill_skillButtonList_nobody","#notSkill_skillButtonList_body");//重新描绘技能列表
};

//确认执行技能
var confirmExecuteSkill = function(){
    $("#errorMsgDiv").html("");//清除错误信息
    if(skillSetting.isSelectedSeatNumList.length != skillSetting.needSelectedCount){
        $("#errorMsgDiv").html("你至少需要选择" + skillSetting.needSelectedCount + "张牌");
        return;
    }
    //showSetting(["#notSkill_confirmExecuteSkillButtonDiv"]);//隐藏【确认执行技能】按钮
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

//初始化投票按钮列表
var iniVoteButttonList = function(peopleCount){
    skillSetting.ini();
    skillSetting.peopleCount = peopleCount;
    describeSkillButtonList("#notVote_skillButtonList_nobody","#notVote_skillButtonList_body");//重新描绘技能列表
};

//确认投票
var confirmExecuteVote = function(isSelectedSeatNum){
    $.ajax({
        type : "POST",  //提交方式
        url : "/activity/executeVote.json",//路径
        data : {
            activityId : $("#activityId").html(),
            isSelectedSeatNum : isSelectedSeatNum,
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

//点击确认投票按钮
$("#notVote_confirmExecuteVoteButton").click(function () {
    $("#errorMsgDiv").html("");//清除错误信息
    if(skillSetting.isSelectedSeatNumList.length != skillSetting.needSelectedCount){
        $("#errorMsgDiv").html("你至少需要选择" + skillSetting.needSelectedCount + "张牌");
        return;
    }
    confirmExecuteVote(skillSetting.isSelectedSeatNumList[0]);//确认投票
});

//点击弃票按钮
$("#notVote_confirmExecuteAbstainedButton").click(function () {
    confirmExecuteVote(0);//确认投票 0为弃票
});

//处理场次状态显示内容
var processActivityStatusShowContent = function(data){
    if(data.status == "NOT_JOIN"){//当前用户没有参与本场次
        showSetting(["#notBigin","#notSkill","#notVote"]);//显示设置
        $("#errorMsgDiv").html("你并没有参与本场次");
    } else if(data.status == "NOT_BEGIN"){//未开始
        showSetting(["#notBigin","#notSkill","#notVote"],["#notBigin"]);//显示"未开始"模块
        $("#notBigin_mySeatNum").html(data.mySeatNum);//我的座号
        $("#notBigin_waitingPeopleCount").html(data.peopleCount - data.lockPeopleCount);//等待锁定座号人数
        progressBarSetting("#notBigin_progress",data.lockPeopleCount / data.peopleCount);//进度条设置
        setTimeout(askActivityStatus,1000);
    } else if(data.status == "NOT_SKILL"){//未执行技能
        showSetting(["#notBigin","#notSkill","#notVote"],["#notSkill"]);//显示"未执行技能"模块
        showSetting([],["#mySkillMessage"]);//显示我的信息
        $("#mySeatNum").html(data.mySeatNum);//我的座号
        $("#myRoleCardName").html(data.myRoleCard.name);//我的角色牌名称
        $("#notSkill_currentRoleName").html(data.currentRole.name);//当前执行技能的角色名称
        $("#notSkill_skillRoleCount").html(data.skillRoleCount);//已执行技能的角色数量
        $("#notSkill_roleCount").html(data.roleCount);//本房间角色的数量
        if(data.isSkillByCurrent || data.mySkillStatus == 1){
            iniSkillButtonList(data.myRole.id, data.peopleCount,data.mySeatNum,data.mySkillStatus,data.mySkillExtendInfo);//初始化技能按钮列表
        }
        if(data.isSkillByCurrent){//轮到当前用户执行 或 当前用户已执行时
            showSetting(["#notSkill_isSkillByCurrent_false"],["#notSkill_isSkillByCurrent_true"]);//显示设置
        } else{//等待其它用户执行技能
            showSetting(["#notSkill_isSkillByCurrent_true"],["#notSkill_isSkillByCurrent_false"]);//显示设置
            progressBarSetting("#notSkill_progress",data.skillRoleCount / data.roleCount);//进度条设置
            setTimeout(askActivityStatus,1000);
        }
    } else if(data.status == "NOT_VOTE"){//未投票
        showSetting(["#notBigin","#notSkill","#notVote"],["#notVote"]);//显示"未执行技能"模块
        showSetting([],["#mySkillMessage"]);//显示我的信息
        $("#notVote_speakNum").html(data.speakNum);
        $("#mySeatNum").html(data.mySeatNum);//我的座号
        $("#myRoleCardName").html(data.myRoleCard.name);//我的角色牌名称
        var myVoteNum = data.myVoteNum;//我的投票号
        if(myVoteNum == undefined){//未投票时
            showSetting(["#notVote_isNoteVote_false"],["#notVote_isNoteVote_true"]);//显示设置
            iniSkillButtonList(data.myRole.id, data.peopleCount,data.mySeatNum,data.mySkillStatus,data.mySkillExtendInfo);//初始化技能按钮列表
            iniVoteButttonList(data.peopleCount);//初始化投票按钮列表
        } else{//已投票时
            showSetting(["#notVote_isNoteVote_true"],["#notVote_isNoteVote_false"]);//显示设置
            var promptMsg = "我";
            if(myVoteNum < 0){
                promptMsg += "投了【底牌" + (0 - myVoteNum) + "】";
            } else if(myVoteNum > 0){
                promptMsg += "投了【" + myVoteNum + "号】";
            } else if(myVoteNum == 0){
                promptMsg += "选择了【弃票】";
            }
            promptMsg += "，还有" + (data.peopleCount - data.voteCount) + "人未投票";
            $("#notVote_promptMsg").html(promptMsg);
            progressBarSetting("#notVote_progress",data.voteCount / data.peopleCount);//进度条设置
            setTimeout(askActivityStatus,1000);
        }
    } else if(data.status == "END"){//结束
        window.location.href = "/result?id=" + $("#activityId").html();
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
    var activityId = basic.getGetQueryString("id");
    $("#activityId").html(activityId);
    askActivityStatus();//询问场次状态
};

//初始化
$(function () {
    ini();
});