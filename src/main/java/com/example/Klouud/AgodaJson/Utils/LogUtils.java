package com.example.Klouud.AgodaJson.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Data
@Component
public class LogUtils {

    public static final String BEGINNING = "*****************************************************************************BEGINNING*******************************************************************************";
    public static final String ENDING = "*****************************************************************************ENDING********************************************************************";
    public static final String PACKAGENAME = "Agoda-Revised";


    public static void printDebugLogStatement(String API,StackTraceElement stackTraceElement,String statement,Object data){

        String lineSeparator = System.getProperty("line.separator");
        StringBuilder logBuilder = new StringBuilder();

        logBuilder.append(BEGINNING)
                .append(lineSeparator)
                .append("Time-Stamp")
                .append(LocalDateTime.now())
                .append(" | ")
                .append("API :")
                .append(API)
                .append(" | ")
                .append(stackTraceElement.getClassName())
                .append(" | ")
                .append("Method :")
                .append(stackTraceElement.getMethodName())
                .append("Line number :")
                .append(stackTraceElement.getLineNumber())
                .append(" | ")
                .append("Statement : ")
                .append(statement == null || statement.isEmpty() ? "NA" : statement)
                .append(" | ")
                .append("DATA :")
                .append(null == data ? "NA" : data)
                .append(lineSeparator)
                .append(ENDING);
        log.info(logBuilder.toString());
    }

    public static void printErrorLogStatment(String API, Exception e) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder logStatmentBuilder = new StringBuilder();
        logStatmentBuilder
                .append(BEGINNING)
                .append(lineSeparator)
                .append("TIMESTAMP : ")
                .append(LocalDateTime.now())
                .append(" | ")
                .append("API : ")
                .append(API)
                .append(lineSeparator)
                .append("EXCEPTION STACK TRACE : ");
        log.info(logStatmentBuilder.toString(), e);
        log.info(ENDING);
    }

}
