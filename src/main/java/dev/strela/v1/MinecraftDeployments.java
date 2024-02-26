package dev.strela.v1;

import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A repository for Minecraft deployments
 */
public class MinecraftDeployments extends KubernetesCrudRepository<dev.strela.v1.MinecraftDeployment> {

  /**
   * Creates a new MinecraftDeployments repository
   *
   * @param kubernetesClient the Kubernetes client
   * @param defaultNamespace the default namespace
   */
  public MinecraftDeployments(KubernetesClient kubernetesClient, String defaultNamespace) {
    super(dev.strela.v1.MinecraftDeployment.class, kubernetesClient, defaultNamespace);
  }

  /**
   * Lists all Minecraft deployments with the given type
   *
   * @param defaultNamespace the default namespace
   * @param type             the type of the deployments
   * @return a future that will be completed when the deployments are listed. The future will contain the list of deployments
   */
  public CompletableFuture<List<dev.strela.v1.MinecraftDeployment>> list(String defaultNamespace, MinecraftDeploymentType type) {
    return CompletableFuture.supplyAsync(() -> kubernetesClient.resources(customResourceClass).inNamespace(defaultNamespace).list().getItems().stream().filter(minecraftDeployment -> minecraftDeployment.getSpec().getType().equals(type.toString())).toList());
  }

  /**
   * Lists all Minecraft deployments with the given type
   *
   * @param type the type of the deployments
   * @return a future that will be completed when the deployments are listed. The future will contain the list of deployments
   */
  public CompletableFuture<List<dev.strela.v1.MinecraftDeployment>> list(MinecraftDeploymentType type) {
    return list(defaultNamespace, type);
  }

  /**
   * Lists all Minecraft deployments with proxy as the type
   *
   * @param defaultNamespace the default namespace
   * @return a future that will be completed when the proxies are listed. The future will contain the list of proxies
   */
  public CompletableFuture<List<dev.strela.v1.MinecraftDeployment>> listProxies(String defaultNamespace) {
    return list(defaultNamespace, MinecraftDeploymentType.PROXY);
  }

  /**
   * Lists all Minecraft deployments with proxy as the type
   *
   * @return a future that will be completed when the proxies are listed. The future will contain the list of proxies
   */
  public CompletableFuture<List<dev.strela.v1.MinecraftDeployment>> listProxies() {
    return listProxies(defaultNamespace);
  }

  /**
   * Lists all Minecraft deployments with server as the type
   *
   * @param defaultNamespace the default namespace
   * @return a future that will be completed when the servers are listed. The future will contain the list of servers
   */
  public CompletableFuture<List<dev.strela.v1.MinecraftDeployment>> listServers(String defaultNamespace) {
    return list(defaultNamespace, MinecraftDeploymentType.SERVER);
  }

  /**
   * Lists all Minecraft deployments with server as the type
   *
   * @return a future that will be completed when the servers are listed. The future will contain the list of servers
   */
  public CompletableFuture<List<dev.strela.v1.MinecraftDeployment>> listServers() {
    return listServers(defaultNamespace);
  }

}
