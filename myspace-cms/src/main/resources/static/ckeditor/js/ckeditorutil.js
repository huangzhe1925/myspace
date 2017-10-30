function CKEditorUtil(){
	
	this.ckeditorID='';
	var that=this;

	this.init=function(ckeditorID) {
		that.ckeditorID = ckeditorID;
		that.setupEditor();
	}
	
	this.setupEditor=function(){
		CKEDITOR.replace(that.ckeditorID);
	}
/*
 * html text : CKEDITOR.instances.editor.getData()
 * origin text : CKEDITOR.instances.editor.document.getBody().getText();
 * */
	
	this.showLog=function(msg){
		//console.log(msg);	
	}
	
};



















