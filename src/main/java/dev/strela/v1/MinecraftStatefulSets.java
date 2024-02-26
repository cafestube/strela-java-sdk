package dev.strela.v1;

import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * A repository for Minecraft stateful sets
 */
public class MinecraftStatefulSets extends KubernetesCrudRepository<dev.strela.v1.MinecraftStatefulSet> {

  /**
   * Creates a new MinecraftStatefulSets repository
   *
   * @param kubernetesClient the Kubernetes client
   * @param defaultNamespace the default namespace
   */
  public MinecraftStatefulSets(KubernetesClient kubernetesClient, String defaultNamespace) {
    super(dev.strela.v1.MinecraftStatefulSet.class, kubernetesClient, defaultNamespace);
  }

}
