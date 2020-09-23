# BookStore - a microservice testbed


### Overall information

The application has the following services:

* Catalogue Management,
* User Management,
* Employee Management,
* Cart Management,
* Order Management.

Each service has its own [MongoDB](https://www.mongodb.com/de) database instance. The asynchromous communication between the services is implemented with help of [RabbitMQ](https://www.rabbitmq.com/).

To get the **OpenAPI 3.0** specification run the following command, where _name_ is the pod's name of the desired service:

`kubectl exec _name_ -- curl localhost:7777/v3/api-docs > api.txt`.

To see it in the Swagger UI run `kubectl exec _name_ -- curl localhost:7777/swagger-ui.html`.

> The pod's name can be retrieved with `kubectl get pods`.

## Requirements

- A Google Cloud account,
- The [gcloud-command tool](https://cloud.google.com/sdk/docs/quickstart),
- [istioctl-command tool](https://istio.io/latest/docs/setup/getting-started/#download).

## Installation

> The application can be run either in a local Kubernetes cluster, or in a cluster in the cloud. The recommended choice is using the [GKE cloud](https://cloud.google.com/kubernetes-engine). Therefore, the whole setup is under the assumption that the cloud option has been chosen.

0. First, run `gcloud auth login` in the command line. Set the zone to the desired one, e.g., to us-west1-a: `gcloud config set compute/zone us-west1-a`.
1. Create a project `gcloud projects create bookstore12312434`.
   - if the project is not created, keep changing the name until you find an available one,
   - visit https://console.cloud.google.com/projectselector/kubernetes to enable billing in your projext.
2. Set the configured project as your default `gcloud config set project bookstore12312434`.
3. Install the kubectl-command tool for your project `gcloud components install kubectl`.
4. Create a cluster with Istio enabled: 

  ```
  gcloud beta container clusters create bookstore-cluster 
    --addons=Istio --istio-config=auth=MTLS_PERMISSIVE 
    --machine-type=n1-standard-2 
    --num-nodes=4
  ```
5. Verify that the cluster is running `gcloud container clusters list` and services istio-citadel, istio-egressgateway, istio-pilot, istio-ingressgateway, istio-policy, istio-sidecar-injector, and istio-telemetry are deployed `kubectl get service -n istio-system`.
    - to get credentials for the cluster run `gcloud container clusters get-credentials bookstore-cluster`.
6. Grant cluster admin permissions to the current user:

`kubectl create clusterrolebinding cluster-admin-binding 
    --clusterrole=cluster-admin 
    --user=$(gcloud config get-value core/account)`
    
7. Enable the sidecar injection for the cluster `kubectl label namespace default istio-injection=enabled`.
8. Deploy the desired application configuration. For instance, to deploy version 1:

```
kubectl apply -f deployment-v1.yaml
kubectl apply -f rabbitmq.yaml
```

>All configurations used in the thesis are available as as-is files. To construct your own configuration, the deployment files of individual services are available in the folders deployment-files-xx (where xx is the version of the services in the folder). Keep in mind, that if you want to have both versions of one service running (the mixed folder), you need to deploy not only its deployments and service, but also the appropriate destination rule which is located in deployment-files-mixed/destination rules.

9. Ensure all the services are running `kubectl get pods` and deploy the gateway `kubectl apply -f gateway.yaml`.
10. To find out, on what ip the cluster is running, perform following commands: 

  - **ip**: `kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.status.loadBalancer.ingress[0].ip}'`
  - _port_: `kubectl -n istio-system get service istio-ingressgateway -o jsonpath='{.spec.ports[?(@.name==\"http2\")].port}'`

If no errors occured - the application has to be running on **ip**:_port_.

 > For more details visit https://cloud.google.com/istio/docs/istio-on-gke/installing.

## Monitoring

> None of the monitoring addons are pre-installed in the GKE cluster. The desired ones need to be manually added. Besides, some might find installing every addon overwhelming, and therefore, are able to decide which ones they really require.


To enable all addons at once, run `istioctl manifest apply --set profile=demo`. If any errors occur, you can ignore them (not relevant for this project).

Run following commands to open a responsive dashboard:

* Prometheus: `istioctl dashboard prometheus`,
* Grafana: `istioctl dashboard grafana`,
* Kiali: `istioctl dashboard kiali`,
* Jaeger: `istioctl dashboard jaeger`.


## Extending the application

In our case, extending the testbed can be delivered easily thanks to Istio. In addition to simply increasing the number of instances of one service with `kubectl scale --replicas=3 -f foo`, one can also quickly add another service. Follow the next steps:
1. Copy one of the existing services.
    - If desired, edit it in the preferred IDE - _can be skipped_.
2. Change its main path in the **controller class** (e.g., change "/cart" to "/cart1" in the CartController class for the cart service).
3. Create a new Docker image.
4. Copy the deployment file of the chosen service and edit it with the next changes:
    - a different name, app and value for the enviromental variable "VERSION", e.g., "cart1" instead of "cart" for the cart service,
    - change the image to the created in _step 3_ Docker image,
    - change "port" in the Service configuration to an arbitrary one.

> Note that the new port number is arbitrary but should not collide with the existing services. For that check the ports in the deployment files in the respective Service configuration sections.
  
5. Extend the gateway.yaml file accordingly with a new entry for the virtual service under **http**. Following is an example for a changed cart service with "/cart1" and named "cart1": 
```
- match:
    - uri:
        prefix: "/cart1"
    route:
    - destination:
        host: cart1
 ```  

Author: Anastasiia Sivirina, Technische Universität Berlin.
       




  



