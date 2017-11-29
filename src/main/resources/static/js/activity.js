
//获得get参数
var getGetQueryString = function (name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};

//进度条设置
var progressBarSetting = function(htmlId,progressValue){
    var percentage = parseInt(progressValue * 100) + "%";
    $(htmlId).css("width", percentage);
    $(htmlId).html(percentage);
};

//处理场次状态显示内容
var processActivityStatusShowContent = function(data){
    $("#notBigin").css("display","none");
    if(data.status == "NOT_JOIN"){//当前用户没有参与本场次
        $("#errorMsgDiv").html("你并没有参与本场次");
    } else if(data.status == "NOT_BEGIN"){//未开始
        $("#notBigin").css("display","block");
        $("#notBigin_lockSeatName").html(data.seatNum);//已锁定座号
        $("#notBigin_waitingPeopleCount").html(data.peopleCount - data.lockPeopleCount);//等待锁定座号人数
        progressBarSetting("#notBigin_progress",data.lockPeopleCount / data.peopleCount);//进度条设置
        setTimeout(askActivityStatus,1000);
    } else if(data.status == "NOT_SKILL"){//未执行技能
        $("#notSkill").css("display","block");
        $("#notSkill_myRoleCardName").html(data.myRoleCard.name);//当前抽到的角色牌
        $("#notSkill_currentRoleCardName").html(data.currentRoleCard.name);//当前执行技能的角色牌
        $("#notSkill_currentOrderNum").html(data.currentOrderNum);//当前执行牌位序号
        $("#notSkill_cardCount").html(data.cardCount);//本房间场次的总牌数
        if(data.isNobody){//当前是否是轮到底牌角色执行
            //////////////////////////////////////////////////////////////////////
        }
        $("#notSkill_isCurrentUser_true").css("display","none");
        $("#notSkill_isCurrentUser_false").css("display","none");
        if(data.isCurrentUser){//是否轮到当前用户执行
            $("#notSkill_isCurrentUser_true").css("display","block");
        } else{//等待其它用户执行技能
            $("#notSkill_isCurrentUser_false").css("display","block");
            progressBarSetting("#notSkill_progress",data.currentOrderNum / data.cardCount);//进度条设置
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