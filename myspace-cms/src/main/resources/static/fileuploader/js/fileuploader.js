function FileUploader(){
	
	this.uploaderID='';
	this.uploaderAttr=null;
	this.fileExistenceCheckUrl=null;
	this.onFilepreupload=null;
	this.onFileuploaded=null;
	this.onCheckLocalUploadedFileExistence=null;
	this.onCheckRemoteUploadedFileExistence=null;
	
	this.currentSelectedFile=null;
	
	this.uploadedFiles=null;

	var that=this;

	this.init=function(uploaderID,uploaderAttr) {
		that.uploaderID = uploaderID;
		that.uploaderAttr=uploaderAttr;
		that.fileExistenceCheckUrl=uploaderAttr.fileExistenceCheckUrl;
		that.initUploader();
		that.registeEvent();
	}
	
	this.initUploader=function(){
		if(null==that.uploaderAttr||undefined==that.uploaderAttr['uploadUrl']||null==that.uploaderAttr['uploadUrl']){
			that.showLog("No upload Url attribute assigned !!!");
			return;
		}
		that.uploaderAttr['maxFilesNum']=1;
		that.uploaderAttr['required']=true;
		that.uploaderAttr['maxFilePreviewSize']=10240;
		
	    $("#"+that.uploaderID).fileinput(that.uploaderAttr);
	}
	
	this.disable=function(){
		$("#"+that.uploaderID).fileinput("disable");
	};
	
	this.enable=function(){
		$("#"+that.uploaderID).fileinput("enable");
	}
	
	this.clear=function(){
		$('#'+that.uploaderID).fileinput('clear');
	}
	
	this.registeEvent=function(){
		$('#'+that.uploaderID).on('filepreupload', that.onInnerFilepreupload);
		$('#'+that.uploaderID).on('fileuploaded', that.onInnerFileuploaded);
		$('#'+that.uploaderID).on('fileloaded', that.onInnerFileloaded);
		$('#'+that.uploaderID).on('fileclear', that.onInnerFileclear);
	}
	
	this.onInnerFileclear=function(event, id, index) {
		console.log("onInnerFileclear");
		that.currentSelectedFile=null;
	};
	
	this.onInnerFileloaded=function(event, file, previewId, index, reader) {
		that.currentSelectedFile=file;
	}
	
	this.onInnerFilepreupload=function(event, data, previewId, index){
		//var form = data.form, files = data.files, extra = data.extra,
        //response = data.response, reader = data.reader;
		if(null!=that.onFilepreupload){
			return that.onFilepreupload(event, data, previewId, index);
		}
	}
	
	this.onInnerFileuploaded=function(event, data, previewId, index){
		//var form = data.form, files = data.files, extra = data.extra,
        //response = data.response, reader = data.reader;
		if(data.files!=null){
			if(null==that.uploadedFiles){
				that.uploadedFiles=[];
			}
			for(var index=0;index<data.files.length;index++){
				that.uploadedFiles.push({
					'fileName':data.files[index].name,
					'nodePath':data.extra.nodePath,
					'nodeName':data.extra.nodeName
					});
			}
		}
		
		if(null!=that.onFilepreupload){
			return that.onFileuploaded(event, data, previewId, index);
		}
	}
	
	this.checkLocalUploadedFileExistence=function(){
	}
	
	this.checkRemoteUploadedFileExistence=function(data){
		if(null!=that.fileExistenceCheckUrl){
			$.ajax({
	            type: "POST",
	            url: that.fileExistenceCheckUrl,
	            data:JSON.stringify(data),
	            contentType: "application/json;charset=utf-8", 
	            dataType: "json",
	            success: function(retData){
	            	that.onCheckRemoteUploadedFileExistence(retData);
	            }
	        });
		}else{
			return that.onCheckRemoteUploadedFileExistence({'isSuccess':false,'message':'empty fileExistenceCheckUrl'});
		}
	}
		
	this.showLog=function(msg){
		console.log(msg);	
	}
	

};
