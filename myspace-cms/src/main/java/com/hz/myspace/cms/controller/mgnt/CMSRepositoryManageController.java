package com.hz.myspace.cms.controller.mgnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.hz.myspace.cms.service.mgnt.CMSRepositoryManageService;
import com.hz.myspace.cms.util.session.impl.SessionManager;

@Controller
@RequestMapping(value = "cmsrepomgnt")
public class CMSRepositoryManageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CMSRepositoryManageService cmsRepositoryManageService;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public FileUploadResponseDTO uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("nodeProperyName") String nodeProperyName, @RequestParam("nodeName") String nodeName,
			@RequestParam("nodePath") String nodePath) {

		logger.info("CMSRepositoryManageController uploadFile entered ");

		FileUploadResponseDTO response = cmsRepositoryManageService.uploadFile(file, nodeProperyName, nodeName,
				nodePath);

		logger.info("CMSRepositoryManageController uploadFile exited ");

		return response;
	}
	
	@RequestMapping(value = "uploadedfilecheck", method = RequestMethod.POST)
	@ResponseBody
	public FileUploadedCheckResponseDTO uploadedCheck(
			@RequestBody FileUploadedCheckResquestDTO fileUploadedCheckResquestDTO) {

		FileUploadedCheckResponseDTO response = cmsRepositoryManageService.uploadedCheck(fileUploadedCheckResquestDTO);

		return response;
	}

	@RequestMapping(value = "createnode", method = RequestMethod.POST)
	@ResponseBody
	public NodeCreationResponseDTO createNode(@RequestBody NodeCreationRequestDTO nodeCreationRequestDTO,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		extractCookieValues(httpRequest);
		NodeCreationResponseDTO response = cmsRepositoryManageService.createNode(nodeCreationRequestDTO);
		return response;
	}

	@RequestMapping(value = "deletenode", method = RequestMethod.POST)
	@ResponseBody
	public NodeDeletionResponseDTO deleteNode(@RequestBody NodeDeletionRequestDTO nodeDeletionRequestDTO,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		extractCookieValues(httpRequest);
		NodeDeletionResponseDTO response = cmsRepositoryManageService.deleteNode(nodeDeletionRequestDTO);
		return response;
	}

	@RequestMapping(value = "savesession", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponseDTO saveSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		extractCookieValues(httpRequest);
		CommonResponseDTO commonResponseDTO = cmsRepositoryManageService.saveSession();
		return commonResponseDTO;
	}

	@RequestMapping(value = "refreshsession", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponseDTO refreshSession(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		extractCookieValues(httpRequest);
		CommonResponseDTO commonResponseDTO = cmsRepositoryManageService.refreshSession(false);
		return commonResponseDTO;
	}

	@RequestMapping(value = "getnodepoint")
	@ResponseBody
	public NodePointResponseDTO getNodePoint(@RequestBody NodePointRequestDTO nodePointRequestDTO,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Map<String, String> cookieValues=extractCookieValues(httpRequest);
		NodePointResponseDTO response = cmsRepositoryManageService.getNodePoint(nodePointRequestDTO);
		setCookieValues(cookieValues,httpResponse);
		return response;
	}
	
	private void setCookieValues(Map<String,String> headerValues, HttpServletResponse httpResponse){
		
		List<Cookie> cookieList=new ArrayList<>();
		
		String sessionKey=SessionManager.sessionKey.get();
		if(!headerValues.containsKey(SessionManager.SESSION_KEY_ATTRIBUTE)||
				!headerValues.get(SessionManager.SESSION_KEY_ATTRIBUTE).equals(sessionKey)){
			if(!StringUtils.isEmpty(sessionKey)){
				Cookie cookie = new Cookie(SessionManager.SESSION_KEY_ATTRIBUTE, sessionKey);
			    cookie.setMaxAge(1000); //set cookie expiration as 10s
			    cookieList.add(cookie);
			}
		}
		cookieList.forEach((cook)->{
			httpResponse.addCookie(cook);
		});
	}
	
	private Map<String, String> extractCookieValues(HttpServletRequest httpRequest){
		Map<String,String> valueMap=new HashMap<String,String>();
		Cookie[] cookieArr=httpRequest.getCookies();//(SessionManager.SESSION_KEY_ATTRIBUTE);
		if(null==cookieArr){
			return valueMap;
		}
		for(Cookie coo:cookieArr){
			if(SessionManager.SESSION_KEY_ATTRIBUTE.equals(coo.getName())){
				String sessionkey=coo.getValue();
				if(!StringUtils.isEmpty(sessionkey)){
					valueMap.put(SessionManager.SESSION_KEY_ATTRIBUTE, sessionkey);
					SessionManager.sessionKey.set(sessionkey);
				}
				break;
			}
		}
		
		return valueMap;
	}

}
