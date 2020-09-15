# BookStore - a microservice testbed

### Services' overview

* Catalogue Management
* User Management
* Employee Management
* Cart Management
* Order Management

To get the **OpenAPI 3.0** specification run the following command, where _name_ is the pod's name of the desired service:

`kubectl exec _name_ -- curl localhost:7777/v3/api-docs > api.txt`.

To see it in the ui run `kubectl exec _name_ -- curl localhost:7777/swagger-ui.html`.

> The pod's name can be retrieved with `kubectl get pods`.

## Requirements

- A Google Cloud account
- The [gcloud-command tool](https://cloud.google.com/sdk/docs/quickstart)

## Installation

> The application can be run either in a local Kubernetes cluster, or in a cluster in the cloud. The recommended choice is using the [GKE cloud](https://cloud.google.com/kubernetes-engine). Therefore, the whole setup is under the assumption that the cloud option has been chosen.

0. First, run `gcloud init` in the command line.
1. Create a project `gcloud projects create bookStore`.
2. Set the configured project as your default `gcloud config set project bookStore`.
3. Install the kubectl-command tool for your project `gcloud components install kubectl`.
4. Create a cluster with Istio enabled: 

  ```
  gcloud beta container clusters create bookStore-cluster \
    --addons=Istio --istio-config=auth=MTLS_PERMISSIVE \
    --cluster-version=1.4.10-gke.5 \
    --machine-type=n1-standard-2 \
    --num-nodes=4
  ```
5. Verify that the cluster is running `gcloud container clusters list` and services istio-citadel, istio-egressgateway, istio-pilot, istio-ingressgateway, istio-policy, istio-sidecar-injector, and istio-telemetry are deployed `kubectl get service -n istio-system`.
6. Grant cluster admin permissions to the current user:

`kubectl create clusterrolebinding cluster-admin-binding \
    --clusterrole=cluster-admin \
    --user=$(gcloud config get-value core/account)`
    
7. Enable the sidecar injection for the cluster `kubectl label namespace default istio-injection=enabled`.
8. Deploy the desired application configuration. For instance, to deploy version 1:

```
kubectl apply -f deployment-v1.yaml
kubectl apply -f rabbitmq.yaml
```

>Keep in mind, that if you want to have both versions of one service running, you need to deploy not only its deployments and services, but also the appropriate destination rule.

9. Ensure all the services are running `kubectl get pods` and deploy the gateway `kubectl apply -f gateway.yaml`.
10. To find out, on what ip the cluster is running, perform following commands: 

  - **ip**: `kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}')`
  - _port_: `(kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')`

If no errors occured - the application has to be running on **ip**:_port_.

 > For more details visit.

## Monitoring

### Prometheus

* Install the Prometheus addon `kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.7/samples/addons/prometheus.yaml`.
* Verify it is running `kubectl get svc prometheus -n istio-system`.

To access the Prometheus dashboard run the following command:

`kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=prometheus -o jsonpath='{.items[0].metadata.name}') 9090:9090`

and go to localhost:9090.

### Grafana

* Install the Grafana addon `kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.7/samples/addons/grafana.yaml`.
* Verify it is running `kubectl get svc grafana -n istio-system`.


To access the Grafana dashboard run the following command:

`kubectl -n istio-system port-forward $(kubectl -n istio-system get pod -l app=grafana -o jsonpath='{.items[0].metadata.name}') 3000:3000`

and go to localhost:3000.

### Jaeger


* Install the Jaeger addon `kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.7/samples/addons/jaeger.yaml`.
* Verify it is running `kubectl get svc jaeger -n istio-system`.


To access the Jaeger dashboard run the following command:

`kubectl port-forward -n istio-system $(kubectl get pod -n istio-system -l app=jaeger -o jsonpath='{.items[0].metadata.name}') 16686:16686`

and go to localhost:16686.

### Kiali


* Install the Kiali addon `kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.7/samples/addons/kiali.yaml`.
* Verify it is running `kubectl get svc kiali -n istio-system`.


To access the Kiali dashboard run the following command:

`kubectl port-forward -n istio-system $(kubectl get pod -n istio-system -l app=kiali -o jsonpath='{.items[0].metadata.name}') 20001:20001`

and go to localhost:20001.


## Extend the application

In our case, extending the testbed can be delivered easily thanks to Istio. Follow the next steps:
1. Copy one of the existing services.
    - If desired, edit it in the preferred IDE -_can be skipped_.
2. Change its main path in the **controller class** (e.g., change "/cart" to "/cart1" in the CartController class for the cart service).
3. Create a new Docker image.
4. Add another deployment file of the chosen service (but with a different naming, e.g., "cart1" instead of "cart" for the cart service, and the created in step 4 Docker image).
5. Extend the istio-gateway file accordingly with a new entry for the virtual service under **http**. Following is an example for an added cart service with "/cart1", named "cart1" and 8888 as a port: 
```
- match:
    - uri:
        prefix: "/cart1"
    route:
    - destination:
        host: cart1
        port:
          number: 8888
 ```  

       
> Note that the new port number is arbitrary but should not collide with the existing services. For that check the ports in the gateway deployment file.



  


