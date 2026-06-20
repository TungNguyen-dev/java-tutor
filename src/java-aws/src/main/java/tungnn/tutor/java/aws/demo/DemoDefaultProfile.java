package tungnn.tutor.java.aws.demo;

import software.amazon.awssdk.services.iam.IamClient;
import tungnn.tutor.java.aws.AwsClientsFactory;
import tungnn.tutor.java.aws.service.iam.IamUserUtil;

public class DemoDefaultProfile {

  static void main() {
    var clientsFactory = new AwsClientsFactory();

    var iamClient = clientsFactory.buildClient(IamClient.builder());
    try (iamClient) {
      IamUserUtil.listAllUsers(iamClient).forEach(System.out::println);
    }
  }
}
