package com.wangrg.java_lib.web;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * by wangrongjun on 2017/4/3.
 */
public class WebXmlFileCreator {

    public static String create(List<Info> infoList) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("web-app");
        root.setAttribute("xmlns", "http://java.sun.com/xml/ns/javaee");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee" + " " +
                "http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd");

        for (Info info : infoList) {
            Element servlet = document.createElement("servlet");
            Element servletName = document.createElement("servlet-name");
            servletName.setTextContent(info.getServletName());
            Element servletClass = document.createElement("servlet-class");
            servletClass.setTextContent(info.getClassName());
            servlet.appendChild(servletName);
            servlet.appendChild(servletClass);

            Element servletMapping = document.createElement("servlet-mapping");
            servletName = document.createElement("servlet-name");
            servletName.setTextContent(info.getServletName());
            Element urlPattern = document.createElement("url-pattern");
            urlPattern.setTextContent(info.getUrlPattern());
            servletMapping.appendChild(servletName);
            servletMapping.appendChild(urlPattern);

            root.appendChild(servlet);
            root.appendChild(servletMapping);
        }

        document.appendChild(root);

        OutputFormat format = new OutputFormat(document);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer writer = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(writer, format);
        serializer.serialize(document);
        return writer.toString();
    }

    public static class Info {
        /**
         * servlet-name,such as "Login"
         */
        private String servletName;
        /**
         * servlet-class,such as "servlet.Login"
         */
        private String className;
        /**
         * url-pattern,such as "/login"
         */
        private String urlPattern;

        public Info(String servletName, String className, String urlPattern) {
            this.servletName = servletName;
            this.className = className;
            this.urlPattern = urlPattern;
        }

        public String getServletName() {
            return servletName;
        }

        public String getClassName() {
            return className;
        }

        public String getUrlPattern() {
            return urlPattern;
        }
    }

}
