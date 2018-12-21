package com.test;


import java.util.List;
import java.util.UUID;

import com.github.hermannpencole.nifi.swagger.client.ProcessorsApi;
import com.github.hermannpencole.nifi.swagger.client.model.*;
import org.junit.Test;

import com.github.hermannpencole.nifi.swagger.ApiClient;
import com.github.hermannpencole.nifi.swagger.ApiException;
import com.github.hermannpencole.nifi.swagger.client.FlowApi;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.glassfish.jersey.client.ClientConfig;

public class MyJerseyTest {

    String baseURL = "http://35.231.236.230:8080/nifi-api";
    String url = "http://35.231.236.230:8080/nifi-api/flow/processor-types";
    final String STATUS_RUNNING = "Running";
    final String STATUS_STOPPED = "Stopped";

    private void prt(Object obj){
        System.out.println(obj);
    }
    @Test
    public void test2() {
        ApiClient apiClient = new ApiClient().setBasePath("http://35.231.236.230:8080/nifi-api");
        FlowApi flowApi = new FlowApi();
        flowApi.setApiClient(apiClient);

        ProcessorsApi procApi = new ProcessorsApi(apiClient);

        String bundleGroupFilter = ""; // String | If specified, will only return types that are a member of this bundle group.
        String bundleArtifactFilter = ""; // String | If specified, will only return types that are a member of this bundle artifact.
        String type = ""; // String | If specified, will only return types whose fully qualified classname matches.
        try {
            //flowApi.getprocessorT
            ProcessorTypesEntity result = flowApi.getProcessorTypes(null, null, null);
            List<DocumentedTypeDTO> list = result.getProcessorTypes();
            prt("Processor types:");
            for (DocumentedTypeDTO d : list) {
                System.out.println(d.getType());

            }



            ProcessGroupStatusEntity rootGrpStatus = flowApi.getProcessGroupStatus("root", true, true, null);
            ProcessGroupStatusDTO processGroupStatus = rootGrpStatus.getProcessGroupStatus();
            prt("group name:" + processGroupStatus.getName());
            prt("group id:" + processGroupStatus.getId());

            String rootGrpId = processGroupStatus.getId();

            ProcessGroupFlowEntity nifi_flow = flowApi.getFlow("root");

            List<ProcessorEntity> processors = nifi_flow.getProcessGroupFlow().getFlow().getProcessors();

            for(ProcessorEntity p:processors){
                ProcessorStatusDTO processorStatusDTO = p.getStatus();
                prt(p.getComponent().getName() + " " + processorStatusDTO.getRunStatus());
                ProcessorDTO processorDTO = p.getComponent();
                ProcessorDTO.StateEnum processorState = processorDTO.getState();

                if(processorState == ProcessorDTO.StateEnum.RUNNING){
                    processorDTO.setState(ProcessorDTO.StateEnum.STOPPED);
                }if(processorState == ProcessorDTO.StateEnum.STOPPED){
                    processorDTO.setState(ProcessorDTO.StateEnum.RUNNING);
                }
                p.setComponent(processorDTO);


                if(processorStatusDTO.getRunStatus().equalsIgnoreCase(STATUS_RUNNING)){
                    processorStatusDTO.setRunStatus(STATUS_STOPPED);
                }else if(processorStatusDTO.getRunStatus().equalsIgnoreCase(STATUS_STOPPED)){
                    processorStatusDTO.setRunStatus(STATUS_RUNNING);
                }else{
                    prt("processor status NOT valid");
                }

                p.setStatus(processorStatusDTO);
                RevisionDTO revisionDTO = p.getRevision();
                revisionDTO.setClientId(UUID.randomUUID().toString());
                p.setRevision(revisionDTO);
                prt(p.toString());
                procApi.updateProcessor(p.getId(),p);
            }




            //System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling FlowApi#getProcessorTypes");
            e.printStackTrace();
        }
    }

    //@Test
    public void test1() {

/*
        ApiClient c = new ApiClient();


        FlowApi apiInstance = new FlowApi();
        try {
            ProcessorTypesEntity result = apiInstance.getProcessorTypes();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling FlowApi#getProcessorTypes");
            e.printStackTrace();
        }
        */

        Client client = ClientBuilder.newClient();
        client.register(ProcessorTypesEntity.class);

        WebTarget target = client.target(url);


        ProcessorTypesEntity entity = target.request(MediaType.APPLICATION_JSON_TYPE).get(ProcessorTypesEntity.class);

        System.out.println(entity);

    }


}
