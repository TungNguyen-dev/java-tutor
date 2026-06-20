package tungnn.tutor.java.aws.demo;

import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.ssm.SsmClient;
import tungnn.tutor.java.aws.AwsClientsFactory;
import tungnn.tutor.java.aws.AwsClientsRegistry;
import tungnn.tutor.java.aws.AwsProfileRegistry;
import tungnn.tutor.java.aws.service.iam.IamUserUtil;

public class DemoMultiProfile {

  static void main() {
    var clientsFactory = new AwsClientsFactory();
    var registry = new AwsClientsRegistry(clientsFactory);

    // register builders
    registry.registerClient(IamClient.class, IamClient::builder);
    registry.registerClient(SsmClient.class, SsmClient::builder);

    // dùng
    var profileRegistry = new AwsProfileRegistry();
    var iamClient = registry.getClient(IamClient.class, profileRegistry.profile("default"));
    try (iamClient) {
      IamUserUtil.listAllUsers(iamClient).forEach(System.out::println);
    }
  }
}
