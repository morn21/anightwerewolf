
var basic = {
    /**生成选择按钮html*/
    generateSelectButtonHtml : function(buttonName,onclickFunctionStr,isSelected,isDisabled){
        //debugger;
        var html = "";
        html += "<button type='button' class='btn marginTop10 ";
        if(isSelected){
            html += "btn-primary";
        } else {
            html += "btn-default";
        }
        html += "' ";
        if(isDisabled){
            html += "disabled='false'";
        } else {
            html += "onclick=\"" + onclickFunctionStr + "\"";
        }
        html += ">";
        html += buttonName;
        html += "</button> ";
        return html;
    },
    //获得get参数
    getGetQueryString : function (name){
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
};