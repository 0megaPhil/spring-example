package com.arryved.example.data;

import com.arryved.core.AbstractSpanner;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.DatabaseInfo;
import com.google.cloud.spanner.InstanceAdminClient;
import com.google.cloud.spanner.InstanceConfigId;
import com.google.cloud.spanner.InstanceId;
import com.google.cloud.spanner.Instance;
import com.google.cloud.spanner.InstanceInfo;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spring.data.spanner.core.SpannerTemplate;
import com.google.spanner.admin.instance.v1.CreateInstanceMetadata;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Component;

//@Component
// TODO(phil): Investigate local spanner
public class EchoSpanner extends AbstractSpanner<Echo> {

  public EchoSpanner(SpannerTemplate spannerTemplate) {
    super(spannerTemplate, Echo.class);
    createInstance();
  }

  private static void createInstance() {
    createInstance("spring-example", "example-spanner");
  }

  private static void createInstance(String projectId, String instanceId) {
    Spanner spanner = SpannerOptions.newBuilder()
        .setEmulatorHost("http://localhost:9020")
        .setProjectId(projectId)
        .build().getService();
    InstanceAdminClient instanceAdminClient = spanner.getInstanceAdminClient();
//    DatabaseAdminClient databaseAdminClient = spanner.getDatabaseAdminClient();

    // Set Instance configuration.
    String configId = "local-spanner";
    int nodeCount = 2;
    String displayName = "Emulated Spanner";

    // Create an InstanceInfo object that will be used to create the instance.
    InstanceInfo instanceInfo =
        InstanceInfo.newBuilder(InstanceId.of(projectId, instanceId))
            .setInstanceConfigId(InstanceConfigId.of(projectId, configId))
            .setNodeCount(nodeCount)
            .setDisplayName(displayName)
            .build();
    OperationFuture<Instance, CreateInstanceMetadata> operation =
        instanceAdminClient.createInstance(instanceInfo);

//    DatabaseInfo databaseInfo = databaseAdminClient.createDatabase();
    try {
      // Wait for the createInstance operation to finish.
      Instance instance = operation.get();
      // TODO(phil): Switch out for real logging
      System.out.printf("Instance %s was successfully created%n", instance.getId());
    } catch (ExecutionException e) {
      System.out.printf(
          "Error: Creating instance %s failed with error message %s%n",
          instanceInfo.getId(), e.getMessage());
    } catch (InterruptedException e) {
      System.out.println("Error: Waiting for createInstance operation to finish was interrupted");
    } finally {
      spanner.close();
    }
  }

}