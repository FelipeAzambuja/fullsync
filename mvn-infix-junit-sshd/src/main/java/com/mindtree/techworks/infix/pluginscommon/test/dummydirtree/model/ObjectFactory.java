/*
 * $HeadURL$
 * 
 * Copyright (c) 2010 MindTree Ltd. 
 * 
 * This file is part of Infix Maven Plugins
 * 
 * Infix Maven Plugins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Infix Maven Plugins is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * Infix Maven Plugins. If not, see <http://www.gnu.org/licenses/>.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.09 at 10:02:31 PM MST 
//

package com.mindtree.techworks.infix.pluginscommon.test.dummydirtree.model;

import javax.xml.bind.annotation.XmlRegistry;



/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sourceforge.mvn_infix.pluginscommon.test.dummydirtree package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.sourceforge.mvn_infix.pluginscommon.test.dummydirtree
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DirectoryTree }
     * 
     */
    public DirectoryTree createDirectoryTree() {
        return new DirectoryTree();
    }

    /**
     * Create an instance of {@link Directory }
     * 
     */
    public DirectoryTree.Directory createDirectory() {
        return new DirectoryTree.Directory();
    }

    /**
     * Create an instance of {@link Directory.File }
     * 
     */
    public DirectoryTree.File createDirectoryFile() {
        return new DirectoryTree.File();
    }

}