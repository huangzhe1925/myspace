package com.hz.myspace.cms.mgnt.dto.response;

import com.hz.myspace.cms.pojo.mgnt.NodePoint;

public class NodeTreeResponseDTO extends CommonResponseDTO{
	
	private NodePoint nodePoint;

	public NodePoint getNodePoint() {
		return nodePoint;
	}

	public void setNodePoint(NodePoint nodePoint) {
		this.nodePoint = nodePoint;
	}

}
