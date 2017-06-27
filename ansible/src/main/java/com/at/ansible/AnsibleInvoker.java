package com.at.ansible;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnsibleInvoker {
    private static final Logger logger = LoggerFactory.getLogger(AnsibleInvoker.class);
    public static void main(String[] args) throws IOException, InterruptedException{
        if(args.length < 3){
            logger.error("java " + AnsibleInvoker.class.getName() + " [stdout filename] [stderr filename] ansible [ansible parameters...]");
            return;
        }
        if(logger.isInfoEnabled()){
            StringBuilder sb = new StringBuilder("args: ");
            for(String a : args){
                sb.append(a).append(", ");
            }
            String args_str = sb.toString().substring(0, sb.length() - 2);
            logger.info(args_str);
        }
        File stdout = new File(args[0]);
        stdout.createNewFile(); // create if not exist
        File stderr = new File(args[1]);
        stderr.createNewFile(); // create if not exist


        ProcessBuilder pb = new ProcessBuilder();
        pb.redirectOutput(stdout);
        pb.redirectError(stderr);
        List<String> cmds = new ArrayList<String>();
        for(int i = 2 ; i < args.length ; i++){ // kick out stdout and stderr, start from the 3rd index
            cmds.add(args[i]);
        }
        pb.command(cmds);

        Process p = pb.start();
        p.waitFor();
        if(p.isAlive()){
            p.destroyForcibly(); // shutdown the process forcibly
        }
        int exitValue = p.exitValue();
        if(exitValue == 0){
            logger.info("Exit value: '" + exitValue + "'");
        }else{
            logger.error("Exit value: '" + exitValue + "'");
            String stdoutstr = FileUtils.readFileToString(stdout);
            logger.error("stdout: '" + stdoutstr + "'");
            String stderrstr = FileUtils.readFileToString(stderr);
            logger.error("stderr: '" + stderrstr + "'");
        }
    }
}