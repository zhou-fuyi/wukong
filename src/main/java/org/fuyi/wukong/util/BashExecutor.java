package org.fuyi.wukong.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Bash 执行器
 *
 * @author: <a href="mailto:thread.zhou@gmail.com">Fuyi</a>
 * @time: 2021/8/11 16:21
 * @since: 1.0
 **/
public class BashExecutor {

    private static Logger logger = LoggerFactory.getLogger(BashExecutor.class.getName());

    /**
     * 仅执行Bash，不返回结果
     * @param bash
     */
    public static void execute(String bash){
        try {
            Process process = Runtime.getRuntime().exec(bash);
            ExecutorTemplate.executor.execute(new BashOutputStreamRunnable("Info", process.getInputStream()));
            ExecutorTemplate.executor.execute(new BashOutputStreamRunnable("Error", process.getErrorStream()));
            int status = process.waitFor();
            if (status != 0){
                throw new RuntimeException("Failed to call shell's command and the return status's is: " + status);
            }
            logger.info("The bash is executed");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行Bash并返回结果
     * @param bash
     * @return
     */
    public static String executeWithResult(String bash){
        BufferedReader bufferedReader = null;
        StringBuilder result = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(bash);
            ExecutorTemplate.executor.execute(new BashOutputStreamRunnable("Error", process.getErrorStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                logger.info(line);
                result.append(line);
            }
            int status = process.waitFor();
            if (status != 0){
                throw new RuntimeException("Failed to call shell's command and the return status's is: " + status);
            }
            logger.info("The bash is executed");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    private static class BashOutputStreamRunnable implements Runnable {

        private BufferedReader reader = null;
        private String type = null;

        public BashOutputStreamRunnable(String type, InputStream inputStream) {
            this.type = type;
            try {
                this.reader = new BufferedReader(new InputStreamReader(inputStream));
            }catch (Exception e){
                throw e;
            }
        }

        @Override
        public void run() {
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    logger.info(type + ": " + line);
                }
                reader.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
