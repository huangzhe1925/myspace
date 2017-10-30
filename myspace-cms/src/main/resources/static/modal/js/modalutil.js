function ModalUtil(){
	
	this.modalID='';
	this.modalAttr=null;
	this.onSubmitButton=null;
	this.onContinueButton=null;
	this.onCloseButton=null;
	this.onShow=null;
	
	var that=this;

	this.init=function(modalID,modalAttr) {
		that.modalID = modalID;
		that.modalAttr=modalAttr;
		that.initModalData();
		that.registeEvent();
	}
	
	this.registeEvent=function(){
		$("#"+that.modalID+"-closeBtn").on('click',function(){
			that.onInnerCloseButton();
		});
		
		$("#"+that.modalID+"-submitBtn").on('click',function(){
			that.onInnerSubmitButton();
		});
		
		$("#"+that.modalID+"-continueBtn").on('click',function(){
			that.onInnerContinueButton();
		});
		
		$("#"+that.modalID).on('show.bs.modal', function () {
			that.onInnerShow();
		});
	}
	
	this.onInnerContinueButton=function(){
		if(null!=that.onContinueButton){
			that.onContinueButton();
		}
	}
	
	this.onInnerShow=function(){
		if(null!=that.onShow){
			that.onShow();
		}
	}
	
	this.onInnerCloseButton=function(){
		if(null!=that.onCloseButton){
			that.onCloseButton();
		}
	}
	
	this.onInnerSubmitButton=function(){
		if(null!=that.onSubmitButton){
			that.onSubmitButton();
		}
	}
	
	this.show=function(){
		$("#"+that.modalID).modal('show');
	}
	
	this.hide=function(){
		$("#"+that.modalID).modal('hide');
	}
	
	this.initModalData=function(){
		$("#"+that.modalID).modal(that.modalAttr);
	}
	
	this.setModalTitle=function(titleContent){
		$("#"+that.modalID+"-TitleContent").html(titleContent);
	}
	
	this.setModalBodyContent=function(bodyContent){
		$("#"+that.modalID+"-BodyText").html(bodyContent);
	}
	
	this.showLog=function(msg){
		//console.log(msg);	
	}
	
};
