package com.test;



import java.util.List;

import org.junit.Test;

import com.github.hermannpencole.nifi.swagger.ApiClient;
import com.github.hermannpencole.nifi.swagger.ApiException;
import com.github.hermannpencole.nifi.swagger.client.FlowApi;
import com.github.hermannpencole.nifi.swagger.client.model.DocumentedTypeDTO;
import com.github.hermannpencole.nifi.swagger.client.model.ProcessorTypesEntity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.glassfish.jersey.client.ClientConfig;

public class MyJerseyTest {
    
    String url = "http://35.231.236.230:8080/nifi-api/flow/processor-types";
    
    @Test
    public void test2(){
        ApiClient apiClient = new ApiClient().setBasePath("http://35.231.236.230:8080/nifi-api");
        FlowApi apiInstance = new FlowApi();
        apiInstance.setApiClient(apiClient);
String bundleGroupFilter = ""; // String | If specified, will only return types that are a member of this bundle group.
String bundleArtifactFilter = ""; // String | If specified, will only return types that are a member of this bundle artifact.
String type = ""; // String | If specified, will only return types whose fully qualified classname matches.
try {
    //apiInstance.getprocessorT
    ProcessorTypesEntity result = apiInstance.getProcessorTypes(null, null, null);
    List<DocumentedTypeDTO> list = result.getProcessorTypes();
    for(DocumentedTypeDTO d:list){
        System.out.println(d.getType());
        
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
