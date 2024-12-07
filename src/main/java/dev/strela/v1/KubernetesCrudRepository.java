package dev.strela.v1;

import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonDeletingOperation;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A repository for Kubernetes custom resources
 *
 * @param <R> the type of the custom resource
 */
public abstract class KubernetesCrudRepository<R extends CustomResource> {

  protected final Class<R> customResourceClass;
  protected final KubernetesClient kubernetesClient;
  protected final String defaultNamespace;

  /**
   * Creates a new KubernetesCrudRepository
   *
   * @param customResourceClass the class of the custom resource
   * @param kubernetesClient    the Kubernetes client
   * @param defaultNamespace    the default namespace
   */
  protected KubernetesCrudRepository(Class<R> customResourceClass, KubernetesClient kubernetesClient, String defaultNamespace) {
    this.customResourceClass = customResourceClass;
    this.kubernetesClient = kubernetesClient;
    this.defaultNamespace = defaultNamespace;
  }

  /**
   * Creates a new resource
   *
   * @param resource the resource to create
   * @return a future that will be completed when the resource is created. The future will contain the created resource
   */
  public CompletableFuture<R> create(R resource) {
    return create(defaultNamespace, resource);
  }

  /**
   * Creates a new resource
   *
   * @param namespace the namespace to create the resource in
   * @param resource  the resource to create
   * @return a future that will be completed when the resource is created. The future will contain the created resource
   */
  public CompletableFuture<R> create(String namespace, R resource) {
    return CompletableFuture.supplyAsync(() -> {
      kubernetesClient.resource(resource)
        .inNamespace(namespace)
        .create();
      return resource;
    });
  }

  /**
   * Updates a resource
   *
   * @param resource the resource to update
   * @return a future that will be completed when the resource is updated. The future will contain the updated resource
   */
  public CompletableFuture<R> update(R resource) {
    return update(defaultNamespace, resource);
  }

  /**
   * Updates a resource
   *
   * @param namespace the namespace to update the resource in
   * @param resource  the resource to update
   * @return a future that will be completed when the resource is updated. The future will contain the updated resource
   */
  public CompletableFuture<R> update(String namespace, R resource) {
    return CompletableFuture.supplyAsync(() -> {
      kubernetesClient.resource(resource)
        .inNamespace(namespace)
        .unlock()
        .createOr(NonDeletingOperation::update);
      return resource;
    });
  }

  /**
   * Updates a resource
   *
   * @param name    the name of the resource to update
   * @param updater a function that will be called with the resource to update
   * @return a future that will be completed when the resource is updated. The future will contain the updated resource
   */
  public CompletableFuture<R> update(String name, Consumer<R> updater) {
    return update(defaultNamespace, name, updater);
  }

  /**
   * Updates a resource
   *
   * @param namespace the namespace to update the resource in
   * @param name      the name of the resource to update
   * @param updater   a function that will be called with the resource to update
   * @return a future that will be completed when the resource is updated. The future will contain the updated resource
   */
  public CompletableFuture<R> update(String namespace, String name, Consumer<R> updater) {
    return get(namespace, name).thenApplyAsync(resource -> {
      updater.accept(resource);
      return update(namespace, resource).join();
    });
  }

  /**
   * Gets a resource
   *
   * @param name the name of the resource to get
   * @return a future that will be completed when the resource is retrieved. The future will contain the resource
   */
  public CompletableFuture<R> get(String name) {
    return get(defaultNamespace, name);
  }

  /**
   * Gets a resource
   *
   * @param namespace the namespace to get the resource from
   * @param name      the name of the resource to get
   * @return a future that will be completed when the resource is retrieved. The future will contain the resource
   */
  public CompletableFuture<R> get(String namespace, String name) {
    return CompletableFuture.supplyAsync(() -> kubernetesClient.resources(customResourceClass)
      .inNamespace(namespace)
      .withName(name)
      .get());
  }

  /**
   * Lists all resources
   *
   * @return a future that will be completed when the resources are listed. The future will contain a list of resources
   */
  public CompletableFuture<List<R>> list() {
    return list(defaultNamespace);
  }

  /**
   * Lists all resources
   *
   * @param namespace the namespace to list the resources from
   * @return a future that will be completed when the resources are listed. The future will contain a list of resources
   */
  public CompletableFuture<List<R>> list(String namespace) {
    return CompletableFuture.supplyAsync(() -> kubernetesClient.resources(customResourceClass)
      .inNamespace(namespace)
      .list()
      .getItems());
  }

  /**
   * Deletes a resource
   *
   * @param name the name of the resource to delete
   * @return a future that will be completed when the resource is deleted
   */
  public CompletableFuture<Void> delete(String name) {
    return delete(defaultNamespace, name);
  }

  /**
   * Deletes a resource
   *
   * @param namespace the namespace to delete the resource from
   * @param name      the name of the resource to delete
   * @return a future that will be completed when the resource is deleted
   */
  public CompletableFuture<Void> delete(String namespace, String name) {
    return CompletableFuture.runAsync(() -> kubernetesClient.resources(customResourceClass)
      .inNamespace(namespace)
      .withName(name)
      .delete());
  }

  /**
   * Creates an informer builder for a resource in the default namespace
   *
   * @return a builder for the informer
   */
  public CrudResourceInformerBuilder createInformer() {
    return createInformer(defaultNamespace);
  }

  /**
   * Creates an informer builder for a resource
   *
   * @param namespace the namespace to inform the resource in
   * @return a builder for the informer
   */
  public CrudResourceInformerBuilder createInformer(String namespace) {
    return new CrudResourceInformerBuilder(namespace);
  }

  /**
   * A builder for a resource informer
   */
  public class CrudResourceInformerBuilder {

    private final String namespace;

    private Consumer<R> addHandler = ConsumerUtil.empty();
    private BiConsumer<R, R> updateHandler = ConsumerUtil.emptyBi();
    private Consumer<R> deleteHandler = ConsumerUtil.empty();


    public CrudResourceInformerBuilder(String namespace) {
      this.namespace = namespace;
    }

    /**
     * Sets the handler for when a resource is added
     *
     * @param onAdd the handler for when a resource is added
     * @return this builder
     */
    public CrudResourceInformerBuilder withAddHandler(Consumer<R> onAdd) {
      this.addHandler = onAdd;
      return this;
    }

    /**
     * Sets the handler for when a resource is updated
     *
     * @param onUpdate the handler for when a resource is updated
     * @return this builder
     */
    public CrudResourceInformerBuilder withUpdateHandler(BiConsumer<R, R> onUpdate) {
      this.updateHandler = onUpdate;
      return this;
    }

    /**
     * Sets the handler for when a resource is deleted
     *
     * @param onDelete the handler for when a resource is deleted
     * @return this builder
     */
    public CrudResourceInformerBuilder withDeleteHandler(Consumer<R> onDelete) {
      this.deleteHandler = onDelete;
      return this;
    }

    public void inform() {
      CompletableFuture.runAsync(() -> {
        kubernetesClient.resources(customResourceClass)
          .inNamespace(namespace)
          .inform(new ResourceEventHandler<R>() {
            @Override
            public void onAdd(R obj) {
              addHandler.accept(obj);
            }

            @Override
            public void onUpdate(R oldObj, R newObj) {
              updateHandler.accept(oldObj, newObj);
            }

            @Override
            public void onDelete(R obj, boolean deletedFinalStateUnknown) {
              deleteHandler.accept(obj);
            }
          });
      });
    }
  }

}
