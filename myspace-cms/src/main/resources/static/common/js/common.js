function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}


String.prototype.startWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substr(0, str.length) == str)
		return true;
	else
		return false;
	return true;
};

String.prototype.endWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substring(this.length - str.length) == str)
		return true;
	else
		return false;
	return true;
};

Array.prototype.del=function(n) {　//n表示第几项，从0开始算起。
	//prototype为对象原型，注意这里为对象增加自定义方法的方法。
	　if(n<0)　//如果n<0，则不进行任何操作。
	return this;
	　else
	return this.slice(0,n).concat(this.slice(n+1,this.length));
}

function CommonUtil(){
	var that=this;
	/**
	 *  help to convert TreeNodePoint into table Object so pass to table view
	 * */
	this.convertTNPropToTabObj=function(properties){
		
		var retObj=new Object();
		
		if(!properties||properties.length==0){
			return retObj;
		}
		
		retObj.tbody=new Array();
		var tr;
		var td;
		var tdValue;
		properties.forEach(function(prop, index ) {
			tr=new Array();
			td={td:[]};
			tdValue={val:prop.propertyName,href:''}; 
			td.td.push(tdValue);
			tr.push(td);
			
			td={td:[]};
			td.td=new Array();
			tdValue={val:prop.propertyType,href:''}; 
			td.td.push(tdValue);
			tr.push(td);
			
			if(prop.propertyStringValues!=undefined&&prop.propertyStringValues.length>0){
				var values="";
				prop.propertyStringValues.forEach(function(propVal, index1 ) {
					if(index1==0){
						values+=propVal;
					}else{
						values+=","+propVal;
					}
				});
				td={td:[]};
				tdValue={val:values,href:''}; 
				td.td.push(tdValue);
				tr.push(td);
			}
			retObj.tbody.push(tr);
		});
		
		return retObj;
	};

};
commonUtil=new CommonUtil();
