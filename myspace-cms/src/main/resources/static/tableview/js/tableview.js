function TableView(){
	
	this.tableID='';
	var that=this;
	/*
	 * 
	 * table Data like below
	 * {
	 * 	caption:c1,
	 * 	thead:[
	 * 			[{th:[{val:th1,href:h1}]},{th:[{val:th1,href:h1}]},{th:[{val:th1,href:h1}]}], // TR
	 * 		  ],
	 *         
	 * 	tbody:[
	 * 			[{td:[{val:th1,href:h1}]},{td:[{val:th1,href:h1}]},{td:[{val:th1,href:h1}]}], // TR
	 * 			[{td:[{val:th1,href:h1}]},{td:[{val:th1,href:h1}]},{td:[{val:th1,href:h1}]}], // TR
	 * 			[{td:[{val:th1,href:h1}]},{td:[{val:th1,href:h1}]},{td:[{val:th1,href:h1}]}] // TR
	 * 		  ]
	 * 
	 * } 
	 * 
	 * 
	 * */

	this.init=function(tableID) {
		that.tableID = tableID;
	}
	
	this.updateTable=function(tableData){
		var tableSel=$('#' + that.tableID);
		if(!$(tableSel).hasClass("table")){
			$(tableSel).addClass("table");
		}
		if(!$(tableSel).hasClass("table-bordered")){
			$(tableSel).addClass("table-bordered");
		}
		that.clearTableContent();
		that.renderTableHTML(tableData);
		
	}
	
	this.renderTableHTML=function(tableData){
		var tableSel=$('#' + that.tableID);
		var retHTML='';
		if(tableData.caption!=undefined&&tableData.caption){
			retHTML+="<caption>"+tableData.caption+"</caption>";
		}
		 			/* 	thead:[
		   		 * 			[{th:[{val:th1,href:h1}]},{th:[{val:th1,href:h1}]},{th:[{val:th1,href:h1}]}], // TR
		   		 * 		  ],
		   		 * 
		   		 * */
		if(tableData.thead!=undefined&&tableData.thead&&tableData.thead.length==1){
			retHTML+="<thead>";
			tableData.thead.forEach(function(trVal, index ) {
				
				if(trVal.length>0){
					retHTML+="<tr>";
					trVal.forEach(function(thVal, index ) {
						if(thVal.th!=undefined&&thVal.th&&thVal.th.length>0){
							thVal.th.forEach(function(thsVal, index ) {
								retHTML+="<th>";
								if(index==0){
									retHTML+=thsVal.val;
								}else{
									retHTML+=" , "+thsVal.val;
								}
								retHTML+="</th>";
							});
						}
					});
					retHTML+="</tr>";
				}
				
			});
			retHTML+="</thead>";
		}
		
		if(tableData.tbody!=undefined&&tableData.tbody&&tableData.tbody.length>0){
			retHTML+="<tbody>";
			tableData.tbody.forEach(function(trVal, index ) {
				
				if(trVal.length>0){
					retHTML+="<tr>";
					trVal.forEach(function(tdVal, index ) {
						if(tdVal.td!=undefined&&tdVal.td&&tdVal.td.length>0){
							tdVal.td.forEach(function(tdsVal, index ) {
								retHTML+="<td>";
								if(index==0){
									retHTML+=tdsVal.val;
								}else{
									retHTML+=" , "+tdsVal.val;
								}
								retHTML+="</td>";
							});
						}
					});
					retHTML+="</tr>";
				}
			});
			retHTML+="</tbody>";
		}
		
		if(retHTML==''){
			that.createDefaultTable();
		}else{
			$(tableSel).html(retHTML);
		}
		
	}
	
	this.createDefaultTable=function(){
		var tableSel=$('#' + that.tableID);
		that.clearTableContent();
		var tableData={
			  	tbody:[
			  			[{td:[{val:" ",href:""}]},{td:[{val:" ",href:""}]},{td:[{val:" ",href:""}]}], // TR
			  			[{td:[{val:" ",href:""}]},{td:[{val:" ",href:""}]},{td:[{val:" ",href:""}]}], // TR
			  			[{td:[{val:" ",href:""}]},{td:[{val:" ",href:""}]},{td:[{val:" ",href:""}]}] // TR
			  		  ]
			  
			  }; 
		that.renderTableHTML(tableData);
		
	}
	
	this.clearTableContent=function(){
		var tableSel=$('#' + that.tableID);
		$(tableSel).html("");
	}
	
};
