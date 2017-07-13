package com.yanyan.core.sml;

import org.dom4j.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 11:29
 */
public class XmlObject implements Element {
    public QName getQName() {
        return null;
    }

    public void setQName(QName qName) {

    }

    public Namespace getNamespace() {
        return null;
    }

    public QName getQName(String s) {
        return null;
    }

    public Namespace getNamespaceForPrefix(String s) {
        return null;
    }

    public Namespace getNamespaceForURI(String s) {
        return null;
    }

    public List getNamespacesForURI(String s) {
        return null;
    }

    public String getNamespacePrefix() {
        return null;
    }

    public String getNamespaceURI() {
        return null;
    }

    public String getQualifiedName() {
        return null;
    }

    public List additionalNamespaces() {
        return null;
    }

    public List declaredNamespaces() {
        return null;
    }

    public Element addAttribute(String s, String s1) {
        return null;
    }

    public Element addAttribute(QName qName, String s) {
        return null;
    }

    public Element addComment(String s) {
        return null;
    }

    public Element addCDATA(String s) {
        return null;
    }

    public Element addEntity(String s, String s1) {
        return null;
    }

    public Element addNamespace(String s, String s1) {
        return null;
    }

    public Element addProcessingInstruction(String s, String s1) {
        return null;
    }

    public Element addProcessingInstruction(String s, Map map) {
        return null;
    }

    public Element addText(String s) {
        return null;
    }

    public void add(Attribute attribute) {

    }

    public void add(CDATA cdata) {

    }

    public void add(Entity entity) {

    }

    public void add(Text text) {

    }

    public void add(Namespace namespace) {

    }

    public boolean remove(Attribute attribute) {
        return false;
    }

    public boolean remove(CDATA cdata) {
        return false;
    }

    public boolean remove(Entity entity) {
        return false;
    }

    public boolean remove(Namespace namespace) {
        return false;
    }

    public boolean remove(Text text) {
        return false;
    }

    public boolean supportsParent() {
        return false;
    }

    public Element getParent() {
        return null;
    }

    public void setParent(Element element) {

    }

    public Document getDocument() {
        return null;
    }

    public void setDocument(Document document) {

    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean hasContent() {
        return false;
    }

    public String getName() {
        return null;
    }

    public void setName(String s) {

    }

    public String getText() {
        return null;
    }

    public void setText(String s) {

    }

    public String getTextTrim() {
        return null;
    }

    public String getStringValue() {
        return null;
    }

    public String getPath() {
        return null;
    }

    public String getPath(Element element) {
        return null;
    }

    public String getUniquePath() {
        return null;
    }

    public String getUniquePath(Element element) {
        return null;
    }

    public String asXML() {
        return null;
    }

    public void write(Writer writer) throws IOException {

    }

    public short getNodeType() {
        return 0;
    }

    public String getNodeTypeName() {
        return null;
    }

    public Node detach() {
        return null;
    }

    public List selectNodes(String s) {
        return null;
    }

    public Object selectObject(String s) {
        return null;
    }

    public List selectNodes(String s, String s1) {
        return null;
    }

    public List selectNodes(String s, String s1, boolean b) {
        return null;
    }

    public Node selectSingleNode(String s) {
        return null;
    }

    public String valueOf(String s) {
        return null;
    }

    public Number numberValueOf(String s) {
        return null;
    }

    public boolean matches(String s) {
        return false;
    }

    public XPath createXPath(String s) throws InvalidXPathException {
        return null;
    }

    public Node asXPathResult(Element element) {
        return null;
    }

    public void accept(Visitor visitor) {

    }

    public Object clone() {
        return null;
    }

    public Object getData() {
        return null;
    }

    public void setData(Object o) {

    }

    public List attributes() {
        return null;
    }

    public void setAttributes(List list) {

    }

    public int attributeCount() {
        return 0;
    }

    public Iterator attributeIterator() {
        return null;
    }

    public Attribute attribute(int i) {
        return null;
    }

    public Attribute attribute(String s) {
        return null;
    }

    public Attribute attribute(QName qName) {
        return null;
    }

    public String attributeValue(String s) {
        return null;
    }

    public String attributeValue(String s, String s1) {
        return null;
    }

    public String attributeValue(QName qName) {
        return null;
    }

    public String attributeValue(QName qName, String s) {
        return null;
    }

    @Deprecated
    public void setAttributeValue(String s, String s1) {

    }

    @Deprecated
    public void setAttributeValue(QName qName, String s) {

    }

    public Element element(String s) {
        return null;
    }

    public Element element(QName qName) {
        return null;
    }

    public List elements() {
        return null;
    }

    public List elements(String s) {
        return null;
    }

    public List elements(QName qName) {
        return null;
    }

    public Iterator elementIterator() {
        return null;
    }

    public Iterator elementIterator(String s) {
        return null;
    }

    public Iterator elementIterator(QName qName) {
        return null;
    }

    public boolean isRootElement() {
        return false;
    }

    public boolean hasMixedContent() {
        return false;
    }

    public boolean isTextOnly() {
        return false;
    }

    public void appendAttributes(Element element) {

    }

    public Element createCopy() {
        return null;
    }

    public Element createCopy(String s) {
        return null;
    }

    public Element createCopy(QName qName) {
        return null;
    }

    public String elementText(String s) {
        return null;
    }

    public String elementText(QName qName) {
        return null;
    }

    public String elementTextTrim(String s) {
        return null;
    }

    public String elementTextTrim(QName qName) {
        return null;
    }

    public Node getXPathResult(int i) {
        return null;
    }

    public Node node(int i) throws IndexOutOfBoundsException {
        return null;
    }

    public int indexOf(Node node) {
        return 0;
    }

    public int nodeCount() {
        return 0;
    }

    public Element elementByID(String s) {
        return null;
    }

    public List content() {
        return null;
    }

    public Iterator nodeIterator() {
        return null;
    }

    public void setContent(List list) {

    }

    public void appendContent(Branch branch) {

    }

    public void clearContent() {

    }

    public List processingInstructions() {
        return null;
    }

    public List processingInstructions(String s) {
        return null;
    }

    public ProcessingInstruction processingInstruction(String s) {
        return null;
    }

    public void setProcessingInstructions(List list) {

    }

    public Element addElement(String s) {
        return null;
    }

    public Element addElement(QName qName) {
        return null;
    }

    public Element addElement(String s, String s1) {
        return null;
    }

    public boolean removeProcessingInstruction(String s) {
        return false;
    }

    public void add(Node node) {

    }

    public void add(Comment comment) {

    }

    public void add(Element element) {

    }

    public void add(ProcessingInstruction processingInstruction) {

    }

    public boolean remove(Node node) {
        return false;
    }

    public boolean remove(Comment comment) {
        return false;
    }

    public boolean remove(Element element) {
        return false;
    }

    public boolean remove(ProcessingInstruction processingInstruction) {
        return false;
    }

    public void normalize() {

    }
}
