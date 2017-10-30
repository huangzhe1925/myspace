var cmsTree;
var tableView;
var fileUploader;
var addNodeModal;
var continueModal;
var confirmModal;
var inputTableGroups;
var editorTabs;
var ckeditorUtils={};

$(function(){
	
	var frameHeight=$(window).height();
	$( ".left_resizable" ).height(frameHeight);
	$( ".right_resizable" ).height(frameHeight);
	var leftWidth=$(".left_resizable").width();
	var rightWidth=$(".right_resizable").width();
	
	$( ".left_resizable" ).resizable({
		minHeight: frameHeight-3,
		maxHeight: frameHeight-3,
		maxWidth:leftWidth-5
	});
	$( ".right_resizable" ).resizable({
		minHeight: frameHeight-3,
		maxHeight: frameHeight-3,
		maxWidth:rightWidth-5
	});
	
	
	tableView = new TableView();
	tableView.init("tableview");
	tableView.updateTable(new Object());
	
	cmsTree = new TreeView();
	cmsTree.init("cmstree",'/cmsrepomgnt/getnodepoint');
	cmsTree.onSelected=function(selNode){
		var tableData=commonUtil.convertTNPropToTabObj(selNode.properties);
		//console.log(tableData);
		tableView.updateTable(tableData);
	}
	
	cmsTree.doubleClickNode=function(data){
		console.log("doubleClickNode");
		console.log(data);
		alert("doubleClickNode");
	}
	
	addNodeModal=new ModalUtil();
	addNodeModal.init('addNodeModal',{ keyboard: true ,show:false});
	addNodeModal.onShow=function(){
		var selNodePath=cmsTree.getSelectedNodeabsolutePath();
		//console.log(selNodePath);
		$("#newNodePath").val('');
		if(null!=selNodePath){
			$("#newNodePath").val(selNodePath);
			$("#newNodePath").prop("disabled",true);
		}else{
			$("#newNodeName").val();
			$("#newNodePath").removeAttr("disabled");
		}
	}
	addNodeModal.onSubmitButton=function(){
		processSubmit();
	}
	
	function processSubmit(){
		if($("#uploadercb").prop('checked')){
			var newNodeName = $("#newNodeName").val();
			var newNodePath = $("#newNodePath").val();
			var newFilePropName= $("#newFilePropName").val();
			var selectedFile=fileUploader.currentSelectedFile;
			if(null==newNodeName||''==newNodeName){
				alert("empty node name");
				return
			}
			if(null==newNodePath||''==newNodePath){
				alert("empty node path");
				return
			}
			if(null==newNodeName||''==newNodeName){
				alert("empty node name");
				return
			}
			if(null==selectedFile){
				alert("empty file");
				return
			}
			var data={'nodeName':newNodeName,
					'nodePath':newNodePath,
					'fileName':selectedFile.name,
					'nodePropertyName':newFilePropName};
			console.log(selectedFile);
			console.log(data);
			fileUploader.checkRemoteUploadedFileExistence(data);
		}else{
			createNewNode();
		}
	}
	
	$('#addNodeBtn').on('click',function(){
		addNodeModal.show() // 初始化后立即调用 show 方法
		resetAddNodeModal();
	});
	
	$('#refreshNodeBtn').on('click',function(){
		var selNodePath=cmsTree.getSelectedNodeabsolutePath();
		if(null!=selNodePath&&selNodePath!=''){
			cmsTree.refreshNode(selNodePath);
		}
	});
	
	$('#saveSessionBtn').on('click',function(){
		$.ajax({
            type: "POST",
            url: '/cmsrepomgnt/savesession',
            data:JSON.stringify({}),
            contentType: "application/json;charset=utf-8", 
            dataType: "json",
            success: function(retData){
            	console.log(retData);
            	if(!retData.isSuccessful){
            		confirmModal.setModalBodyContent('Save Session Failed: '+retData.message);
        			confirmModal.show();
            	}
            }
        });
	});
	
	$('#refreshSessionBtn').on('click',function(){
		$.ajax({
            type: "POST",
            url: '/cmsrepomgnt/refreshsession',
            data:JSON.stringify({}),
            contentType: "application/json;charset=utf-8", 
            dataType: "json",
            success: function(retData){
            	console.log(retData);
            	if(!retData.isSuccessful){
            		confirmModal.setModalBodyContent('Refresh Session Failed: '+retData.message);
        			confirmModal.show();
            	}
            }
        });
	});
	
	
	
	$('#delNodeBtn').on('click',function(){
		var selNodePath=cmsTree.getSelectedNodeabsolutePath();
		if(null==selNodePath||selNodePath==''){
			confirmModal.setModalBodyContent('Empty node path,please select one node to delete');
			confirmModal.show();
		}else{
			continueModal.setModalBodyContent('Do you confirm to delete node　'+selNodePath);
			continueModal.onContinueButton=function(){
				$.ajax({
		            type: "POST",
		            url: '/cmsrepomgnt/deletenode',
		            data:JSON.stringify({'nodePath':selNodePath}),
		            contentType: "application/json;charset=utf-8", 
		            dataType: "json",
		            success: function(retData){
		            	console.log("deleted Node");
		            	console.log(retData);
		            	if(retData.isSuccessful){
		            		continueModal.hide();
		            		cmsTree.deleteTreeNode(selNodePath);
		            	}else{
		            		confirmModal.setModalBodyContent(retData.message);
		        			confirmModal.show();
		            	}
		            }
		        });
			}
			continueModal.show();
		}
	});
	
	continueModal=new ModalUtil();
	continueModal.init('continueModal',{ keyboard: true ,show:false});
	
	confirmModal=new ModalUtil();
	confirmModal.init('confirmModal',{ keyboard: true ,show:false});
	
	function resetAddNodeModal(){
		fileUploader.clear();
		 $("#uploadercb").prop('checked',false);
		 $("#newFilePropName").val('');
		 $("#newNodeName").val('');
		 $("#fileTabContent").hide();
		 $('#addNodeTabs li:eq(0) a').tab('show');
		 
		 inputTableGroups.reset();
	}
	
	//------------------- input groups part ----------------------
	inputTableGroups=new InputTableGroups();
	inputTableGroups.init('addPropInputGroups');
	
	
	//-------------------file uploader part-----------------
	fileUploader=new FileUploader();
	fileUploader.init('fileuploader',
		{
		 uploadUrl: '/cmsrepomgnt/upload', // you must set a valid URL here else you will get an error
		 fileExistenceCheckUrl: '/cmsrepomgnt/uploadedfilecheck',
	     uploadExtraData: function getUploadExtraData(){
	    	 				var newNodeName = $("#newNodeName").val();
	    	 				var newNodePath = $("#newNodePath").val();
	    	 				var newFilePropName= $("#newFilePropName").val();
	    	 				return {'nodeName':newNodeName,'nodePath':newNodePath,'nodeProperyName':newFilePropName};
	     }
		}
	);
	
	fileUploader.onFilepreupload=function(event, data, previewId, index){
		var extra=data.extra;
		console.log("extra");
		console.log(extra);
		console.log(extra.nodeProperyName=="");
		if (null==extra.nodeName||null==extra.nodePath||
				extra.nodePath==""||extra.nodeName=="" ||
					null==extra.nodeProperyName||extra.nodeProperyName=="") {
			return {
				message: 'Node Path and Node Name and Property Name must be specified!',
				data: {'error': 'nodePath'}
			};
		}
	}
	
	fileUploader.onFileuploaded=function(event, data, previewId, index){
		console.log("file uploaded");
		//console.log(fileUploader.uploadedFiles);
	}
	
	
	fileUploader.onCheckRemoteUploadedFileExistence=function(data){
		console.log(data);
		if(data.isSuccessful&&data.isFileUploaded){
			createNewNode();
		}
	}
	
	function createNewNode(){
		var data={};
		var newNodeName = $("#newNodeName").val();
		var newNodePath = $("#newNodePath").val();
		data.nodeName=newNodeName;
		data.nodePath=newNodePath;
		var propList=new Array();
		data.propList=propList;
		if($("#uploadercb").prop('checked')){
			var fileName=fileUploader.currentSelectedFile.name;
			var newFilePropName= $("#newFilePropName").val();
			propList.push(
					{'propertyName':newFilePropName,
					'propertyType':'Binary',
					'propertyStringValues':[fileName]});
		}
		
		var propArr=inputTableGroups.getInputValues();
		if(null!=propArr&&propArr.length>0){
			$.each(propArr,function(index,domEle){
				propList.push(
						{'propertyName':propArr[index].key,
						'propertyType':'String',
						'propertyStringValues':propArr[index].values});
			});
		}
		
		$.ajax({
            type: "POST",
            url: '/cmsrepomgnt/createnode',
            data:JSON.stringify(data),
            contentType: "application/json;charset=utf-8", 
            dataType: "json",
            success: function(retData){
            	console.log(retData);
            	if(retData.isSuccessful){
            		addNodeModal.hide();
            		cmsTree.refreshNode(newNodePath); // no need to refreshNode as session not been saved.
            	}
            }
        });
		
	}
	
	
    $("#uploadercb").prop('checked',false);
    $("#fileTabContent").hide();
    $("#uploadercb").on("click", function(){
    	if($(this).prop('checked')){
    		fileUploader.enable();
    		$("#fileTabContent").show();
    	}else{
    		fileUploader.disable();
    		$("#fileTabContent").hide();
    	}
    });
    
    //----------------editorTabs------------------------------------
    editorTabs=new AppendTabsUtil();
    editorTabs.init('editorTabs');
    editorTabs.addNewTab('newTab1',"TestTab","<div id=\"newTab1-editor\"></div>");
    editorTabs.addNewTab('newTab2',"TestTab2","test");
    
    
    //-------------------ckeditor-------------------------------
    
    ckeditorUtils=new Object();
    ckeditorUtils['newTab1-editor']=new CKEditorUtil();
    ckeditorUtils['newTab1-editor'].init('newTab1-editor');
	
});




