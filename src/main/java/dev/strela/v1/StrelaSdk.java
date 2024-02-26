package dev.strela.v1;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

public class StrelaSdk {

  private final MinecraftDeployments minecraftDeployments;
  private final MinecraftStatefulSets minecraftStatefulSets;
  private final MinecraftServerSets minecraftServerSets;
  private final MinecraftServers minecraftServers;

  private StrelaSdk(KubernetesClient kubernetesClient, String defaultNamespace) {
    this.minecraftDeployments = new MinecraftDeployments(kubernetesClient, defaultNamespace);
    this.minecraftStatefulSets = new MinecraftStatefulSets(kubernetesClient, defaultNamespace);
    this.minecraftServerSets = new MinecraftServerSets(kubernetesClient, defaultNamespace);
    this.minecraftServers = new MinecraftServers(kubernetesClient, defaultNamespace);
  }

  public MinecraftDeployments minecraftDeployments() {
    return minecraftDeployments;
  }

  public MinecraftStatefulSets minecraftStatefulSets() {
    return minecraftStatefulSets;
  }

  public MinecraftServerSets minecraftServerSets() {
    return minecraftServerSets;
  }

  public MinecraftServers minecraftServers() {
    return minecraftServers;
  }

  public static StrelaSdk create(KubernetesClient kubernetesClient, String defaultNamespace) {
    return new StrelaSdk(kubernetesClient, defaultNamespace);
  }

  public static StrelaSdk create(String defaultNamespace) {
    return new StrelaSdk(new KubernetesClientBuilder().build(), defaultNamespace);
  }

  public static StrelaSdk create() {
    return new StrelaSdk(new KubernetesClientBuilder().build(), "strela-system");
  }

}
