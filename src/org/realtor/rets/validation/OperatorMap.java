/* $Header: /usr/local/cvsroot/rets/validation/src/org/realtor/rets/validation/OperatorMap.java,v 1.3 2003/12/04 15:28:33 rsegelman Exp $  */
package org.realtor.rets.validation;

import org.apache.log4j.Category;

import org.realtor.rets.validation.operators.*;

import java.io.*;

import java.lang.reflect.Modifier;

import java.net.*;

import java.util.*;
import java.util.Hashtable;
import java.util.jar.*;
import java.util.zip.*;


/**
 *  OperatorMap.java Created Aug 25, 2003
 *
 *
 *  Copyright 2003, Avantia inc.
 *  @version $Revision: 1.3 $
 *  @author scohen
 */
public class OperatorMap {
    private static Hashtable map;
    static Category cat = Category.getInstance(OperatorMap.class);
    private static String packageName = "org.realtor.rets.validation.operators";

    static {
        map = new Hashtable();
        getAllOperators();
    }

    /*
     * Loads all operators in the org.realtor.rets.validation.operators package
     */
    private synchronized static void getAllOperators() {
        ClassLoader loader = OperatorMap.class.getClassLoader();

        URL pkg = loader.getResource(packageName.replace('.', '/'));

        File directory = new File(pkg.getFile());
        String[] files = null;

        if (directory.exists()) {
            files = getClassListFromDirectory(directory);
        } else {
            files = getClassListFromJar(pkg);
        }

        for (int i = 0; i < files.length; i++) {
            // removes the .class extension
            String classname = files[i].substring(0, files[i].length() - 6);

            try {
                // Try to create an instance of the object
                cat.debug("getAllOperators instantiating class= " + classname);

                Object o = Class.forName(classname).newInstance();

                if (o instanceof Operator) {
                    Operator oper = (Operator) o;
                    map.put(oper.getSymbol(), oper);
                }
            } catch (ClassNotFoundException cnfex) {
                cat.error("error:" + classname, cnfex);
            } catch (InstantiationException iex) {
                cat.error("error: " + classname +
                    " We try to instantiate an interface or an object that does not have a default constructor ",
                    iex);

                // We try to instantiate an interface
                // or an object that does not have a
                // default constructor
            } catch (IllegalAccessException iaex) {
                cat.error("error: " + classname + " The class is not public ",
                    iaex);

                // The class is not public
            }
        }
    }

    public static Operator map(String operator) {
        return (Operator) map.get(operator);
    }

    public static void main(String[] args) {
        System.out.println(OperatorMap.map("+"));
    }

    private static String[] getClassListFromJar(URL pkg) {
        String[] files = null;

        try {
            JarURLConnection conn = (JarURLConnection) pkg.openConnection();
            JarFile file = conn.getJarFile();
            Enumeration entries = file.entries();
            ArrayList classList = new ArrayList();

            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryName = entry.getName().replace('/', '.');
                cat.debug("entryName prior to add" + entryName);
                cat.debug("packageName" + packageName);

                if (entryName.startsWith(packageName) &&
                        entryName.endsWith("class")) {
                    cat.debug("adding entryName " + entryName);
                    classList.add(entryName);
                }
            }

            files = (String[]) classList.toArray(new String[classList.size()]);
        } catch (IOException io) {
            cat.error("error creating jarfile ", io);
        }

        return files;
    }

    private static String[] getClassListFromDirectory(File directory) {
        String[] files = directory.list();

        for (int i = 0; i < files.length; i++) {
            // we are only interested in .class files
            if (files[i].endsWith(".class")) {
                files[i] = packageName + "." + files[i];
                cat.debug("getClassListFromDirectory file is " + files[i]);
            }
        }

        return files;
    }
}
