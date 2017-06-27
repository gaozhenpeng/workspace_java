package com.at.amazonaws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class AWSListInstanceMain {
    private static final Logger logger = LoggerFactory.getLogger(AWSListInstanceMain.class);
    
    public static void main(String[] args) throws Exception {
        AmazonEC2 ec2client = new AmazonEC2Client(
                new BasicAWSCredentials("<accesskey>", "<secretkey>"));
        ec2client.setRegion(Region.getRegion(Regions.CN_NORTH_1));
        ec2client.setEndpoint("https://ec2.cn-north-1.amazonaws.com.cn");
        
        try {
            DescribeAvailabilityZonesResult availabilityZonesResult = ec2client.describeAvailabilityZones();
            logger.info("You have access to " + availabilityZonesResult.getAvailabilityZones().size()
                    + " Availability Zones.");

            DescribeInstancesResult describeInstancesRequest = ec2client.describeInstances();
            List<Reservation> reservations = describeInstancesRequest.getReservations();
            Set<Instance> instances = new HashSet<Instance>();

            for (Reservation reservation : reservations) {
                instances.addAll(reservation.getInstances());
            }
            
            Integer instances_size = instances.size();
            logger.info("You have " + instances_size + " Amazon EC2 instance(s) running.");
            
            if(instances_size <= 0){
                return;
            }

            StringBuilder insContSB = new StringBuilder("\n");
            Integer ins_idx = 0;
            for(Instance ins : instances){
                ins_idx++;
                append(insContSB, ins, "InstanceId", 1);
                append(insContSB, ins, "ImageId", 2);
                
                append(insContSB, ins, "State", 2);
                append(insContSB, ins, "PrivateDnsName", 2);
                append(insContSB, ins, "PublicDnsName", 2);
                append(insContSB, ins, "StateTransitionReason", 2);
                append(insContSB, ins, "KeyName", 2);
                append(insContSB, ins, "AmiLaunchIndex", 2);
                append(insContSB, ins, "ProductCodes", 2);
                append(insContSB, ins, "InstanceType", 2);
                
                append(insContSB, ins, "LaunchTime", 2);
                append(insContSB, ins, "Placement", 2);
                append(insContSB, ins, "KernelId", 2);
                append(insContSB, ins, "RamdiskId", 2);
                append(insContSB, ins, "Platform", 2);
                append(insContSB, ins, "Monitoring", 2);
                append(insContSB, ins, "SubnetId", 2);
                append(insContSB, ins, "VpcId", 2);
                append(insContSB, ins, "PrivateIpAddress", 2);
                append(insContSB, ins, "PublicIpAddress", 2);
                append(insContSB, ins, "StateReason", 2);
                append(insContSB, ins, "Architecture", 2);
                append(insContSB, ins, "RootDeviceType", 2);
                append(insContSB, ins, "RootDeviceName", 2);
                append(insContSB, ins, "BlockDeviceMappings", 2);
                append(insContSB, ins, "VirtualizationType", 2);
                append(insContSB, ins, "InstanceLifecycle", 2);
                append(insContSB, ins, "SpotInstanceRequestId", 2);
                append(insContSB, ins, "ClientToken", 2);
                append(insContSB, ins, "Tags", 2);
                append(insContSB, ins, "SecurityGroups", 2);
                append(insContSB, ins, "SourceDestCheck", 2);
                append(insContSB, ins, "Hypervisor", 2);
                append(insContSB, ins, "NetworkInterfaces", 2);
                append(insContSB, ins, "IamInstanceProfile", 2);
                append(insContSB, ins, "EbsOptimized", 2);
                append(insContSB, ins, "SriovNetSupport", 2);
                append(insContSB, ins, "ProductCodes", 2);
                append(insContSB, ins, "ProductCodes", 2);
            }

            logger.info(insContSB.toString());
            
        } catch (AmazonServiceException ase) {
            StringBuilder exinfo = new StringBuilder();
            exinfo.append("Caught Exception: " + ase.getMessage());
            exinfo.append("Reponse Status Code: " + ase.getStatusCode());
            exinfo.append("Error Code: " + ase.getErrorCode());
            exinfo.append("Request ID: " + ase.getRequestId());
            String errmsg = exinfo.toString();
            logger.error(errmsg, ase);
            exinfo = null;
        }

    }
    public static void describeInstance(StringBuilder sb, Instance ins) throws Exception{
        if(sb == null || ins == null){
            return;
        }
        append(sb, ins, "InstanceId", 1);
        append(sb, ins, "ImageId", 2);
        
        append(sb, ins, "State", 2);
        append(sb, ins, "PrivateDnsName", 2);
        append(sb, ins, "PublicDnsName", 2);
        append(sb, ins, "StateTransitionReason", 2);
        append(sb, ins, "KeyName", 2);
        append(sb, ins, "AmiLaunchIndex", 2);
        append(sb, ins, "ProductCodes", 2);
        append(sb, ins, "InstanceType", 2);
        
        append(sb, ins, "LaunchTime", 2);
        append(sb, ins, "Placement", 2);
        append(sb, ins, "KernelId", 2);
        append(sb, ins, "RamdiskId", 2);
        append(sb, ins, "Platform", 2);
        append(sb, ins, "Monitoring", 2);
        append(sb, ins, "SubnetId", 2);
        append(sb, ins, "VpcId", 2);
        append(sb, ins, "PrivateIpAddress", 2);
        append(sb, ins, "PublicIpAddress", 2);
        append(sb, ins, "StateReason", 2);
        append(sb, ins, "Architecture", 2);
        append(sb, ins, "RootDeviceType", 2);
        append(sb, ins, "RootDeviceName", 2);
        append(sb, ins, "BlockDeviceMappings", 2);
        append(sb, ins, "VirtualizationType", 2);
        append(sb, ins, "InstanceLifecycle", 2);
        append(sb, ins, "SpotInstanceRequestId", 2);
        append(sb, ins, "ClientToken", 2);
        append(sb, ins, "Tags", 2);
        append(sb, ins, "SecurityGroups", 2);
        append(sb, ins, "SourceDestCheck", 2);
        append(sb, ins, "Hypervisor", 2);
        append(sb, ins, "NetworkInterfaces", 2);
        append(sb, ins, "IamInstanceProfile", 2);
        append(sb, ins, "EbsOptimized", 2);
        append(sb, ins, "SriovNetSupport", 2);
        append(sb, ins, "ProductCodes", 2);
        append(sb, ins, "ProductCodes", 2);
    }
    public static void append(StringBuilder sb, Object o, String label, Integer... assoc) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        if(sb == null || o == null || label == null || "".equals(label.trim())){
            return;
        }
        Integer indentLevel = 1;
        if(assoc.length > 0){
            indentLevel = assoc[0];
        }
        
        
        Method m = o.getClass().getDeclaredMethod("get" + label);
        
        for(int i = 0; i < indentLevel; i++){
            sb.append("  ");
        }
        sb.append(label);
        sb.append(": '");
        sb.append(m.invoke(o));
        sb.append("'");
        sb.append("\n");
    }
}