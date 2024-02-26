package dev.strela.v1;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class StrelaSdk {

  private final KubernetesClient kubernetesClient;

  private StrelaSdk(KubernetesClient kubernetesClient) {
    this.kubernetesClient = kubernetesClient;
  }

  public static StrelaSdk create(KubernetesClient kubernetesClient) {
    return new StrelaSdk(kubernetesClient);
  }

  public static StrelaSdk create() {
    return new StrelaSdk(new KubernetesClientBuilder().build());
  }

}
