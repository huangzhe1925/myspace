function TreeView(){
	
	this.treeID='';
	this.requestUrl='';
	this.treeData={};
	this.selNode=null;
	this.onSelected=null;
	this.lastSelectedNodeId = null;
    this.lastSelectTime = null;
    this.doubleClickNode=null;
	var that=this;
		
	this.init=function(treeID,requestUrl) {
		that.treeID = treeID;
		that.requestUrl=requestUrl;
		that.initTreeViewData();
	}
	
	this.getSelectedNodeabsolutePath=function(){
		if(null!=that.selNode){
			if(null!=that.selNode.tags&&that.selNode.tags.length>0){
				return that.selNode.tags[0];
			}
		}
		return null;
	}

	this.initTreeViewData=function(){
		that.getNodePointData('/');
	};
	
	this.innerUpdateTreeDataFromNP=function(treePointData,nodePointData){
		
		if(treePointData==null||nodePointData==null){
			return;
		}
		//create node part
		if(treePointData.tags==undefined){
			that.createNode(treePointData,nodePointData);
			if(nodePointData.childNodes&&nodePointData.childNodes.length>0){
				treePointData.nodes=new Array();
				for(var index=0;index<nodePointData.childNodes.length;index++){
					treePointData.nodes[index]=new Object();
					that.innerUpdateTreeDataFromNP(treePointData.nodes[index],nodePointData.childNodes[index]);
				}
			}
		}//update part
		else{			
			if(treePointData.tags.length>0&&treePointData.tags[0]==nodePointData.nodePath){
				that.updateNode(treePointData,nodePointData);
				
				if(nodePointData.childNodes&&nodePointData.childNodes.length>0){
					var newChildNodesArr=new Array();
					for(var index=0;index<nodePointData.childNodes.length;index++){
						var findTreeNode=that.innerGetNodeInNPS(nodePointData.childNodes[index],treePointData.nodes);
						if(findTreeNode!=null){
							that.updateNode(findTreeNode,nodePointData.childNodes[index]);
							newChildNodesArr.push(findTreeNode);
						}else{
							var newTreePoint=new Object();
							//treePointData.nodes.push(newTreePoint);
							newChildNodesArr.push(newTreePoint);
							that.createNode(newTreePoint,nodePointData.childNodes[index]);
						}
					}
					treePointData.nodes=newChildNodesArr;
				}
			}else{
				if(treePointData.nodes!=undefined&&treePointData.nodes.length>0){
					for(var index=0;index<treePointData.nodes.length;index++){
						that.innerUpdateTreeDataFromNP(treePointData.nodes[index],nodePointData);
					}
				}
			}
		}
	}
	
	this.deleteTreeNode=function(nodePath){
		that.innerDeleteTreeNode(that.treeData,nodePath);
		console.log("deleted node path "+nodePath);
		that.refreshTree();
	}
	
	this.innerDeleteTreeNode=function(treeNode,nodePath){
		
		if(treeNode.tags!=undefined&&treeNode.tags.length>0&&treeNode.tags[0]==nodePath){
			return true;
		}else{
			if(treeNode.nodes!=undefined&&treeNode.nodes.length>0){
				for(var index=0;index<treeNode.nodes.length;index++){
					var nodeFound=that.innerDeleteTreeNode(treeNode.nodes[index],nodePath);
					if(nodeFound==true){
						delete treeNode.nodes[index];
						return false;
					}
				}
			}
		}
		
		return false;
	}
	
	this.innerGetNodeInNPS=function(childNode,nodes){
		var retNode=null;
		if(nodes==undefined||nodes.length==0){
			return retNode;
		}
		
		for(var index=0;index<nodes.length;index++){
			if(nodes[index].tags!=undefined&&nodes[index].tags[0]==childNode.nodePath){
				retNode=nodes[index]
				break;
			}
		}
		return retNode;
	}
	
	this.getTreeNodeOnPath=function(treeNode,nodePath){
		
		var retNode=null; 
		
		if(treeNode.tags!=undefined&&treeNode.tags.length>0&&treeNode.tags[0]==nodePath){
			retNode = treeNode;
		}else{
			
			if(treeNode.nodes!=undefined&&treeNode.nodes.length>0){
				for(var index=0;index<treeNode.nodes.length;index++){
					retNode = that.getTreeNodeOnPath(treeNode.nodes[index],nodePath);
					if(retNode!=null){
						break;
					}
				}
			}
		}
		
		return retNode;
	}
	
	this.updateNode=function(newTreeNode,nodePointData){
		newTreeNode.text="/"+nodePointData.nodeName;
		
		if(nodePointData.properties){
			newTreeNode.properties=new Array();
			for(var index=0;index<nodePointData.properties.length;index++){
				newTreeNode.properties[index]=new Object();
				that.updateNodeProperty(newTreeNode.properties[index],nodePointData.properties[index]);
			}
		}
	}
	
	this.createNode=function(newTreeNode,nodePointData){
		newTreeNode.text="/"+nodePointData.nodeName;
		if(newTreeNode.text=="/"){
			newTreeNode.state={expanded:true};
		}
		newTreeNode.selectable=true;
		newTreeNode.tags=new Array();
		newTreeNode.tags[0]=nodePointData.nodePath;
		
		newTreeNode.nodes=[];
		
		if(nodePointData.properties){
			newTreeNode.properties=new Array();
			for(var index=0;index<nodePointData.properties.length;index++){
				newTreeNode.properties[index]=new Object();
				that.updateNodeProperty(newTreeNode.properties[index],nodePointData.properties[index]);
			}
		}
	}
	
	this.updateNodeProperty=function(newNodeProperty,nodePointPropData){
		newNodeProperty.propertyName=nodePointPropData.propertyName;
		newNodeProperty.propertyStringValues = nodePointPropData.propertyStringValues;
		newNodeProperty.propertyType = nodePointPropData.propertyType;
	}
	
	this.updateNodeState=function(eventType,treeNode, eventNode){
		
		if(eventType=='nodeSelected'){
			var tempSelNode= that.getTreeNodeOnPath(treeNode,eventNode.tags[0]);
			if(tempSelNode==null){
				return;
			}
			if(that.selNode!=null&&that.selNode.state!=undefined){
				delete that.selNode.state.selected;
			}
			if(tempSelNode.state==undefined){
				tempSelNode.state={};
			}
			tempSelNode.state.selected=true;
			that.selNode=tempSelNode;
			
			if(that.onSelected!=null){
				that.onSelected(that.selNode);
			}
			return;
		}
		
		if(eventType=='nodeUnselected'){
			var tempSelNode= that.getTreeNodeOnPath(treeNode,eventNode.tags[0]);
			if(tempSelNode==null){
				return;
			}
			if(that.selNode!=null&&that.selNode.state!=undefined){
				delete that.selNode.state.selected;
			}
			if(tempSelNode.state==undefined){
				tempSelNode.state={};
			}
			tempSelNode.state.selected=false;
			that.selNode=null;
		}
		
		if(eventType=='nodeExpanded'){
			var tempExpNode= that.getTreeNodeOnPath(treeNode,eventNode.tags[0]);
			if(tempExpNode==null){
				return;
			}
			if(tempExpNode.state==undefined){
				tempExpNode.state={};
			}
			tempExpNode.state.expanded=true;
			return;
		}
		
	}
	
	this.showLog=function(msg){
		//console.log(msg);	
	}
	

	this.getNodePointData=function(nodePath) {
		var inputData = {'nodePath' : nodePath };
		$.ajax({
			type : 'POST',
			url : that.requestUrl,
			dataType : 'json',
			contentType : 'application/json;charset=utf-8',
			data : JSON.stringify(inputData),
			success : function(data) {
				that.showLog(data);
				if(data.isSuccessful){
					that.innerUpdateTreeDataFromNP(that.treeData,data.nodePoint);
					that.refreshTree();
				}else{
					that.showLog(data);
				}
			},
			error : function(msg) {
				that.showLog(msg);
			}
		});
	}
	
	this.refreshNode=function(nodePath){
		that.getNodePointData(nodePath);
	}
	
	this.refreshTree=function() {
		$('#' + that.treeID).treeview({
			color : "#428bca",
			data : [that.treeData],
			onNodeSelected: function(event, data) {
				that.showLog(event);
				that.showLog(data);
				that.innerClickNode(event, data);
				that.updateNodeState(event.type,that.treeData,data);
			 },
			 onNodeUnselected: function(event, data){
				 that.showLog(event);
				that.showLog(data);
				that.innerClickNode(event, data);
				that.updateNodeState(event.type,that.treeData,data);
			 },
			 onNodeExpanded: function(event, data) {
				 that.showLog(event);
				 that.showLog(data);
				 var tempExpNode= that.getTreeNodeOnPath(that.treeData,data.tags[0]);
				 if(tempExpNode.state==undefined||!tempExpNode.state.expanded){
					 that.updateNodeState(event.type,tempExpNode,data);
					 that.getNodePointData(data.tags[0]);
				 }else{
					 that.updateNodeState(event.type,tempExpNode,data);
				 }
			 }
		});
	}
	
	this.innerDoubleClickNode=function(data){
    	if(null!=that.doubleClickNode){
    		that.doubleClickNode(data);
    	}
    }

    this.innerClickNode=function(event, data) {
    	//console.log("enter innerClickNode");
        if (that.lastSelectedNodeId && that.lastSelectTime) {
            var time = new Date().getTime();
            var t = time - that.lastSelectTime;
            console.log("time "+t);
            if (that.lastSelectedNodeId == data.nodeId && t < 400) {
                that.innerDoubleClickNode(data);
            }
        }
        
        that.lastSelectedNodeId = data.nodeId;
        that.lastSelectTime = new Date().getTime();
    }

};
