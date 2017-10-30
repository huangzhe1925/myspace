function InputTableGroups(){
	
	this.inputTableGroupsID='';
	this.appendBtnID='';
	this.rowIndex=1;
	
	var that=this;

	this.init=function(inputTableGroupsID) {
		that.inputTableGroupsID=inputTableGroupsID;
		that.appendBtnID=inputTableGroupsID+'-appendBtn';
		that.bindAppendBtn();
	}

	this.bindAppendBtn=function(){
		$("#"+that.appendBtnID).on('click',function(){
			that.appendTableInputHTML();
		});
	}
	
	this.appendTableInputHTML=function(){
		var inputHtml="<tr>" +
						"<td> key: </td>" +
						"<td> <input type='text' class='form-control propKey' placeholder='key'></td>" +
						"<td> &nbsp;value: </td>"+
						"<td> <input type='text' class='form-control propValue' placeholder='value'> </td>"+
						"<td> &nbsp;&nbsp;<button class='btn-danger row-"+that.rowIndex+"' >[-]</button></td>"+
					   "</tr>";
		$("#"+that.inputTableGroupsID).append(inputHtml);
		
		$("#"+that.inputTableGroupsID+" .row-"+that.rowIndex).on("click",function(){
			$(this).parents("tr").remove();
		});
		
		that.rowIndex=that.rowIndex+1;
		//console.log(that.getInputValues());
	}
	
	this.getInputValues=function(){
		var keyValArr=new Array();
		$("#"+that.inputTableGroupsID+" tr").each(function(index){
			var key=$(this).find(".propKey").val();
			var val=$(this).find(".propValue").val();
			var vals=val.split(",");
			keyValArr.push({"key":key,'values':vals});
		});
		return keyValArr;
	}
	
	this.reset=function(){
		$("#"+that.inputTableGroupsID+" tr").not(":eq(0)").remove();
		$("#"+that.inputTableGroupsID+" input").val('');
	}
		
	this.showLog=function(msg){
		console.log(msg);	
	}
	

};
