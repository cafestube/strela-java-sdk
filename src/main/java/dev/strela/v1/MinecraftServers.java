package dev.strela.v1;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Filterable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

  /**
   * Gets the pod for a Minecraft server in the default namespace
   *
   * @param server the server
   * @return a future that will be completed when the pod is found. The future will contain the pod
   */
  public CompletableFuture<Pod> getPodByMinecraftServer(dev.strela.v1.MinecraftServer server) {
    return getPodByMinecraftServer(defaultNamespace, server);
  }

  /**
   * Gets the pod for a Minecraft server
   *
   * @param namespace the namespace
   * @param server    the server
   * @return a future that will be completed when the pod is found. The future will contain the pod
   */
  public CompletableFuture<Pod> getPodByMinecraftServer(String namespace, dev.strela.v1.MinecraftServer server) {
    return CompletableFuture.supplyAsync(() -> kubernetesClient.pods()
      .inNamespace(namespace)
      .withName(server.getMetadata().getName())
      .get());

  }

}
