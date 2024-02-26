package dev.strela.v1;

import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * A repository for Minecraft servers
 */
public class MinecraftServers extends KubernetesCrudRepository<dev.strela.v1.MinecraftServer> {

  /**
   * Creates a new MinecraftServers repository
   *
   * @param kubernetesClient the Kubernetes client
   * @param defaultNamespace the default namespace
   */
  public MinecraftServers(KubernetesClient kubernetesClient, String defaultNamespace) {
    super(dev.strela.v1.MinecraftServer.class, kubernetesClient, defaultNamespace);
  }

}
