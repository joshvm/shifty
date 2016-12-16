package ca.mohawk.jdw.shifty.server.utils;

/*
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import ca.mohawk.jdw.shifty.companyapi.Company;
import ca.mohawk.jdw.shifty.companyapi.creator.CompanyCreator;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public final class CompanyLoader {

    private CompanyLoader(){}

    public static Company load(final File jarFile) throws Exception {
        final JarFile jar = new JarFile(jarFile);
        final Enumeration<JarEntry> entries = jar.entries();
        final URLClassLoader cl = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
        while(entries.hasMoreElements()){
            final JarEntry entry = entries.nextElement();
            if(!entry.getName().endsWith(".class"))
                continue;
            final Class clazz = cl.loadClass(entry.getName().replace(".class", "").replace('/', '.'));
            if(clazz.isInterface())
                continue;
            final Optional<Class> loaderClassOpt = Stream.of(clazz.getInterfaces())
                    .filter(CompanyCreator.class::equals)
                    .findFirst();
            if(!loaderClassOpt.isPresent())
                continue;
            final CompanyCreator creator = (CompanyCreator) clazz.newInstance();
            return creator.create();
        }
        return null;
    }
}
