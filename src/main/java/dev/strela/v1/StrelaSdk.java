package dev.strela.v1;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

import java.util.concurrent.CompletableFuture;

public class StrelaSdk {

  private final static String DEFAULT_NAMESPACE = "strela-system";
  private final static String POD_NAME_ENV = "POD_NAME";

  private final MinecraftDeployments minecraftDeployments;
  private final MinecraftStatefulSets minecraftStatefulSets;
  private final MinecraftServerSets minecraftServerSets;
  private final MinecraftServers minecraftServers;

  private final String currentPodName;

  /**
   * Creates a new Strela SDK
   *
   * @param kubernetesClient the Kubernetes client
   * @param defaultNamespace the default namespace
   */
  private StrelaSdk(KubernetesClient kubernetesClient, String defaultNamespace) {
    this.minecraftDeployments = new MinecraftDeployments(kubernetesClient, defaultNamespace);
    this.minecraftStatefulSets = new MinecraftStatefulSets(kubernetesClient, defaultNamespace);
    this.minecraftServerSets = new MinecraftServerSets(kubernetesClient, defaultNamespace);
    this.minecraftServers = new MinecraftServers(kubernetesClient, defaultNamespace);

    this.currentPodName = System.getenv(POD_NAME_ENV);
  }

  /**
   * @return the Minecraft deployments repository
   */
  public MinecraftDeployments minecraftDeployments() {
    return minecraftDeployments;
  }

  /**
   * @return the Minecraft stateful sets repository
   */
  public MinecraftStatefulSets minecraftStatefulSets() {
    return minecraftStatefulSets;
  }

  /**
   * @return the Minecraft server sets repository
   */
  public MinecraftServerSets minecraftServerSets() {
    return minecraftServerSets;
  }

  /**
   * @return the Minecraft servers repository
   */
  public MinecraftServers minecraftServers() {
    return minecraftServers;
  }


  /**
   * @return the name of the current Minecraft server. If the SDK is not running in a Kubernetes pod, an IllegalStateException is thrown.
   */
  public String getCurrentMinecraftServerName() {
    if (currentPodName == null) {
      throw new IllegalStateException("The SDK is not running in a Kubernetes pod. The current pod name is not available.");
    }

    return currentPodName;
  }

  /**
   * @return the current Minecraft server. If the SDK is not running in a Kubernetes pod, an IllegalStateException is thrown.
   */
  public CompletableFuture<dev.strela.v1.MinecraftServer> getCurrentMinecraftServer() {
    return minecraftServers.get(getCurrentMinecraftServerName());
  }

  public static StrelaSdk create(KubernetesClient kubernetesClient, String defaultNamespace) {
    return new StrelaSdk(kubernetesClient, defaultNamespace);
  }

  public static StrelaSdk create(String defaultNamespace) {
    return new StrelaSdk(new KubernetesClientBuilder().build(), defaultNamespace);
  }

  public static StrelaSdk create() {
    return new StrelaSdk(new KubernetesClientBuilder().build(), DEFAULT_NAMESPACE);
  }

}
