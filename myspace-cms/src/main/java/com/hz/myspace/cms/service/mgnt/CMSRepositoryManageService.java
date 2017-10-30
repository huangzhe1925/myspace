package com.hz.myspace.cms.service.mgnt;

import java.io.IOException;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PropertyType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hz.myspace.cms.mgnt.dto.request.FileUploadedCheckResquestDTO;
import com.hz.myspace.cms.mgnt.dto.request.NodeCreationRequestDTO;
import com.hz.myspace.cms.mgnt.dto.request.NodeDeletionRequestDTO;
import com.hz.myspace.cms.mgnt.dto.request.NodePointRequestDTO;
import com.hz.myspace.cms.mgnt.dto.response.CommonResponseDTO;
import com.hz.myspace.cms.mgnt.dto.response.FileUploadResponseDTO;
import com.hz.myspace.cms.mgnt.dto.response.FileUploadedCheckResponseDTO;
import com.hz.myspace.cms.mgnt.dto.response.NodeCreationResponseDTO;
import com.hz.myspace.cms.mgnt.dto.response.NodeDeletionResponseDTO;
import com.hz.myspace.cms.mgnt.dto.response.NodePointResponseDTO;
import com.hz.myspace.cms.pojo.mgnt.NodePoint;
import com.hz.myspace.cms.pojo.mgnt.PropertyPoint;
import com.hz.myspace.cms.util.FileUploaderUtil;
import com.hz.myspace.cms.util.node.NodeManagerUtil;
import com.hz.myspace.common.fileupload.StorageService;

@Service
public class CMSRepositoryManageService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StorageService storageService;

	@Autowired
	private NodeManagerUtil nodeManagerUtil;

	public FileUploadResponseDTO uploadFile(MultipartFile file, String nodeProperyName, String nodeName,
			String nodePath) {

		FileUploadResponseDTO response = new FileUploadResponseDTO();
		if (file.isEmpty()) {
			response.setIsSuccessful(false);
			response.setMessage("empty file");
			return response;
		}
		if (StringUtils.isEmpty(nodePath) || StringUtils.isEmpty(nodeName) || StringUtils.isEmpty(nodeProperyName)) {
			response.setIsSuccessful(false);
			response.setMessage("Node Name or path empty");
			return response;
		}
		if (!nodePath.startsWith("/")) {
			response.setIsSuccessful(false);
			response.setMessage("Node Name or path empty");
			return response;
		}

		logger.info("nodeName ：" + nodeName);
		logger.info("nodePath ：" + nodePath);
		logger.info("nodeProperyName ：" + nodeProperyName);

		String fileName = file.getOriginalFilename();
		logger.info("uploaded file name ：" + fileName);
		String fullFileName = FileUploaderUtil.gernerateFullFileName(nodePath, nodeName, nodeProperyName, fileName);
		logger.info("uploaded full file name ：" + fullFileName);
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		logger.info("uploaded file name suffix ：" + suffixName);

		try {
			storageService.store(file, fullFileName);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		response.setIsSuccessful(true);
		response.setUploadedFileName(fullFileName);

		return response;
	}

	public FileUploadedCheckResponseDTO uploadedCheck(FileUploadedCheckResquestDTO fileUploadedCheckResquestDTO) {
		FileUploadedCheckResponseDTO response = new FileUploadedCheckResponseDTO();
		String nodePath = fileUploadedCheckResquestDTO.getNodePath();
		String nodeName = fileUploadedCheckResquestDTO.getNodeName();
		String fileName = fileUploadedCheckResquestDTO.getFileName();
		String nodeProperyName = fileUploadedCheckResquestDTO.getNodePropertyName();

		if (StringUtils.isEmpty(nodePath) || StringUtils.isEmpty(nodeName) || StringUtils.isEmpty(nodeProperyName)) {
			response.setIsSuccessful(false);
			response.setMessage("Node Name or path empty");
			return response;
		}

		if (!nodePath.startsWith("/")) {
			response.setIsSuccessful(false);
			response.setMessage("Node Name or path empty");
			return response;
		}

		logger.info("checkNodePath ：" + nodePath);
		logger.info("checkNodeName ：" + nodeName);
		logger.info("checkNodeProperyName ：" + nodeProperyName);
		logger.info("checkFileName ：" + fileName);

		String fullFileName = FileUploaderUtil.gernerateFullFileName(nodePath, nodeName, nodeProperyName, fileName);

		Boolean isFileExist = storageService.isFileExist(fullFileName);
		response.setIsSuccessful(true);
		response.setIsFileUploaded(isFileExist);
		if (isFileExist) {
			response.setUploadedFileName(fileName);
			response.setUploadedFileFullName(fullFileName);
		}

		return response;
	}

	public NodePointResponseDTO getNodePoint(NodePointRequestDTO nodePointRequestDTO) {
		NodePointResponseDTO response = new NodePointResponseDTO();
		logger.info("CMSManageservice getNodePoint " + nodePointRequestDTO.getNodePath());
		if (StringUtils.isEmpty(nodePointRequestDTO.getNodePath())) {
			response.setMessage("empty request parameter");
			response.setIsSuccessful(false);
		} else {
			NodePoint np = null;
			try {
				np = new NodePoint();
				np.setNodePath(nodePointRequestDTO.getNodePath());
				this.nodeManagerUtil.getOneNodePoint(np);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				response.setMessage(e.getMessage());
				response.setIsSuccessful(false);
			}
			response.setNodePoint(np);
			response.setIsSuccessful(true);
		}

		return response;
	}

	public NodeDeletionResponseDTO deleteNode(NodeDeletionRequestDTO nodeDeletionRequestDTO) {
		NodeDeletionResponseDTO response = new NodeDeletionResponseDTO();

		String nodePath = nodeDeletionRequestDTO.getNodePath();
		if (StringUtils.isEmpty(nodePath)) {
			response.setIsSuccessful(false);
			response.setMessage("Node Path is empty");
			return response;
		}
		try {
			nodeManagerUtil.deleteNode(nodePath);
		} catch (RuntimeException ex) {
			response.setNodePath(nodePath);
			response.setMessage(ex.getMessage());
			response.setIsSuccessful(Boolean.FALSE);
			return response;
		}

		response.setNodePath(nodePath);
		response.setIsSuccessful(Boolean.TRUE);
		logger.info("deleteNode "+nodePath);
		//saveSession();

		return response;
	}

	public CommonResponseDTO saveSession() {
		CommonResponseDTO commonResponseDTO=new CommonResponseDTO();
		try {
			nodeManagerUtil.getRootNode().getSession().save();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			commonResponseDTO.setIsSuccessful(false);
			commonResponseDTO.setMessage(e.getMessage());
			return commonResponseDTO;
		}
		
		commonResponseDTO.setIsSuccessful(true);
		return commonResponseDTO;
		
	}
	
	public CommonResponseDTO refreshSession(boolean keepChanges) {
		CommonResponseDTO commonResponseDTO=new CommonResponseDTO();
		try {
			nodeManagerUtil.getRootNode().getSession().refresh(keepChanges);;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			commonResponseDTO.setIsSuccessful(false);
			commonResponseDTO.setMessage(e.getMessage());
			return commonResponseDTO;
		}
		commonResponseDTO.setIsSuccessful(true);
		return commonResponseDTO;
	}

	public NodeCreationResponseDTO createNode(NodeCreationRequestDTO nodeCreationRequestDTO) {
		NodeCreationResponseDTO response = new NodeCreationResponseDTO();
		response.setIsSuccessful(false);
		String nodePath = nodeCreationRequestDTO.getNodePath();
		String nodeName = nodeCreationRequestDTO.getNodeName();
		List<PropertyPoint> propList = nodeCreationRequestDTO.getPropList();

		if (StringUtils.isEmpty(nodePath) || StringUtils.isEmpty(nodeName)) {
			response.setIsSuccessful(false);
			response.setMessage("Node Name or path empty");
			return response;
		}

		if (!nodePath.startsWith("/")) {
			response.setIsSuccessful(false);
			response.setMessage("Node Name or path empty");
			return response;
		}

		logger.info("createNodePath ：" + nodePath);
		logger.info("createNodeName ：" + nodeName);
		logger.info("createProperties ：" + propList);

		NodePoint np = new NodePoint();
		np.setNodeName(nodeName);
		np.setNodePath(nodePath);
		np.setProperties(propList);

		Node newNode = nodeManagerUtil.createNode(np);

		if (null != newNode && null != propList) {
			propList.forEach((pp) -> {
				try {
					String propname = pp.getPropertyName();
					int propType = PropertyType.valueFromName(pp.getPropertyType());
					if (PropertyType.BINARY == propType) {
						String fileName = pp.getPropertyStringValues().get(0);
						String fileFullName = FileUploaderUtil.gernerateFullFileName(nodePath, nodeName, propname,
								fileName);
						Binary binFile = FileUploaderUtil.convertResourceToBinary(
								storageService.loadAsResource(fileFullName), newNode.getSession());
						nodeManagerUtil.setNodeBinaryProperty(newNode, propname, binFile);
					} else if (PropertyType.STRING == propType) {
						String[] values = pp.getPropertyStringValues().toArray(new String[] {});
						nodeManagerUtil.setNodeStringProperty(newNode, propname, values);
					}
					response.setIsSuccessful(true);
				} catch (Exception e) {
					response.setIsSuccessful(false);
					response.setMessage(e.getMessage());
					logger.error(e.getMessage(), e);
				}
			});
		}
		
		//saveSession();
		return response;
	}

}
