package com.hz.myspace.cms.util.node;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hz.myspace.cms.pojo.mgnt.NodePoint;
import com.hz.myspace.cms.pojo.mgnt.PropertyPoint;
import com.hz.myspace.cms.util.session.impl.SessionManager;

@Component
public class NodeManagerUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionManager sessionManager;

	public Node createNode(NodePoint np) {
		Session adminSession = null;
		Node newNode = null;
		try {
			adminSession = sessionManager.getAdminSession();

			if (!isNodeExist(np.getNodePath())) {
				return newNode;
			}
			String newPath = np.getNodePath() + "/" + np.getNodeName();
			if (isNodeExist(newPath)) {
				return newNode;
			}

			Node node = adminSession.getNode(np.getNodePath());
			newNode = node.addNode(np.getNodeName());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return newNode;
	}

	public Node getRootNode() {
		Session adminSession = null;
		Node node = null;
		try {
			adminSession = sessionManager.getAdminSession();
			node = adminSession.getRootNode();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return node;
	}

	public Boolean setNodeStringProperty(Node node, String propName, String... values) {
		Boolean isSuccess = false;
		try {
			if (values == null) {
				return isSuccess;
			}
			logger.info("values : " + values.toString());
			if (values.length == 1) {
				node.setProperty(propName, values[0].toString());
			} else {
				node.setProperty(propName, values);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return isSuccess;
	}

	public Boolean setNodeBinaryProperty(Node node, String propName, Binary value) {
		Boolean isSuccess = false;
		try {
			node.setProperty(propName, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return isSuccess;
	}

	public NodePoint getOneNodePoint(NodePoint np) throws LoginException, RepositoryException {

		Session adminSession = null;
		String nodePath = np.getNodePath();
		logger.info("RepositoryUtil getOneNodePoint " + nodePath);

		if (StringUtils.isEmpty(nodePath)) {
			return np;
		}

		adminSession = sessionManager.getAdminSession();
		Node node = adminSession.getNode(nodePath);
		if (null != node) {
			np.setNodeName(node.getName());
			logger.info("RepositoryUtil getOneNodePoint " + node.getName());
			NodeIterator nodeIterator = node.getNodes();
			List<NodePoint> childNodeList = new ArrayList<NodePoint>();
			np.setChildNodes(childNodeList);
			while (nodeIterator.hasNext()) {
				Node childNode = nodeIterator.nextNode();
				NodePoint newNodePoint = new NodePoint();
				newNodePoint.setNodeName(childNode.getName());
				newNodePoint.setNodePath(childNode.getPath());
				newNodePoint.setProperties(getNodePointProperties(newNodePoint));
				childNodeList.add(newNodePoint);
			}

			List<PropertyPoint> propertiesList = getNodePointProperties(np);
			np.setProperties(propertiesList);

		} else {
			np.setChildNodes(null);
			np.setProperties(null);
		}

		return np;
	}

	public List<PropertyPoint> getNodePointProperties(NodePoint np) {

		Session adminSession;
		String nodePath = np.getNodePath();
		logger.info("RepositoryUtil getNodePointProperties " + nodePath);

		if (StringUtils.isEmpty(nodePath)) {
			return null;
		}

		List<PropertyPoint> propertiesList = new ArrayList<PropertyPoint>();

		try {
			adminSession = sessionManager.getAdminSession();
			Node node = adminSession.getNode(nodePath);
			PropertyIterator propertyIterator = node.getProperties();

			while (propertyIterator.hasNext()) {
				Property prop = propertyIterator.nextProperty();
				PropertyPoint newPropertyPoint = new PropertyPoint();
				newPropertyPoint.setPropertyName(prop.getName());
				newPropertyPoint.setPropertyType(PropertyType.nameFromValue(prop.getType()));
				List<String> propStringValues = new ArrayList<String>();
				if (prop.getType() == PropertyType.BINARY) {
					propStringValues.add(prop.getPath());
				}
				if (prop.getType() == PropertyType.STRING || prop.getType() == PropertyType.NAME) {
					if (prop.isMultiple()) {
						for (Value val : prop.getValues()) {
							propStringValues.add(val.getString());
						}
					} else {
						propStringValues.add(prop.getString());
					}
				}
				newPropertyPoint.setPropertyStringValues(propStringValues);
				propertiesList.add(newPropertyPoint);
			}

		} catch (Exception e) {
			propertiesList = null;
			logger.error(e.getMessage(), e);
		}

		return propertiesList;
	}

	public Boolean isNodeExist(String nodePath) {
		Boolean retVal = false;
		Session adminSession = null;
		try {
			adminSession = sessionManager.getAdminSession();
			retVal = adminSession.nodeExists(nodePath);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retVal;
	}

	public void deleteNode(String nodePath) {
		if (!isNodeExist(nodePath)) {
			return;
		}
		Session adminSession = null;
		try {
			adminSession = sessionManager.getAdminSession();
			Node node = adminSession.getNode(nodePath);
			node.remove();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getNodeStringProperty(String nodePath, String propertyName) {

		String retValue = null;
		Session adminSession = null;

		try {
			adminSession = sessionManager.getAdminSession();
			Node node = adminSession.getNode(nodePath);

			if (null != node) {
				if (node.hasProperty(propertyName)) {
					retValue = node.getProperty(propertyName).getValue().getString();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return retValue;
	}
}
