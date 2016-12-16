package ca.mohawk.jdw.shifty.server.utils;

/*
  Capstone Project - Chatter
  
  An instant messenger that lets you connect with your friends 
  and meet new people with similar interests.
  
  I, Josh Maione, 000320309 certify that this material is my original work. 
  No other person's work has been used without due acknowledgement. 
  I have not made my work available to anyone else.

  Module: server
  
  Developed By: Josh Maione (000320309)
*/

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Utils {

    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Utils(){}

    public static Properties props(final Path path) throws IOException {
        try(final InputStream in = Files.newInputStream(path)){
            final Properties props = new Properties();
            props.load(in);
            return props;
        }
    }

    public static <K, V> Map<K, V> map(final V[] values, Function<V, K> key){
        return Stream.of(values)
                .collect(Collectors.toMap(key, Function.identity()));
    }

    public static Timestamp timestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp timestampPlus(final long value,
                                          final TimeUnit unit){
        return new Timestamp(System.currentTimeMillis() + unit.toMillis(value));
    }

    public static Timestamp timestamp(final String timestamp){
        try{
            return new Timestamp(TIMESTAMP_FORMAT.parse(timestamp).getTime());
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static String timestampSqlString(final Timestamp timestamp){
        return TIMESTAMP_FORMAT.format(timestamp);
    }

    public static boolean between(final int value, final int min, final int max){
        return value >= min && value <= max;
    }

    public static boolean between(final Timestamp value,
                                  final Timestamp min,
                                  final Timestamp max){
        return value.after(min) && value.before(max);
    }

    public static boolean patternsContain(final List<String> listOfPatterns, final String value){
        return listOfPatterns.stream()
                .anyMatch(pattern -> pattern.matches(value));
    }

    public static int other(final int checkValue, final int value1, final int value2){
        return checkValue == value1 ? value2 : value1;
    }

}
