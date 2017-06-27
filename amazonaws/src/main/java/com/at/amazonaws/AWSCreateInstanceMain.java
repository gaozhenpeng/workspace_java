package com.at.amazonaws;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.BlockDeviceMapping;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeSubnetsRequest;
import com.amazonaws.services.ec2.model.DescribeSubnetsResult;
import com.amazonaws.services.ec2.model.DescribeVpcsRequest;
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.EbsBlockDevice;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceNetworkInterfaceSpecification;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Subnet;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.VolumeType;

public class AWSCreateInstanceMain {
    private static final Logger logger = LoggerFactory.getLogger(AWSCreateInstanceMain.class);
    
    public static String querySubnetIDbyVPCId(AmazonEC2 ec2client, String vpcId){
        if(ec2client == null || vpcId == null || "".equals(vpcId.trim())){
            return null;
        }
        
        DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
        describeVpcsRequest.withVpcIds(vpcId);
        DescribeVpcsResult describeVpcsResult = ec2client.describeVpcs(describeVpcsRequest);
        logger.info("vpcs: '" + describeVpcsResult.toString() + "'");
        
        Filter subnetFilter_vpcId = new Filter();
        subnetFilter_vpcId.withName("vpc-id").withValues(vpcId);
        Filter subnetFilter_defaultAZ = new Filter();
        subnetFilter_defaultAZ.withName("default-for-az").withValues("true");
        Filter subnetFilter_state = new Filter();
        subnetFilter_state.withName("state").withValues("available");
        
        DescribeSubnetsRequest describeSubnetsRequest = new DescribeSubnetsRequest();
        describeSubnetsRequest.withFilters(subnetFilter_vpcId, subnetFilter_defaultAZ, subnetFilter_state);
        DescribeSubnetsResult describeSubnetsResult  = ec2client.describeSubnets(describeSubnetsRequest);
        logger.info("Filtered subnets: '" + describeSubnetsResult.toString() + "'");
        logger.info("describeSubnets: '" + ec2client.describeSubnets().toString() + "'");
        
        List<Subnet> subnets = describeSubnetsResult.getSubnets();
        assert subnets.size() == 1 : "Only 1 default available subnet is expected." ;
        
        return subnets.get(0).getSubnetId();
    }
    
    public static void setUserData(RunInstancesRequest runInstancesRequest) throws UnsupportedEncodingException{
        if(runInstancesRequest == null){
            return ;
        }
        
        StringBuilder userData = new StringBuilder();
        userData.append("#! /bin/bash").append("\n");
        userData.append("echo 'Hello World!' > /tmp/startup.log");
        
        String userDataB64 = Base64.getEncoder().encodeToString(userData.toString().getBytes("UTF-8"));
        userData = null;
        
        runInstancesRequest.setUserData(userDataB64);
    }
    
    public static void setNetworkInterfaces(RunInstancesRequest runInstancesRequest){
        if(runInstancesRequest == null){
            return;
        }
        
        InstanceNetworkInterfaceSpecification insNWIntSpec = new InstanceNetworkInterfaceSpecification();
        insNWIntSpec.withDeviceIndex(0) // Each network interface requires a device index
                .withNetworkInterfaceId("eni-ee25708a") // ethernet interface id, eni-<ID>
                .withSubnetId("subnet-22f22447") // subnet id, if not set, the default one will be applied
                ;
        
        List<InstanceNetworkInterfaceSpecification> insNWIntSpecs = new ArrayList<InstanceNetworkInterfaceSpecification>();
        insNWIntSpecs.add(insNWIntSpec);
        
        runInstancesRequest.setNetworkInterfaces(insNWIntSpecs);
//      runInstancesRequest.withNetworkInterfaces(insNWIntSpec) // collection: override former definitions ; <single value>: append to the list
//      runInstancesRequest.withNetworkInterfaces(insNWIntSpecs) // collection: override former definitions ; <single value>: append to the list
    }
    private static void setBlockDeviceMappings(RunInstancesRequest runInstancesRequest){
        if(runInstancesRequest == null){
            return;
        }
        
        BlockDeviceMapping blockDeviceMappingRoot = new BlockDeviceMapping();
        blockDeviceMappingRoot
                .withDeviceName("/dev/sda1") // /dev/sda1 for root disk
                .withEbs(
                        (new EbsBlockDevice())
                                .withDeleteOnTermination(true)
                                .withVolumeSize(8) // GiB
                                .withVolumeType("gp2") //  gp2 for General Purpose (SSD) volumes, io1 for Provisioned IOPS (SSD) volumes, and standard for Magnetic volumes.
                )
                ;
        
        BlockDeviceMapping blockDeviceMappingData = new BlockDeviceMapping();
        blockDeviceMappingData
                .withDeviceName("/dev/sdb") // data disk
                .withEbs(
                        (new EbsBlockDevice())
                                .withDeleteOnTermination(true)
                                .withVolumeSize(10) // GiB
                                .withVolumeType("gp2") //  gp2 for General Purpose (SSD) volumes, io1 for Provisioned IOPS (SSD) volumes, and standard for Magnetic volumes.
                )
                ;
        
        
        List<BlockDeviceMapping> blockDeviceMappings = new ArrayList<BlockDeviceMapping>();
        blockDeviceMappings.add(blockDeviceMappingRoot);
        blockDeviceMappings.add(blockDeviceMappingData);
        
        runInstancesRequest.setBlockDeviceMappings(blockDeviceMappings);
    }
    private static void setPlacement(RunInstancesRequest runInstancesRequest){
        if(runInstancesRequest == null){
            return;
        }
        
        Placement placement = new Placement();
        placement.setAvailabilityZone("cn-north-1a"); // subnet implies AvailabilityZone
        runInstancesRequest.setPlacement(placement);
        
    }
    
    public static void main(String[] args) throws Exception {
        AmazonEC2 ec2client = new AmazonEC2Client(
                new BasicAWSCredentials("<accesskey>", "<secretkey>"));
        ec2client.setRegion(Region.getRegion(Regions.CN_NORTH_1));
        ec2client.setEndpoint("https://ec2.cn-north-1.amazonaws.com.cn");
        
        String subnetId = querySubnetIDbyVPCId(ec2client, "vpc-6fdf050a");
        logger.info("default subnetId for vpc-6fdf050a: " + subnetId);
        
        DescribeAvailabilityZonesResult describeAvailabilityZonesResult = ec2client.describeAvailabilityZones();
        logger.info("AvailabilityZones: '" + describeAvailabilityZonesResult.getAvailabilityZones().toString() + "'");
        
        // create instance (launch instance)
        RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
        
        setUserData(runInstancesRequest);
        setBlockDeviceMappings(runInstancesRequest);
        setPlacement(runInstancesRequest);
        
        runInstancesRequest
                .withImageId("ami-0220b23b")
                .withInstanceType("t2.micro")
                .withMinCount(1)
                .withMaxCount(1)
                .withKeyName("test-at-20151211") // ssh key name
                .withSubnetId("subnet-22f22447") // subnet id
                .withSecurityGroupIds("sg-8de040e8")  // security group id; Network interfaces and an instance-level security groups may not be specified on the same request
//              .withSecurityGroups("test-sg-at-20151209") // security group name;
                                                            // if not default vpc, use security group id instead of security group name ;
                                                            // Network interfaces and an instance-level security groups may not be specified on the same request
                ;
        RunInstancesResult runInstancesResult = ec2client.runInstances(runInstancesRequest);
        
        
        // 
        String instanceId = null;
        if(runInstancesResult != null && runInstancesResult.getReservation() != null){
            StringBuilder insDesc = new StringBuilder("\n");
            Reservation reservation = runInstancesResult.getReservation();
            for(Instance ins : reservation.getInstances()){
                describeInstance(insDesc, ins);
                instanceId = ins.getInstanceId();
            }
            logger.info(insDesc.toString());
            insDesc = null;
        }
        
        // update instance instance name by tag ("Name" : "Instance Name"), 
        CreateTagsRequest createTagsRequest = new CreateTagsRequest();
        createTagsRequest.withResources(instanceId)
                .withTags(new Tag("Name", "test-vm-at-"+(new Date().getTime())));
        ec2client.createTags(createTagsRequest);
        
        logger.info("Done.");
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