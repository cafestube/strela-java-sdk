package dev.strela.v1;

import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * A repository for Minecraft server sets
 */
public class MinecraftServerSets extends KubernetesCrudRepository<dev.strela.v1.MinecraftServerSet> {

  /**
   * Creates a new MinecraftServerSets
   *
   * @param kubernetesClient the Kubernetes client
   * @param defaultNamespace the default namespace
   */
  public MinecraftServerSets(KubernetesClient kubernetesClient, String defaultNamespace) {
    super(dev.strela.v1.MinecraftServerSet.class, kubernetesClient, defaultNamespace);
  }

}
