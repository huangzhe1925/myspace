function AppendTabsUtil(){
	
	this.appendTabsID='';
	this.appendTabsContentID='';
	this.onCloseTab=null;
	var that=this;

	this.init=function(appendTabsID) {
		that.appendTabsID = appendTabsID;
		that.appendTabsContentID=appendTabsID+"Content";
	}
/*
 * html text : CKEDITOR.instances.editor.getData()
 * origin text : CKEDITOR.instances.editor.document.getBody().getText();
 * */
	
	this.showLog=function(msg){
		//console.log(msg);	
	}
	
	this.registeCloseBtn=function(newTabID){
		$("#"+that.appendTabsID+" ."+newTabID+"-CloseBtn").on("click",function(){
			that.innerCloseTab(newTabID);
		});
	}
	
	this.innerCloseTab=function(newTabID){
		var continueCloseTab=true;
		if(that.onCloseTab!=null){
			continueCloseTab=that.onCloseTab(newTabID);
		}
		if(that.onCloseTab!=null&&!continueCloseTab){
			return;
		}
		$("#"+that.appendTabsID+" ."+newTabID+"li").remove();
		$("#"+that.appendTabsID+"-"+newTabID).remove();
	}
	
	this.addNewTab=function(newTabID,newTabTitle,newTabContent){
		if(null==newTabID||newTabID==''){
			return;
		}
		
		$("#"+that.appendTabsID).append("<li class=\""+newTabID+"li\"><a href=\"#"+that.appendTabsID+"-"+newTabID+"\" data-toggle=\"tab\">"+newTabTitle
				+"<button type=\"button\" class=\"btn-xs btn-danger "+newTabID+"-CloseBtn"+"\">[x]</button></a></li>");
		$("#"+that.appendTabsContentID).append("<div class=\"tab-pane\" id=\""+that.appendTabsID+"-"+newTabID+"\"></div>");
		$("#"+that.appendTabsID+"-"+newTabID).html(newTabContent);
		
		that.registeCloseBtn(newTabID);
	}
	
};



















