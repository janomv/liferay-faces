/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.bridge.renderkit.html_basic;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import com.liferay.faces.bridge.BridgeConstants;
import com.liferay.faces.util.application.ResourceConstants;
import com.liferay.faces.util.lang.StringPool;


/**
 * @author  Neil Griffin
 */
public class HeadResource {

	// Private Data Members
	private List<HeadResourceAttribute> attributeList;
	private boolean duplicate;
	private String facesResource;
	private String facesLibrary;
	private String text;
	private String type;
	private String url;

	public HeadResource(String type, String url) {

		if (type != null) {
			type = type.toLowerCase();
		}

		this.type = type;
		this.url = url;
		initialize();
	}

	public HeadResource(String type, Attributes attributes) {

		if (type != null) {
			type = type.toLowerCase();
		}

		this.type = type;

		if (attributes != null) {
			this.attributeList = new ArrayList<HeadResourceAttribute>();

			int totalAttributes = attributes.getLength();

			for (int i = 0; i < totalAttributes; i++) {
				String name = attributes.getLocalName(i);
				String value = attributes.getValue(i);
				HeadResourceAttribute headResourceAttribute = new HeadResourceAttribute(name, value);
				this.attributeList.add(headResourceAttribute);
			}
		}

		if (StringPool.LINK.equals(type)) {
			url = attributes.getValue(StringPool.HREF);
		}
		else if (StringPool.SCRIPT.equals(type)) {
			url = attributes.getValue(BridgeConstants.SRC);
		}

		initialize();
	}

	@Override
	public boolean equals(Object headResource) {

		boolean equal = false;
		HeadResource otherHeadResource = (HeadResource) headResource;

		if ((url != null) && url.equals(otherHeadResource.getURL())) {
			equal = true;
		}
		else if ((StringPool.LINK.equals(type) || StringPool.SCRIPT.equals(type)) && type.equals(otherHeadResource.getType())) {

			String facesResource2 = otherHeadResource.getFacesResource();
			String facesLibrary2 = otherHeadResource.getFacesLibrary();

			if ((facesResource != null) && facesResource.equals(facesResource2)) {

				if ((facesLibrary != null) && facesLibrary.equals(facesLibrary2)) {
					equal = true;
				}
			}
		}

		return equal;
	}

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(StringPool.LESS_THAN);
		stringBuilder.append(type);

		if (attributeList != null) {

			for (HeadResourceAttribute headResourceAttribute : attributeList) {
				stringBuilder.append(StringPool.SPACE);
				stringBuilder.append(headResourceAttribute.getName());
				stringBuilder.append(StringPool.EQUAL);
				stringBuilder.append(StringPool.QUOTE);
				stringBuilder.append(headResourceAttribute.getValue());
				stringBuilder.append(StringPool.QUOTE);
			}
		}

		stringBuilder.append(StringPool.GREATER_THAN);

		if (text != null) {
			stringBuilder.append(text);
		}

		stringBuilder.append(StringPool.LESS_THAN);
		stringBuilder.append(StringPool.FORWARD_SLASH);
		stringBuilder.append(type);
		stringBuilder.append(StringPool.GREATER_THAN);

		return stringBuilder.toString();
	}

	protected void initialize() {

		if (url != null) {

			int queryPos = url.indexOf(StringPool.QUESTION);

			if (queryPos > 0) {
				String parameters = url.substring(queryPos + 1);
				String[] nameValuePairs = parameters.split(BridgeConstants.REGEX_AMPERSAND_DELIMITER);

				for (String nameValuePair : nameValuePairs) {
					int equalsPos = nameValuePair.indexOf(StringPool.EQUAL);

					if (equalsPos > 0) {
						String name = nameValuePair.substring(0, equalsPos);

						if (name.endsWith(ResourceConstants.JAVAX_FACES_RESOURCE)) {
							facesResource = nameValuePair.substring(equalsPos + 1);
						}
						else if (name.endsWith(ResourceConstants.LN)) {
							facesLibrary = nameValuePair.substring(equalsPos + 1);
						}
					}

					if ((facesResource != null) && (facesLibrary != null)) {
						break;
					}
				}
			}
		}
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public String getFacesLibrary() {
		return facesLibrary;
	}

	public String getFacesResource() {
		return facesResource;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public String getURL() {
		return url;
	}
}
