package tungnn.tutor.java.aws.service.iam;

import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;

import java.util.List;

public final class PolicyUtil {

  private PolicyUtil() {}

  public static List<Policy> listAllPolicies(IamClient client) {
    return client
        .listPoliciesPaginator(ListPoliciesRequest.builder().scope(PolicyScopeType.ALL).build())
        .stream()
        .flatMap(r -> r.policies().stream())
        .toList();
  }

  public static Policy createPolicy(
      IamClient client, String policyName, String policyDocumentJson) {

    return client
        .createPolicy(
            CreatePolicyRequest.builder()
                .policyName(policyName)
                .policyDocument(policyDocumentJson)
                .build())
        .policy();
  }

  public static void deletePolicy(IamClient client, String policyArn) {
    client.deletePolicy(DeletePolicyRequest.builder().policyArn(policyArn).build());
  }
}
