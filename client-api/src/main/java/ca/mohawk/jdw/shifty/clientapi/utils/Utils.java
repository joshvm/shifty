package ca.mohawk.jdw.shifty.clientapi.utils;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: client-api
  
  Developed By: Josh Maione (000320309)
*/

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Utils {

    public static Timestamp timestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static Properties props(final Path path) throws IOException{
        try(final InputStream in = Files.newInputStream(path)){
            final Properties props = new Properties();
            props.load(in);
            return props;
        }
    }

    public static <K, V> Map<K, V> map(final V[] values, final Function<V, K> key){
        return Stream.of(values)
                .collect(Collectors.toMap(key, Function.identity()));
    }

    public static String device(){
        return String.format("%s@%s", System.getProperty("user.name"), System.getProperty("os.name"));
    }

    public static boolean between(final int value,
                                  final int minValue,
                                  final int maxValue){
        return value >= minValue && value <= maxValue;
    }

    public static boolean close(final Closeable closeable, final boolean printStackTrace){
        try{
            closeable.close();
            return true;
        }catch(Exception ex){
            if(printStackTrace)
                ex.printStackTrace();
            return false;
        }
    }
}
