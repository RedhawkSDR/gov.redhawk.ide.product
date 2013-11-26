/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.product.ide.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Tests the ide.product file to ensure custom changes are not overwritten by
 * the Product Editor.
 */
public class IdeProductDefinitionTest {

	private DocumentBuilder builder;
	private List<Node> configurationElements;

	/**
	 * Parse the ide.product file and build list of items under the
	 * configurations node.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		this.builder = factory.newDocumentBuilder();
		Document document = this.parseIdeProduct();
		this.buildConfigElementList(document);

	}

	/**
	 * Tear down the builder and configElements list between tests.
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.builder = null;
		this.configurationElements = null;
	}

	/**
	 * Tests for presence of a property element in the configuration section of
	 * the ide.product file.
	 */
	@Test
	public void isConfigurationPropertyElementPresent() {
		Assert.assertNotNull("The ide.product file must contain a configuration/property element", this.configurationElements);
	}

	/**
	 * Tests for presence and correct value of the eclipse.buildId property.
	 */
	@Test
	public void containsEclipseBuildIdProperty() {
		Element buildIdElement = null;
		for (Node node : this.configurationElements) {
			if (node instanceof Element) {
				Element temp = (Element) node;
				if ("property".equals(temp.getNodeName()) && "eclipse.buildId".equals(temp.getAttribute("name"))) {
					buildIdElement = temp;
					break;
				}
			}
		}
		Assert.assertNotNull("The ide.product file must contain an eclipse.buildId property", buildIdElement);
		Assert.assertNotNull("The eclipse.buildId property must have a value", buildIdElement.getAttribute("value"));
	}

	/**
	 * Parses the ide.product file.
	 * 
	 * @return the {@link Document} resulting from parsing the ide.product file
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document parseIdeProduct() throws SAXException, IOException {
		return this.builder.parse(FileLocator.toFileURL(FileLocator.find(Platform.getBundle("gov.redhawk.product.ide"), new Path("ide.product"), null)).toString());
	}

	/**
	 * Builds a list of {@link Node} under the "configurations" element.
	 * 
	 * @param document the {@link Document} associated with the ide.product file
	 */
	private void buildConfigElementList(Document document) {
		Element root = document.getDocumentElement();
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Element child = (Element) node;
				if ("configurations".equals(child.getNodeName())) {
					NodeList configurationsNodes = child.getChildNodes();
					for (int j = 0; j < configurationsNodes.getLength(); j++) {
						if ("property".equals(configurationsNodes.item(j).getNodeName())) {
							if (this.configurationElements == null) {
								this.configurationElements = new ArrayList<Node>();
							}
							this.configurationElements.add(configurationsNodes.item(j));
						}
					}
				}
			}
		}
	}
}
